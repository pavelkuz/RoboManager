package kz.kuzovatov.pavel.robomanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String CON_PROPS = "RobomanagerSettings";
    private final static String SERVER_ADDRESS = "SERVER_ADDRESS";
    private final static String WAITING_TIME = "WAITING_TIME";
    private SharedPreferences settings;
    private Button saveButton;
    private EditText serverAddress;
    private EditText waitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initActionBar();
        initButtons();
        initFields();
        settings = getSharedPreferences(CON_PROPS, MODE_PRIVATE);
    }

    public void initFields(){
        serverAddress = (EditText) findViewById(R.id.setServerIP);
        waitTime = (EditText) findViewById(R.id.setWaiting);
    }

    public void initButtons(){
        saveButton = (Button) findViewById(R.id.saveSettings);
        saveButton.setOnClickListener(this);
    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveSettings:
                if (String.valueOf(serverAddress.getText()).length() > 0 && String.valueOf(waitTime.getText()).length() > 0){
                    if (Integer.parseInt(String.valueOf(waitTime.getText())) >= 10000) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.clear();
                        editor.putString(SERVER_ADDRESS, String.valueOf(serverAddress.getText()));
                        editor.putInt(WAITING_TIME, Integer.parseInt(String.valueOf(waitTime.getText())));
                        editor.apply();
                        Intent intent = new Intent(SettingsActivity.this, RobotListActivity.class);
                        startActivity(intent);
                        Toast.makeText(SettingsActivity.this, "Settings applied successfully!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    Toast.makeText(SettingsActivity.this, "Waiting response time must be at least 10000 ms!", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(SettingsActivity.this, "No empty server IP, port and waiting response time required!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
