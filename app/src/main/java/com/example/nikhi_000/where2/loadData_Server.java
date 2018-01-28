package com.example.nikhi_000.where2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by nikhi_000 on 1/27/2018.
 */

public class loadData_Server extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String zipCode = intent.getStringExtra("ZipCode");

        new AsyncTask_US(zipCode).execute();

    }

    private class AsyncTask_US extends AsyncTask<String, String, String>{

        HttpURLConnection connection;
        URL url = null;
        private String zipCode;


        public AsyncTask_US(String zipCode_Name){
            this.zipCode = zipCode_Name;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            String url_US_Events = "https://app.ticketmaster.com/discovery/v2/events.json?postalCode="+zipCode+"&apikey=NAuywMRiMdz02hIZwUgbzF0tn8JBfs9b";

            try {
                url = new URL(url_US_Events);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                //connection.setDoOutput(true);
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                int feedBack_ID = connection.getResponseCode();

                Log.e("feedBack_ID", " "+ feedBack_ID);
                if(feedBack_ID == HttpURLConnection.HTTP_OK){
                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    StringBuilder result = new StringBuilder();
                    String line;

                    while((line = br.readLine()) != null){
                        result.append(line);
                    }
                    Log.e("the Result (per Line): "," "+result);
                    return(result.toString());
                }
                else return ("UNSUCCESSFUL");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result){
            List<String> eventNames = new ArrayList<String>();
            List<String> genre = new ArrayList<String>();
            List<dateDuration> duration = new ArrayList<dateDuration>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("_embedded");
                JSONArray jsonArray = jsonObject1.getJSONArray("events");

                for(int i = 0; i < jsonArray.length(); i++){
                    dateDuration dateDuration = new dateDuration();
                    JSONObject obj = jsonArray.getJSONObject(i);
                    eventNames.add(obj.getString("name"));
                    JSONArray objArray = obj.getJSONArray("classifications");
                    genre.add(objArray.getJSONObject(2).getString("genre"));

                    JSONObject salesObj = obj.getJSONObject("sales");
                    JSONObject publicObj = salesObj.getJSONObject("public");
                    dateDuration.startDate = publicObj.getString("startDateTime");
                    dateDuration.endDate = publicObj.getString("endDateTime");
                    duration.add(dateDuration);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(loadData_Server.this, Preferences_Screen.class);
            intent.putExtra("EventNames", (Serializable) eventNames);
            intent.putExtra("Genre", (Serializable) genre);
            intent.putExtra("Duration", (Serializable) duration);
            startActivity(intent);
        }
    }

}
