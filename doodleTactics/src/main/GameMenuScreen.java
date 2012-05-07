package main;

import items.Cuirass;
import items.Equipment;
import items.Footgear;
import items.Item;
import items.ItemException;
import items.Shield;
import items.Weapon;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


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
	private MenuItem _infoBoxTitle;
	private MenuItem _staticMap;
	private MenuItem _downArrow;
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
	private HashMap<optionButton, Character> _buttonToChar;
	private ArrayList<optionButton> _buttonList;
	private BufferedImage _charBoxImage, _listItem, _listItemHovered;

	//options vars
	private String[] _optionsText = {"Overworld Move Left","Overworld Move Right", "Overworld Move Up","Overworld Move Down", "Interact"};
	private int _currOption;
	private int[] _keyCodes;
	private String[] _keys = {"A","D","W","S","Space"};
	private final static int NUM_OPTIONS = 5;

	//Save game
	private BufferedImage _saveBg;
	private SaveMenuItem _saveMenuItem;
	private JTextField _typeText;

	private JLayeredPane _layers;
	private buttonPanel _buttons;

	private int SCROLLBOX_X, SCROLLBOX_Y;

	public GameMenuScreen(DoodleTactics dt) {
		super(dt);
		this.setBackground(java.awt.Color.DARK_GRAY);
		this.setVisible(true);
		this.setLayout(null);
		_dt = dt;
		_labelToCharacter = new HashMap<JLabel, Character>();
		_labelToItem = new HashMap<JLabel, Item>();
		//		System.out.println("set to false");
		_showingItemOptions = false;
		_buttonList = new ArrayList<optionButton>();
		_buttonToChar = new HashMap<optionButton, Character>();
		BufferedImage unitsD = _dt.importImage("src/graphics/menu/units.png");
		BufferedImage unitsH = _dt.importImage("src/graphics/menu/units_hovered.png");
		BufferedImage mapD = _dt.importImage("src/graphics/menu/map.png");
		BufferedImage mapH = _dt.importImage("src/graphics/menu/map_hovered.png");
		BufferedImage saveD = _dt.importImage("src/graphics/menu/save.png");
		BufferedImage saveH = _dt.importImage("src/graphics/menu/save_hovered.png");
		BufferedImage optionsD = _dt.importImage("src/graphics/menu/options.png");
		BufferedImage optionsH = _dt.importImage("src/graphics/menu/options_hovered.png");
		BufferedImage quitD = _dt.importImage("src/graphics/menu/quit_game_menu.png");
		BufferedImage quitH = _dt.importImage("src/graphics/menu/quit_game_menu_hovered.png");
		BufferedImage titleD = _dt.importImage("src/graphics/menu/overlay.png");
		BufferedImage infoBoxTitle = _dt.importImage("src/graphics/menu/item_info_label.png");
		BufferedImage staticMapImg = _dt.importImage("src/graphics/menu/static_map.png");
		BufferedImage downArrow = _dt.importImage("src/graphics/menu/downarrow.gif");

		_charBoxImage = _dt.importImage("src/graphics/menu/units_box.png");
		_listItem = _dt.importImage("src/graphics/menu/game_menu_option_hovered.png");
		_listItemHovered = _dt.importImage("src/graphics/menu/game_menu_option.png");

		_units = new MenuItem(this, unitsD,unitsH, dt);
		_map = new MenuItem(this, mapD,mapH,dt);
		_save = new MenuItem(this, saveD, saveH,dt);
		_options = new MenuItem(this, optionsD,optionsH,dt);
		_quit = new MenuItem(this, quitD, quitH,dt);
		_title = new MenuItem(this, titleD, titleD, dt);
		_infoBoxTitle = new MenuItem(this, infoBoxTitle, infoBoxTitle, dt);
		_staticMap = new MenuItem(this, staticMapImg, staticMapImg, dt);
		_downArrow = new MenuItem(this, downArrow, downArrow, dt);

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
		_staticMap.setLocation(200, 120);

		_units.setVisible(true);
		_map.setVisible(true);
		_save.setVisible(true);
		_options.setVisible(true);
		_quit.setVisible(true);
		_title.setVisible(true);

		_charInfoList = new LinkedList<CharInfo>();

		_unitsBox = new JPanel();
		_unitsBox.setBackground(java.awt.Color.DARK_GRAY);
		_unitsBox.setLocation(0,0);
		//		_unitsBox.setSize(760, 1000);
		//		_unitsBox.setOpaque(false);
		//		_unitsBox.setBounds(0,0,760,1000);

		_layers = new JLayeredPane();
		_layers.setBackground(java.awt.Color.DARK_GRAY);
		_layers.setLayout(null);
		//		_layers.setOpaque(false);
		//		_layers.setBounds(0, 0, 0, 0);
		_layers.add(_unitsBox, new Integer(0), 0);

		_buttons = new buttonPanel(new ArrayList<optionButton>());
		_buttons.setVisible(true);
		_buttons.setBackground(java.awt.Color.MAGENTA);
		_buttons.setVisible(true);
		_layers.add(_buttons, new Integer(1), 0);
		_layers.setVisible(true);

		_scrollBar = new JScrollPane(_layers, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		_scrollBar.setBackground(java.awt.Color.DARK_GRAY);
		Dimension d = new Dimension(760, 660);
		_scrollBar.setSize(d);
		_scrollBar.setPreferredSize(d);
		_scrollBar.setMaximumSize(d);
		_scrollBar.setMinimumSize(d);
		SCROLLBOX_X = 190;
		SCROLLBOX_Y = 120;
		_scrollBar.setLocation(new Point(SCROLLBOX_X, SCROLLBOX_Y));
		_scrollBar.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
		_scrollBar.setOpaque(false);
		_numOptions = 0;
		_itemOptBoxX = 0;
		_itemOptBoxY = 0;

		_itemInfoBox = new JPanel();
		_itemInfoBox.setVisible(true);
		_itemInfoBox.setOpaque(false);
		_itemInfoBox.setLayout(new BorderLayout());
		_itemInfoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		_itemInfoBox.setLocation(15, 460);
		_itemInfoBox.setSize(144, 340);

		_unitsBoxListener = new unitsBoxListener();

		//options
		_currOption = 0;
		//_optionsText = new String[NUM_OPTIONS];

		_keyCodes  = new int[NUM_OPTIONS];
		_keyCodes[0] = dt.getLeftKey();
		_keyCodes[1] = _dt.getRightKey();
		_keyCodes[2] = _dt.getUpKey();
		_keyCodes[3] = _dt.getDownKey();
		_keyCodes[4] = _dt.getInteractKey();

		//save
		_saveBg = _dt.importImage("src/graphics/menu/save_menu.png");
		BufferedImage hoveredSaveImg = _dt.importImage("src/graphics/menu/accept_hovered.png");
		BufferedImage saveImg = _dt.importImage("src/graphics/menu/accept.png");
		Font myFont = new Font("Arial", Font.BOLD, 28);
		_typeText  = new JTextField();
		_typeText.setFont(myFont);
		_typeText.setVisible(true);
		_typeText.setEditable(true);
		_typeText.setOpaque(false);
		_typeText.setSize(400, 50);
		_typeText.setBorder(null);
		_typeText.setCaretColor(java.awt.Color.CYAN);
		_typeText.getCaret().setBlinkRate(400);
		_typeText.setLocation(420,420);
		_typeText.setFocusable(true);
//		_typeText.grabFocus();
		_typeText.setDocument(new MaxLengthDoc());


		_saveMenuItem = new SaveMenuItem(saveImg,hoveredSaveImg,_dt);
	}

	public class SaveMenuItem extends MenuItem{

		public SaveMenuItem(BufferedImage defltPath, BufferedImage hoveredPath, DoodleTactics dt) {
			super(dt.getGameScreen(), defltPath,hoveredPath,dt);
			this.setLocation(440,550);
			this.setSize(defltPath.getWidth(), defltPath.getHeight());
		}
		@Override
		public void paint(Graphics2D brush){
			brush.drawImage(_saveBg, null,187,119);
			brush.drawImage(_current,null,440,550);
		}
		
		public boolean containsText(){
			return !_typeText.getText().equals("");
		}
	}

	private class MaxLengthDoc extends PlainDocument {

		public void insertString(int offset, String text, AttributeSet attributes) {
			if (text != null && !text.equals(" ") && this.getLength() + text.length() <= 15) {
				try {
					super.insertString(offset, text, attributes);
				} catch (BadLocationException e) {
					_dt.error("Error when typing name into the text box");
				}
			}
		}
	}


	/**
	 * Initialized the screen to be set to the units tab
	 */

	public void setDefaultTabToUnits() {
		_units.setHovered();
		_currClicked = 1;
		_unitsBox.removeAll();
		_unitsBox.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		int counter = 0;
		//		_dt.getParty().size(), 0, 10, 10)
		_unitsBox.addMouseMotionListener(_unitsBoxListener);
		_unitsBox.addMouseListener(_unitsBoxListener);
		_labelToCharacter = new HashMap<JLabel, Character>();
		_labelToItem = new HashMap<JLabel, Item>();
		_charInfoList = new LinkedList<CharInfo>();
		_infoBoxTitle.setLocation(12, 460);
		_infoBoxTitle.setVisible(true);
		for (Character chrter: _dt.getParty()) {
			CharInfo toAdd = new CharInfo(this, chrter);
			toAdd.setVisible(true);
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(0,0,10,10);
			c.gridx = 0;
			c.gridy = counter;
			_unitsBox.add(toAdd, c);
			_charInfoList.add(toAdd);
			counter+=1;
		}
		_unitsBox.setBounds(0,0,760,counter*(200+10));
		//		_unitsBox.setPreferredSize(new Dimension(760, counter*(200+10)-10));
		_layers.setPreferredSize(new Dimension(760, counter*(200+10)-10));
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
		this.grabFocus();
		if (_currClicked == 1) {
			_itemInfoBox.setLocation(15, 570);
			_itemInfoBox.setSize(144, 230);
			_itemInfoBox.revalidate();
			_scrollBar.setLocation(new Point(SCROLLBOX_X, SCROLLBOX_Y));
		}
		_title.paint((Graphics2D) g, _title.getImage());
		_units.paint((Graphics2D) g, _units.getImage());
		_save.paint((Graphics2D) g, _save.getImage());
		_quit.paint((Graphics2D) g, _quit.getImage());
		_options.paint((Graphics2D) g, _options.getImage());
		_map.paint((Graphics2D) g, _map.getImage());
		_staticMap.paint((Graphics2D) g, _staticMap.getImage());
		_downArrow.setLocation(_dt.getGameScreen().getMap().getMapCords().getX(), _dt.getGameScreen().getMap().getMapCords().getY());
		_downArrow.paint((Graphics2D) g, _downArrow.getImage());
		_infoBoxTitle.paint((Graphics2D) g, _infoBoxTitle.getImage());

		//Save!
		if(_currClicked == 5){
			_typeText.grabFocus();
			_saveMenuItem.setVisible(true);
			_saveMenuItem.paint((Graphics2D) g);
		}


		if(_currClicked == 4){
			//Options
			this.grabFocus();
			int y = 1;
			((Graphics2D) g).setRenderingHint(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
			((Graphics2D) g).setFont(new Font("M",Font.BOLD,30));
			((Graphics2D) g).setColor(new Color(64,224,208));
			((Graphics2D) g).drawString("Change Controls (Up/Down to Move)",200,150);
			((Graphics2D) g).setFont(new Font("M",Font.BOLD,20));
			g.drawLine(190, 160, 780, 160);
			//draw other strings
			((Graphics2D) g).setColor(new Color(1,1,1));
			y = 2;
			for(int i=0; i< NUM_OPTIONS; i++){
				y++;
				if(i == _currOption){
					((Graphics2D) g).setColor(new Color(64,224,208));
					((Graphics2D) g).drawString(_optionsText[i] + ": " + _keys[i] + " " + "(Enter to Change)",200,y*50 + 50);
					((Graphics2D) g).setColor(new Color(1,1,1));
				} else{
					((Graphics2D) g).drawString(_optionsText[i] + ": " + _keys[i],200,y*50 + 50);
				}
			}


			((Graphics2D) g).setFont(new Font("M",Font.BOLD,30));
			((Graphics2D) g).setColor(new Color(64,224,208));
			((Graphics2D) g).drawString("Autosaving",200,500);
			g.drawLine(190, 515, 385, 515);



		}

	}

	/**
	 * sets all the menu items (left bar) back to their default images
	 */

	public void setDefault() {
		_units.setDefault();
		_map.setDefault();
		_quit.setDefault();
		_options.setDefault();
		_save.setDefault();
		_infoBoxTitle.setVisible(false);
	}

	public MenuItem checkContains(java.awt.Point point) {

		/* set all of the buttons to default */

		/* check if the point is in any of the buttons */
		MenuItem clicked = null;

		System.out.println("curr clicked: " + _currClicked);

		if(_units.contains(point)) {
			_staticMap.setVisible(false);
			_downArrow.setVisible(false);
			this.removeAll();
			this.setDefault();
			_units.setHovered();
			clicked = _units;
			_currClicked = 1;
			_unitsBox.removeAll();
			_unitsBox.addMouseMotionListener(_unitsBoxListener);
			_unitsBox.addMouseListener(_unitsBoxListener);
			_infoBoxTitle.setLocation(12, 460);
			_infoBoxTitle.setVisible(true);
			_unitsBox.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			_labelToCharacter = new HashMap<JLabel, Character>();
			_labelToItem = new HashMap<JLabel, Item>();
			_charInfoList = new LinkedList<CharInfo>();
			int counter = 0;
			for (Character chrter: _dt.getParty()) {
				CharInfo toAdd = new CharInfo(this, chrter);
				toAdd.setVisible(true);
				c.fill = GridBagConstraints.BOTH;
				c.insets = new Insets(0,0,10,10);
				c.gridx = 0;
				c.gridy = counter;
				_unitsBox.add(toAdd, c);
				_charInfoList.add(toAdd);
				counter+=1;
			}
			_unitsBox.revalidate();
			_layers.revalidate();
			this.add(_scrollBar);
			_scrollBar.revalidate();
			this.revalidate();
		}

		if(_map.contains(point)) {
			_staticMap.setVisible(false);
			_downArrow.setVisible(false);
			this.removeAll();
			this.setDefault();
			_map.setHovered();
			clicked = _map;
			_staticMap.setVisible(true);
			_downArrow.setVisible(true);
			_currClicked = 2;
		}

		if(_quit.contains(point)) {
			System.exit(0);
		}

		if(_options.contains(point)) {
			_staticMap.setVisible(false);
			_downArrow.setVisible(false);
			this.removeAll();
			this.setDefault();
			_options.setHovered();
			clicked = _options;
			_currClicked = 4;
		}

		if(_save.contains(point)) {
			_staticMap.setVisible(false);
			_downArrow.setVisible(false);
			this.removeAll();
			this.setDefault();
			_save.setHovered();
			_typeText.setVisible(true);
			this.add(_typeText);

			//_dt.getGameScreen().saveGame("src/tests/data/testSave");	
			clicked = _save;
			_currClicked = 5;
		}

		if(_saveMenuItem.contains(point) && _saveMenuItem.containsText()){
			String filepath = _typeText.getText();
			_dt.getGameScreen().saveGame(filepath);	
		}

		this.repaint();
		return clicked;
	}

	public void checkItemContains(java.awt.Point point, JLabel _label) {
		_itemInfoBox.removeAll();
		this.remove(_itemInfoBox);
		_itemInfoBox.setVisible(false);
		//			System.out.println("Point X: " + point.getX() + "; Y: " + point.getY());
		//			System.out.println("Label X: " + label.getLocationOnScreen().getX() + "; Y: " + label.getLocationOnScreen().getY());

		if (_label.contains(point)) {
			this.showItemInfo(_labelToItem.get(_label));
			_itemInfoBox.revalidate();
			_itemInfoBox.repaint();
		}
		this.repaint();
	}

	public void checkStopHovering(MouseEvent e) {
		Point point = e.getPoint();
		for (JLabel label: _labelToCharacter.keySet()) {
			if (label.contains(point)) {
				return;
			}
		}
		this.remove(_itemInfoBox);
		this.repaint();
	}

	public void checkStopShowingOptionsBox(java.awt.Point point) {
		this.grabFocus();
		//		System.out.println("check stop showing point x: " + point.getX() + "; y: " + point.getY());
		//		System.out.println("_buttons location on screen x: " + _buttons.getLocationOnScreen().getX() + "; y: " + _buttons.getLocationOnScreen().getY());
		if (_showingItemOptions) {
			/**
			 * point.getX() and point.getY() get the X and Y coordinates only relative to the units box, 
			 * so these values get the location of the units box added to them before checking
			 */

			//			System.out.println("_buttons width: " + _buttons.getWidth() + "height: " + _buttons.getHeight());


			if (_buttons.getLocationOnScreen().getX() < point.getX() && _buttons.getLocationOnScreen().getY() < point.getY() && _buttons.getLocationOnScreen().getX()+_buttons.getWidth()>point.getX() && (_buttons.getLocationOnScreen().getY()+_buttons.getHeight())>point.getY()) {

				int buttonNum = (int)((point.getY()-_buttons.getLocationOnScreen().getY())/27);
				//					System.out.println("button y: " + button.getY());
				_buttonList.get(buttonNum).activate(_buttonList.get(buttonNum));
				_buttons.removeAll();
				_layers.remove(_buttons);
				_showingItemOptions = false;
				this.repaint();
			}
			else {
				_buttons.removeAll();
				_layers.remove(_buttons);
				_showingItemOptions = false;
				this.repaint();
			}
			//			else {
			//				Point np = new Point();
			//				np.setLocation(point.getX()+200, point.getY()+120);
			////				System.out.println();
			//				for (int i=0; i<_buttonList.size(); i++) {
			////					System.out.println("--------------");
			////					System.out.println("X: " + np.getX() + "; Y: " + np.getY());
			////					System.out.println("HERE X: " + _buttonList.get(i).getX() + "; Y: " + _buttonList.get(i).getY());
			////					System.out.println(_buttonList.get(i).getSize());
			//					if (np.getX() > _buttonList.get(i).getX() && np.getY() > _buttonList.get(i).getY()+_scrollBar.getVerticalScrollBar().getValue() && np.getX() < _buttonList.get(i).getX()+200 && np.getY() < _buttonList.get(i).getY()+15+_scrollBar.getVerticalScrollBar().getValue()) {
			//						System.out.println("HERE X: " + _buttonList.get(i).getX() + "; Y: " + _buttonList.get(i).getY());
			//						_buttonList.get(i).activate(_buttonList.get(i));
			//						for (int j=0; j<_buttonList.size(); j++) {
			//							_buttonList.get(i).setVisible(false);
			//						}
			//						_showingItemOptions = false;
			//						this.repaint();
			//						break;
			//					}
			//				}
			//			}
		}
	}

	public void checkOptionHovered(MouseEvent e) {
		Point point = e.getLocationOnScreen();
		if (_buttons.getLocationOnScreen().getX() < point.getX() && _buttons.getLocationOnScreen().getY() < point.getY() && _buttons.getLocationOnScreen().getX()+_buttons.getWidth()>point.getX() && (_buttons.getLocationOnScreen().getY()+_buttons.getHeight())>point.getY()) {
			int buttonNum = (int)((point.getY()-_buttons.getLocationOnScreen().getY())/27);
			//				System.out.println("button y: " + button.getY());
			//			System.out.println("whee");
			for (int i=0; i<_buttonList.size(); i++) {
				if (i==buttonNum) {
					_buttonList.get(buttonNum).setHovered();
				}
				else {
					_buttonList.get(i).setDefault();
				}
			}
		}
		else {
			for (int i=0; i<_buttonList.size(); i++) {
				_buttonList.get(i).setDefault();
			}
		}
		this.repaint();
	}

	/**
	 * Displays the item info at the left side of the screen
	 * @param item- Item that is being hovered
	 */

	public void showItemInfo(Item item) {
		_itemInfoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		_itemInfoBox.removeAll();
		_itemInfoBox.setLocation(15, 460);
		_itemInfoBox.setSize(144, 340);
		_itemInfoBox.setOpaque(false);
		_itemInfoBox.setLayout(new BorderLayout());
		//		GridBagConstraints c = new GridBagConstraints();
		JLabel profile = new JLabel(new ImageIcon(item.getImage()));
		profile.setSize(65, 65);
		//		c.fill = GridBagConstraints.BOTH;
		//		c.weighty = 0.5;
		//		c.gridx = 0;
		//		c.gridy = 0;
		profile.setVisible(true);
		_itemInfoBox.add(profile, BorderLayout.NORTH);
//		if (item.isEquip()) {
//
//		}
//		else {
			JTextArea description = new JTextArea(5, 5);
			description.setOpaque(false);
			description.setFont(new Font("Arial", Font.BOLD, 14));
			description.setForeground(java.awt.Color.BLACK);
			description.setText(item.getDescription());
			description.setSize(130, 250);
			description.setVisible(true);
			description.setLineWrap(true);
			description.setWrapStyleWord(true);
			description.revalidate();
			//			c.fill = GridBagConstraints.BOTH;
			//			c.gridx = 0;
			//			c.gridy = 1;
			_itemInfoBox.add(description, BorderLayout.CENTER);
//		}
		_itemInfoBox.setVisible(true);
		this.add(_itemInfoBox);
	}

	public void checkItemClicked(java.awt.Point point, JLabel label) {
		System.out.println("point x: " + point.getX() + "; y: " + point.getY());
		if (label.contains(point)) {
			//			ImageIcon img;
			//			try {
			//				img = new ImageIcon(ImageIO.read(new File("src/graphics/characters/thief_front.png")));
			//				label.setIcon(img);
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}
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
			_buttonList.get(i).setVisible(false);
		}
		repaint();
	}

	public void displayItemOptions(JLabel label) {

		/**
		 * number of options is set by the #of people in the party -1, plus 3 options for each item
		 */
		Item item = _labelToItem.get(label);
		Character character = _labelToCharacter.get(label);

		_buttons.removeAll();

		int labelWidth = 152;
		int labelHeight = 27;
		for (int i=0; i<_buttonList.size(); i++) {
			_buttonList.get(i).setVisible(false);
		}

		_buttonToChar = new HashMap<optionButton, Character>();
		_buttonList = new ArrayList<optionButton>();

		System.out.println("Label Location on Screen X: " + label.getLocationOnScreen().getX() + "; Y: " + label.getLocationOnScreen().getY());

		_itemOptBoxX = (int) label.getLocationOnScreen().getX();
		_itemOptBoxY = (int) label.getLocationOnScreen().getY();

		int numOptionsInserted = 1;

		_selectedItem = _labelToItem.get(label);
		_selectedChar = character;

		_numOptions = _dt.getParty().size()+2;

		boolean isEquipped = false;
		
		if (_labelToItem.get(label).isEquip()) {
			if (character.getCuirass() != null) {
				if (character.getCuirass().equals(item)) {
					isEquipped = true;
				}
			}
			if (character.getWeapon() != null) {
				if (character.getWeapon().equals(item)) {
					isEquipped = true;
				}
			}
			if (character.getShield() != null) {
				if (character.getShield().equals(item)) {
					isEquipped = true;
				}
			}
			if (character.getFootgear() != null) {
				if (character.getFootgear().equals(item)) {
					isEquipped = true;
				}
			}
			if (isEquipped) {
				if (character.getInventory().size() != character.getCapacity()) {
					unequipButton _unequip = new unequipButton(this, _listItem, _listItemHovered, _dt);
					_unequip.addLabel("Unequip from " + character.getName());
					_unequip.setSize(labelWidth, labelHeight);
					_unequip.setVisible(true);
					_unequip.setLocation(_itemOptBoxX, _itemOptBoxY);
					_buttonToChar.put(_unequip, character);
					_buttonList.add(_unequip);
				}
			}
			else {
				if (_selectedItem.isWeapon()) {
					Weapon _selectedWeapon = (Weapon) _selectedItem;
					if (_selectedWeapon.canBeEquipped(character)) {
						equipButton _equip = new equipButton(this, _listItem, _listItemHovered, _dt);
						_equip.addLabel("Equip to " + character.getName());
						_equip.setSize(labelWidth, labelHeight);
						_equip.setVisible(true);
						_equip.setLocation(_itemOptBoxX, _itemOptBoxY);
						_buttonToChar.put(_equip, character);
						_buttonList.add(_equip);
					}
				}
				else {
					equipButton _equip = new equipButton(this, _listItem, _listItemHovered, _dt);
					_equip.addLabel("Equip to " + character.getName());
					_equip.setSize(labelWidth, labelHeight);
					_equip.setVisible(true);
					_equip.setLocation(_itemOptBoxX, _itemOptBoxY);
					_buttonToChar.put(_equip, character);
					_buttonList.add(_equip);
				}
			}
		}
		else {
			useButton _use = new useButton(this, _listItem, _listItemHovered, _dt);
			_use.addLabel("Use on " + character.getName());
			_use.setSize(labelWidth, labelHeight);
			_use.setVisible(true);
			_use.setLocation(_itemOptBoxX, _itemOptBoxY);
			_buttonToChar.put(_use, character);
			_buttonList.add(_use);
		}
		if (!isEquipped) {
			if (_dt.getParty().size() > 0) {
				for (int i=0; i<_dt.getParty().size(); i++) {
					System.out.println("Party member: " + _dt.getParty().get(i) + "; label to char: " + _labelToCharacter.get(label));
					if (_dt.getParty().get(i) != (_labelToCharacter.get(label))) {
						if (_dt.getParty().get(i).getInventory().size() != _dt.getParty().get(i).getCapacity()) {
							giveToCharButton _giveToChar = new giveToCharButton(this, _listItem, _listItemHovered, _dt);
							_giveToChar.addLabel("Give to " + _dt.getParty().get(i).getName());
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
			dropButton _drop = new dropButton(this, _listItem, _listItemHovered, _dt);
			_drop.addLabel("Drop Item");
			_drop.setSize(labelWidth, labelHeight);
			_drop.setVisible(true);
			_drop.setLocation(_itemOptBoxX, _itemOptBoxY+(numOptionsInserted*labelHeight));
			_buttonToChar.put(_drop, character);
			_buttonList.add(_drop);
		}

		boolean canPutRight;
		boolean canPutDown;
		System.out.println("ScrollBar value: " + _scrollBar.getVerticalScrollBar().getValue());

		if (label.getLocationOnScreen().getX()+labelWidth < 800) {
			canPutRight = true;
		}
		else {
			canPutRight = false;
		}
		if (label.getLocationOnScreen().getY()+(labelHeight*_buttonList.size()) < 700 && label.getLocationOnScreen().getY() - _scrollBar.getVerticalScrollBar().getValue() > 0) {
			canPutDown = true;
		}
		else {
			canPutDown = false;
		}

		if (!canPutRight) {
			_itemOptBoxX = _itemOptBoxX-labelWidth-SCROLLBOX_X;
		}
		if (canPutRight) {
			_itemOptBoxX = _itemOptBoxX-SCROLLBOX_X;
		}
		if (!canPutDown) {
			System.out.println("can't put down");
			_itemOptBoxY = (_itemOptBoxY-labelHeight*_buttonList.size()-SCROLLBOX_Y)+_scrollBar.getVerticalScrollBar().getValue();
		}
		if (canPutDown) {
			_itemOptBoxY = _itemOptBoxY-SCROLLBOX_Y+_scrollBar.getVerticalScrollBar().getValue();
		}
		
		for (int i=0; i<_buttonList.size(); i++) {
			_buttonList.get(i).setVisible(true);
		}

		_showingItemOptions = true;
		//		System.out.println("_buttons location x: " + (int)label.getLocationOnScreen().getX() + "; y: " + (int)label.getLocationOnScreen().getY());
		//		_buttons.setLocation((int)label.getLocationOnScreen().getX(), (int)label.getLocationOnScreen().getY());
		_buttons.setButtonList(_buttonList);
		//		System.out.println("Size: " + _buttonList.size());
		
//		System.out.println("option box location x: " + _itemOptBoxX + "; y: " + _itemOptBoxY);
		_buttons.setBounds(_itemOptBoxX,_itemOptBoxY,labelWidth, labelHeight*_buttonList.size());
		_buttons.grabFocus();
		_layers.add(_buttons, new Integer(1), 0);
		this.repaint();
//		_unitsBox.grabFocus();
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

			Insets inset = new Insets(2, 2, 2, 2);

			JLabel name = new JLabel(chrter.getName());
			name.setForeground(java.awt.Color.BLACK);
			name.setSize(200, 50);
			name.setVisible(true);

			JLabel level = new JLabel("Level : " + chrter.getLevel());
			level.setForeground(java.awt.Color.BLACK);
			level.setSize(200, 50);
			level.setVisible(true);

			JLabel exp = new JLabel("EXP : " + chrter.getExp());
			exp.setForeground(java.awt.Color.BLACK);
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
			//			HP.setFont(new Font("Arial", Font.BOLD, 12));
			//			HP.setForeground(java.awt.Color.WHITE);
			HP.setForeground(java.awt.Color.BLACK);
			HP.setSize(150, 50);
			HP.setVisible(true);

			int extraAttack = 0;
			int extraDef = 0;
			int extraAcc = 0;
			int extraSpeed = 0;
			
			if (chrter.getWeapon() != null) {
				extraAttack+=chrter.getWeapon().getPower();
				extraAcc+=chrter.getWeapon().getAccuracy();
			}
			if (chrter.getCuirass() != null) {
				extraDef+=chrter.getCuirass().getDefense();
			}
			if (chrter.getShield() != null) {
				extraDef+=chrter.getShield().getDefense();
			}
			if (chrter.getFootgear() != null) {
				extraSpeed+=chrter.getFootgear().getSpeed();
			}
			
			JLabel strength = new JLabel("STRENGTH : " + chrter.getBaseStats()[0] + "+" + extraAttack);
//			strength.setFont(new Font("Verdana", Font.BOLD, 12));
			strength.setForeground(java.awt.Color.BLACK);
			strength.setSize(150, 50);
			strength.setVisible(true);

			JLabel defense = new JLabel("DEFENSE : " + chrter.getBaseStats()[1] + "+" + extraDef);
			defense.setForeground(java.awt.Color.BLACK);
			defense.setSize(150, 50);
			defense.setVisible(true);

			JLabel special = new JLabel("SPECIAL : " + chrter.getBaseStats()[2]);
			special.setForeground(java.awt.Color.BLACK);
			special.setSize(150, 50);
			special.setVisible(true);

			JLabel resistance = new JLabel("RESISTANCE : " + chrter.getBaseStats()[3]);
			resistance.setForeground(java.awt.Color.BLACK);
			resistance.setSize(150, 50);
			resistance.setVisible(true);

			JLabel speed = new JLabel("SPEED : " + chrter.getBaseStats()[4] + "+" + extraSpeed);
			speed.setForeground(java.awt.Color.BLACK);
			speed.setSize(150, 50);
			speed.setVisible(true);

			JLabel skill = new JLabel("ACCURACY : " + chrter.getBaseStats()[5] + "+" + extraAcc);
			skill.setForeground(java.awt.Color.BLACK);
			skill.setSize(150, 50);
			skill.setVisible(true);

			JLabel luck = new JLabel("LUCK : " + chrter.getBaseStats()[6]);
			luck.setForeground(java.awt.Color.BLACK);
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

			Insets insetItems = new Insets(1, 1, 1, 1);

			JLabel inventory = new JLabel(new ImageIcon(inventoryPic));
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridwidth = 5;
			constraint.gridx = 0;
			constraint.gridy = 0;
			constraint.weightx = 1.0;
			row1.add(inventory, constraint);
			int count = 0;
			itemListener listener;
			for (Item item: chrter.getInventory().values()) {
				JLabel itemPic = new JLabel(new ImageIcon(item.getImage()));
				//				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				listener = new itemListener(itemPic);
				itemPic.addMouseMotionListener(listener);
				itemPic.setSize(65,65);
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
				JLabel item = new JLabel(new ImageIcon(_dt.importImage("src/graphics/items/empty_slot.png")));
				//				item.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				item.setSize(65, 65);
				item.setVisible(true);
				constraint.fill = GridBagConstraints.BOTH;
				constraint.insets = insetItems;
				constraint.gridwidth = 1;
				constraint.gridx = i+(chrter.getInventory().size());
				constraint.gridy = 1;
				row1.add(item, constraint);
			}

			BufferedImage equippedPic = null;


			equippedPic = _dt.importImage("src/graphics/menu/equipped.png");
			//			System.out.println("inventory pic size: w: " + inventoryPic.getWidth() + "y: " + inventoryPic.getHeight());


			JLabel equipped = new JLabel(new ImageIcon(equippedPic));
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridwidth = 5;
			constraint.gridx = 0;
			constraint.gridy = 2;
			constraint.weightx = 1.0;
			row1.add(equipped, constraint);

			JLabel weapon;
			itemListener listener1;
			if (chrter.getWeapon() != null) {
				weapon = new JLabel(new ImageIcon(chrter.getWeapon().getImage()));
				//			weapon.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				listener1 = new itemListener(weapon);
				weapon.addMouseMotionListener(listener1);
				weapon.addMouseListener(listener1);
				_labelToCharacter.put(weapon, chrter);
				_labelToItem.put(weapon, chrter.getWeapon());
			}
			else {
				weapon = new JLabel(new ImageIcon(_dt.importImage("src/graphics/items/empty_slot.png")));
			}
			weapon.setSize(65,65);
			weapon.setVisible(true);
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridwidth = 1;
			constraint.gridx = 0;
			constraint.gridy = 3;
			row1.add(weapon, constraint);


			JLabel cuirass;
			if (chrter.getCuirass() != null) {
				cuirass = new JLabel(new ImageIcon(chrter.getCuirass().getImage()));
				//				cuirass.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				listener1 = new itemListener(cuirass);
				cuirass.addMouseMotionListener(listener1);
				cuirass.addMouseListener(listener1);
				_labelToCharacter.put(cuirass, chrter);
				_labelToItem.put(cuirass, chrter.getCuirass());
			}
			else {
				cuirass = new JLabel(new ImageIcon(_dt.importImage("src/graphics/items/empty_slot.png")));
			}
			cuirass.setSize(65,65);
			cuirass.setVisible(true);
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = insetItems;
			constraint.gridx = 1;
			constraint.gridy = 3;
			row1.add(cuirass, constraint);

			JLabel shield;
			if (chrter.getShield() != null) {
				shield = new JLabel(new ImageIcon(chrter.getShield().getImage()));
				//				shield.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				listener1 = new itemListener(shield);
				shield.addMouseMotionListener(listener1);
				shield.addMouseListener(listener1);
				_labelToCharacter.put(shield, chrter);
				_labelToItem.put(shield, chrter.getShield());
			}
			else {
				shield = new JLabel(new ImageIcon(_dt.importImage("src/graphics/items/empty_slot.png")));
			}
			shield.setSize(65,65);
			shield.setVisible(true);
			constraint.fill = GridBagConstraints.BOTH;
			constraint.gridwidth = 1;
			constraint.insets = insetItems;
			constraint.gridx = 2;
			constraint.gridy = 3;
			row1.add(shield, constraint);

			JLabel footgear;
			if (chrter.getFootgear() != null) {
				footgear = new JLabel(new ImageIcon(chrter.getFootgear().getImage()));
				//				footgear.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
				listener1 = new itemListener(footgear);
				footgear.addMouseMotionListener(listener1);
				footgear.addMouseListener(listener1);
				_labelToCharacter.put(footgear, chrter);
				_labelToItem.put(footgear, chrter.getFootgear());
			}
			else {
				footgear = new JLabel(new ImageIcon(_dt.importImage("src/graphics/items/empty_slot.png")));
			}
			footgear.setSize(65,65);
			footgear.setVisible(true);
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

	private class itemListener implements MouseListener, MouseMotionListener {

		JLabel _itemPic;
		boolean showingMenu = false;

		public itemListener(JLabel itemPic) {
			_itemPic = itemPic;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (_currClicked == 1) {
				//				showingMenu = true;
				GameMenuScreen.this.checkItemClicked(e.getPoint(), _itemPic);
				//				showingMenu = false;
				//				Item clickedItem = GameMenuScreen.this.checkItemContains(e.getPoint());
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
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (_currClicked == 1 && !_showingItemOptions && !showingMenu) {
				GameMenuScreen.this.checkItemContains(e.getPoint(), _itemPic);
			}
		}
	}

	private class unitsBoxListener implements MouseMotionListener, MouseListener {

		public void mouseDragged(MouseEvent e) {

		}

		public void mouseMoved(MouseEvent e) {
			if (!_showingItemOptions) {
				GameMenuScreen.this.checkStopHovering(e);
			}
			else {
				GameMenuScreen.this.checkOptionHovered(e);
			}
		}

		public void mouseClicked(MouseEvent e) {
			if (_showingItemOptions) {
				GameMenuScreen.this.checkStopShowingOptionsBox(e.getLocationOnScreen());
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

	private abstract class optionButton extends MenuItem {

		JLabel _label;
		//		Point _point;

		public optionButton(JPanel panel, BufferedImage curr, BufferedImage hovered, DoodleTactics dt) {
			super(panel, curr, hovered, dt);
			this.setSize(152, 27);
		}
		public abstract void activate(optionButton button);

		public void addLabel(String words) {
			_label = new JLabel(words);
			//			_label.setLocation((int)this.getX(), (int)this.getY());
			_label.setSize(152, 27);
			_label.setVisible(false);
			_label.setOpaque(false);
			_label.setFont(new Font("Arial", Font.BOLD, 12));
		}

		public JLabel getLabel() {
			return _label;
		}
	}


	private class giveToCharButton extends optionButton {

		public giveToCharButton(JPanel panel, BufferedImage curr, BufferedImage hovered, DoodleTactics dt) {
			super(panel, curr, hovered, dt);
		}

		public void activate(optionButton button) {
			Character myChar = _buttonToChar.get(button);
			try {
				if (myChar.getInventory().size() != myChar.getCapacity()) {
					myChar.addToInventory(_selectedItem);
					_selectedChar.removeFromInventory(_selectedItem);
					//					for (int i=0; i<_buttonList.size(); i++) {
					//						_buttonList.get(i).setVisible(false);
					//					}
					GameMenuScreen.this.setDefaultTabToUnits();
					System.out.println("set to false");
					_showingItemOptions = false;
					GameMenuScreen.this.repaint();
				}
			} catch (ItemException e1) {
				System.out.println("Something bad happened in the Game Menu Screen");
			}
		}
	}

	private class dropButton extends optionButton {

		public dropButton(JPanel panel, BufferedImage curr, BufferedImage hovered, DoodleTactics dt) {
			super(panel, curr, hovered, dt);
		}

		public void activate(optionButton button) {
			try {
				_selectedChar.removeFromInventory(_selectedItem);
				//				for (int i=0; i<_buttonList.size(); i++) {
				//					_buttonList.get(i).setVisible(false);
				//				}
				GameMenuScreen.this.setDefaultTabToUnits();
				System.out.println("set to false");
				_showingItemOptions = false;
				GameMenuScreen.this.repaint();
			} catch (ItemException e1) {
				System.out.println("Something bad happened in the Game Menu Screen");
			}
		}
	}

	private class unequipButton extends optionButton {

		public unequipButton(JPanel panel, BufferedImage curr, BufferedImage hovered, DoodleTactics dt) {
			super(panel, curr, hovered, dt);
		}

		@Override
		public void activate(optionButton button) {
			try {
				if (_selectedChar.getWeapon() != null) {
					if (_selectedChar.getWeapon().equals(_selectedItem)) {
						_selectedChar.addToInventory(_selectedChar.getWeapon());
						_selectedChar.removeWeapon();
					}
				}
				if (_selectedChar.getCuirass() != null) {
					if (_selectedChar.getCuirass().equals(_selectedItem)) {
						_selectedChar.addToInventory(_selectedChar.getCuirass());
						_selectedChar.removeCuirass();
					}
				}
				if (_selectedChar.getShield() != null) {
					if (_selectedChar.getShield().equals(_selectedItem)) {
						_selectedChar.addToInventory(_selectedChar.getShield());
						_selectedChar.removeShield();
					}
				}
				if (_selectedChar.getFootgear() != null) {
					if (_selectedChar.getFootgear().equals(_selectedItem)){
						_selectedChar.addToInventory(_selectedChar.getFootgear());
						_selectedChar.removeFootgear();
					}
				}
				GameMenuScreen.this.setDefaultTabToUnits();
				System.out.println("set to false");
				_showingItemOptions = false;
				GameMenuScreen.this.repaint();
			} catch (ItemException e) {
				_dt.error("The item you tried to access does not exist.");
			}
		}

	}

	private class equipButton extends optionButton {

		JLabel _label;

		public equipButton(JPanel panel, BufferedImage curr, BufferedImage hovered, DoodleTactics dt) {
			super(panel, curr, hovered, dt);
			// TODO Auto-generated constructor stub
		}

		public void activate(optionButton button) {
			try {
				if (!_selectedChar.equip((Equipment)_selectedItem)) {
					_dt.error("That item cannot be equipped.");
				}
				
				GameMenuScreen.this.setDefaultTabToUnits();
			//	System.out.println("set to false");
				_showingItemOptions = false;
				GameMenuScreen.this.repaint();
			} catch (ClassCastException e) {
				_dt.error("The item you tried to access does not exist.");
			}
		}
	}

	private class useButton extends optionButton {

		public useButton(JPanel panel, BufferedImage curr, BufferedImage hovered, DoodleTactics dt) {
			super(panel, curr, hovered, dt);
		}

		public void activate(optionButton button) {
			_selectedItem.exert(_selectedChar);
			//			for (int i=0; i<_buttonList.size(); i++) {
			//				_buttonList.get(i).setVisible(false);
			//			}
			GameMenuScreen.this.setDefaultTabToUnits();
			System.out.println("set to false");
			_showingItemOptions = false;
			GameMenuScreen.this.repaint();
		}
	}

	private class buttonPanel extends JPanel {

		private ArrayList<optionButton> _list;
		private boolean _visible;

		public buttonPanel (ArrayList<optionButton> list) {
			_list = list;
		}

		public void setButtonList(ArrayList<optionButton> list) {
			_list = list;
		}

		public void setVisible(boolean bool) {
			super.setVisible(bool);
			_visible = bool;
		}

		public void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if (_visible) {
				for (int i=0; i<_list.size(); i++) {
					if (_list.get(i) != null) {
						//						System.out.println("X: " + x.getX() + "; Y: " + x.getY());
						_list.get(i).setLocation(0,i*27);
						_list.get(i).setVisible(true);
						_list.get(i).paint((Graphics2D) g, _list.get(i).getImage());
						this.add(_list.get(i).getLabel());
						_list.get(i).getLabel().setLocation(10, (i*27)+5);
						_list.get(i).getLabel().setVisible(true);
					}
				}
			}
		}
	}

	public void increaseCurrOption(){
		_currOption  = (_currOption + 1)  % NUM_OPTIONS;
	}

	public void decreaseCurrOption(){
		_currOption = (_currOption - 1) % NUM_OPTIONS;
		if(_currOption < 0){
			_currOption = NUM_OPTIONS - 1;
		}
	}

	public void assignKey(KeyEvent e){
		int key = e.getKeyCode();
		String c = KeyEvent.getKeyText(e.getKeyCode());
		for(int i=0; i< NUM_OPTIONS; i++)
			if(_keys[i].equals(c))
				return;
		_keys[_currOption] = c;
		_keyCodes[_currOption] = key;
		if(_currOption == 0)
			_dt.setLeftKey(key);
		else if(_currOption == 1)
			_dt.setRightKey(key);
		else if(_currOption == 2)
			_dt.setUpKey(key);
		else if(_currOption == 3)
			_dt.setDownKey(key);
		else if(_currOption == 4)
			_dt.setInteractKey(key);
		else if(_currOption == 5)
			_dt.setMenuKey(key);
		this.repaint();
	}

	/**
	 * Reset key names to old values
	 */
	public void load(int[] keycodes){
		_keyCodes = keycodes;
		for(int i=0; i<NUM_OPTIONS; i++){
			_keys[i] = KeyEvent.getKeyText(_keyCodes[i]);
		}

		_dt.setLeftKey(_keyCodes[0]);
		_dt.setRightKey(_keyCodes[1]);
		_dt.setUpKey(_keyCodes[2]);
		_dt.setDownKey(_keyCodes[3]);
		_dt.setInteractKey(_keyCodes[4]);
	}

	public int[] getKeyCodes(){
		return _keyCodes;
	}

	public SaveMenuItem getSaveMenuItem(){
		return _saveMenuItem;
	}

	/*public char getKey(int key){
		if(_currOption == 0)
			return _dt.getLeftKey();
		else if(_currOption == 1)
			_dt.setRightKey(key);
		else if(_currOption == 2)
			_dt.setUpKey(key);
		else if(_currOption == 3)
			_dt.setDownKey(key);
		else if(_currOption == 4)
			_dt.setInteractKey(key);
		else if(_currOption == 5)
			_dt.setMenuKey(key);

	}*/

	//	private class arrowTimer extends java.awt.Timer() {
	//		
	//	}

}