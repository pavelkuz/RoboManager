package kz.kuzovatov.pavel.robomanager.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kz.kuzovatov.pavel.robomanager.models.BaseEntity;
import kz.kuzovatov.pavel.robomanager.models.Robot;

public enum WebServiceCaller {
    INSTANCE;
    private static final String TAG = "WebServiceCaller";

    /**
     * Method that calling WS for get all entities
     *
     * @param url url of rest api
     * @param waitingResponseTime time to wait response from the server, if it ends without result,
     * than messaging error.
     * @return response string
     */
    public String callWebService(String url, int waitingResponseTime){
        String response = null;
        URL urlAddress = null;
        try {
            urlAddress = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException in callWebService method: ", e);
        }
        HttpURLConnection urlConnection = null;
        try {
            if (urlAddress != null) {
                urlConnection = (HttpURLConnection) urlAddress.openConnection();
                urlConnection.setReadTimeout(waitingResponseTime);
                urlConnection.setConnectTimeout(waitingResponseTime);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with connection in callWebService method: ", e);
        }
        InputStream in = null;
        try {
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());
                response = readStream(in);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with response reading in callWebService method: ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    /**
     * Method that reading response for each byte
     *
     * @param is input stream
     * @return response string
     */
    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            Log.e(TAG, "Read stream error: ", e);
            return "";
        }
    }

    /**
     * Method that calling WS for update entity
     *
     * @param url url of rest api with entity id
     * @param robot entity that could be update
     * @param waitingResponseTime time to wait response from the server, if it ends without result,
     * than messaging error.
     * @return response string
     */
    public String putCallWebService(String url, Robot robot, int waitingResponseTime){
        String response = null;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", robot.getName());
            jsonObject.put("type", robot.getType());
            jsonObject.put("year", robot.getYear());
        } catch (JSONException e) {
            Log.e(TAG, "JSONException in putCallWebService method: ", e);
        }

        String json = jsonObject.toString();

        URL urlAddress = null;

        try {
            urlAddress = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException in putCallWebService method: ", e);
        }

        HttpURLConnection urlConnection = null;

        try {
            if (urlAddress != null) {
                urlConnection = (HttpURLConnection) urlAddress.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(waitingResponseTime);
                urlConnection.setConnectTimeout(waitingResponseTime);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with connection in putCallWebService method: ", e);
        }

        OutputStreamWriter out = null;

        try {
            if (urlConnection != null) {
                out = new OutputStreamWriter(urlConnection.getOutputStream());
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with output stream open in putCallWebService method: ", e);
        }

        try {
            if(out!=null) {
                out.write(json);
                out.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with output stream write in putCallWebService method: ", e);
        }

        InputStream in = null;
        try {
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());
                response = readStream(in);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with response reading in putCallWebService method: ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    /**
     * Method that calling WS for save entity
     *
     * @param url url of rest api
     * @param robot entity that could be saved
     * @param waitingResponseTime time to wait response from the server, if it ends without result,
     * than messaging error.
     * @return response string
     */
    public String postCallWebService(String url, Robot robot, int waitingResponseTime){
        String response = null;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", robot.getName());
            jsonObject.put("type", robot.getType());
            jsonObject.put("year", robot.getYear());
        } catch (JSONException e) {
            Log.e(TAG, "JSONException in postCallWebService method: ", e);
        }

        String json = jsonObject.toString();

        URL urlAddress = null;

        try {
            urlAddress = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException in postCallWebService method: ", e);
        }

        HttpURLConnection urlConnection = null;

        try {
            if(urlAddress != null) {
                urlConnection = (HttpURLConnection) urlAddress.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(waitingResponseTime);
                urlConnection.setConnectTimeout(waitingResponseTime);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with connection in postCallWebService method: ", e);
        }

        OutputStreamWriter out = null;

        try {
            if (urlConnection != null) {
                out = new OutputStreamWriter(urlConnection.getOutputStream());
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with output stream open in postCallWebService method: ", e);
        }

        try {
            if(out!=null) {
                out.write(json);
                out.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with output stream write in postCallWebService method: ", e);
        }

        InputStream in = null;
        try {
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());
                response = readStream(in);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with response reading in postCallWebService method: ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    /**
     * Method that calling WS for deleting entity
     *
     * @param url url, that contains id of the entity for deleting
     * @param waitingResponseTime time to wait response from the server, if it ends without result,
     * than messaging error.
     * @return response string
     */
    public String deleteCallWebService(String url, int waitingResponseTime){
        String response = null;
        URL urlAddress = null;
        try {
            urlAddress = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException in deleteCallWebService method: ", e);
        }
        HttpURLConnection urlConnection = null;
        try {
            if (urlAddress != null) {
                urlConnection = (HttpURLConnection) urlAddress.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setReadTimeout(waitingResponseTime);
                urlConnection.setConnectTimeout(waitingResponseTime);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with connection in deleteCallWebService method: ", e);
        }
        InputStream in = null;
        try {
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());
                response = readStream(in);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException with response reading in deleteCallWebService method: ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response;
    }
}
