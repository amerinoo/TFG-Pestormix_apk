package com.example.albert.pestormix_apk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;
import com.example.albert.pestormix_apk.repositories.QuestionRepository;
import com.example.albert.pestormix_apk.models.Question;

/**
 * Created by Albert on 25/01/2016.
 */
public class DetailHelpFragment extends PestormixMasterFragment {
    private View mainView;
    private int id;
    private TextView question;
    private TextView answer;

    public static DetailHelpFragment getInstance(int id) {
        DetailHelpFragment fragment = new DetailHelpFragment();
        fragment.id = id;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_help, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        configView();
    }

    private void configView() {
        question = (TextView) mainView.findViewById(R.id.question);
        answer = (TextView) mainView.findViewById(R.id.answer);

        update(id);
    }

    public void update(int id) {
        Question questionObject = QuestionRepository.getQuestionById(getRealm(), id);
        question.setText(questionObject.getQuestion());
        answer.setText(questionObject.getAnswer());
    }
}
