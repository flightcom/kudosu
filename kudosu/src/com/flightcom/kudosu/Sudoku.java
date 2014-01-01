package com.flightcom.kudosu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.util.Log;

public class Sudoku {
	
	ArrayList<Integer> numbersList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	int[][] grid = new int[9][9];

	public Sudoku(){

		for (int i = 0; i < this.grid.length; i++){

			int[] col = this.makeRow(i);
			String sCol = Sudoku.rowToString(col);

			//Log.e(null, sCol);

			for (int j = 0; j < this.grid[i].length; j++){
				this.grid[i][j] = col[j];
			}
			
			if(i < 3){
				if(!this.areaIsOk(1) || !this.areaIsOk(2) || !this.areaIsOk(3))
					i--;
			} else if ( i < 6) {
				if(!this.areaIsOk(4) || !this.areaIsOk(5) || !this.areaIsOk(6))
					i--;
			} else if ( i < 9) {
				if(!this.areaIsOk(7) || !this.areaIsOk(8) || !this.areaIsOk(9))
					i--;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public int[] makeRow(int row){

		int[] col = new int[9];
		ArrayList<Integer> numbs = (ArrayList<Integer>) this.numbersList.clone();
		ArrayList<Integer> forbiddenNumbs = new ArrayList<Integer>();
		
		for(int i = 0; i < col.length; i++){

			// On rŽcupre les chiffres dans la colonne de la grille 
			for (int j = 0; j < row; j++){
				forbiddenNumbs.add(this.grid[j][i]);
			}

			int r = Sudoku.getRandom(0, numbs.size()-1);
			col[i] = numbs.get(r);
			numbs.remove(r);
		}

		return col;
	}

	static int getRandom(int start, int end){

		Random rand = new Random();
		int n = rand.nextInt(end-start+1)+start;
		return n;
	}

	public void print(){

		for(int i = 0; i < this.grid.length; i++){
			for(int j = 0; j < this.grid[0].length; j++){
				
				String s = (Integer.toString(this.grid[i][j]) == null) ? "_" : Integer.toString(this.grid[i][j]);
				
				if(j < this.grid[0].length -1)
					System.out.print(s);
				else
					System.out.println(s);
					
			}
		}
		
	}
	
	public int getCaseAt(int row, int col){

		return (Integer.toString(this.grid[row][col]) != null) ? this.grid[row][col] : 0;

	}
	
	static String rowToString(int[] col){
		
		String res = "";
		
		for(int i: col){
			res += Integer.toString(i);
		}
		
		return res;
		
	}
	
	public int[] areaToArray(int area){
		
		int[] res = new int[9];
		int minRow = 0;
		int minCol = 0;
		
		switch(area){
			case 1: minRow = 0; minCol = 0; break;
			case 2: minRow = 0; minCol = 3; break;
			case 3: minRow = 0; minCol = 6; break;
			case 4: minRow = 3; minCol = 0; break;
			case 5: minRow = 3; minCol = 3; break;
			case 6: minRow = 3; minCol = 6; break;
			case 7: minRow = 6; minCol = 0; break;
			case 8: minRow = 6; minCol = 3; break;
			case 9: minRow = 6; minCol = 6; break;
		}
		
		int x = 0;
		
		for (int i = minRow; i < minRow + 3; i++){
			for (int j = minCol; j < minCol +3; j++){
				if(this.grid[i][j] != 0){
					res[x] = this.grid[i][j];
					x++;
				}
			}
		}
		
		return res;
	}
	
	public boolean areaIsOk(int area){
		
		boolean res = true;
		
		int[] aArea = this.areaToArray(area);
		ArrayList<Integer> numbersInArea = new ArrayList<Integer>(); 
		for(int x : aArea){
			if(x != 0 && !numbersInArea.contains(x)){
				numbersInArea.add(x);
			} else {
				res = false;
			}
		}
		
		return res;
	}
	
	public int getAreaFromCase(int lacase){
		
		int res = 0;
		ArrayList<Integer> allAreas = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
		ArrayList<Integer> possibleAreas = allAreas;
		
		String sCase = Integer.toString(lacase);
		int row = Integer.parseInt(sCase.substring(0, 1)) -1;
		int col = Integer.parseInt(sCase.substring(1, 2)) -1;
		
		if(row < 3)
			possibleAreas.retainAll(new ArrayList<Integer>(Arrays.asList(1,2,3)));
		else if(row < 6)
			possibleAreas.retainAll(new ArrayList<Integer>(Arrays.asList(4,5,6)));
		else if(row < 9)
			possibleAreas.retainAll(new ArrayList<Integer>(Arrays.asList(7,8,9)));

		if(col < 3)
			possibleAreas.retainAll(new ArrayList<Integer>(Arrays.asList(1,4,7)));
		else if(col < 6)
			possibleAreas.retainAll(new ArrayList<Integer>(Arrays.asList(2,5,8)));
		else if(col < 9)
			possibleAreas.retainAll(new ArrayList<Integer>(Arrays.asList(3,6,9)));
		
		res = possibleAreas.get(0);
		
		return res;

	}
}
