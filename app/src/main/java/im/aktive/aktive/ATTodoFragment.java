package im.aktive.aktive;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.aktive.aktive.api_requester.ATAPICallWrapper;
import im.aktive.aktive.manager.ATUserActivityManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATModelUpdateCallback;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATUserActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ATTodoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ATTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ATTodoFragment extends Fragment implements ATModelUpdateCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ATAPICallWrapper apiCallWrapper = null;

    private OnFragmentInteractionListener mListener;

    private ListView mListView = null;
    private GeneralListAdapter mListAdapter = null;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ATTodoFragment newInstance() {
        ATTodoFragment fragment = new ATTodoFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ATTodoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mListView = (ListView) view.findViewById(R.id.list_todo);
        mListAdapter = new GeneralListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            apiCallWrapper = new ATAPICallWrapper();
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        apiCallWrapper.onStop();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ATUserActivityManager.getInstance().addObserver(this);
        updateView();
    }

    @Override
    public void onPause() {
        ATUserActivityManager.getInstance().removeObserver(this);
    }

    private void updateView() {
//        ATUser user = ATUserManager.getInstance().getCurrentUser();
//        ATActivity activity = new ATActivity(0, "Cook breakfast");
//        ATUserActivity userActivity = new ATUserActivity(0, user, activity);
//        ATActivity activity1 = new ATActivity(1, "Cook breakfast");
//        ATUserActivity userActivity1 = new ATUserActivity(1, user, activity1);
//        ATActivity activity2 = new ATActivity(2, "Learn to play the guitar or a longer name");
//        ATUserActivity userActivity2 = new ATUserActivity(2, user, activity2);

        List<ATUserActivity> listUserActivity = new ArrayList<ATUserActivity>();
//        listUserActivity.add(userActivity);
//        listUserActivity.add(userActivity1);
//        listUserActivity.add(userActivity2);

        listUserActivity.addAll(ATUserActivityManager.getInstance().getListTodoUserActivity());
        mListAdapter.refreshWithList(listUserActivity);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /*class UserActivityListAdapter extends ArrayAdapter<ATUserActivity> {
        int mViewResourceId;
        Context context;
        List<ATUserActivity> listUserActivity;
        int removedItem = 1;
        public UserActivityListAdapter(Context context, List<ATUserActivity> listEvent) {
            super(context, R.layout.list_todo_view_item, listEvent);
            this.context = context;
            this.listUserActivity = listEvent;
            this.mViewResourceId = R.layout.list_todo_view_item;
        }

        @Override
        public int getCount() {
            if (listUserActivity.size() == 0)
            {
                return 1;
            } else {
                return listUserActivity.size();
            }
        }

        @Override
        public ATUserActivity getItem(int position) {
            if (listUserActivity.size() == 0)
            {
                return null;
            } else {
                return listUserActivity.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView (final int position, View contentView, ViewGroup parent)
        {
            final ATUserActivity anItem = getItem(position);
            RelativeLayout itemView = null;
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            if (contentView == null ||
                    (!contentView.getTag().equals(Integer.valueOf(mViewResourceId)))){
                itemView = new RelativeLayout(getContext());
                li.inflate(mViewResourceId, itemView, true);
            } else
            {
                itemView = (RelativeLayout)contentView;
            }
            TextView nameTextView = (TextView)itemView.findViewById(R.id.item_name_tv);
            TextView deadlineTextView = (TextView)itemView.findViewById(R.id.item_deadline_tv);
            nameTextView.setText(anItem.getActivity().getName());
            deadlineTextView.setText(anItem.getDeadLineForDisplay());
            return itemView;
        }

        public void refreshWithList(
                List<ATUserActivity> listUserActivities) {
            this.listUserActivity.clear();
            this.listUserActivity.addAll(listUserActivities);
            this.notifyDataSetChanged();
        }
    }*/

    class GeneralListAdapter extends BaseAdapter {
        private static final int TYPE_TODO_ITEM = 0;
        private static final int TYPE_FINISHED_ITEM = 1;
        private static final int TYPE_EMPTY_LIST_ITEM = 2;
        private static final int TYPE_MAX_COUNT = 3;
        Context context;
        private ArrayList<Integer> listItemType;
        private ArrayList<Object> listItem;

        private int removedItemId = -1;

        public GeneralListAdapter(Context context) {
            this.context = context;
            listItem = new ArrayList<Object>();
            listItemType = new ArrayList<Integer>();
        }

        private void addItem(int type, Object object)
        {
            listItemType.add(Integer.valueOf(type));
            listItem.add(object);
        }

        public void refreshWithList(List<ATUserActivity> listUserActivities)
        {
            listItemType.clear();
            listItem.clear();
            for (ATUserActivity activity : listUserActivities)
            {
                if (removedItemId == activity.getId())
                {
                    addItem(TYPE_FINISHED_ITEM, activity);
                } else {
                    addItem(TYPE_TODO_ITEM, activity);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return listItemType.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return listItem.size();
        }
        @Override
        public Object getItem(int pos) {
            return listItem.get(pos);
        }
        @Override
        public long getItemId(int pos) {
            return pos;
        }
        @Override
        public View getView(final int position, View contentView,
                            ViewGroup parent) {
            int itemType = getItemViewType(position);
            if (contentView == null)
            {
                contentView = inflateViewAccordingToType(itemType);
            }
            Object item = getItem(position);
            populateContentView(contentView, itemType, item);
            return contentView;
        }

        private View inflateViewAccordingToType(int itemType) {
            LinearLayout itemView = null;
            itemView = new LinearLayout(context);
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(inflater);
            int viewResourceId = 0;
            switch (itemType) {
                case TYPE_TODO_ITEM:
                    viewResourceId = R.layout.list_todo_view_item;
                    break;
                case TYPE_FINISHED_ITEM:
                    viewResourceId = R.layout.list_todo_removed_view_item;
                    break;
                case TYPE_EMPTY_LIST_ITEM:
                    viewResourceId = R.layout.list_todo_removed_view_item;
                    break;
            }
            li.inflate(viewResourceId, itemView, true);
            return itemView;
        }

        private void populateContentView(View contentView, int itemType, Object item) {
            switch (itemType) {
                case TYPE_TODO_ITEM:
                    populateTodoViewItem(contentView, (ATUserActivity) item);
                    break;
                case TYPE_FINISHED_ITEM:
                    populateFinishedTodoViewItem(contentView, (ATUserActivity) item);
                    break;
            }
        }

        private void populateTodoViewItem(View itemView, ATUserActivity item) {
            TextView nameTextView = (TextView)itemView.findViewById(R.id.item_name_tv);
            TextView deadlineTextView = (TextView)itemView.findViewById(R.id.item_deadline_tv);
            nameTextView.setText(item.getActivity().getName());
            deadlineTextView.setText(item.getDeadLineForDisplay());
        }

        private void populateFinishedTodoViewItem(View itemView, ATUserActivity item) {
        }
    }

    @Override
    public void onModelUpdated() {
        updateView();
    }

}
