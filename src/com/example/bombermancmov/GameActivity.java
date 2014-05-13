package com.example.bombermancmov;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GameActivity extends ActionBarActivity {

	private static final String TAG = GameLoopThread.class.getSimpleName();
	private FrameLayout frm;
	private Button pauseButton, leftButton, rightButton, upButton, downButton,
			bombButton;
	private MainGamePanel mGamePanel;
	private PlayerInput playerInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// save for posterior use
		TextView nameTextView = (TextView) findViewById(R.id.playerName);
		TextView scoreTextView = (TextView) findViewById(R.id.playerScore);
		TextView timeLeftTextView = (TextView) findViewById(R.id.timeLeft);
		TextView numPlayersTextView = (TextView) findViewById(R.id.numberPlayers);
		StatusScreenUpdater updater = new StatusScreenUpdater(nameTextView,
				scoreTextView, timeLeftTextView, numPlayersTextView, this);
		
		pauseButton = (Button) findViewById(R.id.buttonPause);
		frm = (FrameLayout) findViewById(R.id.frameLayout);
		
		setOnClickListenersToButtons();
		
		boolean isLocal = getIntent().getBooleanExtra("isLocal", true);
		int playerId = getIntent().getIntExtra("playerId", 0);
		if (isLocal) {
			mGamePanel = new MainGamePanel(this, updater, true); //true for isSinglePlayer
			playerInput = new LocalPlayerInput(mGamePanel);
		} else {
			mGamePanel = new MainGamePanel(this, updater, false);
			playerInput = new RemotePlayerInput(mGamePanel.getGame(), playerId);
		}
		frm.addView(mGamePanel);
		
		Log.d(TAG, "View added");
	}

	private void setOnClickListenersToButtons() {
		leftButton = (Button) findViewById(R.id.buttonLeft);
		leftButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveLeft();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}
				return true;
			}
		});

		upButton = (Button) findViewById(R.id.buttonUp);
		upButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveUp();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}
				return true;
			}

		});

		downButton = (Button) findViewById(R.id.buttonDown);
		downButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveDown();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}

				return true;
			}
		});

		rightButton = (Button) findViewById(R.id.buttonRight);
		rightButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					playerInput.tryMoveRight();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					playerInput.tryStop();
				}
				return true;
			}
		});

		bombButton = (Button) findViewById(R.id.buttonBomb);
		bombButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playerInput.placeBomb();
			}
		});
	}

	public void pauseOrResumeGame(View b) {
		String btnTxt = pauseButton.getText().toString();
		String pause = getResources().getString(R.string.pause_button_text);
		String resume = getResources().getString(R.string.resume_button_text);
		if (btnTxt.equals(pause)) {
			mGamePanel.pauseThread();
			pauseButton.setText(resume);
		} else {
			mGamePanel.resumeThread();
			pauseButton.setText(pause);
		}
	}

	public void quitGame(View v) {
		//game finished, go back to main menu
		finish();
	}

}
