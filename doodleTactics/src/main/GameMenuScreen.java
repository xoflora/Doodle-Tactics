package main;

import items.Item;
import items.ItemException;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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
	private JPanel _unitsBox;
	private JPanel _itemInfoBox;
	private HashMap<JLabel, Character> _labelToCharacter; //stores who each item belongs to
	private HashMap<JLabel, Item> _labelToItem; //stores each item image to the actual item to holds
	private JScrollPane _scrollBar;
	private boolean _showingItemOptions;
	private unitsBoxListener _unitsBoxListener;
	private int _itemOptBoxX, _itemOptBoxY, _numOptions;
	private Item _selectedItem;
	private Character _selectedChar;
	private HashMap<JButton, Character> _buttonToChar;
	private ArrayList<optionButton> _buttonList;
	private BufferedImage _charBoxImage, _infoBoxImage, _infoBoxTitle;
	
	public GameMenuScreen(DoodleTactics dt) {
		
		super(dt);
		this.setBackground(java.awt.Color.DARK_GRAY);
		this.setVisible(true);
		_dt = dt;
		_labelToCharacter = new HashMap<JLabel, Character>();
		_labelToItem = new HashMap<JLabel, Item>();
		System.out.println("set to false");
		_showingItemOptions = false;
		_buttonList = new ArrayList<optionButton>();
		_buttonToChar = new HashMap<JButton, Character>();
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
//			BufferedImage infoBoxTitle = ImageIO.read();
			_charBoxImage = ImageIO.read(new File("src/graphics/menu/units_box.png"));
			_units = new MenuItem(this, unitsD,unitsH, dt);
			_map = new MenuItem(this, mapD,mapH,dt);
			_save = new MenuItem(this, saveD, saveH,dt);
			_options = new MenuItem(this, optionsD,optionsH,dt);
			_quit = new MenuItem(this, quitD, quitH,dt);
			_title = new MenuItem(this, titleD, titleD, dt);
//			_infoBoxTitle = new MenuItem(this, dt);
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
		_unitsBox.setBackground(java.awt.Color.DARK_GRAY);
		
		_unitsBox.setSize(750,660);
		_unitsBox.setLocation(1,0);
		
		_itemInfoBox = new JPanel();
		_itemInfoBox.setVisible(true);
		_itemInfoBox.setBackground(java.awt.Color.DARK_GRAY);
		
		_scrollBar = new JScrollPane(_unitsBox, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		_scrollBar.setBackground(java.awt.Color.DARK_GRAY);
		Dimension d = new Dimension(752, 662);
		_scrollBar.setSize(d);
		_scrollBar.setLocation(new Point(200, 120));
		_scrollBar.setOpaque(false);
		
		_numOptions = 0;
		_itemOptBoxX = 0;
		_itemOptBoxY = 0;
		
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
		_charInfoList = new LinkedList<CharInfo>();
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
		if (_showingItemOptions) {
//			System.out.println("X: " + _itemOptBoxX + "; Y: " + _itemOptBoxY);
			for (int i=0; i<_buttonList.size(); i++) {
//				System.out.println("button x: " + _buttonList.get(i).getX() + "; y: " + _buttonList.get(i).getY());
				_buttonList.get(i).repaint();
			}
		}
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
			_charInfoList = new LinkedList<CharInfo>();
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
			this.add(_scrollBar);
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
		this.grabFocus();
		if (_showingItemOptions) {
			/**
			 * point.getX() and point.getY() get the X and Y coordinates only relative to the units box, 
			 * so these values get the location of the units box added to them before checking
			 */
			if (point.getX()+200 < _itemOptBoxX || point.getX()+200 > _itemOptBoxX+200 || point.getY()+120 < _itemOptBoxY+_scrollBar.getVerticalScrollBar().getValue() || point.getY()+120 > _itemOptBoxY+200+_scrollBar.getVerticalScrollBar().getValue()) {
				for (int i=0; i<_buttonList.size(); i++) {
					this.remove(_buttonList.get(i));
				}
				System.out.println("set to false");
				_showingItemOptions = false;
				this.repaint();
			}
			else {
				Point np = new Point();
				np.setLocation(point.getX()+200, point.getY()+120);
				for (int i=0; i<_buttonList.size(); i++) {
//					System.out.println("--------------");
//					System.out.println("X: " + np.getX() + "; Y: " + np.getY());
//					System.out.println("HERE X: " + _buttonList.get(i).getX() + "; Y: " + _buttonList.get(i).getY());
//					System.out.println(_buttonList.get(i).getSize());
					if (np.getX() > _buttonList.get(i).getX() && np.getY() > _buttonList.get(i).getY()+_scrollBar.getVerticalScrollBar().getValue() && np.getX() < _buttonList.get(i).getX()+200 && np.getY() < _buttonList.get(i).getY()+15+_scrollBar.getVerticalScrollBar().getValue()) {
//						System.out.println("HERE X: " + _buttonList.get(i).getX() + "; Y: " + _buttonList.get(i).getY());
						_buttonList.get(i).activate(_buttonList.get(i));
						for (int j=0; j<_buttonList.size(); j++) {
							this.remove(_buttonList.get(j));
						}
						_showingItemOptions = false;
						this.repaint();
					}
				}
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
		this.add(_itemInfoBox);
	}
	
	public void checkItemClicked(java.awt.Point point, JLabel label) {
		System.out.println("point x: " + point.getX() + "; y: " + point.getY());
		if (label.contains(point)) {
			ImageIcon img;
			try {
				img = new ImageIcon(ImageIO.read(new File("src/graphics/characters/thief_front.png")));
				label.setIcon(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
			_showingItemOptions = true;
			this.displayItemOptions(label);
			return;
		}
		/**
		 * Might not ever get here...
		 */
		System.out.println("should not get here in checkItemClicked. set to false");
		_showingItemOptions = false;
		for (int i=0; i<_buttonList.size(); i++) {
			this.remove(_buttonList.get(i));
		}
		repaint();
	}
	
	public void displayItemOptions(JLabel label) {
				
		/**
		 * number of options is set by the #of people in the party -1, plus 3 options for each item
		 */
		
		System.out.println("whee");
		
		Item item = _labelToItem.get(label);
		Character character = _labelToCharacter.get(label);
		
		int labelWidth = 200;
		int labelHeight = 15;
//		label.revalidate();
		for (int i=0; i<_buttonList.size(); i++) {
			this.remove(_buttonList.get(i));
		}
		
		_buttonList = new ArrayList<optionButton>();
		_buttonToChar = new HashMap<JButton, Character>();
		
		System.out.println("Label Location on Screen X: " + label.getLocationOnScreen().getX() + "; Y: " + label.getLocationOnScreen().getY());
		
		_itemOptBoxX = (int) label.getLocationOnScreen().getX();
		_itemOptBoxY = (int) label.getLocationOnScreen().getY();
				
		int numOptionsInserted = 1;
		
		_selectedItem = _labelToItem.get(label);
		_selectedChar = character;
		
		_numOptions = _dt.getParty().size()+2;
		
		if (_labelToItem.get(label).isEquip()) {
			for (int i=0; i<character.getInventory().size(); i++) {
				if (character.getInventory().get(i).equals(item)) {
					unequipButton _unequip = new unequipButton("Unequip from " + character.getName());
					
					_unequip.setSize(labelWidth, labelHeight);
					_unequip.setVisible(true);
					_unequip.setLocation(_itemOptBoxX, _itemOptBoxY);
					_buttonList.add(_unequip);
					break;
				}
				if (i==character.getInventory().size()) {
					equipButton _equip = new equipButton("Equip to " + character.getName());
					_equip.setSize(labelWidth, labelHeight);
					_equip.setPreferredSize(new Dimension(labelWidth, labelHeight));
					_equip.setVisible(true);
					_equip.setLocation(_itemOptBoxX, _itemOptBoxY);
					_buttonList.add(_equip);
				}
			}
		}
		else {
			useButton _use = new useButton("Use on " + character.getName());
			_use.setSize(labelWidth, labelHeight);
			_use.setVisible(true);
			_use.setLocation(_itemOptBoxX, _itemOptBoxY);
			_buttonToChar.put(_use, character);
			_buttonList.add(_use);
		}
		if (_dt.getParty().size() > 0) {
			for (int i=0; i<_dt.getParty().size(); i++) {
				if (!_dt.getParty().get(i).equals(_labelToCharacter.get(label))) {
					if (_dt.getParty().get(i).getInventory().size() != _dt.getParty().get(i).getCapacity()) {
						giveToCharButton _giveToChar = new giveToCharButton("Give to " + _dt.getParty().get(i).getName());
						_giveToChar.setSize(labelWidth, labelHeight);
						_giveToChar.setVisible(true);
						_giveToChar.setLocation(_itemOptBoxX, _itemOptBoxY+(numOptionsInserted*labelHeight));
						numOptionsInserted+=1;
						_buttonToChar.put(_giveToChar, _dt.getParty().get(i));
						_buttonList.add(_giveToChar);
					}
				}
			}
		}
		dropButton _drop = new dropButton("Drop item");
		_drop.setSize(labelWidth, labelHeight);
		_drop.setVisible(true);
		_drop.setLocation(_itemOptBoxX, _itemOptBoxY+(numOptionsInserted*labelHeight));
		_buttonToChar.put(_drop, character);
		_buttonList.add(_drop);
		
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
		
		if (!canPutRight) {
			_itemOptBoxX = _itemOptBoxX-labelWidth;
		}
		if (!canPutDown) {
			_itemOptBoxY = _itemOptBoxY-labelHeight;
		}
		
		for (int i=0; i<_buttonList.size(); i++) {
			_buttonList.get(i).setVisible(true);
			this.add(_buttonList.get(i));
		}
		
//		System.out.println("X Pos: " + _itemOptBoxX + "; Y Pos: " + _itemOptBoxY);
		_showingItemOptions = true;
//		this.add(_itemOptionsBox);
//		_itemOptionsBox.repaint();
		this.repaint();
		_unitsBox.grabFocus();
//		this.grabFocus();
//		_buttonList.get(3).grabFocus();
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
//			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
						
			this.setOpaque(false);
			
			JLabel profile = new JLabel(new ImageIcon(chrter.getProfileImage()));
			profile.setSize(150, 150);
			profile.setPreferredSize(new Dimension(150,150));
			profile.setMaximumSize(new Dimension(150, 150));
//			profile.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			profile.setVisible(true);
			
			Insets inset = new Insets(5, 5, 5, 5);
			
			JLabel name = new JLabel(chrter.getName());
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
			System.out.println(chrter.getName() + " inventory size: " + chrter.getInventory().size());
			int count = 0;
			for (Item item: chrter.getInventory().values()) {
				JLabel itemPic = new JLabel(new ImageIcon(item.getImage()));
//				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				itemListener listener = new itemListener(itemPic);
				itemPic.addMouseMotionListener(listener);
				itemPic.setSize(75,75);
				itemPic.setVisible(true);
				constraint.fill = GridBagConstraints.BOTH;
				constraint.insets = insetItems;
				constraint.gridwidth = 1;
				constraint.gridx = count;
				constraint.gridy = 1;
				row1.add(itemPic, constraint);
				itemPic.addMouseListener(listener);
				_labelToCharacter.put(itemPic, chrter);
				_labelToItem.put(itemPic, item);
				count+=1;
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
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Image bg = (new ImageIcon(_charBoxImage)).getImage();
			g.drawImage(bg, (this.getWidth()/2) - (bg.getWidth(this) / 2),(this.getHeight()/2) - (bg.getHeight(this) / 2),bg.getWidth(this),bg.getHeight(this),this);
		}
	}
	
	private class itemInfoBox extends JPanel {
		public itemInfoBox(Item myItem) {
			this.setLayout(new GridBagLayout());
			GridBagConstraints constraint = new GridBagConstraints();
			java.awt.Dimension panelSize = new java.awt.Dimension(730,200);
			this.setPreferredSize(panelSize);
			this.setSize(730,200);
			this.setLocation(15, 15);
//			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
						
			this.setOpaque(false);
			
//			JLabel profile = new JLabel(new ImageIcon(chrter.getProfileImage()));
//			profile.setSize(150, 150);
//			profile.setPreferredSize(new Dimension(150,150));
//			profile.setMaximumSize(new Dimension(150, 150));
//			profile.setVisible(true);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Image box = (new ImageIcon(_charBoxImage)).getImage();
//			g.drawImage(bg, (this.getWidth()/2) - (bg.getWidth(this) / 2),(this.getHeight()/2) - (bg.getHeight(this) / 2),bg.getWidth(this),bg.getHeight(this),this);
		}
	}
	
	private class itemListener implements MouseListener, MouseMotionListener {

		JLabel _itemPic;
		
		public itemListener(JLabel itemPic) {
			_itemPic = itemPic;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("clicked in itemListener");
			GameMenuScreen.this.checkItemClicked(e.getPoint(), _itemPic);
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
	
	private abstract class optionButton extends JButton {
		
		public optionButton(String string) {
			super(string);
		}
		public abstract void activate(optionButton button);
	}
	
	
	private class giveToCharButton extends optionButton {
		
		public giveToCharButton(String string) {
			super(string);
		}
		
		public void activate(optionButton button) {
			Character myChar = _buttonToChar.get(button);
			try {
				if (myChar.getInventory().size() != myChar.getCapacity()) {
					myChar.addToInventory(_selectedItem);
					_selectedChar.removeFromInventory(_selectedItem);
					for (int i=0; i<_buttonList.size(); i++) {
						this.remove(_buttonList.get(i));
					}
					GameMenuScreen.this.setDefaultTabToUnits();
					System.out.println("set to false");
					_showingItemOptions = false;
					this.repaint();
				}
			} catch (ItemException e1) {
				System.out.println("Something bad happened in the Game Menu Screen");
			}
		}
	}
	
	private class dropButton extends optionButton {

		public dropButton(String string) {
			super(string);
		}

		public void activate(optionButton button) {
			try {
				_selectedChar.removeFromInventory(_selectedItem);
				for (int i=0; i<_buttonList.size(); i++) {
					this.remove(_buttonList.get(i));
					_buttonList.get(i).setVisible(false);
				}
				GameMenuScreen.this.setDefaultTabToUnits();
				System.out.println("set to false");
				_showingItemOptions = false;
				this.repaint();
			} catch (ItemException e1) {
				System.out.println("Something bad happened in the Game Menu Screen");
			}
		}
	}
	
	private class unequipButton extends optionButton {

		public unequipButton(String string) {
			super(string);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void activate(optionButton button) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class equipButton extends optionButton {

		public equipButton(String string) {
			super(string);
			// TODO Auto-generated constructor stub
		}

		public void activate(optionButton button) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class useButton extends optionButton {

		public useButton(String string) {
			super(string);
			// TODO Auto-generated constructor stub
		}

		public void activate(optionButton button) {
			_selectedItem.exert(_selectedChar);
			for (int i=0; i<_buttonList.size(); i++) {
				this.remove(_buttonList.get(i));
				_buttonList.get(i).setVisible(false);
			}
			GameMenuScreen.this.setDefaultTabToUnits();
			System.out.println("set to false");
			_showingItemOptions = false;
			this.repaint();
		}
	}
}