package com.example.bombermancmov;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.bombermancmov.model.Character;
import com.example.bombermancmov.model.Game;
import com.example.bombermancmov.model.Resource;
import com.example.bombermancmov.model.component.SoundComponent;

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

	private SurfaceHolder surfaceHolder;
	
	private GameActivity context;

	/* Game model */
	private Game game;
	private Resource mResource;

	private StatusScreenUpdater mStatusScreenUpdater; // HORRIBLE HACK!

	public MainGamePanel(GameActivity context, StatusScreenUpdater updater,
			boolean isSingleplayer) {
		super(context);
		this.context = context;
		surfaceHolder = getHolder();
		mStatusScreenUpdater = updater;

		// adding the callback(this) to the surface holder to intercept events
		surfaceHolder.addCallback(this);

		// create level
		mResource = new Resource(this);
		mResource.setExplosionSoundComponent(new SoundComponent(this));
		game = new Game(mResource, isSingleplayer);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		Log.d(TAG, "constructor");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Surface changed");
		int newWidth = getWidth() / game.getLevel().getGrid().getRowSize();
		int newHeight = getHeight() / game.getLevel().getGrid().getColSize();
		mResource.scaleResources(newWidth, newHeight);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");		
		// create & start the game loop thread
		gameLoopThread = new GameLoopThread(surfaceHolder, this);
		gameLoopThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// shut down all the threads
		shutDown(gameLoopThread);
		Log.d(TAG, "Game loop thread was shut down cleanly");
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
		
		if(this.game.isFinished()) {
			this.buildEnddialog();
		}
	}

	public void tryUp(int id) {
		game.getPlayerByNumber(id).tryMoveUp();
		
		if(this.game.isFinished()) {
			this.buildEnddialog();
		}
	}

	public void tryDown(int id) {
		game.getPlayerByNumber(id).tryMoveDown();
		
		if(this.game.isFinished()) {
			this.buildEnddialog();
		}
	}

	public void tryRight(int id) {
		game.getPlayerByNumber(id).tryMoveRight();
		
		if(this.game.isFinished()) {
			this.buildEnddialog();
		}
	}

	public void tryStop(int id) {
		game.getPlayerByNumber(id).stop();
		
		if(this.game.isFinished()) {
			this.buildEnddialog();
		}
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
		
	//Enddialog for finishing a game
	public void buildEnddialog() {
    	invalidate();
        //game completed
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);        
        
		switch(this.game.getEndStatus()) {
			//Singleplayer
			case 1: 
				builder.setTitle(context.getText(R.string.finished_title_win) + " Points: " + this.game.getPlayerByNumber(0).getPoints());
				break;
			case 2: 
				builder.setTitle(context.getText(R.string.finished_title_lost_killed) + " Points: " + this.game.getPlayerByNumber(0).getPoints());
				break;
			case 3: 
				builder.setTitle(context.getText(R.string.finished_title_lost_time) + " Points: " + this.game.getPlayerByNumber(0).getPoints());
				break;
			default:
				builder.setTitle(context.getText(R.string.finished_title_lost_unkown) + " Points: " + this.game.getPlayerByNumber(0).getPoints());
				break;	        	
		}
		        
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.finish, null);
        builder.setView(view);
        View closeButton = view.findViewById(R.id.closeGame);
        
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View clicked) {
                if(clicked.getId() == R.id.closeGame) {
                    context.finish();
                }
            }
        });
        AlertDialog finishDialog = builder.create();  
        
        finishDialog.show();
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
