package ai;

import java.util.Comparator;

public class CostComparator implements Comparator<PriorityNode> {

	@Override
	public int compare(PriorityNode arg0, PriorityNode arg1) {
		// TODO Auto-generated method stub
		return arg0.getCost() - arg1.getCost();
	}

}
