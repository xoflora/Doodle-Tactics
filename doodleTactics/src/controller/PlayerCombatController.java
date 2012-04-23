package controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import util.Util;

import main.DoodleTactics;
import map.*;
import character.Character;

/**
 * 
 * @author rroelke
 * 
 */
public class PlayerCombatController extends CombatController {
	
	private Tile _selectedTile;
	private Character _selectedCharacter;
	private Character _hovered;
	
	private List<Tile> _selectedMovementRange;
	private List<Tile> _enemyAttackRange;
	
	public PlayerCombatController(DoodleTactics dt) {
		super(dt);
		
		_enemyAttackRange = new ArrayList<Tile>();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null && t.getOccupant() != null) {
			Character c = t.getOccupant();
			if (c.getAffiliation() == this) {
				_selectedTile = t;
				_selectedMovementRange = _map.getMovementRange(t, c.getMovementRange());
				
				//
			}
			else if (_enemyAffiliations.contains(c.getAffiliation())) {
				_enemyAttackRange = Util.union(_enemyAttackRange,
						_map.getAttackRange(t, c.getMovementRange(),
								c.getMinAttackRange(), c.getMaxAttackRange()));
				//paint all tiles in that range.
				for (Tile paint : _enemyAttackRange) {
					
				}
			}
		}
	}

	@Override
	/**
	 * does nothing
	 */
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	/**
	 * does nothing
	 */
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
			_hovered = t.getOccupant();
		}
	}

	@Override
	/**
	 * ends the player's turn
	 */
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * begins the player's turn
	 */
	public void take() {
		// TODO Auto-generated method stub
		
	}
}
