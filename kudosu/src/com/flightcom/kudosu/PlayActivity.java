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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class PlayActivity extends Activity {
	
	EditText selectedCase = null;
	Sudoku sudoku;
	Chronometer chrono;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		LinearLayout root = (LinearLayout) findViewById(R.id.root);

		Bundle bundle = getIntent().getExtras();
		int level = bundle.getInt("level");
		sudoku = new Sudoku(level);
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
		Button btPause = (Button) findViewById(R.id.buttonPause);
		Button btVal = (Button) findViewById(R.id.buttonValider);

		Button[] buttons = { bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, btDel, btBack, btPause, btVal };
		
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
//							@Override
//							public void onTouch(View v) {
//								// TODO Auto-generated method stub
//									if(v.hasFocus()){
//										v.setBackgroundColor(Color.GREEN);
//										selectedCase = (EditText) v;
//									} else {
//										v.setBackground(coin);
//								}
//							}
//
//						});
						
						caseFinale.setInputType(InputType.TYPE_NULL);
						caseFinale.setBackground(coin);
						int caseValueI = sudoku.getCaseAt(Integer.parseInt(posX), Integer.parseInt(posY));
						String caseValueS = (caseValueI == 0) ? "" : Integer.toString(caseValueI);
						String caseNum_S = posX + posY;
						int caseNum_I = Integer.parseInt(caseNum_S);
						caseFinale.setText(caseValueS);
						if ( caseValueI != 0 ) {
							caseFinale.setFocusable(false);
							caseFinale.setTextColor(Color.BLACK);
						}
						else {
							caseFinale.setTextColor(Color.BLUE);
						}
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
		LinearLayout buttonsLayout = (LinearLayout)findViewById(R.id.buttonlayout);
		LayoutParams buttonsLayoutParams = (LayoutParams)buttonsLayout.getLayoutParams();
		int buttonsHeight = ((LinearLayout)findViewById(R.id.buttonlayout)).getLayoutParams().height;

		for(int i = 0; i < buttons.length; i++) {
			LayoutParams childParams = (LayoutParams)buttons[i].getLayoutParams();
			childParams.height = (height - gridHeight)/10;
		}
		
//		buttonsLayoutParams.height = height - gridHeight;
		//root.addView(btn);
		 
		chrono.start();
		//setContentView(lSolve);
		//setContentView(R.layout.activity_solve);
	}

	protected void onWindowFocusChanged() {
		
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		
	}

	protected void onResume() {
		
		super.onResume();
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		
	}

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
					//Log.e(null, Integer.toString(sudoku.gridFull[i][j]) + ":" + Integer.toString(sudoku.gridUser[i][j]));
					EditText et = (EditText) findViewById(Sudoku.caseCoordToInt(i, j));
					if(sudoku.gridFull[i][j] != sudoku.gridUser[i][j]){
						et.setBackgroundColor(Color.RED);
						isSuccessfullyFilled = false;
					} else {
						et.setBackgroundColor(Color.GREEN);
					}
				}
			}
			if(isSuccessfullyFilled == true){
				chrono.stop();
				Toast.makeText(getApplicationContext(), "BRAVO !!!", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getApplicationContext(), "BOUUH !!!", Toast.LENGTH_SHORT).show();
		}
	};

}
