package com.example.bombermancmov;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
import com.example.bombermancmov.model.component.MasterNetworkComponent;
import com.example.bombermancmov.model.component.NullMasterNetworkComponent;
import com.example.bombermancmov.model.component.NullPeerNetworkComponent;
import com.example.bombermancmov.model.component.PeerNetworkComponent;
import com.example.bombermancmov.model.component.SoundComponent;
import com.example.bombermancmov.wifi.AcceptNewPeersThread;
import com.example.bombermancmov.wifi.CommandRequest;
import com.example.bombermancmov.wifi.CommandRequestUtil;
import com.example.bombermancmov.wifi.commands.Command;
import com.example.bombermancmov.wifi.commands.PlaceBombCommand;
import com.example.bombermancmov.wifi.commands.TryMoveCommand;
import com.example.bombermancmov.wifi.commands.TryStopCommand;

/**
 * This is the main surface that handles the on-touch events and draws the image
 * to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	public static final int ROUND_TIME = 1000; // milliseconds
	public static final int MASTER_PORT = 10001;
	private static final int MASTER_ID = 0;
	public String masterHost;

	/** Update and render game model in a separate thread. */
	private GameLoopThread gameLoopThread;
	private AcceptNewPeersThread acceptPeersThread;
	private MasterNetworkComponent masterNetComp;
	private PeerNetworkComponent peerNetComp;
	private Map<String, Command> mCommands;

	// Run level.nextRound() every x milliseconds
	private LevelNextRoundThread levelNextRound;

	private SurfaceHolder surfaceHolder;

	private Activity context;

	/* Game model */
	private Game game;
	private Resource mResource;
	private boolean isMaster;

	private StatusScreenUpdater mStatusScreenUpdater; // HORRIBLE HACK!

	public MainGamePanel(GameActivity gameActivity,
			StatusScreenUpdater updater, boolean isSingleplayer,
			boolean isMaster, String masterIp) {
		super(gameActivity);
		this.context = gameActivity;
		surfaceHolder = getHolder();
		mStatusScreenUpdater = updater;

		// adding the callback(this) to the surface holder to intercept events
		surfaceHolder.addCallback(this);

		// create level
		mResource = new Resource(this);
		mResource.setExplosionSoundComponent(new SoundComponent(this));

		// TODO more flags bleh dX
		this.isMaster = isMaster;
		this.masterHost = masterIp;
		if (isSingleplayer) {
			masterNetComp = new NullMasterNetworkComponent();
			peerNetComp = new NullPeerNetworkComponent();
			game = new Game(mResource, isSingleplayer);
		} else {
			masterNetComp = new MasterNetworkComponent();
			peerNetComp = new PeerNetworkComponent();
			game = new Game(mResource, isSingleplayer);
		}
		// make the GamePanel focusable so it can handle events
		mCommands = initCommands();
		setFocusable(true);
		Log.d(TAG, "constructor");
	}

	private Map<String, Command> initCommands() {
		Map<String, Command> commands = new HashMap<String, Command>();
		commands.put(TryMoveCommand.CODE, new TryMoveCommand(game));
		commands.put(TryStopCommand.CODE, new TryStopCommand(game));
		commands.put(PlaceBombCommand.CODE, new PlaceBombCommand());
		return commands;
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
		// create master thread
		if (isMaster) {
			try {
				masterNetComp.createServerSocket(MASTER_PORT);
			} catch (IOException e) {
				Log.e(TAG, "IO new server socket: " + e.getMessage());
			}
			acceptPeersThread = new AcceptNewPeersThread(masterNetComp);
			acceptPeersThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		shutDownThreads();
	}

	private void shutDownThreads() {
		gameLoopThread.setRunning(false);
		shutDown(gameLoopThread);
		Log.d(TAG, "Game loop thread was shut down cleanly");

		levelNextRound.setRunning(false);
		shutDown(levelNextRound);
		Log.d(TAG, "Level next round thread was shut down cleanly");

		if (isMaster) {
			acceptPeersThread.interrupt();
			try {
				masterNetComp.closeServerSocket();
			} catch (IOException e) {
				Log.e(TAG, "IO closing sockets: " + e.getMessage());
			}
			shutDown(acceptPeersThread);
			Log.d(TAG, "Accept peers thread was shut down cleanly");
		}
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

		if (this.game.isFinished()) {
			this.buildEnddialog();
		}
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

		if (this.game.isFinished()) {
			this.buildEnddialog();
		}
	}

	public void placeBomb(int id) {
		Character player = game.getPlayerByNumber(id);
		if (player.isAlive()) {
			int x = (int) Math.rint(player.getX());
			int y = (int) Math.rint(player.getY());
			game.placeBomb(id, x, y);
		}
	}

	/**
	 * Send command requests to master. If master, execute commands received.
	 * 
	 * @param timePassed
	 */
	public void update(long timePassed) {
		List<CommandRequest> cmdRequests = new ArrayList<CommandRequest>();
		if (isMaster) {
			receiveCommandRequests(cmdRequests);

			game.getPlayerInput().setPlayerId(MASTER_ID);
			cmdRequests = game.getPlayerInput().consumeCommandRequests();
			CommandRequestUtil.executeCommandRequests(cmdRequests, mCommands);

			game.update(timePassed);
			updateStatusScreen();
		} else {
			try {
				// Get player id from master and update
				peerNetComp.createClientSocket(masterHost, MASTER_PORT);
				int playerId = peerNetComp.getPlayerId();
				PlayerInput playerInput = game.getPlayerInput();
				playerInput.setPlayerId(playerId);
				// Send command requests
				cmdRequests = game.getPlayerInput().consumeCommandRequests();
				CommandRequestUtil.executeCommandRequests(cmdRequests,
						mCommands);
				sendCommandRequests(cmdRequests);
			} catch (ClassNotFoundException e) {
				Log.e(TAG, "ClassNotFound new client socket: " + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, "IO new client socket: " + e.getMessage());
			}
		}
	}

	private void sendCommandRequests(List<CommandRequest> cmdRequests) {
		try {
			peerNetComp.sendCommandRequests(cmdRequests);
		} catch (IOException e) {
			Log.e(TAG, "IO sending command requests: " + e.getMessage());
		}
	}

	private void receiveCommandRequests(List<CommandRequest> cmdRequests) {
		try {
			cmdRequests = masterNetComp.receiveCommandRequests();
		} catch (OptionalDataException e) {
			Log.e(TAG,
					"OptionalData receive command requests: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(TAG,
					"ClassNotFound receive command requests: " + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "IO receive command requests: " + e.getMessage());
		}
		CommandRequestUtil.executeCommandRequests(cmdRequests, mCommands);
	}

	private void updateStatusScreen() {
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

	// Enddialog for finishing a game
	public void buildEnddialog() {
		invalidate();
		// game completed
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);

		switch (this.game.getEndStatus()) {
		case 1:
			builder.setTitle(context.getText(R.string.finished_title_win));
			break;
		case 2:
			builder.setTitle(context
					.getText(R.string.finished_title_lost_killed));
			break;
		case 3:
			builder.setTitle(context.getText(R.string.finished_title_lost_time));
			break;
		default:
			builder.setTitle(context
					.getText(R.string.finished_title_lost_unkown));
			break;
		}

		LayoutInflater inflater = context.getLayoutInflater();
		View view = inflater.inflate(R.layout.finish, null);
		builder.setView(view);
		View closeButton = view.findViewById(R.id.closeGame);

		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View clicked) {
				if (clicked.getId() == R.id.closeGame) {
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

	public Resource getResource() {
		return mResource;
	}
}
