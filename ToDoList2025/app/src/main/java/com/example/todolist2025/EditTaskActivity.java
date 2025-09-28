package com.example.todolist2025;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {
    private EditText etContent;
    private Button btPickDate, btPickTime, btUpdate, btCancel;
    private CheckBox chHi;
    private int action; // add, edit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        etContent = findViewById(R.id.etContent);
        btPickDate = findViewById(R.id.btDate);
        btPickTime = findViewById(R.id.btTime);
        btUpdate = findViewById(R.id.btUpdate);
        btCancel = findViewById(R.id.btCancel);
        chHi=findViewById(R.id.chHi);


        Intent intent = getIntent();
        if ( intent.getExtras() != null)
        {
            etContent.setText(intent.getStringExtra("key_content"));
            String str = intent.getStringExtra("key_deadline");
            if( str != null && !str.equals("") )
            {
                btPickDate.setText(str);
            }
            chHi.setChecked(intent.getBooleanExtra("key_hi", false));
            action = intent.getIntExtra("key_action", -1);
        }


        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                intent.putExtra("key_content", etContent.getText().toString());
                intent.putExtra("key_hi", chHi.isChecked());
                if( !btPickDate.getText().toString().equalsIgnoreCase("Pick Date"))
                    intent.putExtra("key_deadline", btPickDate.getText().toString());
                intent.putExtra("key_action", action);
                //startActivity(intent);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }
        });

        btPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar systemCalender = Calendar.getInstance();
                int year = systemCalender.get(Calendar.YEAR);
                int month = systemCalender.get(Calendar.MONTH);
                int day = systemCalender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btPickDate.setText( dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }


}