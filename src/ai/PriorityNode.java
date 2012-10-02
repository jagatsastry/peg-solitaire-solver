package ai;

import java.util.LinkedList;


public class PriorityNode {

		private int cost;
		private int distance;
		private LinkedList<Board> states;
		
		
		public int getCost() {
			return cost;
		}
		public void setCost(int cost) {
			this.cost = cost;
		}
		public LinkedList<Board> getStates() {
			return states;
		}
		public void setStates(LinkedList<Board> states) {
			this.states = states;
		}
		public int getDistance() {
			return distance;
		}
		public void setDistance(int distance) {
			this.distance = distance;
		}
		
		
		
}
