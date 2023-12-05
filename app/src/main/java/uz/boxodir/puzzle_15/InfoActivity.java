package uz.boxodir.puzzle_15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.puzzle_15.R;

public class InfoActivity extends AppCompatActivity {

    TextView textView;

    private Animation myAnimation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_info);
        textView = findViewById(R.id.info);
        textView.setText(R.string.text);

        myAnimation = AnimationUtils.loadAnimation(this,R.anim.myanimation);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.infoactivity));


            findViewById(R.id.back_menu).setOnClickListener(v -> {
                myAnimation.start();
            startActivity(new Intent(this,StartActivity.class));
            finish();

        });
    }

}