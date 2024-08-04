package com.example.mbeat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.BlastVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btnPlay, btnNext, btnPrevious, btnfastForward, btnfastBackward, btnAboutMe;
    TextView txtSongName, txtSongStart, txtSongEnd;
    SeekBar seekMusicBar;
    ImageView imageView;
    BlastVisualizer blastVisualizer;

    String songName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    AnimatorSet animatorSet;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);

        // Initialize UI components
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);
        btnfastForward = findViewById(R.id.btnfastForward);
        btnfastBackward = findViewById(R.id.btnfastBackward);
        txtSongName = findViewById(R.id.txtSong);
        txtSongStart = findViewById(R.id.txtSongStart);
        txtSongEnd = findViewById(R.id.txtSongEnd);
        seekMusicBar = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.imgView);
        blastVisualizer = findViewById(R.id.blast);
        btnAboutMe = findViewById(R.id.aboutMe);

        // Check if mediaPlayer is not null and release if necessary
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        position = bundle.getInt("pos", 0);
        txtSongName.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        songName = mySongs.get(position).getName();
        txtSongName.setText(songName);

        // Initialize mediaPlayer and start playback
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            startAnimation(imageView); // Start the rotation animation
            updateSeekBar(); // Start updating the SeekBar
        } else {
            Log.e("PlayerActivity", "MediaPlayer is null");
            return; // Exit the method to avoid null pointer exceptions
        }

        // Set up SeekBar
        seekMusicBar.setMax(mediaPlayer.getDuration());
        seekMusicBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
        seekMusicBar.getThumb().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);

        seekMusicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        txtSongEnd.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtSongStart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        btnAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutmeintent = new Intent(PlayerActivity.this, AboutMe.class);
                startActivity(aboutmeintent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    btnPlay.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                    if (animatorSet != null && animatorSet.isRunning()) {
                        animatorSet.cancel(); // Stop the animation
                    }
                } else {
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                    startAnimation(imageView); // Start the rotation animation
                    updateSeekBar(); // Resume SeekBar updates
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.removeCallbacks(updateSeekBarRunnable); // Stop updating SeekBar
                btnNext.performClick(); // Move to the next song
            }
        });

        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1) {
            blastVisualizer.setAudioSessionId(audioSessionId);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position + 1) % mySongs.size();
                Uri uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName = mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();
                startAnimation(imageView); // Restart the rotation animation
                updateSeekBar(); // Start updating the SeekBar for the new song
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position - 1) < 0) ? (mySongs.size() - 1) : position - 1;
                Uri uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songName = mySongs.get(position).getName();
                txtSongName.setText(songName);
                mediaPlayer.start();
                startAnimation(imageView); // Restart the rotation animation
                updateSeekBar(); // Start updating the SeekBar for the previous song
            }
        });

        btnfastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                }
            }
        });

        btnfastBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                }
            }
        });
    }

    private final Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                try {
                    seekMusicBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 500); // Update every 500ms
                } catch (IllegalStateException e) {
                    Log.e("PlayerActivity", "Error updating SeekBar: " + e.getMessage());
                }
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(updateSeekBarRunnable, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setBackgroundResource(R.drawable.play);
            if (animatorSet != null && animatorSet.isRunning()) {
                animatorSet.cancel(); // Stop the animation
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null); // Stop all callbacks
    }

    public void startAnimation(View view) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotateAnimator.setDuration(2000); // 2 seconds for a full rotation
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimator.setRepeatMode(ObjectAnimator.RESTART);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator);
        animatorSet.start();
    }

    public String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        time += min + ":";
        if (sec < 10) {
            time += "0";
        }
        time += sec;
        return time;
    }
}
