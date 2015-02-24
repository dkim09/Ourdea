package com.ourdea.ourdea.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.activities.AddEditTaskActivity;
import com.ourdea.ourdea.adapters.TaskListAdapter;
import com.ourdea.ourdea.api.TaskApi;
import com.ourdea.ourdea.api.models.TaskModel;

import org.json.JSONArray;

public class TaskListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_SECTION = "section";

    private String section;

    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;

    private ArrayAdapter<TaskModel> mAdapter;

    private TaskListContent taskListContent;

    public static TaskListFragment newInstance(String position) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public TaskListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TaskListAdapter(getActivity(), R.layout.item_task);

        if (getArguments() != null) {
            section = getArguments().getString(ARG_SECTION);
        }
    }

    private void loadTasks() {
        String taskListType = "me";
        switch (Integer.valueOf(section)) {
            case 1:
                taskListType = "me";
                break;
            case 2:
                taskListType = "todo";
                break;
            case 3:
                taskListType = "inprogress";
                break;
            case 4:
                taskListType = "completed";
                break;
        }

        TaskApi.getAll(taskListType, this.getActivity(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SERVER_SUCCESS", "TaskModel list retrieved");
                        taskListContent = new TaskListContent(response);
                        buildTaskList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "TaskModel list cannot be retrieved");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
        if (taskListContent != null) buildTaskList();
    }

    private void buildTaskList() {
        mAdapter.clear();
        for (TaskModel task : taskListContent.getTaskItems()) {
            mAdapter.add(task);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //if (null != mListener) {
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        //mListener.onFragmentInteraction(TaskListContent.ITEMS.get(position).id);
        //}
        Intent intent = new Intent(this.getActivity(), AddEditTaskActivity.class);
        String taskId = ((TaskModel) parent.getAdapter().getItem(position)).getId();
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }

    /**
     * The default name for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
