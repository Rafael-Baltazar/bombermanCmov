package com.example.bombermancmov.model;

public class LevelGrid {

	public static final char EMPTY = '-';
	public static final char WALL = 'W';
	public static final char OBSTACLE = 'O';
	public static final char ROBOT = 'R';
	public static final char BOMB = 'B';
	
	private char[][] gridLayout;
	private int rowSize;
	private int colSize;

	public LevelGrid(char[][] gridLayout) {
		if(gridLayout == null) {
			//Default game field
			this.gridLayout = new char [][] {
					{ 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W' },
					{ 'W', '-', '-', '-', '-', '1', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'O', 'O', 'W' },
					{ 'W', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
					{ 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
					{ 'W', 'W', '-', 'W', 'O', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
					{ 'W', '-', '-', '-', 'O', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
					{ 'W', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
					{ 'W', '-', 'R', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
					{ 'W', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
					{ 'W', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
					{ 'W', 'W', '-', 'W', 'O', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', '-', 'W', 'W' },
					{ 'W', '-', '-', '-', 'O', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'W' },
					{ 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W' } 
					};
		} else {
			this.gridLayout = gridLayout;
		}
		this.colSize = this.gridLayout.length;
		this.rowSize = this.gridLayout[0].length;
	}

	public char[][] getGridLayout() {
		return gridLayout;
	}

	public int getRowSize() {
		return rowSize;
	}
	
	public int getColSize(){
		return colSize;
	}

	public void setGridLayout(char[][] gridLayout) {		
		this.gridLayout = gridLayout;		
		this.colSize = this.gridLayout.length;
		this.rowSize = this.gridLayout[0].length;
	}

	public char getGridCell(int x, int y) {
		return gridLayout[x][y];
	}

	public void setGridCell(int x, int y, char c) {
		gridLayout[x][y] = c;
	}
	
}