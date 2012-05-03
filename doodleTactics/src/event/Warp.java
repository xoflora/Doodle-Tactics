package event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import map.Tile;

public class Warp extends Event {
	
	private Tile _tile;
	private String _eventPath;
	
	private int _newX;
	private int _newY;
	
	//Constructor used during normal gameplay
	public Warp(DoodleTactics dt, Tile tile, String eventPath, int x, int y) {
		super(dt,false);
		_tile = tile;
		_eventPath = eventPath;
		
		_newX = x;
		_newY = y;
	}
	
	public Warp(DoodleTactics dt, Tile tile, String eventPath, boolean hasOccured) {
		super(dt,hasOccured);
		_tile = tile;
		_eventPath = eventPath;
	}

	@Override
	public void take() {
		super.take();
		_gameScreen.setMap(_eventPath, _newX, _newY);
		_tile.removeOccupant();
		_gameScreen.popControl();
	}

	@Override
	public String save() {
		return "warp," + _eventPath + "," + Boolean.toString(this._hasOccurred);
	}

}
