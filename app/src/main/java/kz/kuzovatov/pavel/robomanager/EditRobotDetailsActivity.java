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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kz.kuzovatov.pavel.robomanager.models.Robot;
import kz.kuzovatov.pavel.robomanager.utils.PreferenceManager;
import kz.kuzovatov.pavel.robomanager.utils.WebServiceCaller;

public class EditRobotDetailsActivity extends AppCompatActivity {
    private static final String TAG = "EditRobotDetAct";
    //Url handling components
    private PreferenceManager preferenceManager = PreferenceManager.INSTANCE;
    private String url;
    private int waitingTime;
    //Controls
    private EditText robotName;
    private EditText robotType;
    private EditText robotYear;
    private Button editRobot;
    //Entities
    private Robot robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_robot_details);
        initActionBar();
        Bundle bundle = getIntent().getExtras();
        robot = bundle.getParcelable("robot");
        initEditTextViews();
        initButtons();
        url = preferenceManager.getUrl(this);
        waitingTime = preferenceManager.getWaitingResponseTime(this);
    }

    public void initButtons(){
        editRobot = (Button) findViewById(R.id.editSubmit);
        editRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.editSubmit:
                        int nameLength = String.valueOf(robotName.getText()).length();
                        int typeLength = String.valueOf(robotType.getText()).length();
                        int yearLength = String.valueOf(robotYear.getText()).length();

                        if (nameLength > 0 && typeLength > 0 && yearLength > 0) {
                            if (nameLength > 255 || typeLength > 255 || yearLength > 255) {
                                Toast.makeText(EditRobotDetailsActivity.this, "More than 255 symbols in fields are not allowed!", Toast.LENGTH_LONG).show();
                                break;
                            }
                            if (!String.valueOf(robotYear.getText()).matches("\\d+")) {
                                Toast.makeText(EditRobotDetailsActivity.this, "Year must be a digit!", Toast.LENGTH_LONG).show();
                                break;
                            }
                            robot.setName(String.valueOf(robotName.getText()));
                            robot.setType(String.valueOf(robotType.getText()));
                            robot.setYear(Integer.parseInt(String.valueOf(robotYear.getText())));
                            checkNetworkAndUpdate();
                            break;
                        }
                        Toast.makeText(EditRobotDetailsActivity.this, "No empty fields required!", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void initEditTextViews(){
        robotName = (EditText) findViewById(R.id.editName);
        robotName.setText(String.valueOf(robot.getName()));
        robotType = (EditText) findViewById(R.id.editType);
        robotType.setText(String.valueOf(robot.getType()));
        robotYear = (EditText) findViewById(R.id.editYear);
        robotYear.setText(String.valueOf(robot.getYear()));
    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void checkNetworkAndUpdate(){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new UpdateRobot().execute(url + robot.getId());
        } else {
            Log.i(TAG, "No network connection available.");
            Toast.makeText(EditRobotDetailsActivity.this, "No network connection available!", Toast.LENGTH_LONG).show();
        }
    }

    private class UpdateRobot extends AsyncTask<String, Void, String> {
        WebServiceCaller wsc = new WebServiceCaller();
        String response = null;

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return wsc.putCallWebService(urls[0], robot, waitingTime);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result==null){
                Toast.makeText(EditRobotDetailsActivity.this, "No response from the server!", Toast.LENGTH_LONG).show();
            } else {
                if (robot != null) {
                    Toast.makeText(EditRobotDetailsActivity.this, "Robot by id: " + String.valueOf(robot.getId()) + " was edited!", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(EditRobotDetailsActivity.this, RobotListActivity.class);
                startActivity(intent);
            }
        }
    }
}
