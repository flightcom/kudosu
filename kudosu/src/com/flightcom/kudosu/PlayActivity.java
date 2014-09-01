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

public class PlayActivity extends GameActivity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_play);
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		int level = bundle.getInt("level");
		sudoku = new Sudoku(level);
		chrono = (Chronometer) findViewById(R.id.chrono);

		Button btVal = (Button) findViewById(R.id.buttonValider);
		btVal.setOnClickListener(clickValidateListener);

		chrono.start();
	}
	
	private OnClickListener clickValidateListener = new View.OnClickListener() {
		
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
