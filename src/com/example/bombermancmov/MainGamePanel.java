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
	private boolean[] tryDirection; /*FIXME: HACK*/

	private String playerName = "Rafael Baltazar"; // DEFAULT NAME

	/* Game model */
	private Game game;

	private SingleGameActivity act; // HORRIBLE HACK!

	public MainGamePanel(Context context) {
		super(context);
		tryDirection = new boolean[]{false, false, false, false};
		surfaceHolder = getHolder();
		act = (SingleGameActivity) context;
		act.setPlayerName(playerName);

		// adding the callback(this) to the surface holder to intercept events
		surfaceHolder.addCallback(this);

		// create level
		game = new Game(this);

		act.setPlayerScore(game.getPlayer().getPoints());
		act.setTimeLeft(game.getTimeLeft());
		act.setNumPlayers(game.getLevel().getMaxNumberPlayers());

		// create the game loop thread
		thread = new GameLoopThread(surfaceHolder, this);

		// create env thread
		levelNextRound = new LevelNextRoundThread(game, ROUND_TIME);
		levelNextRound.start();

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
		game.scale();
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// shut down all the threads
		thread.setRunning(false);
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				Log.d(TAG, "Interrupted exception at surface destroyed.");
			}
		}
		Log.d(TAG, "Game loop thread was shut down cleanly");
		levelNextRound.setRunning(false);
		while (true) {
			try {
				levelNextRound.join();
				break;
			} catch (InterruptedException e) {
				Log.d(TAG, "Interrupted exception at surface destroyed.");
			}
		}
		Log.d(TAG, "Level next round thread was shut down cleanly");
	}

	public boolean doAction(int actionCode) {
		switch (actionCode) {
		case 0: {
			game.getPlayer().moveLeft(10);
			break;
		}
		case 1: {
			game.getPlayer().moveUp(10);
			break;
		}
		case 2: {
			game.getPlayer().moveDown(10);
			break;
		}
		case 3: {
			game.getPlayer().moveRight(10);
			break;
		}
		case 4: {
			game.placeBomb((int)Math.rint(game.getPlayer().getX()), (int)Math.rint(game.getPlayer().getY()));
			break;
		}
		default:
			return false;
		}
		return true;
	}
	public void tryStop(){
		int b;
		for(b=0; b<4; ++b){
			tryDirection[b]=false;
			Log.d("CHAR", "stopping");
		}
	}
	
	public void tryWalk(int dir){
		int b;
		for(b=0; b<4; ++b){
			if(b==dir){
				tryDirection[b]=true;
				Log.d("CHAR", "walking " + b);
			}else {
				tryDirection[b]=false;
			}
		}
		
	}

	public void updateLayer(long frameDu) {
		int b;
		for(b=0; b<4; ++b){
			if(tryDirection[b]){
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
