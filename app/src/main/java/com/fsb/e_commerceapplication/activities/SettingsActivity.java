package com.fsb.e_commerceapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fsb.e_commerceapplication.R;

public class SettingsActivity extends AppCompatActivity {

    RadioButton arabe, english, french, spanish;
    Context context;
    SharedPreferences sharedPref;
    SQLiteDatabase mydatabase;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        arabe = findViewById(R.id.radio_arabe);
        english = findViewById(R.id.radio_anglais);
        french = findViewById(R.id.radio_francais);
        spanish = findViewById(R.id.radio_espagnol);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mydatabase = openOrCreateDatabase("Language", MODE_PRIVATE, null);
        textView = findViewById(R.id.SqlLite);
        String lanuage = sharedPref.getString("language", "English");
        switch (lanuage) {
            case "Spanish":
                english.setChecked(false);
                arabe.setChecked(false);
                french.setChecked(false);
                spanish.setChecked(true);
                break;
            case "Arabic":
                english.setChecked(false);
                arabe.setChecked(true);
                french.setChecked(false);
                spanish.setChecked(false);
                break;
            case "French":
                english.setChecked(false);
                arabe.setChecked(false);
                french.setChecked(true);
                spanish.setChecked(false);
                break;
            default:
                english.setChecked(true);
                arabe.setChecked(false);
                french.setChecked(false);
                spanish.setChecked(false);
                break;
        }
        if (lanuage.equals("English"))
            textView.setText("Your Language choosed is English");
        else {
            Cursor resultSet = mydatabase.rawQuery("Select * from Language",null);
            resultSet.moveToFirst();
            Toast.makeText(this, resultSet.getString(0), Toast.LENGTH_SHORT).show();
            String username = resultSet.getString(0);
            textView.setText("Your Language choosed is " + username + " Edited By SQLLite");
        }

    }

    public void onRadioButtonClicked(View view) {
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Language(lang VARCHAR);");
        boolean checked = ((RadioButton) view).isChecked();
        SharedPreferences.Editor e = sharedPref.edit();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_arabe:
                if (checked) {
                    e.putString("language", "Arabic");
                    e.apply();
                    mydatabase.execSQL("INSERT INTO Language VALUES('Arabic');");
                }
                break;
            case R.id.radio_anglais:
                if (checked) {
                    e.putString("language", "English");
                    e.apply();
                    mydatabase.execSQL("INSERT INTO Language VALUES('English');");
                }
                break;
            case R.id.radio_francais:
                if (checked) {
                    e.putString("language", "French");
                    e.apply();
                    mydatabase.execSQL("INSERT INTO Language VALUES('French');");
                }
                break;
            case R.id.radio_espagnol:
                if (checked) {
                    e.putString("language", "Spanish");
                    e.apply();
                    mydatabase.execSQL("INSERT INTO Language VALUES('Spanish');");
                }
                break;
        }
    }
}