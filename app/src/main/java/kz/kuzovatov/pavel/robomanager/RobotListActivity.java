package kz.kuzovatov.pavel.robomanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kz.kuzovatov.pavel.robomanager.adapters.RobotAdapter;
import kz.kuzovatov.pavel.robomanager.models.Robot;
import kz.kuzovatov.pavel.robomanager.utils.PreferenceManager;
import kz.kuzovatov.pavel.robomanager.utils.RobotsResponseHandler;
import kz.kuzovatov.pavel.robomanager.utils.WebServiceCaller;

public class RobotListActivity extends AppCompatActivity {
    private static final String TAG = "RobotListActivity";
    //Url handling components
    private PreferenceManager preferenceManager = PreferenceManager.INSTANCE;
    private String url;
    private String urlOfSelected;
    private int waitingTime;
    //Controls
    private Toolbar toolbar;
    private Toolbar bottomToolbar;
    private EditText searchField;
    //Recycler view components
    private RecyclerView robotRecycler;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private RecyclerView.Adapter robotsAdapter;
    private List<Robot> robotList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_list);
        url = preferenceManager.getUrl(this);
        waitingTime = preferenceManager.getWaitingResponseTime(this);
        robotList = new ArrayList<>();
        searchField = (EditText) findViewById(R.id.search_field);
        initToolbar();
        initBottomToolbar();
        initRecyclerView();
        getRobots();
    }

    public void initRecyclerView(){
        robotRecycler = (RecyclerView) findViewById(R.id.robots_recycler_view);
        recyclerLayoutManager = new LinearLayoutManager(this);
        robotRecycler.setLayoutManager(recyclerLayoutManager);
        robotsAdapter = new RobotAdapter(robotList, this, robotRecycler);
        robotRecycler.setAdapter(robotsAdapter);
    }

    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        if(searchField.getText().length()>0) {
                            getRobotByName(String.valueOf(searchField.getText()));
                            break;
                        }
                        Toast.makeText(RobotListActivity.this, "Nothing was entered for search!", Toast.LENGTH_LONG).show();
                        break;
                    default: break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu_robot_list);
    }

    public void initBottomToolbar(){
        bottomToolbar = (Toolbar) findViewById(R.id.bottomToolbar);
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.refresh:
                        getRobots();
                        break;
                    case R.id.create:
                        intent = new Intent(RobotListActivity.this, CreateRobotActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.open:
                        for (Robot robot: robotList){
                            if (robot.isChecked()){
                                intent = new Intent(RobotListActivity.this, RobotDetailsActivity.class);
                                intent.putExtra("robot", robot);
                                startActivity(intent);
                            }
                        }
                        break;
                    case R.id.edit:
                        for (Robot robot: robotList){
                            if (robot.isChecked()){
                                intent = new Intent(RobotListActivity.this, EditRobotDetailsActivity.class);
                                intent.putExtra("robot", robot);
                                startActivity(intent);
                            }
                        }
                        break;
                    case R.id.delete:
                        for (Robot robot: robotList){
                            if (robot.isChecked()){
                                urlOfSelected = url + robot.getId();
                                deleteRobot();
                            }
                        }
                        break;
                    case R.id.settings:
                        intent = new Intent(RobotListActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    default: break;
                }
                return false;
            }
        });
        bottomToolbar.inflateMenu(R.menu.menu_bottom);
    }

    public void getRobots(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new GetRobots().execute(url);
        } else {
            Log.i(TAG, "No network connection available.");
            Toast.makeText(RobotListActivity.this, "No network connection available!", Toast.LENGTH_LONG).show();
        }
    }

    private class GetRobots extends AsyncTask<String, Void, String> {
        WebServiceCaller wsc = new WebServiceCaller();
        String response = "";

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return wsc.callWebService(urls[0], waitingTime);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result==null){
                Toast.makeText(RobotListActivity.this, "No response from the server!", Toast.LENGTH_LONG).show();
            } else {
                response = result;
                RobotsResponseHandler handler = new RobotsResponseHandler();
                robotList = handler.getObjectsResponse(response);
                robotsAdapter = new RobotAdapter(robotList, RobotListActivity.this, robotRecycler);
                robotRecycler.setAdapter(robotsAdapter);
            }
        }
    }

    public void getRobotByName(String name){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new GetRobotByName().execute(url + "search/" + name.replace(" ", "%20"));
        } else {
            Log.i(TAG, "No network connection available.");
            Toast.makeText(RobotListActivity.this, "No network connection available!", Toast.LENGTH_LONG).show();
        }
    }

    private class GetRobotByName extends AsyncTask<String, Void, String> {
        WebServiceCaller wsc = new WebServiceCaller();
        String response = "";

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return wsc.callWebService(urls[0], waitingTime);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result==null){
                Toast.makeText(RobotListActivity.this, "No response from the server!", Toast.LENGTH_LONG).show();
            } else {
                response = result;
                RobotsResponseHandler handler = new RobotsResponseHandler();
                Robot robot = handler.getObjectResponse(response);
                Intent intent = new Intent(RobotListActivity.this, RobotDetailsActivity.class);
                intent.putExtra("robot", robot);
                startActivity(intent);
            }
        }
    }

    public void deleteRobot(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DeleteRobot().execute(urlOfSelected);
        } else {
            Log.i(TAG, "No network connection available.");
            Toast.makeText(RobotListActivity.this, "No network connection available!", Toast.LENGTH_LONG).show();
        }
    }

    private class DeleteRobot extends AsyncTask<String, Void, String> {
        WebServiceCaller wsc = new WebServiceCaller();
        String response = "";

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return wsc.deleteCallWebService(urls[0], waitingTime);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Robot robot = null;
            if(result==null){
                Toast.makeText(RobotListActivity.this, "No response from the server!", Toast.LENGTH_LONG).show();
            } else {
                response = result;
                RobotsResponseHandler handler = new RobotsResponseHandler();
                if (response != null) robot = handler.getObjectResponse(response);
                if (robot != null) {
                    Toast.makeText(RobotListActivity.this, "Robot by name: " + String.valueOf(robot.getName()) + " was deleted!", Toast.LENGTH_LONG).show();
                }
                getRobots();
            }
        }
    }
}
