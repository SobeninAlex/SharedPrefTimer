package com.example.sharedpreftimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> liveDataQuestion = new MutableLiveData<>();
    public LiveData<String> getLiveDataQuestion() {
        return liveDataQuestion;
    }

    private MutableLiveData<List<String>> liveDataAnswers = new MutableLiveData<>();
    public MutableLiveData<List<String>> getLiveDataAnswers() {
        return liveDataAnswers;
    }

    private String question;
    public static int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;

    public MainViewModel() {
        generateQuestion();
        setAnswer();
    }

    public void generateQuestion() {
        Random random = new Random();
        int a = random.nextInt(max - min) + min;
        int b = random.nextInt(max - min) + min;
        int mark = random.nextInt(2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        liveDataQuestion.setValue(question);
        rightAnswerPosition = random.nextInt(4);
    }

    public void setAnswer() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i == rightAnswerPosition) {
                list.add(Integer.toString(rightAnswer));
                continue;
            }
            list.add(Integer.toString(generateWrongAnswer()));
        }
        liveDataAnswers.setValue(list);
    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

}
