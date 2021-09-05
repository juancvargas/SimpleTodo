package com.cejajuan.simpletodo.dashboard;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cejajuan.simpletodo.editscreen.EditActivity;
import com.cejajuan.simpletodo.R;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_INDEX = "item_index";

    // container for the todo items
    ArrayList<String> items;

    // references for views in layouts
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdaptor itemsAdaptor;
    ActivityResultLauncher<Intent> mEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                            assert data != null;
                            String newText = data.getStringExtra(MainActivity.KEY_ITEM_TEXT);
                            int position = data.getIntExtra(MainActivity.KEY_ITEM_INDEX, 0);
                            items.set(position, newText);

                            saveItems();
                            itemsAdaptor.notifyItemChanged(position);
                            Toast.makeText(getApplicationContext(), "Item Updated!"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get view references
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdaptor.OnLongClickListener longClickListener = position -> {
                    // delete the item from model
                    items.remove(position);
                    // notify the adaptor of the position of the deleted item
                    itemsAdaptor.notifyItemRemoved(position);
                    Toast.makeText(getApplicationContext(), "Item Removed!"
                            , Toast.LENGTH_SHORT).show();
                    saveItems();
                };

        ItemsAdaptor.OnClickListener clickListener = position -> {
            // first parameter is the context, second is the class of the activity to launch
            Intent i = new Intent(MainActivity.this, EditActivity.class);

            // put "extras" into the bundle for access in the edit activity
            i.putExtra(KEY_ITEM_TEXT, items.get(position));
            i.putExtra(KEY_ITEM_INDEX, position);

            // brings up the edit activity
            mEditLauncher.launch(i);
        };

        // provide an Adaptor and Layout to the recycler view
        itemsAdaptor = new ItemsAdaptor(items, longClickListener, clickListener);
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