package com.example.bombermancmov;

import com.example.bombermancmov.model.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is the main surface that handles the ontouch events and draws the image
 * to the screen.
 * 
 * @author impaler
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	private static final int ROUND_TIME = 1000;// ms

	/**
	 * Update and render game model in a separate thread.
	 */
	private GameLoopThread thread;

	// Run level.nextRound() every x milliseconds
	private LevelNextRoundThread levelNextRound;

	private SurfaceHolder surfaceHolder;
	private boolean[] tryDirection; /* FIXME: HACK */

	private String playerName; // DEFAULT NAME

	/* Game model */
	private Game game;

	private SingleGameActivity mActivity; // HORRIBLE HACK!

	public MainGamePanel(Context context) {
		super(context);
		tryDirection = new boolean[] { false, false, false, false };
		surfaceHolder = getHolder();
		mActivity = (SingleGameActivity) context;
		playerName = mActivity.getIntent().getStringExtra("PlayerName");
		mActivity.setPlayerName(playerName);

		// adding the callback(this) to the surface holder to intercept events
		surfaceHolder.addCallback(this);

		// create level
		game = new Game(this);

		// for single player, TODO for multiplayer
		mActivity.setPlayerScore(game.getPlayerByNumber(0).getPoints());
		mActivity.setTimeLeft(game.getTimeLeft());
		mActivity.setNumPlayers(game.getLevel().getMaxNumberPlayers());

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		Log.d(TAG, "constructor");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Surface changed");
		game.scale();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
		startThreads();
	}

	private void startThreads() {
		// create the game loop thread
		thread = new GameLoopThread(surfaceHolder, this);
		thread.setRunning(true);
		thread.start();
		// create env thread
		levelNextRound = new LevelNextRoundThread(game, ROUND_TIME);
		levelNextRound.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// shut down all the threads
		thread.setRunning(false);
		shutDown(thread);
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
				Log.d(TAG, "Interrupted exception at surface destroyed.");
			}
		}
	}

	// TODO: multiplayer approach
	public boolean doAction(int actionCode) {
		switch (actionCode) {
		case 0: {
			game.getPlayerByNumber(0).moveLeft(10);
			break;
		}
		case 1: {
			game.getPlayerByNumber(0).moveUp(10);
			break;
		}
		case 2: {
			game.getPlayerByNumber(0).moveDown(10);
			break;
		}
		case 3: {
			game.getPlayerByNumber(0).moveRight(10);
			break;
		}
		case 4: {
			game.placeBomb((int) Math.rint(game.getPlayerByNumber(0).getX()),
					(int) Math.rint(game.getPlayerByNumber(0).getY()));
			break;
		}
		default:
			return false;
		}
		return true;
	}

	public void tryStop() {
		int b;
		for (b = 0; b < 4; ++b) {
			tryDirection[b] = false;
			Log.d("CHAR", "stopping");
		}
	}

	public void tryWalk(int dir) {
		int b;
		for (b = 0; b < 4; ++b) {
			if (b == dir) {
				tryDirection[b] = true;
				Log.d("CHAR", "walking " + b);
			} else {
				tryDirection[b] = false;
			}
		}

	}

	public void updateLayer(long frameDu) {
		int b;
		for (b = 0; b < 4; ++b) {
			if (tryDirection[b]) {
				this.doAction(b);
				break;
			}

		}
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
}
