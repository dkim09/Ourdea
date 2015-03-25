package com.ourdea.ourdea.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.activities.AddEditTaskActivity;
import com.ourdea.ourdea.activities.TaskActivity;
import com.ourdea.ourdea.adapters.TaskListAdapter;
import com.ourdea.ourdea.dto.TaskDto;
import com.ourdea.ourdea.resources.TaskResource;

import org.json.JSONArray;

public class TaskListFragment extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String ARG_SECTION = "section";

    private String section;

    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;

    private ArrayAdapter<TaskDto> mAdapter;

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

    private void loadTasks(int sect) {
        String taskListType = "me";
        switch (sect) {
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

        TaskResource.getAll(taskListType, this.getActivity(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SERVER_SUCCESS", "Task list retrieved");
                        taskListContent = new TaskListContent(response);
                        buildTaskList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Task list cannot be retrieved");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks(Integer.valueOf(section));
    }

    private void buildTaskList() {
        mAdapter.clear();
        for (TaskDto task : taskListContent.getTaskItems()) {
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
        mListView.setOnItemLongClickListener(this);
        //mListView.setEmptyView(view.findViewById(android.R.id.empty));

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
        String taskId = ((TaskDto) parent.getAdapter().getItem(position)).getId();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        String [] options = new String[] {"Delete"};

        final TaskDto taskDto = mAdapter.getItem(position);
        final String taskStatus = taskDto.getStatus();

        switch (taskStatus) {
            case "todo":
                options = new String[] {"In Progress", "Done", "Delete"};
                break;
            case "completed":
                options = new String[] {"In Progress", "To Do", "Delete"};
                break;
            case "inprogress":
                options = new String[] {"Done", "To Do", "Delete"};
                break;
        }

        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final TaskActivity taskActivity = (TaskActivity) getActivity();
                switch (which) {
                    case 0:
                        if (taskStatus.equals("completed") || taskStatus.equals("todo")) {
                            TaskResource.updateStatus(taskDto.getId(), "inprogress", getActivity(), new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    Toast.makeText(getActivity(), "Task in progress", Toast.LENGTH_SHORT).show();
                                    taskActivity.getViewPager().setCurrentItem(2);
                                    mListView.setAdapter(mAdapter);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Task could not be moved to in progress", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (taskStatus.equals("inprogress")) {
                            TaskResource.updateStatus(taskDto.getId(), "completed", getActivity(), new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    taskActivity.getViewPager().setCurrentItem(3);
                                    mListView.setAdapter(mAdapter);
                                    Toast.makeText(getActivity(), "Task completed", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Task could not be moved to completed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case 1:
                        if (taskStatus.equals("completed") || taskStatus.equals("inprogress")) {
                            TaskResource.updateStatus(taskDto.getId(), "todo", getActivity(), new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    Toast.makeText(getActivity(), "Task marked to do", Toast.LENGTH_SHORT).show();
                                    taskActivity.getViewPager().setCurrentItem(1);
                                    mListView.setAdapter(mAdapter);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Task could not be moved to 'to do'", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (taskStatus.equals("todo")) {
                            TaskResource.updateStatus(taskDto.getId(), "completed", getActivity(), new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    Toast.makeText(getActivity(), "Task completed", Toast.LENGTH_SHORT).show();
                                    taskActivity.getViewPager().setCurrentItem(3);
                                    mListView.setAdapter(mAdapter);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Task could not be moved to completed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case 2:
                        TaskResource.delete(taskDto.getId(), getActivity(), new Response.Listener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
                                        loadTasks(Integer.valueOf(section));
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity(), "Task could not be deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                }
            }
        });

        dialogBuilder.show();

        return true;
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
