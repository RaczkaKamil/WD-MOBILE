package com.wsiz.wirtualny.ui.kalendarz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.gms.plus.PlusOneButton;
import com.wsiz.wirtualny.R;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class KalendarzFragment extends Fragment {
    private CalendarView mCalendarView;
    private List<EventDay> mEventDays = new ArrayList<>();
    private Calendar lastSelectedCalendar = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_kalendarz, container, false);
        mCalendarView = root.findViewById(R.id.calendarView);
        // Calendar saturday;
        Calendar poniedzialek;
        Calendar wtorek;
        Calendar sroda;
        Calendar czwartek;
        Calendar niedziela;

        Calendar poniedzialek2;
        Calendar wtorek2;
        Calendar sroda2;
        Calendar czwartek2;
        Calendar niedziela2;
        List<Calendar> weekends = new ArrayList<>();
        int weeks = 500;
        Date date = new Date();
        for (int i = 0; i < (weeks * 7) ; i = i + 7) {

        poniedzialek = Calendar.getInstance();
        poniedzialek.add(Calendar.DAY_OF_YEAR, (Calendar.MONDAY - poniedzialek.get(Calendar.DAY_OF_WEEK) + i));

        wtorek = Calendar.getInstance();
        wtorek.add(Calendar.DAY_OF_YEAR, (Calendar.TUESDAY - wtorek.get(Calendar.DAY_OF_WEEK) + i));

        sroda = Calendar.getInstance();
        sroda.add(Calendar.DAY_OF_YEAR, (Calendar.WEDNESDAY - sroda.get(Calendar.DAY_OF_WEEK) + i));

        czwartek = Calendar.getInstance();
        czwartek.add(Calendar.DAY_OF_YEAR, (Calendar.THURSDAY - czwartek.get(Calendar.DAY_OF_WEEK) + i));

        niedziela = Calendar.getInstance();
        niedziela.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - niedziela.get(Calendar.DAY_OF_WEEK) + i));

        weekends.add(poniedzialek);
        weekends.add(wtorek);
        weekends.add(sroda);
        weekends.add(czwartek);
        weekends.add(niedziela);
    }

        for (int i = 0; i < (weeks * 7) ; i = i + 7) {

            poniedzialek2 = Calendar.getInstance();
            poniedzialek2.add(Calendar.DAY_OF_YEAR, (Calendar.MONDAY - poniedzialek2.get(Calendar.DAY_OF_WEEK) - i));

            wtorek2 = Calendar.getInstance();
            wtorek2.add(Calendar.DAY_OF_YEAR, (Calendar.TUESDAY - wtorek2.get(Calendar.DAY_OF_WEEK) - i));

            sroda2 = Calendar.getInstance();
            sroda2.add(Calendar.DAY_OF_YEAR, (Calendar.WEDNESDAY - sroda2.get(Calendar.DAY_OF_WEEK) - i));

            czwartek2 = Calendar.getInstance();
            czwartek2.add(Calendar.DAY_OF_YEAR, (Calendar.THURSDAY - czwartek2.get(Calendar.DAY_OF_WEEK) - i));

            niedziela2 = Calendar.getInstance();
            niedziela2.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - niedziela2.get(Calendar.DAY_OF_WEEK) - i));

            weekends.add(poniedzialek2);
            weekends.add(wtorek2);
            weekends.add(sroda2);
            weekends.add(czwartek2);
            weekends.add(niedziela2);
        }

    Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);
        mCalendarView.setDisabledDays(Arrays.asList(disabledDays));
        return root;
}


}
