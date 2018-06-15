package com.software.abs.videotext;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.Bundle;
import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class GridActivity extends Activity {

    //private static final String MEDIA_PATH = new String("/mnt/sdcard/FreedomMic/test");
    String[] fileList = null;
    GridView gridView;
    //String FILE_PATH = "/mnt/sdcard/Freedommic/test/";
    String MiME_TYPE = "video/mp4";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

//        Realm.init(this);
//        mRealm = Realm.getDefaultInstance();
        updateSongList();
        //getAllVidsNotes();
        gridView = (GridView) findViewById(R.id.gridView1);
        if (fileList != null) {
            gridView.setAdapter(new ImageAdapter(this, fileList));
        }
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        ((TextView) v.findViewById(R.id.grid_item_label))
                                .getText(), Toast.LENGTH_SHORT).show();
                File sdCard = Environment.getExternalStorageDirectory();
                String videoFilePath = sdCard.getAbsolutePath() + "/vidText/Rec Videos/"+ fileList[position];
                System.out
                        .println(MiME_TYPE+"**MiME_TYPE*****************videoFilePath****************"
                                + videoFilePath);

                Intent intent = new Intent(GridActivity.this, VideoPlayerActivity.class);
                intent.putExtra("path", videoFilePath);
                startActivity(intent);
                /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                File newFile = new File(videoFilePath);
                intent.setDataAndType(Uri.fromFile(newFile), MiME_TYPE);
                startActivity(intent);*/

            }
        });

    }

    public void updateSongList() {
        File sdCard = Environment.getExternalStorageDirectory();
        File videoFiles = new File(sdCard.getAbsolutePath() + "/vidText/Rec Videos");

        //File videoFiles = new File(MEDIA_PATH);
        Log.d("**Value of videoFiles**", videoFiles.toString());

        if (videoFiles.isDirectory()) {
            fileList = videoFiles.list();
        }
        if (fileList == null) {
            System.out.println("File doesnot exit");
            Toast.makeText(this, "There is no file", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("fileList****************" + fileList);
            for (int i = 0; i < fileList.length; i++) {
                Log.e("Video:" + i + " File name", fileList[i]);
            }
        }
    }

   /* private void getAllVidsNotes() {
        //RealmResults<VideoData> results = mRealm.where(VideoData.class).findAll();
        mRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
          //  personDetailsModelArrayList.add(results.get(i));
            System.out
                    .println("Title>>>>>>>>>>>>>>>"
                            +results.get(i).getTitle());
            System.out
                    .println("Title>>>>>>>>>>>>>>>"
                            +results.get(i).getDateTimeMillis());
        }

        // if(results.size()>0)
        //     id = mRealm.where(VideoData.class).max("id").intValue() + 1;
        mRealm.commitTransaction();
        //personDetailsAdapter.notifyDataSetChanged();
    }
*/

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private final String[] VideoValues;

        ImageAdapter(Context context, String[] VideoValues) {
            this.context = context;
            this.VideoValues = VideoValues;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                File sdCard = Environment.getExternalStorageDirectory();
                File videoFiles = new File(sdCard.getAbsolutePath() + "/vidText/Rec Videos");
                gridView = new View(context);

                // get layout from gridlayout.xml
                gridView = inflater.inflate(R.layout.gridlayout, null);

                // set value into textview
                TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
                textView.setText(fileList[position]);
                System.out.println("value of fileList[position]" + fileList[0]);
                // set image
                ImageView imageThumbnail = (ImageView) gridView.findViewById(R.id.grid_item_image);

                //Bitmap bmThumbnail;

                System.out
                        .println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> file path>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                                +videoFiles.getAbsolutePath() + fileList[position]);

             //   Bitmap bmThumbnail = createThumbnailAtTime(videoFiles.getAbsolutePath()+"/"  + fileList[position],3);

                Glide.with(context)
                        .load(videoFiles.getAbsolutePath()+"/" + fileList[position])
                        .placeholder(R.drawable.ic_media_play)
                        .into(imageThumbnail);

                //  bmThumbnail = ThumbnailUtils.createVideoThumbnail(FILE_PATH   + fileList[position],   MediaStore.Video.Thumbnails.MINI_KIND);
                /*      bmThumbnail = ThumbnailUtils.createVideoThumbnail(videoFiles.getPath()   + fileList[position],   MediaStore.Video.Thumbnails.MINI_KIND);
                if (bmThumbnail != null) {
                    System.out
                            .println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> THUMB NAIL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                    imageThumbnail.setImageBitmap(bmThumbnail);
                } else {
                    System.out
                            .println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>NO THUMB NAIL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                }*/

            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }

        public int getCount() {
            // return 0;
            return VideoValues.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

    }

    private Bitmap createThumbnailAtTime(String filePath, int timeInSeconds){
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(filePath);
        //api time unit is microseconds
        return mMMR.getFrameAtTime(timeInSeconds*1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //personDetailsModelArrayList.clear();
        //mRealm.close();
    }
}