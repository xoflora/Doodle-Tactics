package controller;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import main.*;

public abstract class Controller implements MouseListener, KeyListener {
	
	protected DoodleTactics _dt;
	
	public abstract void release();
}
