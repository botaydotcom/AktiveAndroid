package im.aktive.aktive;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ATDatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener
{
    private DatePickerDialog.OnDateSetListener mListener = null;
    private DatePickerDialog datePickerDialog = null;
    private Calendar mCalendar = Calendar.getInstance();

    public void setListener(DatePickerDialog.OnDateSetListener listener)
    {
        mListener = listener;
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
                        DatePicker datePicker = datePickerDialog.getDatePicker();
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
        Calendar minLimit = GregorianCalendar.getInstance();
        datePickerDialog.getDatePicker().setMaxDate(minLimit.getTimeInMillis());
        minLimit.add(Calendar.YEAR, -100);
        datePickerDialog.getDatePicker().setMinDate(minLimit.getTimeInMillis());
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    }
}