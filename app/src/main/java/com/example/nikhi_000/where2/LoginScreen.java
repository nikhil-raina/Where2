package com.example.nikhi_000.where2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    //private ImageButton imageButton;
    private Button nextButton;
    private TextView zipCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //imageButton = (ImageButton) findViewById(R.id.imageButton2);
        zipCodeView = (TextView) findViewById(R.id.editText);
        nextButton = (Button) findViewById(R.id.button);



    }

    public void actionNext_Button(View view) {
        String zipCode = zipCodeView.getText().toString();
        Intent intent = new Intent(this, loadData_Server.class);
        intent.putExtra("ZipCode", zipCode);
        startActivity(intent);
    }
}
