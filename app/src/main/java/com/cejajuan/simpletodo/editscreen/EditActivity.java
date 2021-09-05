package com.cejajuan.simpletodo.editscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.cejajuan.simpletodo.R;
import com.cejajuan.simpletodo.dashboard.MainActivity;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    // references to the views in the activity_edit xml file
    EditText editText;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Item");

        editText = findViewById(R.id.etItemUpdate);
        btnSave = findViewById(R.id.btnSaveEdit);

        // set the editText to the current text
        editText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        int position = getIntent().getIntExtra(MainActivity.KEY_ITEM_INDEX, 0);

        btnSave.setOnClickListener(view -> {
            // intent which will contain the result
            Intent data = new Intent();

            // pass the result of editing
            data.putExtra(MainActivity.KEY_ITEM_TEXT, editText.getText().toString());
            data.putExtra(MainActivity.KEY_ITEM_INDEX, position);

            setResult(Activity.RESULT_OK, data);

            // close the screen and go back to MainActivity
            finish();
        });
    }
}