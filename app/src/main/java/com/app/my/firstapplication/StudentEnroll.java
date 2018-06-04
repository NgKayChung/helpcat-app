package com.app.my.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class StudentEnroll extends AppCompatActivity {
    Spinner sp;
    Button AddB,DrpB,SmtB;
    ListView sjL;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_enroll);

        sp =(Spinner) findViewById(R.id.spinner);
        AddB =(Button) findViewById(R.id.addBtn);
        DrpB=(Button) findViewById(R.id.drpBtn);
        SmtB=(Button) findViewById(R.id.smtBtn);
        sjL=(ListView) findViewById(R.id.sbjList);

        arrayList=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(StudentEnroll.this,android.R.layout.simple_list_item_1,arrayList);
        sjL.setAdapter(adapter);

        addOnClick();
    }

    public void addOnClick()
    {
        AddB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result =sp.getSelectedItem().toString();
                arrayList.add(result);
            }
        });
    }
}
