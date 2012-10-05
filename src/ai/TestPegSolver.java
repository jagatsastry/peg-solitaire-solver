package ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ai.PegSolitaireSolver.Heuristic;

public class TestPegSolver {

	static List<List<String>> getTests(File inputFile) throws IOException {
		List<List<String>> tests = new ArrayList<List<String>>();
		
		BufferedReader in = new BufferedReader(new FileReader(inputFile));
		String line = null;
		List<String> test1 = null;
		int idx = 0;
        while((line = in.readLine()) != null) {
        	 if(idx++%Board.SIZE == 0) {
        		 test1 = new ArrayList<String>();
        		 tests.add(test1);
        	 }
        	 test1.add(line);
	  }
      return tests;
	}
	
	static List<List<String>> getTests() {
		List<List<String>> tests = new ArrayList<List<String>>();
		
		List<String> test1 = new ArrayList<String>()
		 { 
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
	
	boolean f(int n) {
		if(n < 0) return false;
		if(n == 0 ) return true;
		if(n == 1) return false;
		int m = 0;
		while(n > 0) {
			m = m + (n&1);
			n = n >> 1;
			m = m - (n&1);
			n = n>>1;
		}
		return f(m);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			List<List<String>> boardConfigs = getTests(); 
			int algoType = -1;
			if(args.length > 0) {
				boardConfigs = getTests(new File(args[0]));
				if(args.length > 1) 
					algoType = Integer.parseInt(args[1]);
			}
			
		    int idx = 0;
			for(List<String> cfg: boardConfigs) {
				System.out.println();
				System.out.println("***********************************");
				System.out.println("TEST " + (++idx));
				Board testBoard = Board.getBoard(cfg);
				System.out.println(testBoard); 
				if(algoType == -1 || algoType == 1) {
					PegSolitaireSolver solver = new PegSolitaireSolver(testBoard);
					System.out.println("Solving using DFS");
					if(!solver.dfs())
						System.err.println("The given game instance cannot be solved using DFS");
					else {
						solver.printSummary("Depth First Search");
	//					solver.printSteps();
	//					List<List<Integer>> path = solver.getCompressedPaths();
	//					System.out.println(path);
					}
					System.out.println();
				}
				
				List<Heuristic> heuristics = new ArrayList<Heuristic>();
				if(algoType == -1 || algoType == 2)
					heuristics.add(Heuristic.MANHATTAN);
				if(algoType == -1 || algoType == 3)
					heuristics.add(Heuristic.WEIGHTED_COST);
						
				for(Heuristic hr: heuristics) {
					System.out.println("Solving using A-Star with " + hr);
					testBoard = Board.getBoard(cfg);
					PegSolitaireSolver astarSolver = new PegSolitaireSolver(testBoard);
					astarSolver.setHeuristic(hr);
					if(!astarSolver.aStar())
						System.err.println("The given game instance cannot be solved using A-Star");
					else {
						astarSolver.printSummary("A-Star with " + hr);
	//					astarSolver.printSteps();
	//					List<List<Integer>> path = astarSolver.getCompressedPaths();
	//					System.out.println(path);
					}
				}

				//solver.printMoveSeq();
//				System.out.println("Number of states expanded by A-Star: "+ astarSolver.numExpandedStates());
				
				
				//System.err.println("The given game instance cannot be solved");
				//else print steps
			}		
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
