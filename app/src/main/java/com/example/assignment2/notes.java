package com.example.assignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class notes extends AppCompatActivity {
    TextView name_txt;
    ListView note_txt;
    String name,notes,new_note="";
    DBhelper db;
    String[] tokens;
    ArrayList<String>Note;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        name_txt=findViewById(R.id.user_name);
        note_txt=findViewById(R.id.notes);
        db= new DBhelper(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                name= null;
                notes= null;
            } else {
                name= extras.getString("name");
                notes= extras.getString("notes");

            }
        } else {
            name= (String) savedInstanceState.getSerializable("name");
            notes= (String) savedInstanceState.getSerializable("notes");
        }
        name_txt.setText("Notes for "+name+" :");
        tokens = notes.split("\\n");
        Note=new ArrayList<String>();
        for(int i=0;i<tokens.length;i++)
            Note.add(tokens[i]);
        adapter = new ArrayAdapter<String>(this,R.layout.activity_listview,R.id.lis,Note);
        note_txt.setAdapter(adapter);
    }

    public void add_note(View view) {
showInputDialog();
    }
    protected void showInputDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                new_note=userInput.getText().toString();
                              Log.i("name",name+new_note);
                                boolean insertion= db.InsertNote(name,new_note);
                                if(insertion) {
                                    Toast.makeText(getApplicationContext(), "ADDED", Toast.LENGTH_LONG).show();
                                   // note_txt.setText(db.getNotes(name));
                                    Note.clear();
                                    tokens = db.getNotes(name).split("\\n");
                                    for(int i=0;i<tokens.length;i++)
                                        Log.i("notes", tokens[i]);
                                    for(int j=0;j<tokens.length;j++)
                                        Note.add(tokens[j]);
                                    adapter.notifyDataSetChanged();

                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Failed to add", Toast.LENGTH_LONG).show();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
    public void updatedData(String[] items) {

        adapter.clear();

        if (items != null){

            for (Object object : items) {

                adapter.insert(object, adapter.getCount());
            }
        }

        adapter.notifyDataSetChanged();

    }

    public void clear(View view) {
        boolean insertion= db.UpdateNote(name);
        if(insertion) {
            Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_LONG).show();
            Note.clear();
            adapter.notifyDataSetChanged();
        }
        else
            Toast.makeText(getApplicationContext(), "Failed to clear", Toast.LENGTH_LONG).show();

    }
}

