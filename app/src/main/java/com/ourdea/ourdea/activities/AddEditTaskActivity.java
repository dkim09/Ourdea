package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.api.ApiUtilities;
import com.ourdea.ourdea.api.LabelApi;
import com.ourdea.ourdea.api.TaskApi;
import com.ourdea.ourdea.api.UserApi;
import com.ourdea.ourdea.dto.LabelDto;
import com.ourdea.ourdea.dto.TaskDto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddEditTaskActivity extends Activity {

    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Check if we are editing a task by getting the ID
        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");

        // Update title
        if (taskId != null) {
            setTitle("Edit Task");
        }

        final Context context = this;

        // Get references to UI elements
        final AutoCompleteTextView labelAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.label_autocomplete);
        final AutoCompleteTextView assigneeAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.assignee);
        final Button addTaskButton = (Button) findViewById(R.id.add_task);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText description = (EditText) findViewById(R.id.description);

        //assigneeAutoCompleteTextView.setText("me");
        // Set on click listener
        ImageView assignToMeImageView = (ImageView) findViewById(R.id.action_assign_to_me);
        assignToMeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assigneeAutoCompleteTextView.setText(ApiUtilities.Session.getUser(getApplicationContext()));
            }
        });

        // Load task if needed
        if (taskId != null) {
            TaskApi.get(taskId, context,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            TaskDto task = new TaskDto(response);
                            labelAutoCompleteTextView.setText(task.getLabel());
                            assigneeAutoCompleteTextView.setText(task.getAssignedTo());
                            name.setText(task.getName());
                            description.setText(task.getDescription());
                            Log.d("SERVER_SUCCESS", "Task loaded");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("SERVER_ERROR", "Task could not be loaded");
                        }
                    });
        }

        // Load labels
        LabelApi.getAll(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SERVER_SUCCESS", "Retrieved labels");

                        ArrayList<String> labels = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                LabelDto label = new LabelDto(response.getJSONObject(i));
                                labels.add(label.getName());
                            } catch (Exception exception) {
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, labels);
                        labelAutoCompleteTextView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Cannot retrieve labels");
                    }
                });

        // Load users
        UserApi.getAll(context,
            new Response.Listener<JSONArray>() {
                       @Override
                       public void onResponse(JSONArray response) {
                           Log.d("SERVER_SUCCESS", "Retrieved users");

                           ArrayList<String> users = new ArrayList<>();
                           for (int i = 0; i < response.length(); i++) {
                               try {
                                   users.add(response.getJSONObject(i).getString("email"));
                               } catch (Exception exception) {
                               }
                           }

                           ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, users);
                           assigneeAutoCompleteTextView.setAdapter(adapter);
                       }
                   },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("SERVER_ERROR", "Cannot retrieve users");
                }
            });

        // Set listener on save button
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameValue = name.getText().toString();
                final String descriptionValue = description.getText().toString();
                final String labelValue = labelAutoCompleteTextView.getText().toString();
                final String assigneeValue = assigneeAutoCompleteTextView.getText().toString();

                if (taskId == null) {
                    TaskApi.create(nameValue, descriptionValue, labelValue, assigneeValue, context,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    finish();
                                    Log.d("SERVER_SUCCESS", "Task created");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("SERVER_ERROR", "Task could not be created");
                                }
                            });
                } else {
                    TaskApi.update(taskId, nameValue, descriptionValue, labelValue, assigneeValue, context,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                finish();
                                Log.d("SERVER_SUCCESS", "Task saved");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("SERVER_ERROR", "Task could not be saved");
                            }
                        });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_task, menu);

        if (taskId != null) {
            menu.findItem(R.id.action_delete_task).setVisible(true);
            menu.findItem(R.id.action_done_task).setVisible(true);
            menu.findItem(R.id.action_in_progress_task).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_task) {
            TaskApi.delete(taskId, this,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.d("SERVER_SUCCESS", "Task deleted");
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Task could not be deleted");
                    }
                });
            return true;
        } else if (id == R.id.action_done_task) {
            TaskApi.updateStatus(taskId, "completed", this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SERVER_SUCCESS", "Task marked as done");
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Task cannot be marked as done");
                    }
                });
        } else if (id == R.id.action_in_progress_task) {
            TaskApi.updateStatus(taskId, "inprogress", this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SERVER_SUCCESS", "Task marked as inprogress");
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Task cannot be marked as inprogress");
                    }
                });
        }

        return super.onOptionsItemSelected(item);
    }
}
