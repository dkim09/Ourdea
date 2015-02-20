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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.api.LabelApi;
import com.ourdea.ourdea.api.TaskApi;
import com.ourdea.ourdea.api.models.LabelModel;
import com.ourdea.ourdea.api.models.TaskModel;

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
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.label_autocomplete);
        final Button addTaskButton = (Button) findViewById(R.id.add_task);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText description = (EditText) findViewById(R.id.description);

        // Load task if needed
        TaskApi.get(taskId, context,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    TaskModel task = new TaskModel(response);
                    autoCompleteTextView.setText(task.getLabel());
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

        // Load labels
        LabelApi.getAll(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SERVER_SUCCESS", "Retrieved labels");

                        ArrayList<String> labels = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                LabelModel label = new LabelModel(response.getJSONObject(i));
                                labels.add(label.getName());
                            } catch (Exception exception) {
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, labels);
                        autoCompleteTextView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Cannot retrieve labels");
                    }
                });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameValue = name.getText().toString();
                final String descriptionValue = description.getText().toString();
                final String labelValue = autoCompleteTextView.getText().toString();

                if (taskId == null) {
                    TaskApi.create(nameValue, descriptionValue, labelValue, context,
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
                    TaskApi.update(taskId, nameValue, descriptionValue, labelValue, context,
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_task) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
