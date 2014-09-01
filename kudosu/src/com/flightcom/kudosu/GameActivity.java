package com.flightcom.kudosu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class GameActivity extends Activity {
	
	EditText selectedCase = null;
	Chronometer chrono;
	Sudoku sudoku;
	SudokuSolver solver;
	AlertDialog levelDialog;
	int height;
	int width;
	ArrayList<EditText> cases = new ArrayList<EditText>();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solve, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final LinearLayout root = (LinearLayout) findViewById(R.id.root);

		this.sudoku = new Sudoku();
		this.chrono = (Chronometer) findViewById(R.id.chrono);

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
		Button btBack = (Button) findViewById(R.id.buttonBack);
		Button btVal = (Button) findViewById(R.id.buttonValider);

		Button[] buttons = { bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, btDel, btBack, btVal };
		
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

		// On récupère les dimensions de l'écran
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		final LinearLayout lSolve = (LinearLayout) findViewById(R.id.gridlayout);

	    ViewTreeObserver vto = root.getViewTreeObserver();
	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					
	       @Override
	       public void onGlobalLayout() {		    
	           	//remove listener to ensure only one call is made.
	    	   	root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			   	height = root.getHeight();
			   	width = root.getWidth();
				Log.i(null, "Total height : " + height);
				for ( EditText mCase : cases ) {
					mCase.setWidth((int)width/9);
					mCase.setHeight((int)width/9);
				}
				int h = lSolve.getHeight();
				Log.i(null, "Grid height : " + h);
	       }
	    });
	    
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

						EditText caseFinale = (EditText)getLayoutInflater().inflate(R.layout.cell, null);
						caseFinale.setId(Sudoku.caseCoordToInt(Integer.parseInt(posX)-1, Integer.parseInt(posY)-1));
						final Drawable coin;
						
						String draw = "";
						
						int area = Sudoku.getAreaFromCase(Integer.parseInt(posX)-1, Integer.parseInt(posY)-1);
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
						caseFinale.setBackground(coin);
						
						cases.add(caseFinale);
						
						lCaseVert.addView(caseFinale);
					}

					lCasesHor.addView(lCaseVert);
				}

				lCasesVert.addView(lCasesHor);
			}

			lSolve.addView(lCasesVert);
		}
		
	}

	protected void onWindowFocusChanged() {
		
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    final Configuration newCfg = newConfig;
		final LinearLayout root = (LinearLayout) findViewById(R.id.root);
		final LinearLayout gridLayout = (LinearLayout) findViewById(R.id.gridlayout);
		final LinearLayout btnLayout = (LinearLayout) findViewById(R.id.buttonlayout);

		ViewTreeObserver observer = root.getViewTreeObserver();
	    observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

	        @Override
	        public void onGlobalLayout() {
	           	//remove listener to ensure only one call is made.
	    	   	root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
	    	   	int height = root.getHeight();
	    	   	int width = root.getWidth();
	    	   	Log.d(null, "Height : " + height + ", width : " + width);

	    	   	// Checks the orientation of the screen
	    	    if (newCfg.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	        root.setOrientation(LinearLayout.HORIZONTAL);
	    	        int gridWidth = gridLayout.getWidth();
		    	   	Log.d(null, "Grid width : " + gridWidth + ", buttons width : " + (width-gridWidth));
	    	        btnLayout.setLayoutParams(new LinearLayout.LayoutParams(width-height, height));
					for ( EditText mCase : cases ) {
						mCase.setWidth(height/9);
						mCase.setHeight(height/9);
					}
	    	    } else if (newCfg.orientation == Configuration.ORIENTATION_PORTRAIT){
	    	        root.setOrientation(LinearLayout.VERTICAL);
	    	        int gridHeight = gridLayout.getHeight();
		    	   	Log.d(null, "Grid height : " + gridHeight);
	    	        btnLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height-width));
					for ( EditText mCase : cases ) {
						mCase.setWidth(width/9);
						mCase.setHeight(width/9);
					}
	    	    }

	        }

	    });
	    
	}
	
	//	protected void onResume() {
//		
//		super.onResume();
//		View decorView = getWindow().getDecorView();
//		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//		
//	}

	private OnClickListener clickNumberListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Button bt = (Button) v;
			String value = bt.getText().toString();
			selectedCase.setTextColor(Color.BLACK);
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
			int id = selectedCase.getId();
			int[] coords = Sudoku.caseIntToCoor(id);
			sudoku.del(coords[0], coords[1]);
		}
	};
	
	private OnKeyListener editCaseListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	
}