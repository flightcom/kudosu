package com.flightcom.kudosu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import android.util.Log;

public class SudokuSolver {

	Sudoku sudoku;
	ArrayList<Integer> numbersList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	boolean[][] marker = new boolean[9][9];
	
	@SuppressWarnings("unchecked")
	ArrayList<Integer>[][] candidates = new ArrayList[9][9];
	
	public SudokuSolver(Sudoku sudoku){
		
		this.sudoku = sudoku;
		this.sudoku.grid = this.sudoku.gridUser.clone();

	}
	
	public int run() {
	
		int process = 0;
		
		int nb1 = 0, nb2 = 0, nb3 = 0, nb4 = 0;
		
		do { this.checkCandidates(); nb1 = this.updateGrid(); process+= nb1; } while (nb1 > 0);
		do { this.checkUniquePlace(); nb2 = this.updateGrid(); process+= nb2; } while (nb2 > 0);
		do { this.checkImpossibleCandidates(); nb3 = this.updateGrid(); process+= nb3; } while (nb3 > 0);
		do { this.checkSameCandidates(); nb4 = this.updateGrid(); process+= nb4; } while (nb4 > 0);
		
		return process;

	}

	private void checkCandidates(){
		
		for( int i = 0; i < this.sudoku.grid.length; i++) {

			for ( int j = 0; j < this.sudoku.grid[i].length; j++) {
				
				Integer cell = this.sudoku.grid[i][j];
				ArrayList<Integer> candidates = (ArrayList<Integer>) numbersList.clone();
				ArrayList<Integer> notCandidates = new ArrayList<Integer>();
				
				if( cell == null ) {
					
					ArrayList<Integer> colNums =  new ArrayList<Integer>();
					ArrayList<Integer> rowNums =  new ArrayList<Integer>();
					ArrayList<Integer> areaNums =  new ArrayList<Integer>();
					
					// Check the column
					for(int ik = 0; ik < this.sudoku.grid.length; ik++) {
						colNums.add(this.sudoku.grid[ik][j]);
					}

					// Check the row
					for(int jl = 0; jl < this.sudoku.grid[i].length; jl++) {
						rowNums.add(this.sudoku.grid[i][jl]);
					}

					// Check the area
					int area = Sudoku.getAreaFromCase(i, j);
					Integer[] areaVals = Sudoku.areaToArray(area);
					for ( int x : areaVals) {
						areaNums.add(this.sudoku.getCaseAt(x)) ;
					}
					
					notCandidates.addAll(colNums);
					notCandidates.addAll(rowNums);
					notCandidates.addAll(areaNums);
					
					candidates.removeAll(notCandidates);
					this.candidates[i][j] = candidates;

				} else {
					
					this.candidates[i][j] = new ArrayList<Integer>(Arrays.asList(this.sudoku.grid[i][j]));

				}

			}
			
		}
		
	}
	
