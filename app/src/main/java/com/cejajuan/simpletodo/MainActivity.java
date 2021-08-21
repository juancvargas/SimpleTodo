package com.cejajuan.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {
    // container for the todo items
    ArrayList<String> items;

    // references for components aka views in the activity_main.xml layout
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdaptor itemsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get view references
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdaptor.OnLongClickLister onLongClickLister =
                position -> {
                    // delete the item from model
                    items.remove(position);
                    // notify the adaptor of the position of the deleted item
                    itemsAdaptor.notifyItemRemoved(position);
                    Toast.makeText(getApplicationContext(), "Item Removed!"
                            , Toast.LENGTH_SHORT).show();
                    saveItems();
                };

        // provide an Adaptor and Layout to the recycler view
        itemsAdaptor = new ItemsAdaptor(items, onLongClickLister);
        rvItems.setAdapter(itemsAdaptor);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // handler for the add button
        btnAdd.setOnClickListener(view -> {
            String todoItem = etItem.getText().toString();

            if (todoItem.isEmpty()) {
                Toast.makeText(getApplicationContext(), "List item not provided!"
                        , Toast.LENGTH_SHORT).show();
                return;
            }

            // notify adaptor that an item is inserted
            items.add(todoItem);
            itemsAdaptor.notifyItemInserted(items.size() - 1);
            etItem.setText("");
            Toast.makeText(getApplicationContext(), "Item was added"
                        , Toast.LENGTH_SHORT).show();
            saveItems();
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(),
                    Charset.defaultCharset().toString()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}