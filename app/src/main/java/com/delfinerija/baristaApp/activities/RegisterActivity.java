package com.delfinerija.baristaApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.entities.User;
import com.delfinerija.baristaApp.entities.ViewDialog;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.InitApiService;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BasicActivity {

    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private EditText password;
    private EditText repeat_password;
    private TextInputLayout email_layout;
    private TextInputLayout password_layout;
    private TextInputLayout repeat_password_layout;
    private CheckBox checkBox;
    private Button register;
    private ApiService apiService;
    private Call<ResponseBody> registerUser;
    private Call<ResponseBody> checkEmail;
    private ViewDialog viewDialog;
    private TextView login_text;
    private boolean emailTaken = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        apiService = InitApiService.apiService;
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repeat_password = findViewById(R.id.repeat_password);
        email_layout = findViewById(R.id.emaiL_layout);
        password_layout = findViewById(R.id.pass_layout);
        repeat_password_layout = findViewById(R.id.again_layout);
        checkBox = findViewById(R.id.checkBox);
        register = findViewById(R.id.register_button);
        login_text = findViewById(R.id.login_text);
        viewDialog = new ViewDialog(this);

        initListeners();
    }

    private void initListeners(){

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    isEmailTaken(email.getText().toString().trim());
                    if(!isEmailValid(email.getText().toString().trim())){
                        email_layout.setErrorEnabled(true);
                        email_layout.setError("Not a valid email!");
                    }

                    if(isEmailValid(email.getText().toString().trim())){
                        email_layout.setErrorEnabled(false);
                    }
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmailValid(email.getText().toString().trim())){
                    email_layout.setErrorEnabled(false);
                }
            }
        });



        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(password.getText().toString().length() < 6){
                    password_layout.setErrorEnabled(true);
                    password_layout.setError("The password is too short!");
                }

                if(password.getText().toString().length() >= 6){
                    password_layout.setErrorEnabled(false);
                }
            }
        });

        repeat_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!repeat_password.getText().toString().equals(password.getText().toString())){
                    repeat_password_layout.setErrorEnabled(true);
                    repeat_password_layout.setError("The passwords need to match!");
                }else{
                    repeat_password_layout.setErrorEnabled(false);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first_name.getText().toString().length() == 0 || last_name.getText().toString().length() == 0 || email.getText().toString().length() == 0 || password.getText().toString().length() == 0 || repeat_password.getText().toString().length() == 0){
                    Toasty.warning(RegisterActivity.this, "Please fill every box!", Toast.LENGTH_SHORT, true).show();
                }else if(password.getText().toString().length() < 6){
                    Toasty.warning(RegisterActivity.this, "Your password is too short!", Toast.LENGTH_SHORT, true).show();
                }else if(!repeat_password.getText().toString().equals(password.getText().toString())){
                    Toasty.warning(RegisterActivity.this, "Your passwords do not match!", Toast.LENGTH_SHORT, true).show();
                }else if(!checkBox.isChecked()){
                    Toasty.warning(RegisterActivity.this, "The checkbox can't be unchecked!", Toast.LENGTH_SHORT, true).show();
                }else if(!isEmailValid(email.getText().toString().trim())){
                    Toasty.warning(RegisterActivity.this,"This email address is not valid!",Toast.LENGTH_SHORT,true).show();
                }
                else if(emailTaken){
                    Toasty.warning(RegisterActivity.this,"This email address is already taken!",Toast.LENGTH_SHORT,true).show();
                }
                else{
                    User user = new User(first_name.getText().toString().trim(),last_name.getText().toString().trim(),email.getText().toString().trim(),password.getText().toString(),null);
                    registration(user);
                }
            }
        });
    }

    private void isEmailTaken(String email){
        checkEmail = apiService.checkEmail(email);
        checkEmail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    email_layout.setErrorEnabled(false);
                    resetEmailBoolean(false);
                }else{
                    if(response.code() == 409){
                        email_layout.setErrorEnabled(true);
                        email_layout.setError("This email is already taken!");
                        resetEmailBoolean(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(RegisterActivity.this,"An unexpected error occurred!",Toast.LENGTH_SHORT,true).show();
            }
        });
    }

    private void registration(User user){
        viewDialog.showDialog();
        registerUser = apiService.registerUser(user);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                registerUser.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            viewDialog.hideDialog();
                            Toasty.success(RegisterActivity.this,"Registration successful.",Toast.LENGTH_SHORT,true).show();
                            Intent intent = new Intent(RegisterActivity.this,ConfirmEmailActivity.class);
                            intent.putExtra("email",email.getText().toString().trim());
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }else{
                            viewDialog.hideDialog();
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
        },500);
    }

    private void resetEmailBoolean(boolean value){
        emailTaken = value;
    }
}
