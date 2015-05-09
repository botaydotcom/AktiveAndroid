package im.aktive.aktive;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.aktive.aktive.api_requester.ATAPICallWrapper;
import im.aktive.aktive.manager.ATTagManager;
import im.aktive.aktive.manager.ATTagValueManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATTag;
import im.aktive.aktive.model.ATTagValue;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;

interface QuestionPageInteractionListener {
    void onAnswerChosen(int mTagId, int id);
}


public class ATSuggestionActivity extends ActionBarActivity implements  QuestionPageInteractionListener{
    private RelativeLayout mSuggestionPanel = null;
    private RelativeLayout mThinkingOverlayout = null;
    private ViewPager mQuestionPager = null;
    private QuestionPageAdapter mQuestionPagerAdapter = null;
    private List<ATTag> mTagList = null;
    Map<Integer, Integer> mMapTagAnswer = new HashMap<Integer, Integer>();
    private int mCurrentPage = 0;
    private ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        mTagList = ATTagManager.getInstance().getListTagForSuggestion();
        mQuestionPager = (ViewPager) findViewById(R.id.pager_questions);
        mQuestionPagerAdapter = new QuestionPageAdapter(getSupportFragmentManager());
        mQuestionPager.setAdapter(mQuestionPagerAdapter);
        mSuggestionPanel = (RelativeLayout)findViewById(R.id.suggestion_panel);
        mThinkingOverlayout = (RelativeLayout)findViewById(R.id.thinking_overlay_panel);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.atsuggestion, menu);
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

    @Override
    public void onAnswerChosen(int tagId, int tagValueId) {
        if (tagId > 0)
        {
            mMapTagAnswer.put(Integer.valueOf(tagId), Integer.valueOf(tagValueId));
        }
        if (mCurrentPage + 1 < mQuestionPagerAdapter.getCount())
        {
            mCurrentPage++;
            mQuestionPager.setCurrentItem(mCurrentPage);
        } else {
            showResult();
        }
    }

    private void showResult() {
        mSuggestionPanel.setVisibility(View.GONE);
        mThinkingOverlayout.setVisibility(View.VISIBLE);
        ATUserManager.getInstance().inputTag(mMapTagAnswer, new ATWrappedModelRequestCallback(mCallWrapper) {
            @Override
            public void onSuccess(Object object) {
                mSuggestionPanel.setVisibility(View.GONE);
                mThinkingOverlayout.setVisibility(View.GONE);
                List<ATActivity> listActivity = (List<ATActivity>) object;
                if (listActivity.size() == 0)
                {
                    Toast.makeText(ATSuggestionActivity.this, R.string.no_result_text, Toast.LENGTH_LONG).show();
                    mMapTagAnswer.clear();
                    mCurrentPage = 0;
                    mQuestionPager.setCurrentItem(mCurrentPage);
                    return;
                }
                ArrayList<Integer> listInteger = new ArrayList<Integer>();
                for (ATActivity activity : listActivity)
                {
                    listInteger.add(activity.getId());
                }
                Intent i = new Intent(ATSuggestionActivity.this, ATSuggestionResultActivity.class);
                i.putExtra("ListResult", listInteger);
                startActivity(i);
            }

            @Override
            public void onFailed(String msg) {
                mSuggestionPanel.setVisibility(View.VISIBLE);
                mThinkingOverlayout.setVisibility(View.GONE);
            }
        });
    }

    private class QuestionPageAdapter extends FragmentStatePagerAdapter {
        private int mVisibility = View.VISIBLE;
        private SparseArray<Fragment> mPageReferenceMap = null;

        public QuestionPageAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new SparseArray<Fragment>();
            for (int i = 0; i < mTagList.size(); i++)
            {
                ATTag tag = mTagList.get(i);
                FragmentQuestion questionPage = FragmentQuestion.newInstance(tag.getId());
                mPageReferenceMap.put(i, questionPage);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mPageReferenceMap.get(position);
        }

        @Override
        public int getCount() {
            // 4 default fragment include: intro / set gender / set birthday / finish
            return mTagList.size();
        }

        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }
    }

    public static class FragmentQuestion extends Fragment implements View.OnClickListener {
        QuestionPageInteractionListener mListener = null;
        ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();
        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            try {
                mListener = (QuestionPageInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement QuestionPageInteractionListener");
            }

        }

        private static String ARG_TAG = "tag_id";
        private int mTagId;
        private ATTag mTag;
        public FragmentQuestion()
        {
        }

        public static FragmentQuestion newInstance(int tagId) {
            FragmentQuestion fragment = new FragmentQuestion();
            Bundle args = new Bundle();
            args.putInt(ARG_TAG, tagId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mTagId = getArguments().getInt(ARG_TAG);
                mTag = ATTagManager.getInstance().getTag(mTagId);
            }
        }

        int min(int a, int b)
        {
            return (a < b) ? a : b;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_page_onboarding_question, container, false);
            TextView question = (TextView) rootView.findViewById(R.id.text_onboard_question);
            question.setText(mTag.getQuestion());
            List<ATTagValue> listAnswer = mTag.getTagValues();
            ATTagValue skipTagValue = ATTagValueManager.getInstance().getSkipTagValue();
            listAnswer.add(skipTagValue);
            int numRow = (listAnswer.size() + 1) / 2;
            LinearLayout listAnswerLayout = (LinearLayout)rootView.findViewById(R.id.list_answers);
            for (int i = 0; i < numRow; i++)
            {
                LinearLayout answerRow = new LinearLayout(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                answerRow.setLayoutParams(params);
                for (int j = i * 2; j < min(listAnswer.size(), i * 2 + 2); j++)
                {
                    ATTagValue tagValue = listAnswer.get(j);
                    TextView answer = new TextView(getActivity());
                    answer.setText(tagValue.getName());
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    answer.setLayoutParams(params1);
                    answer.setTag(tagValue);
                    answer.setOnClickListener(this);
                    answer.setGravity(Gravity.CENTER_HORIZONTAL);
                    answer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    answer.setTextColor(getResources().getColor(R.color.aktive_orange_normal));
                    answerRow.addView(answer);
                }
                listAnswerLayout.addView(answerRow);
            }
            return rootView;
        }

        @Override
        public void onClick(View view) {
            TextView answerView = (TextView)view;
            ATTagValue value = (ATTagValue)answerView.getTag();
            Toast.makeText(getActivity(), "Chose: " + value.getName(), Toast.LENGTH_LONG).show();
            mListener.onAnswerChosen(mTagId, value.getId());
        }
    }
}
