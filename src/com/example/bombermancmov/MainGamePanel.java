package com.example.bombermancmov;

import com.example.bombermancmov.model.Level;

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
	private static final int ROUND_TIME = 700;// ms

	/**
	 * Update and render game model in a separate thread.
	 */
	private GameLoopThread thread;
	
	// Run level.nextRound() every x milliseconds 
	private Thread levelNextRound;

	private SurfaceHolder surfaceHolder;

	private String playerName = "Rafael Baltazar"; // DEFAULT NAME

	/* Game model */
	private Level level;

	private BomberActivity act; // HORRIBLE HACK!

	public MainGamePanel(Context context) {
		super(context);
		surfaceHolder = getHolder();
		act = (BomberActivity) context;
		act.setPlayerName(playerName);

		// adding the callback(this) to the surface holder to intercept events
		surfaceHolder.addCallback(this);

		// create level
		level = new Level(this);

		act.setPlayerScore(level.getPlayer().getPoints());
		act.setTimeLeft(level.getTimeLeft());
		act.setNumPlayers(level.getNumberPlayers());
		
		// create the game loop thread
		thread = new GameLoopThread(surfaceHolder, this);

		// create env thread
		levelNextRound = new Thread(new Runnable() {
			@Override
			public void run() {
				while (level.nextRound()) {
					try {
						Thread.sleep(ROUND_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		levelNextRound.start();

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		Log.d(TAG, "constructor");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Surface changed");
		level.scale();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
		level.scale();
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down
		thread.setRunning(false);

		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				Log.d(TAG, "Interrupted exception at surface destroyed.");
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	public boolean doAction(int actionCode) {
		switch (actionCode) {
		case 0: {
			level.getPlayer().moveLeft();
			break;
		}
		case 1: {
			level.getPlayer().moveUp();
			break;
		}
		case 2: {
			level.getPlayer().moveDown();
			break;
		}
		case 3: {
			level.getPlayer().moveRight();
			break;
		}
		case 4: {
			level.placeBomb(level.getPlayer().getX(), level.getPlayer().getY());
			break;
		}
		default:
			return false;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("onDraw", "Using onDraw.");
		drawGameModel(canvas);
	}

	public void drawGameModel(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		level.draw(canvas);
	}
}
