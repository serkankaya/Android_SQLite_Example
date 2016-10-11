package com.example.java3.a9_adresdefteriodev;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DB db = new DB(this);
    EditText baslik, il, ilce, sokak, adres;
    ListView list;
    ArrayList<String> adresbaslik = new ArrayList<>();
    ArrayList<String> adresdetay = new ArrayList<>();
    ArrayList<String> ID = new ArrayList<>();
    String silinecekID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baslik = (EditText) findViewById(R.id.txtbaslik);
        il = (EditText) findViewById(R.id.txtil);
        ilce = (EditText) findViewById(R.id.txtilce);
        sokak = (EditText) findViewById(R.id.txtsokak);
        adres = (EditText) findViewById(R.id.txtadres);
        list = (ListView) findViewById(R.id.liste);
        dataGetir();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                silinecekID = "";
                Toast.makeText(MainActivity.this, "Adres : " + adresdetay.get(i), Toast.LENGTH_SHORT).show();
                silinecekID = ID.get(i);
            }
        });
    }

    public void fncEkle(View v) {
        String adresbas = baslik.getText().toString().trim();
        String ail = il.getText().toString().trim();
        String ailce = ilce.getText().toString().trim();
        String asokak = sokak.getText().toString().trim();
        String aadres = adres.getText().toString().trim();
        if (adresbas.equals("") || ail.equals("") || ailce.equals("") || asokak.equals("") || aadres.equals("")) {
            Toast.makeText(MainActivity.this, "Lütfen Tüm Alanları Doldurunuz ...", Toast.LENGTH_SHORT).show();
        } else {
            try {
                db.yaz().execSQL("insert into adresler values(null,'" + adresbas + "','" + ail + "','" + ailce + "','" + asokak + "','" + aadres + "')");
                baslik.setText("");
                il.setText("");
                ilce.setText("");
                sokak.setText("");
                adres.setText("");
                baslik.requestFocus();
                dataGetir();
                Toast.makeText(MainActivity.this, "Ekleme İşlemi Başarılı", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Log.d("Ekleme Hatası : ", ex.toString());
            }
        }
    }
    public void fncSil(View v) {
        if (silinecekID.equals("")) {
            Toast.makeText(MainActivity.this, "Lütfen Silinecek veriyi seçiniz ...", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Silme İşlemi");
            builder.setMessage("Silmek İstediğinizden Emin misiniz ?");
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Yes Durumunda
                    try {
                        int sonuc = db.sil("adresler", "id", silinecekID);
                        if (sonuc > 0) {
                            dataGetir();
                            silinecekID = "";
                            Toast.makeText(MainActivity.this, "Silme İşlemi Başarılı ...", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception ex) {
                        Log.d("Silme Hatası : ", ex.toString());
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // No Durumunda
                    silinecekID="";
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void dataGetir() {
        try {
            adresbaslik.clear();
            adresdetay.clear();
            ID.clear();
            Cursor cr = db.yaz().rawQuery("select * from adresler", null);
            while (cr.moveToNext()) {
                adresbaslik.add(cr.getString(1));
                String duzenlenmisadres = cr.getString(2) + " " + cr.getString(3) + " " + cr.getString(4) + " " + cr.getString(5);
                adresdetay.add(duzenlenmisadres);
                ID.add(cr.getString(0));
            }
            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adresbaslik);
            list.setAdapter(adp);
        } catch (Exception ex) {
            Log.d("Data Getirme Hatası : ", ex.toString());
        }

    }
}