	private void checkUniquePlace() {
		
		// Check the area
		for(int area : numbersList) {
			
			Integer[] cellNums = Sudoku.areaToArray(area);
			
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
					this.candidates[coords[0]][coords[1]] = new ArrayList<Integer>(Arrays.asList(numero));
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
					this.candidates[tuplet[0]][tuplet[1]] = new ArrayList<Integer>(Arrays.asList(numero));
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
					this.candidates[tuplet[0]][tuplet[1]] = new ArrayList<Integer>(Arrays.asList(numero));
				}
				
			}
			
		}
		
	}

	private void checkImpossibleCandidates() {
		
		for ( int area : this.numbersList ) {
			
			int[] adjs = Sudoku.getAdjacentAreas(area);
			
			int z = 0;
			for(int adj : adjs) {
				
				Integer [] cases = Sudoku.areaToArray(adj);
				char orientation = z < 2 ? 'c' : 'r';
				
				// Log.i(null, "Area : " + area + ", Adj : " + adj);
				for ( int numero : this.numbersList ) {
					
					Set<Integer> stack = new HashSet<Integer>();
					for ( int mCase : cases ) {
						
						int[] coords = Sudoku.caseIntToCoor(mCase);

						if(this.candidates[coords[0]][coords[1]].size() > 1 && this.candidates[coords[0]][coords[1]].contains(numero)) {

							// On ajoute le numero de ligne/colonne a la pile
							switch ( orientation ) {
								case 'c' :
									stack.add(coords[1]);
									break;
								case 'r' :
									stack.add(coords[0]);
									break;
							}
						
						}
						
					}
					
					if ( stack.size() == 1) { // une seule ligne ou colonne

						// On supprime le numero en tant que candidats dans toute la ligne ou colonne
						Integer [] cells = Sudoku.areaToArray(area);

						switch ( orientation ) {
							case 'c' : // On compare les numeros de colonnes
								for ( int cell : cells ) {
									int[] co = Sudoku.caseIntToCoor(cell);
									if ( co[1] == Integer.parseInt(stack.toArray()[0].toString()) ) {
										this.candidates[co[0]][co[1]].remove((Integer)numero);
										//this.sudoku.solver.candidates[co[0]][co[1]].remove(numero);
									}
								}
								break;
							case 'r' : // On compare les numeros de lignes
								for ( int cell : cells ) {
									int[] co = Sudoku.caseIntToCoor(cell);
									if ( co[1] == Integer.parseInt(stack.toArray()[0].toString()) ) {
										this.candidates[co[0]][co[1]].remove((Integer)numero);
									}
								}
								break;
						}
					}
				}
				
				z++;
			}
			
		}
		
	}
	
	private void checkImpossibleCandidates2() {
		
		for ( int area : this.numbersList ) {
			
			Integer [] cases = Sudoku.areaToArray(area);
			
			for ( int numero : this.numbersList ) {
			
				for ( Integer mCase : cases ) {
					
					int[] coords = Sudoku.caseIntToCoor(mCase);
					
					
					
				}
				
			}
			
		}
		
	}
	
	private void checkSameCandidates() {
		
		ArrayList<Integer> mNums = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
		
		for ( Integer i = 0 ; i < mNums.size(); i++ ) {
			
			for ( Integer j = 0 ; j < mNums.size(); j++ ) {
				
				if ( this.marker[i][j] == false) {
					
					this.checkSameCandidates("row", Sudoku.caseCoordToInt(i, j));
					this.checkSameCandidates("col", Sudoku.caseCoordToInt(i, j));
					this.checkSameCandidates("area", Sudoku.caseCoordToInt(i, j));

				}
				
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void checkSameCandidates(String unit, Integer mCase) {
		
		int[] coords = Sudoku.caseIntToCoor(mCase);
		Set<Integer> candidats = new TreeSet<Integer>(this.candidates[coords[0]][coords[1]]);

		if ( candidats.size() <= 1) { return; }

		ArrayList<Integer> unitCells;
		ArrayList<Integer> cellsIdem = new ArrayList<Integer>();
		
		switch ( unit ) {
			case "row" : unitCells = new ArrayList<Integer>(Arrays.asList(Sudoku.rowToArray(coords[0]))); break;
			case "col" : unitCells = new ArrayList<Integer>(Arrays.asList(Sudoku.colToArray(coords[1]))); break;
			case "area": unitCells = new ArrayList<Integer>(Arrays.asList(Sudoku.areaToArray(Sudoku.getAreaFromCase(coords[0], coords[1])))); break;
			default: unitCells = null;
		}
		
		ArrayList<Integer> cells2check = (ArrayList<Integer>)unitCells.clone();
		cells2check.remove(mCase);
		cellsIdem.add(mCase);
		
		ArrayList<String> sCandidats = new ArrayList<String>();
		
		for ( Integer cell : cells2check ) {
			
			int[] co = Sudoku.caseIntToCoor(cell);
			Set<Integer> cdts = new TreeSet<Integer>(this.candidates[co[0]][co[1]]);
			
			if ( cdts.size() != candidats.size() ) { continue; }

			//Log.i("check " + mCase.toString(), candidats.toString() + " " + cdts.toString() + " " + (candidats.equals(cdts) ? "yes" : "no"));
			if ( candidats.equals(cdts) ) {
				cellsIdem.add(cell);
			}
			
		}
		
		unitCells.removeAll(cellsIdem);
		
		if ( cellsIdem.size() == candidats.size() ) {
			
			Log.i("Same Candidates ("+unit+")", cellsIdem.toString() + " have same candidates ("  + candidats.toString() + ")");

			// On supprime les candidats
			for ( Integer uc : unitCells ) {
				
				int[] co = Sudoku.caseIntToCoor(uc);
				this.candidates[co[0]][co[1]].removeAll(candidats);
			}
			
			// On marque les cellules
			for ( Integer id : cellsIdem ) {
				
				int[] co = Sudoku.caseIntToCoor(id);
				this.marker[co[0]][co[1]] = true;
				
			}
		}
		
	}
	
	public int updateGrid() {
		
		int nbCellsFound = 0;
		
		for( int i = 0; i < this.sudoku.grid.length; i++) {

			for ( int j = 0; j < this.sudoku.grid[i].length; j++) {

				if(this.candidates[i][j].size() == 1 && this.sudoku.grid[i][j] == null) {
			
					nbCellsFound++;
					this.sudoku.grid[i][j] = this.candidates[i][j].get(0);
				
				}
			}

		}
			
		return nbCellsFound;

	}
	
}
