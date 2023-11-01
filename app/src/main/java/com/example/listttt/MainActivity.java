package com.example.listttt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> noteNames = new ArrayList();
    private Button addNoteButton;

    static final int REQUEST_CODE = 1;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // Обновите noteNames при возврате из NoteActivity
                        updateNoteNamesFromSharedPreferences();
                        // Затем обновите адаптер
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
                        adapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = findViewById(R.id.listView);
        addNoteButton = findViewById(R.id.addNoteButton);
        // Загрузите noteNames из SharedPreferences при создании MainActivity
        updateNoteNamesFromSharedPreferences();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteNames);
        listView.setAdapter(adapter);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем NoteActivity для создания новой заметки с введенным названием
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("topic", ""); // Передаем пустую строку как название новой заметки
                mStartForResult.launch(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTopic = noteNames.get(position);
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("topic", selectedTopic);
                mStartForResult.launch(intent);
            }
        });
    }

    private void updateNoteNamesFromSharedPreferences() {
        noteNames.clear();
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().endsWith("_title")) {
                String topic = entry.getKey().replace("_title", "");
                noteNames.add(topic);
            }
        }
    }
}
