package controller.combatController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import util.Util;

import main.DoodleTactics;
import map.Tile;
import controller.GameScreenController;
import character.Character;

/**
 * controller class corresponding to the combat set-up step
 * @author rroelke
 *
 */
public class PlayerSetup extends GameScreenController implements PoolDependent {
	
	private List<Tile> _validTiles;
	private List<Character> _units;
	private Character _selectedCharacter;
	private List<Character> _toPlace;
	private List<Character> _inPlace;
	private Tile _selectedTile;
	private UnitPool _pool;

	public PlayerSetup(DoodleTactics dt, List<Tile> validTiles) {
		super(dt);
		_validTiles = validTiles;
		_units = _dt.getParty();
		_toPlace = Util.clone(_units);
		_inPlace = new ArrayList<Character>();
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void take() {
	/*	_pool = new CharacterPool(_units);
		
		ListIterator<Character> _unitCycle = _units.listIterator();
		Character c;
		for (Tile t : _validTiles) {
			if (_unitCycle.hasNext()) {
				c = _unitCycle.next();
				t.setOccupant(c);
				_toPlace.remove(c);
				_pool.removeCharacter(c);
			}
			else
				break;
		}
		
		_pool.setVisible(true);		*/
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (_validTiles.contains(t)) {
			_selectedTile = t;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

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
		
	}

	@Override
	public void getCharacterFromPool(Character c) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Character> getUnits() {
		return _units;
	}

	@Override
	public UnitPool getPool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCharacterToPool(Character c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUnitFromPool(Character c) {
		// TODO Auto-generated method stub
		
	}

}
