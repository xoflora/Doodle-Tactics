package controller.combatController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.Screen;
import controller.Controller;
import controller.GameScreenController;
import character.Character;

public class CombatWindowController extends GameScreenController {
	
	private Character _src;
	private Character _dest;

	public CombatWindowController(DoodleTactics dt, Character src, Character dest) {
		super(dt);
		
		_src = src;
		_dest = dest;
	}

	@Override
	public void release() {
		super.release();
	}

	@Override
	public void take() {
		super.take();
		_gameScreen.getPopUpCombat().prepareWindow(_src, _dest, this);
		_gameScreen.getPopUpCombat().animate();
	}
	
	/**
	 * 
	 */
	public void done() {
		_gameScreen.popControl();
	}
}
