package com.example.bombermancmov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bombermancmov.model.Level;

/**
 * @author impaler This is the main surface that handles the ontouch events and
 *         draws the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	private static final int ROUND_TIME = 700;//ms

	private GameLoopThread thread;
	private SurfaceHolder surfaceHolder = getHolder();
	
	private String playerName = "Rafael Baltazar"; //DEFAULT NAME

	/* Game model */
	private Level level;
	
	private BomberActivity act; //HORRIBLE HACK!



	public MainGamePanel(Context context) {
		super(context);
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
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(level.nextRound()){
					thread.run();
					try {
						Thread.sleep(ROUND_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		Log.d(TAG, "constructor");
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Surface changed");
		level.scale();
		thread.run();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
		thread.setRunning(true);
		level.scale();
		thread.run();
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
	/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int motionAction = event.getActionMasked();
		boolean isTouch = motionAction == MotionEvent.ACTION_DOWN;
		if (isTouch) {
			int pointerIndex = event.getActionIndex();
			float pointerX = event.getX(pointerIndex);
			float pointerY = event.getY(pointerIndex);
			Log.d(TAG, "X: " + pointerX + " Y: " + pointerY);
			// Translate the origin (0,0) to the middle of the 
			// surface
			//TODO: Are the values given by getX and getY absolute 
			// in screen or surface?
			float surfaceWidth = this.getWidth();
			float surfaceHeight = this.getHeight();
			pointerX -= surfaceWidth/2;
			pointerY -= surfaceHeight/2;
			float slope = surfaceHeight/surfaceWidth;
			// Scale the coordinates, so y is negative in the 
			// lower part of the screen and positive in the 
			// upper part
			pointerY *= -1;
			// Use simple linear functions (f(x) = y = x 
			// and f(x) = y = -x) to divide the view in four 
			// parts and then determine where to move the player
			boolean upperLeftTriangle = pointerY > slope * pointerX;
			boolean upperRightTriangle = pointerY > -slope * pointerX;
			boolean leftTriangle = upperLeftTriangle && !upperRightTriangle;
			boolean rightTriangle = upperRightTriangle && !upperLeftTriangle;
			if(upperLeftTriangle) {
				if(leftTriangle) {
					Log.d(TAG, "leftTriangle");
					player.moveLeft();
				} else {
					Log.d(TAG, "upperTriangle");
					player.moveUp();
				}
			} else {
				if(rightTriangle) {
					Log.d(TAG, "rightTriangle");
					player.moveRight();
				} else {
					Log.d(TAG, "downTriangle");
					player.moveDown();
				}
			}
			thread.run();
		}
		return isTouch;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int keyaction = event.getKeyCode();

		switch (keyaction) {
		case KeyEvent.KEYCODE_W:
		case KeyEvent.KEYCODE_DPAD_UP:
			if (player.moveUp())
				Log.d("KEY_DOWN", "Moved Up");
			else
				Log.d("KEY_DOWN", "Collided moving up");
			break;
		case KeyEvent.KEYCODE_S:
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (player.moveDown())
				Log.d("KEY_DOWN", "Moved Down");
			else
				Log.d("KEY_DOWN", "Collided moving down");
			break;
		case KeyEvent.KEYCODE_A:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (player.moveLeft())
				Log.d("KEY_DOWN", "Moved Left");
			else
				Log.d("KEY_DOWN", "Collided moving left");
			break;
		case KeyEvent.KEYCODE_D:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (player.moveRight())
				Log.d("KEY_DOWN", "Moved Right");
			else
				Log.d("KEY_DOWN", "Collided moving right");
			break;
		case KeyEvent.KEYCODE_SPACE:
			level.placeBomb(player.getX(), player.getY());
			break;
		}
		thread.run();
		return super.onKeyDown(keyCode, event);
	}*/
	
	
	public boolean doAction(int actionCode){
		switch(actionCode){
			case 0:{
				level.getPlayer().moveUp();
				break;
			}
			case 1:{
				level.getPlayer().moveDown();
				break;
			}
			case 2:{
				level.getPlayer().moveLeft();
				break;
			}
			case 3:{
				level.getPlayer().moveRight();
				break;
			}
			case 4:{
				level.placeBomb(level.getPlayer().getX(), level.getPlayer().getY());
				break;
			}
			default:{
				return false;
			}
		}
		
		thread.run();
		return true;
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("onDraw", "Using onDraw.");
		drawGameModel(canvas);
	}

	private void drawGameModel(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		level.draw(canvas);
	}
}
