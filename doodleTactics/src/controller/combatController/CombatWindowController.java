package controller.combatController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.Screen;
import map.Tile;
import controller.Controller;
import controller.GameScreenController;
import character.Character;

public class CombatWindowController extends GameScreenController {
	
	private Tile _src;
	private Tile _dest;
	private int _range;

	public CombatWindowController(DoodleTactics dt, Tile src, Tile dest) {
		super(dt);
		
		_src = src;
		_dest = dest;
		_range = src.gridDistanceToTile(dest);
	}

	@Override
	public void release() {
		super.release();
	}

	@Override
	public void take() {
		super.take();
		_gameScreen.getPopUpCombat().animate(_src, _dest, this, _range);
	}
	
	/**
	 * 
	 */
	public void done() {
		_gameScreen.popControl();
	}
}
