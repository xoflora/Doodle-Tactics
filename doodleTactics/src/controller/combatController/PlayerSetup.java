package controller.combatController;

import graphics.Rectangle;
import graphics.Shape;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Util;

import main.DoodleTactics;
import map.Tile;
import controller.GameScreenController;
import character.Character;

public class PlayerSetup extends GameScreenController {
	
	/**
	 * 
	 * @author rroelke
	 *
	 */
	private class CharacterPool extends JFrame {

		private JPanel _panel;
		private HashMap<Character, JLabel> _labels;
		
		public CharacterPool(List<Character> characters) {
			super();
			_panel = new JPanel(new FlowLayout());
			
			for (Character c : characters) {
				JLabel label = new JLabel(new ImageIcon(c.getDownImage()));
				_labels.put(c, label);
				_panel.add(label);
			}
			
			add(_panel);
		}
		
		public void removeCharacter(Character c) {
			_panel.remove(_labels.get(c));
		}
		
		public void addCharacter(Character c) {
			_panel.add(_labels.get(c));
		}
	}
	
	private List<Tile> _validTiles;
	private List<Character> _units;
	private Character _selectedCharacter;
	private List<Character> _toPlace;
	private List<Character> _inPlace;
	private Tile _selectedTile;
	private CharacterPool _pool;

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
		_pool = new CharacterPool(_units);
		
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
		
		_pool.setVisible(true);
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

}
