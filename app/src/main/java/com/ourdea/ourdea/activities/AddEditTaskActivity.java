package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.LabelDto;
import com.ourdea.ourdea.dto.TaskDto;
import com.ourdea.ourdea.fragments.DatePickerFragment;
import com.ourdea.ourdea.fragments.TimePickerFragment;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.LabelResource;
import com.ourdea.ourdea.resources.ProjectResource;
import com.ourdea.ourdea.resources.TagResource;
import com.ourdea.ourdea.resources.TaskResource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddEditTaskActivity extends Activity implements PickerResponse {

    private String taskId;

    private ArrayList<String> users;

    private Calendar taskDueDateTime = Calendar.getInstance();

    private String status;

    private ArrayList<String> usersExceptMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Check if we are editing a task by getting the ID
        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");

        final Context context = this;

        // Get references to UI elements
        final AutoCompleteTextView labelAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.label_autocomplete);
        final AutoCompleteTextView assigneeAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.assignee);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText description = (EditText) findViewById(R.id.description);
        final EditText date = (EditText) findViewById(R.id.select_date);
        final EditText time = (EditText) findViewById(R.id.select_time);
        final Button saveTaskButton = (Button) findViewById(R.id.save_task);

        // Update title and unhide save task button if we are editting a task
        if (taskId != null) {
            setTitle("Edit Task");
            saveTaskButton.setVisibility(View.VISIBLE);
        }

        // Set listener on save task button
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDto taskToUpdate = new TaskDto(
                        name.getText().toString(),
                        description.getText().toString(),
                        assigneeAutoCompleteTextView.getText().toString(),
                        labelAutoCompleteTextView.getText().toString(),
                        taskDueDateTime.getTimeInMillis(),
                        status);

                TaskResource.update(taskId, taskToUpdate, context, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        finish();
                        Toast.makeText(AddEditTaskActivity.this, "Task saved", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddEditTaskActivity.this, "Task could not be saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Set on click listener
        ImageView assignToMeImageView = (ImageView) findViewById(R.id.action_assign_to_me);
        assignToMeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assigneeAutoCompleteTextView.setText(ApiUtilities.Session.getEmail(getApplicationContext()));
            }
        });

        // Load task if needed
        if (taskId != null) {
            TaskResource.get(taskId, context,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            TaskDto task = new TaskDto(response);
                            labelAutoCompleteTextView.setText(task.getLabel());
                            assigneeAutoCompleteTextView.setText(task.getAssignedTo());
                            name.setText(task.getName());
                            description.setText(task.getDescription());
                            taskDueDateTime.setTimeInMillis(task.getDueDate());
                            setFormattedDateFromCalendar();
                            setFormattedTimeFromCalendar();
                            status = task.getStatus();

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
        LabelResource.getAll(this,
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
        ProjectResource.getMembers(ApiUtilities.Session.getProjectId(context), context,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SERVER_SUCCESS", "Retrieved users");

                        users = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                users.add(response.getJSONObject(i).getString("email"));
                            } catch (Exception exception) {
                            }
                        }
                        usersExceptMe = new ArrayList<>(users);
                        usersExceptMe.remove(ApiUtilities.Session.getEmail(getApplicationContext()));

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_task, menu);

        if (taskId != null) {
            menu.findItem(R.id.action_delete_task).setVisible(true);
            menu.findItem(R.id.action_done_task).setVisible(true);
            menu.findItem(R.id.action_in_progress_task).setVisible(true);
            menu.findItem(R.id.action_tag).setVisible(true);
            menu.findItem(R.id.action_save_task).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_task) {
            TaskResource.delete(taskId, this,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Toast.makeText(AddEditTaskActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
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
            TaskResource.updateStatus(taskId, "completed", this,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(AddEditTaskActivity.this, "Task completed", Toast.LENGTH_SHORT).show();
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
            TaskResource.updateStatus(taskId, "inprogress", this,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(AddEditTaskActivity.this, "Task in progress", Toast.LENGTH_SHORT).show();
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
        } else if (id == R.id.action_tag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tag user for attention");

            final View dialogView = View.inflate(this, R.layout.dialog_tag_user, null);
            final AutoCompleteTextView userAutoFill = (AutoCompleteTextView) dialogView.findViewById(R.id.user_autofill);
            final EditText message = (EditText) dialogView.findViewById(R.id.message);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersExceptMe);
            userAutoFill.setAdapter(adapter);
            builder.setView(dialogView);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Tag", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TagResource.tag(userAutoFill.getText().toString(), Long.parseLong(taskId), message.getText().toString(), getApplicationContext(),
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    Toast.makeText(getApplicationContext(), "User tagged", Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Could not tag user", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            builder.show();
        } else if (id == R.id.action_save_task) {
            Context context = this;
            // Get references to UI elements
            final AutoCompleteTextView labelAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.label_autocomplete);
            final AutoCompleteTextView assigneeAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.assignee);
            final EditText name = (EditText) findViewById(R.id.name);
            final EditText description = (EditText) findViewById(R.id.description);

            final String nameValue = name.getText().toString();
            final String descriptionValue = description.getText().toString();
            final String labelValue = labelAutoCompleteTextView.getText().toString();
            final String assigneeValue = assigneeAutoCompleteTextView.getText().toString();

            if (taskId == null) {
                TaskDto newTask = new TaskDto(nameValue, descriptionValue, assigneeValue, labelValue, taskDueDateTime.getTimeInMillis(), "todo");
                TaskResource.create(newTask, context,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(AddEditTaskActivity.this, "Task created", Toast.LENGTH_SHORT).show();
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
                TaskDto taskToUpdate = new TaskDto(nameValue, descriptionValue, assigneeValue, labelValue, taskDueDateTime.getTimeInMillis(), "todo");
                TaskResource.update(taskId, taskToUpdate, context,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                finish();
                                Toast.makeText(AddEditTaskActivity.this, "Task saved", Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        newFragment.delegate = this;
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
        newFragment.delegate = this;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        taskDueDateTime.set(Calendar.YEAR, year);
        taskDueDateTime.set(Calendar.MONTH, month);
        taskDueDateTime.set(Calendar.DAY_OF_MONTH, day);

        setFormattedDateFromCalendar();
    }

    private void setFormattedDateFromCalendar() {
        SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
        String formattedDateString = formatter.format(taskDueDateTime.getTime());

        EditText editText = (EditText) findViewById(R.id.select_date);
        editText.setText(formattedDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        taskDueDateTime.set(Calendar.HOUR_OF_DAY, hour);
        taskDueDateTime.set(Calendar.MINUTE, minute);

        setFormattedTimeFromCalendar();
    }

    private void setFormattedTimeFromCalendar() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        String formattedTimeString = formatter.format(taskDueDateTime.getTime());

        EditText editText = (EditText) findViewById(R.id.select_time);
        editText.setText(formattedTimeString);
    }
}
