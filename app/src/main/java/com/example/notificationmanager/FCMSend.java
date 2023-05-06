package com.example.notificationmanager;

import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAAMclUQCc:APA91bHmm8KF9PiY-tLSrHNvseuJh9Y0UTxk06slO46gf3yJEtAOPWiWJ8xBmBhQlJJj9HC0kfmaghNxTTnAEPEeHytt9hjwUdYhPJAua1wy_xHZTl5TIp2hZFWmT_i6KG3OL8mX56mN";

    public static void pushNotification(Context context, String token, String title , String message,String type){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            JSONObject json = new JSONObject();
            json.put("to",token);
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body",message);
            notification.put("type",type);
            json.put("data", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL , json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("FCM " + response);

                }

                }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };
      queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void pushNotificationMultiple(Context context, JSONArray token, String title , String message,String type){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            JSONObject json = new JSONObject();
            json.put("registration_ids",token);
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body",message);
            notification.put("type",type);
            json.put("data", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL , json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("FCM " + response);

                }

            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void pushNotification(Context context, String token, String title , String message,String imageURL,String type){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);

        try{

            JSONObject json = new JSONObject();
            json.put("to",token);
            /*JSONObject json = new JSONObject();
            json.put("to",token);*/
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body",message);
            notification.put("image", Uri.parse( imageURL));
            notification.put("type",type);
            json.put("data", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL , json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println("FCM " + response);

                }

            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void pushNotificationMultiple(Context context, JSONArray token, String title , String message, String imageURL,String type){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);

        try{

            JSONObject json = new JSONObject();
            json.put("registration_ids",token);
            /*JSONObject json = new JSONObject();
            json.put("to",token);*/
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body",message);
            notification.put("type",type);
            notification.put("image", Uri.parse( imageURL));
            json.put("data", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL , json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println("FCM " + response);

                }

            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void pushNotificationMultipleSeedNumber(Context context, JSONArray token, String title , String message,String type, int seedNumber){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            JSONObject json = new JSONObject();
            json.put("registration_ids",token);
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body",message);
            notification.put("type",type);
            notification.put("SeedNumber",seedNumber);
            json.put("data", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL , json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("FCM " + response);

                }

            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

