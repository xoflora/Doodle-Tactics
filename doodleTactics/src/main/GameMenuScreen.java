package main;

import items.Item;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import character.Character;

import graphics.MenuItem;
import controller.GameMenuController;

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
	public int _currClicked = 0;
	private DoodleTactics _dt;
	private LinkedList<CharInfo> _charInfoList;
	private JLayeredPane _unitsBox, _itemInfoBox;
	private JLayeredPane _itemOptionsBox;
	private HashMap<JLabel, Character> _labelToCharacter;
	private HashMap<JLabel, Item> _labelToItem;
	private JScrollPane _scrollBar;
//	public boolean _beingHovered = false;
	private boolean _showingItemOptions;
	private itemListener _itemListener;
	private unitsBoxListener _unitsBoxListener;
	private int _itemOptBoxX, _itemOptBoxY, _numOptions;
	
	public GameMenuScreen(DoodleTactics dt) {
		
		super(dt);
		this.setBackground(java.awt.Color.DARK_GRAY);
		this.setVisible(true);
		_dt = dt;
		_labelToCharacter = new HashMap<JLabel, Character>();
		_labelToItem = new HashMap<JLabel, Item>();
		System.out.println("set to false");
		_showingItemOptions = false;
		
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
		
		_unitsBox = new JLayeredPane();
//		_unitsBox.setVisible(true);
		_unitsBox.setBackground(java.awt.Color.DARK_GRAY);
		
		_unitsBox.setSize(750,660);
		_unitsBox.setLocation(1,0);
		
		_itemInfoBox = new JLayeredPane();
		_itemInfoBox.setVisible(true);
		_itemInfoBox.setBackground(java.awt.Color.DARK_GRAY);
		
		_scrollBar = new JScrollPane(_unitsBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		_scrollBar.setVisible(true);
		_scrollBar.setBackground(java.awt.Color.DARK_GRAY);
		Dimension d = new Dimension(752, 662);
		_scrollBar.setSize(d);
//		_scrollBar.setPreferredSize(d);
//		_scrollBar.setMaximumSize(d);
//		_scrollBar.setMinimumSize(d);
		_scrollBar.setLocation(new Point(200, 120));
		
//		_scrollBar.setSize(new Dimension(750, 660));
//		_scrollBar.setLocation(new Point(200, 120));
//		_unitsBox.setSize(750,660);
//		_unitsBox.setLocation(1,0);
		
		_itemOptionsBox = new JLayeredPane();
		_itemOptionsBox.setBackground(java.awt.Color.WHITE);
		_itemOptionsBox.setVisible(true);
		_numOptions = 0;
		_itemOptBoxX = 0;
		_itemOptBoxY = 0;
		
		_itemListener = new itemListener();
		_unitsBoxListener = new unitsBoxListener();
	}
	
	/**
	 * Initialized the screen to be set to the units tab
	 */
	
	public void setDefaultTabToUnits() {
		_units.setHovered();
		_currClicked = 1;
		_unitsBox.removeAll();
		_unitsBox.setLayout(new GridLayout(_dt.getParty().size(), 0, 10, 10));
		_unitsBox.addMouseMotionListener(_unitsBoxListener);
		_unitsBox.addMouseListener(_unitsBoxListener);
		_labelToCharacter = new HashMap<JLabel, Character>();
		_labelToItem = new HashMap<JLabel, Item>();
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
//		if (_showingItemOptions) {
//			System.out.println("X: " + _itemOptBoxX + "; Y: " + _itemOptBoxY);
//			_itemOptionsBox.setSize(5, 5);
//			_itemOptionsBox.setLocation(_itemOptBoxX, _itemOptBoxY);
//		}
		_scrollBar.setSize(new Dimension(748, 660));
		_scrollBar.setLocation(new Point(200, 120));
		_title.paint((Graphics2D) g, _title.getImage());
		_units.paint((Graphics2D) g, _units.getImage());
		_save.paint((Graphics2D) g, _save.getImage());
		_quit.paint((Graphics2D) g, _quit.getImage());
		_options.paint((Graphics2D) g, _options.getImage());
		_map.paint((Graphics2D) g, _map.getImage());
	}

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
			_unitsBox.addMouseMotionListener(_unitsBoxListener);
			_unitsBox.addMouseListener(_unitsBoxListener);
//			_scrollBar.removeAll();
			_unitsBox.setLayout(new GridLayout(_dt.getParty().size(), 0, 10, 10));
			_labelToCharacter = new HashMap<JLabel, Character>();
			_labelToItem = new HashMap<JLabel, Item>();
//			_scrollBar.setVisible(true);
			for (Character chrter: _dt.getParty()) {
				CharInfo toAdd = new CharInfo(this, chrter);
				toAdd.setVisible(true);
				_unitsBox.add(toAdd);
				_charInfoList.add(toAdd);
			}
			_unitsBox.revalidate();
//			_unitsBox.addMouseListener(this.getController());
//			_scrollBar.getViewport().add(_unitsBox);
//			this.add(_unitsBox);
//			_scrollBar.add(_unitsBox);
			this.add(_scrollBar, 3);
			_scrollBar.revalidate();
			this.revalidate();
		}
		
		if(_map.contains(point)) {
			this.removeAll();
			this.setDefault();
			_map.setHovered();
			clicked = _map;
			_currClicked = 2;
		}
		
		if(_quit.contains(point)) {
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
		this.repaint();
		return clicked;
	}
	
	public Item checkItemContains(java.awt.Point point) {
		_itemInfoBox.removeAll();
		this.remove(_itemInfoBox);
		_itemInfoBox.setVisible(false);
		for (JLabel label: _labelToCharacter.keySet()) {
			if (label.contains(point)) {
				this.showItemInfo(_labelToItem.get(label));
//				_beingHovered = true;
				_itemInfoBox.repaint();
				return _labelToItem.get(label);
			}
		}
//		_beingHovered = false;
		this.repaint();
		return null;
	}
	
	public void checkStopHovering(java.awt.Point point) {
		for (JLabel label: _labelToCharacter.keySet()) {
			if (label.contains(point)) {
				return;
			}
		}
		this.remove(_itemInfoBox);
//		_beingHovered = false;
		this.repaint();
	}
	
	public void checkStopShowingOptionsBox(java.awt.Point point) {
		if (_showingItemOptions) {
			/**
			 * point.getX() and point.getY() get the X and Y coordinates only relative to the units box, 
			 * so these values get the location of the units box added to them before checking
			 */
			if (point.getX()+200 < _itemOptBoxX || point.getX()+200 > _itemOptBoxX+200 || point.getY()+120 < _itemOptBoxY || point.getY()+120 > _itemOptBoxY+200) {
				this.remove(_itemOptionsBox);
				System.out.println("set to false");
				_showingItemOptions = false;
				this.repaint();
			}
		}
	}
	
	/**
	 * Displays the item info at the left side of the screen
	 * @param item- Item that is being hovered
	 */
	
	public void showItemInfo(Item item) {
		_itemInfoBox.setLocation(15, 460);
		_itemInfoBox.setSize(144, 340);
		_itemInfoBox.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets inset = new Insets(5, 5, 5, 5);
		JLabel profile = new JLabel(new ImageIcon(item.getImage()));
		profile.setSize(75, 75);
//		profile.setPreferredSize(new Dimension(150,150));
//		profile.setMaximumSize(new Dimension(150, 150));
		profile.setVisible(true);
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.5;
		c.insets = inset;
		c.gridx = 0;
		c.gridy = 0;
		_itemInfoBox.add(profile, c);
		_itemInfoBox.setVisible(true);
		this.add(_itemInfoBox, 2);
	}
	
	public void checkItemClicked(java.awt.Point point) {
		System.out.println("doing stuff");
		for (JLabel label: _labelToCharacter.keySet()) {
			if (label.contains(point)) {
				System.out.println("1");
				_showingItemOptions = true;
				this.displayItemOptions(label);
				return;
			}
		}
		/**
		 * Might not ever get here...
		 */
		System.out.println("set to false");
		_showingItemOptions = false;
		this.remove(_itemOptionsBox);
		repaint();
	}
	
	public void displayItemOptions(JLabel label) {
				
		/**
		 * number of options is set by the #of people in the party -1, plus 3 options for each item
		 */
		
		Item item = _labelToItem.get(label);
		Character character = _labelToCharacter.get(label);
		
		int labelWidth = 200;
		int labelHeight = 15;
		label.revalidate();
		this.remove(_itemOptionsBox);
		_itemOptionsBox.removeAll();
		_numOptions = _dt.getParty().size()+2;
		GridLayout lay = new GridLayout(_numOptions, 0);
		_itemOptionsBox.setLayout(lay);
		if (_labelToItem.get(label).isEquip()) {
			for (int i=0; i<character.getInventory().size(); i++) {
				if (character.getInventory().get(i).equals(item)) {
					JLabel _unequip = new JLabel("Unequip from " + character.getName());
					_unequip.setSize(labelWidth, labelHeight);
					_unequip.setVisible(true);
					_itemOptionsBox.add(_unequip);
					break;
				}
				if (i==character.getInventory().size()) {
					JLabel _equip = new JLabel("Equip to " + character.getName());
					_equip.setSize(labelWidth, labelHeight);
					_equip.setPreferredSize(new Dimension(labelWidth, labelHeight));
					_equip.setVisible(true);
					_itemOptionsBox.add(_equip);
				}
			}
		}
		else {
			JButton _use = new JButton("Use on " + character.getName());
			_use.setSize(labelWidth, labelHeight);
			_use.setVisible(true);
			_itemOptionsBox.add(_use);
		}
		if (_dt.getParty().size() > 0) {
			for (int i=0; i<_dt.getParty().size()-1; i++) {
				if (!_dt.getParty().get(i).equals(_labelToCharacter.get(label))) {
					if (_dt.getParty().get(i).getInventory().size() != _dt.getParty().get(i).getCapacity()) {
						JButton _giveToChar = new JButton("Give to " + _dt.getParty().get(i).getName());
						_giveToChar.setSize(labelWidth, labelHeight);
						_giveToChar.setVisible(true);
						_itemOptionsBox.add(_giveToChar);
					}
				}
			}
		}
		JButton _drop = new JButton("Drop item");
		_drop.setSize(labelWidth, labelHeight);
		_drop.setVisible(true);
		_itemOptionsBox.add(_drop);
		
		boolean canPutRight;
		boolean canPutDown;
		label.setVisible(true);
		if (label.getLocationOnScreen().getX()+labelWidth < 800) {
			canPutRight = true;
		}
		else {
			canPutRight = false;
		}
		if (label.getLocationOnScreen().getY()+(labelHeight*_numOptions) < 700) {
			canPutDown = true;
		}
		else {
			canPutDown = false;
		}
		
		_itemOptBoxX = (int) label.getLocationOnScreen().getX();
		_itemOptBoxY = (int) label.getLocationOnScreen().getY();
		if (!canPutRight) {
			_itemOptBoxX = _itemOptBoxX-labelWidth;
		}
		if (!canPutDown) {
			_itemOptBoxY = _itemOptBoxY-labelHeight;
		}
//		System.out.println("X Pos: " + _itemOptBoxX + "; Y Pos: " + _itemOptBoxY);
		_showingItemOptions = true;
		_itemOptionsBox.setLocation(_itemOptBoxX, _itemOptBoxY);
		_itemOptionsBox.setSize(labelWidth, labelHeight*_numOptions);
		_itemOptionsBox.setVisible(true);
		_itemOptionsBox.setLayout(new GridLayout(4, 0));
//		_dt.getContentPane().add(_itemOptionsBox, 0);
//		this.add(_itemOptionsBox, 0);
		this.add(_itemOptionsBox, 1);
//		this.revalidate();
		_itemOptionsBox.repaint();
	}
	
	public void switchToGameScreen() {
		_dt.changeScreens(_dt.getGameScreen());
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
			constraint.weightx = 1.0;
			row1.add(inventory, constraint);
			
			for (int i=0; i<chrter.getInventory().size(); i++) {
				JLabel item = new JLabel(new ImageIcon(chrter.getInventory().get(i).getImage()));
//				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				item.addMouseMotionListener(_itemListener);
				item.setSize(75,75);
				item.setVisible(true);
				constraint.fill = GridBagConstraints.BOTH;
				constraint.insets = insetItems;
				constraint.gridwidth = 1;
				constraint.gridx = i;
				constraint.gridy = 1;
				row1.add(item, constraint);
				item.addMouseListener(_itemListener);
				_labelToCharacter.put(item, chrter);
				_labelToItem.put(item, chrter.getInventory().get(i));
			}
						
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
			constraint.weightx = 1.0;
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
	
	private class itemListener implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			GameMenuScreen.this.checkItemClicked(e.getPoint());
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
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (_currClicked == 1) {
//				if (!_showingItemOptions) {
				Item clickedItem = GameMenuScreen.this.checkItemContains(e.getPoint());
//				}
			}
		}
	}
	
	private class unitsBoxListener implements MouseMotionListener, MouseListener {

		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseMoved(MouseEvent e) {
			if (!_showingItemOptions) {
				GameMenuScreen.this.checkStopHovering(e.getPoint());
			}
		}

		public void mouseClicked(MouseEvent e) {
			if (_showingItemOptions) {
				GameMenuScreen.this.checkStopShowingOptionsBox(e.getPoint());
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
	}
}