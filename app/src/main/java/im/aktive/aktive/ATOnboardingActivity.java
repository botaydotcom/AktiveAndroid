package im.aktive.aktive;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import im.aktive.aktive.R;
import im.aktive.aktive.api_requester.ATAPICallWrapper;
import im.aktive.aktive.manager.ATTagManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATGender;
import im.aktive.aktive.model.ATTag;
import im.aktive.aktive.model.ATTagValue;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;

interface OnboardingPageInteractionListener {
    void goToNextPage();
    void onAnswerChosen(int mTagId, int id);
}

public class ATOnboardingActivity extends ActionBarActivity implements OnboardingPageInteractionListener{
    private ATUser mUser = null;
    private List<ATTag> mTagList = null;
    private int mCurrentPage = 0;
    private ViewPager mImagePager = null;
    private OnboardingPageAdapter mImagePagerAdapter = null;
    private Map<Integer, Integer> mMapTagAnswer = new HashMap<Integer, Integer>();
    ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        mTagList = ATTagManager.getInstance().getListTag(true);
        mImagePager = (ViewPager) findViewById(R.id.pager_image_background);
        mImagePagerAdapter = new OnboardingPageAdapter(getSupportFragmentManager());
        mImagePager.setAdapter(mImagePagerAdapter);
        /*mImagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int page) {
                mCurrentPage = page;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.atonboarding, menu);
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
    public void goToNextPage()
    {
        if (mCurrentPage + 1 < mImagePagerAdapter.getCount())
        {
            mCurrentPage++;
            mImagePager.setCurrentItem(mCurrentPage);
        } else {
            ATUserManager.getInstance().inputTagFirstTime(mMapTagAnswer, new ATWrappedModelRequestCallback(mCallWrapper) {

                @Override
                public void onSuccess(Object object) {
                    Intent i = new Intent(ATOnboardingActivity.this, ATHomeActivity.class);
                    startActivity(i);
                    ATOnboardingActivity.this.finish();
                }

                @Override
                public void onFailed(String msg) {
                    Toast.makeText(ATOnboardingActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onAnswerChosen(int tagId, int tagValueId)
    {
        mMapTagAnswer.put(Integer.valueOf(tagId), Integer.valueOf(tagValueId));
    }

    private class OnboardingPageAdapter extends FragmentStatePagerAdapter {
        private int mVisibility = View.VISIBLE;
        private SparseArray<Fragment> mPageReferenceMap = null;
        private static final int PAGE_INTRO = 0;
        private static final int PAGE_SET_GENDER = 1;
        private static final int PAGE_SET_BIRTHDAY = 2;

        public OnboardingPageAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new SparseArray<Fragment>();
            FragmentPageOnboardingIntro introPage = FragmentPageOnboardingIntro.newInstance();
            mPageReferenceMap.put(PAGE_INTRO, introPage);
            FragmentPageOnboardingSetGender setGenderPage = FragmentPageOnboardingSetGender.newInstance();
            mPageReferenceMap.put(PAGE_SET_GENDER, setGenderPage);
            FragmentPageOnboardingSetBirthday setBirthdayPage = FragmentPageOnboardingSetBirthday.newInstance();
            mPageReferenceMap.put(PAGE_SET_BIRTHDAY, setBirthdayPage);
            FragmentPageOnboardingFinish finishPage = FragmentPageOnboardingFinish.newInstance();
            mPageReferenceMap.put(getCount() - 1, finishPage);
            for (int i = 0; i < mTagList.size(); i++)
            {
                ATTag tag = mTagList.get(i);
                FragmentPageOnboardingQuestion questionPage = FragmentPageOnboardingQuestion.newInstance(tag.getId());
                mPageReferenceMap.put(3 + i, questionPage);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mPageReferenceMap.get(position);
        }

        @Override
        public int getCount() {
            // 4 default fragment include: intro / set gender / set birthday / finish
            return mTagList.size() + 4;
        }

        public void destroyItem(View container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }
    }

    public static class FragmentPageOnboardingIntro extends Fragment {
        OnboardingPageInteractionListener mListener = null;
        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            try {
                mListener = (OnboardingPageInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement NewEventFragmentInterface");
            }

        }

        public FragmentPageOnboardingIntro()
        {
        }

        public static FragmentPageOnboardingIntro newInstance() {
            FragmentPageOnboardingIntro fragment = new FragmentPageOnboardingIntro();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_page_onboarding_intro, container, false);
            TextView startCommand = (TextView) rootView.findViewById(R.id.text_onboard_start_command);
            startCommand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.goToNextPage();
                }
            });
            return rootView;
        }
    }

    public static class FragmentPageOnboardingSetGender extends Fragment {
        OnboardingPageInteractionListener mListener = null;
        ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();
        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            try {
                mListener = (OnboardingPageInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement NewEventFragmentInterface");
            }

        }

        private ATUser mUser = null;
        public FragmentPageOnboardingSetGender()
        {
            mUser = ATUserManager.getInstance().getCurrentUser();
        }

        public static FragmentPageOnboardingSetGender newInstance() {
            FragmentPageOnboardingSetGender fragment = new FragmentPageOnboardingSetGender();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_page_onboarding_set_gender, container, false);
            TextView answerMale= (TextView) rootView.findViewById(R.id.text_answer_male);
            TextView answerFemale= (TextView) rootView.findViewById(R.id.text_answer_female);
            answerMale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUser.setBeingGender(ATGender.MALE);
                    updateUserGenderAndContinue();
                }
            });

            answerFemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUser.setBeingGender(ATGender.FEMALE);
                    updateUserGenderAndContinue();
                }
            });
            return rootView;
        }

        private void updateUserGenderAndContinue() {
            ATUserManager.getInstance().updateUserToServer(mUser, new ATWrappedModelRequestCallback(mCallWrapper) {

                @Override
                public void onSuccess(Object object) {
                    mListener.goToNextPage();
                }

                @Override
                public void onFailed(String msg) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static class FragmentPageOnboardingSetBirthday extends Fragment
            implements DatePickerDialog.OnDateSetListener{
        OnboardingPageInteractionListener mListener = null;
        ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();
        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            try {
                mListener = (OnboardingPageInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement NewEventFragmentInterface");
            }

        }
        private ATUser mUser = null;
        private ATDatePickerFragment mFragment = null;

        public FragmentPageOnboardingSetBirthday()
        {
            mUser = ATUserManager.getInstance().getCurrentUser();
        }

        public static FragmentPageOnboardingSetBirthday newInstance() {
            FragmentPageOnboardingSetBirthday fragment = new FragmentPageOnboardingSetBirthday();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_page_onboarding_set_birthday, container, false);
            TextView mBirthdayButton = (TextView)rootView.findViewById(R.id.text_select_date);
            mBirthdayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    displayDatePickupDialog();
                }
            });

            return rootView;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mUser.setBeingBirthday(calendar.getTime());
            ATUserManager.getInstance().updateUserToServer(mUser, new ATWrappedModelRequestCallback(mCallWrapper) {

                @Override
                public void onSuccess(Object object) {
                    mListener.goToNextPage();
                }

                @Override
                public void onFailed(String msg) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void displayDatePickupDialog()
        {
            if (mFragment == null)
            {
                mFragment = new ATDatePickerFragment();
                mFragment.setListener(this);
            }
            mFragment.show(getFragmentManager(), "datePicker");
            if (mUser.getBeingBirthday() != null)
            {
                // need to use an UTC calendar
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTime(mUser.getBeingBirthday());
                mFragment.setCalendar(calendar);
            }
        }
    }

    public static class FragmentPageOnboardingQuestion extends Fragment implements View.OnClickListener {
        OnboardingPageInteractionListener mListener = null;
        ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();
        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            try {
                mListener = (OnboardingPageInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement NewEventFragmentInterface");
            }

        }

        private static String ARG_TAG = "tag_id";
        private int mTagId;
        private ATTag mTag;
        public FragmentPageOnboardingQuestion()
        {
        }

        public static FragmentPageOnboardingQuestion newInstance(int tagId) {
            FragmentPageOnboardingQuestion fragment = new FragmentPageOnboardingQuestion();
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
            int numRow = listAnswer.size() / 2;
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
            mListener.goToNextPage();
        }
    }

    public static class FragmentPageOnboardingFinish extends Fragment {
        OnboardingPageInteractionListener mListener = null;
        ATAPICallWrapper mCallWrapper = new ATAPICallWrapper();
        @Override
        public void onAttach(Activity activity)
        {
            super.onAttach(activity);
            try {
                mListener = (OnboardingPageInteractionListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement NewEventFragmentInterface");
            }

        }

        public FragmentPageOnboardingFinish()
        {
        }

        public static FragmentPageOnboardingFinish newInstance() {
            FragmentPageOnboardingFinish fragment = new FragmentPageOnboardingFinish();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {

            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_page_onboarding_finish, container, false);
            TextView finishCommand = (TextView) rootView.findViewById(R.id.text_onboard_finish_command);
            finishCommand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.goToNextPage();
                }
            });
            return rootView;
        }
    }
}
