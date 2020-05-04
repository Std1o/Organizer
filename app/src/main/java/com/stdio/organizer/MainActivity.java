package com.stdio.organizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<DataModel> dataList;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
    }

    private void initRecyclerView() {
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        initializeData();
        initializeAdapter();
    }

    private void initializeData() {
        dataList = new ArrayList<>();
        DataModel dataModel = new DataModel();
        dataModel.setTitle("Title");
        dataModel.setDescription("Description one two three four");
        dataModel.setTime("12.03.2020 15:50");
        dataList.add(dataModel);
        dataList.add(dataModel);
        dataList.add(dataModel);
        dataList.add(dataModel);
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(dataList, this);
        rv.setAdapter(adapter);
    }
}
