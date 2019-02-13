package com.delfinerija.baristaApp.entities;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.delfinerija.baristaApp.R;

public class ViewDialog {

    Activity activity;
    Dialog dialog;
    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_activity);

        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.spin_kit);
        lottieAnimationView.playAnimation();
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }

}