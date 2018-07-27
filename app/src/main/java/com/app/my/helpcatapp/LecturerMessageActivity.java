package com.app.my.helpcatapp;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.*;

public class LecturerMessageActivity extends AppCompatActivity {
    private ArrayList<CourseSubject> classList = new ArrayList<>();
    private ArrayList<CourseSubject> selectedList = new ArrayList<>();

    private ListView classesListView;
    private EditText messageContentEditText;
    private Button submitMessageBtn;

    private CustomClassesList classesAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_message);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // initialize classes list
        classesListView = (ListView) findViewById(R.id.message_class_listView);
        classesAdapter = new CustomClassesList(this, R.layout.message_checkbox_item, classList);
        classesListView.setAdapter(classesAdapter);

        // retrieve subjects tutor by the lecturer
        databaseReference.child("subjects").orderByChild("lecturerID").equalTo(preferences.getString("KEY_ID", null)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot subListSnapshot) {
                for(DataSnapshot subjectSnapshot : subListSnapshot.getChildren()) {
                    classList.add(subjectSnapshot.getValue(CourseSubject.class));
                }
                classesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

        // initialize message text field
        messageContentEditText = (EditText) findViewById(R.id.messageContent_editText);

        // initialize submit button
        submitMessageBtn = (Button) findViewById(R.id.messageSubmit_btn);
        submitMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedList.size() >= 1) {
                    String messageContent = messageContentEditText.getText().toString().trim().replaceAll("\n", "");
                    if(messageContent.length() > 0) {
                        for(Iterator<CourseSubject> it = selectedList.iterator(); it.hasNext(); ) {
                            CourseSubject currentSubject = it.next();

                            Message currentMessage = new Message(messageContent, Calendar.getInstance(Locale.ENGLISH).getTimeInMillis(), new Lecturer(preferences.getString("KEY_ID", null), preferences.getString("KEY_NAME", null)), currentSubject);

                            databaseReference.child("messages").push().setValue(currentMessage);
                        }

                        Toast.makeText(getApplicationContext(), "Message submitted successfully", Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter your message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please choose your class", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class CustomClassesList extends ArrayAdapter<CourseSubject> {
        private ArrayList<CourseSubject> classList;
        private int layout;

        public CustomClassesList(Context context, int listLayout, ArrayList<CourseSubject> data) {
            super(context, listLayout, data);
            this.layout = listLayout;
            this.classList = data;
        }

        @Override
        public CourseSubject getItem(int position) {
            return classList.get(position);
        }

        public int getLayout() {
            return this.layout;
        }

        private class ViewHolder {
            protected CheckBox checkBox;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder viewHolder;

            if(view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(this.layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.checkBox = (CheckBox) view.findViewById(R.id.class_cbx);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final CourseSubject subject = getItem(position);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()) {
                        selectedList.add(subject);
                    } else {
                        selectedList.remove(subject);
                    }
                }
            });
            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setText(subject.toString());
            viewHolder.checkBox.setChecked(selectedList.contains(subject));

            return view;
        }
    }
}