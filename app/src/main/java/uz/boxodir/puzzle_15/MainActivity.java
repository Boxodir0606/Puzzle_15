package uz.boxodir.puzzle_15;


import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;


import com.example.puzzle_15.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final int N = 4;
    private TextView[][] views;
    private ArrayList<Integer> numbers;

    private int[][] checkArray = new int[4][4];
    private Pairs pairs;
    private int count;
    private ViewGroup group;

    private TextView textScore;
    private AppCompatButton restart;
    private AppCompatButton finish;
    private Boolean isSave = true;

    MediaPlayer buttomSound;
    MediaPlayer winSound;
    private Chronometer chronometer;
    private AppCompatImageView volue;
    private boolean isSound = true;

    private MediaPlayer media;

    private SharedPreferences pref;
    private SharedPreferences.Editor shape;

    private Animation myAnimation;
    String TAG = "TTT";


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAnimation = AnimationUtils.loadAnimation(this, R.anim.myanimation);

        pref = this.getSharedPreferences("puzzle", Context.MODE_PRIVATE);

        shape = pref.edit();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.gameactivity));

        init();
        loadViews();
        initNumbers();

        if (getIntent().getIntExtra("data", -1) == 1) {

            Log.d("TTT", "o'yin kelgan joyidan ketyabdi");
            String save = pref.getString("save", " ");
            String[] temp = save.split("#");
            Log.d("TTT", Arrays.toString(temp) + "");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(temp));
            if (temp.length == 1 || temp.length == 0) {
                loadNUmbersToViews();
            } else {
                loadSavedNumbers(list);
            }
            long del = pref.getLong("time", -1);
            chronometer.setBase(SystemClock.elapsedRealtime() + del);
            isSound = pref.getBoolean("isSound", true);
            chronometer.start();
            count = pref.getInt("step", 0);
            textScore.setText(String.valueOf(count));
        } else {
            loadNUmbersToViews();
            Log.d("TTT", "yangi o'yin");
        }
        setButtomSoundIcon();
        menu();


    }

    @SuppressLint("CommitPrefEdits")
    public void save() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(views[i / 4][i % 4].getText().toString()).append("#");
            Log.d("TTT", sb.toString());
        }

        shape.putString("save", String.valueOf(sb)).apply();
        shape.putBoolean("isSound", isSound);
        chronometer.stop();
        long delTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        shape.putLong("time", delTime);
        shape.putInt("step", count);

    }


    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        save();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
        save();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        save();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ");
        long del = pref.getLong("time", -1);
        chronometer.setBase(SystemClock.elapsedRealtime() + del);
        chronometer.start();
        super.onRestart();
        if (isSound) {
            volue.setImageResource(R.drawable.sound);
        } else {
            volue.setImageResource(R.drawable.nosound);
        }
        setButtomSoundIcon();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume:");
        super.onResume();
        if (isSound) {
            volue.setImageResource(R.drawable.sound);
        } else {
            volue.setImageResource(R.drawable.nosound);
        }
        setButtomSoundIcon();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart:");
        super.onStart();
        if (isSound) {
            volue.setImageResource(R.drawable.sound);
        } else {
            volue.setImageResource(R.drawable.nosound);
        }
        setButtomSoundIcon();
    }


    private void menu() {
        restart.setOnClickListener(view -> {
            restart.startAnimation(myAnimation);
            showRestartDialog();
        });
        finish.setOnClickListener(view -> {
            finish.startAnimation(myAnimation);

            save();
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void showRestartDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_restart);
        dialog.setCancelable(true);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        dialog.findViewById(R.id.textHa).setOnClickListener(view -> {
            dialog.findViewById(R.id.textHa).startAnimation(myAnimation);
            dialog.dismiss();
            loadNUmbersToViews();
        });

        dialog.findViewById(R.id.textYoq).setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setButtomSoundIcon() {
        volue.setOnClickListener(view -> {
            volue.startAnimation(myAnimation);
            if (isSound) {
                volue.setImageResource(R.drawable.nosound);
                isSound = false;
            } else {
                volue.setImageResource(R.drawable.sound);
                isSound = true;
            }
        });
    }

    private void init() {
        restart = findViewById(R.id.restart);
        finish = findViewById(R.id.finish);
        textScore = findViewById(R.id.tv_score);
        restart = findViewById(R.id.restart);
        chronometer = findViewById(R.id.chronometer);
        buttomSound = MediaPlayer.create(MainActivity.this, R.raw.btn);
        winSound = MediaPlayer.create(MainActivity.this, R.raw.win);
        media = MediaPlayer.create(MainActivity.this, R.raw.menu_click);
        volue = findViewById(R.id.volue);

    }

    private void loadViews() {
        group = findViewById(R.id.relative_container);
        int count = group.getChildCount();
        int size = (int) Math.sqrt(count);
        views = new TextView[size][size];

        for (int i = 0; i < count; i++) {
            View view = group.getChildAt(i);
            TextView view1 = (TextView) view;
            view1.setOnClickListener(this::onViewCLick);
            int y = i / size;
            int x = i % size;
            view1.setTag(new Pairs(x, y));
            views[y][x] = view1;
        }
    }

    private void initNumbers() {
        numbers = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {

            numbers.add(i);
        }
    }

    public void loadNUmbersToViews() {
        Collections.shuffle(numbers);
        for (int i = 0; i < views.length; i++) {
            for (int j = 0; j < views.length; j++) {
                int index = i * 4 + j;
                if (index < 15) {
                    views[i][j].setText(String.valueOf(numbers.get(index)));
                    checkArray[i][j] = numbers.get(index);
                }
            }
        }

        if (!isSolvable(checkArray)) loadNUmbersToViews();
        views[3][3].setText("");
        pairs = new Pairs(3, 3);


        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        count = 0;
        textScore.setText(String.valueOf(count));
    }


    private void onViewCLick(View view) {
        TextView view1 = (TextView) view;
        Pairs c = (Pairs) view1.getTag();
        int eX = pairs.getI();
        int eY = pairs.getJ();
        int dX = abs(c.getI() - eX);
        int dY = abs(c.getJ() - eY);
        if (dX + dY == 1) {
            setButtomSound();
            count++;
            textScore.setText(String.valueOf(count));
            views[eY][eX].setText(view1.getText());
            view1.setText("");
            pairs = c;
            if (isWin()) {
                if (isSound) {
                    winSound.start();
                    shape.clear();
                } else winSound.pause();

            }
        }
    }

    private void setButtomSound() {
        if (isSound) {
            buttomSound.start();
        }
    }

    @SuppressLint("CommitPrefEdits")
    private boolean isWin() {
        if (!(pairs.getI() == 3 && pairs.getJ() == 3)) return false;
        for (int i = 0; i < 15; i++) {
            String s = views[i / 4][i % 4].getText().toString();
            if (!s.equals(String.valueOf(i + 1))) return false;
        }

        showDialogWin();
        chronometer.stop();
        return true;
    }

    static int getInvCount(int[] arr) {
        int inv_count = 0;
        for (int i = 0; i < N * N - 1; i++) {
            for (int j = i + 1; j < N * N; j++) {
                if (arr[j] != 0 && arr[i] != 0
                        && arr[i] > arr[j])
                    inv_count++;
            }
        }
        return inv_count;
    }

    static int findXPosition(int[][] puzzle) {
        for (int i = N - 1; i >= 0; i--)
            for (int j = N - 1; j >= 0; j--)
                if (puzzle[i][j] == 0)
                    return N - i;
        return -1;
    }

    static boolean isSolvable(int[][] puzzle) {
        int[] arr = new int[N * N];
        int k = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                arr[k++] = puzzle[i][j];

        int invCount = getInvCount(arr);
        int pos = findXPosition(puzzle);
        if (pos % 2 == 1)
            return invCount % 2 == 0;
        else
            return invCount % 2 == 1;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        group = findViewById(R.id.relative_container);
        outState.putInt("COUNT", count);
        outState.putBoolean("sound", isSound);
        long delTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        outState.putLong("time", delTime);
        super.onSaveInstanceState(outState);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            TextView textView = (TextView) group.getChildAt(i);
            list.add(textView.getText().toString());
            outState.putStringArrayList("buttons", list);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt("COUNT", 0);
        long delTime = savedInstanceState.getLong("time", 0);
        chronometer.setBase(SystemClock.elapsedRealtime() + delTime);
        textScore.setText(String.valueOf(count));
        isSound = savedInstanceState.getBoolean("sound");
        ArrayList<String> list = savedInstanceState.getStringArrayList("buttons");
        loadSavedNumbers(list);
    }


    private void loadSavedNumbers(List<String> numbers) {
        textScore.setText(String.valueOf(count));
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i).equals("")) {
                pairs = new Pairs(i % 4, i / 4);
            }
            views[i / 4][i % 4].setText(numbers.get(i));
        }
    }

    private void showDialogWin() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_win);


        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        long delTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
        ((AppCompatTextView) dialog.findViewById(R.id.moves)).setText(String.valueOf(count));
        ((Chronometer) dialog.findViewById(R.id.chronometer)).setBase(SystemClock.elapsedRealtime() + delTime);

        dialog.findViewById(R.id.ok).setOnClickListener(v -> {
            dialog.findViewById(R.id.ok).startAnimation(myAnimation);
            loadNUmbersToViews();
            dialog.dismiss();
        });


        dialog.show();
    }
}