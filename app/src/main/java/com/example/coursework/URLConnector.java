package com.example.coursework;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class URLConnector {
    //TODO: Set the logics  of connection with server
    double estimatedValue;
    private static String testUrl = "";
    RequestQueue mRequestQueue;

    public URLConnector(Context context, String url){
        testUrl = url;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    private void getMark(){
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, testUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mark = response.getJSONObject("mark");
                    estimatedValue = mark.getDouble("mark");
                    // присваеваем переменным соответствующие значения из API
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // в случае возникновеня ошибки
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request); // добавляем запрос в очередь
    }

    public double getValue(){
        getMark();
        return estimatedValue;
    }
}
