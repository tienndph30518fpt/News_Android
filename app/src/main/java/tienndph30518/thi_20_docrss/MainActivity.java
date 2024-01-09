package tienndph30518.thi_20_docrss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView view = findViewById(R.id.imgManHinhChao);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animation_xoay);
        view.startAnimation(animation);

//        Glide.with(this).load(R.drawable.manhinhchao).into(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent  intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        },3000);
    }
}