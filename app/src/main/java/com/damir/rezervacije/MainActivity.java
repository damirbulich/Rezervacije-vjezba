package com.damir.rezervacije;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setDatum(View view){
        DialogFragment datum = new DatePickerFragment();
        datum.show(getSupportFragmentManager(), "date picker");
    }
    public void setVrijeme(View view){
        DialogFragment vrijeme = new TimePickerFragment();
        vrijeme.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String current = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView t = findViewById(R.id.textView7);
        t.setText(current);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView t2 = findViewById(R.id.textView8);
        t2.setText(hourOfDay+":"+minute);
    }
}
