package com.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.simpletodo.MainActivity.ITEM_POSITION;
import static com.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    // Track editText
    EditText etItemText;

    //position of edited item in list
    int position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //resolve edit text from layout
        etItemText = (EditText) findViewById(R.id.etItemText);

        //set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        //UPDATE POSITION FROM INTENT EXTRA
        position = getIntent().getIntExtra(ITEM_POSITION,0);

        //UPDATE THE TITLE BAR OF THE ACTIVITY
        getSupportActionBar().setTitle(" Edited Item ");
    }
    //Handler for save button
    public void onSaveItem (View v) {
        // PREPARE NEW INTENT FOR RESULT
        Intent i = new Intent();

        //PASS UPDATED ITEM TEXT AS EXTRA
        i.putExtra(ITEM_TEXT,etItemText.getText().toString());

        //PASS ORIGIN POSITION AS EXTRA
        i.putExtra(ITEM_POSITION, position);

        //SET THE INTENT AS THE RESULT OF THE ACTIVITY
        setResult(RESULT_OK,i);

        //CLOSE THE ACTIVITY AND REDIRECT TO MAIN
        finish();
    }

}
