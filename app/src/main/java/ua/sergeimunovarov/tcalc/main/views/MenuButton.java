/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.widget.AppCompatImageButton;
import ua.sergeimunovarov.tcalc.R;
import ua.sergeimunovarov.tcalc.main.MainActivityViewModel;


public class MenuButton extends AppCompatImageButton implements View.OnClickListener {


    private MainActivityViewModel.MenuClickEvent mEvent;


    {
        setOnClickListener(this);
    }


    public MenuButton(Context context) {
        super(context);
    }


    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setMenuClickEvent(MainActivityViewModel.MenuClickEvent event) {
        mEvent = event;
    }


    @Override
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), this);
        popupMenu.inflate(R.menu.menu_more);
        popupMenu.setOnMenuItemClickListener(item -> {
            mEvent.setValue(item.getItemId());
            return true;
        });
        popupMenu.show();
    }
}
