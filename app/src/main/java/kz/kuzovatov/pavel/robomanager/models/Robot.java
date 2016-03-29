package kz.kuzovatov.pavel.robomanager.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Robot extends NamedEntity implements Parcelable {
    private String type;
    private int year;
    private boolean isChecked;

    public Robot() {
    }

    //constructor as-is in DB
    public Robot(int id, String name, String type, int year) {
        super(id, name);
        this.type = type;
        this.year = year;
    }

    //Constructor for the adapter, pass state of the checking through model
    public Robot(int id, String name, String type, int year, boolean isChecked) {
        super(id, name);
        this.type = type;
        this.year = year;
        this.isChecked = isChecked;
    }

    //Parcelling constructor testing
    public Robot(Parcel source){
        this.setId(source.readInt());
        this.setName(source.readString());
        this.type = source.readString();
        this.year = source.readInt();
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    //may using for pass object through intent
    public String jsonSerialize(Robot robot){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", robot.getId());
            jsonObject.put("name", robot.getName());
            jsonObject.put("type", robot.getType());
            jsonObject.put("year", robot.getYear());
        } catch (JSONException e) {
            Log.e("Robot class", "JSONException in serialization method: ", e);
        }
        return String.valueOf(jsonObject);
    }

    //may using for get object through intent
    public Robot jsonDeSerialize(String source){
        JSONObject jsonResponse;
        Robot robot = new Robot();
        JSONObject robotObject;
        try {
            jsonResponse = new JSONObject(source);
            robotObject = jsonResponse.getJSONObject("robot");
            Log.i("Robot class", "Got oject: " + robotObject);
            robot.setId(robotObject.optInt("id"));
            robot.setName(robotObject.optString("name"));
            robot.setType(robotObject.optString("type"));
            robot.setYear(robotObject.optInt("year"));
        } catch (JSONException e) {
            Log.e("Robot class", "JSONException in deserialization method: ", e);
        }
        return robot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeString(type);
        dest.writeInt(year);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Robot createFromParcel(Parcel source) {
            return new Robot(source);
        }

        public Robot[] newArray(int size) {
            return new Robot[size];
        }
    };
}
