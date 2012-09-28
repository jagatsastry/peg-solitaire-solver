/**
 * 
 */
package ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import ai.Board.Hole;

/**
 * @author jagat
 *
 */
public class PegSolitaireSolver {
    
	public static class Move {
		int m_fromx, m_fromy, m_tox, m_toy;

		public Move(int fromx, int fromy, int tox, int toy) {
			super();
			this.m_fromx = fromx;
			this.m_fromy = fromy;
			this.m_tox = tox;
			this.m_toy = toy;
		}

		public void printMove() {
			System.out.printf("Move (%d, %d) to (%d, %d)%n", m_fromx, m_fromy, m_tox, m_toy);
		}
	}
	
	private Board m_board;
	private Set<Long> m_unsolvableStates = new HashSet<Long>();
	private Stack<Move> m_moves = new Stack<Move>();
	public PegSolitaireSolver(Board board) {
		m_board = board;
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

	private static List<List<String>> getTests() {
		List<List<String>> tests = new ArrayList<List<String>>();
		List<String> test = new ArrayList<String>() { 
			private static final long serialVersionUID = 1L;
			{add("--000--"); } 
			{add("--0X0--"); } 
	        {add("00XXX00"); } 
	        {add("000X000"); }
	        {add("000X000"); }
	        {add("--000--"); }
	        {add("--000--"); }
        }; 
		
		tests.add(test);
		return tests;
	}

	private void printSteps() {
        while(!m_moves.empty())
        	m_moves.pop().printMove();
	}
    
	int[][] deltas = new int[][]{{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
	
			
	private boolean solve() {
		int pegCount = 0;
		for(int x = 0; x < Board.SIZE; x++) {
			for(int y = 0; y < Board.SIZE; y++) {
				if(this.m_board.get(x, y) == Hole.PEG) {
					pegCount++; 
					for(int[] delta: deltas) {
						int dx = delta[0];
						int dy = delta[1];
						if(validMove(x, y, dx, dy)) {
							m_board.move(x, y, dx, dy);
							Long boardCfg = m_board.bitMap();
							if(!m_unsolvableStates.contains(boardCfg)) {
								if(solve()) {
									m_moves.push(new Move(x, y, x + dx, y + dy));
									return true;
								}
								m_unsolvableStates.add(boardCfg);
							}
							m_board.restore(x, y, dx, dy);
						}
					}
				}
			}
		}
		return pegCount == 1;
	}

	private boolean validMove(int x, int y, int dx, int dy) {
		int stepx = x + dx/2, stepy = y + dy/2,
				jumpx = x + dx, jumpy = y + dy;
		if(stepx < 0 || stepx >= Board.SIZE || stepy < 0 || stepy >= Board.SIZE 
				|| jumpx < 0 || jumpx >= Board.SIZE || jumpy < 0 || jumpy >= Board.SIZE)
			return false;

		Hole intHole = m_board.get(stepx,  stepy);
        Hole destHole = m_board.get(jumpx, jumpy);
        if(intHole == Hole.INVALID || intHole == Hole.EMPTY || 
        		destHole == Hole.INVALID || destHole != Hole.EMPTY)
        	return false;
        return true;
	}
}
