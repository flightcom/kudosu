package com.flightcom.kudosu;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private InterstitialAd mInterstitialAd;
	Button bPlay = null;
	Button bSolve = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mInterstitialAd = new InterstitialAd(this);
        String adUnitId = getResources().getString(R.string.ad_unit_id);
        mInterstitialAd.setAdUnitId(adUnitId);
		
        // Create an ad request.
        AdRequest adRequestBuilder = new AdRequest.Builder()
        	.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
        	.addTestDevice("D74CB3B9E5A0526682270F026BAC2564")
        	.build();

        // Set an AdListener.
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            	Toast.makeText(MainActivity.this,"The interstitial is loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                // Proceed to game.
            	play();
            }
        });
        
        mInterstitialAd.loadAd(adRequestBuilder);

        setContentView(R.layout.activity_main);
		bPlay = (Button) findViewById(R.id.buttonPlay);
		bSolve = (Button) findViewById(R.id.buttonSolve);
		
		bPlay.setOnClickListener(onPlayButtonClickListener);
		bSolve.setOnClickListener(onSolveButtonClickListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private OnClickListener onPlayButtonClickListener = new OnClickListener(){
		
		@Override
		public void onClick(View v){
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                // Proceed to game.
            	play();
            }
		}
	};
	
	private OnClickListener onSolveButtonClickListener = new OnClickListener(){
		
		@Override
		public void onClick(View v){
			Intent solveIntent = new Intent(getApplicationContext(), SolveManualActivity.class);
			startActivity(solveIntent);
		}
	};
	
	private void play() {
		
		Intent playIntent = new Intent(getApplicationContext(), LevelSelectionActivity.class);
		startActivity(playIntent);
		
	}
}
