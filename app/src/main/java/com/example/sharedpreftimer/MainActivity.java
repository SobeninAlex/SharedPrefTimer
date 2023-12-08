package com.example.sharedpreftimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewOpinion0;
    private TextView textViewOpinion1;
    private TextView textViewOpinion2;
    private TextView textViewOpinion3;

    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewQuestion;

    private static final String SHARED_PREF = "score";

    private List<TextView> options = new ArrayList<>();

    private int countOfRightAnswer = 0;
    private int countOfQuestion = 0;

    private boolean gameOver = false;

    private MainViewModel viewModel;
//    private MainViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        options.add(textViewOpinion0);
        options.add(textViewOpinion1);
        options.add(textViewOpinion2);
        options.add(textViewOpinion3);

//        viewModelFactory = new MainViewModelFactory(options);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModelObserver();
        setScore();

        CountDownTimer timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTime(millisUntilFinished));
                if (millisUntilFinished < 7000) {
                    textViewTimer.setTextColor(getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                var preferences = getApplication().getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                int max = preferences.getInt(SHARED_PREF, 0);
                if (countOfRightAnswer >= max) {
                    preferences.edit().putInt(SHARED_PREF, countOfRightAnswer).apply();
                }
                var intent = ResultActivity.newIntent(MainActivity.this, countOfRightAnswer, countOfQuestion);
                startActivity(intent);
                finish();
            }
        };
        timer.start();

    }

    public void setScore() {
        var score = String.format("%s / %s", countOfRightAnswer, countOfQuestion);
        textViewScore.setText(score);
    }

    private String getTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private void viewModelObserver() {
        viewModel.getLiveDataQuestion().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String question) {
                textViewQuestion.setText(question);
            }
        });

        viewModel.getLiveDataAnswers().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> answers) {
                options.forEach(option -> option.setText(answers.get(options.indexOf(option))));
            }
        });
    }

    private void initViews() {
        textViewOpinion0 = findViewById(R.id.textViewOpinion0);
        textViewOpinion1 = findViewById(R.id.textViewOpinion1);
        textViewOpinion2 = findViewById(R.id.textViewOpinion2);
        textViewOpinion3 = findViewById(R.id.textViewOpinion3);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestion = findViewById(R.id.textViewQuestion);
    }

    public void onClickAnswer(View view) {
        if (!gameOver) {
            var textView = (TextView) view;
            var message = "";
            var answer = Integer.parseInt(textView.getText().toString());
            if (answer == MainViewModel.rightAnswer) {
                message = "Верно!";
                countOfRightAnswer++;

            } else {
                message = "Неверно!";
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            countOfQuestion++;
            setScore();

            viewModel.generateQuestion();
            viewModel.setAnswer();
        }
    }

    public static Intent newIntent(Context context) {
        var intent = new Intent(context, MainActivity.class);
        return intent;
    }
}