package com.example.mysimpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //---Variables---
    //private static final int EDIT_TEXT_CODE = 1; --> Deprecated
    public static final String KEY_ITEM_TEXT = "item";
    public static final String KEY_ITEM_POSITION = "position";
    private List<String> items;
    private Button addButton;
    private EditText editItem;
    private RecyclerView rvItem;
    private ItemAdapter adapter;

    //---Methods---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addButton);
        editItem = findViewById(R.id.editItem);
        rvItem = findViewById(R.id.rvItem);

        // Handling the result of EditActivity
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String item = null;
                            int position = 0;

                            if (data != null) {
                                // Retrieve the updated text value
                                item = data.getStringExtra(KEY_ITEM_TEXT);

                                // Extract the original position of the edited item from the position key
                                position = data.getExtras().getInt(KEY_ITEM_POSITION);
                            } else {
                                Log.w("MainActivity", "Error: returned item text is null");
                            }

                            // Update the model at the right position and notify the adapter
                            items.set(position, item);
                            adapter.notifyItemChanged(position);
                            saveItems();
                            Toast.makeText(getApplicationContext(), "Item changed successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Warning log
                            Log.w("MainActivity", "Unknown call to onActivityResult");
                        }
                    }
                }
        );

        loadItems();

        // Removing an item
        ItemAdapter.OnLongClickListener longClick = new ItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                adapter.notifyItemRemoved(position);
                // Notify the user
                Toast.makeText(getApplicationContext(), "Item removed successfully!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        // Single Click Mechanism to edit item
        ItemAdapter.OnClickListener click = new ItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Log.d("MainActivity", "New item is" + newItem);
                // Create the activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                // pass the data being edited
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);

                // set the animation
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                        MainActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);

                // display the activity
                activityResultLauncher.launch(intent, options);
                // startActivityForResult(intent, EDIT_TEXT_CODE); --> Deprecated method
            }
        };

        adapter = new ItemAdapter(items, longClick, click);
        rvItem.setAdapter(adapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        // Listener
        // Dictates what happen when user press the "add" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new todoItem from the editItem View object
                String inputString = editItem.getText().toString();
                // Validate inputString
                // If string is empty, skip it
                if (!inputString.isEmpty()) {
                    // Add the new todoItem to the List of String
                    items.add(inputString);
                    // Notify adapter that an item is inserted
                    adapter.notifyItemInserted(items.size() - 1);
                    // Notify the user
                    Toast.makeText(getApplicationContext(), "Item added successfully!", Toast.LENGTH_SHORT).show();
                    saveItems();
                    // Clear the editItem writing space
                    editItem.setText("");
                }
            }
        });
    }

    // getting the data file from local storage
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

//      // Handle the result of EditActivity (Deprecated method!)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == EDIT_TEXT_CODE && resultCode == RESULT_OK) {
//            // Retrieve the updated text value
//            String item = data.getStringExtra(KEY_ITEM_TEXT);
//
//            // Extract the original position of the edited item from the position key
//            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
//
//            // Update the model at the right position and notify the adapter
//            items.set(position, item);
//            adapter.notifyItemChanged(position);
//            saveItems();
//            Toast.makeText(getApplicationContext(), "Item changed successfully!", Toast.LENGTH_SHORT).show();
//        } else {
//            // Warning log
//            Log.w("MainActivity", "Unknown call to onActivityResult");
//        }
//    }
}

