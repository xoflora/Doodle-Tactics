package controller;

import java.util.Date;

import main.GameScreen;
import map.Map;
import map.Tile;
import character.Character;

public class OverworldMover extends Mover {
	
	/**
	 * constructs a movement thread which moves a character between two tiles
	 * @param screen the game screen
	 * @param src the source tile (assumed to contain the character)
	 * @param dest the destination tile (assumed unoccupied)
	 * @param c the character to move
	 * @param follow whether or not the game screen should follow the character with the camera
	 */
	public OverworldMover(GameScreen screen, Tile src, Tile dest, Character c) {
		super(screen, src, dest, c);
	}
	
	@Override
	public void run() {
		super.run();
		
		if (_dest.hasEnterEvent())
			_screen.pushControl(_dest.getEvent());
		else if (_screen.getMap().generatesRandomBattle(_dest))
			_screen.getMap().startBattle(_dest);
		
		return;
	}
	
	synchronized public boolean moveCompleted() {
		return _complete;
	}
}
