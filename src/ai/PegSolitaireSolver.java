/**
 * 
 */
package ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;


/**
 * @author jagat
 *
 */
public class PegSolitaireSolver {
    
	private Board m_origBoard;
	private Board m_board;
	private Set<Long> m_unsolvableStates = new HashSet<Long>();
	private Map<Long, Long> m_visitedStates = new HashMap<Long, Long>();
	private Stack<Move> m_moves = new Stack<Move>();
	private static CostComparator m_cc = new CostComparator();
	private PriorityQueue<PriorityNode> m_priorityQueue = new  PriorityQueue<PriorityNode>(11,m_cc);
	public int m_numMoves=0;
	
	private Heuristic m_heuristic = Heuristic.PAGODA;
	public int getNumMoves() {
		return m_numMoves;
	}

	public PegSolitaireSolver(Board board) {
		m_board = board;
		m_origBoard = m_board.copyBoard();
	}

	void printSteps() {
		Board cpy = m_origBoard.copyBoard();
		System.out.println(cpy);
        while(!m_moves.empty()) {
        	Move mv = m_moves.pop();
        	mv.printMove();
        	cpy.move(mv);
        	System.out.println(cpy);
        }
	}
    
	int[][] deltas = new int[][]{{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
	
			
	boolean solve() {
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
							m_numMoves++;
							
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
	
	public static enum Heuristic { PAGODA, CENTRIC};
	
	private int getCost() {
		switch(m_heuristic) {
		case PAGODA:
			return Pagoda.evaluatePagoda(m_board);
		case CENTRIC:
			
			return 0; //TODO
		}
		return 0;
	}
	
	int aStar(){
		int noNodesVisited=0;
		int distance=0;
		PriorityNode root = new PriorityNode();
		root.setCost(getCost());
		root.setState(m_board.bitMap());
		root.setPrevState(-1);
		m_priorityQueue.add(root);
		//check for whether state is goal state
		Board currentState=m_board;
		
		//m_visitedStates.put(m_board.bitMap(), (long)-1);
		for(Long st: m_board.getSymmetricConfigs()) 
			m_visitedStates.put(st, (long)-1);

		while(!isGoalState(currentState) && !m_priorityQueue.isEmpty()){
			System.out.println(noNodesVisited++);
			long curStateBmp = m_priorityQueue.peek().getState();
			currentState = Board.getBoard(curStateBmp);
			PriorityNode curNode = m_priorityQueue.poll();
			distance = curNode.getDistance();
//			Long bcf = currentState.bitMap();
//			m_visitedStates.addAll(currentState.getSymmetricConfigs());	
//			if(m_visitedStates.contains(currentState.bitMap()))
//				continue;
			int[] iterOrder = new int[Board.SIZE];
			int[] deltaOrder = new int[4];
			int idx = 0;
			for(int i = 0; i<Board.SIZE; i++) 
				iterOrder[i] = idx++;
			
			idx = 0;
			for(int i = 0; i<4; i++) 
				deltaOrder[i] = idx++;
			
			Collections.shuffle(Arrays.asList(iterOrder));
			Collections.shuffle(Arrays.asList(deltaOrder));
			for(int x: iterOrder) 
				for(int y: iterOrder){
					if(currentState.get(x, y) == Hole.PEG){	
						for(int index: deltaOrder) {
							m_board= currentState.copyBoard();
							int dx = deltas[index][0];
							int dy = deltas[index][1];
							if(validMove(x, y, dx, dy)){
								m_board.move(x, y, dx, dy);
								
								Long boardSt = m_board.bitMap();
								if(m_visitedStates.get(boardSt) != null)
									continue;
								m_visitedStates.put(boardSt, curStateBmp);
								for(Long st: m_board.getSymmetricConfigs()) 
									m_visitedStates.put(st, curStateBmp);
								
								
								PriorityNode pn=new PriorityNode();
								pn.setState(boardSt);
								pn.setPrevState(curStateBmp);
								pn.setDistance(++distance);
								
								pn.setCost(getCost());
								--distance;
								m_priorityQueue.add(pn);
								
							}
						}
					}
				}
			if(!m_priorityQueue.isEmpty())
				currentState = Board.getBoard(m_priorityQueue.peek().getState());
//			long curStateBmp = m_priorityQueue.peek().getState();
//			currentState = Board.getBoard(curStateBmp);
		}
		if(isGoalState(currentState) && !m_priorityQueue.isEmpty()) {
			long st = currentState.bitMap();
			while(st != -1) {
				m_moveSeq.add(Board.getBoard(st));
				st = this.m_visitedStates.get(st);
			}
			//System.out.println(m_priorityQueue.poll());
		}
		return noNodesVisited;
	}
    
	private Stack<Board> m_moveSeq = new Stack<Board>();
	public void printMoveSeq() {
		while(!m_moveSeq.empty()) {
			System.out.println(m_moveSeq.pop());
			System.out.println();
		}
	}
	
	private boolean validMove(int x, int y, int dx, int dy) {
		int stepx = x + dx/2, stepy = y + dy/2,
				jumpx = x + dx, jumpy = y + dy;
		if(Board.invalidPos(stepx, stepy) || Board.invalidPos(jumpx, jumpy))
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
		if(state.get(3, 3) != Hole.PEG)
			return false;
		for(int i=0;i<Board.SIZE;i++)
			for(int j=0;j<Board.SIZE;j++)
				if(state.get(i, j)==Hole.PEG)
					pegCount++;
		if(1==pegCount)
			return true;
		return false;
	}
}
