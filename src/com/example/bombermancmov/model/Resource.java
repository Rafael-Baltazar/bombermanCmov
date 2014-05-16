package com.example.bombermancmov.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

import com.example.bombermancmov.R;
import com.example.bombermancmov.model.component.SoundComponent;

public class Resource {

	public static final int LEVEL_1 = 1;
	public static final int LEVEL_2 = 2;
	public static final int LEVEL_3 = 3;

	private Bitmap wallBitMap;
	private Bitmap obstacleBitmap;
	private Bitmap[] bombBitmap;
	private Bitmap[][] playerBitmap;
	private Bitmap[] droidBitmap;
	private SurfaceView surfaceView;
	private SoundComponent explosionSoundComponent;

	public Resource(SurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		decodeResources();
	}

	public Bitmap getWallBitMap() {
		return wallBitMap;
	}

	public void setWallBitMap(Bitmap wallBitMap) {
		this.wallBitMap = wallBitMap;
	}

	public Bitmap getObstacleBitmap() {
		return obstacleBitmap;
	}

	public void setObstacleBitmap(Bitmap obstacleBitmap) {
		this.obstacleBitmap = obstacleBitmap;
	}

	public Bitmap[] getBombBitmap() {
		return bombBitmap;
	}

	public void setBombBitmap(Bitmap[] bombBitmap) {
		this.bombBitmap = bombBitmap;
	}

	public Bitmap[][] getPlayerBitmap() {
		return playerBitmap;
	}

	public void setPlayerBitmap(Bitmap[][] playerBitmap) {
		this.playerBitmap = playerBitmap;
	}

	public Bitmap[] getDroidBitmap() {
		return droidBitmap;
	}

	public void setDroidBitmap(Bitmap[] droidBitmap) {
		this.droidBitmap = droidBitmap;
	}

	public SurfaceView getSurfaceView() {
		return surfaceView;
	}

	public void setSurfaceView(SurfaceView surfaceView) {
		this.surfaceView = surfaceView;
	}

	public SoundComponent getExplosionSoundComponent() {
		return explosionSoundComponent;
	}

	public void setExplosionSoundComponent(
			SoundComponent explosionSoundComponent) {
		this.explosionSoundComponent = explosionSoundComponent;
	}

	/** Load resources only once to increase performance. */
	public void decodeResources() {
		decodeWallBitmap();
		decodeObstacleBitmap();
		decodeBombBitmaps();
		decodePlayerBitmaps();
		decodeDroidBitmaps();
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeWallBitmap() {
		setWallBitMap(BitmapFactory.decodeResource(getSurfaceView()
				.getResources(), R.drawable.wall_1));
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeObstacleBitmap() {
		setObstacleBitmap(BitmapFactory.decodeResource(getSurfaceView()
				.getResources(), R.drawable.obstacle_0));
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeBombBitmaps() {
		setBombBitmap(new Bitmap[3]);
		getBombBitmap()[Bomb.NORMAL] = BitmapFactory.decodeResource(
				getSurfaceView().getResources(), R.drawable.bomb_0);
		getBombBitmap()[Bomb.NEARLY] = BitmapFactory.decodeResource(
				getSurfaceView().getResources(), R.drawable.bomb_1);
		getBombBitmap()[Bomb.EXPLODING] = BitmapFactory.decodeResource(
				getSurfaceView().getResources(), R.drawable.bomb_2);
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodePlayerBitmaps() {
		setPlayerBitmap(new Bitmap[4][4]);
		decodePlayer1Bitmaps();
		decodePlayer2Bitmaps();
		decodePlayer3Bitmaps();
		decodePlayer4Bitmaps();
	}

	private void decodePlayer1Bitmaps() {
		getPlayerBitmap()[Game.PLAYER_1][Character.FRONT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.eevee_front);
		getPlayerBitmap()[Game.PLAYER_1][Character.LEFT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.eevee_left);
		getPlayerBitmap()[Game.PLAYER_1][Character.RIGHT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.eevee_right);
		getPlayerBitmap()[Game.PLAYER_1][Character.BACK] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.eevee_back);
	}

	private void decodePlayer2Bitmaps() {
		getPlayerBitmap()[Game.PLAYER_2][Character.FRONT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.espeon_front);
		getPlayerBitmap()[Game.PLAYER_2][Character.LEFT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.espeon_left);
		getPlayerBitmap()[Game.PLAYER_2][Character.RIGHT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.espeon_right);
		getPlayerBitmap()[Game.PLAYER_2][Character.BACK] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.espeon_back);
	}

	private void decodePlayer3Bitmaps() {
		getPlayerBitmap()[Game.PLAYER_3][Character.FRONT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.flareon_front);
		getPlayerBitmap()[Game.PLAYER_3][Character.LEFT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.flareon_left);
		getPlayerBitmap()[Game.PLAYER_3][Character.RIGHT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.flareon_right);
		getPlayerBitmap()[Game.PLAYER_3][Character.BACK] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.flareon_back);
	}

	private void decodePlayer4Bitmaps() {
		getPlayerBitmap()[Game.PLAYER_4][Character.FRONT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.umbreon_front);
		getPlayerBitmap()[Game.PLAYER_4][Character.LEFT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.umbreon_left);
		getPlayerBitmap()[Game.PLAYER_4][Character.RIGHT] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.umbreon_right);
		getPlayerBitmap()[Game.PLAYER_4][Character.BACK] = BitmapFactory
				.decodeResource(getSurfaceView().getResources(),
						R.drawable.umbreon_back);
	}

