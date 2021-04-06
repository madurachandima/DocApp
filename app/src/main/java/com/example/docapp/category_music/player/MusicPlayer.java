package com.example.docapp.category_music.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.docapp.R;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

    private Bundle mSongExtraData;
    private ArrayList<File> mSongFileList;
    private int position;

    private SeekBar mSeekBar;
    private TextView mSongTitle;
    private ImageView mPlayBtn;
    private ImageView mNextBtn;
    private ImageView mPrevBtn;
    private TextView mCurrentTime;
    private TextView mFullTime;
    private static MediaPlayer mMediaPlayer;

    private static final String TAG = "MusicPlayer";

    //TODO add song like count .................................................................................................................

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mSeekBar = findViewById(R.id.id_musicProgressBar);
        mSongTitle = findViewById(R.id.id_songTitle);
        mPlayBtn = findViewById(R.id.id_btnPlay);
        mNextBtn = findViewById(R.id.id_btnNextRight);
        mPrevBtn = findViewById(R.id.id_btnNextLeft);
        mCurrentTime = findViewById(R.id.id_currentTime);
        mFullTime = findViewById(R.id.id_fullTime);

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }

        Intent songData = getIntent();
        mSongExtraData = songData.getExtras();

        mSongFileList = (ArrayList) mSongExtraData.getParcelableArrayList("songFileList");
        position = mSongExtraData.getInt("position", 0);

        initMusicPlayer(position);

        //setup play pause button...
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        // set next button
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < mSongFileList.size() - 1) {
                    position++;
                } else {
                    position = 0;
                }
                initMusicPlayer(position);
            }
        });
        //set prev button
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position <= 0) {
                    position = mSongFileList.size() - 1;
                } else {
                    position--;
                }

                initMusicPlayer(position);
            }
        });
    }

    private void initMusicPlayer(int position) {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
        }

        String name = mSongFileList.get(position).getName();
        Log.d(TAG, "initMusicPlayer: " + mSongFileList);
        mSongTitle.setText(name);

        Uri songResourcesUrl = Uri.parse(mSongFileList.get(position).toString());


        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songResourcesUrl);

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //set seekBar maximum duration...
                mSeekBar.setMax(mMediaPlayer.getDuration());


                //set max duration of the song..
                String totalTime = createTimeLabel(mediaPlayer.getDuration());
                mFullTime.setText(totalTime);


                //start musicPlayer...
                mMediaPlayer.start();

                //setIconToPause...
                mPlayBtn.setImageResource(R.drawable.iconpause);


            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mPlayBtn.setImageResource(R.drawable.iconplay);
            }
        });


        //setting up the seekbar...

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayer.seekTo(progress);//seek the song..
                    mSeekBar.setProgress(progress);//set the seekbar progress

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    Message message = new Message();
                    message.what = mMediaPlayer.getCurrentPosition();
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //create handler to set progress..
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            mCurrentTime.setText(createTimeLabel(msg.what));
            mSeekBar.setProgress(msg.what);
        }
    };

    private void play() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayBtn.setImageResource(R.drawable.iconplay);
        } else {

            mMediaPlayer.start();
            mPlayBtn.setImageResource(R.drawable.iconpause);
        }
    }

    public String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        timeLabel += min + ":";

        if (sec < 10) timeLabel += 0;
        timeLabel += sec;

        return timeLabel;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMediaPlayer.stop();
    }
}