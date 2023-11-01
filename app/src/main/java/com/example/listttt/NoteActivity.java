package com.example.listttt;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {
    private EditText noteTitleEditText;
    private EditText noteContentEditText;
    private Button saveButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteTitleEditText = findViewById(R.id.Zam1);
        noteContentEditText = findViewById(R.id.Zam2);
        saveButton = findViewById(R.id.savebutton);
        backButton = findViewById(R.id.backbutton);

        Intent intent = getIntent();
        String topic = intent.getStringExtra("topic");

        if (topic != null && !topic.isEmpty()) {
            setTitle(topic);

            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String savedTitle = preferences.getString(topic + "_title", "");
            String savedContent = preferences.getString(topic + "_content", "");

            if (!savedTitle.isEmpty() && !savedContent.isEmpty()) {
                noteTitleEditText.setText(savedTitle);
                noteContentEditText.setText(savedContent);
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = getTitle().toString();
                String title = noteTitleEditText.getText().toString();
                String content = noteContentEditText.getText().toString();
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                preferences.edit()
                        .putString(topic + "_title", title)
                        .putString(topic + "_content", content)
                        .apply();


                // Отправляем информацию о том, что данные успешно сохранены
                Intent returnIntent = new Intent();
                returnIntent.putExtra("updatedTopic", topic);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
