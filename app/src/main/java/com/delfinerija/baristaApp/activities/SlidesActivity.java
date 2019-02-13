package com.delfinerija.baristaApp.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.slides.IntroSlide1;
import com.delfinerija.baristaApp.slides.IntroSlide2;
import com.delfinerija.baristaApp.slides.IntroSlide3;
import com.delfinerija.baristaApp.slides.IntroSlide4;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.animations.IViewTranslation;
import androidx.annotation.FloatRange;

public class SlidesActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setBoolean();
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });


        addSlide(new IntroSlide1());
        addSlide(new IntroSlide2());
        addSlide(new IntroSlide3());
        addSlide(new IntroSlide4());
    }


    private void setBoolean() {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("appOpenedBefore",true);
        editor.apply();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
