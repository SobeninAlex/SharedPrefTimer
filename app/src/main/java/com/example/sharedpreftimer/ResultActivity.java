package com.example.sharedpreftimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView textViewResult;
    private Button buttonRestart;

    private static final String EXTRA_COUNT_RIGHT_ANSWER = "right_answer";
    private static final String EXTRA_COUNT_OF_QUESTION = "count_of_question";
    private static final int DEFAULT_VALUE = 0;
    private static final String SHARED_PREF = "score";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViews();

        var countOfRightAnswer = getIntent().getIntExtra(EXTRA_COUNT_RIGHT_ANSWER, DEFAULT_VALUE);
        var countOfQuestion = getIntent().getIntExtra(EXTRA_COUNT_OF_QUESTION, DEFAULT_VALUE);

        var preferences = getApplication().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        var max = preferences.getInt(SHARED_PREF, 0);

        var message = String.format("Правильных ответов %d из %d\nЛучший результат: %d", countOfRightAnswer, countOfQuestion, max);

        textViewResult.setText(message);

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var intent = MainActivity.newIntent(ResultActivity.this);
                startActivity(intent);
                finish();
            }
        });

    }

    public static Intent newIntent(Context context, int countOfRightAnswer, int countOfQuestion) {
        var intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_COUNT_RIGHT_ANSWER, countOfRightAnswer);
        intent.putExtra(EXTRA_COUNT_OF_QUESTION, countOfQuestion);
        return intent;
    }

    private void initViews() {
        textViewResult = findViewById(R.id.textViewResult);
        buttonRestart = findViewById(R.id.buttonRestart);
    }
}