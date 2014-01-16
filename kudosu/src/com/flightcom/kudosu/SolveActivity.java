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
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SolveActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
    }
    
/*    private void drawGrid(){
    	
		// On récupère les dimensions de l'écran
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;
		
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		Bitmap b = Bitmap.createBitmap(width-50, height-50, Bitmap.Config.ARGB_8888);
        //Canvas c = new Canvas(b);
        Canvas c = (RelativeLayout) findViewById(R.id.canvas);
        c.drawLine(50, 50, 100, 100, p);
        DrawCanvas dc = new DrawCanvas
    }*/
    
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
