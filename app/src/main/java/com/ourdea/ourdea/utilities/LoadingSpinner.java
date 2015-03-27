package com.ourdea.ourdea.utilities;

import android.view.View;
import android.widget.ProgressBar;

public class LoadingSpinner {

    private ProgressBar spinner;

    public LoadingSpinner(View spinner) {
        this.spinner = (ProgressBar) spinner;
    }

    public void show() {
        spinner.setVisibility(View.VISIBLE);
    }

    public void hide() {
        spinner.setVisibility(View.GONE);
    }

}
