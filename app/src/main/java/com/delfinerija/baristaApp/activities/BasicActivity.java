package com.delfinerija.baristaApp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.delfinerija.baristaApp.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BasicActivity extends AppCompatActivity {

    protected void showError(String message){
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .create()
                .show();
    }

    protected boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected void saveUserInMemory(User user){
        getSharedPreferences("UserData", MODE_PRIVATE)
                .edit()
                .putString("token",user.getToken())
                .putString("name",user.getFirst_name())
                .putBoolean("isLogged",true)
                .apply();
    }

    protected String getUserToken(){
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = prefs.getString("token","");
        return token;
    }

    protected void vibratePhone(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

}
