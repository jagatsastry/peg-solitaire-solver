package ai;

import java.util.Comparator;

/**
 * Comparator used by priority node. Gives preference to node having smaller
 * cost. If cost is same it gives preference to the distance from the goal
 * 
 * 
 */
public class CostComparator implements Comparator<PriorityNode> {

	@Override
	public int compare(PriorityNode arg0, PriorityNode arg1) {
		// TODO Auto-generated method stub
		int c1 = arg0.getCost();
		int c2 = arg1.getCost();
		if (c1 == c2)
			return arg1.getDistance() - arg0.getDistance();
		return c1 - c2;
	}

}
