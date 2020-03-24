package com.damir.rezervacije;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class UnosActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText naziv, br_osoba, na_ime;
    TextView datum, vrijeme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unos);
        naziv = findViewById(R.id.editText3);
        br_osoba = findViewById(R.id.editText7);
        na_ime = findViewById(R.id.editText8);
        datum = findViewById(R.id.textView14);
        vrijeme = findViewById(R.id.textView15);
    }

    /* metoda koja poziva datepicker kada se klikne na tekst "odaberi datum" (logika preuzeta sa youtube tutorijala) */
    public void setDatumInitial(View view){
        DialogFragment datum = new DatePickerFragment();
        datum.show(getSupportFragmentManager(), "date picker");
    }

    /* metoda koja poziva timepicker kada se klikne na tekst "odaberi vrijeme" (logika preuzeta sa youtube tutorijala) */
    public void setVrijemeInitial(View view){
        DialogFragment vrijeme = new TimePickerFragment();
        vrijeme.show(getSupportFragmentManager(), "time picker");
    }

    /* metoda skuplja sve unesene podatke i vraća na prethodnu aktivnost (Merlin prezentacije / StackOverflow [potvrda da se može vise puta pozvati metoda .putExtra()] ) */
    public void vrati(View view){
        Intent intent = getIntent();
        intent.putExtra("naziv", naziv.getText().toString());
        intent.putExtra("datum", datum.getText().toString());
        intent.putExtra("vrijeme", vrijeme.getText().toString());
        intent.putExtra("br_osoba", br_osoba.getText().toString());
        intent.putExtra("na_ime", na_ime.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    /* metoda onDateSet postavlja vrijednost textview14 na odabrani datum (logika preuzeta sa youtube tutorijala) */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String current = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView t = findViewById(R.id.textView14);
        t.setText(current);
    }

    /* metoda onTimeSet postavlja vrijednost textview15 na odabrano vrijeme (logika preuzeta sa youtube tutorijala) */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView t2 = findViewById(R.id.textView15);
        t2.setText(hourOfDay+":"+minute);
    }
}
