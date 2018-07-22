package com.app.my.helpcatapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class AdminResetPasswordApprovalActivity extends AppCompatActivity {
    private TextView approvalResetTextView;
    private ListView changePasswordListView;

    private ArrayAdapter<ResetPasswordSubmission> resetListAdapter;

    private ArrayList<ResetPasswordSubmission> resetList = new ArrayList<>();

    private FirebaseDatabase firebase;

    private ValueEventListener resetPostEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reset_password_approval);

        approvalResetTextView = (TextView) findViewById(R.id.resetPasswordMessage_txt);
        changePasswordListView = (ListView) findViewById(R.id.resetPassList);

        firebase = FirebaseDatabase.getInstance();

        resetListAdapter = new CustomResetList(this, R.layout.reset_password_list_item, resetList, new CustomClickListener() {
            @Override
            public void onClick(int position) {
                String loginID = resetList.get(position).getID();
                String newPassword = "";

                if(loginID.charAt(0) == 'C') {
                    newPassword = "Helpcat" + loginID.substring(loginID.length() - 3, loginID.length());
                } else if(loginID.charAt(0) == 'I') {
                    //if lecturer
                    newPassword = "HelpcatL" + loginID.substring(loginID.length() - 4, loginID.length());
                }

                firebase.getReference("users").child(loginID).child("password").setValue(newPassword);
                firebase.getReference("reset_password_approval").child(loginID).removeValue();
            }
        }, new CustomClickListener() {
            @Override
            public void onClick(int position) {
                String loginID = resetList.get(position).getID();
                firebase.getReference("reset_password_approval").child(loginID).removeValue();
            }
        });
        changePasswordListView.setAdapter(resetListAdapter);

        resetPostEventListener = firebase.getReference("reset_password_approval").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resetList.clear();
                approvalResetTextView.setVisibility(View.GONE);
                changePasswordListView.setVisibility(View.GONE);

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ResetPasswordSubmission post = postSnapshot.getValue(ResetPasswordSubmission.class);

                    resetList.add(post);
                }

                if(resetList.size() >= 1) {
                    changePasswordListView.setVisibility(View.VISIBLE);
                } else {
                    approvalResetTextView.setVisibility(View.VISIBLE);
                }

                resetListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error : " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        firebase.getReference("reset_password_approval").removeEventListener(resetPostEventListener);
        super.onDestroy();
    }

    public class CustomResetList extends ArrayAdapter<ResetPasswordSubmission> {
        private ArrayList<ResetPasswordSubmission> resetList;
        private int layout;

        private CustomClickListener acceptClickListener, denyClickListener;

        public CustomResetList(Context context, int listLayout, ArrayList<ResetPasswordSubmission> data, CustomClickListener acceptClickListener, CustomClickListener denyClickListener) {
            super(context, listLayout, data);
            this.layout = listLayout;
            this.resetList = data;
            this.acceptClickListener = acceptClickListener;
            this.denyClickListener = denyClickListener;
        }

        @Override
        public ResetPasswordSubmission getItem(int position) {
            return resetList.get(position);
        }

        public int getLayout() {
            return this.layout;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // Get the data item for this position
            String loginid = (String) resetList.get(position).getID();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(this.layout, parent, false);
            TextView loginid_txt = (TextView) view.findViewById(R.id.loginID_txt);
            loginid_txt.setText(loginid);

            Button acceptBtn = (Button) view.findViewById(R.id.accptBtn);
            acceptBtn.setTag(position);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(acceptClickListener != null)
                        acceptClickListener.onClick((Integer) v.getTag());
                }
            });

            Button denyBtn = (Button) view.findViewById(R.id.denyBtn);
            denyBtn.setTag(position);
            denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(denyClickListener != null)
                        denyClickListener.onClick((Integer) v.getTag());
                }
            });

            // Return the completed view to render on screen
            return view;
        }
    }
}