	/**
	 * @see #decodeResources()
	 */
	private void decodeDroidBitmaps() {
		setDroidBitmap(new Bitmap[4]);
		getDroidBitmap()[Character.FRONT] = BitmapFactory.decodeResource(
				getSurfaceView().getResources(), R.drawable.c0_0);
		getDroidBitmap()[Character.LEFT] = BitmapFactory.decodeResource(
				getSurfaceView().getResources(), R.drawable.c0_1);
		getDroidBitmap()[Character.RIGHT] = BitmapFactory.decodeResource(
				getSurfaceView().getResources(), R.drawable.c0_2);
		getDroidBitmap()[Character.BACK] = BitmapFactory.decodeResource(
				getSurfaceView().getResources(), R.drawable.c0_3);
	}

	/**
	 * Scale bitmaps only once to increase performance.
	 * 
	 * @param newWidth
	 *            the desired new width
	 * @param newHeight
	 *            the desired new height
	 */
	public void scaleResources(int newWidth, int newHeight) {
		scaleWallBitmap(newWidth, newHeight);
		scaleObstacleBitmap(newWidth, newHeight);
		scaleBombBitmaps(newWidth, newHeight);
		scalePlayerBitmaps(newWidth, newHeight);
		scaleDroidBitmaps(newWidth, newHeight);
	}

	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources(int, int)
	 */
	private void scaleWallBitmap(int newWidth, int newHeight) {
		setWallBitMap(Bitmap.createScaledBitmap(getWallBitMap(), newWidth,
				newHeight, false));
	}

	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources(int, int)
	 */
	private void scaleObstacleBitmap(int newWidth, int newHeight) {
		setObstacleBitmap(Bitmap.createScaledBitmap(getObstacleBitmap(),
				newWidth, newHeight, false));
	}

	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources(int, int)
	 */
	private void scaleBombBitmaps(int newWidth, int newHeight) {
		scaleBitmapArray(newWidth, newHeight, getBombBitmap());
	}

	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources(int, int)
	 */
	private void scalePlayerBitmaps(int newWidth, int newHeight) {
		for (Bitmap[] bitmaps : getPlayerBitmap()) {
			scaleBitmapArray(newWidth, newHeight, bitmaps);
		}
	}

	/**
	 * @param newWidth
	 * @param newHeight
	 * @see #scaleResources(int, int)
	 */
	private void scaleDroidBitmaps(int newWidth, int newHeight) {
		scaleBitmapArray(newWidth, newHeight, getDroidBitmap());
	}

	/**
	 * Scales all given bitmaps to the new width and height.
	 * 
	 * @param newWidth
	 *            The new desired width.
	 * @param newHeight
	 *            The new desired height.
	 * @param bitmaps
	 *            The bitmap array to be scaled.
	 */
	private void scaleBitmapArray(int newWidth, int newHeight, Bitmap[] bitmaps) {
		for (int i = 0; i < bitmaps.length; ++i) {
			bitmaps[i] = Bitmap.createScaledBitmap(bitmaps[i], newWidth,
					newHeight, false);
		}
	}
}