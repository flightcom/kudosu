package com.flightcom.kudosu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class SolveManualActivity extends Activity {
	
	EditText selectedCase = null;
	Chronometer chrono;
	Sudoku sudoku;
	SudokuSolver solver;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solve, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.solve_action_clear: 
	        	this.sudoku.grid = this.sudoku.gridUser;
	        	print();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solve_manual);
		
		LinearLayout root = (LinearLayout) findViewById(R.id.root);

		Bundle bundle = getIntent().getExtras();
		sudoku = new Sudoku();
		chrono = (Chronometer) findViewById(R.id.chrono);

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
		btVal.setOnClickListener(solveListener);

		// On r�cup�re les dimensions de l'�cran
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		int height = displaymetrics.heightPixels;
		
		LinearLayout lSolve = (LinearLayout) findViewById(R.id.gridlayout);

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

//						caseFinale.setOnTouchListener(new OnTouchListener(){
//							
//							public boolean onTouch(View v, MotionEvent event) {
//
//								if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE ) {
//									v.setBackgroundColor(Color.GREEN);
//									selectedCase = (EditText) v;
//									return true;
//								} else {
//									v.setBackground(coin);
//								}
//								
//								return false;
//								
//							}
//
//						});

						caseFinale.setInputType(InputType.TYPE_NULL);
						caseFinale.setBackground(coin);
						caseFinale.setWidth((int)width/9);
						caseFinale.setHeight((int)width/9);
						
						lCaseVert.addView(caseFinale);
					}

					lCasesHor.addView(lCaseVert);
				}

				lCasesVert.addView(lCasesHor);
			}

			lSolve.addView(lCasesVert);
		}
		
		int gridHeight = lSolve.getLayoutParams().height;

		for(int i = 0; i < buttons.length; i++) {
			LayoutParams childParams = (LayoutParams)buttons[i].getLayoutParams();
			childParams.height = (height - gridHeight)/11;
		}
		
	}

	protected void onWindowFocusChanged() {
		
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		
	}

	protected void onResume() {
		
		super.onResume();
		View decorView = getWindow().getDecorView();
//		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		
	}

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
	
	private OnClickListener solveListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			sudoku.solve();
			print();
			
		}

	};

	private void print() {

		for( int i = 0; i < sudoku.grid.length; i++) {
			
			for ( int j = 0; j < sudoku.grid[i].length; j++) {
				
				String caseId = Integer.toString(Sudoku.caseCoordToInt(i, j));
				int resID = getResources().getIdentifier(caseId, "id", getPackageName());
				EditText mCase = (EditText)findViewById(resID);
				
				int color = (sudoku.grid[i][j] == sudoku.gridUser[i][j]) ? Color.BLACK : Color.BLUE;
				
				mCase.setTextColor(color);
				String val = sudoku.grid[i][j].intValue() == 0 ? "" : Integer.toString(sudoku.grid[i][j]);
				mCase.setText(val);

			}
			
		}
		
	}	
	
}
