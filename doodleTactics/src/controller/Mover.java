package controller;

import java.util.Date;

import main.GameScreen;
import map.Map;
import map.Tile;
import character.Character;

public class Mover extends Thread {
	
	protected static final int ADVANCE = 1;

	protected GameScreen _screen;
	protected Tile _src;
	protected Tile _dest;
	protected Character _c;
	protected int _direction;
	
	protected boolean _complete;
	
	/**
	 * constructs a movement thread which moves a character between two tiles
	 * @param screen the game screen
	 * @param src the source tile (assumed to contain the character)
	 * @param dest the destination tile (assumed unoccupied)
	 * @param c the character to move
	 * @param follow whether or not the game screen should follow the character with the camera
	 */
	public Mover(GameScreen screen, Tile src, Tile dest, Character c) {
		_screen = screen;
		_src = src;
		_dest = dest;
		_c = c;
		_complete = false;
		
		if (_src.x() == _dest.x())
			if (_src.y() < _dest.y())
				_direction = Map.SOUTH;
			else
				_direction = Map.NORTH;
		else
			if (_src.x() < _dest.x())
				_direction = Map.EAST;
			else
				_direction = Map.WEST;
	}
	
	@Override
	public void run() {
		int dx, dy;
		if (_direction == Map.NORTH) {
			dx = 0;
			dy = -ADVANCE;
		}
		else if (_direction == Map.EAST) {
			dx = ADVANCE;
			dy = 0;
		}
		else if (_direction == Map.SOUTH) {
			dx = 0;
			dy = ADVANCE;
		}
		else {
			dx = -ADVANCE;
			dy = 0;
		}

		synchronized (_src) {
			synchronized(_dest) {
				
				if (_src.getOccupant() != _c || _dest.isOccupied())
					return;
				
				_src.setOccupant(null);
				_dest.setOccupant(_c);
				
				boolean update = true;
				long time = new Date().getTime();
				int distAcc = 0;
				
				while (distAcc < Tile.TILE_SIZE) {
					if ((new Date().getTime() - time) % 5 == 0) {
						if (update) {
							_c.setLocation(_c.getX() + dx, _c.getY() + dy);
							distAcc += ADVANCE;

							synchronized(_screen) {
								_screen.pan(-dx, -dy);
								_screen.repaint();
							}
							
							update = false;
						}
					}
					else {
						update = true;
					}
				}
			}
		}
		
		_complete = true;
		
		return;
	}
	
	synchronized public boolean moveCompleted() {
		return _complete;
	}
}
