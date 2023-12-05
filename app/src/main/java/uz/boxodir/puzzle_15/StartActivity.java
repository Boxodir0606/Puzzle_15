package uz.boxodir.puzzle_15;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.example.puzzle_15.R;


public class StartActivity extends AppCompatActivity {
    private AppCompatButton play;
    private AppCompatButton info;
    private AppCompatButton share;
    private Animation myAnimation;

    private SharedPreferences pref;

    private SharedPreferences.Editor shape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        myAnimation = AnimationUtils.loadAnimation(this,R.anim.myanimation);


//        Window window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(this.getResources().getColor(R.color.startactivity));


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        sound();
    }

    private void init() {
        play = findViewById(R.id.btn_play);
        info = findViewById(R.id.btn_info);
        share = findViewById(R.id.btn_shareProject);
    }

    private void sound() {
        play.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            play.startAnimation(myAnimation);
            startActivity(intent);
        });

        info.setOnClickListener(v -> {
            info.startAnimation(myAnimation);
            startActivity(new Intent(this, InfoActivity.class));
        });
        share.setOnClickListener(v -> {
            share.startAnimation(myAnimation);
            shareProgramm(StartActivity.this);
        });

    }


    private void shareProgramm(Context context) {
        final String packagename = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "\"Hoziroq yuklab oling!: \"  + \"https://play.google.com/store/apps/details?id=$"+ packagename);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

}