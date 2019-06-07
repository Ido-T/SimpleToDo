package com.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
     // A numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;

    // KEY USED FOR PASSING DATA BT ACTIVITIES
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> items  ;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        lvItems =  (ListView)  findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        // mock data

       // items.add("First Item");
       // items.add("Second Item");
       // items.add("Third Item");
        setupListViewListener();


    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener (){
    Log.i("MainActivity", "set up listener on List View");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("MainActivity", "item removed from list: " +position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        // set up item listener for edit (Regular click)

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 // create a new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                //pass the data being edited
                i.putExtra(ITEM_TEXT,items.get(position));
                i.putExtra(ITEM_POSITION,position);

                // display the data being edited
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

        // handle result from result activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the activity complete ok
        if(resultCode == RESULT_OK && requestCode ==EDIT_REQUEST_CODE){

            //extract uptaded item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            // EXTRACT ORIGINAL POSITION OF EDITED ITEM
            int position = data.getExtras().getInt(ITEM_POSITION);

            //UPDATE THE MODEL WITH THE NEW ITEM TEXT AT THE EDITED POSITION
            items.set(position, updatedItem);

            //NOTIFY THE ADAPTER THAT THE MODEL CHANGED
            itemsAdapter.notifyDataSetChanged();

            //PERSIST THE MODEL
            writeItems();

            //NOTIFY THE USER THE OPERATION COMPLETE
            Toast.makeText(this," Item updated successfully ", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile () {

            return new File(getFilesDir(), "todo.text");
        }

        private void readItems (){
            try {
                items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            } catch (IOException e) {
                Log.e("MainActivity","error reading File", e);
                items = new ArrayList<>();
            }
        }

        private void writeItems (){
            try {
                FileUtils.writeLines(getDataFile(), items);
            } catch (IOException e) {
                Log.e("MainActivity","error writing File", e);
            }
        }

}
