package tests;
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
		
		//Archer, leveled up
		String filename = "testCharSerial1.ser";
		Archer a = new Archer("archer");
		try {
			a.levelUp();
		} catch (InvalidLevelException e) {
			assert(false);
		}
		a.serialize(filepath + filename);
		Archer restored = (Archer) Archer.restore(filepath + filename);
		assert(restored.equals(a));
		assert(!restored.equals(new Archer("archer")));
		
		//Warrior
		filename = "testCharSerial2.ser";
		Warrior w = new Warrior("warrior");
		w.serialize(filepath + filename);
		Warrior restoredW = (Warrior) Warrior.restore(filepath + filename);
		assert(restoredW.equals(w));
		
		//Mage
		filename = "testCharSerial3.ser";
		Mage m = new Mage("mage");
		m.serialize(filepath + filename);
		Mage restoredM = (Mage) Mage.restore(filepath + filename);
		assert(restoredM.equals(m));
		
		//Thief
		filename = "testCharSerial4.ser";
		Thief t = new Thief("thief");
		t.serialize(filepath + filename);
		Thief restoredT = (Thief) Thief.restore(filepath + filename);
		assert(restoredT.equals(t));

	}
}
