package com.flightcom.kudosu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

public class SudokuSolver {

	Sudoku sudoku;
	ArrayList<Integer> numbersList = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	
	@SuppressWarnings("unchecked")
	ArrayList<Integer>[][] candidates = new ArrayList[9][9];
	
	public SudokuSolver(Sudoku sudoku){
		
		this.sudoku = sudoku;
		this.sudoku.grid = this.sudoku.gridUser.clone();

	}
	
	public int run() {
	
		int process = 0;
		
		int nb1 = 0, nb2 = 0, nb3 = 0, nb4 = 0;
		
		do { this.checkCandidates(); nb1 = this.updateGrid(); Log.i(null, "nb1 =  " + nb1); process+= nb1; } while (nb1 > 0);
		do { this.checkUniquePlace(); nb2 = this.updateGrid(); Log.i(null, "nb2 =  " + nb2); process+= nb2; } while (nb2 > 0);
		do { this.checkImpossibleCandidates(); nb3 = this.updateGrid(); Log.i(null, "nb3 =  " + nb3); process+= nb3; } while (nb3 > 0);
		do { this.checkSameCandidates(); nb4 = this.updateGrid(); Log.i(null, "nb4 =  " + nb4); process+= nb4; } while (nb4 > 0);
		
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
					
					this.candidates[i][j] = new ArrayList<Integer>(Arrays.asList(this.sudoku.grid[i][j]));

				}

			}
			
		}
		
	}
	
	private void checkUniquePlace() {
		
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
				
				int [] cases = Sudoku.areaToArray(adj);
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
						int [] cells = Sudoku.areaToArray(area);

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
	
	@SuppressWarnings("unchecked")
	private void checkSameCandidates() {
		
		ArrayList<Integer> mNums = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
		
		for ( Integer i = 0 ; i < mNums.size(); i++ ) {
			
			for ( Integer j = 0 ; j < mNums.size(); j++ ) {
				
				ArrayList<Integer> candidats = (ArrayList<Integer>) this.candidates[i][j].clone();
				Collections.sort(candidats);
				int nbCandidats = candidats.size();
				
				if ( nbCandidats <= 1) { continue; }

				// Check the row
				ArrayList<Integer> cellsWithSameCandidatesR = new ArrayList<Integer>();
				ArrayList<Integer> otherCols = (ArrayList<Integer>) mNums.clone();
				otherCols.remove(j);
				ArrayList<Integer> colsToDeleteCandidates = (ArrayList<Integer>) otherCols.clone();
				colsToDeleteCandidates.remove(j);

				cellsWithSameCandidatesR.add(Sudoku.caseCoordToInt(i, j));

				for ( Integer col : otherCols ) {
					
					ArrayList<Integer> cCandidates = (ArrayList<Integer>) this.candidates[i][col].clone();
					Collections.sort(cCandidates);
					
					if ( cCandidates.toString() == candidats.toString() ) {
						cellsWithSameCandidatesR.add(Sudoku.caseCoordToInt(i, col));
						colsToDeleteCandidates.remove(col);
					}
					
				}
				
				if ( cellsWithSameCandidatesR.size() == nbCandidats ) {
					
					Log.i("Same Candidates (row)", cellsWithSameCandidatesR.toString() + " have same candidates ("  + candidats.toString() + ")");
					
					for ( int c : colsToDeleteCandidates ) {
						for ( Integer cdt : candidats ) {
							this.candidates[i][c].remove(cdt);
						}
					}
					
				}

				// Check the col
				ArrayList<Integer> cellsWithSameCandidatesC = new ArrayList<Integer>();
				ArrayList<Integer> otherRows = (ArrayList<Integer>) mNums.clone();
				otherRows.remove(j);
				ArrayList<Integer> rowsToDeleteCandidates = (ArrayList<Integer>) otherRows.clone();

				cellsWithSameCandidatesC.add(Sudoku.caseCoordToInt(i, j));
				rowsToDeleteCandidates.remove(i);
				
				for ( Integer row : otherRows ) {
					
					ArrayList<Integer> rCandidates = (ArrayList<Integer>) this.candidates[row][j].clone();
					Collections.sort(rCandidates);
					
					if ( rCandidates.toString() == candidats.toString() ) {
						cellsWithSameCandidatesC.add(Sudoku.caseCoordToInt(row, j));
						rowsToDeleteCandidates.remove(row);
					}
					
				}
				
				if ( cellsWithSameCandidatesC.size() == nbCandidats ) {
					
					Log.i("Same Candidates (col)", cellsWithSameCandidatesC.toString() + " have same candidates ("  + candidats.toString() + ")");

					for ( int r : rowsToDeleteCandidates ) {
						for ( Integer cdt : candidats ) {
							this.candidates[r][j].remove(cdt);
						}
					}
					
				}

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
					// Log.i(null, "Candidats " + Integer.toString(i)+','+Integer.toString(j) + " : " + this.candidates[i][j].toString() );
				
				}
			}

		}
			
		return nbCellsFound;

	}
	
}
