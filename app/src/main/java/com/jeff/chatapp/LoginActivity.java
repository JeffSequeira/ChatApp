package com.jeff.chatapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView tvsignup;
    private EditText etloginmail, etloginpass;
    private Button btnlogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvsignup=(TextView)findViewById(R.id.tvsignup);
        etloginmail=(EditText)findViewById(R.id.etloginmail);
        etloginpass=(EditText)findViewById(R.id.etloginpass);
        btnlogin=(Button)findViewById(R.id.btnSignIn);

        progressDialog= new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();


        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),RegisterActivity.class));
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkInternet()){
                    final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Info");
                    alertDialog.setMessage("Internet not available, Check your internet connectivity and try again");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
                userLogin();
            }
        });
    }

    private void userLogin() {

        String loginmail=etloginmail.getText().toString().trim();
        String loginpass=etloginpass.getText().toString().trim();

        if(TextUtils.isEmpty(loginmail)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;}
        if(TextUtils.isEmpty(loginpass)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;}

        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(loginmail,loginpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(getBaseContext(),"Logged In",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(),MainActivity.class));}
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Login failed. Please enter correct credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
