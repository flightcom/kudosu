package com.flightcom.kudosu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.util.Log;
import android.widget.EditText;

public class SudokuSolver {

	Sudoku sudoku;
	HashSet<Integer> numbersList = new HashSet<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	
	@SuppressWarnings("unchecked")
	HashSet<Integer>[][] candidates = new HashSet[9][9];
	
	//ArrayList<ArrayList<ArrayList<Integer>>> candidates = new ArrayList<ArrayList<ArrayList<Integer>>>();
	
	
	public SudokuSolver(Sudoku sudoku){
		
		this.sudoku = sudoku;
		this.sudoku.grid = this.sudoku.gridUser;

	}
	
	public void run() {
		
		int nb1 = this.checkCandidates();
		int nb2 = this.checkUniquePlace();

	}

	@SuppressWarnings("unchecked")
	private int checkCandidates(){
		
		int nbCellsFound = 0;

		for( int i = 0; i < this.sudoku.grid.length; i++) {

			for ( int j = 0; j < this.sudoku.grid[i].length; j++) {
				
				Integer cell = this.sudoku.grid[i][j];
				HashSet<Integer> candidates = (HashSet<Integer>) numbersList.clone();
				HashSet<Integer> notCandidates = new HashSet<Integer>();
				
				if( cell != null ) {
					
					ArrayList<Integer> colNums =  new ArrayList<Integer>();
					ArrayList<Integer> rowNums =  new ArrayList<Integer>();
					ArrayList<Integer> areaNums =  new ArrayList<Integer>();
					
					for(int ik = 0; ik < this.sudoku.grid.length; ik++) {
						colNums.add(this.sudoku.grid[ik][j]);
					}

					for(int jl = 0; jl < this.sudoku.grid[i].length; jl++) {
						rowNums.add(this.sudoku.grid[i][jl]);
					}

					int area = Sudoku.getAreaFromCase(i, j);
					int[] areaVals = Sudoku.areaToArray(area);
					for ( int x : areaVals) {
						areaNums.add(this.sudoku.getCaseAt(x)) ;
					}
					
					notCandidates.addAll(colNums);
					notCandidates.addAll(rowNums);
					notCandidates.addAll(areaNums);
					
					candidates.removeAll(notCandidates);
					this.candidates[i][j] = candidates;

				} else {
					
					this.candidates[i][j] = new HashSet<Integer>(Arrays.asList(this.sudoku.grid[i][j]));

				}

				
				if(this.candidates[i][j].size() == 1) {
					
					nbCellsFound++;
					Log.i(null, "Candidats " + Integer.toString(i)+','+Integer.toString(j) + " : " + this.candidates[i][j].toString() );
					this.sudoku.grid[i][j] = Integer.parseInt(this.candidates[i][j].toString().replace("[", "").replace("]", ""));
					
				}

			}
			
		}
		
		return nbCellsFound;
		
	}
	
	private int checkUniquePlace() {
		
		int nbCellsFound = 0;
		
		// Check the area
		for(int area : numbersList) {
			
			int[] cellNums = Sudoku.areaToArray(area);
			
			for ( int numero : numbersList ) {
				
				ArrayList<Integer> casesContainingNum = new ArrayList<Integer>(); 
				
				for ( int cellNum : cellNums ) {
					
					int coords[] = Sudoku.caseIntToCoor(cellNum);
					if ( this.candidates[coords[0]][coords[1]].contains(numero) ) {
						casesContainingNum.add(cellNum);
					}
					
				}
				
				if ( casesContainingNum.size() == 1 ) {
					int coords[] = Sudoku.caseIntToCoor(casesContainingNum.get(0));
					this.candidates[coords[0]][coords[1]] = new HashSet<Integer>(Arrays.asList(numero));
				}
				
			}
			
		}
		
		// Check the row
		for ( int i = 0; i < this.sudoku.grid.length; i++ ) {
			
			for ( int numero : this.numbersList ) {
				
				ArrayList<Integer[]> casesContainingNum = new ArrayList<Integer[]>(); 
				Integer[] row = this.sudoku.grid[i];
				
				for ( int j = 0; j < row.length; j++ ) {
					
					if ( this.candidates[i][j].contains(numero) ) {
						casesContainingNum.add(new Integer[]{i, j});
					}

				}

				if ( casesContainingNum.size() == 1 ) {
					Integer[] tuplet = casesContainingNum.get(0);
					this.candidates[tuplet[0]][tuplet[1]] = new HashSet<Integer>(Arrays.asList(numero));
				}
			}
			
		}
		
		// Check the column
		for ( int j = 0; j < this.sudoku.grid[0].length; j++ ) {

			for ( int numero : this.numbersList ) {
				
				ArrayList<Integer[]> casesContainingNum = new ArrayList<Integer[]>(); 
				Integer[] column = new Integer[9];
				
				for ( int i = 0; i < this.sudoku.grid.length; i++ ) {
					
					column[i] = (this.sudoku.grid[i][j]);
					
				}

				for ( int i = 0; i < column.length; i++ ) {
					
					if ( this.candidates[i][j].contains(numero) ) {
						casesContainingNum.add(new Integer[]{i, j});
					}

				}

				if ( casesContainingNum.size() == 1 ) {
					Integer[] tuplet = casesContainingNum.get(0);
					this.candidates[tuplet[0]][tuplet[1]] = new HashSet<Integer>(Arrays.asList(numero));
				}
				
			}
			
		}
		
		for( int i = 0; i < this.sudoku.grid.length; i++) {

			for ( int j = 0; j < this.sudoku.grid[i].length; j++) {
				
				if(this.candidates[i][j].size() == 1) {
					
					nbCellsFound++;
					Log.i(null, "Candidats " + Integer.toString(i)+','+Integer.toString(j) + " : " + this.candidates[i][j].toString() );
					this.sudoku.grid[i][j] = Integer.parseInt(this.candidates[i][j].toString().replace("[", "").replace("]", ""));
					
				}

			}
			
		}
		return nbCellsFound;
		
	}
	
}