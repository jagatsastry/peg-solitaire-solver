/**
 * 
 */
package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;


/**
 * Contains the DFS and A* algorithm. Also contains other utility methods. For
 * details on how the algorithm is implemented refer report.
 * 
 */
public class PegSolitaireSolver {
    private static final boolean RANDOMIZE_DFS = false;
    private static final boolean RANDOMIZE_ASTAR = false;
	private Heuristic m_heuristic = Heuristic.WEIGHTED_COST;
	private boolean m_pagodaPrune = true;
	private boolean m_symmetryPrune = true;
	
	private long m_timeTaken;
	
	public void setHeuristic(Heuristic heuristic) {
		m_heuristic = heuristic;
	}

	private Board m_origBoard;
	private Board m_board;
	private Set<Long> m_unsolvableStates = new HashSet<Long>();
	private Map<Long, PriorityNode> m_visitedStates = new HashMap<Long, PriorityNode>();
	private LinkedList<Move> m_moves = new LinkedList<Move>();
	private static CostComparator m_cc = new CostComparator();
	private PriorityQueue<PriorityNode> m_priorityQueue = new  PriorityQueue<PriorityNode>(11,m_cc);
	public int m_numExpandedStates=0;

	public PegSolitaireSolver(Board board) {
		m_board = board;
		m_origBoard = m_board.copyBoard();
	}
	
	public int numExpandedStates() {
		return m_numExpandedStates;
	}

	void printSteps() {
		Board cpy = m_origBoard.copyBoard();
		LinkedList<Move> moveCopy = new LinkedList<Move>(m_moves);
		//System.out.println(cpy);
        while(!moveCopy.isEmpty()) {
        	Move mv = moveCopy.pop();
        	mv.printMove();
        	cpy.move(mv);
        	//System.out.println(cpy);
        }
	}
    
