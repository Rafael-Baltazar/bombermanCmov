package com.example.bombermancmov.model;

public class DroidAI {
	Character[] droids;

	public DroidAI(Character[] droids) {
		super();
		this.droids = droids;
	}
	
	public void moveRandomly() {
		for(int i = 0; i < droids.length; i++) {
			double rnd = Math.random();
			if(rnd < 0.25) {
				droids[i].moveDown(10);
			} else if(rnd < 0.5) {
				droids[i].moveLeft(10);
			} else if(rnd < 0.75) {
				droids[i].moveRight(10);
			} else {
				droids[i].moveUp(10);
			}
		}
	}
}
