package main;

import items.Item;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
	private HashMap<JLabel, Character> _labelToCharacter;
	private HashMap<JLabel, Item> _labelToItem;
	private JScrollPane _scrollBar;
	
	public GameMenuScreen(DoodleTactics dt) {
		
		super(dt);
		this.setBackground(java.awt.Color.DARK_GRAY);
		this.setVisible(true);
		_dt = dt;
		_labelToCharacter = new HashMap<JLabel, Character>();
		_labelToItem = new HashMap<JLabel, Item>();
		
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
		
		int buttonHeight = _units.getImage().getHeight();
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
		
		_scrollBar = new JScrollPane(_unitsBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		_scrollBar.setVisible(true);
		_scrollBar.setSize(new Dimension(752, 662));
		_scrollBar.setLocation(new Point(200, 120));
	}
	
	/**
	 * Initialized the screen to be set to the units tab
	 */
	
	public void setDefaultTabToUnits() {
		_units.setHovered();
		_currClicked = 1;
		_unitsBox.removeAll();
		_unitsBox.setLayout(new GridLayout(_dt.getParty().size(), 0, 10, 10));
		for (Character chrter: _dt.getParty()) {
			CharInfo toAdd = new CharInfo(this, chrter);
			toAdd.setVisible(true);
			_unitsBox.add(toAdd);
			_charInfoList.add(toAdd);
		}
		_unitsBox.revalidate();
		this.add(_scrollBar);
		_scrollBar.revalidate();
		this.revalidate();
	}
	
	protected GameMenuController defaultController() {
		return new GameMenuController(_dt, this);
	}

	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		_scrollBar.setSize(new Dimension(750, 660));
		_scrollBar.setLocation(new Point(200, 120));
		_unitsBox.setSize(750,660);
		_unitsBox.setLocation(1,0);
//		_scrollBar.setSize(new Dimension(750, 660));
//		_scrollBar.setLocation(new Point(200, 120));
		_title.paint((Graphics2D) g, _title.getImage());
		_units.paint((Graphics2D) g, _units.getImage());
		_save.paint((Graphics2D) g, _save.getImage());
		_quit.paint((Graphics2D) g, _quit.getImage());
		_options.paint((Graphics2D) g, _options.getImage());
		_map.paint((Graphics2D) g, _map.getImage());
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
//			_scrollBar.removeAll();
			_unitsBox.setLayout(new GridLayout(_dt.getParty().size(), 0, 10, 10));
//			_scrollBar.setVisible(true);
			if (_currClicked == 1) {
				for (Character chrter: _dt.getParty()) {
					CharInfo toAdd = new CharInfo(this, chrter);
					toAdd.setVisible(true);
					_unitsBox.add(toAdd);
					_charInfoList.add(toAdd);
				}
				_unitsBox.revalidate();
				_unitsBox.addMouseListener(this.getController());
//				_scrollBar.getViewport().add(_unitsBox);
//				this.add(_unitsBox);
//				_scrollBar.add(_unitsBox);
				this.add(_scrollBar);
				_scrollBar.revalidate();
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
//			this.removeAll();
//			this.setDefault();
//			_quit.setHovered();
//			clicked = _quit;
//			_currClicked = 3;
			System.exit(0);
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
		
		for (JLabel label: _labelToCharacter.keySet()) {
			if (label.contains(point)) {
				System.out.println("clicked an item");
			}
		}
		this.repaint();
		return clicked;
	}
	
	public MenuItem checkItemContains(java.awt.Point point) {
		for (JLabel label: _labelToCharacter.keySet()) {
			if (label.contains(point)) {
				System.out.println("hovered over an item");
			}
		}
		return null;
	}
	
	public void switchToGameScreen() {
		
	}
	
	/**
	 * A char info box represents one character info box in the units tab.
	 * @author fjin
	 *
	 */
	
	private class CharInfo extends JPanel {
		//represents a box that will display all the characters current stats, items, inventory, etc.
		public CharInfo(Screen screen, Character chrter) {
			this.setLayout(new GridBagLayout());
			GridBagConstraints constraint = new GridBagConstraints();
			java.awt.Dimension panelSize = new java.awt.Dimension(730,200);
			this.setPreferredSize(panelSize);
			this.setSize(730,200);
			this.setLocation(15, 15);
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			this.setBackground(java.awt.Color.LIGHT_GRAY);
			JLabel profile = new JLabel(new ImageIcon(chrter.getProfileImage()));
			profile.setSize(150, 150);
			profile.setPreferredSize(new Dimension(150,150));
			profile.setMaximumSize(new Dimension(150, 150));
//			profile.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			profile.setVisible(true);
			
			Insets inset = new Insets(5, 5, 5, 5);
			
			JLabel name = new JLabel("Renoir");
			name.setSize(200, 50);
			name.setVisible(true);
			
			JLabel level = new JLabel("Level : " + chrter.getLevel());
			level.setSize(200, 50);
			level.setVisible(true);
			
			JLabel exp = new JLabel("EXP : " + chrter.getExp());
			level.setSize(200, 50);
			level.setVisible(true);
//			
//			JPanel col1 = new JPanel();
//			col1.setLayout(new GridBagLayout());
//			col1.setOpaque(false);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.insets = inset;
			constraint.gridx = 0;
			constraint.gridy = 0;
			this.add(profile, constraint);
			
			JPanel col2 = new JPanel(new GridLayout(3,0));
			col2.setOpaque(false);
			
			col2.add(name, constraint);
			col2.add(level, constraint);
			col2.add(exp, constraint);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.insets = new Insets(15, 15, 15, 15);
			constraint.gridx = 1;
			constraint.gridy = 0;
			this.add(col2, constraint);
			
			JLabel HP = new JLabel("HP : " + chrter.getHP() + "/" + chrter.getBaseStats()[7]);
			HP.setFont(new Font("Myriad Pro", 12, 1));
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
			_scrollBar.setVisible(true);
			JLabel skill = new JLabel("SKILL : " + chrter.getBaseStats()[5]);
			skill.setSize(150, 50);
			skill.setVisible(true);
			
			JLabel luck = new JLabel("LUCK : " + chrter.getBaseStats()[6]);
			luck.setSize(150, 50);
			luck.setVisible(true);
			
			JPanel col3 = new JPanel();
			col3.setLayout(new GridLayout(8,0, 5, 5));
			col3.setOpaque(false);
			col3.add(HP);
			col3.add(strength);
			col3.add(defense);
			col3.add(special);
			col3.add(resistance);
			col3.add(speed);
			col3.add(skill);
			col3.add(luck);
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = inset;
			constraint.gridx = 2;
			constraint.gridy = 0;
			this.add(col3, constraint);
			
//			BufferedImage = 
			
			JPanel row1 = new JPanel();
			row1.setLayout(new GridBagLayout());
//			row1.setLayout(new GridLayout(2, 5, 5, 5));
			row1.setOpaque(false);
			
			BufferedImage inventoryPic = null;
			
			try {
				inventoryPic = ImageIO.read(new File("src/graphics/menu/inventory.png"));
//				System.out.println("inventory pic size: w: " + inventoryPic.getWidth() + "y: " + inventoryPic.getHeight());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Insets insetItems = new Insets(5, 5, 5, 5);
						
			JLabel inventory = new JLabel(new ImageIcon(inventoryPic));
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridwidth = 5;
			constraint.gridx = 0;
			constraint.gridy = 0;
			row1.add(inventory, constraint);
			
			for (int i=0; i<chrter.getInventory().size(); i++) {
				JLabel item = new JLabel(new ImageIcon(chrter.getInventory().get(i).getImage()));
//				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				item.addMouseMotionListener(GameMenuScreen.this.getController());
				item.setSize(75,75);
				item.setVisible(true);
				constraint.fill = GridBagConstraints.BOTH;
				constraint.insets = insetItems;
				constraint.gridwidth = 1;
				constraint.gridx = i;
				constraint.gridy = 1;
				row1.add(item, constraint);
				item.addMouseListener(screen.getController());
				_labelToCharacter.put(item, chrter);
				_labelToItem.put(item, chrter.getInventory().get(i));
			}
			
			System.out.println(chrter.getInventory().size());
			
			for (int i = 0; i<(5-chrter.getInventory().size()); i++) {
				JLabel item = new JLabel(new ImageIcon(chrter.getRightImage()));
//				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				item.setSize(75, 75);
				item.setVisible(true);
				constraint.fill = GridBagConstraints.BOTH;
				constraint.insets = insetItems;
				constraint.gridwidth = 1;
				constraint.gridx = i+(chrter.getInventory().size());
				constraint.gridy = 1;
				row1.add(item, constraint);
			}
			
			BufferedImage equippedPic = null;
			
			try {
				equippedPic = ImageIO.read(new File("src/graphics/menu/equipped.png"));
//				System.out.println("inventory pic size: w: " + inventoryPic.getWidth() + "y: " + inventoryPic.getHeight());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JLabel equipped = new JLabel(new ImageIcon(equippedPic));
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridwidth = 5;
			constraint.gridx = 0;
			constraint.gridy = 2;
			row1.add(equipped, constraint);
			
			JLabel weapon = new JLabel(new ImageIcon(chrter.getDownImage()));
//			weapon.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridwidth = 1;
			constraint.gridx = 0;
			constraint.gridy = 3;
			row1.add(weapon, constraint);
			
//			if (chrter.getCuirass() != null) {
				JLabel cuirass = new JLabel(new ImageIcon(chrter.getDownImage()));
//				cuirass.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				constraint.fill = GridBagConstraints.BOTH;
				constraint.insets = insetItems;
				constraint.gridx = 1;
				constraint.gridy = 3;
				row1.add(cuirass, constraint);
//			}
			JLabel shield = new JLabel(new ImageIcon(chrter.getDownImage()));
//			shield.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			constraint.fill = GridBagConstraints.BOTH;
			constraint.gridwidth = 1;
			constraint.insets = insetItems;
			constraint.gridx = 2;
			constraint.gridy = 3;
			row1.add(shield, constraint);
			
			JLabel footgear = new JLabel(new ImageIcon(chrter.getRightImage()));
//			footgear.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridwidth = 1;
			constraint.gridx = 3;
			constraint.gridy = 3;
			row1.add(footgear, constraint);
			
			constraint.fill = GridBagConstraints.BOTH;
			constraint.weighty = 0.5;
			constraint.insets = inset;
			constraint.gridx = 3;
			constraint.gridy = 0;
			this.add(row1, constraint);
			
		}
	}
}