package com.app.my.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;

public class RegisterNewStudentActivity extends AppCompatActivity {
    private EditText studentName_textInput, nric_passport_textInput, emailAddress_textInput, phoneNumber_textInput, livingAddress_textInput;
    private TextView studentName_errField, nric_passport_errField, emailAddress_errField, phoneNumber_errField, gender_errField, livingAddress_errField, nationality_errField, programme_errField, intakeMonth_errField, intakeYear_errField;
    private Spinner genderSpinner, nationalitySpinner, programmeSpinner, intakeMonthSpinner, intakeYearSpinner;
    private ArrayList<String> genderList, nationalityList, programmeList, intakeMonthList, intakeYearList;
    private ArrayAdapter<String> genderSpinnerAdapter, nationalitySpinnerAdapter, programmeSpinnerAdapter, intakeMonthSpinnerAdapter, intakeYearSpinnerAdapter;
    private Button registerBtn;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_student);

        firebaseDatabase = FirebaseDatabase.getInstance();

        studentName_textInput = (EditText) findViewById(R.id.studentName_txt);
        nric_passport_textInput = (EditText) findViewById(R.id.nric_passport_txt);
        emailAddress_textInput = (EditText) findViewById(R.id.emailAddress_txt);
        phoneNumber_textInput = (EditText) findViewById(R.id.phoneNumber_txt);
        livingAddress_textInput = (EditText) findViewById(R.id.livingAddress_txt);

        studentName_errField = (TextView) findViewById(R.id.studentName_err);
        nric_passport_errField = (TextView) findViewById(R.id.nric_passport_err);
        emailAddress_errField = (TextView) findViewById(R.id.emailAddress_err);
        phoneNumber_errField = (TextView) findViewById(R.id.phoneNumber_err);
        gender_errField = (TextView) findViewById(R.id.gender_err);
        livingAddress_errField = (TextView) findViewById(R.id.livingAddress_err);
        nationality_errField = (TextView) findViewById(R.id.nationality_err);
        programme_errField = (TextView) findViewById(R.id.programme_err);
        intakeMonth_errField = (TextView) findViewById(R.id.intakeMonth_err);
        intakeYear_errField = (TextView) findViewById(R.id.intakeYear_err);

        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        nationalitySpinner = (Spinner) findViewById(R.id.nationality_spinner);
        programmeSpinner = (Spinner) findViewById(R.id.programme_spinner);
        intakeMonthSpinner = (Spinner) findViewById(R.id.intakeMonth_spinner);
        intakeYearSpinner = (Spinner) findViewById(R.id.intakeYear_spinner);

        genderList = new ArrayList<>();
        genderList.add("Select");
        genderList.add("MALE");
        genderList.add("FEMALE");
        genderSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, genderList);
        genderSpinner.setAdapter(genderSpinnerAdapter);

        nationalityList = new ArrayList<>();
        nationalityList.add("Select");
        nationalityList.add("Malaysia");
        nationalityList.add("Indonesia");
        nationalityList.add("Thailand");
        nationalityList.add("Laos");
        nationalityList.add("Pakistan");
        nationalityList.add("Bangladesh");
        nationalitySpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nationalityList);
        nationalitySpinner.setAdapter(nationalitySpinnerAdapter);

        programmeList = new ArrayList<>();
        programmeList.add("Select");
        programmeList.add("Diploma in Computer Science");
        programmeList.add("Diploma in Business Information System");
        programmeList.add("ARU Degree in Computer Science");
        programmeList.add("ARU Degree in Business Information System");
        programmeSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, programmeList);
        programmeSpinner.setAdapter(programmeSpinnerAdapter);

        programmeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intakeMonthList.clear();
                intakeMonthList.add("Select");
                intakeMonthSpinner.setSelection(0);

                if(programmeList.get(position).contains("ARU")) {
                    intakeMonthList.add("January");
                    intakeMonthList.add("July");
                    intakeMonthList.add("September");
                } else {
                    intakeMonthList.add("January");
                    intakeMonthList.add("April");
                    intakeMonthList.add("August");
                }

                intakeMonthSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        intakeMonthList = new ArrayList<>();
        intakeMonthList.add("Select");
        intakeMonthList.add("January");
        intakeMonthList.add("April");
        intakeMonthList.add("August");
        intakeMonthSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, intakeMonthList);
        intakeMonthSpinner.setAdapter(intakeMonthSpinnerAdapter);

        intakeYearList = new ArrayList<>();
        intakeYearList.add("Select");
        for(int year = Calendar.getInstance().get(Calendar.YEAR);year <= Calendar.getInstance().get(Calendar.YEAR) + 10;year++) {
            intakeYearList.add(String.valueOf(year));
        }
        intakeYearSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, intakeYearList);
        intakeYearSpinner.setAdapter(intakeYearSpinnerAdapter);

        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentName_errField.setText("");
                nric_passport_errField.setText("");
                emailAddress_errField.setText("");
                phoneNumber_errField.setText("");
                gender_errField.setText("");
                livingAddress_errField.setText("");
                nationality_errField.setText("");
                programme_errField.setText("");
                intakeMonth_errField.setText("");
                intakeYear_errField.setText("");

                if(validateFields()) {
                    registerNewStudent();
                }
            }
        });
    }

    public boolean validateFields() {
        String studentName = studentName_textInput.getText().toString().trim();
        String studentNricPassport = nric_passport_textInput.getText().toString().trim().replaceAll("-", "");
        String studentEmailAddress = emailAddress_textInput.getText().toString().trim();
        String studentPhoneNumber = phoneNumber_textInput.getText().toString().trim().replaceAll("-", "");
        int genderSelect = genderSpinner.getSelectedItemPosition();
        String studentLivingAddress = livingAddress_textInput.getText().toString().trim();
        int nationalitySelect = nationalitySpinner.getSelectedItemPosition();
        int enrollProgrammeSelect = programmeSpinner.getSelectedItemPosition();
        int intakeMonthSelect = intakeMonthSpinner.getSelectedItemPosition();
        int intakeYearSelect = intakeYearSpinner.getSelectedItemPosition();

        boolean success = true;

        if(studentName.isEmpty()) {
            studentName_errField.setText("Field is required");
            success = false;
        }

        if(studentNricPassport.isEmpty()) {
            nric_passport_errField.setText("Field is required");
            success = false;
        } else if(studentNricPassport.length() == 12 || studentNricPassport.length() == 14) {
            studentNricPassport = studentNricPassport.replaceAll("-", "");
            if(!studentNricPassport.matches("^[0-9]{2}(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\\d{2}\\d{4}$")) {
                nric_passport_errField.setText("Invalid NRIC Number");
                success = false;
            }
        } else if(studentNricPassport.length() == 9) {
            if(!studentNricPassport.matches("^[A][0-9]{8}$")) {
                nric_passport_errField.setText("Invalid Passport Number");
                success = false;
            }
        } else {
            nric_passport_errField.setText("Invalid NRIC / Passport Number");
            success = false;
        }

        if(studentEmailAddress.isEmpty()) {
            emailAddress_errField.setText("Field is required");
            success = false;
        } else if(!studentEmailAddress.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            emailAddress_errField.setText("Invalid email address");
            success = false;
        }

        studentPhoneNumber = studentPhoneNumber.replaceAll("-", "");

        if(studentPhoneNumber.isEmpty()) {
            phoneNumber_errField.setText("Field is required");
            success = false;
        } else if(studentPhoneNumber.length() == 10) {
            if(!studentPhoneNumber.matches("^[0][1](0|2|3|4|6|7|8|9)\\d{7}")) {
                phoneNumber_errField.setText("Invalid phone number");
                success = false;
            }
        } else if(studentPhoneNumber.length() == 11) {
            if(!studentPhoneNumber.matches("^[0][1][1]\\d{8}")) {
                phoneNumber_errField.setText("Invalid phone number");
                success = false;
            }
        } else {
            phoneNumber_errField.setText("Invalid phone number");
            success = false;
        }

        if(genderSelect == 0) {
            gender_errField.setText("Field is required");
            success = false;
        }

        if(studentLivingAddress.isEmpty()) {
            livingAddress_errField.setText("Field is required");
            success = false;
        }

        if(nationalitySelect == 0) {
            nationality_errField.setText("Field is required");
            success = false;
        }

        if(enrollProgrammeSelect == 0) {
            programme_errField.setText("Field is required");
            success = false;
        }

        if(intakeMonthSelect == 0) {
            intakeMonth_errField.setText("Field is required");
            success = false;
        }

        if(intakeYearSelect == 0) {
            intakeYear_errField.setText("Field is required");
            success = false;
        }

        return success;
    }

    public void registerNewStudent() {
        firebaseDatabase.getReference("users").orderByChild("ID").startAt("C").endAt("C~").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String studentID = "C";

                if(dataSnapshot.getChildrenCount() == 0) {
                    studentID += ((String.valueOf(Calendar.getInstance().get(Calendar.YEAR))).substring(2)) + "00001";
                } else {
                    String lastStudentID = dataSnapshot.getChildren().iterator().next().getKey();

                    if(lastStudentID.substring(1, 3).equals((String.valueOf(Calendar.getInstance().get(Calendar.YEAR))).substring(2))) {
                        studentID += String.valueOf((Long.valueOf(lastStudentID.substring(1)) + 1));
                    } else {
                        studentID += ((String.valueOf(Calendar.getInstance().get(Calendar.YEAR))).substring(2)) + "00001";
                    }
                }

                String studentPassword = "Helpcat" + studentID.substring(studentID.length() - 3, studentID.length());
                String studentName = studentName_textInput.getText().toString().trim();
                String studentNricPassport = nric_passport_textInput.getText().toString().trim().replaceAll("-", "");
                String studentEmailAddress = emailAddress_textInput.getText().toString().trim();
                String studentPhoneNumber = phoneNumber_textInput.getText().toString().trim().replaceAll("-", "");
                String studentGender = genderSpinner.getSelectedItem().toString();
                String studentLivingAddress = livingAddress_textInput.getText().toString().trim().replaceAll("\n", " ");
                String studentNationality = nationalitySpinner.getSelectedItem().toString();
                String studentEnrollProgramme = programmeSpinner.getSelectedItem().toString();
                String studentIntakeMonth = intakeMonthSpinner.getSelectedItem().toString();
                String studentIntakeYear = intakeYearSpinner.getSelectedItem().toString();
                String studentIntake = studentIntakeMonth.substring(0, 3).toUpperCase() + studentIntakeYear;

                Student new_student = new Student(studentID, studentEmailAddress, studentName, studentPassword, studentNricPassport, studentNationality, studentGender, studentPhoneNumber, studentLivingAddress, studentEnrollProgramme, studentIntake);
                firebaseDatabase.getReference("users").child(studentID).setValue(new_student);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });
    }
}