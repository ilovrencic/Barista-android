package com.delfinerija.baristaApp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.delfinerija.baristaApp.R;

public class SplashLoginActivity extends AppCompatActivity {

    private Button google_login;
    private Button register_button;
    private TextView sign_in;
    private TextView terms_of_service;
    private LinearLayout hiddenPanel;
    private LottieAnimationView animation;
    private View animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlogin);

        animationView=findViewById(R.id.animation_view);
        animation=findViewById(R.id.animation_view);
        hiddenPanel=findViewById(R.id.hiddenPanel);
        terms_of_service=findViewById(R.id.terms_of_service);
        google_login = findViewById(R.id.google_button);
        register_button = findViewById(R.id.register_button);
        sign_in = findViewById(R.id.sign_in_text);
        animation.setImageAssetsFolder("images/");

        animation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveLogoUp();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if (firstTimeAppOpen()){
            Intent intent = new Intent(SplashLoginActivity.this,SlidesActivity.class);
            startActivityForResult(intent,1);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        else{
            animation.playAnimation();
            initListeners();
        }

    }


    private void moveLogoUp() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(animationView, "translationY",
                0f, -(Resources.getSystem().getDisplayMetrics().heightPixels / 4));
        animator.setDuration(1500);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(hiddenPanel,"alpha",1f);
                anim.setDuration(500);
                anim.start();
                ObjectAnimator anim2 = ObjectAnimator.ofFloat(terms_of_service,"alpha",1f);
                anim2.setDuration(500);
                anim2.start();
            }
        });
        animator.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(animationView, "scaleY",
                1f, 0.666f);
        animator2.setDuration(1000);
        animator2.setInterpolator(new OvershootInterpolator());
        animator2.start();

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(animationView, "scaleX",
                1f, 0.666f);
        animator3.setDuration(1000);
        animator3.setInterpolator(new OvershootInterpolator());
        animator3.start();
    }

    private boolean firstTimeAppOpen() {
        return !getSharedPreferences("UserData", MODE_PRIVATE).getBoolean("appOpenedBefore", false);
        //return true; //odkomentirati ovo i zakomentirati gore prilikom testiranja i radenja slideActivitya
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            animation.playAnimation();
            initListeners();
        }
    }

    private void initListeners(){
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashLoginActivity.this,QRActivitiy.class);
                startActivity(intent);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        terms_of_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
