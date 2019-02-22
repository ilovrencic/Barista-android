package com.delfinerija.baristaApp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.entities.Session;
import com.delfinerija.baristaApp.entities.User;
import com.delfinerija.baristaApp.entities.UserResponse;
import com.delfinerija.baristaApp.entities.ViewDialog;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.GenericResponse;
import com.delfinerija.baristaApp.network.InitApiService;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login_button;
    private TextView register_text;
    private ApiService apiService;
    private Call<GenericResponse<UserResponse>> login;
    private ViewDialog viewDialog;
    private TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        apiService = InitApiService.apiService;
        viewDialog = new ViewDialog(this);

        email = findViewById(R.id.email_signin);
        password = findViewById(R.id.password_signin);
        login_button = findViewById(R.id.login_button);
        register_text = findViewById(R.id.register_text);
        forgot_password = findViewById(R.id.forgot_password);

        initListeners();
    }

    private void initListeners(){
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().length() == 0 || password.getText().toString().length() == 0){
                    Toasty.warning(LoginActivity.this,"Please fill all fields!", Toast.LENGTH_SHORT,true).show();
                }else{
                    login_user(email.getText().toString().trim(),password.getText().toString().trim());
                }
            }
        });

        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO reset the password screen
            }
        });
    }

    private void login_user(String email,String password){
        Session session = new Session(email,password);

        viewDialog.showDialog();
        login = apiService.signInUser(email,password,session);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                login.enqueue(new Callback<GenericResponse<UserResponse>>() {
                    @Override
                    public void onResponse(Call<GenericResponse<UserResponse>> call, Response<GenericResponse<UserResponse>> response) {
                        viewDialog.hideDialog();
                        if(response.isSuccessful()){

                            UserResponse userResponse = response.body().getResponseData();
                            saveUserInMemory(userResponse.getUser());

                            Intent intent = new Intent(LoginActivity.this,MainMenuActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }else{
                            //TODO change error message
                            try {
                                showError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse<UserResponse>> call, Throwable t) {
                        viewDialog.hideDialog();
                        showError(t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        },500);

    }

    private void saveUserInMemory(User user){
        getSharedPreferences("UserData", MODE_PRIVATE)
                        .edit()
                        .putString("token",user.getToken())
                        .putString("name",user.getFirst_name())
                        .putBoolean("isLogged",true)
                        .apply();
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