package com.example.personlist;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultActivity extends AppCompatActivity {


    ActionBar actionBar;
    RecyclerView mRecyclerView;
    DatabaseHelper databaseHelper;

    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        String name = getIntent().getStringExtra("title_value");

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Found information");

        mRecyclerView =  findViewById(R.id.searchResult);
        databaseHelper = new DatabaseHelper(this);

        Adapter adapter = new Adapter(SearchResultActivity.this, databaseHelper.searchInfo(name));
        mRecyclerView.setAdapter(adapter);


    }


}
