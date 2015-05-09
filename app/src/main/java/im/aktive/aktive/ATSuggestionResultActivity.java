package im.aktive.aktive;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import im.aktive.aktive.manager.ATActivityManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATActivity;

public class ATSuggestionResultActivity extends ActionBarActivity {
    private List<ATActivity> mListActivity = null;
    private int mCurrentIndex = 0;
    private TextView mLeadingTextView = null;
    private TextView mActivityNameTextView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_result);
        Intent i = getIntent();
        ArrayList<Integer> listActivityId = i.getIntegerArrayListExtra("ListResult");
        mListActivity = new ArrayList<ATActivity>();
        for (Integer id : listActivityId)
        {
            ATActivity activity = ATActivityManager.getInstance().getActivity(id);
            mListActivity.add(activity);
        }
        mActivityNameTextView = (TextView)findViewById(R.id.activity_name_text_view);
        mLeadingTextView = (TextView)findViewById(R.id.leading_text_view);
        TextView acceptButton = (TextView)findViewById(R.id.accept_button);
        TextView rejectButton = (TextView)findViewById(R.id.reject_button);
        TextView deferButton = (TextView)findViewById(R.id.defer_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ATUserManager.getInstance().postSuggestionResult(activity, ATActivityAction.CHOSEN);
//                Intent i = new Intent(ATSuggestionResultActivity.this, ATAddNewUserTodoActivity.class);
//                ATActivity activity = mListActivity.get(mCurrentIndex);
//                i.putExtra("ActivityId", activity.getId());
//                startActivity(i);
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex++;
                showSuggestion();
            }
        });
        deferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex++;
                showSuggestion();
            }
        });
        TextView noChoiceButton = (TextView)findViewById(R.id.no_choice_button);
        noChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ATSuggestionResultActivity.this, ATAddNewUserTodoActivity.class);
                i.putExtra("ActivityId", -1);
                startActivity(i);
                ATSuggestionResultActivity.this.finish();
            }
        });
        showSuggestion();
    }

    private void showSuggestion(){
        if (mCurrentIndex == mListActivity.size())
        {
            showNoMoreResult();
            return;
        } else {
            ATActivity activity = mListActivity.get(mCurrentIndex);
            mActivityNameTextView.setText(activity.getName());
            String[] leadStringText = getResources().getStringArray(R.array.suggestion_leading_text);
            Random random = new Random();
            int randomId = random.nextInt(leadStringText.length);
            String leadString = leadStringText[randomId];
            mLeadingTextView.setText(leadString);
        }

    }

    private void showNoMoreResult() {
        RelativeLayout resultPanel = (RelativeLayout)findViewById(R.id.result_panel);
        resultPanel.setVisibility(View.GONE);
        RelativeLayout noResultOverlayPanel = (RelativeLayout)findViewById(R.id.no_result_overlay_panel);
        noResultOverlayPanel.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.atsuggestion_result, menu);
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
