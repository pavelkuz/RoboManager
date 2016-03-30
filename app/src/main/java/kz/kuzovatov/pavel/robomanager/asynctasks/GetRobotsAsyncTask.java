package kz.kuzovatov.pavel.robomanager.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kz.kuzovatov.pavel.robomanager.adapters.RobotAdapter;
import kz.kuzovatov.pavel.robomanager.models.Robot;
import kz.kuzovatov.pavel.robomanager.utils.PreferenceManager;
import kz.kuzovatov.pavel.robomanager.utils.RobotsResponseHandler;
import kz.kuzovatov.pavel.robomanager.utils.WebServiceCaller;

public class GetRobotsAsyncTask extends AsyncTask<String, Void, String> {
    private PreferenceManager preferenceManager = PreferenceManager.INSTANCE;
    private WebServiceCaller webServiceCaller = WebServiceCaller.INSTANCE;
    private Context context;
    private String response = "";
    private List<Robot> robotList;
    private RobotAdapter robotsAdapter;

    private static volatile GetRobotsAsyncTask instance;

    public static GetRobotsAsyncTask getInstance(Context context, RobotAdapter adapter) {
        GetRobotsAsyncTask localInstance = instance;
        if (localInstance == null) {
            synchronized (GetRobotsAsyncTask.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GetRobotsAsyncTask();
                }
            }
        }
        localInstance.setContext(context);
        localInstance.setRobotsAdapter(adapter);
        return localInstance;
    }

    @Override
    protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        return webServiceCaller.callWebService(urls[0], preferenceManager.getWaitingResponseTime(context));
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        if(result==null){
            Toast.makeText(context, "No response from the server!", Toast.LENGTH_LONG).show();
        } else {
            response = result;
            RobotsResponseHandler handler = new RobotsResponseHandler();
            robotList = handler.getObjectsResponse(response);
            robotsAdapter.setData(robotList);
            instance = null;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRobotsAdapter(RobotAdapter robotsAdapter) {
        this.robotsAdapter = robotsAdapter;
    }
}
