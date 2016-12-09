package com.jeff.chatapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

public class RegisterActivity extends AppCompatActivity {

    EditText etemail,etpass,etinputname1,etpassmatch;
    Button btnregister;
    TextView tvlogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog= new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();

        etinputname1=(EditText)findViewById(R.id.etinputname1);
        etpassmatch=(EditText)findViewById(R.id.etpassmatch);
        btnregister=(Button)findViewById(R.id.btnregister);
        etemail=(EditText)findViewById(R.id.etloginmail);
        etpass=(EditText)findViewById(R.id.etpass);
        tvlogin=(TextView)findViewById(R.id.tvlog);

        if(etpassmatch.getText().toString().compareTo(etpass.getText().toString())!=0)
            etpassmatch.setError("Should be same as password");

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }

    private void registerUser(){

        final String inputusername= etinputname1.getText().toString();
        final String checkpass= etpassmatch.getText().toString();
        final String email=etemail.getText().toString().trim();
        final String pass=etpass.getText().toString().trim();

        etinputname1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(TextUtils.isEmpty(inputusername))
                    etinputname1.setError("Input User Name");
            }
        });

        etpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(TextUtils.isEmpty(pass))
                    etpass.setError("Password is Mandatory for login");
            }
        });

        etemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(TextUtils.isEmpty(email))
                    etemail.setError("Email is mandatory for login");
            }
        });

        etpassmatch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(checkpass.compareTo(pass)!=0)
                    etpassmatch.setError("Should be same as password");
            }
        });

        if(TextUtils.isEmpty(inputusername)){
            Toast.makeText(RegisterActivity.this,"Please Enter Username",Toast.LENGTH_SHORT).show();
            return;}
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;}
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;}
        if(TextUtils.isEmpty(checkpass)){
            Toast.makeText(RegisterActivity.this,"Please Re-enter password",Toast.LENGTH_SHORT).show();
            return;}
        if(pass.length()<6){
            Toast.makeText(RegisterActivity.this,"6 or more characters for password",Toast.LENGTH_SHORT).show();
            return;}
        if(checkpass.compareTo(pass)!=0){
            Toast.makeText(RegisterActivity.this,"Password does not match",Toast.LENGTH_SHORT).show();
            return;}

        /*if(!checkInternet()){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Info");
            alertDialog.setMessage("Internet not available, Check your internet connectivity and try again");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }*/

        //validation done
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        Intent intent=new Intent(getBaseContext(),LoginActivity.class);
        intent.putExtra("usernamelogin",inputusername);
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Registered Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Registeration Failed",Toast.LENGTH_SHORT).show();
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

