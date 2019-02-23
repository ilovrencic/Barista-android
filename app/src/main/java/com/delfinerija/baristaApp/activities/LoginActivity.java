package com.delfinerija.baristaApp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
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
    private Call<ResponseBody> reset_password;
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
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

    private void show_dialog(){
        LayoutInflater inflater= LayoutInflater.from(LoginActivity.this);
        View view=inflater.inflate(R.layout.dialog_reset_passoword, null);
        final EditText editText = view.findViewById(R.id.email_reset);
        Button reset_button = view.findViewById(R.id.reset_button_dialog);
        Button cancel_button = view.findViewById(R.id.cancel_button_dialog);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmailValid(editText.getText().toString())){
                    reset_password(editText.getText().toString());
                }else{
                    Toasty.error(LoginActivity.this,"Not a valid email address!",Toast.LENGTH_SHORT,false).show();
                }
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setView(view);
        final AlertDialog alert = alertDialog.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.show();
    }

    public void showError(String message){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void reset_password(final String email){
        viewDialog.showDialog();
        reset_password = apiService.resetPassword(email);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reset_password.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        viewDialog.hideDialog();
                        if(response.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else{
                            try {
                                showError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        viewDialog.hideDialog();
                        showError(t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        },300);
    }
}