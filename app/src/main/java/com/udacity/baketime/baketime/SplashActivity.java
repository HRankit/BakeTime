package com.udacity.baketime.baketime;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView iv = findViewById(R.id.imageView);


        int j = getRandom();

//        int[] array = getResources().getIntArray(R.array.random_imgs);
//        int imgsrc = array[new Random().nextInt(array.length)];


        TypedArray imgs = getResources().obtainTypedArray(R.array.random_imgs);
        iv.setBackgroundResource(imgs.getResourceId(j, 0));

        imgs.recycle();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private int getRandom() {
        Random random = new Random();
        return random.nextInt((4) + 1);
    }

}
