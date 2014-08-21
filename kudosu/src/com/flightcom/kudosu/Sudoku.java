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
	Integer[][] grid = new Integer[9][9];
	// Grille remplie
	Integer[][] gridFull = new Integer[9][9];
	// Grille de départ avec remplissage du joueur
	Integer[][] gridUser = new Integer[9][9];
	
	SudokuSolver solver;
	
	boolean gridReady = false;

	public Sudoku(){
		
		
	}
	
	public Sudoku(int level){
		
		this.init();
		this.setDifficulty(level);

	}

	@SuppressWarnings("unchecked")
	private void init(){
		
		int maxAttempts = 3;
		int attempts = 0;
		
		rowLoop : for (int i = 0; i < this.gridFull.length; i++){

			//Log.e(null, "> Row " + i);

			int[] row = new int[9];

			colLoop : for(int z = 0; z < row.length; z++){

				//Log.e(null, "--> Col " + j);

				ArrayList<Integer> numbs = (ArrayList<Integer>) this.numbersList.clone();
				ArrayList<Integer> forbiddenNumbs = new ArrayList<Integer>();

				// On récupère les chiffres en amont dans la colonne de la grille 
				for (int k = 0; k < i+1; k++){
					if(!forbiddenNumbs.contains(this.gridFull[k][z]))
						forbiddenNumbs.add(this.gridFull[k][z]);
				}
				
				// On récupère les chiffres en amont dans la ligne de la grille
				for (int l = 0; l < z; l++){
					if(!forbiddenNumbs.contains(row[l]))
						forbiddenNumbs.add(row[l]);
				}
				
				// On récupère ceux de la case
				int area = Sudoku.getAreaFromCase(i,z);
				int[] areaValues = this.areaToArray(area);
				for(int x : areaValues){
					if(!forbiddenNumbs.contains(x))
						forbiddenNumbs.add(x);
				}
				
				numbs.removeAll(forbiddenNumbs);
				
				if(numbs.size() == 0){
					z = 0;
					attempts++;
					if(attempts == maxAttempts){
						i = ( i > 1 ) ? --i : 0;
						attempts = 0;
						continue rowLoop;
					}
					continue colLoop;
				}
				
				Collections.shuffle(numbs);
				row[z] = numbs.get(0);
			}
			
			if(row.length == this.gridFull[i].length) {
				for (int j = 0; j < this.gridFull[i].length; j++){
					this.gridFull[i][j] = row[j];
					this.grid[i][j] = row[j];
					this.gridUser[i][j] = row[j];
				}
			}
		}

		this.gridReady = true;

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

	public int getCaseAt(int myCase){

		int row = Integer.parseInt(String.valueOf(Integer.toString(myCase).charAt(0))) -1;
		int col = Integer.parseInt(String.valueOf(Integer.toString(myCase).charAt(1))) -1;
		return (this.grid[row][col] != null) ? this.grid[row][col] : 0;

	}

	static String rowToString(int[] col){
		
		String res = "";
		
		for(int i: col){
			res += Integer.toString(i);
		}
		
		return res;
		
	}
	
	public static int[] areaToArray(int area){

		int cases[] = null; 
		
		switch(area){
			case 1: cases = new int[]{11, 12, 13, 21, 22, 23, 31, 32, 33}; break;
			case 2: cases = new int[]{14, 15, 16, 24, 25, 26, 34, 35, 36}; break;
			case 3: cases = new int[]{17, 18, 19, 27, 28, 29, 37, 38, 39}; break;
			case 4: cases = new int[]{41, 42, 43, 51, 52, 53, 61, 62, 63}; break;
			case 5: cases = new int[]{44, 45, 46, 54, 55, 56, 64, 65, 66}; break;
			case 6: cases = new int[]{47, 48, 49, 57, 58, 59, 67, 68, 69}; break;
			case 7: cases = new int[]{71, 72, 73, 81, 82, 83, 91, 92, 93}; break;
			case 8: cases = new int[]{74, 75, 76, 84, 85, 86, 94, 95, 96}; break;
			case 9: cases = new int[]{77, 78, 79, 87, 88, 89, 97, 98, 99}; break;
		}
		
		return cases;
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
	
	public static int getAreaFromCase(int row, int col){
		
		int res = 0;
		ArrayList<Integer> allAreas = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
		ArrayList<Integer> possibleAreas = allAreas;
		
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
				givens = 34;
				nbDigitByCaseMin = 2;
				nbDigitByCaseNbAtMin = -1;
				countDigitMin = 3;
				countDigitNbAtMin = -1;
				break;
			case 2: 
				givens = 32;
				nbDigitByCaseMin = 1;
				nbDigitByCaseNbAtMin = 2;
				countDigitMin = 2;
				countDigitNbAtMin = 2;
				break;
			case 3: 
				givens = 30;
				nbDigitByCaseMin = 1;
				nbDigitByCaseNbAtMin = 4;
				countDigitMin = 1;
				countDigitNbAtMin = 1;
				break;
			case 4: 
				givens = 28;
				nbDigitByCaseMin = 0;
				nbDigitByCaseNbAtMin =-1;
				countDigitMin = 1;
				countDigitNbAtMin = 1;
				break;
			case 5: 
				givens = 26;
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
			int area = Sudoku.getAreaFromCase(row, col);
			
			//Log.e(null, Integer.toString(area) + " : " + Integer.toString(this.countDigitInArea(area)));
			
			if(this.countDigitInArea(area) <= nbDigitByCaseMin + 1) continue;
			if(this.countDigitInGrid(digit) == countDigitMin) continue;
			if(this.countDigitInGrid(digitR) == countDigitMin) continue;
			if(casesDone.contains(caseI) || casesDone.contains(caseIR)) continue;
				
			casesDone.add(caseI);
			casesDone.add(caseIR);
			this.grid[row][col] = 0;
			this.gridUser[row][col] = 0;
			//Log.e(null, Integer.toString(caseI));
			nbCases--;
			this.grid[8-row][8-col] = 0;
			this.gridUser[8-row][8-col] = 0;
			//Log.e(null, Integer.toString(caseIR));
			nbCases--;
			//Log.e(null, Integer.toString(nbCases));
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
		int [] digitInArea = Sudoku.areaToArray(area);
		for(int x : digitInArea){
			if( x != 0)
				count++;
		}
		
		return count;
	}

	public void solve(){
		
		this.solver = new SudokuSolver(this);
		solver.run();
		
	}
	
	public void del(int row, int col) {
		
		this.gridUser[row][col] = null;
		
	}
	
	public static ArrayList<Integer> getAdjacentAreas(int area) {
		
		ArrayList<Integer> adj = new ArrayList<Integer>();
		
		adj.add((area + 3) % 9);
		adj.add((area + 6) % 9);
		
		int m3i = area - (area % 3);
		for(int i = 1; i < 3; i++) {
			if( area + i != area % 3 ) {
				adj.add(i + m3i);
			}
		}
		
		return adj;
	}

}