package com.flightcom.kudosu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button bPlay = null;
	Button bSolve = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			Intent playIntent = new Intent(getApplicationContext(), LevelSelectionActivity.class);
			startActivity(playIntent);
		}
	};
	
	private OnClickListener onSolveButtonClickListener = new OnClickListener(){
		
		@Override
		public void onClick(View v){
			Intent solveIntent = new Intent(getApplicationContext(), SolveManualActivity.class);
			startActivity(solveIntent);
		}
	};
}
