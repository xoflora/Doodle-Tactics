package main;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import character.Character;

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
		this.setVisible(true);
		_dt = dt;

		try {
			BufferedImage unitsD = ImageIO.read(new File("src/graphics/menu/units.png"));
			BufferedImage unitsH = ImageIO.read(new File("src/graphics/menu/units_hovered.png"));
			BufferedImage mapD = ImageIO.read(new File("src/graphics/menu/map.png"));
			BufferedImage mapH = ImageIO.read(new File("src/graphics/menu/map_hovered.png"));
			BufferedImage saveD = ImageIO.read(new File("src/graphics/menu/save.png"));
			BufferedImage saveH = ImageIO.read(new File("src/graphics/menu/save_hovered.png"));
			BufferedImage optionsD = ImageIO.read(new File("src/graphics/menu/options.png"));
			BufferedImage optionsH = ImageIO.read(new File("src/graphics/menu/options_hovered.png"));
			BufferedImage quitD = ImageIO.read(new File("src/graphics/menu/quit_game_menu.png"));
			BufferedImage quitH = ImageIO.read(new File("src/graphics/menu/quit_game_menu_hovered.png"));
			BufferedImage titleD = ImageIO.read(new File("src/graphics/menu/overlay.png"));
			_units = new MenuItem(this, unitsD,unitsH, dt);
			_map = new MenuItem(this, mapD,mapH,dt);
			_save = new MenuItem(this, saveD, saveH,dt);
			_options = new MenuItem(this, optionsD,optionsH,dt);
			_quit = new MenuItem(this, quitD, quitH,dt);
			_title = new MenuItem(this, titleD, titleD, dt);
		} catch (IOException e) {
			
		}
		
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
		_unitsBox.setVisible(true);
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
		_unitsBox.setSize(735,660);
		_unitsBox.setLocation(200, 120);
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
			this.removeAll();
			this.setDefault();
			_units.setHovered();
			clicked = _units;
			_currClicked = 1;
			_unitsBox.removeAll();
			_unitsBox.setLayout(new GridLayout(_dt.getParty().size(), 1, 10, 10));
			if (_currClicked == 1) {
				for (Character chrter: _dt.getParty()) {
					CharInfo toAdd = new CharInfo(chrter);
					toAdd.setVisible(true);
					_unitsBox.add(toAdd);
					_charInfoList.add(toAdd);
				}
				_unitsBox.revalidate();
				this.add(_unitsBox);
				this.revalidate();
			}
		}
		
		if(_map.contains(point)) {
			this.removeAll();
			this.setDefault();
			_map.setHovered();
			clicked = _map;
			_currClicked = 2;
		}
		
		if(_quit.contains(point)) {
			this.removeAll();
			this.setDefault();
			_quit.setHovered();
			clicked = _quit;
			_currClicked = 3;
		}
		
		if(_options.contains(point)) {
			this.removeAll();
			this.setDefault();
			_options.setHovered();
			clicked = _options;
			_currClicked = 4;
		}
		
		if(_save.contains(point)) {
			this.removeAll();
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
		public CharInfo(Character chrter) {
			this.setLayout(new GridLayout(1, 4));
			java.awt.Dimension panelSize = new java.awt.Dimension(715,150);
			this.setPreferredSize(panelSize);
			this.setSize(715,150);
			this.setLocation(15, 15);
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			this.setBackground(java.awt.Color.MAGENTA);
//			graphics.MenuItem profile = new graphics.MenuItem(this, chrter.getProfileImage(), chrter.getProfileImage(), _dt);
//			profile.setLocation(20, 20);
//			profile.setVisible(true);
			JLabel profile = new JLabel(new ImageIcon(chrter.getProfileImage()));
			profile.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			profile.setSize(chrter.getProfileImage().getWidth(), chrter.getProfileImage().getHeight());
			profile.setVisible(true);
//			profile.setLocation(10, 10);
			JPanel col1 = new JPanel(new GridLayout(2, 1));
			this.add(col1);
			col1.add(profile);
		}
	}
}