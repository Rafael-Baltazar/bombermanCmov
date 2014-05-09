package com.example.bombermancmov;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SingleGameActivity extends ActionBarActivity {

	private static final String TAG = GameLoopThread.class.getSimpleName();
	private FrameLayout frm;
	private Button pauseButton, leftButton, rightButton, upButton, downButton,
			bombButton;
	private TextView nameTextView, scoreTextView, timeLeftTextView,
			numPlayersTextView;
	private MainGamePanel mGamePanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// save for posterior use
		nameTextView = (TextView) findViewById(R.id.playerName);
		scoreTextView = (TextView) findViewById(R.id.playerScore);
		timeLeftTextView = (TextView) findViewById(R.id.timeLeft);
		numPlayersTextView = (TextView) findViewById(R.id.numberPlayers);

		pauseButton = (Button) findViewById(R.id.buttonPause);
		setOnClickListenersToButtons();

		frm = (FrameLayout) findViewById(R.id.frameLayout);
		StatusScreenUpdater updater = new StatusScreenUpdater(nameTextView,
				scoreTextView, timeLeftTextView, numPlayersTextView, this);
		mGamePanel = new MainGamePanel(this, updater);
		frm.addView(mGamePanel);

		Log.d(TAG, "View added");
	}

	private void setOnClickListenersToButtons() {
		leftButton = (Button) findViewById(R.id.buttonLeft);
		leftButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mGamePanel.tryWalk(0);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mGamePanel.tryStop();
				}
				return true;
			}
		});

		upButton = (Button) findViewById(R.id.buttonUp);
		upButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mGamePanel.tryWalk(1);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mGamePanel.tryStop();
				}
				return true;
			}

		});

		downButton = (Button) findViewById(R.id.buttonDown);
		downButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mGamePanel.tryWalk(2);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mGamePanel.tryStop();
				}

				return true;
			}
		});

		rightButton = (Button) findViewById(R.id.buttonRight);
		rightButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mGamePanel.tryWalk(3);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mGamePanel.tryStop();
				}
				return true;
			}
		});

		bombButton = (Button) findViewById(R.id.buttonBomb);
		bombButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mGamePanel.doAction(4);
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
		finish();
	}

}
