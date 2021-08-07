package com.example.mysimpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private EditText updateItem;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        updateItem = findViewById(R.id.updateItem);
        saveButton = findViewById(R.id.saveButton);

        getSupportActionBar().setTitle("Edit item");

        Intent intent = getIntent();
        String item = intent.getStringExtra(MainActivity.KEY_ITEM_TEXT);
        int position = intent.getIntExtra(MainActivity.KEY_ITEM_POSITION, 0);

        updateItem.setText(item);

        // Button is clicked when user done editing
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent which will contains the result
                Intent intent = new Intent();

                // Pass the data
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, updateItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, position);

                // set the result of the intent
                setResult(RESULT_OK, intent);

                // finish the activity
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        EditActivity.this.overridePendingTransition(0,android.R.anim.fade_out);
    }
}