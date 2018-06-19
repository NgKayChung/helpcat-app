package com.app.my.firstapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class EnrollApprovalActivity extends AppCompatActivity {
    private ExpandableListView enrollmentListView;

    private CustomEnrollmentExpandableListAdapter enrollmentListAdapter;

    private ArrayList<SubjectEnrollment> enrollmentList = new ArrayList<>();

    private FirebaseDatabase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_approval);

        enrollmentListView = (ExpandableListView) findViewById(R.id.subjectEnrollmentList);

        firebase = FirebaseDatabase.getInstance();

        enrollmentListAdapter = new CustomEnrollmentExpandableListAdapter(this, enrollmentList, new CustomClickListener() {
            @Override
            public void onClick(int position) {
                String studentID = (String) enrollmentList.get(position).getStudentID();
                ArrayList<CourseSubject> enrolledSubjects = (ArrayList<CourseSubject>) enrollmentList.get(position).getSubjectList();

                firebase.getReference("add_sub_approval").child(studentID).child("status").setValue(Integer.valueOf(200));
                firebase.getReference("users").child(studentID).child("enrolledSubjects").setValue(enrolledSubjects);
            }
        }, new CustomClickListener() {
            @Override
            public void onClick(int position) {
                String studentID = (String) enrollmentList.get(position).getStudentID();
                firebase.getReference("add_sub_approval").child(studentID).child("status").setValue(Integer.valueOf(400));
            }
        });
        enrollmentListView.setAdapter(enrollmentListAdapter);

        firebase.getReference("add_sub_approval").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enrollmentList.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    SubjectEnrollment post = postSnapshot.getValue(SubjectEnrollment.class);

                    if(post.getStatus() == 100 && post.isSubmitted()) {
                        enrollmentList.add(post);
                    }
                }

                enrollmentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error : " + databaseError.getMessage());
            }
        });
    }

    public class CustomEnrollmentExpandableListAdapter extends BaseExpandableListAdapter
    {
        private Context context;
        private ArrayList<SubjectEnrollment> subjectEnrollmentList;

        private CustomClickListener approveClickListener, denyClickListener;

        public CustomEnrollmentExpandableListAdapter(Context context, ArrayList<SubjectEnrollment> data, CustomClickListener approveClickListener, CustomClickListener denyClickListener) {
            super();
            this.context = context;
            this.subjectEnrollmentList = data;
            this.approveClickListener = approveClickListener;
            this.denyClickListener = denyClickListener;
        }

        @Override
        public int getGroupCount() {
            return subjectEnrollmentList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return subjectEnrollmentList.get(groupPosition).numberOfSubject();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return subjectEnrollmentList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return subjectEnrollmentList.get(groupPosition).getSubjectList().get(childPosition);
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
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            SubjectEnrollment enrollment = (SubjectEnrollment) getGroup(groupPosition);

            convertView = LayoutInflater.from(context).inflate(R.layout.list_group,null);

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
                    if(approveClickListener != null)
                        approveClickListener.onClick((Integer) v.getTag());
                }
            });

            Button deny_button = (Button) convertView.findViewById(R.id.denyEnrollBtn);
            deny_button.setFocusable(false);
            deny_button.setTag(groupPosition);
            deny_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(denyClickListener != null)
                        denyClickListener.onClick((Integer) v.getTag());
                }
            });

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            CourseSubject subject = (CourseSubject) getChild(groupPosition, childPosition);

            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);

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