	int[][] deltas = new int[][]{{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
	
	/**DFS solver*/
	boolean dfs() {
		long startTime = System.currentTimeMillis();
		try {
			int pegCount = 0;
			
			List<Integer> iterOrder = new ArrayList<Integer>(Board.SIZE);
			List<Integer> deltaOrder = new ArrayList<Integer>(4); //new int[4];
			int idx = 0;
			for(int i = 0; i<Board.SIZE; i++) 
				iterOrder.add(idx++);
			
			idx = 0;
			for(int i = 0; i<4; i++) 
				deltaOrder.add(idx++);
			
			if(RANDOMIZE_DFS) {
				Collections.shuffle(iterOrder);
				Collections.shuffle(deltaOrder);
			}
	
			for(int x: iterOrder) 
				for(int y: iterOrder) {
					if(this.m_board.get(x, y) == Hole.PEG) {
						pegCount++; 
						for(int index: deltaOrder) {
							int[] delta = deltas[index];
							int dx = delta[0];
							int dy = delta[1];
							if(validMove(x, y, dx, dy)) {
								m_board.move(x, y, dx, dy);
								Long boardCfg = m_board.bitMap();
								
								List<Integer> pagodas = Pagoda.evaluatePagodas(m_board);
								if(this.m_pagodaPrune && (pagodas.get(0) < 0 || pagodas.get(4) < 1)) {
								//	System.out.println("Pagoda alert!!!");
								}
	
								else 
									if(!m_unsolvableStates.contains(boardCfg)) {
									m_numExpandedStates++;
									
									if(dfs()) {
										m_moves.push(new Move(x, y, x + dx, y + dy));
										return true;
									}
								}
								if(this.m_symmetryPrune)
									m_unsolvableStates.addAll(m_board.getSymmetricConfigs());
								m_board.restore(x, y, dx, dy);
							}
						}
					}
				}
		} finally {
			this.m_timeTaken = System.currentTimeMillis() - startTime;
		}
		return isGoalState(m_board);
	}
	
	public static enum Heuristic { 
		PAGODA{
			public String toString() {
				return ("Pagoda Function Heuristic");
			}
		}
		, 
		MANHATTAN{
			public String toString() {
				return ("Manhattan Distance Heuristic");
			}
		}
		, 
		WEIGHTED_COST {
			public String toString() {
				return ("Weighted Position Heuristic");
			}
		}
		
		
	};
	
	private int getCost() {
		switch(m_heuristic) {
		case PAGODA:
			return Pagoda.evaluatePagoda(m_board);
		case MANHATTAN:
			return Heuristics.manhattanCost(m_board);
			//return 0; //TODO
		case WEIGHTED_COST:
			return Heuristics.weightedCost(m_board);
		//case 
		}
		return 0;
	}
	
	/**Solve using A-Star*/
	boolean aStar(){
		long startTime = System.currentTimeMillis();
		try {
			m_numExpandedStates = 0;
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
				m_visitedStates.put(st, root);
	        
			int prevX = 0; 
			//int prevX = (Board.SIZE - 1)/2;
			int prevY = 0; 
			//int prevY = (Board.SIZE - 1)/2;
			root.setMove(new Move(prevX, prevY, prevX, prevY));
			while(!isGoalState(currentState) && !m_priorityQueue.isEmpty()){
	//			System.out.println(noNodesVisited++);
				m_numExpandedStates++;
				long curStateBmp = m_priorityQueue.peek().getState();
				currentState = Board.getBoard(curStateBmp);
				PriorityNode curNode = m_priorityQueue.poll();
				distance = curNode.getDistance();
	//			Long bcf = currentState.bitMap();
	//			m_visitedStates.addAll(currentState.getSymmetricConfigs());	
	//			if(m_visitedStates.contains(currentState.bitMap()))
	//				continue;
				List<Integer> xOrder = new ArrayList<Integer>(Board.SIZE);
				List<Integer> yOrder = new ArrayList<Integer>(Board.SIZE);
				List<Integer> deltaOrder = new ArrayList<Integer>(4); //new int[4];
				int idxx = prevX; 
				//int idxx = curNode.getMove().tox();
				for(int i = 0; i<Board.SIZE; i++) 
					xOrder.add(idxx++%Board.SIZE);
	
				int idxy = prevY; 
				//int idxy = curNode.getMove().toy();
				for(int i = 0; i<Board.SIZE; i++) 
					yOrder.add(idxy++%Board.SIZE);
				
				int idx = 0;
				for(int i = 0; i<4; i++) 
					deltaOrder.add(idx++);
				
				if(RANDOMIZE_ASTAR) {
					Collections.shuffle(xOrder);
					Collections.shuffle(yOrder);
					Collections.shuffle(deltaOrder);
				}
				for(int x: xOrder) 
					for(int y: yOrder){
						if(currentState.get(x, y) == Hole.PEG){	
							for(int index: deltaOrder) {
								m_board= currentState.copyBoard();
								int dx = deltas[index][0];
								int dy = deltas[index][1];
								if(validMove(x, y, dx, dy)){
									Move mv = new Move(x, y, x + dx, y + dy);
									m_board.move(mv);
									
									Long boardSt = m_board.bitMap();
									if(m_visitedStates.get(boardSt) != null)
										continue;
									int pgCost = getCost();
									
									PriorityNode pn=new PriorityNode();
									pn.setState(boardSt);
									pn.setPrevState(curStateBmp);
									pn.setDistance(++distance);
									pn.setMove(mv);
									pn.setCost(pgCost);
									--distance;
									m_visitedStates.put(boardSt, pn);
									if(this.m_symmetryPrune)
										for(Long st: m_board.getSymmetricConfigs()) 
											m_visitedStates.put(st, pn);
	
									List<Integer> pagodas = Pagoda.evaluatePagodas(m_board);
									if(this.m_pagodaPrune && (pagodas.get(0) < 0 || pagodas.get(4) < 1)) {
									//	System.out.println("Pagoda alert!!!");
										continue;
									}
	
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
				while(m_visitedStates.get(st).getPrevState() != -1) {
					PriorityNode pn = m_visitedStates.get(st);
					assert pn != null;
					this.m_moves.push(pn.getMove());
					st = this.m_visitedStates.get(st).getPrevState();
				}
			}
	//			while(st != -1) {
	//				m_moveSeq.add(Board.getBoard(st));
	//				st = this.m_visitedStates.get(st).getPrevState();
	//			}
				//System.out.println(m_priorityQueue.poll());
			return isGoalState(currentState);

		} finally {
			this.m_timeTaken = System.currentTimeMillis() - startTime;
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
	
	public List<List<Integer>> getCompressedPaths() {
		List<List<Integer>> compressedMoves = new LinkedList<List<Integer>>();
		if(m_moves.isEmpty()) {
			return compressedMoves;
		}
		LinkedList<Move> moves = new LinkedList<Move>(this.m_moves);

		Move mv = moves.pop();
		int curSrc = Board.getPosIndex(mv.fromx(), mv.fromy());
		int curDest = Board.getPosIndex(mv.tox(), mv.toy());
		List<Integer> contMoves = new LinkedList<Integer>();
		contMoves.add(curSrc);
		int prevDest = curDest;
		while(!moves.isEmpty()) {
			mv = moves.pop();
			curSrc = Board.getPosIndex(mv.fromx(), mv.fromy());
			curDest = Board.getPosIndex(mv.tox(), mv.toy());

			contMoves.add(prevDest);
			
			if(curSrc != prevDest) {
				compressedMoves.add(contMoves);
				contMoves = new LinkedList<Integer>();
				contMoves.add(curSrc);
			}
			
			//if(prevDest != curSrc) 
			prevDest = curDest;
			//contMoves.add(prevDest = curDest);
		}
		contMoves.add(curDest);
		compressedMoves.add(contMoves);
		return compressedMoves;
	}
	
	private static final long MEGABYTE = 1024L * 1024L;
	public void printSummary(String method) {

		System.out.println("-----------------------");
		System.out.println("Summary of peg-solitaire solution attempt using " + method);
		System.out.println("-----------------------");
		List<List<Integer>> paths = this.getCompressedPaths();
		System.out.println("****Moves****");
		for(List<Integer> move: paths) {
			System.out.println(move);
		}
		System.out.println("-----------------------");
		System.out.println("Number of states expanded: " + this.m_numExpandedStates);
		System.out.println("Number of moves: " + paths.size());
		System.out.printf("Time taken: %.4fms%n", (float)this.m_timeTaken);
		Runtime rt = Runtime.getRuntime();
		System.out.printf("Memory used: %.2f MB%n", (rt.totalMemory() - rt.freeMemory())/(float)MEGABYTE);
		System.out.println("****End of Summary****");
		System.out.println();
		
	}
}
