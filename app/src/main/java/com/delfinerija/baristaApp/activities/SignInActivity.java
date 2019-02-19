package com.delfinerija.baristaApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.entities.User;
import com.delfinerija.baristaApp.entities.ViewDialog;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.GenericResponse;
import com.delfinerija.baristaApp.network.InitApiService;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login_button;
    private TextView register_text;
    private ApiService apiService;
    private Call<GenericResponse<User>> login;
    private ViewDialog viewDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        apiService = InitApiService.apiService;
        viewDialog = new ViewDialog(this);

        email = findViewById(R.id.email_signin);
        password = findViewById(R.id.password_signin);
        login_button = findViewById(R.id.login_button);
        register_text = findViewById(R.id.register_text);

        initListeners();
    }

    private void initListeners(){
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().length() == 0 || password.getText().toString().length() == 0){
                    Toasty.warning(SignInActivity.this,"Please fill all fields!", Toast.LENGTH_SHORT,true).show();
                }else{
                    login_user(email.getText().toString().trim(),password.getText().toString().trim());
                }
            }
        });

        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login_user(String email,String password){
        viewDialog.showDialog();
        login = apiService.signInUser(email,password);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                login.enqueue(new Callback<GenericResponse<User>>() {
                    @Override
                    public void onResponse(Call<GenericResponse<User>> call, Response<GenericResponse<User>> response) {
                        viewDialog.hideDialog();
                        if(response.isSuccessful()){
                            //TODO spremi token
                            Intent intent = new Intent(SignInActivity.this,QRActivitiy.class);
                            startActivity(intent);
                            finish();
                        }else{
                            try {
                                showError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<User>> call, Throwable t) {
                        viewDialog.hideDialog();
                        showError(t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        },500);

    }

    public void showError(String message){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }
}
