package kz.kuzovatov.pavel.robomanager.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kz.kuzovatov.pavel.robomanager.models.Robot;

public class RobotsResponseHandler implements GenericResponseHandler<Robot> {
    private static final String TAG = "RobotsResponseHandler";

    @Override
    public List<Robot> getObjectsResponse(String response) {
        Log.i(TAG, "Got response: " + response);
        JSONObject jsonResponse;
        List<Robot> robotList = new ArrayList<>();
        JSONArray jsonRobotList;
        try {
            jsonResponse = new JSONObject(response);
            jsonRobotList = jsonResponse.getJSONArray("robots");
            for (int i = 0; i < jsonRobotList.length(); i++){
                JSONObject robotObject = jsonRobotList.getJSONObject(i);
                Robot robot = new Robot();
                robot.setId(robotObject.optInt("id"));
                robot.setName(robotObject.optString("name"));
                robot.setType(robotObject.optString("type"));
                robot.setYear(robotObject.optInt("year"));
                robotList.add(robot);
            }
        } catch (JSONException e) {
            Log.w(TAG, "JSONException in getResponse method: ", e);
        }
        return robotList;
    }

    @Override
    public Robot getObjectResponse(String response) {
        JSONObject jsonResponse;
        Robot robot = new Robot();
        JSONObject robotObject;
        try {
            jsonResponse = new JSONObject(response);
            robotObject = jsonResponse.getJSONObject("robot");
            Log.i(TAG, "Got object: " + robotObject);
            robot.setId(robotObject.optInt("id"));
            robot.setName(robotObject.optString("name"));
            robot.setType(robotObject.optString("type"));
            robot.setYear(robotObject.optInt("year"));
        } catch (JSONException e) {
            Log.w(TAG, "JSONException in getResponse method: ", e);
        }
        return robot;
    }
}
