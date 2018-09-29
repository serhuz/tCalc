/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;


public final class EditTextBindingAdapters {

    private EditTextBindingAdapters() {
        throw new AssertionError();
    }


    @InverseBindingAdapter(attribute = "selectionState", event = "selectionStateAttrChanged")
    public static SelectionState getSelectionState(CustomEditText editText) {
        return editText.getSelectionState();
    }


    @BindingAdapter("selectionStateAttrChanged")
    public static void setSelectionListener(CustomEditText editText,
                                            InverseBindingListener bindingListener) {
        CustomEditText.SelectionChangedListener selectionListener = bindingListener::onChange;
        editText.setSelectionChangeListener(selectionListener);
    }


    @InverseBindingAdapter(attribute = "editable", event = "editableAttrChanged")
    public static Editable getEditable(CustomEditText editText) {
        return editText.getText();
    }


    @BindingAdapter("editableAttrChanged")
    public static void setEditableListener(CustomEditText editText,
                                           InverseBindingListener bindingListener) {
        if (bindingListener != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }


                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bindingListener.onChange();
                }


                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
