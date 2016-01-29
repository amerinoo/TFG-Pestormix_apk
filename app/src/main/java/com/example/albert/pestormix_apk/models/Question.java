package com.example.albert.pestormix_apk.models;

/**
 * Created by Albert on 29/01/2016.
 */
public class Question {
    private String question;
    private String answer;

    public Question() {
        question = "";
        answer = "";
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
