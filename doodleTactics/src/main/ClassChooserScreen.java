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

import controller.ClassChooserScreenController;
import controller.Controller;

public class ClassChooserScreen extends Screen {

	DoodleTactics _dt;
	MenuItem _bg, _mage, _warrior, _thief, _archer, _name, _done;
	String _chosenClass;
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
			else if (_chosenClass != null) {
				System.out.println("clicked");
				_dt.getGameScreen().getMainChar().setName(_typeText.getText());
				_dt.setScreen(_dt.getGameScreen());
			}
		}
		else if (_warrior.contains(p)) {
			_chosenClass = "warrior";
		}
//		else if (_)
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
