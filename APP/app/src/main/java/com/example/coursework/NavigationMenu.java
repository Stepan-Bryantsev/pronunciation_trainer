package com.example.coursework;

import android.app.Activity;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

public interface NavigationMenu {
    public void ClickMenu(View view);

    public void openDrawer(DrawerLayout drawerLayout);

    public void ClickLogo(View view);

    public void closeDrawer(DrawerLayout drawerLayout);

    public void ClickTrain(View view);

    public void ClickChosenTrain(View view);

    public void ClickHistory(View view);

    public void ClickAboutUs(View view);

    public void ClickLogout(View view);

    public void logout(final Activity activity);


    public void redirectActivity(Activity activity, Class aclass);
}
