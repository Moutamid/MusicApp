package com.moutimid.musicapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MusicPlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ArrayList<String> playlist;
    private int currentSongIndex;
    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Get playlist from intent
        playlist = getIntent().getStringArrayListExtra("playlist");
        currentSongIndex = getIntent().getIntExtra("position", 0);
        String name1 = getIntent().getStringExtra("name");
        String details = getIntent().getStringExtra("details");
        // Initialize UI elements
        TextView song_name_text_view = findViewById(R.id.song_name_text_view);
        TextView name = findViewById(R.id.name);
        TextView song_details_text_view = findViewById(R.id.song_details_text_view);
        name.setText(name1);
        song_name_text_view.setText(name1);
        song_details_text_view.setText(details);
        ImageView playButton = findViewById(R.id.play_pause_button);
        ImageView previousButton = findViewById(R.id.previous_button);
        ImageView nextButton = findViewById(R.id.next_button);
        ImageView shuffleButton = findViewById(R.id.shuffle_button);
        seekBar = findViewById(R.id.seek_bar);

        // Set up playback controls
        playButton.setOnClickListener(v -> togglePlayPause());
        previousButton.setOnClickListener(v -> playPreviousSong());
        nextButton.setOnClickListener(v -> playNextSong());
        shuffleButton.setOnClickListener(v -> shuffleSongs());

        // Set up seek bar
        mediaPlayer.setOnPreparedListener(mp -> {
            seekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar();
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });

        // Play the selected song
        playSong(currentSongIndex);
    }

    private void playSong(int songIndex) {
        try {
            mediaPlayer.reset();
            int resourceId = getResources().getIdentifier(playlist.get(songIndex), "raw", getPackageName());
            mediaPlayer = MediaPlayer.create(this, resourceId);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopSong() {
        mediaPlayer.stop();
    }

    private void playNextSong() {
        currentSongIndex++;
        if (currentSongIndex >= playlist.size()) {
            currentSongIndex = 0; // Wrap around to the first song
        }
        stopSong();
        playSong(currentSongIndex);
    }

    private void playPreviousSong() {
        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = playlist.size() - 1; // Wrap around to the last song
        }
        stopSong();
        playSong(currentSongIndex);
    }

    private void togglePlayPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
            updateSeekBar();
        }
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(this::updateSeekBar, 1000); // Update seek bar every second
        }
    }

    private void shuffleSongs() {
        Random random = new Random();
        currentSongIndex = random.nextInt(playlist.size());
        stopSong();
        playSong(currentSongIndex);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updateSeekBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
