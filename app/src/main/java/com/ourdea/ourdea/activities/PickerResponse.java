package com.ourdea.ourdea.activities;

import android.widget.DatePicker;
import android.widget.TimePicker;

public interface PickerResponse {

    void onDateSet(DatePicker view, int year, int month, int day);

    void onTimeSet(TimePicker view, int hour, int minute);

}
