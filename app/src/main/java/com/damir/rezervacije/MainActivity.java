package com.damir.rezervacije;

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
import android.widget.Toast;

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

    /* metoda koja poziva datepicker kada se klikne na tekst "odaberi datum" (logika preuzeta sa youtube tutorijala) */
    public void setDatum(View view){
        DialogFragment datum = new DatePickerFragment();
        datum.show(getSupportFragmentManager(), "date picker");
    }

    /* metoda koja poziva timepicker kada se klikne na tekst "odaberi vrijeme" (logika preuzeta sa youtube tutorijala) */
    public void setVrijeme(View view){
        DialogFragment vrijeme = new TimePickerFragment();
        vrijeme.show(getSupportFragmentManager(), "time picker");
    }

    /* metoda za konverziju liste u json format i spremanje u datoteku moja_sprema.xml (logika preuzeta sa youtube tutorijala) */
    public void saveRezervacije(){
        SharedPreferences sprema = getSharedPreferences("moja_sprema", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sprema.edit();
        Gson g = new Gson();
        String jsonRezervacije = g.toJson(rezervacije);
        editor.putString("rezervacije", jsonRezervacije);
        editor.apply();
    }

    /* metoda za dohvat podataka iz datoteke i konverziju podataka iz json formata u listu (logika preuzeta sa youtube tutorijala) */
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

    /* metoda pozvana klikom na gumb Dodaj otvara novu aktivnost za unos nove rezervacije */
    public void addRezervacija(View view){
        Intent intent = new Intent(this, UnosActivity.class);
        startActivityForResult(intent, 1);
    }

    /* povratkom iz aktivnosti za unos nove rezervacije spremamo novu rezervaciju */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Prezentacija Aktivnosti na Merlinu */
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                /* učitaj sve rezervacije */
                loadRezervacije();
                //Rezervacija nova = new Rezervacija(1234,"Orsan", "2.2.2020.", "12:23", "11", "Damir");

                /* kreiraj novu instancu rezervacije sa podatcima iz unosa */
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

    /* jako neefikasna metoda za provjeru */
    public boolean sadrzi(List<Rezervacija> rezervacijas, Rezervacija nova){
        if(rezervacijas==null) return false;
        for(Rezervacija rezervacija : rezervacijas){
            if (rezervacija.getPin()==nova.getPin()){
                return true;
            }
        }
        return false;
    }

    /* metoda pozvana klikom na gumb Pretraži pretraživa rezervacije po pinu */
    public void findRezervacija(View view){
        int id = Integer.parseInt(pin.getText()+"");
        loadRezervacije();
        Rezervacija odabrana = getRezervacija(id, rezervacije);
        if (odabrana == null){
            pin.setText("");
            naziv.setText("");
            datum.setText("");
            vrijeme.setText("");
            br_osoba.setText("");
            ime.setText("");
            Toast.makeText(getApplicationContext(),
                    "Ne postoji rezervacija pod tim pinom!",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            pin.setText(odabrana.getPin()+"");
            naziv.setText(odabrana.getRestoran()+"");
            datum.setText(odabrana.getDatum()+"");
            vrijeme.setText(odabrana.getVrijeme()+"");
            br_osoba.setText(odabrana.getBr_osoba()+"");
            ime.setText(odabrana.getIme()+"");
            Toast.makeText(getApplicationContext(),
                    "Dohvaćena rezervacija: "+odabrana.getPin(),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /* neefikasna metoda za traženje rezervacija */
    public Rezervacija getRezervacija(int pin, List<Rezervacija> rezervacijas){
        for (Rezervacija rez : rezervacijas){
            if ( rez.getPin() == pin){
                return rez;
            }
        }
        return null;
    }

    /* metoda za update rezervacija poziva se pritiskom na gumb "uredi" */
    public void updateRezervacija(View view){
        int id = Integer.parseInt(pin.getText()+"");
        boolean found = false;
        loadRezervacije();
        for (Rezervacija rez : rezervacije){
            if ( rez.getPin() == id){
                rez.setRestoran(naziv.getText().toString());
                rez.setDatum(datum.getText().toString());
                rez.setVrijeme(vrijeme.getText().toString());
                rez.setBr_osoba(br_osoba.getText().toString());
                rez.setIme(ime.getText().toString());
                found = true;
                Toast.makeText(getApplicationContext(),
                        "Izmjenjena rezervacija: "+rez.getPin(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if(found)
            saveRezervacije();
        else
            Toast.makeText(getApplicationContext(),
                    "Nije nađena rezervacija: "+id,
                    Toast.LENGTH_SHORT)
                    .show();
    }


    public void deleteRezervacija(View view){
        int id = Integer.parseInt(pin.getText()+"");
        loadRezervacije();
        rezervacije.remove(getRezervacija(id, rezervacije));
        saveRezervacije();
    }

    /* metoda onDateSet postavlja vrijednost textview7 na odabrani datum (logika preuzeta sa youtube tutorijala) */
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

    /* metoda onTimeSet postavlja vrijednost textview8 na odabrano vrijeme (logika preuzeta sa youtube tutorijala) */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView t2 = findViewById(R.id.textView8);
        t2.setText(hourOfDay+":"+minute);
    }
}
