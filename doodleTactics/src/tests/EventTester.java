package tests;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import event.*;
import main.DoodleTactics;
import org.junit.*;

import character.*;
import character.Character;

/**
 * 
 * @author czchapma
 * Tests the classes and methods of the package event
 */
public class EventTester {
	@BeforeClass
	public static void setUpClass() throws Exception {
		
	}
	
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

	@Test
	/**
	 * tests parsing in files
	 */
	public void testDialogueParsing(){
		HashMap<String, Character> map = new HashMap<String,Character>();
		DialogueBox db;
		Archer a = new Archer();
		Mage m = new Mage();
		Thief t = new Thief();
		Warrior w = new Warrior();
		
		//base case (no characters)
		try{
			db = new DialogueBox("src/tests/data/testDialogueEmpty", map);
			assert(db.getCharList().isEmpty());
			assert(db.getCharList().isEmpty());
		} catch(FileNotFoundException e){
			assert(false);
		} catch(IOException e){
			assert(false);
		} catch(InvalidFileException e){
			assert(false);
		}
		
		//general case (4 characters)
		map.put("archer",a);
		map.put("mage", m);
		map.put("thief", t);
		map.put("warrior", w);
		
		LinkedList<Character> expectedCharList = new LinkedList<Character>();
		expectedCharList.add(a);
		expectedCharList.add(m);
		expectedCharList.add(t);
		expectedCharList.add(w);
		
		LinkedList<String> expectedStringList = new LinkedList<String>();
		expectedStringList.add("i am an archer");
		expectedStringList.add("i am a mage");
		expectedStringList.add("i am a thief");
		expectedStringList.add("i am a warrior");

		try{
			db = new DialogueBox("src/tests/data/testDialogue1", map);
			System.out.println(db.getDialogueList());
			System.out.println(expectedStringList);
			assert(db.getDialogueList().equals(expectedStringList));
			assert(db.getCharList().equals(expectedCharList));
		} catch(FileNotFoundException e){
			assert(false);
		} catch(IOException e){
			assert(false);
		} catch(InvalidFileException e){
			assert(false);
		}
		
	}

}
