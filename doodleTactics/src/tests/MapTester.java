package tests;

import org.junit.*;

import map.*;

/**
 * 
 * @author rroelke
 * tests the classes and methods of the map package
 */
public class MapTester {
	
	private static Map[] _tests;
	
	@BeforeClass
    public static void setUpClass() throws Exception {
		_tests = new Map[1];
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
	 * tests the Map's pathfinding capabilities
	 */
	public void testPathFinding() {
		
	}
	
}
