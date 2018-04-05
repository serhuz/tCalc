/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

import ua.sergeimunovarov.tcalc.BuildConfig;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.ops.Converter;
import ua.sergeimunovarov.tcalc.main.ops.InputPattern;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * An {@link AppCompatEditText} subclass with custom font.
 */
public class CustomEditText extends AppCompatEditText {

    private SelectionChangedListener mSelectionChangedListener;


    public CustomEditText(Context context) {
        super(context);
    }


    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (mSelectionChangedListener != null) {
            mSelectionChangedListener.onSelectionChanged();
        }
    }


    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.paste:
                paste();
                return true;
            default:
                return super.onTextContextMenuItem(id);
        }
    }


    private void paste() {
        ClipboardManager clipboardManager =
                (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);

        if (!clipboardManager.hasPrimaryClip()) return;

        ClipData clip = clipboardManager.getPrimaryClip();

        String clipText = clip.getItemAt(0).coerceToText(getContext()).toString();
        if (BuildConfig.DEBUG) Log.d("clip text", clipText);

        StringTokenizer tokenizer = new StringTokenizer(clipText, "()+-*/", true);

        StringBuilder builder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            boolean matchFound = false;

            for (InputPattern inputPattern : InputPattern.values()) {
                Matcher matcher = inputPattern.getPattern().matcher(token);

                if (matcher.find()) {
                    matchFound = true;
                    switch (inputPattern) {
                        case DHMS:
                            long millis = Converter.toMillis(token);
                            builder.append(Converter.formatDurationHms(millis));
                            break;
                        default:
                            builder.append(token);
                            break;
                    } // end switch
                } // end if
            } // end for

            if (!matchFound) {
                Toast.makeText(
                        getContext(),
                        getContext().getString(R.string.toast_copy_error),
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
        }

        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();

        if (selectionStart == selectionEnd) {
            this.getText().insert(selectionStart, builder.toString());
        } else {
            this.getText().replace(selectionStart, selectionEnd, builder.toString());
        }
    }


    public void setSelectionChangeListener(SelectionChangedListener selectionChangeListener) {
        mSelectionChangedListener = selectionChangeListener;
    }


    public void setEditable(@Nullable Editable editable) {
        if (editable != null && !getText().toString().equals(editable.toString())) {
            super.setText(editable);
        }
    }


    public SelectionState getSelectionState() {
        return SelectionState.create(getSelectionStart(), getSelectionEnd());
    }


    public void setSelectionState(SelectionState state) {
        if (state != null && !getSelectionState().equals(state)) {
            setSelection(state.selectionStart(), state.selectionEnd());
        }
    }


    public interface SelectionChangedListener {

        void onSelectionChanged();
    }
}
