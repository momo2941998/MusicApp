package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindComponent();
        AddSong();
        RenewMedia();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setImageResource(android.R.drawable.ic_media_pause);
                }else {
                    mediaPlayer.start();
                    play.setImageResource((android.R.drawable.ic_media_play));
                }
                SetTotalTime();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
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
    }

    private void AddSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("My love",R.raw.mylove));
        arraySong.add(new Song("Hello",R.raw.hello));

    }

    private void SetTotalTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
        totalTime.setText(timeFormat.format(mediaPlayer.getDuration()));

        // cập nhật thời gian max của song
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private  void RenewMedia(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        title.setText(arraySong.get(position).getTitle());
    }
}
