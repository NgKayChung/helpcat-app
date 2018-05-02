package com.app.my.firstapplication;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainEvent extends AppCompatActivity {

    ListView mListView;
    int[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
    String[] TextEvent = {"Big Data", "App develop", "ARU promote"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.EventList);
        CustomAdaptor customAdaptor = new CustomAdaptor();
        mListView.setAdapter(customAdaptor);

    }

    class CustomAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.event_list_layout,null);
            ImageView mImageView = (ImageView) view.findViewById(R.id.imageView);
            TextView mTextView =(TextView) view.findViewById(R.id.textView);

            mImageView.setImageResource(images[position]);
            mTextView.setText(TextEvent[position]);

            return view;
        }
    }


}





