package com.streetninja.corp.android.peoplemap.auth;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by marctang on 9/9/15.
 */
public class AuthenticationServerRequest {

    public static final String SIGNIN_URL = "https://peoplemap-app.appspot.com/auth";

    public static final int REQUEST_SYNC = 4001;
    public static final int REQUEST_ASYNC = 4002;

    public static String syncUserSignIn(Context context, final String username, final String password, String authTokenType, final ResponseListener responseListener){
        RequestFuture<String> future = RequestFuture.newFuture();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, SIGNIN_URL, future, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               responseListener.requestEndedWithError(error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", username);
                params.put("password",password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);

        try {
            return future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return "Interrupted out";
        } catch (ExecutionException e) {
            return "Exec Timed out";
        } catch (TimeoutException e) {
            return "Timed out";
        }

    }


    public static void asyncUserSignIn(Context context, final String username, final String password, String authTokenType, final ResponseListener responseListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, SIGNIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseListener.requestCompleted(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseListener.requestEndedWithError(error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", username);
                params.put("password",password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }


    public static interface ResponseListener{
        public void requestStarted();
        public void requestCompleted();
        public void requestCompleted(String response);
        public void requestEndedWithError(VolleyError error);
    }

}
