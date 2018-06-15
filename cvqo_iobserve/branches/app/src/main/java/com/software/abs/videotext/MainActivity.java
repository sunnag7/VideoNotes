package com.software.abs.videotext;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends Activity implements OnClickListener{
    private int requestCodeForCamera=1;
    private TextView timerTextView,videoDurationTextView;
    private static String TAG = "PermissionDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openCamera = (Button) findViewById(R.id.button);
        Button openVids = (Button) findViewById(R.id.button3);
        openVids.setOnClickListener(this);
        requestPermission();

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAPTURE_VIDEO_OUTPUT);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            openCamera.setOnClickListener(this);
        }

        timerTextView=(TextView)findViewById(R.id.timer);
        videoDurationTextView=(TextView)findViewById(R.id.videoDurationTextView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            //intent.putExtra("time", 15);
            //intent.putExtra("url", "/sdcard/recordingvideo.mp4");
            startActivityForResult(intent, requestCodeForCamera);
        }
        else if (v.getId()==R.id.button3){
            Intent i = new Intent(MainActivity.this, GridActivity.class);
            // Pass String arrays FilePathStrings
            startActivity(i);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCodeForCamera==requestCode){
            try{
                Bundle extras=data.getExtras();
                if(extras!=null){
                    if(extras.containsKey("url")){
                        timerTextView.setText(extras.getString("url").toString());
                    }
                    if(extras.containsKey("videoDuration")){
                        timerTextView.setText(extras.getString("videoDuration").toString());
                    }
                }else{
                    timerTextView.setText("not url");
                }
            }catch(Exception ignored){
            }
        }
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

}
