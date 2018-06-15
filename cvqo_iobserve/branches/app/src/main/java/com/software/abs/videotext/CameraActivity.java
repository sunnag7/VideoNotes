package com.software.abs.videotext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;

import android.widget.Toast;
import android.widget.VideoView;

public class CameraActivity extends Activity implements Callback, OnClickListener, CompoundButton.OnCheckedChangeListener {
    private VideoView videoView = null;
    private MediaController mc = null;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mediaRecorder = new MediaRecorder();
    private Camera mCamera;
    private ImageButton  btnStart, butnNote;
    ArrayList<VideoData> mVidNoteArr = null;
    CheckBox chkb1,chkb2,chkb3,chkb4,chkb5,chkb6;
    EditText addNotes;
    Chronometer myChronometer;
    Boolean isRecStarted = false, isNoteAdded =false;
   // private Realm mRealm;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Realm.init(this);
       // mRealm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_camera);
        mVidNoteArr = new ArrayList<VideoData>();
        chkb1 = (CheckBox) findViewById(R.id.checkBox);
        chkb2 = (CheckBox) findViewById(R.id.checkBox1);
        chkb3 = (CheckBox) findViewById(R.id.checkBox2);
        chkb4 = (CheckBox) findViewById(R.id.checkBox3);
        chkb5 = (CheckBox) findViewById(R.id.checkBox4);
        chkb6 = (CheckBox) findViewById(R.id.checkBox5);

        chkb1.setOnCheckedChangeListener(this);
        chkb2.setOnCheckedChangeListener(this);
        chkb6.setOnCheckedChangeListener(this);
        chkb3.setOnCheckedChangeListener(this);
        chkb4.setOnCheckedChangeListener(this);
        chkb5.setOnCheckedChangeListener(this);

        surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mCamera = Camera.open();
        myChronometer = (Chronometer)findViewById(R.id.chronometer);
        // mCamera.setDisplayOrientation(90);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        butnNote = (ImageButton ) findViewById(R.id.button2);
        butnNote.setOnClickListener(this);
        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        addNotes = (EditText) findViewById(R.id.editText);

    }

    File file;
    String fileName = "";
    @SuppressLint("NewApi")
    protected void startRecording() throws IOException {
        if (mCamera == null)
            mCamera = Camera.open();

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/vidText/Rec Videos");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Date date = new Date();
        fileName =  "/rec" + date.toString().replace(" ", "_").replace(":", "_") + ".mp4";
        file = new File(dir, fileName);

        mediaRecorder = new MediaRecorder();
        mCamera.lock();
        mCamera.unlock();
        // Please maintain sequence of following code.
        // If you change sequence it will not work.

        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        //mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setOutputFile(dir + fileName);
        mediaRecorder.setOrientationHint(90);
        mediaRecorder.prepare();
        mediaRecorder.start();
        refreshGallery(file);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    protected void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mCamera.release();
            // mCamera.lock();
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera != null) {
            Parameters params = mCamera.getParameters();
            mCamera.setParameters(params);
            mCamera.setDisplayOrientation(90);
            Log.i("Surface", "Created");
        } else {
            Toast.makeText(getApplicationContext(), "Camera not available!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }

    private static int id = 1;
    long mTimer= 0, starttime =0, stoptime =0 ;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                if (!isRecStarted) {
                    //btnStart.setText("Stop");
                    btnStart.setImageResource(R.drawable.stop);
                    myChronometer.start();
                    starttime = System.currentTimeMillis();
                    Log.d("starttime ", "onClick:time "+starttime);
                    try {
                        startRecording();
                    } catch (IOException e) {
                        String message = e.getMessage();
                        Log.i(null, "Problem " + message);
                        mediaRecorder.release();
                        e.printStackTrace();
                    }
                    isRecStarted = true;
                } else {
                    //btnStart.setText("Start");
                    btnStart.setImageResource(R.drawable.dot_circle);
                    myChronometer.setBase(SystemClock.elapsedRealtime());
                    myChronometer.stop();

                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
                break;

            case R.id.button2:
               // long mTimer= 0, starttime =0, stoptime =0 ;
                if (isRecStarted && !isNoteAdded) {
                    addNotes.setText("");
                    addNotes.setVisibility(View.VISIBLE);
                    //butnNote.setText("Save");
                    butnNote.setImageResource(R.drawable.save_file);

                    Log.d("stoptime ", "onClick:time "+stoptime);
                    mTimer = (long) (SystemClock.elapsedRealtime() - myChronometer .getBase());
                    //add xml coding
                    isNoteAdded = true;
                }
                else{
                    //butnNote.setText("Add Note");
                    butnNote.setImageResource(R.drawable.text_file);
                    addNotes.setVisibility(View.GONE);

                    isNoteAdded = false;
                    onBackPressed();
                  //  addDataToRealm(model);
                }

                break;

            default:
                break;
        }
    }

    public void startTimer(){
        new CountDownTimer(20*60000, 1000) {

            public void onTick(long millisUntilFinished) {
              //  _tv.setText("seconds remaining: " +new SimpleDateFormat("mm:ss:SS").format(new Date( millisUntilFinished)));
            }

            public void onFinish() {
              //  _tv.setText("done!");
            }
        }.start();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkBox:
                if (isChecked) {
                    //cbPickDrop.setChecked(false);
                    VideoData model = new VideoData();
                    stoptime = 0;
                    stoptime = System.currentTimeMillis()-starttime;
                    model.setDateTimeMillis(stoptime);
                    model.setPath(file.getPath());
                    model.setTitle(fileName);
                    model.setNote(chkb1.getText().toString());

                    DBHelper db = new DBHelper(this);
                    db.insertNotes(model);
                }
                break;
            case R.id.checkBox1:
                if (isChecked) {
                    VideoData model = new VideoData();
                    stoptime = 0;
                    stoptime = System.currentTimeMillis()-starttime;
                    model.setDateTimeMillis(stoptime);
                    model.setPath(file.getPath());
                    model.setTitle(fileName);
                    model.setNote(chkb2.getText().toString());

                    DBHelper db = new DBHelper(this);
                    db.insertNotes(model);
                }
                break;
            case R.id.checkBox2:
                if (isChecked) {
                    VideoData model = new VideoData();
                    stoptime = 0;
                    stoptime = System.currentTimeMillis()-starttime;
                    model.setDateTimeMillis(stoptime);
                    model.setPath(file.getPath());
                    model.setTitle(fileName);
                    model.setNote(chkb3.getText().toString());

                    DBHelper db = new DBHelper(this);
                    db.insertNotes(model);
                }
                break;
            case R.id.checkBox3:
                if (isChecked) {
                    VideoData model = new VideoData();
                    stoptime = 0;
                    stoptime = System.currentTimeMillis()-starttime;
                    model.setDateTimeMillis(stoptime);
                    model.setPath(file.getPath());
                    model.setTitle(fileName);
                    model.setNote(chkb4.getText().toString());

                    DBHelper db = new DBHelper(this);
                    db.insertNotes(model);
                }
                break;
            case R.id.checkBox4:
                if (isChecked) {
                    VideoData model = new VideoData();
                    stoptime = System.currentTimeMillis()-starttime;
                    model.setDateTimeMillis(stoptime);
                    model.setPath(file.getPath());
                    model.setTitle(fileName);
                    model.setNote(chkb5.getText().toString());

                    DBHelper db = new DBHelper(this);
                    db.insertNotes(model);
                }
                break;
            case R.id.checkBox5:
                if (isChecked) {
                    VideoData model = new VideoData();
                    stoptime = System.currentTimeMillis()-starttime;
                    model.setDateTimeMillis(stoptime);
                    model.setPath(file.getPath());
                    model.setTitle(fileName);
                    model.setNote(chkb6.getText().toString());

                    DBHelper db = new DBHelper(this);
                    db.insertNotes(model);
                }
                break;
        }
    }

    //private static ArrayList<VideoData> arrViddata = new ArrayList<>();

   /* private void addDataToRealm(VideoData model) {
        mRealm.beginTransaction();
        VideoData detailsModel = mRealm.createObject(VideoData.class);
        detailsModel.setDateTimeMillis(model.getDateTimeMillis());
        detailsModel.setPath(model.getPath());
        detailsModel.setTitle(model.getTitle());
        detailsModel.setNote(model.getNote());
      // detailsModel = model

        arrViddata.add(model);
        mRealm.commitTransaction();


        //personDetailsAdapter.notifyDataSetChanged();
       // id++;
    }*/

}