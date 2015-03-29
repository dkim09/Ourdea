package com.ourdea.ourdea.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.TaskDto;
import com.ourdea.ourdea.fragments.TaskListContent;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.LabelAnalyzer;
import com.ourdea.ourdea.resources.TaskResource;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;

public class DashboardActivity extends DrawerActivity {
    private WebView webView;
    private Context mContext;
    private ImageView imageView;

    private TaskListContent toDoTaskListContent;
    private TaskListContent inProgressTaskListContent;
    private TaskListContent doneTaskListContent;

    private int numInProgress;
    private int numToDo;
    private int numDone;
    private int numMyInProgress;
    private int numMyToDo;
    private int numMyDone;
    private String labels;
    private String toDoLabels;
    private String inProgressLabels;
    private String doneLabels;
    private LabelAnalyzer labelAnalyzer;
    private boolean finishLoadToDo;
    private boolean finishLoadInProgress;
    private boolean finishLoadDone;

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        numInProgress = 0;
        numToDo = 0;
        numDone = 0;
        numMyInProgress = 0;
        numMyToDo = 0;
        numMyDone = 0;
        labels ="";
        toDoLabels = "";
        inProgressLabels = "";
        doneLabels = "";
        labelAnalyzer = new LabelAnalyzer();
        finishLoadToDo = false;
        finishLoadToDo = false;
        finishLoadDone = false;

        setContentView(R.layout.activity_main);
        super.setActivity("Dashboard");
        super.onCreate(savedInstanceState);

        imageView = (ImageView)findViewById(R.id.image);
        webView = (WebView)findViewById(R.id.web);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setBackgroundColor(0);

        try {
            InputStream ims = getAssets().open("default_msg.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageView.setImageDrawable(d);
        }catch(IOException ex){
            return;
        }
        loadTasks();
    }

    public void loadTasks(){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "Loading Dashboard...", false, false);
        TaskResource.getAll("inprogress", mContext,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        inProgressTaskListContent = new TaskListContent(response);
                        numInProgress = inProgressTaskListContent.getTaskItems().size();

                        labelAnalyzer.addInProgressTaskList(inProgressTaskListContent);

                        Log.d("SERVER_SUCCESS", "In Progress tasks retrieved " + numInProgress);
                        for (TaskDto t : inProgressTaskListContent.getTaskItems()){
                            if (t.getAssignedTo().equals(ApiUtilities.Session.getEmail(mContext)))
                                numMyInProgress ++;
                        }

                        finishLoadInProgress = true;
                        if (finishLoadInProgress && finishLoadToDo && finishLoadDone){
                            labels =labelAnalyzer.getLabels();
                            toDoLabels = labelAnalyzer.getToDo();
                            inProgressLabels = labelAnalyzer.getInProgress();
                            doneLabels = labelAnalyzer.getDone();
                            Log.d("labelanalyzer", labels + " | " + toDoLabels + " | " + inProgressLabels + " | " +doneLabels);
                            //Check if there are any tasks
                            progressDialog.dismiss();
                            if ((numToDo + numInProgress + numDone) == 0) {
                                imageView.setVisibility(View.VISIBLE);
                                webView.setVisibility(View.GONE);
                            }else {
                                webView.loadUrl("file:///android_asset/chart.html");
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "In Progress tasks cannot be retrieved" );
                    }
                });

        TaskResource.getAll("completed", mContext,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        doneTaskListContent = new TaskListContent(response);
                        numDone = doneTaskListContent.getTaskItems().size();

                        labelAnalyzer.addDoneTaskList(doneTaskListContent);
                        Log.d("SERVER_SUCCESS", "Done tasks retrieved " + numDone);

                        for (TaskDto t : doneTaskListContent.getTaskItems()){
                            if (t.getAssignedTo().equals(ApiUtilities.Session.getEmail(mContext)))
                                numMyDone ++;
                        }

                        finishLoadDone = true;
                        if (finishLoadInProgress && finishLoadToDo && finishLoadDone){
                            labels =labelAnalyzer.getLabels();
                            toDoLabels = labelAnalyzer.getToDo();
                            inProgressLabels = labelAnalyzer.getInProgress();
                            doneLabels = labelAnalyzer.getDone();
                            Log.d("labelanalyzer", labels + " | " + toDoLabels + " | " + inProgressLabels + " | " +doneLabels);
                            //Check if there are any tasks
                            progressDialog.dismiss();
                            if ((numToDo + numInProgress + numDone) == 0) {
                                imageView.setVisibility(View.VISIBLE);
                                webView.setVisibility(View.INVISIBLE);
                            }else {
                                webView.loadUrl("file:///android_asset/chart.html");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Done tasks cannot be retrieved");
                    }
                });

        TaskResource.getAll("todo", mContext,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        toDoTaskListContent = new TaskListContent(response);
                        numToDo = toDoTaskListContent.getTaskItems().size();

                        labelAnalyzer.addToDoTaskList(toDoTaskListContent);
                        Log.d("SERVER_SUCCESS", "To Do tasks retrieved " + numToDo);

                        for (TaskDto t : toDoTaskListContent.getTaskItems()){
                            if (t.getAssignedTo().equals(ApiUtilities.Session.getEmail(mContext)))
                                numMyToDo ++;
                        }
                        finishLoadToDo = true;
                        if (finishLoadInProgress && finishLoadToDo && finishLoadDone){
                            labels =labelAnalyzer.getLabels();
                            toDoLabels = labelAnalyzer.getToDo();
                            inProgressLabels = labelAnalyzer.getInProgress();
                            doneLabels = labelAnalyzer.getDone();
                            Log.d("labelanalyzer", labels + " | " + toDoLabels + " | " + inProgressLabels + " | " +doneLabels);
                            //Check if there are any tasks
                            progressDialog.dismiss();
                            if ((numToDo + numInProgress + numDone) == 0) {
                                imageView.setVisibility(View.VISIBLE);
                                webView.setVisibility(View.INVISIBLE);
                            }else {
                                webView.loadUrl("file:///android_asset/chart.html");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "To Do tasks cannot be retrieved");
                    }
                });
    }


    public class WebAppInterface {

        @JavascriptInterface
        public int getNumInProgressTasks() {
            return numInProgress;
        }

        @JavascriptInterface
        public int getNumDoneTasks() {
            return numDone;
        }

        @JavascriptInterface
        public int getNumToDoTasks() {
            return numToDo;
        }

        @JavascriptInterface
        public int getNumMyInProgressTasks() {
            return numMyInProgress;
        }

        @JavascriptInterface
        public int getNumMyToDoTasks() {
            return numMyToDo;
        }

        @JavascriptInterface
        public int getNumMyDoneTasks() {
            return numMyDone;
        }

        @JavascriptInterface
        public String getLabels() {
            return labels;
        }

        @JavascriptInterface
        public String getToDoLabels() { return toDoLabels; }

        @JavascriptInterface
        public String getInProgressLabels() { return inProgressLabels; }

        @JavascriptInterface
        public String getDoneLabels() { return doneLabels; }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share_project).getActionProvider();
        mShareActionProvider.setShareIntent(doShare());

        // Return true to display menu
        return true;
    }

    public Intent doShare() {
        // populate the share intent with data
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, I'd like to invite you to my Ourdea project \"" +
                ApiUtilities.Session.getProjectName(this) + "\":" + "\n\nProjectID: " + ApiUtilities.Session.getProjectId(this)
                + "\nPassword: " + ApiUtilities.Session.getProjectPassword(this));
        return intent;
    }



}
