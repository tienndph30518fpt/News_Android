package tienndph30518.thi_20_docrss.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    int nhac;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!= null){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                nhac = bundle.getInt("nhac");
            }
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),nhac);
        mediaPlayer.start();

        return START_NOT_STICKY;
    }
}
