package com.flightcom.kudosu;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SolveManualActivity extends GameActivity {
	
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
	        	this.sudoku = new Sudoku();
	        	print();
	        	return true;
	        case R.id.solve_action_fill:
	        	this.sudoku = new Sudoku();
	        	String[] items = getResources().getStringArray(R.array.difficulty_levels_array);
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	String string = getResources().getString(R.string.level_selection);
                builder.setTitle(string);
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int item) {
                        sudoku.fill(item);
                        print();
	                    levelDialog.dismiss();
                    }
                });
                levelDialog = builder.create();
                levelDialog.show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Button btVal = (Button) findViewById(R.id.buttonValider);
		
		btVal.setOnClickListener(clickSolveListener);
	}

	private OnClickListener clickSolveListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			//chrono.setBase(SystemClock.elapsedRealtime());
			//chrono.start();
			sudoku.solve();
			//chrono.stop();
			print();
			
		}

	};

	private void print() {

		for( int i = 0; i < sudoku.grid.length; i++) {
			
			for ( int j = 0; j < sudoku.grid[i].length; j++) {
				
				String caseId = Integer.toString(Sudoku.caseCoordToInt(i, j));
				int resID = getResources().getIdentifier(caseId, "id", getPackageName());
				EditText mCase = (EditText)findViewById(resID);
				
				int color = (mCase.length() > 0) ? Color.BLACK : Color.BLUE;
				
				//for (int nb : )
				
				mCase.setTextColor(color);
				//String val = (sudoku.grid[i][j] == null ) ? sudoku.solver.candidates[i][j].toString() : Integer.toString(sudoku.grid[i][j]);
				//String val = (sudoku.grid[i][j] == null ) ? (sudoku.solver.candidates[i][j] == null ) ? "" : sudoku.solver.candidates[i][j].toString() : Integer.toString(sudoku.grid[i][j]);
				String val = (sudoku.grid[i][j] == null ) ? "" : Integer.toString(sudoku.grid[i][j]);
				//float size = (sudoku.grid[i][j] == null ) ? 10 : 30;
				//mCase.setTextSize(size);
				mCase.setText(val);

			}
			
		}
		
	}	
	
	static String toString(ArrayList<Integer> al) {
		
		String result = "";
		
		int cpt = 0;
		for ( int i = 1; i < 9; i++ ) {
			if ( al.get(cpt) ==  i ) {
				result +=  i;
				cpt++;
				if ( cpt == al.size() ) {
					return result;
				}
			}
			else {
				result += "  ";
			}
			result += result.length() % 5 == 0 ? "\n" : "  ";
		}
		
		return null;
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
	   	int height = root.getHeight();
	   	int width = root.getWidth();

	   	// Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        for ( EditText mCase : this.cases ) {
				mCase.setWidth((int)height/9);
				mCase.setHeight((int)height/9);
	        }
	        Toast.makeText(getApplicationContext(), "Mode Paysage", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        for ( EditText mCase : this.cases ) {
				mCase.setWidth((int)width/9);
				mCase.setHeight((int)width/9);
	        }
	        Toast.makeText(getApplicationContext(), "Mode Portrait", Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void onOrientationChanged(int orientation) {
		
		LinearLayout root = (LinearLayout) findViewById(R.id.root);
	   	int height = root.getHeight();
	   	int width = root.getWidth();

	   	Log.i(null, Integer.toString(orientation));
	   	// Checks the orientation of the screen
	    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        for ( EditText mCase : this.cases ) {
				mCase.setWidth((int)height/9);
				mCase.setHeight((int)height/9);
	        }
	        Toast.makeText(getApplicationContext(), "Mode Paysage", Toast.LENGTH_SHORT).show();
	    } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
	        for ( EditText mCase : this.cases ) {
				mCase.setWidth((int)width/9);
				mCase.setHeight((int)width/9);
	        }
	        Toast.makeText(getApplicationContext(), "Mode Portrait", Toast.LENGTH_SHORT).show();
	    }
	}
	
}