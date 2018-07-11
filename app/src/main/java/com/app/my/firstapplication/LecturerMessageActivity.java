package com.app.my.firstapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.*;

public class LecturerMessageActivity extends AppCompatActivity {
    private ArrayList<CourseSubject> classList = new ArrayList<>();

    private ListView classesListView;
    private EditText messageContentEditText;
    private Button submitMessageBtn;

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

        classesListView = (ListView) findViewById(R.id.message_class_listView);

        final CustomClassesList classesAdapter = new CustomClassesList(this, R.layout.message_checkbox_item, classList);
        classesListView.setAdapter(classesAdapter);

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

        messageContentEditText = (EditText) findViewById(R.id.messageContent_editText);

        submitMessageBtn = (Button) findViewById(R.id.messageSubmit_btn);
        submitMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CourseSubject> selectedList = classesAdapter.getSelectedClasses();

                if(selectedList.size() >= 1) {
                    String messageContent = messageContentEditText.getText().toString().trim().replaceAll("\n", "");

                    for(Iterator<CourseSubject> it = selectedList.iterator(); it.hasNext(); ) {
                        CourseSubject currentSubject = it.next();

                        Message currentMessage = new Message(messageContent, Calendar.getInstance(Locale.ENGLISH).getTimeInMillis(), new Lecturer(preferences.getString("KEY_ID", null), preferences.getString("KEY_NAME", null)), currentSubject);

                        databaseReference.child("messages").push().setValue(currentMessage);
                    }

                    Toast.makeText(getApplicationContext(), "Message submitted successfully", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(getApplicationContext(), "Please choose your class", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class CustomClassesList extends ArrayAdapter<CourseSubject> {
        private ArrayList<CourseSubject> classList;
        private ArrayList<CourseSubject> selectedClasses = new ArrayList<>();
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

        public ArrayList<CourseSubject> getSelectedClasses(){
            return this.selectedClasses;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            // Get the data item for this position
            String classTxt = (String) getItem(position).toString();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(this.layout, parent, false);
            CheckBox classCheckBox = (CheckBox) view.findViewById(R.id.class_cbx);
            classCheckBox.setText(classTxt);

            classCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        selectedClasses.add(getItem(position));
                    } else {
                        selectedClasses.remove(getItem(position));
                    }
                }
            });

            // Return the completed view to render on screen
            return view;
        }
    }
}