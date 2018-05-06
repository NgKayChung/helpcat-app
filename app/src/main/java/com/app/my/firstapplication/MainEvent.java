package com.app.my.firstapplication;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainEvent extends AppCompatActivity {


    private ExpandableListView ExpendList;
    private  ExpandableListAdapter adapter;
    private  ArrayList<String> listCategoria;
    private Map<String,ArrayList<String>> mapChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);


        ExpendList =(ExpandableListView) findViewById(R.id.ExpendList);
        listCategoria = new ArrayList<>();
        mapChild = new HashMap<>();
        cargarDatos();


    }

    private void cargarDatos()
    {
        ArrayList<String> listBigData = new ArrayList<>();
        ArrayList<String> listAruPromote = new ArrayList<>();
        ArrayList<String> listUIU = new ArrayList<>();
        ArrayList<String> listACCA = new ArrayList<>();


        listCategoria.add("BigData");
        listCategoria.add("Aru");
        listCategoria.add("UIU");
        listCategoria.add("ACCA");

        listBigData.add("Mr Hello are going to have an introduction of big data in level 4 at 12:00");
        listAruPromote.add("Ms Vasugi");
        listUIU.add("Dnt know");
        listACCA.add("ACCA");

        mapChild.put(listCategoria.get(0),listBigData);
        mapChild.put(listCategoria.get(1),listAruPromote);
        mapChild.put(listCategoria.get(2),listUIU);
        mapChild.put(listCategoria.get(3),listACCA);

        adapter = new ExpandableListAdapter(listCategoria, mapChild, this);


        ExpendList.setAdapter(adapter);


        }






}





