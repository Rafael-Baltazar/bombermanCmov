package com.example.bombermancmov.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LevelLoader {
		
	public static Level loadLevel(InputStream file) {
		Level level = new Level();
		LevelGrid levelgrid = new LevelGrid();
		
		if(file != null) {
			try {		
				BufferedReader reader = new BufferedReader(new InputStreamReader(file));
				String nextLine = "";
				
				//Set values
				for(int valLine = 0; valLine < 9; valLine++) {
					nextLine = reader.readLine();
					
					switch(nextLine.split(":")[0]) {						
						case "LN":{
							level.setLevelName(nextLine.split(":")[1]);
							break;
						}
						case "GD":{
							level.setGameDuration(Long.parseLong(nextLine.split(":")[1]));
							break;
						}
						case "MP":{
							level.setMaxNumberPlayers(Integer.parseInt(nextLine.split(":")[1]));
							break;
						}
						case "ET":{
							level.setExplosionTimeout(Integer.parseInt(nextLine.split(":")[1]));
							break;
						}
						case "ED":{
							level.setExplosionDuration(Float.parseFloat(nextLine.split(":")[1]));
							break;
						}
						case "ER":{
							level.setExplosionRange(Float.parseFloat(nextLine.split(":")[1]));
							break;
						}
						case "RS":{
							level.setRobotSpeed(Float.parseFloat(nextLine.split(":")[1]));
							break;
						}
						case "PR":{
							level.setPointsPerRobotKilled(Float.parseFloat(nextLine.split(":")[1]));
							break;
						}
						case "PO":{
							level.setPointsPerOpponentKilled(Float.parseFloat(nextLine.split(":")[1]));
							break;
						}
					}		
				}
				
				//get height & width
				nextLine = reader.readLine();
				int rows = Integer.parseInt(nextLine.split(":")[1]);
				int cols = Integer.parseInt(nextLine.split(":")[2]);			
				
				//Reading LevelGrid
				char[][] grid = new char[cols][rows];
				int rowCnt = 0;
				
				while((nextLine = reader.readLine()) != null) {
					grid[rowCnt++] = nextLine.toCharArray();
				}				
				
				levelgrid.setGridLayout(grid);
				level.setGrid(levelgrid);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//default level
			level.setLevelName("default");
			level.setExplosionTimeout(4);
			level.setExplosionDuration(2000);
			level.setExplosionRange(2);
			level.setRobotSpeed(1);
			level.setPointsPerOpponentKilled(2);
			level.setPointsPerRobotKilled(1);
			level.setMaxNumberPlayers(3);
			
			levelgrid.setDefaultLevel(); //default grid
			level.setGrid(levelgrid); 
		}		
		return level;
	}
}