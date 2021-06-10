package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText name_text,password_text;
    Button log,reg;
    CheckBox box;
    String name,pass;
    DBhelper db;
    boolean remember;
    Boolean exists=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_text = findViewById(R.id.name);
        password_text=findViewById(R.id.password);
        db= new DBhelper(this);
        box = findViewById(R.id.checkbox);
        String unm="";
        String pas="";
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        unm=sp1.getString("Unm", null);
        pas = sp1.getString("Psw", null);
        name_text.setText(unm);
        password_text.setText(pas);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!box.isChecked()) {
            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("Unm", "");
            Ed.putString("Psw", "");
            Ed.commit();
            Toast.makeText(getApplicationContext(),"Thank you for using my app", Toast.LENGTH_LONG).show();
        }
    }

    public void log(View view) {

        name= name_text.getText().toString().trim();
        pass=password_text.getText().toString().trim();
        if(name.isEmpty()||pass.isEmpty())
        {Toast.makeText(getApplicationContext(),"Fill username and password Fields", Toast.LENGTH_LONG).show();}
        else
        {
            Cursor res= db.getData();
            exists=false;
            if(res.getCount()==0)
            {
                Toast.makeText(getApplicationContext(),"No users in the database", Toast.LENGTH_LONG).show();
            }else
            {
                while (res.moveToNext())
                {   //db.deleteData(res.getString(1));
                    Log.i("name","Name :"+res.getString(1)+ " Password: "+res.getString(2)+ " Notes: "+res.getString(3));
                    if((res.getString(1).trim().equals(name)) && (res.getString(2).trim().equals(pass)))
                    { //+ " Notes: "+res.getString(3)

                        exists=true;
                        Log.i("name","Exists" );

                    }
                }
                if(exists)
                {
                    Toast.makeText(getApplicationContext(),"Login successful", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),notes.class);
                    intent.putExtra("name",name);
                    intent.putExtra("notes",db.getNotes(name));
                    if(remember) {
                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("Unm", name);
                        Ed.putString("Psw", pass);
                        Ed.commit();
                    }else
                    {
                        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor Ed = sp.edit();
                        Ed.putString("Unm", "");
                        Ed.putString("Psw", "");
                        Ed.commit();
                    }
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"User doesn't exist", Toast.LENGTH_LONG).show();

                }
            }
        }
    }


    public void reg(View view) {
            Intent intent = new Intent(getApplicationContext(), registration.class);
            startActivity(intent);

    }


    public void checkBox(View view) {
        if(box.isChecked())
            remember=true;
        else {
            remember=false;
            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("Unm", "");
            Ed.putString("Psw", "");
            Ed.commit();
        }

    }
}