package event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import map.Tile;

public class Warp extends Event {
	
	private Tile _tile;
	private String _eventPath;
	
	//Constructor used during normal gameplay
	public Warp(DoodleTactics dt, Tile tile, String eventPath) {
		super(dt,false);
		_tile = tile;
		_eventPath = eventPath;
	}
	
	public Warp(DoodleTactics dt, Tile tile, String eventPath,boolean hasOccured) {
		super(dt,hasOccured);
		_tile = tile;
		_eventPath = eventPath;
	}

	@Override
	public void take() {
		super.take();
		_gameScreen.setMap(_eventPath);
		_tile.removeOccupant();
		_gameScreen.popControl();
	}

	@Override
	public String save() {
		return "warp," + _eventPath + "," + Boolean.toString(this._hasOccurred);
	}

}
