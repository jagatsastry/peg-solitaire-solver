package ai;

import java.util.ArrayList;
import java.util.List;

public class PriorityNode {
        
	    private List<Integer> pagodas = new ArrayList<Integer>(Pagoda.NUM_PAGODAS);
	    
		List<Integer> getPagodas() {
			return pagodas;
		}
		void setPagodas(List<Integer> pagodas) {
			this.pagodas = pagodas;
		}

		private int cost;
		private int distance;
		private Move m_move;
		
		public Move getMove() {
			return m_move;
		}
		
		public void setMove(Move mv) {
			m_move = mv;
		}
		
		@Override
		public String toString() {
			return "PriorityNode [cost=" + cost + ", distance=" + distance
					+ ", state=" + Board.getBoard(state) + "]";
		}
		
		private long state;
		private long m_prevState;
		
		
		public int getCost() {
			return cost;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
		
		public long getState() {
			return state; //states;
		}

		public void setState(long state) {
			this.state = state;
		}

		public long getPrevState() {
			return m_prevState; //states;
		}
		
		public void setPrevState(long m_prevState) {
			this.m_prevState = m_prevState;
		}

		public int getDistance() {
			return distance;
		}
		public void setDistance(int distance) {
			this.distance = distance;
		}
		
		
		
}
