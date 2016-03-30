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
import android.widget.EditText;
import android.widget.Toast;

import kz.kuzovatov.pavel.robomanager.models.Robot;
import kz.kuzovatov.pavel.robomanager.utils.PreferenceManager;
import kz.kuzovatov.pavel.robomanager.utils.WebServiceCaller;

public class CreateRobotActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateRobotActivity";
    private WebServiceCaller webServiceCaller = WebServiceCaller.INSTANCE;
    //Url handling components
    private PreferenceManager preferenceManager = PreferenceManager.INSTANCE;
    private String url;
    private int waitingTime;
    //Controls
    private EditText robotName;
    private EditText robotType;
    private EditText robotYear;
    private Button saveRobot;
    //Entities
    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_robot);
        url = preferenceManager.getUrl(this);
        waitingTime = preferenceManager.getWaitingResponseTime(this);
        initFields();
        initButtons();
        initActionBar();
    }

    public void initFields(){
        robotName = (EditText) findViewById(R.id.editName);
        robotType = (EditText) findViewById(R.id.editType);
        robotYear = (EditText) findViewById(R.id.editYear);
    }

    public void initButtons(){
        saveRobot = (Button) findViewById(R.id.saveRobot);
        saveRobot.setOnClickListener(this);
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
            case R.id.saveRobot:
                int nameLength = robotName.getText().length();
                int typeLength = robotType.getText().length();
                int yearLength = robotYear.getText().length();
                if (nameLength > 0 && typeLength > 0 && yearLength > 0) {
                    if (nameLength > 255 || typeLength > 255 || yearLength > 255) {
                        Toast.makeText(this, "More than 255 symbols in fields are not allowed!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if (!String.valueOf(robotYear.getText()).matches("\\d+")) {
                        Toast.makeText(this, "Year must be a digit!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    robot = new Robot();
                    robot.setName(String.valueOf(robotName.getText()));
                    robot.setType(String.valueOf(robotType.getText()));
                    robot.setYear(Integer.parseInt(String.valueOf(robotYear.getText())));
                    saveRobot();
                    break;
                }
                Toast.makeText(this, "No empty fields required!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    public void saveRobot(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new SaveRobot().execute(url);
        } else {
            Log.i(TAG, "No network connection available.");
            Toast.makeText(CreateRobotActivity.this, "No network connection available!", Toast.LENGTH_LONG).show();
        }
    }

    private class SaveRobot extends AsyncTask<String, Void, String> {
        String response = null;

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return webServiceCaller.postCallWebService(urls[0], robot, waitingTime);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result==null){
                Toast.makeText(CreateRobotActivity.this, "No response from the server!", Toast.LENGTH_LONG).show();
            } else {
                response = result;
                Log.i(TAG, "Response: " + response);
                Intent intent = new Intent(CreateRobotActivity.this, RobotListActivity.class);
                startActivity(intent);
            }
        }
    }
}
