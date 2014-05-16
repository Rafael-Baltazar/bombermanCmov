package com.example.bombermancmov;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.bombermancmov.model.Resource;
import com.example.bombermancmov.model.component.SoundComponent;

/**
 * This is the main surface that handles the on-touch events and draws the image
 * to the screen.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
	//Tag for notifications
	private static final String TAG = MainGamePanel.class.getSimpleName();

	private Resource mResource;
	private GameActivity activity;	
	private AlertDialog finishDialog;	

	public MainGamePanel(GameActivity activity) {
		super(activity);
		this.activity = activity;
		this.finishDialog = null;
		this.getHolder().addCallback(this);

		this.mResource = new Resource(this);
		this.mResource.setExplosionSoundComponent(new SoundComponent(this));
		
		this.setFocusable(true);
		
		Log.d(TAG, "constructor");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Surface changed");
		int newWidth = getWidth() / activity.getLevel().getGrid().getRowSize();
		int newHeight = getHeight() / activity.getLevel().getGrid().getColSize();
		mResource.scaleResources(newWidth, newHeight);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being created");
		this.activity.startThreads();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		this.activity.shutDownThreads();
	}

	// Enddialog for finishing a game
	public void buildEnddialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
		builder.setCancelable(false);
		
		String endMsgTitle = "";

		switch (this.activity.getGame().getEndStatus()) {
		case 1: endMsgTitle = this.activity.getText(R.string.finished_title_win).toString();
			break;
		case 2: endMsgTitle = this.activity.getText(R.string.finished_title_lost_killed).toString();
			break;
		case 3: endMsgTitle = this.activity.getText(R.string.finished_title_lost_time).toString();
			break;
		default: endMsgTitle = this.activity.getText(R.string.finished_title_lost_unkown).toString();
			break;
		}
		builder.setTitle(endMsgTitle + "! Points: " + ((int)this.activity.getGame().getPlayerByNumber(this.activity.getGame().getPlayerInput().getPlayerId()).getPoints()) + "\n");
		
		if(this.activity.getGame().getPlayers().size() > 1) {
			String strScore = "";
			for(int i = 0; i < this.activity.getGame().getPlayers().size(); i++) {
				if(i != activity.getGame().getPlayerInput().getPlayerId()) {
					strScore = strScore + (int) this.activity.getGame().getPlayerByNumber(i).getPoints() + " by " + this.activity.getGame().getPlayerByNumber(i).getName() +  "\n";
				}
			}
			builder.setMessage(strScore);
		}		

		LayoutInflater inflater = this.activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.finish, null);
		builder.setView(view);
		View closeButton = view.findViewById(R.id.closeGame);

		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View clicked) {
				if (clicked.getId() == R.id.closeGame) {
					activity.finish();
				}
			}
		});
		
		this.finishDialog = builder.create();
		this.finishDialog.show();		
	}

	public void drawGameModel(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		this.activity.getGame().draw(canvas);
	}
	
	/** GETTERS & SETTERS */
	
	public Resource getResource() {
		return mResource;
	}
	
	public AlertDialog getFinishDialog() {
		return finishDialog;
	}
}
