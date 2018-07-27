package com.app.my.helpcatapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class AdminEnrollApprovalActivity extends AppCompatActivity {
    private TextView approvalMessageTextView;
    private ExpandableListView enrollmentListView;

    private CustomEnrollmentExpandableListAdapter enrollmentListAdapter;

    private ArrayList<SubjectEnrollment> enrollmentList = new ArrayList<>();

    private FirebaseDatabase firebase;

    private ValueEventListener postEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_enroll_approval);

        // initialize layout components
        approvalMessageTextView = (TextView) findViewById(R.id.approvalMsg_txt);
        enrollmentListView = (ExpandableListView) findViewById(R.id.subjectEnrollmentList);

        firebase = FirebaseDatabase.getInstance();

        // create a custom list adapter for display list of submitted subject enrollments
        enrollmentListAdapter = new CustomEnrollmentExpandableListAdapter(this, enrollmentList, new CustomClickListener() {
            // set click event for approve button
            @Override
            public void onClick(int position) {
                enrollmentListView.collapseGroup(position);

                String studentID = (String) enrollmentList.get(position).getStudentID();
                ArrayList<CourseSubject> enrolledSubjects = (ArrayList<CourseSubject>) enrollmentList.get(position).getSubjectList();

                // set enrollment status to 200 - indicates accepted
                firebase.getReference("add_sub_approval").child(studentID).child("status").setValue(Integer.valueOf(200));

                // set subjects for student's enrolled subjects
                firebase.getReference("users").child(studentID).child("enrolledSubjects").setValue(enrolledSubjects);
            }
        }, new CustomClickListener() {
            // set click event for deny button
            @Override
            public void onClick(int position) {
                enrollmentListView.collapseGroup(position);

                String studentID = (String) enrollmentList.get(position).getStudentID();

                // set enrollment status to 400 - indicates denied
                firebase.getReference("add_sub_approval").child(studentID).child("status").setValue(Integer.valueOf(400));
            }
        });
        enrollmentListView.setAdapter(enrollmentListAdapter);

        // retrieve list of enrollments from database which status = 100
        postEventListener = firebase.getReference("add_sub_approval").orderByChild("status").equalTo(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enrollmentList.clear();
                approvalMessageTextView.setVisibility(View.GONE);
                enrollmentListView.setVisibility(View.GONE);

                // add enrollment to list for which submitted = true
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    SubjectEnrollment post = postSnapshot.getValue(SubjectEnrollment.class);

                    if(post.isSubmitted()) {
                        enrollmentList.add(post);
                    }
                }

                if(enrollmentList.size() >= 1) {
                    enrollmentListView.setVisibility(View.VISIBLE);
                } else {
                    approvalMessageTextView.setVisibility(View.VISIBLE);
                }

                enrollmentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error : " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        firebase.getReference("add_sub_approval").removeEventListener(postEventListener);
        super.onDestroy();
    }

    public class CustomEnrollmentExpandableListAdapter extends BaseExpandableListAdapter
    {
        private Context context;
        private ArrayList<SubjectEnrollment> enrollmentList;

        private CustomClickListener approveClickListener, denyClickListener;

        public CustomEnrollmentExpandableListAdapter(Context context, ArrayList<SubjectEnrollment> enrollmentData, CustomClickListener approveClickListener, CustomClickListener denyClickListener) {
            super();
            this.context = context;
            this.enrollmentList = enrollmentData;
            this.approveClickListener = approveClickListener;
            this.denyClickListener = denyClickListener;
        }

        @Override
        public int getGroupCount() {
            return enrollmentList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return enrollmentList.get(groupPosition).numberOfSubject();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return enrollmentList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return enrollmentList.get(groupPosition).getSubjectList().get(childPosition);
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
            SubjectEnrollment enrollment = (SubjectEnrollment) getGroup(groupPosition);

            convertView = LayoutInflater.from(context).inflate(R.layout.enroll_approval_list_group,null);

            TextView studentID_textView = (TextView) convertView.findViewById(R.id.studentid_txt);
            studentID_textView.setText(enrollment.getStudentID());

            TextView subjectNum_textView = (TextView) convertView.findViewById(R.id.subjectNum_txt);
            subjectNum_textView.setText(String.valueOf(enrollment.numberOfSubject()));

            Button approve_button = (Button) convertView.findViewById(R.id.approveEnrollBtn);
            approve_button.setFocusable(false);
            approve_button.setTag(groupPosition);
            approve_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(approveClickListener != null) {
                        approveClickListener.onClick((Integer) v.getTag());
                    }
                }
            });

            Button deny_button = (Button) convertView.findViewById(R.id.denyEnrollBtn);
            deny_button.setFocusable(false);
            deny_button.setTag(groupPosition);
            deny_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(denyClickListener != null) {
                        denyClickListener.onClick((Integer) v.getTag());
                    }
                }
            });

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            CourseSubject subject = (CourseSubject) getChild(groupPosition, childPosition);

            convertView = LayoutInflater.from(context).inflate(R.layout.enroll_approval_list_item, null);

            TextView subjectName_textView = (TextView) convertView.findViewById(R.id.subjectName_txt);
            subjectName_textView.setText(subject.toString());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}