package im.aktive.aktive;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import im.aktive.aktive.api_requester.ATAPICallWrapper;
import im.aktive.aktive.helper.ATDateTimeUtils;
import im.aktive.aktive.manager.ATActivityManager;
import im.aktive.aktive.manager.ATUserActivityManager;
import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;

public class ATAddNewUserTodoActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener{
    private EditText mTodoNameEditText = null;
    private EditText mTodoDescriptionEditText = null;
    private TextView mDeadlineTextview = null;
    private ATActivity mActivity = null;
    private ATDatePickerFragment mFragment = null;
    private Date mDeadline = null;
    ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user_todo);
        mTodoNameEditText = (EditText)findViewById(R.id.new_todo_name_edittext);
        mTodoDescriptionEditText = (EditText)findViewById(R.id.new_todo_description_edittext);
        mDeadlineTextview = (TextView)findViewById(R.id.deadline_textview);
        Intent intent = getIntent();
        int activtyId = intent.getIntExtra("ActivityId", -1);
        if (activtyId != -1)
        {
            mActivity = ATActivityManager.getInstance().getActivity(activtyId);
        }
        if (mActivity != null)
        {
            mTodoNameEditText.setText(mActivity.getName());
            if (mActivity.getDescription() != null)
            {
                mTodoDescriptionEditText.setText(mActivity.getDescription());
            }
        }
        mDeadline = new Date();
        mDeadlineTextview.setText(ATDateTimeUtils.getDateTimeForDisplay(mDeadline));
        mDeadlineTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseDeadlineClicked();
            }
        });
        TextView cancelButton = (TextView)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClicked();
            }
        });
        TextView okButton = (TextView)findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkClicked();
            }
        });
    }

    private void onChooseDeadlineClicked() {
        displayDatePickupDialog();
    }

    private void onCancelClicked() {
        this.finish();
    }

    private void onOkClicked() {
        if (mActivity != null)
        {
            createTodoTask();
        } else {
            createActivity();
        }
    }

    private void createActivity() {
        String activityName = mTodoNameEditText.getText().toString();
        if (activityName.length() <= 0)
        {
            Toast.makeText(this, R.string.activity_name_too_short_notice, Toast.LENGTH_LONG).show();
            return;
        }
        String description = mTodoDescriptionEditText.getText().toString();
        ATActivityManager.getInstance().postActivity(activityName, description,
                new ATWrappedModelRequestCallback(mCallWrapper)
                {

                    @Override
                    public void onSuccess(Object object) {
                        mActivity = (ATActivity)object;
                        createTodoTask();
                    }

                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(ATAddNewUserTodoActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createTodoTask() {
        ATUserActivityManager.getInstance().postTodoUserActivity(mActivity, mDeadline,
                new ATWrappedModelRequestCallback(mCallWrapper){

                    @Override
                    public void onSuccess(Object object) {
                        ATAddNewUserTodoActivity.this.finish();
                    }

                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(ATAddNewUserTodoActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void displayDatePickupDialog()
    {
        if (mFragment == null)
        {
            mFragment = ATDatePickerFragment.newInstance(Calendar.getInstance(), null, Calendar.getInstance());
            mFragment.setListener(this);
        }
        mFragment.show(getSupportFragmentManager(), "datePicker");
        if (mDeadline != null)
        {
            // need to use an UTC calendar
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(mDeadline);
            mFragment.setCalendar(calendar);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mDeadline = calendar.getTime();
        mDeadlineTextview.setText(ATDateTimeUtils.getDateTimeForDisplay(mDeadline));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.atadd_new_user_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
