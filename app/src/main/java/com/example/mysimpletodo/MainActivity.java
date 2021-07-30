package com.example.mysimpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    List<String> items;
    Button addButton;
    EditText editItem;
    RecyclerView rvItem;
    ItemAdapter adapter;
    ItemAdapter.OnLongClickListener longClick;

    //---Methods---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        editItem = findViewById(R.id.editItem);
        rvItem = findViewById(R.id.rvItem);

        loadItems();

        // Removing an item
        longClick = new ItemAdapter.OnLongClickListener() {
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

        adapter = new ItemAdapter(items, longClick);
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
                }
                // Clear the editItem writing space
                editItem.setText("");

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
}

