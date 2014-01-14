package com.flightcom.kudosu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LevelSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_selection);
		
		Button level1 = (Button) findViewById(R.id.buttonLevel1);
		Button level2 = (Button) findViewById(R.id.buttonLevel2);
		Button level3 = (Button) findViewById(R.id.buttonLevel3);
		Button level4 = (Button) findViewById(R.id.buttonLevel4);
		Button level5 = (Button) findViewById(R.id.buttonLevel5);

		level1.setOnClickListener(onLevelSelectionClickListener);
		level2.setOnClickListener(onLevelSelectionClickListener);
		level3.setOnClickListener(onLevelSelectionClickListener);
		level4.setOnClickListener(onLevelSelectionClickListener);
		level5.setOnClickListener(onLevelSelectionClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_selection, menu);
		return true;
	}

	private OnClickListener onLevelSelectionClickListener = new OnClickListener(){
		
		int level;
		
		@Override
		public void onClick(View v){
			
			switch(v.getId()){
				case R.id.buttonLevel1: level = 1; break;
				case R.id.buttonLevel2: level = 2; break;
				case R.id.buttonLevel3: level = 3; break;
				case R.id.buttonLevel4: level = 4; break;
				case R.id.buttonLevel5: level = 5; break;
			}
			
			Intent playIntent = new Intent(getApplicationContext(), PlayActivity.class);
			playIntent.putExtra("level", level);
			Toast.makeText(getApplicationContext(), "Level : " + level, Toast.LENGTH_SHORT).show();
			startActivity(playIntent);
		}
	};
}
