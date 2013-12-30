package com.flightcom.kudosu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Sudoku {
	
	ArrayList<Integer> numbersList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	int[][] grid = new int[9][9];

	public Sudoku(){
		
		int[] col = this.makeColumn();
		
		for (int i = 0; i < 9; i++){
			this.grid[i][0] = col[i];
		}
		
	}
	
	public int[] makeColumn(){
		
		int[] col = new int[9];
		ArrayList<Integer> numbs = this.numbersList;
		
		for(int i = 0; i < 9; i++){
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
}
