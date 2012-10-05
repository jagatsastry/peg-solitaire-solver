package ai;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * Contains the definitions of the Pagoda fucntions used for pruning
 *
 */
public class Pagoda {

	public static final int NUM_PAGODAS = 6;
	private static List<int[][]> pagodaFunctions = new ArrayList<int[][]>() {
		
		private static final long serialVersionUID = 1L;


		{add( new int[][]{ 
				  { 0,  0, 0, 0, 0, 0,  0 },
				  { 0,  0, 0, 1, 0, 0,  0 }, 
				  { -1, 1, 0, 1, 0, 1, -1 },
				  { 0,  0, 0, 0, 0, 0,  0 }, 
				  { -1, 1, 0, 1, 0, 1, -1 },
				  { 0,  0, 0, 1, 0, 0,  0 }, 
				  { 0,  0, 0, 0, 0, 0,  0 }}); } 
		

	   { add( new int[][]{ 
			  { 0, 0, 1, 0, 1, 0, 0 },
			  { 0, 0, 0, 0, 0, 0, 0 }, 
			  { 1, 0, 1, 0, 1, 0, 1 },
			  { 0, 0, 0, 0, 0, 0, 0 }, 
			  { 1, 0, 1, 0, 1, 0, 1 },
			  { 0, 0, 0, 0, 0, 0, 0 }, 
			  { 0, 0, 1, 0, 1, 0, 0 }}); }

		{add( new int[][]{ 
				  { 0, 0, 0, 1, 0, 0, 0 },
				  { 0, 0, 0, 0, 0, 0, 0 }, 
				  { 0, 1, 0, 1, 0, 1, 0 },
				  { 0, 0, 0, 0, 0, 0, 0 }, 
				  { 0, 1, 0, 1, 0, 1, 0 },
				  { 0, 0, 0, 0, 0, 0, 0 }, 
				  { 0, 0, 0, 1, 0, 0, 0 }}); } 
		
		{add( new int[][]{ 
				  { 0, 0, -1, 0, -1, 0, 0 },
				  { 0, 0,  1, 0, 1, 0, 0 }, 
				  { 0, 0, 0, 0, 0, 0, 0 },
				  { 0, 1, 1, 0, 1, 1, 0 }, 
				  { 0, 0, 0, 0, 0, 0, 0 },
				  { 0, 0, 1, 0, 1, 0, 0 }, 
				  { 0, 0, -1, 0, -1, 0, 0 }}); } 
		

		{add( new int[][]{ 
			  { 0, 0, 0, 0, 0, 0, 0 },
			  { 0, 0, 0, 1, 0, 0, 0 }, 
			  { 0, 0, 0, 0, 0, 0, 0 },
			  { 0, 1, 0, 1, 0, 1, 0 }, 
			  { 0, 0, 0, 0, 0, 0, 0 },
			  { 0, 0, 0, 1, 0, 0, 0 }, 
			  { 0, 0, 0, 0, 0, 0, 0 }}); } 

	
		{add( new int[][]{ 
				  { 0,  0, 0, 0, 0, 0,  0 },
				  { 0,  0, 1, 0, 1, 0,  0 }, 
				  { 0,  0, 0, 0, 0, 0,  0 },
				  { 1,  0, 1, 0, 1, 0,  1 }, 
				  { 0,  0, 0, 0, 0, 0,  0 },
				  { 0,  0, 1, 0, 1, 0,  0 }, 
				  { 0,  0, 0, 0, 0, 0,  0 }}); } 
		
	};

	public static List<Integer> evaluatePagodas(Board board) {
		List<Integer> pagodas = new ArrayList<Integer>();
		for(int k = 0; k < pagodaFunctions.size(); k++) {
			
			pagodas.add(evaluatePagoda(board, k));
		}
		return pagodas;
	}
	
	private static int evaluatePagoda(Board board, int idx) {
		return Heuristics.evaluateCostMatrix(board, pagodaFunctions.get(idx));
	}

	public static int evaluatePagoda(Board board) {
		return evaluatePagodas(board).get(1);
	}
	
	public static int evaluateHeutristic(int x,int y,int dx,int dy){
		
		int value=0;
		
		if(x<=2 && dx == 2 && dy==0)
			value = 2;
		else if(x>4 && x-dx>0 && dy==0)
			value+=2;
		else if(y<=2 && dy-y>0 && dx==0)
			value+=2;
		else if(y>=4 && y-dy>0 && dx==0)
			value+=2;
		else
			value+=1;
		return value; 
	}
	
	

}
