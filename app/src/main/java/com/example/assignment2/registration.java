package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class registration extends AppCompatActivity {
    EditText name_txt,pass_txt,txt;
    String name,pass,text="";
    DBhelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        db= new DBhelper(this);
        name_txt = findViewById(R.id.name_reg);
        pass_txt=findViewById(R.id.pass_reg);
        txt = findViewById(R.id.txt);

    }

    public void Register(View view) {
        name= name_txt.getText().toString();
        pass=pass_txt.getText().toString();
        text=txt.getText().toString();
        if(name.isEmpty()||pass.isEmpty()||text.isEmpty())
        {Toast.makeText(getApplicationContext(),"Fill the 3 Fields", Toast.LENGTH_LONG).show();}
        else {
            if (pass.length() < 6 || pass.length() > 12)
                Toast.makeText(getApplicationContext(), "Password should between 6 and 12 characters", Toast.LENGTH_LONG).show();
            else{
            boolean exists = false;
            Cursor res = db.getData();
            while (res.moveToNext()) {
                if ((res.getString(1).trim().equals(name)))
                    exists = true;

            }
            if (exists) {
                Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();

            } else {
                Boolean checkinsertion = db.InsertUser(name, pass, text);
                if (checkinsertion) {
                    Toast.makeText(getApplicationContext(), "Registeration Completed", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Registration faild", Toast.LENGTH_LONG).show();

                }
            }
        }
        }


    }
}