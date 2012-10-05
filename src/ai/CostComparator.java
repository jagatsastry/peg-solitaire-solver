package ai;

import java.util.Comparator;

public class CostComparator implements Comparator<PriorityNode> {

	@Override
	public int compare(PriorityNode arg0, PriorityNode arg1) {
		// TODO Auto-generated method stub
		int c1 = arg0.getCost();
		int c2 = arg1.getCost();
		if(c1 == c2) 
			return arg1.getDistance() - arg0.getDistance();
		return c1 - c2;
	}

}
