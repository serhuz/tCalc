/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

import ua.sergeimunovarov.tcalc.Application;
import ua.sergeimunovarov.tcalc.BuildConfig;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.ops.Converter;
import ua.sergeimunovarov.tcalc.main.ops.ParserTokenType;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * An {@link EditText} subclass with custom font.
 */
public class CustomEditText extends EditText {

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
        init();
    }

    private void init() {
        Typeface tf = Application.getAppComponent().typefaceHolder().getTypeface();
        this.setTypeface(tf, Typeface.NORMAL);
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
            if (BuildConfig.DEBUG) Log.d("paste", String.format("current token: %s", token));
            boolean matchFound = false;

            for (ParserTokenType parserTokenType : ParserTokenType.values()) {
                Matcher matcher = parserTokenType.getPattern().matcher(token);

                if (matcher.find()) {
                    matchFound = true;
                    switch (parserTokenType) {
                        case TYPE_DHMS:
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
            if (BuildConfig.DEBUG) Log.d("paste action", "selection start=selection end");
            this.getText().insert(selectionStart, builder.toString());
        } else {
            if (BuildConfig.DEBUG) Log.d("paste action", "selection start!=selection end");
            this.getText().replace(selectionStart, selectionEnd, builder.toString());
        }
    }

}
