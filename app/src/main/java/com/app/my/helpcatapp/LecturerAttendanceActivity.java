package com.app.my.helpcatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LecturerAttendanceActivity extends AppCompatActivity {
    private ExpandableListView classListView;
    private TextView startClassName_textView;
    private TextView startClass_textView;
    private Button attendanceButton;

    private int lastExpandedPosition = -1;

    private CustomClassesExpandableListAdapter classListAdapter;

    private ArrayList<ExtendedCourseSubject> classList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private SharedPreferences preferences;

    private ExtendedCourseSubject currentClass = null;

    private boolean isClassStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_attendance);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // initialize list components
        classList = new ArrayList<>();
        classListView = (ExpandableListView) findViewById(R.id.lecturerClass_list);
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

        // initialize upcoming / starting class components
        startClassName_textView = (TextView) findViewById(R.id.startClassName_txt);
        startClass_textView = (TextView) findViewById(R.id.startClass_txt);

        // retrieve subjects tutor by the lecturer
        databaseReference.child("subjects").orderByChild("lecturerID").equalTo(preferences.getString("KEY_ID", null)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot subjectListSnapshot) {
                // get current day of week by day name - eg. Monday
                String dayOfTheWeek = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
                long countDownMilli = 0;

                // iterate through the subject snapshot
                for(DataSnapshot subjectSnapshot : subjectListSnapshot.getChildren()) {
                    // get value of the current cursor object and set to ExtendedCourseSubject object
                    final ExtendedCourseSubject subject = subjectSnapshot.getValue(ExtendedCourseSubject.class);
                    classList.add(subject);

                    // iterate through class time
                    for (Iterator<SubjectClass> it = subject.getClassTime().iterator(); it.hasNext(); ) {
                        SubjectClass subjectClass = it.next();

                        // if the current class day is same as current day
                        if(subjectClass.getDay().equals(dayOfTheWeek)) {
                            Calendar currentTime = Calendar.getInstance(Locale.ENGLISH);
                            Calendar startTime = Calendar.getInstance(Locale.ENGLISH);
                            Calendar endTime = Calendar.getInstance(Locale.ENGLISH);
                            SimpleDateFormat sdf = new SimpleDateFormat("hh.mmaa", Locale.ENGLISH);

                            try {
                                // parse class start time to Date object
                                Date startParse = sdf.parse(subjectClass.getStartTime());
                                startTime.setTimeInMillis(startParse.getTime());
                                startTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));

                                // if current time is after class start time
                                if(currentTime.after(startTime)) {
                                    // parse class end time to Date object
                                    Date endParse = sdf.parse(subjectClass.getEndTime());
                                    endTime.setTimeInMillis(endParse.getTime());
                                    endTime.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));

                                    // and if current time is before class end time
                                    if(currentTime.before(endTime)) {
                                        currentClass = subject;
                                        isClassStarted = true;
                                    }
                                } else if(currentTime.before(startTime)) { // if current time is earlier than class start time
                                    // set countdown time value until class starts
                                    Long tempCountDownMilli = startTime.getTimeInMillis() - Calendar.getInstance(Locale.ENGLISH).getTimeInMillis();

                                    if (currentClass == null) {
                                        currentClass = subject;
                                        countDownMilli = tempCountDownMilli;
                                    } else {
                                        if (tempCountDownMilli < countDownMilli) {
                                            currentClass = subject;
                                            countDownMilli = tempCountDownMilli;
                                        }
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                classListAdapter.notifyDataSetChanged();

                // if class is started
                if(isClassStarted) {
                    // set class is started message and enable 'Start Class' button
                    startClassName_textView.setText(currentClass.toString());
                    startClass_textView.setText("Class is started");
                    attendanceButton.setClickable(true);
                    attendanceButton.setEnabled(true);
                    attendanceButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if(countDownMilli > 0) { // if countdown time value is more than 0
                    // create a countdown timer
                    new CountDownTimer(countDownMilli, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // set class text message and display countdown timer
                            startClassName_textView.setText(currentClass.toString());
                            startClass_textView.setText(String.format("%02d Hours : %02d Minutes : %02d Seconds", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                        public void onFinish() {
                            // when countdown timer finishes, can start class
                            startClassName_textView.setText(currentClass.toString());
                            startClass_textView.setText("Class is started");
                            attendanceButton.setClickable(true);
                            attendanceButton.setEnabled(true);
                            attendanceButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                    }.start();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

        // initialize 'Start Class' button
        attendanceButton = (Button) findViewById(R.id.attendance_btn);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LecturerAttendanceActivity.this, ScannerActivity.class);

                // put a parameter to pass to ScannerActivity
                intent.putExtra("subjectID", currentClass.getSubjectCode());
                startActivity(intent);
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