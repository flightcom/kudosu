package com.flightcom.kudosu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

public class SolveActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Camera mCamera;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_solve);
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//    	Toast.makeText(getApplicationContext(), "Take a picture of your grid", Toast.LENGTH_SHORT).show();
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

}
