package main;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import graphics.MenuItem;
import graphics.ScreenChangeMenuItem;
import controller.Controller;
import controller.GameMenuController;
import controller.OverworldController;

/** 
 * 
 * @author jeshapir
 */

@SuppressWarnings("serial")
public class GameMenuScreen extends Screen<GameMenuController> {

	private MenuItem _units;
	private MenuItem _quit;
	private MenuItem _save;
	private MenuItem _map;
	private MenuItem _options;
	private MenuItem _title;
	private int _currClicked = 0;
	private DoodleTactics _dt;
	private LinkedList<CharInfo> _charInfoList;
	private JPanel _unitsBox;
	
	public GameMenuScreen(DoodleTactics dt) {
		
		super(dt);
		this.setBackground(java.awt.Color.DARK_GRAY);
//		java.awt.Dimension panelSize = new java.awt.Dimension(875,1000);
//		this.setPreferredSize(panelSize);
//		this.setSize(panelSize);
//		this.setLayout(new java.awt.BorderLayout());
		_dt = dt;

		_units = new MenuItem(this, "src/graphics/menu/units.png","src/graphics/menu/units_hovered.png", dt);
		_map = new MenuItem(this, "src/graphics/menu/map.png","src/graphics/menu/map_hovered.png",dt);
		_save = new MenuItem(this, "src/graphics/menu/save.png","src/graphics/menu/save_hovered.png",dt);
		_options = new MenuItem(this, "src/graphics/menu/options.png","src/graphics/menu/options_hovered.png",dt);
		_quit = new MenuItem(this, "src/graphics/menu/quit_game_menu.png","src/graphics/menu/quit_game_menu_hovered.png",dt);
		_title = new MenuItem(this, "src/graphics/menu/overlay.png", "src/graphics/menu/overlay.png", dt);
		
		int buttonHeight = _units.getCurrentImage().getHeight();
		int buffer = 5;
		int top = 115;
		int left = 8;
		_title.setLocation(0, 0);
		_units.setLocation(left, top);
		_map.setLocation(left, top+buttonHeight+buffer);
		_save.setLocation(left, top+(buttonHeight*2)+(buffer*2));
		_options.setLocation(left, top+(buttonHeight*3)+(buffer*3));
		_quit.setLocation(left, top+(buttonHeight*4)+(buffer*4));
		
		_units.setVisible(true);
		_map.setVisible(true);
		_save.setVisible(true);
		_options.setVisible(true);
		_quit.setVisible(true);
		_title.setVisible(true);
		
		_charInfoList = new LinkedList<CharInfo>();
		
		_unitsBox = new JPanel();
		_unitsBox.setOpaque(false);
	}
	
	protected GameMenuController defaultController() {
		return new GameMenuController(_dt, this);
	}

	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		_title.paint((Graphics2D) g, _title.getCurrentImage());
		_units.paint((Graphics2D) g, _units.getCurrentImage());
		_save.paint((Graphics2D) g, _save.getCurrentImage());
		_quit.paint((Graphics2D) g, _quit.getCurrentImage());
		_options.paint((Graphics2D) g, _options.getCurrentImage());
		_map.paint((Graphics2D) g, _map.getCurrentImage());
		this.removeAll();
		if (_currClicked == 1) {
			_unitsBox.removeAll();
			_unitsBox.setLayout(new GridLayout(_charInfoList.size(), 1, 10, 10));
			_unitsBox.setSize(735,660);
			_unitsBox.setLocation(200, 120);
			for (character.Character character: _dt.getParty()) {
				CharInfo toAdd = new CharInfo(character);
				toAdd.setVisible(true);
				toAdd.revalidate();
				_unitsBox.add(toAdd);
				_charInfoList.add(toAdd);
			}
			this.add(_unitsBox);
			this.revalidate();
		}
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public void setDefault() {
		_units.setDefault();
		_map.setDefault();
		_quit.setDefault();
		_options.setDefault();
		_save.setDefault();
	}
	
	public MenuItem checkContains(java.awt.Point point) {
		
		/* set all of the buttons to default */
		
		/* check if the point is in any of the buttons */
		MenuItem clicked = null;
		
		if(_units.contains(point)) {
			this.setDefault();
			_units.setHovered();
			clicked = _units;
			_currClicked = 1;
		}
		
		if(_map.contains(point)) {
			this.setDefault();
			_map.setHovered();
			clicked = _map;
			_currClicked = 2;
		}
		
		if(_quit.contains(point)) {
			this.setDefault();
			_quit.setHovered();
			clicked = _quit;
			_currClicked = 3;
		}
		
		if(_options.contains(point)) {
			this.setDefault();
			_options.setHovered();
			clicked = _options;
			_currClicked = 4;
		}
		
		if(_save.contains(point)) {
			this.setDefault();
			_save.setHovered();
			clicked = _save;
			_currClicked = 5;
		}
		this.repaint();
		return clicked;
	}
	
	private class CharInfo extends JPanel {
		//represents a box that will display all the characters current stats, items, inventory, etc.
		public CharInfo(character.Character character) {
			java.awt.Dimension panelSize = new java.awt.Dimension(715,150);
			this.setPreferredSize(panelSize);
			this.setSize(715,150);
			this.setLocation(15, 15);
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			BufferedImage _profilePic = character.getProfileImage();
			this.add(_profilePic);
		}
	}
}
