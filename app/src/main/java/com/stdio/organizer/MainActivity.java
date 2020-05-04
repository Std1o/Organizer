package com.stdio.organizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<DataModel> list;
    private RecyclerView rv;
    DBHelper dbHelper;
    private static SQLiteDatabase database;
    RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        rv = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        setAdapter();
    }

    private void getData() {
        list = new ArrayList();

        Cursor cursor = database.query(DBHelper.TABLE_EXPENSES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME);
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            do {
                DataModel dataModel = new DataModel();
                dataModel.setTitle(cursor.getString(titleIndex));
                dataModel.setDescription(cursor.getString(descriptionIndex));
                dataModel.setTime(cursor.getString(timeIndex));
                dataModel.setId(cursor.getInt(idIndex));
                list.add(dataModel);
            } while (cursor.moveToNext());
        } else {
            cursor.close();
        }
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToDB(etTitle.getText().toString(), etDescription.getText().toString(), "15.05.2020 14:55");
                DataModel dataModel = new DataModel();
                dataModel.setTitle(etTitle.getText().toString());
                dataModel.setDescription(etDescription.getText().toString());
                dataModel.setTime("15.05.2020 14:55");
                list.add(dataModel);
                adapter.notifyDataSetChanged();
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

    private void addToDB(String title, String desc, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TITLE, title);
        contentValues.put(DBHelper.KEY_DESCRIPTION, desc);
        contentValues.put(DBHelper.KEY_TIME, time);
        database.insert(DBHelper.TABLE_EXPENSES, null, contentValues);
    }

    private void setAdapter() {
        adapter = new RVAdapter(list, this);
        rv.setAdapter(adapter);
    }
}
