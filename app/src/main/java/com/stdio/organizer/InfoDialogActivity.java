package com.stdio.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InfoDialogActivity extends AppCompatActivity {

    TextView tvTitle, tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dialog);
        this.setFinishOnTouchOutside(true);
        tvTitle = findViewById(R.id.tvTitle);
        tvText = findViewById(R.id.tvText);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                tvTitle.setText(extras.getString("title"));
                tvText.setText(extras.getString("text"));
            }
        } else {
            tvTitle.setText((String) savedInstanceState.getSerializable("title"));
            tvText.setText((String) savedInstanceState.getSerializable("text"));
        }
    }
}
