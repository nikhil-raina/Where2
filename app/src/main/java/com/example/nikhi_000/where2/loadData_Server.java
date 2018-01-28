package com.example.nikhi_000.where2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
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

            String url_US_Events = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=US&apikey=7J8pOipo9RjqHiHS4Ws2WoQZ6EJgeNMV";

            try {
                url = new URL(url_US_Events);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

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


        }
    }

}
