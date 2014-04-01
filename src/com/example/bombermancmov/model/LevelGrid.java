package com.example.bombermancmov.model;

public class LevelGrid {

	public static final char EMPTY = '-';
	public static final char WALL = 'W';
	public static final char OBSTACLE = 'O';
	public static final char ROBOT = 'R';
	/**
	 * 1-3 Players
	 */
	private char[] gridLayout;
	private int rowSize;

	public LevelGrid() {
	}

	public char[] getGridLayout() {
		return gridLayout;
	}

	public int getRowSize() {
		return rowSize;
	}

	public void setGridLayout(char[] gridLayout, int rowSize) {
		this.gridLayout = gridLayout;
		this.rowSize = rowSize;
	}

	public char getGridCell(int x, int y) {
		return gridLayout[x + y * rowSize];
	}

	public void setGridCell(int x, int y, char c) {
		gridLayout[x + y * rowSize] = c;
	}
}