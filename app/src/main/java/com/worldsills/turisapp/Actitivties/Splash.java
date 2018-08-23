package com.worldsills.turisapp.Actitivties;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.worldsills.turisapp.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Animation animation= AnimationUtils.loadAnimation(this, R.anim.anim_aparece);

        ImageView image= findViewById(R.id.imageSplash);
        image.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putInt("FRAG_ACTIVO", 0);

        editor.apply();
    }
}
