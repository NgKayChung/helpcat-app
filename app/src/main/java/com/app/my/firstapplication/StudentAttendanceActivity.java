package com.app.my.firstapplication;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.*;

public class StudentAttendanceActivity extends AppCompatActivity {
    private ExpandableListView classListView;
    private TextView startClassName_textView;
    private TextView startClass_textView;
    private Button attendClass_button;

    private int lastExpandedPosition = -1;

    private CustomClassesExpandableListAdapter classListAdapter;

    private ArrayList<ExtendedCourseSubject> classList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        classList = new ArrayList<>();
        classListView = (ExpandableListView) findViewById(R.id.studentClass_list);
        classListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    classListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        classListAdapter = new CustomClassesExpandableListAdapter(this, classList);
        classListView.setAdapter(classListAdapter);

        startClassName_textView = (TextView) findViewById(R.id.startClassName_txt);
        startClass_textView = (TextView) findViewById(R.id.startClass_txt);

        databaseReference.child("users").child(preferences.getString("KEY_ID", null)).child("enrolledSubjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot enrolledListSnapshot) {
                if(enrolledListSnapshot.exists()) {
                    for(DataSnapshot subjectSnapshot : enrolledListSnapshot.getChildren()) {
                        ExtendedCourseSubject enrolledSubject = subjectSnapshot.getValue(ExtendedCourseSubject.class);
                        String subjectCode = enrolledSubject.getSubjectCode();

                        databaseReference.child("subjects").orderByChild("subjectCode").equalTo(subjectCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot subjectSnapshot) {
                                ExtendedCourseSubject subject = subjectSnapshot.getChildren().iterator().next().getValue(ExtendedCourseSubject.class);
                                classList.add(subject);
                                classListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled (DatabaseError databaseError){

                            }
                        });
                    }
                } else {
                    Log.d("E", "Please proceed to enroll subject");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        attendClass_button = (Button)findViewById(R.id.attendClass_btn);
        attendClass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentAttendanceActivity.this, QRGenerateActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    public class CustomClassesExpandableListAdapter extends BaseExpandableListAdapter
    {
        private Context context;
        private ArrayList<ExtendedCourseSubject> subjectsList;

        public CustomClassesExpandableListAdapter(Context context, ArrayList<ExtendedCourseSubject> subjectsData) {
            super();
            this.context = context;
            this.subjectsList = subjectsData;
        }

        @Override
        public int getGroupCount() {
            return subjectsList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return subjectsList.get(groupPosition).numberOfClasses();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return subjectsList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return subjectsList.get(groupPosition).getClass(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
            ExtendedCourseSubject subject = (ExtendedCourseSubject) getGroup(groupPosition);

            convertView = LayoutInflater.from(context).inflate(R.layout.class_list_group,null);

            TextView className_textView = (TextView) convertView.findViewById(R.id.className_txt);
            className_textView.setText(subject.toString());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            SubjectClass subjectClass = (SubjectClass) getChild(groupPosition, childPosition);

            convertView = LayoutInflater.from(context).inflate(R.layout.class_list_item, parent, false);

            TextView dateTime_textView = (TextView) convertView.findViewById(R.id.class_dateTime_txt);
            dateTime_textView.setText(subjectClass.getDateTime());

            TextView venue_textView = (TextView) convertView.findViewById(R.id.venue_txt);
            venue_textView.setText("Venue: " + subjectClass.getVenue());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}

/*

 */