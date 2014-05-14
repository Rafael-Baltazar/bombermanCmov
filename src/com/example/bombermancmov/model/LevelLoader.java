package com.example.bombermancmov.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class LevelLoader {
		
	public static Level loadLevel(String levelName, InputStream file) {
		Level level = new Level();
		
		level.setLevelName("default");
		level.setExplosionTimeout(4);
		level.setExplosionDuration(2000);
		level.setExplosionRange(2);
		level.setRobotSpeed(1);
		level.setPointsPerOpponentKilled(2);
		level.setPointsPerRobotKilled(1);
		level.setMaxNumberPlayers(3);
		
		/*if(levelName == null || file == null) {
			level.setLevelName("default");
			level.setExplosionTimeout(4);
			level.setExplosionDuration(2000);
			level.setExplosionRange(2);
			level.setRobotSpeed(1);
			level.setPointsPerOpponentKilled(2);
			level.setPointsPerRobotKilled(1);
			level.setMaxNumberPlayers(3);
		} else {
			BufferedReader in = null;
			float ln, gd, et, ed, er, rs, pr, po;			
			
			try {
				in = new BufferedReader(new FileReader(new File("levels/level1.dat")));
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
			String line = "";
			
			int rl;
			String[] parts;
			for(rl = 0; rl <=7; ++rl){
				line = in.readLine();
				parts = line.split(":");
				switch (parts[0]){
				
					case "LN":{
						ln = Float.parseFloat(parts[1]);
						break;
					}
					case "GD":{
						gd = Float.parseFloat(parts[1]);
						break;
					}
					case "ET":{
						et = Float.parseFloat(parts[1]);
						break;
					}
					case "ED":{
						ed = Float.parseFloat(parts[1]);
						break;
					}
					case "ER":{
						er = Float.parseFloat(parts[1]);
						break;
					}
					case "RS":{
						rs = Float.parseFloat(parts[1]);
						break;
					}
					case "PR":{
						pr = Float.parseFloat(parts[1]);
						break;
					}
					case "PO":{
						po = Float.parseFloat(parts[1]);
						break;
					}
				}
			}
			line = in.readLine();
			
			parts = line.split(":");
			int height = Integer.parseInt(parts[2]);
			int weight = Integer.parseInt(parts[1]);
			System.out.println(height + " X " + weight);
			char[][] grid = new char[height][weight];
			int i, j;
			char c;
			for(j = 0; j< height; ++j){
				
				for(i = 0; i<weight; ++i){
					c = (char) in.read();
					if(c == '\n' || c == '\r'){
						--i;
						continue;
					}
					grid[j][i] = c;
					
				}
			}
			for(j = 0; j< height; ++j){
				
				for(i = 0; i<weight; ++i){
					
					System.out.print(grid[j][i]);
					
				}
				System.out.print("\n");
			}
		}*/
		
		return level;
	}
}