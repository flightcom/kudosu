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
import android.widget.FrameLayout;
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

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
		
		Paint paint = new Paint();
		int dim;
		int caseDim;
		Canvas canvas = new Canvas();
		dim = (int) ((Integer)width*0.9);
		caseDim = (int)width/9;
		paint.setColor(Color.WHITE);
		canvas.drawLine(0, 0, 0, dim, paint);
		canvas.drawLine(0, dim, dim, dim, paint);
		canvas.drawLine(dim, dim, dim, 0, paint);
		canvas.drawLine(dim, 0, 0, 0, paint);
		for(int i = 1; i < 9; i++) {
			canvas.drawLine(0, caseDim*i, dim, caseDim*i, paint);
			canvas.drawLine(caseDim*i, 0, caseDim*i, dim, paint);
		}

        //drawView = new DrawView(this, width);
        ImageView iv = (ImageView)findViewById(R.id.imageview);
        iv.draw(canvas);
        
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
