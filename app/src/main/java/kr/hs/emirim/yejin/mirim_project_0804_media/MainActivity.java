package kr.hs.emirim.yejin.mirim_project_0804_media;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button btnPlay, btnStop, btnPause;
    TextView textMusic;
    ProgressBar proBar;
    ArrayList<String> musicList;
    String selectedMusic;

    String musicPath = Environment.getExternalStorageDirectory().getPath()+"/";
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MP3 Player");
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, MODE_PRIVATE);
        listView = findViewById(R.id.listView);
        btnPlay = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnPause = findViewById(R.id.btn_wait);
        textMusic = findViewById(R.id.text_music);
        proBar = findViewById(R.id.progress);
        musicList = new ArrayList<String>();
        File[] files = new File(musicPath).listFiles();
        String fileName, extName;
        for(File file : files){
            fileName = file.getName();
            extName = fileName.substring(fileName.length()-3);
            if (extName.equals("mp3")) {
                musicList.add(fileName);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, musicList);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);
            listView.setItemChecked(0, true);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedMusic = musicList.get(i);
                }
            });
            selectedMusic = musicList.get(0);
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(musicPath + selectedMusic);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        btnPlay.setClickable(false);
                        btnStop.setClickable(true);
                        btnPause.setClickable(true);
                        textMusic.setText("현재 재생 음악 : "+selectedMusic);
                        proBar.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    btnPlay.setClickable(true);
                    btnStop.setClickable(false);
                    btnPause.setClickable(false);
                    textMusic.setText("현재 재생 음악 : ");
                    proBar.setVisibility(View.INVISIBLE);

                }
            });
            btnPause.setOnClickListener(new View.OnClickListener() {
                int position;
                @Override
                public void onClick(View view) {
                    if(btnPause.getText().equals("일시정지")){
                        position = mediaPlayer.getCurrentPosition();
                        mediaPlayer.pause();
                        btnPlay.setClickable(true);
                        btnStop.setClickable(true);
                        btnPause.setClickable(true);
                        btnPause.setText("이어듣기");
                        proBar.setVisibility(View.INVISIBLE);
                    } else {
                        mediaPlayer.seekTo(position);
                        mediaPlayer.start();
                        btnPlay.setClickable(true);
                        btnStop.setClickable(true);
                        btnPause.setClickable(true);
                        btnPause.setText("일시정지");
                        proBar.setVisibility(View.VISIBLE);
                    }

                }
            });
            btnStop.setClickable(false);
            btnPause.setClickable(false);
        }
    }
}