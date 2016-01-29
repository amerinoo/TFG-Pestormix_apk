package com.example.albert.pestormix_apk.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Albert on 29/01/2016.
 */
public class Question extends RealmObject {
    @PrimaryKey
    private int id;
    private String question;
    private String answer;

    public Question() {
        question = "";
        answer = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
