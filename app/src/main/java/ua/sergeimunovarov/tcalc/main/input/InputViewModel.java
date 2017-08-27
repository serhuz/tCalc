/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.input;

import android.view.View;
import android.widget.Button;

import ua.sergeimunovarov.tcalc.R;

public class InputViewModel {

    private final InputListener mInputListener;

    public InputViewModel(InputListener inputListener) {
        mInputListener = inputListener;
    }

    public void handleSymbolInput(View view) {
        Button inputButton = (Button) view;
        switch (inputButton.getId()) {
            case R.id.btn_multiply:
                mInputListener.onInputReceived("*");
                break;
            case R.id.btn_divide:
                mInputListener.onInputReceived("/");
                break;
            default:
                mInputListener.onInputReceived(inputButton.getText());
                break;
        }
    }

    public void handleEqualInput(View view) {
        mInputListener.onCalculateResult();
    }

    public void handleClearInput(View view) {
        mInputListener.onClearInput();
    }

    public void handleDeleteInput(View view) {
        mInputListener.onDeleteInput();
    }

    public void handleDoubleBracketsInput(View view) {
        mInputListener.onInsertBrackets();
    }

    public void handleMemoryStoreInput(View view) {
        mInputListener.onMemoryStore();
    }

    public void handleMemoryRecallInput(View view) {
        mInputListener.onMemoryRecall();
    }

    public void handleInsertPreviousAnswerInput(View view) {
        mInputListener.onInsertAnswer();
    }
}
