package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.Api;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.adapters.ProjectListAdapter;
import com.ourdea.ourdea.dto.ProjectDto;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.ProjectResource;
import com.ourdea.ourdea.fragments.ProjectListContent;

import org.json.JSONArray;

import java.util.ArrayList;

public class ProjectListActivity extends ListActivity {

    final Context context = this;

    private AbsListView mListView;

    private ArrayAdapter<ProjectDto> mAdapter;

    private ProjectListContent projectListContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        mAdapter = new ProjectListAdapter(this, R.layout.item_project);
        loadProjects();
        setListAdapter(mAdapter);

    }

    private void loadProjects() {

        String email = ApiUtilities.Session.getEmail(this);

        ProjectResource.getAll(email, context,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SERVER_SUCCESS", "Project list retrieved");
                        projectListContent = new ProjectListContent(response);
                        buildProjectList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Project list cannot be retrieved");
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProjects();

    }

    private void buildProjectList() {
        mAdapter.clear();
        for (ProjectDto project : projectListContent.getProjectItems()) {
            mAdapter.add(project);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ProjectDto project = mAdapter.getItem(position);
        Long projectId = project.getProjectId();

        ApiUtilities.Session.storeProjectId(projectId, this);

        Intent activeProjectActivity = new Intent(ProjectListActivity.this, DashboardActivity.class);
        startActivity(activeProjectActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_project) {
            Intent intent = new Intent(this, AddProjectActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_join_project) {
            Intent intent = new Intent(this, ProjectActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
