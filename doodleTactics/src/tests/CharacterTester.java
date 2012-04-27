package tests;
import javax.swing.JPanel;

import org.junit.*;
import character.*;
import character.Character;
/**
 * 
 * @author czchapma
 * Tests the classes and methods of the Character class
 */
public class CharacterTester {
	@Test
	public void testSerialize(){
		//serialization tests
		String filepath = "src/tests/data/";
		JPanel container = new JPanel();
		//Archer, leveled up
		String filename = "testCharSerial1.ser";
		Archer a = new Archer(container,"src/graphics/characters/pokeball.png","src/graphics/characters/pokeball.png","src/graphics/characters/knight_front.png","src/graphics/characters/knight_back.png","src/graphics/characters/knight_left.png","src/graphics/characters/knight_right.png","knight",0,0);
		try {
			a.levelUp();
		} catch (InvalidLevelException e) {
			assert(false);
		}
		a.serialize(filepath + filename);
		Archer restored = (Archer) Archer.restore(filepath + filename);
		assert(restored.equals(a));
		assert(!restored.equals(new Archer(container,"src/graphics/characters/pokeball.png","src/graphics/characters/pokeball.png","src/graphics/characters/knight_front.png","src/graphics/characters/knight_back.png","src/graphics/characters/knight_left.png","src/graphics/characters/knight_right.png","knight",0,0)));
		
		//Warrior
		filename = "testCharSerial2.ser";
		Warrior w = new Warrior(container,"src/graphics/characters/pokeball.png","src/graphics/characters/pokeball.png","src/graphics/characters/knight_front.png","src/graphics/characters/knight_back.png","src/graphics/characters/knight_left.png","src/graphics/characters/knight_right.png","warrior",0,0);
		w.serialize(filepath + filename);
		Warrior restoredW = (Warrior) Warrior.restore(filepath + filename);
		assert(restoredW.equals(w));
		
		//Mage
		filename = "testCharSerial3.ser";
		Mage m = new Mage(container,"src/graphics/characters/pokeball.png","src/graphics/characters/pokeball.png","src/graphics/characters/knight_front.png","src/graphics/characters/knight_back.png","src/graphics/characters/knight_left.png","src/graphics/characters/knight_right.png","mage",0,0);
		m.serialize(filepath + filename);
		Mage restoredM = (Mage) Mage.restore(filepath + filename);
		assert(restoredM.equals(m));
		
		//Thief
		filename = "testCharSerial4.ser";
		Thief t = new Thief(container,"src/graphics/characters/pokeball.png","src/graphics/characters/pokeball.png","src/graphics/characters/knight_front.png","src/graphics/characters/knight_back.png","src/graphics/characters/knight_left.png","src/graphics/characters/knight_right.png","thief",0,0);
		t.serialize(filepath + filename);
		Thief restoredT = (Thief) Thief.restore(filepath + filename);
		assert(restoredT.equals(t));

	}
}
