package com.example.millionaire; // Change to your package name

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button optionAButton, optionBButton, optionCButton, optionDButton;
    private Button prevButton, nextButton;

    private List<JSONObject> questions;
    private int currentQuestionIndex = 0;

    // Store the original button color (replace with your default button color)
    private int originalColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        optionAButton = findViewById(R.id.optionAButton);
        optionBButton = findViewById(R.id.optionBButton);
        optionCButton = findViewById(R.id.optionCButton);
        optionDButton = findViewById(R.id.optionDButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        // Set the original button color (you can define this in your colors.xml)
        originalColor = Color.LTGRAY; // Replace with your default button color

        loadQuestionsFromJSON();
        loadQuestion();

        // Create an OnClickListener for the answer buttons
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button selectedButton = (Button) v;
                String selectedAnswer = selectedButton.getText().toString().substring(0, 1); // Get the option letter (A, B, C, D)
                try {
                    String correctAnswer = questions.get(currentQuestionIndex).getString("answer");
                    if (selectedAnswer.equals(correctAnswer)) {
                        selectedButton.setBackgroundColor(Color.GREEN); // Change button color to green


                        // Delay for 3 seconds before moving to the next question
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                selectedButton.setBackgroundColor(originalColor); // Revert to original color
                                nextQuestion();
                            }
                        }, 3000);
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong answer! Try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        // Set the OnClickListener for each button
        optionAButton.setOnClickListener(optionClickListener);
        optionBButton.setOnClickListener(optionClickListener);
        optionCButton.setOnClickListener(optionClickListener);
        optionDButton.setOnClickListener(optionClickListener);

        // Set OnClickListener for next and previous buttons
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    previousQuestion();
        }
    });
}

private void loadQuestionsFromJSON() {
    String json;
    try {
        InputStream is = getAssets().open("JSONMiljonar.txt");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, StandardCharsets.UTF_8);

        JSONArray jsonArray = new JSONArray(json);
        questions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            questions.add(jsonArray.getJSONObject(i));
        }
    } catch (IOException | JSONException e) {
        e.printStackTrace();
    }
}

private void loadQuestion() {
    try {
        JSONObject question = questions.get(currentQuestionIndex);
        questionTextView.setText(question.getString("question"));
        optionAButton.setText("A: " + question.getString("A"));
        optionBButton.setText("B: " + question.getString("B"));
        optionCButton.setText("C: " + question.getString("C"));
        optionDButton.setText("D: " + question.getString("D"));
        resetButtonColors(); // Reset button colors when loading a new question
    } catch (JSONException e) {
        e.printStackTrace();
    }
}

private void resetButtonColors() {
    optionAButton.setBackgroundColor(originalColor);
    optionBButton.setBackgroundColor(originalColor);
    optionCButton.setBackgroundColor(originalColor);
    optionDButton.setBackgroundColor(originalColor);
}

private void nextQuestion() {
    if (currentQuestionIndex < questions.size() - 1) {
        currentQuestionIndex++;
        loadQuestion();
    } else {
        Toast.makeText(this, "You've reached the end of the questions!", Toast.LENGTH_SHORT).show();
    }
}

private void previousQuestion() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        loadQuestion();
    } else {
        Toast.makeText(this, "This is the first question!", Toast.LENGTH_SHORT).show();
    }
}
}