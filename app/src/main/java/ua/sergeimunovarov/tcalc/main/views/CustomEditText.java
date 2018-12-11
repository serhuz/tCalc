/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;
import ua.sergeimunovarov.tcalc.BuildConfig;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.ops.Converter;
import ua.sergeimunovarov.tcalc.main.ops.InputPattern;

import static android.content.Context.CLIPBOARD_SERVICE;


public class CustomEditText extends AppCompatEditText {

    private SelectionChangedListener mSelectionChangedListener;
    private final int mSpanColor;


    public CustomEditText(Context context) {
        super(context);
    }


    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    {
        mSpanColor = ResourcesCompat.getColor(getResources(), R.color.blue, getContext().getTheme());
    }


    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (mSelectionChangedListener != null) {
            mSelectionChangedListener.onSelectionChanged();
        }

        if (selStart == selEnd && selStart > 0 && getText().length() > 1) {
            post(() -> {
                ForegroundColorSpan[] spans = getText().getSpans(0, getText().length(), ForegroundColorSpan.class);
                for (ForegroundColorSpan span : spans) {
                    getText().removeSpan(span);
                }
                if (getText().charAt(selStart - 1) == '(') {
                    int closingPosition = findClosingPosition(getText().toString().toCharArray(), selStart - 1);
                    if (closingPosition > -1) {
                        setColorSpan(selStart - 1, selStart);
                        setColorSpan(closingPosition, closingPosition + 1);
                    }
                } else if (getText().charAt(selStart - 1) == ')') {
                    int openPosition = findOpenPosition(getText().toString().toCharArray(), selStart - 1);
                    if (openPosition > -1) {
                        setColorSpan(selStart - 1, selStart);
                        setColorSpan(openPosition, openPosition + 1);
                    }
                }
            });
        }
    }


    private void setColorSpan(int start, int end) {
        getText().setSpan(new ForegroundColorSpan(mSpanColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public int findClosingPosition(char[] text, int openPos) {
        int counter = 1;
        for (int i = openPos + 1; i < text.length; i++) {
            char c = text[i];
            if (c == '(') counter++;
            else if (c == ')') counter--;
            if (counter == 0) return i;
        }
        return -1;
    }


    public int findOpenPosition(char[] text, int closePos) {
        int counter = 1;
        for (int i = closePos - 1; i >= 0; i--) {
            char c = text[i];
            if (c == ')') counter++;
            else if (c == '(') counter--;
            if (counter == 0) return i;
        }
        return -1;
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
