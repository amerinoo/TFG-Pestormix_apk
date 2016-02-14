package com.example.albert.pestormix_apk.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by Albert on 14/02/2016.
 */
public class EditTextWithDone extends EditText {

    public EditTextWithDone(Context context) {
        super(context);
    }

    public EditTextWithDone(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithDone(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }
}