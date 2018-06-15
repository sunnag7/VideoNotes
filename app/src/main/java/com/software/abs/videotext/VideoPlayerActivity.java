package com.software.abs.videotext;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import java.io.IOException;
import java.util.ArrayList;

public class VideoPlayerActivity extends Activity  {

    private VidNoteAdapter vidAdapter;
    FullscreenVideoLayout videoLayout;
    private ListView lvVidNoteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        //Realm.init(this);
        //mRealm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);
        lvVidNoteList = (ListView) findViewById(R.id.lstNote);

        //seekTo = (Button) findViewById(R.id.btnSeek);

        Uri videoUri = Uri.parse(path);
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = path.substring(path.lastIndexOf('/') + 1);
        //getAllVidsNotes(fileName);

        DBHelper dbHelper = new DBHelper(this);
        arrViddata = dbHelper.getData(fileName);
        setVidAdapter();
        vidAdapter.notifyDataSetChanged();
        /*   seekTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoLayout.seekTo(5000);
            }
        });*/

        lvVidNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoLayout.seekTo((int) arrViddata.get(position).getDateTimeMillis());
                Log.d("seek time ", "onClick:time "+ arrViddata.get(position).getDateTimeMillis());
            }
        });
    }

    private void setVidAdapter() {
        vidAdapter = new VidNoteAdapter(VideoPlayerActivity.this, arrViddata);
        lvVidNoteList.setAdapter(vidAdapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private static ArrayList<VideoData> arrViddata = new ArrayList<>();
    /*private void getAllVidsNotes(String fileName) {
        // mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        RealmResults<VideoData> results = mRealm.where(VideoData.class).equalTo("title", fileName).findAll();
        for (int i = 0; i < results.size(); i++) {
            arrViddata.add(results.get(i));
            System.out
                    .println(fileName+" Title>>>>>>>>>>>>>>>"
                            +results.get(i).getTitle());
            System.out
                    .println("Title>>>>>>>>>>>>>>>"
                            +results.get(i).getDateTimeMillis());
        }

        // if(results.size()>0)
        //     id = mRealm.where(VideoData.class).max("id").intValue() + 1;
        mRealm.commitTransaction();
        vidAdapter.notifyDataSetChanged();
    }*/
}