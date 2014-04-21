package com.example.bombermancmov.model;

public class LevelGrid {

	public static final char EMPTY = '-';
	public static final char WALL = 'W';
	public static final char OBSTACLE = 'O';
	public static final char ROBOT = 'R';
	public static final char BOMB = 'B';
	/**
	 * 1-3 Players
	 */
	private char[][] gridLayout;
	private int rowSize;
	private int collSize;

	public LevelGrid() {
	}

	public char[][] getGridLayout() {
		return gridLayout;
	}

	public int getRowSize() {
		return rowSize;
	}
	
	public int getCollSize(){
		return collSize;
	}

	public void setGridLayout(char[][] gridLayout, int rowSize, int collSize) {
		this.gridLayout = gridLayout;
		this.rowSize = rowSize;
		this.collSize = collSize;
	}

	public char getGridCell(int x, int y) {
		return gridLayout[x][y];
	}

	public void setGridCell(int x, int y, char c) {
		gridLayout[x][y] = c;
	}
	
}