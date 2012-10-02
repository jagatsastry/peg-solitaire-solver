/**
 * 
 */
package ai;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;


/**
 * @author jagat
 *
 */
public class PegSolitaireSolver {
    
	private Board m_board;
	private Set<Long> m_unsolvableStates = new HashSet<Long>();
	private Stack<Move> m_moves = new Stack<Move>();
	private static CostComparator cc = new CostComparator();
	private PriorityQueue<PriorityNode> priorityQueue = new  PriorityQueue<PriorityNode>(11,cc);
	public static int numMoves=0;
	public PegSolitaireSolver(Board board) {
		m_board = board;
	}

	void printSteps() {
        while(!m_moves.empty())
        	m_moves.pop().printMove();
	}
    
	int[][] deltas = new int[][]{{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
	
			
	boolean solve() {
		numMoves++;
		int pegCount = 0;
		for(int x = 0; x < Board.SIZE; x++) 
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
								m_unsolvableStates.addAll(m_board.getSymmetricConfigs());
							}
							m_board.restore(x, y, dx, dy);
						}
					}
				}
			}
		return pegCount == 1;
	}
	
	int aStar(){
		int noNodesVisited=0;
		int distance=0;
		PriorityNode pn = new PriorityNode();
		LinkedList<Board> stateList = new LinkedList<Board>();
		LinkedList<Board> prevStateList;
		Board currentState;
		pn.setCost(Pagoda.evaluatePagoda(m_board));
		stateList.add(m_board);
		pn.setStates(stateList);
		priorityQueue.add(pn);
		//check for whether state is goal state
		currentState=m_board;
		while(!isGoalState(currentState) && !priorityQueue.isEmpty()){
			noNodesVisited++;
			pn=priorityQueue.poll();
			prevStateList= pn.getStates();
			distance = pn.getDistance();
			for(int x = 0; x < Board.SIZE; x++) 
				for(int y = 0; y < Board.SIZE; y++){
					if(currentState.get(x, y) == Hole.PEG){
						for(int[] delta: deltas) {
							m_board= currentState.copyBoard();
							int dx = delta[0];
							int dy = delta[1];
							if(validMove(x, y, dx, dy)){
								m_board.move(x, y, dx, dy);
								pn=new PriorityNode();
								stateList = new LinkedList<Board>(prevStateList);
								stateList.addFirst(m_board);
								pn.setStates(stateList);
								pn.setDistance(++distance);
								pn.setCost(distance + Pagoda.evaluatePagoda(m_board));
								//--distance;
								priorityQueue.add(pn);
							}
						}
					}
				}
			if(null!=priorityQueue && !priorityQueue.isEmpty())
				currentState = priorityQueue.peek().getStates().peek();
		}
		return noNodesVisited;
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
	
	private boolean isGoalState(Board state){
		int pegCount=0;
		for(int i=0;i<Board.SIZE;i++)
			for(int j=0;j<Board.SIZE;j++)
				if(state.get(i, j)==Hole.PEG)
					pegCount++;
		if(1==pegCount)
			return true;
		return false;
	}
}
