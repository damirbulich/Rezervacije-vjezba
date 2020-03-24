package com.damir.rezervacije;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    List<Rezervacija> rezervacije;
    EditText pin, naziv, br_osoba, ime;
    TextView datum, vrijeme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pin = findViewById(R.id.editText);
        naziv = findViewById(R.id.editText2);
        br_osoba = findViewById(R.id.editText5);
        ime = findViewById(R.id.editText6);
        datum = findViewById(R.id.textView7);
        vrijeme = findViewById(R.id.textView8);

    }

    public void setDatum(View view){
        DialogFragment datum = new DatePickerFragment();
        datum.show(getSupportFragmentManager(), "date picker");
    }
    public void setVrijeme(View view){
        DialogFragment vrijeme = new TimePickerFragment();
        vrijeme.show(getSupportFragmentManager(), "time picker");
    }

    /* metoda za konverziju liste u json format i spremanje u datoteku moja_sprema.xml */
    public void saveRezervacije(){
        SharedPreferences sprema = getSharedPreferences("moja_sprema", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sprema.edit();
        Gson g = new Gson();
        String jsonRezervacije = g.toJson(rezervacije);
        editor.putString("rezervacije", jsonRezervacije);
        editor.apply();
    }

    /* metoda za dohvat podataka iz datoteke i konverziju podataka iz json formata u listu */
    public void loadRezervacije(){
        SharedPreferences sprema = getSharedPreferences("moja_sprema", Context.MODE_PRIVATE);
        Gson g = new Gson();
        String json = sprema.getString("rezervacije", null);
        Type tip = new TypeToken<ArrayList<Rezervacija>>() {}.getType();
        rezervacije = g.fromJson(json, tip);

        if (rezervacije == null){
            rezervacije = new ArrayList<Rezervacija>();
        }
    }

    public void addRezervacija(View view){
        Intent intent = new Intent(this, UnosActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Prezentacija Aktivnosti na Merlinu */
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                loadRezervacije();
                //Rezervacija nova = new Rezervacija(1234,"Orsan", "2.2.2020.", "12:23", "11", "Damir");

                Rezervacija nova = new Rezervacija(
                        data.getStringExtra("naziv"),
                        data.getStringExtra("datum"),
                        data.getStringExtra("vrijeme"),
                        data.getStringExtra("br_osoba"),
                        data.getStringExtra("na_ime")
                );

                /* generiraj novi pin sve dok lista sadrzi rezervaciju s istim pinom */
                do{
                    nova.setPin();
                }while (sadrzi(rezervacije, nova));

                /* dodaj novu rezervaciju u listu */
                rezervacije.add(nova);

                /* Ispiši novokreiranu rezervaciju na ekran */
                pin.setText(nova.getPin()+"");
                naziv.setText(nova.getRestoran()+"");
                datum.setText(nova.getDatum()+"");
                vrijeme.setText(nova.getVrijeme()+"");
                br_osoba.setText(nova.getBr_osoba()+"");
                ime.setText(nova.getIme()+"");

                /* spremi sve rezervacije u datoteku */
                saveRezervacije();
            }
        }
    }

    /* metoda za provjeru */
    public boolean sadrzi(List<Rezervacija> rezervacijas, Rezervacija nova){
        if(rezervacijas==null) return false;
        for(Rezervacija rezervacija : rezervacijas){
            if (rezervacija.getPin()==nova.getPin()){
                return true;
            }
        }
        return false;
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
