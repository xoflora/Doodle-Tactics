package main;

import items.Item;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
			_unitsBox.setLayout(new GridLayout(_dt.getParty().size(), 0, 10, 10));
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
			this.setLayout(new GridBagLayout());
			GridBagConstraints constraint = new GridBagConstraints();
			java.awt.Dimension panelSize = new java.awt.Dimension(715,150);
			this.setPreferredSize(panelSize);
			this.setSize(715,150);
			this.setLocation(15, 15);
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			this.setBackground(java.awt.Color.LIGHT_GRAY);
			JLabel profile = new JLabel(new ImageIcon(chrter.getProfileImage()));
			profile.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			profile.setSize(100, 100);
			profile.setVisible(true);
//			profile.setLocation(10, 10);
			
			JLabel name = new JLabel("Name");
			name.setSize(150, 50);
			name.setVisible(true);
			
			JLabel level = new JLabel("Level : " + chrter.getLevel());
			level.setSize(150, 50);
			level.setVisible(true);
			
			JLabel exp = new JLabel("EXP : " + chrter.getExp());
			level.setSize(150, 50);
			level.setVisible(true);
			
			JButton addToPartyButton = new JButton("Add to Party");
			addToPartyButton.setVisible(true);
			
			JPanel col1 = new JPanel();
			col1.setLayout(new GridBagLayout());
			col1.setOpaque(false);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 0;
			constraint.gridy = 0;
			col1.add(profile, constraint);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 0;
			constraint.gridy = 1;
			col1.add(name, constraint);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 0;
			constraint.gridy = 2;
			col1.add(level, constraint);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 0;
			constraint.gridy = 3;
			col1.add(exp, constraint);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 0;
			constraint.gridy = 4;
			col1.add(addToPartyButton, constraint);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 0;
			constraint.gridy = 0;
			this.add(col1, constraint);
			
			JLabel HP = new JLabel("HP : " + chrter.getHP() + "/" + chrter.getBaseStats()[7]);
			HP.setSize(150, 50);
			HP.setVisible(true);

			JLabel strength = new JLabel("STRENGTH : " + chrter.getBaseStats()[0]);
			strength.setSize(150, 50);
			strength.setVisible(true);
			
			JLabel defense = new JLabel("DEFENSE : " + chrter.getBaseStats()[1]);
			defense.setSize(150, 50);
			defense.setVisible(true);
			
			JLabel special = new JLabel("SPECIAL : " + chrter.getBaseStats()[2]);
			special.setSize(150, 50);
			special.setVisible(true);
			
			JLabel resistance = new JLabel("RESISTANCE : " + chrter.getBaseStats()[3]);
			resistance.setSize(150, 50);
			resistance.setVisible(true);
			
			JLabel speed = new JLabel("SPEED : " + chrter.getBaseStats()[4]);
			speed.setSize(150, 50);
			speed.setVisible(true);
			
			JLabel skill = new JLabel("SKILL : " + chrter.getBaseStats()[5]);
			skill.setSize(150, 50);
			skill.setVisible(true);
			
			JLabel luck = new JLabel("LUCK : " + chrter.getBaseStats()[6]);
			luck.setSize(150, 50);
			luck.setVisible(true);
			
			JPanel col2 = new JPanel();
			col2.setLayout(new GridLayout(8,0, 5, 5));
			col2.setOpaque(false);
			col2.add(HP);
			col2.add(strength);
			col2.add(defense);
			col2.add(special);
			col2.add(resistance);
			col2.add(speed);
			col2.add(skill);
			col2.add(luck);
			constraint.fill = GridBagConstraints.BOTH;
			constraint.gridx = 1;
			constraint.gridy = 0;
			this.add(col2, constraint);
			
//			BufferedImage = 
			
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(0, 5, 5, 5));
			row1.setOpaque(false);
			
			for (Item i: chrter.getInventory()) {
				JLabel item = new JLabel(new ImageIcon(chrter.getProfileImage()));
				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				item.setSize(75,75);
				item.setVisible(true);
				row1.add(item);
			}
			
			for (int i = 0; i<(5-chrter.getInventory().size()); i++) {
				JLabel item = new JLabel(new ImageIcon(chrter.getProfileImage()));
				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				item.setSize(75, 75);
				item.setVisible(true);
				row1.add(item);
			}
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 2;
			constraint.gridy = 0;
			this.add(row1, constraint);
			
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(0, 5, 5, 5));
			row2.setOpaque(false);
			
			JLabel weapon = new JLabel(new ImageIcon(chrter.getProfileImage()));
			weapon.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			row2.add(weapon);
			
//			if (chrter.getCuirass() != null) {
				JLabel cuirass = new JLabel(new ImageIcon(chrter.getProfileImage()));
				cuirass.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				row2.add(cuirass);
//			}
			JLabel shield = new JLabel(new ImageIcon(chrter.getProfileImage()));
			shield.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			row2.add(shield);
			
			JLabel footgear = new JLabel(new ImageIcon(chrter.getProfileImage()));
			footgear.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			row2.add(footgear);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.gridx = 2;
			constraint.gridy = 1;
			this.add(row2, constraint);
			
		}
	}
}