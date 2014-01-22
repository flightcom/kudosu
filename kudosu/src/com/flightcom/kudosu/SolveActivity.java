package com.flightcom.kudosu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SolveActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Camera mCamera;
    private CameraPreview mPreview;
    private DrawView drawView;
    private int height;
    private int width;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);
		RelativeLayout lSolve = (RelativeLayout) findViewById(R.id.canvas);
		RelativeLayout lGrid = (RelativeLayout) findViewById(R.id.grid);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;

        drawView = new DrawView(this, width);
        lGrid.addView(drawView);

    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == Activity.RESULT_OK) {
	        	Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	        	Toast.makeText(getApplicationContext(), "CANCELED", Toast.LENGTH_SHORT).show();
	        } else {
	            // Image capture failed, advise user
	        	Toast.makeText(getApplicationContext(), "PROBLEM ?", Toast.LENGTH_SHORT).show();
	        }
	    }
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
}
