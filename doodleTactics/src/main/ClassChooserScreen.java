package main;

import graphics.MenuItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import character.MainCharacter;

import map.Tile;

import controller.ClassChooserScreenController;
import controller.Controller;

public class ClassChooserScreen extends Screen {

	DoodleTactics _dt;
	MenuItem _bg, _mage, _warrior, _thief, _archer, _name, _done;
	int _chosenClass;
	String _charName;
	JTextField _typeText;
	
	public ClassChooserScreen(DoodleTactics dt) {
		super(dt);
		
		this.setLayout(null);
		
		setBackground(Color.GRAY);
		_dt = dt;
		_bg = new MenuItem(this, _dt.importImage("src/graphics/menu/char_selection_background.png"), _dt.importImage("src/graphics/menu/char_selection_background.png"), _dt);
		_mage = new MenuItem(this, _dt.importImage("src/graphics/menu/char_selection_mage.png"), _dt.importImage("src/graphics/menu/char_selection_mage.png"), _dt);
		_warrior = new MenuItem(this, _dt.importImage("src/graphics/menu/char_selection_warrior.png"), _dt.importImage("src/graphics/menu/char_selection_warrior.png"), _dt);
		_thief = new MenuItem(this, _dt.importImage("src/graphics/menu/char_selection_thief.png"), _dt.importImage("src/graphics/menu/char_selection_thief.png"), _dt);
		_name = new MenuItem(this, _dt.importImage("src/graphics/menu/char_selection_name.png"), _dt.importImage("src/graphics/menu/char_selection_name.png"), _dt);
		_done = new MenuItem(this, _dt.importImage("src/graphics/menu/char_selection_done_default.png"), _dt.importImage("src/graphics/menu/char_selection_done_hovered.png"), _dt);
		
		_bg.setLocation(0, 0);
		_bg.setVisible(true);
		
		_warrior.setLocation(50, 125);
		_warrior.setVisible(true);
		
		_thief.setLocation(520, 125);
		_thief.setVisible(true);
		
		_mage.setLocation(50, 423);
		_mage.setVisible(true);
		
		_name.setLocation(30, 725);
		_name.setVisible(true);
		
		_done.setLocation(730, 710);
		_done.setVisible(true);
		
		_typeText = new JTextField();
		Font myFont = new Font("Arial", Font.BOLD, 28);
		_typeText.setFont(myFont);
		_typeText.setVisible(true);
		_typeText.setEditable(true);
		_typeText.setOpaque(false);
		_typeText.setSize(215, 35);
		_typeText.setBorder(null);
		_typeText.setCaretColor(java.awt.Color.MAGENTA);
		_typeText.getCaret().setBlinkRate(400);
		_typeText.setLocation(490, 735);
		_typeText.setFocusable(true);
		_typeText.grabFocus();
		_typeText.setDocument(new maxLengthDoc());
		
		this.add(_typeText);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D brush = (Graphics2D) g;
		_bg.paint(brush, _bg.getImage());
		_warrior.paint(brush, _warrior.getImage());
		_thief.paint(brush, _thief.getImage());
		_mage.paint(brush, _mage.getImage());
		_name.paint(brush, _name.getImage());
		_done.paint(brush, _done.getImage());
		_typeText.grabFocus();
	}
	
	public void setDefault() {
		_done.setDefault();
	}
	
	public void checkContains(java.awt.Point p) {

		this.setDefault();
		
		if (_done.contains(p)) {
			_done.setHovered();
		}
		this.repaint();
	}
	
	public void checkClicked (java.awt.Point p) {
		if (_done.contains(p)) {
			if (_typeText.getText().equals("")) {
				_typeText.setBackground(java.awt.Color.CYAN);
				_typeText.setOpaque(true);
				this.repaint();
			}
			else if (_chosenClass != 0) {
				System.out.println("Chosen class: " + _chosenClass);
				MainCharacter mainChar = _dt.getGameScreen().getMainChar();
				switch (_chosenClass) {
				case 1:
					mainChar.setStats(8, 6, 5, 5, 6, 7, 3, 15);
					mainChar.setImages(_dt.importImage("src/graphics/characters/warrior_portrait.png"), _dt.importImage("src/graphics/characters/warrior_left_color.png"), _dt.importImage("src/graphics/characters/warrior_right_color.png"), _dt.importImage("src/graphics/characters/warrior_back_color.png"), _dt.importImage("src/graphics/characters/warrior_front_color.png"));
					mainChar.setSize(_dt.importImage("src/graphics/characters/warrior_front_color.png").getWidth(), 48);
					int overflow = 0;
					if(mainChar.getDownImage().getWidth() - Tile.TILE_SIZE <= 25.0)
					overflow = (mainChar.getDownImage().getWidth() - Tile.TILE_SIZE) / 2;
					mainChar.setLocation(mainChar.getX() - overflow,mainChar.getY() - mainChar.getDownImage().getHeight() + Tile.TILE_SIZE);
//					_dt.getGameScreen().set
					break;
				case 2:
					_dt.getGameScreen().getMainChar().setStats(6, 6, 5, 6, 10, 7, 9, 13);
					_dt.getGameScreen().getMainChar().setImages(_dt.importImage("src/graphics/characters/thief_portrait.png"), _dt.importImage("src/graphics/characters/thief_left.png"), _dt.importImage("src/graphics/characters/thief_right.png"), _dt.importImage("src/graphics/characters/thief_front.png"), _dt.importImage("src/graphics/characters/thief_back.png"));
					break;
				case 3:
					_dt.getGameScreen().getMainChar().setStats(4, 5, 9, 8, 7, 5, 5, 12);
					_dt.getGameScreen().getMainChar().setImages(_dt.importImage("src/graphics/characters/mage_portrait.png"), _dt.importImage("src/graphics/characters/mage_left.png"), _dt.importImage("src/graphics/characters/mage_right.png"), _dt.importImage("src/graphics/characters/mage_back.png"), _dt.importImage("src/graphics/characters/mage_front.png"));
					break;
				case 4:
					_dt.getGameScreen().getMainChar().setStats(7, 5, 5, 6, 9, 9, 3, 14);
					break;
				}
				_dt.getGameScreen().getMainChar().setName(_typeText.getText());
				_dt.getGameScreen().repaint();
				_dt.setScreen(_dt.getGameScreen());
			}
		}
		else if (_warrior.contains(p)) {
			_chosenClass = 1;
		}
		else if (_thief.contains(p)) {
			_chosenClass = 2;
		}
		else if (_mage.contains(p)) {
			_chosenClass = 3;
		}
		else if (_archer.contains(p)) {
			_chosenClass = 4;
		}
	}

	@Override
	protected Controller defaultController() {
		// TODO Auto-generated method stub
		return new ClassChooserScreenController(_dt, this);
	}
	
	private class maxLengthDoc extends PlainDocument {
		
		public void insertString(int offset, String text, AttributeSet attributes) {
			if (text != null && !text.equals(" ") && this.getLength() + text.length() <= 10) {
				try {
					super.insertString(offset, text, attributes);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					_dt.error("Error when typing name into the text box");
				}
			}
		}
		
	}

}