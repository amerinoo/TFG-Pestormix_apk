package com.example.albert.pestormix_apk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.albert.pestormix_apk.R;
import com.example.albert.pestormix_apk.application.PestormixMasterFragment;

/**
 * Created by Albert on 25/01/2016.
 */
public class DetailHelpFragment extends PestormixMasterFragment {
    private View mainView;
    private int position;

    public static DetailHelpFragment getInstance(int position) {
        DetailHelpFragment fragment = new DetailHelpFragment();
        fragment.position = position;
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
        TextView question = (TextView) mainView.findViewById(R.id.question);
        TextView answer = (TextView) mainView.findViewById(R.id.answer);

        question.setText(getResources().getStringArray(R.array.questions_array)[position]);
        answer.setText(getResources().getStringArray(R.array.answers_array)[position]);
    }
}
