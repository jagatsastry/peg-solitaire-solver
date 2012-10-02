package ai;

import java.util.ArrayList;
import java.util.List;

public class TestPegSolver {

	static List<List<String>> getTests() {
		List<List<String>> tests = new ArrayList<List<String>>();
		List<String> test = new ArrayList<String>() { 
			private static final long serialVersionUID = 1L;
			{add("--000--"); } 
			{add("--000--"); } 
	        {add("00X0000"); } 
	        {add("0XXXX00"); }
	        {add("00X0000"); }
	        {add("--000--"); }
	        {add("--000--"); }
        }; 
        
		List<String> test1 = new ArrayList<String>() { 
			private static final long serialVersionUID = 1L;
			{add("--XXX--"); } 
			{add("--XXX--"); } 
	        {add("XXXXXXX"); } 
	        {add("XXX0XXX"); }
	        {add("XXXXXXX"); }
	        {add("--XXX--"); }
	        {add("--XXX--"); }
        }; 
        
		tests.add(test1);
		return tests;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			List<List<String>> boardConfigs = getTests();
			for(List<String> cfg: boardConfigs) {
				Board testBoard = Board.getBoard(cfg);
				PegSolitaireSolver solver = new PegSolitaireSolver(testBoard);
				if(!solver.solve())
					System.err.println("The given game instance cannot be solved");
				else solver.printSteps();
			}
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
