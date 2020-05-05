package com.stdio.organizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<DataModel> list = new ArrayList<>();
    private RecyclerView rv;
    DBHelper dbHelper;
    private static SQLiteDatabase database;
    RVAdapter adapter;
    Calendar dateCalendar = Calendar.getInstance();
    TextView tvDate;
    public static TextView tvTime;
    private AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        alarm = new AlarmManagerBroadcastReceiver();
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
        list = new ArrayList<>();

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
        switch (view.getId()) {
            case R.id.fab:
                addNote();
                break;
            case R.id.tvDate:
                showDatePicker();
                break;
            case R.id.tvTime:
                DialogFragment dlg = new TimePickerFragment();
                dlg.show(getSupportFragmentManager(), "TimePicker");
                break;
        }
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = datePickerListener;
        new DatePickerDialog(this, dateSetListener,
                dateCalendar.get(Calendar.YEAR),
                dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tvDate.setText(getDate(year, monthOfYear, dayOfMonth) + " ");
        }
    };

    private String getDate(int year, int monthOfYear, int dayOfMonth) {
        int trueMonth = ++monthOfYear;
        String strDay = null;
        String strTrueMonth = null;
        if (String.valueOf(dayOfMonth).length() == 1){
            strDay = "0" + dayOfMonth;
        }
        else {
            strDay = String.valueOf(dayOfMonth);
        }
        if (String.valueOf(trueMonth).length() == 1){
            strTrueMonth = "0" + trueMonth;
        }
        else {
            strTrueMonth = String.valueOf(trueMonth);
        }
        return strDay + "." + strTrueMonth + "." + year;
    }

    private void addNote() {
        View dialogView = getLayoutInflater().inflate(R.layout.add_dialog, null);
        final EditText etTitle = dialogView.findViewById(R.id.etTitle);
        final EditText etDescription = dialogView.findViewById(R.id.etDescription);
        tvDate = dialogView.findViewById(R.id.tvDate);
        tvTime = dialogView.findViewById(R.id.tvTime);
        tvDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()) + " ");
        tvTime.setText(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String time = tvDate.getText().toString() + tvTime.getText().toString();
                        addToDB(etTitle.getText().toString(), etDescription.getText().toString(), time);
                        DataModel dataModel = new DataModel();
                        dataModel.setTitle(etTitle.getText().toString());
                        dataModel.setDescription(etDescription.getText().toString());
                        dataModel.setTime(time);
                        list.add(dataModel);
                        adapter.notifyDataSetChanged();
                        if(alarm!=null){
                            alarm.SetAlarm(MainActivity.this, time, etTitle.getText().toString());
                        }else{
                            Toast.makeText(MainActivity.this,"Alarm is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dialog.setTitle("Новое напоминание");
        dialog.setNegativeButton("Отмена", null);
        dialog.show();
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
