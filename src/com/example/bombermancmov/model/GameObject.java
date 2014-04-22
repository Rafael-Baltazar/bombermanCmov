package com.example.bombermancmov.model;

public abstract class GameObject {
	private float x, y;

	/**
	 * @param x
	 * @param y
	 */
	public GameObject(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
