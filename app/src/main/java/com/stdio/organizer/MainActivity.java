package com.stdio.organizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        rv = findViewById(R.id.recyclerView);
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

    public void onClick(View view) {
        addNote();
    }

    private void addNote() {
        View view = getLayoutInflater().inflate(R.layout.add_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Новое напоминание");
        final EditText etTitle = view.findViewById(R.id.etTitle);
        final EditText etDescription = view.findViewById(R.id.etDescription);
        final DataModel dataModel = new DataModel();
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataModel.setTitle(etTitle.getText().toString());
                dataModel.setDescription(etDescription.getText().toString());
                //myRef.push().setValue(finalDataModel);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(dataList, this);
        rv.setAdapter(adapter);
    }
}
