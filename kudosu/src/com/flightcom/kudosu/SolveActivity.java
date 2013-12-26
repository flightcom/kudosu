package com.flightcom.kudosu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SolveActivity extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solve);
		
		// On récupère les dimensions de l'écran
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		
		LinearLayout lSolve = (LinearLayout) findViewById(R.id.gridlayout);
		//LinearLayout lSolve = new LinearLayout(this);
		//lSolve.setOrientation(LinearLayout.VERTICAL);
		//lSolve.setGravity(Gravity.CENTER_HORIZONTAL);
		//lSolve.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

		for(int i = 0; i < 3; i++){
			LinearLayout lCasesVert = new LinearLayout(this);
			lCasesVert.setOrientation(LinearLayout.HORIZONTAL);
			lCasesVert.setGravity(Gravity.TOP);
			lCasesVert.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			for(int j = 0; j < 3; j++){
				LinearLayout lCasesHor = new LinearLayout(this);
				lCasesHor.setOrientation(LinearLayout.VERTICAL);
				lCasesHor.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				for(int k = 0; k < 3; k++){
					LinearLayout lCaseVert = new LinearLayout(this);
					lCaseVert.setOrientation(LinearLayout.HORIZONTAL);
					lCaseVert.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

					for(int l = 0; l < 3; l++){
						EditText caseFinale = new EditText(this);
						final Drawable coin;
						String posX = Integer.toString(3*i+k+1);
						String posY = Integer.toString(3*j+l+1);
						
						String draw = "";
						switch(Integer.parseInt(posX) % 3){
							case 0: draw += "bottom"; break;
							case 1: draw += "top"; break;
							case 2: draw += "mid"; break;
						}
						
						switch(Integer.parseInt(posY) % 3){
							case 0: draw += "right"; break;
							case 1: draw += "left"; break;
							default: draw += ""; break;
						}
						
						int drawableId = getResources().getIdentifier(draw+"_edittext", "drawable", getPackageName());
						
						coin = getResources().getDrawable( drawableId );
					
						caseFinale.setOnFocusChangeListener(new OnFocusChangeListener() {
							
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								// TODO Auto-generated method stub
									if(v.hasFocus()){
										v.setBackgroundColor(Color.GREEN);
									} else {
										v.setBackground(coin);
								}
							}
						});

						caseFinale.setInputType(InputType.TYPE_NULL);
						caseFinale.setGravity(Gravity.CENTER);
						caseFinale.setBackground(coin);
						caseFinale.setText(posX+posY);
						caseFinale.setTextSize(16);
						caseFinale.setWidth((int)width/9);
						caseFinale.setHeight((int)width/9);
						caseFinale.setCursorVisible(false);
						lCaseVert.addView(caseFinale);
					}

					lCasesHor.addView(lCaseVert);
				}

				lCasesVert.addView(lCasesHor);
			}

			lSolve.addView(lCasesVert);
		}
		
		//setContentView(lSolve);
		//setContentView(R.layout.activity_solve);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 

}
