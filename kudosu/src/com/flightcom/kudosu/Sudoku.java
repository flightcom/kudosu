package com.flightcom.kudosu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import android.util.Log;

public class Sudoku {
	
	ArrayList<Integer> numbersList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	// Grille de départ
	int[][] grid = new int[9][9];
	// Grille remplie
	int[][] gridFull = new int[9][9];
	// Grille de départ avec remplissage du joueur
	int[][] gridUser = new int[9][9];

	public Sudoku(int level){
		
		this.init();
		this.setDifficulty(level);

	}

	@SuppressWarnings("unchecked")
	private void init(){
		
		int maxAttempts = 3;
		int attempts = 0;
		
		for (int i = 0; i < this.gridFull.length; i++){

			int[] row = new int[9];
			if (attempts == maxAttempts)
				i = 0;
			
			//Log.e(null, "actual row : " + Integer.toString(i) + ", attempts : "+Integer.toString(attempts));

			attempts = 0;
			
			for(int j = 0; j < row.length; j++){

				ArrayList<Integer> numbs = (ArrayList<Integer>) this.numbersList.clone();
				ArrayList<Integer> forbiddenNumbs = new ArrayList<Integer>();

				// On récupère les chiffres en amont dans la colonne de la grille 
				for (int k = 0; k < i+1; k++){
					if(!forbiddenNumbs.contains(this.gridFull[k][j]))
						forbiddenNumbs.add(this.gridFull[k][j]);
				}
				
				// On récupère les chiffres en amont dans la ligne de la grille
				for (int l = 0; l < j; l++){
					if(!forbiddenNumbs.contains(row[l]))
						forbiddenNumbs.add(row[l]);
				}
				
				// On récupère ceux de la case
				int area = Sudoku.getAreaFromCase(Integer.parseInt(Integer.toString(i+1)+Integer.toString(j+1)));
				int[] areaValues = this.areaToArray(area);
				for(int x : areaValues){
					if(!forbiddenNumbs.contains(x))
						forbiddenNumbs.add(x);
				}
				
				//Log.e(null, "forbidden nums : "+forbiddenNumbs.toString());
				
				numbs.removeAll(forbiddenNumbs);

				//Log.e(null, "length : "+Integer.toString(numbs.size()));
				//Log.e(null, "allowed nums : "+numbs.toString());

				Collections.shuffle(numbs);
				
				if(numbs.size() == 0){
					if(attempts >= maxAttempts){
						break;
					} else {
						j = 0;
						attempts++;
						continue;
					}
				} else {
					attempts = 0;
				}
				
				row[j] = numbs.get(0);
				//Log.e(null, "number choosen : " + Integer.toString(row[i]));
			}

			//Log.e(null, sCol);

			for (int j = 0; j < this.gridFull[i].length; j++){
				this.gridFull[i][j] = row[j];
				this.grid[i][j] = row[j];
				this.gridUser[i][j] = row[j];
			}
		}
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

		return (Integer.toString(this.grid[row-1][col-1]) != null) ? this.grid[row-1][col-1] : 0;

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
	
	public static int getAreaFromCase(int lacase){
		
		int res = 0;
		ArrayList<Integer> allAreas = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
		ArrayList<Integer> possibleAreas = allAreas;
		
		String sCase = Integer.toString(lacase);
		//Log.e(null, sCase);
		int row = Integer.parseInt(String.valueOf(sCase.charAt(0))) -1;
		int col = Integer.parseInt(String.valueOf(sCase.charAt(1))) -1;
		
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
	
	private void setDifficulty(int level){
		
		int nbCases = 81;
		int givens =  0;
		int nbDigitByCaseMin = 0;
		int nbDigitByCaseNbAtMin = 0;
		int countDigitMin = 0;
		int countDigitNbAtMin = 0;
		ArrayList<Integer> casesDone = new ArrayList<Integer>();
		int i = 0;
		
		switch (level) {
		
			case 1: 
				givens = 32;
				nbDigitByCaseMin = 2;
				nbDigitByCaseNbAtMin = -1;
				countDigitMin = 3;
				countDigitNbAtMin = -1;
				break;
			case 2: 
				givens = 30;
				nbDigitByCaseMin = 1;
				nbDigitByCaseNbAtMin = 2;
				countDigitMin = 2;
				countDigitNbAtMin = 2;
				break;
			case 3: 
				givens = 28;
				nbDigitByCaseMin = 1;
				nbDigitByCaseNbAtMin = 4;
				countDigitMin = 1;
				countDigitNbAtMin = 1;
				break;
			case 4: 
				givens = 26;
				nbDigitByCaseMin = 0;
				nbDigitByCaseNbAtMin =-1;
				countDigitMin = 1;
				countDigitNbAtMin = 1;
				break;
			case 5: 
				givens = 24;
				nbDigitByCaseMin = 0;
				nbDigitByCaseNbAtMin =-1;
				countDigitMin = 1;
				countDigitNbAtMin = 1;
				break;
			
		}
		
		while (nbCases > givens) {
			
			int row = Sudoku.getRandom(0, 8);
			int col = Sudoku.getRandom(0, 8);
			
			int caseI = Sudoku.caseCoordToInt(row, col);
			int caseIR = Sudoku.caseCoordToInt(8-row, 8-col);
			
			int digit = this.grid[row][col];
			int digitR = this.grid[8-row][8-col];
			int area = Sudoku.getAreaFromCase(caseI);
			
			if(this.countDigitInArea(area) == nbDigitByCaseMin) continue;
			if(this.countDigitInGrid(digit) == countDigitMin) continue;
			if(this.countDigitInGrid(digitR) == countDigitMin) continue;
			if(casesDone.contains(caseI)) continue;
				
			casesDone.add(caseI);
			this.grid[row][col] = 0;
			this.gridUser[row][col] = 0;
			Log.e(null, Integer.toString(caseI));
			nbCases--;
			this.grid[8-row][8-col] = 0;
			this.gridUser[8-row][8-col] = 0;
			Log.e(null, Integer.toString(caseIR));
			nbCases--;
			i++;
			
		}
		
	}
	
	private static String caseCoordToStr(int row, int col){
		
		return Integer.toString(row)+Integer.toString(col);
		
	}
	
	private static int[] caseStrToCoord(String sCase){
		
		int[] res = new int[2];
		
		res[0] = Integer.parseInt(String.valueOf(sCase.charAt(0))) -1;
		res[1] = Integer.parseInt(String.valueOf(sCase.charAt(1))) -1;
		
		return res;

	}

	public static int caseCoordToInt(int row, int col){
		
		int res = 0;
		
		res = Integer.parseInt(Integer.toString(row+1)+Integer.toString(col+1));
		
		return res;

	}

	public static int [] caseIntToCoor(int iCase){
		
		int[] res = new int[2];
		
		res[0] = Integer.parseInt(String.valueOf(Integer.toString(iCase).charAt(0))) -1;
		res[1] = Integer.parseInt(String.valueOf(Integer.toString(iCase).charAt(1))) -1;
		
		return res;

	}

	public int countDigitInGrid(int digit){
		
		int count = 0;
		
		for(int i = 0; i < this.grid.length; i++){
			for(int j = 0; j < this.grid[i].length; j++){
				if(this.grid[i][j] == digit)
					count++;
			}
		}
		
		return count;
		
	}
	
	public int countDigitInArea(int area){
		
		int count = 0;
		int [] digitInArea = this.areaToArray(area);
		for(int x : digitInArea){
			if( x != 0)
				count++;
		}
		
		return count;
	}
	

}