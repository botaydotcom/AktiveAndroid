package im.aktive.aktive;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import im.aktive.aktive.manager.ATTagManager;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ATDatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener
{
    private static final String ARG_CURRENT_DATE = "CurrentDate";
    private static final String ARG_MAX_DATE = "MaxDate";
    private static final String ARG_MIN_DATE = "MinDate";
    private DatePickerDialog.OnDateSetListener mListener = null;
    private DatePickerDialog datePickerDialog = null;
    private Calendar mCalendar = null;
    private DatePicker datePicker;
    private Calendar mMaxLimit = null;
    private Calendar mMinLimit = null;

    public void setListener(DatePickerDialog.OnDateSetListener listener)
    {
        mListener = listener;
    }

    public static ATDatePickerFragment newInstance(Calendar currentDate,
            Calendar maxCalendar, Calendar minCalendar)
    {
        ATDatePickerFragment fragment = new ATDatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENT_DATE, currentDate);
        args.putSerializable(ARG_MAX_DATE, maxCalendar);
        args.putSerializable(ARG_MIN_DATE, minCalendar);
        fragment.setArguments(args);
        return fragment;
    }

    private void setPickerToDate(Calendar calendar)
    {
        if (datePickerDialog != null)
        {
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog.updateDate(year, month, day);
        }
    }

    public void setDate(Date date)
    {
        mCalendar.setTime(date);
        setPickerToDate(mCalendar);
    }

    public void setCalendar(Calendar calendar)
    {
        mCalendar = calendar;
        setPickerToDate(mCalendar);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCalendar = (Calendar)getArguments().getSerializable(ARG_CURRENT_DATE);
            mMaxLimit = (Calendar)getArguments().getSerializable(ARG_MAX_DATE);
            mMinLimit = (Calendar)getArguments().getSerializable(ARG_MIN_DATE);
        }
        if (mCalendar == null)
        {
            mCalendar = Calendar.getInstance();
        }
        if (mMinLimit == null)
        {
            mMinLimit = Calendar.getInstance();
            mMinLimit.add(Calendar.YEAR, -100);
        }
        if (mMaxLimit == null)
        {
            mMaxLimit = Calendar.getInstance();
            mMaxLimit.add(Calendar.YEAR, 10);
        }
        // Use the current date as the default date in the picker
        final Calendar calendar = mCalendar;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.set_action),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker = getDatePicker();
                        mListener.onDateSet(datePicker,
                                datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        dialog.dismiss();
                    }
                });
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel_action),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final Calendar maxLimit = mMaxLimit;
        final Calendar minLimit = mMinLimit;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePickerDialog.getDatePicker().setMaxDate(maxLimit.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(minLimit.getTimeInMillis());
        } else {
            final int minYear = minLimit.get(Calendar.YEAR);
            final int minMonth = minLimit.get(Calendar.MONTH);
            final int minDay = minLimit.get(Calendar.DAY_OF_MONTH);

            final int maxYear = maxLimit.get(Calendar.YEAR);
            final int maxMonth = maxLimit.get(Calendar.MONTH);
            final int maxDay = maxLimit.get(Calendar.DAY_OF_MONTH);

            getDatePicker().init(year, month, day,
                    new DatePicker.OnDateChangedListener() {

                        public void onDateChanged(DatePicker view, int year,
                                                  int month, int day) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, month, day);
                            if (minLimit.after(newDate)) {
                                view.init(minYear, minMonth, minDay, this);
                            }
                            if (maxLimit.before(calendar)) {
                                view.init(maxYear, maxMonth, maxDay, this);
                            }
                        }
                    }
            );
        }
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    }

    public DatePicker getDatePicker() {
        if (datePicker == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // (picker is a DatePicker)
                datePicker = datePickerDialog.getDatePicker();
            } else {
                Field mDatePickerField = null;
                try {
                    mDatePickerField = datePickerDialog.getClass().getDeclaredField("mDatePicker");
                    mDatePickerField.setAccessible(true);
                    datePicker = (DatePicker) mDatePickerField.get(datePickerDialog);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return datePicker;
    }
}