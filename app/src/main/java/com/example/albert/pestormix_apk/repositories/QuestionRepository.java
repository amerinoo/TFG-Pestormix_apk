package com.example.albert.pestormix_apk.repositories;

import com.example.albert.pestormix_apk.controllers.DataController;
import com.example.albert.pestormix_apk.models.Question;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Albert on 29/01/2016.
 */
public abstract class QuestionRepository {
    public static List<Question> init() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Question question = new Question();
            question.setId(i);
            question.setQuestion("Question " + i);
            question.setAnswer("Answer of " + question.getQuestion().toLowerCase());
            questions.add(question);
        }
        return questions;
    }

    public static List<String> getQuestionsStrings(Realm realm) {
        return DataController.getQuestionsStrings(realm);
    }

    public static Question getQuestionById(Realm realm, int id) {
        return DataController.getQuestionById(realm, id);
    }
}
