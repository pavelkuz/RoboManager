package kz.kuzovatov.pavel.robomanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import kz.kuzovatov.pavel.robomanager.models.Robot;
import kz.kuzovatov.pavel.robomanager.utils.PreferenceManager;
import kz.kuzovatov.pavel.robomanager.utils.RobotsResponseHandler;
import kz.kuzovatov.pavel.robomanager.utils.WebServiceCaller;

public class RobotDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RobotDetailsActivity";
    //Url handling components
    private PreferenceManager preferenceManager = PreferenceManager.INSTANCE;
    private String url;
    private int waitingTime;
    //Controls
    private TextView robotName;
    private TextView robotType;
    private TextView robotYear;
    private Button editRobot;
    private Button deleteRobot;
    //Entities
    private Robot robot;
    private int robotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_details);
        url = preferenceManager.getUrl(this);
        waitingTime = preferenceManager.getWaitingResponseTime(this);
        Bundle bundle = getIntent().getExtras();
        robotId = bundle.getInt("robotId");
        robot = bundle.getParcelable("robot");
        initActionBar();
        initButtons();
        initTextViews();
    }

    public void initButtons(){
        editRobot = (Button) findViewById(R.id.editRobotBtn);
        editRobot.setOnClickListener(this);
        deleteRobot = (Button) findViewById(R.id.deleteRobotBtn);
        deleteRobot.setOnClickListener(this);
    }

    public void initTextViews(){
        robotName = (TextView) findViewById(R.id.rName);
        robotType = (TextView) findViewById(R.id.rType);
        robotYear = (TextView) findViewById(R.id.rYear);
        if (robotId != 0){
            url = url + robotId;
            getRobot();
        } else {
            if (robot != null) {
                url = url + robot.getId();
                robotName.setText(String.format(this.getString(R.string.robot_name), robot.getName()));
                robotType.setText(String.format(this.getString(R.string.robot_type), robot.getType()));
                robotYear.setText(String.format(this.getString(R.string.robot_year), robot.getYear()));
            }
        }
    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deleteRobotBtn:
                deleteRobot();
                break;
            case R.id.editRobotBtn:
                Intent intent = new Intent(RobotDetailsActivity.this, EditRobotDetailsActivity.class);
                intent.putExtra("robot", robot);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void deleteRobot(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DeleteRobot().execute(url);
        } else {
            Log.i(TAG, "No network connection available.");
            Toast.makeText(RobotDetailsActivity.this, "No network connection available!", Toast.LENGTH_LONG).show();
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
            if(result==null){
                Toast.makeText(RobotDetailsActivity.this, "No response from the server!", Toast.LENGTH_LONG).show();
            } else {
                response = result;
                RobotsResponseHandler handler = new RobotsResponseHandler();
                if (response != null) robot = handler.getObjectResponse(response);
                if (robot != null) {
                    Toast.makeText(RobotDetailsActivity.this, "Robot by name: " + String.valueOf(robot.getName()) + " was deleted!", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(RobotDetailsActivity.this, RobotListActivity.class);
                startActivity(intent);
            }
        }
    }

    public void getRobot(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new GetRobot().execute(url);
        } else {
            Log.i(TAG, "No network connection available.");
            Toast.makeText(RobotDetailsActivity.this, "No network connection available!", Toast.LENGTH_LONG).show();
        }
    }

    private class GetRobot extends AsyncTask<String, Void, String> {
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
                Toast.makeText(RobotDetailsActivity.this, "No response from the server!", Toast.LENGTH_LONG).show();
            } else {
                response = result;
                RobotsResponseHandler handler = new RobotsResponseHandler();
                robot = handler.getObjectResponse(response);
                if (robot != null) {
                    robotName.setText(String.format(RobotDetailsActivity.this.getString(R.string.robot_name), robot.getName()));
                    robotType.setText(String.format(RobotDetailsActivity.this.getString(R.string.robot_type), robot.getType()));
                    robotYear.setText(String.format(RobotDetailsActivity.this.getString(R.string.robot_year), robot.getYear()));
                }
            }
        }
    }
}
