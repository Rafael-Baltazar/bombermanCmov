package com.example.bombermancmov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;

/**
 * This is the main surface that handles the on-touch events and draws the image
 * to the screen.
 * 
 * @author impaler
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	public static final int ROUND_TIME = 1000;// in milliseconds

	/** Update and render game model in a separate thread. */
	private GameLoopThread gameLoopThread;

	// Run level.nextRound() every x milliseconds
	private LevelNextRoundThread levelNextRound;

	private SurfaceHolder surfaceHolder;

	/* Game model */
	private Game game;

	private StatusScreenUpdater mStatusScreenUpdater; // HORRIBLE HACK!

	public MainGamePanel(Context context, StatusScreenUpdater updater,
			boolean isSingleplayer) {
		super(context);
		surfaceHolder = getHolder();
		mStatusScreenUpdater = updater;

		// adding the callback(this) to the surface holder to intercept events
		surfaceHolder.addCallback(this);

		// create level
		game = new Game(this, isSingleplayer);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		Log.d(TAG, "constructor");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Surface changed");
		game.scaleResources();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
		startThreads();
	}

	private void startThreads() {
		// create the game loop thread
		gameLoopThread = new GameLoopThread(surfaceHolder, this);
		gameLoopThread.setRunning(true);
		gameLoopThread.start();
		// create env thread
		levelNextRound = new LevelNextRoundThread(game, ROUND_TIME);
		levelNextRound.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// shut down all the threads
		gameLoopThread.setRunning(false);
		shutDown(gameLoopThread);
		Log.d(TAG, "Game loop thread was shut down cleanly");

		levelNextRound.setRunning(false);
		shutDown(levelNextRound);
		Log.d(TAG, "Level next round thread was shut down cleanly");
	}

	private void shutDown(Thread thread) {
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				Log.d(TAG, "Surface destroyed: " + e.getMessage());
			}
		}
	}

	public void pauseThread() {
		gameLoopThread.pause();
	}

	public void resumeThread() {
		gameLoopThread.unPause();
	}

	public void tryLeft(int id) {
		game.getPlayerByNumber(id).tryMoveLeft();
	}

	public void tryUp(int id) {
		game.getPlayerByNumber(id).tryMoveUp();
	}

	public void tryDown(int id) {
		game.getPlayerByNumber(id).tryMoveDown();
	}

	public void tryRight(int id) {
		game.getPlayerByNumber(id).tryMoveRight();
	}

	public void tryStop(int id) {
		game.getPlayerByNumber(id).stop();
	}

	public void placeBomb(int id) {
		Character player = game.getPlayerByNumber(id);
		if (player.isAlive()) {
			int x = (int) Math.rint(player.getX());
			int y = (int) Math.rint(player.getY());
			game.placeBomb(x, y);
		}
	}

	public void update(long timePassed) {
		game.update(timePassed);
		mStatusScreenUpdater.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mStatusScreenUpdater.setPlayerScore(game.getPlayerByNumber(0)
						.getPoints());
				mStatusScreenUpdater.setTimeLeft(game.getTimeLeft());
				mStatusScreenUpdater.setNumPlayers(game.getLevel()
						.getMaxNumberPlayers());
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("onDraw", "Using onDraw.");
		drawGameModel(canvas);
	}

	public void drawGameModel(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		game.draw(canvas);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
