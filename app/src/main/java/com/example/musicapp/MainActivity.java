package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView title, timeLast, totalTime;
    SeekBar seekBar;
    ImageButton next, previous, play,stop;
    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;
    ImageView imageDisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindComponent();

        AddSong();

        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        RenewMedia();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    // dừng và chuyển icon sang play
                    mediaPlayer.pause();

                    play.setImageResource((android.R.drawable.ic_media_play));
                }

                else{
                    // chạy và chuyển icon sang dừng
                    mediaPlayer.start();
                    play.setImageResource(android.R.drawable.ic_media_pause);
                }
                // chuyển đổi total time khi chơi nhạc
                SetTotalTime();
                UpdateCurrentTime();
                imageDisc.startAnimation(animation);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stop, release media đang phát
                mediaPlayer.stop();
                mediaPlayer.release();

                // chuyển đổi nút play sang play
                play.setImageResource(android.R.drawable.ic_media_play);
                RenewMedia();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position ++;
                if (position > arraySong.size() -1) position = 0;
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                RenewMedia();
                mediaPlayer.start();
                play.setImageResource(android.R.drawable.ic_media_pause);
                SetTotalTime();
                UpdateCurrentTime();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position --;
                if (position < 0) position = arraySong.size() -1;
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                RenewMedia();
                mediaPlayer.start();
                play.setImageResource(android.R.drawable.ic_media_pause);
                SetTotalTime();
                UpdateCurrentTime();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // khi thay đổi
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // khi bắt đầu chạm
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // khi chạm xong, phát đúng thời gian seekBar kéo đến
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private  void  FindComponent(){
        timeLast     = (TextView) findViewById(R.id.timeLast);
        totalTime    = (TextView) findViewById(R.id.totalTime);
        title        = (TextView) findViewById(R.id.titleSong);
        seekBar      = (SeekBar) findViewById(R.id.seekBar);
        next         = (ImageButton) findViewById(R.id.btnNext);
        previous     = (ImageButton) findViewById(R.id.btnPrevious);
        play         = (ImageButton) findViewById(R.id.btnPlay);
        stop        = (ImageButton) findViewById(R.id.btnStop);
        imageDisc   = (ImageView) findViewById(R.id.imageViewDisc);
    }

    private void AddSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("My love",R.raw.mylove));
        arraySong.add(new Song("Hello",R.raw.hello));

    }

    private void SetTotalTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        totalTime.setText(timeFormat.format(mediaPlayer.getDuration()));

        // cập nhật thời gian max của Song
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private  void RenewMedia(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        title.setText(arraySong.get(position).getTitle());
    }

    private void UpdateCurrentTime(){
        final Handler handler = new Handler();

        // sau 0.1s tạo handler
        handler.postDelayed(new Runnable() {
            @Override

            // tạo Runable để lấy thời gian hiện tại
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                timeLast.setText(timeFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position ++;
                        if (position > arraySong.size() -1) position = 0;
                        if(mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        RenewMedia();
                        mediaPlayer.start();
                        play.setImageResource(android.R.drawable.ic_media_pause);
                        SetTotalTime();
                        UpdateCurrentTime();
                    }
                });
                // vòng lặp sau 0.1s cập nhật tiếp
                handler.postDelayed(this,100);
            }
        }, 100);
    }
}
