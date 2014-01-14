package com.flightcom.kudosu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class PlayActivity extends Activity {
	
	EditText selectedCase = null;
	Sudoku sudoku;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		Bundle bundle = getIntent().getExtras();
		int level = bundle.getInt("level");
		sudoku = new Sudoku(level);

		Button bt1 = (Button) findViewById(R.id.button1);
		Button bt2 = (Button) findViewById(R.id.button2);
		Button bt3 = (Button) findViewById(R.id.button3);
		Button bt4 = (Button) findViewById(R.id.button4);
		Button bt5 = (Button) findViewById(R.id.button5);
		Button bt6 = (Button) findViewById(R.id.button6);
		Button bt7 = (Button) findViewById(R.id.button7);
		Button bt8 = (Button) findViewById(R.id.button8);
		Button bt9 = (Button) findViewById(R.id.button9);
		
		Button btDel = (Button) findViewById(R.id.buttonDel);
		Button btVal = (Button) findViewById(R.id.buttonValider);

		bt1.setOnClickListener(clickNumberListener);
		bt2.setOnClickListener(clickNumberListener);
		bt3.setOnClickListener(clickNumberListener);
		bt4.setOnClickListener(clickNumberListener);
		bt5.setOnClickListener(clickNumberListener);
		bt6.setOnClickListener(clickNumberListener);
		bt7.setOnClickListener(clickNumberListener);
		bt8.setOnClickListener(clickNumberListener);
		bt9.setOnClickListener(clickNumberListener);
		
		btDel.setOnClickListener(clickDelListener);
		btVal.setOnClickListener(validateListener);

		// On récupère les dimensions de l'écran
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;
		
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

						String posX = Integer.toString(3*i+k+1);
						String posY = Integer.toString(3*j+l+1);

						EditText caseFinale = new EditText(this);
						caseFinale.setId(Sudoku.caseCoordToInt(Integer.parseInt(posX)-1, Integer.parseInt(posY)-1));
						final Drawable coin;
						
						String draw = "";
						
						int area = Sudoku.getAreaFromCase(Sudoku.caseCoordToInt(Integer.parseInt(posX)-1, Integer.parseInt(posY)-1));
						if(area %2 == 0)
							draw += "gray_";
						
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
										selectedCase = (EditText) v;
									} else {
										v.setBackground(coin);
								}
							}
						});

						caseFinale.setInputType(InputType.TYPE_NULL);
						caseFinale.setGravity(Gravity.CENTER);
						caseFinale.setBackground(coin);
						int caseValueI = sudoku.getCaseAt(Integer.parseInt(posX), Integer.parseInt(posY));
						String caseValueS = (caseValueI == 0) ? "" : Integer.toString(caseValueI);
						String caseNum_S = posX + posY;
						int caseNum_I = Integer.parseInt(caseNum_S);
						//caseFinale.setText(Integer.toString(Sudoku.getAreaFromCase(caseNum_I)));
						caseFinale.setText(caseValueS);
						//caseFinale.setText(posX+posY);
						if ( caseValueI != 0 ) {
							caseFinale.setFocusable(false);
							caseFinale.setTextColor(Color.BLACK);
						}
						else {
							caseFinale.setTextColor(Color.BLUE);
						}
						caseFinale.setTextSize(24);
						caseFinale.setWidth((int)width/9);
						caseFinale.setHeight((int)width/9);
						caseFinale.setCursorVisible(false);

						/*int area = Sudoku.getAreaFromCase(Sudoku.caseCoordToInt(Integer.parseInt(posX), Integer.parseInt(posY)));
						if(area %2 == 0)
							caseFinale.setBackgroundColor(Color.LTGRAY);
						*/
						lCaseVert.addView(caseFinale);
					}

					lCasesHor.addView(lCaseVert);
				}

				lCasesVert.addView(lCasesHor);
			}

			lSolve.addView(lCasesVert);
		}
		
		int gridHeight = lSolve.getLayoutParams().height;
		int buttonHeight = ((LinearLayout)findViewById(R.id.buttonlayout)).getLayoutParams().height;
		Button btn = new Button(this);
		btn.setText("TEST");
		btn.setHeight(height - gridHeight - buttonHeight);
		
		LinearLayout root = (LinearLayout) findViewById(R.id.root);
		root.addView(btn);
		
		//setContentView(lSolve);
		//setContentView(R.layout.activity_solve);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
/*	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
*/
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
*/
/*	@Override
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
*/
	private OnClickListener clickNumberListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Button bt = (Button) v;
			String value = bt.getText().toString();
			selectedCase.setText(value);
			int[] coord = Sudoku.caseIntToCoor(selectedCase.getId());
			sudoku.gridUser[coord[0]][coord[1]] = Integer.parseInt(value);
			
		}
	};
	
	private OnClickListener clickDelListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Button bt = (Button) v;
			selectedCase.setText("");
		}
	};
	
	private OnKeyListener editCaseListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
	};
	
	private OnClickListener validateListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			boolean isSuccessfullyFilled = true;
			for(int i = 0; i < sudoku.gridFull.length; i++){
				for (int j = 0; j < sudoku.gridFull[i].length; j++){
					Log.e(null, Integer.toString(sudoku.gridFull[i][j]) + ":" + Integer.toString(sudoku.gridUser[i][j]));
					if(sudoku.gridFull[i][j] != sudoku.gridUser[i][j]){
						isSuccessfullyFilled = false;
					}
				}
			}
			if(isSuccessfullyFilled == true)
				Toast.makeText(getApplicationContext(), "BRAVO !!!", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "BOUUH !!!", Toast.LENGTH_SHORT).show();
		}
	};

}
