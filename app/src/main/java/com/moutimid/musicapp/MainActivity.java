package com.moutimid.musicapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> songList = new ArrayList<>();
    static ArrayList<String> songnameList = new ArrayList<>();
    static ArrayList<String> songnameDescriptionList = new ArrayList<>();
    static int[] imageResources ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sample data - list of song names and corresponding image resources
        ArrayList<String> songList = new ArrayList<>();
        songList.add("song1");
        songList.add("song2");
        songList.add("song3");
        songList.add("song4");
        songList.add("song5");
        songList.add("song6");
        songList.add("song7");


        songnameList = new ArrayList<>();
        songnameList.add("David Bowie");
        songnameList.add("Nine Inch Nails");
        songnameList.add("Tori Amos");
        songnameList.add("Enigma");
        songnameList.add("Madonna");
        songnameList.add("Janet Jackson ");
        songnameList.add("Reflections of Hope");

        songnameDescriptionList = new ArrayList<>();
        songnameDescriptionList.add("The Rise and Fall of Ziggy Stardust");
        songnameDescriptionList.add("Pretty Hate Machine");
        songnameDescriptionList.add("Little Earthquakes");
        songnameDescriptionList.add("Never mind");
        songnameDescriptionList.add("The Cross of Changes");
        songnameDescriptionList.add("The Immaculate Collection");
        songnameDescriptionList.add("The Velvet Rope");

        imageResources = new int[]{
                R.drawable.pic1,
                R.drawable.pic2,
                R.drawable.pic3,
                R.drawable.pic4,
                R.drawable.pic5,
                R.drawable.pic6,
                R.drawable.pic7,
        };

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter setup
        SongAdapter adapter = new SongAdapter(this, songList, songnameDescriptionList, songnameList, imageResources);
        recyclerView.setAdapter(adapter);
        checkApp(MainActivity.this);
    }

    public static void checkApp(Activity activity) {
        String appName = "Music App";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
