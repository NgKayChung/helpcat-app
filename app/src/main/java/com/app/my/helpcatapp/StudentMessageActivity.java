package com.app.my.helpcatapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class StudentMessageActivity extends AppCompatActivity {
    private TextView messageTextView;
    private RecyclerView messageRecyclerView;
    private CustomRecyclerViewAdapter recyclerViewAdapter;

    private ArrayList<CourseSubject> enrolledSubjectList;

    private ArrayList<Message> messageList = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ValueEventListener messageEventListener;

    private SharedPreferences preferences;

    private GenericTypeIndicator<ArrayList<CourseSubject>> subjectListGenericTypeIndicator = new GenericTypeIndicator<ArrayList<CourseSubject>>(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_message);

        // initialize layout components
        messageTextView = (TextView) findViewById(R.id.message_txt);
        messageRecyclerView = (RecyclerView) findViewById(R.id.message_recyclerView);
        messageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        messageRecyclerView.setLayoutManager(llm);

        // set adapter to the message list
        recyclerViewAdapter = new CustomRecyclerViewAdapter(messageList);
        messageRecyclerView.setAdapter(recyclerViewAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // retrieve student's enrolled subjects from database
        databaseReference.child("users").child(preferences.getString("KEY_ID", null)).child("enrolledSubjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot subjectSnapshot) {
                if(subjectSnapshot.exists()) {
                    enrolledSubjectList = subjectSnapshot.getValue(subjectListGenericTypeIndicator);

                    // retrieve messages list from database
                    messageEventListener = databaseReference.child("messages").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot messageListSnapshot) {
                            messageTextView.setVisibility(View.GONE);
                            messageRecyclerView.setVisibility(View.GONE);
                            messageList.clear();

                            // loop through all messages retrieved
                            for(DataSnapshot messageSnapshot : messageListSnapshot.getChildren()) {
                                Message currentMessage = messageSnapshot.getValue(Message.class);

                                // loop through student's enrolled subjects list
                                // to add message to the list for the subjects that student enrolled
                                for(Iterator<CourseSubject> subjectIt = enrolledSubjectList.iterator(); subjectIt.hasNext(); ) {
                                    CourseSubject currentSubject = subjectIt.next();

                                    // if the subject same as the subject for the message
                                    if(currentMessage.getSubject().equals(currentSubject)) {
                                        messageList.add(0, currentMessage);
                                        break;
                                    }
                                }

                                // to display only 10 latest messages
                                if(messageList.size() >= 10) {
                                    break;
                                }
                            }
                            recyclerViewAdapter.notifyDataSetChanged();

                            // if message is available, display message list
                            if(messageList.size() >= 1) {
                                messageRecyclerView.setVisibility(View.VISIBLE);
                            } else { // display no message
                                messageTextView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else { // if no enrolled subjects
                    messageTextView.setText("Please enroll subject to view message");
                    messageTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        if(messageEventListener != null) databaseReference.child("message").removeEventListener(messageEventListener);
        super.onDestroy();
    }

    public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MessageViewHolder> {
        private ArrayList<Message> messageList;

        public CustomRecyclerViewAdapter(ArrayList<Message> messageList) {
            this.messageList = messageList;
        }

        public class MessageViewHolder extends RecyclerView.ViewHolder {
            protected CardView messageCardView;
            protected TextView lecturerName_txtView;
            protected TextView subject_txtView;
            protected TextView messageContent_txtView;
            protected TextView postTime_txtView;

            public MessageViewHolder(View itemView) {
                super(itemView);
                messageCardView = (CardView) itemView.findViewById(R.id.message_cardView);
                lecturerName_txtView = (TextView) itemView.findViewById(R.id.lecturer_name_txtView);
                subject_txtView = (TextView) itemView.findViewById(R.id.subject_txtView);
                messageContent_txtView = (TextView) itemView.findViewById(R.id.message_content_txtView);
                postTime_txtView = (TextView) itemView.findViewById(R.id.postTime_txtView);
            }
        }

        @Override
        public void onBindViewHolder(MessageViewHolder messageViewHolder, int position) {
            Message message = messageList.get(position);

            messageViewHolder.lecturerName_txtView.setText(message.getLecturer().getLecturerName());
            messageViewHolder.subject_txtView.setText(message.getSubject().toString());
            messageViewHolder.messageContent_txtView.setText(message.getContent());

            Date messageTime = new Date(message.getPostTimeMillis());

            long timeDiff = Calendar.getInstance(Locale.ENGLISH).getTimeInMillis() - messageTime.getTime();

            long diffDay = timeDiff / (24 * 60 * 60 * 1000);
            long diffHours = timeDiff / (60 * 60 * 1000);
            long diffMinutes = timeDiff / (60 * 1000) % 60;

            String timeString = "";

            if(diffDay >= 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
                timeString = sdf.format(messageTime);
            } else {
                if(diffHours >= 1) {
                    timeString = diffHours + "h ago";
                } else {
                    if(diffMinutes >= 1) {
                        timeString = diffMinutes + "m ago";
                    } else {
                        timeString = "Just now";
                    }
                }
            }

            messageViewHolder.postTime_txtView.setText(timeString);
        }

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_card_view, viewGroup, false);
            MessageViewHolder messageViewHolder = new MessageViewHolder(v);
            return messageViewHolder;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }
    }
}