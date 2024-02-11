package com.moutimid.musicapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.makeramen.roundedimageview.RoundedImageView;
import com.moutimid.musicapp.Model.RepeatMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MusicPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private TextView song_name, artist_name, duration_played, duration_total;
    private SeekBar seekBar;
    RoundedImageView song_image_view;
    private ImageView playButton, nextButton, prevButton, shuffleButton, repeatButton;
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private List<Song> originalSongList;
    private int currentPosition = 0;
    private boolean isPlaying = false;
    private boolean isShuffleOn = false;
    private RepeatMode repeatMode = RepeatMode.NO_REPEAT;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initViews();
        initMediaPlayer();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseMusic();
                } else {
                    playMusic();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSong();
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShuffleOn) {
                    originalSongList = new ArrayList<>(songList);
                    Collections.shuffle(songList);
                    toggleShuffle(true);
                } else {
                    songList.clear();
                    songList.addAll(originalSongList);
                    toggleShuffle(false);
                }
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRepeatMode();
                updateRepeatButtonIcon();
            }
        });

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_played.setText(formatted(mCurrentPosition));
                    duration_total.setText(formatted(mediaPlayer.getDuration() / 1000));
                }
                handler.postDelayed(this, 1000);
            }
        };

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress * 1000);
                    duration_played.setText(formatted(progress));
                    duration_total.setText(formatted(mediaPlayer.getDuration() / 1000));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        if (!isPlaying) {
            playMusic();
        }
    }

    private void initViews() {
        shuffleButton = findViewById(R.id.shuffle);
        repeatButton = findViewById(R.id.repeatButton);
        song_name = findViewById(R.id.song_name_text_view);
        artist_name = findViewById(R.id.song_details_text_view);
        duration_played = findViewById(R.id.duration_played);
        duration_total = findViewById(R.id.duration_total);
        seekBar = findViewById(R.id.seek_bar);
        playButton = findViewById(R.id.play_pause);
        nextButton = findViewById(R.id.next);
        prevButton = findViewById(R.id.prev);
        song_image_view = findViewById(R.id.song_image_view);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        songList = new ArrayList<>();
        songList.add(new Song("David Bowie", "The Rise and Fall of Ziggy Stardust", R.drawable.pic1, R.raw.song1));
        songList.add(new Song("Nine Inch Nails", "Pretty Hate Machine", R.drawable.pic2, R.raw.song2));
        songList.add(new Song("Tori Amos", "Little Earthquakes", R.drawable.pic3, R.raw.song3));
        songList.add(new Song("Enigma", "Never mind", R.drawable.pic4, R.raw.song4));
        songList.add(new Song("Madonna", "The Cross of Changes", R.drawable.pic5, R.raw.song5));
        songList.add(new Song("Janet Jackson", "The Immaculate Collection", R.drawable.pic6, R.raw.song6));
        songList.add(new Song("Reflections of Hope", "The Velvet Rope", R.drawable.pic7, R.raw.song7));  // Add more songs as needed
        currentPosition = getIntent().getIntExtra("position", 0);
        try {
            mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + songList.get(currentPosition).getMusicResourceId()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        duration_total.setText(formatted(mediaPlayer.getDuration() / 1000));

        song_name.setText(songList.get(currentPosition).getName());
        artist_name.setText(songList.get(currentPosition).getDescription());
        song_image_view.setImageResource(songList.get(currentPosition).getImageResourceId());
        playButton.setImageResource(R.drawable.ic_baseline_pause);

        duration_total.setText(formatted(mediaPlayer.getDuration() / 1000));
    }

    private void playMusic() {
        mediaPlayer.start();
        isPlaying = true;
        playButton.setImageResource(R.drawable.ic_baseline_pause);
        handler.postDelayed(updateSeekBar, 0);
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        isPlaying = false;
        playButton.setImageResource(R.drawable.ic_baseline_play_arrow);
        handler.removeCallbacks(updateSeekBar);
    }

    private void nextSong() {
        if (repeatMode == RepeatMode.REPEAT_ONE) {
            // If repeat one mode is enabled, just play the current song again
            playNextOrPreviousSong();
            return;
        }

        if (isShuffleOn) {
            currentPosition = new Random().nextInt(songList.size());
        } else {
            currentPosition = (currentPosition + 1) % songList.size();
        }

        song_name.setText(songList.get(currentPosition).getName());
        artist_name.setText(songList.get(currentPosition).getDescription());
        song_image_view.setImageResource(songList.get(currentPosition).getImageResourceId());
        playButton.setImageResource(R.drawable.ic_baseline_pause);

        duration_total.setText(formatted(mediaPlayer.getDuration() / 1000));
        playNextOrPreviousSong();
    }

    private void prevSong() {
        if (repeatMode == RepeatMode.REPEAT_ONE) {
            // If repeat one mode is enabled, just play the current song again
            playNextOrPreviousSong();
            return;
        }

        if (isShuffleOn) {
            currentPosition = new Random().nextInt(songList.size());
        } else {
            currentPosition = (currentPosition - 1 + songList.size()) % songList.size();
        }

        song_name.setText(songList.get(currentPosition).getName());
        artist_name.setText(songList.get(currentPosition).getDescription());
        song_image_view.setImageResource(songList.get(currentPosition).getImageResourceId());
        playButton.setImageResource(R.drawable.ic_baseline_pause);
        duration_total.setText(formatted(mediaPlayer.getDuration() / 1000));
        playNextOrPreviousSong();
    }

    private void playNextOrPreviousSong() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + songList.get(currentPosition).getMusicResourceId()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        Song currentSong = songList.get(currentPosition);
        song_name.setText(currentSong.getName());
        artist_name.setText(currentSong.getDescription());
        song_image_view.setImageResource(currentSong.getImageResourceId());

        duration_total.setText(formatted(mediaPlayer.getDuration() / 1000));

        isPlaying = true;
    }

    private void toggleShuffle(boolean shuffleEnabled) {
        isShuffleOn = shuffleEnabled;
        if (isShuffleOn) {
            shuffleButton.setImageResource(R.drawable.ic_baseline_shuffle_24);
        } else {
            shuffleButton.setImageResource(R.drawable.ic_baseline_shuffle_off);
        }
    }

    private void toggleRepeatMode() {
        switch (repeatMode) {
            case NO_REPEAT:
                repeatMode = RepeatMode.REPEAT_ONE;
                break;
            case REPEAT_ONE:
                repeatMode = RepeatMode.REPEAT_ALL;
                break;
            case REPEAT_ALL:
                repeatMode = RepeatMode.NO_REPEAT;
                break;
        }
    }

    private void updateRepeatButtonIcon() {
        switch (repeatMode) {
            case NO_REPEAT:
                repeatButton.setImageResource(R.drawable.ic_baseline_repeat_off);
                break;
            case REPEAT_ONE:
                repeatButton.setImageResource(R.drawable.repeat);
                break;
            case REPEAT_ALL:
                repeatButton.setImageResource(R.drawable.ic_baseline_repeat);
                break;
        }
    }

    private String formatted(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (repeatMode == RepeatMode.NO_REPEAT) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            nextSong();
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

    public void onBack(View view) {
        pauseMusic();
        onBackPressed();
    }
}
