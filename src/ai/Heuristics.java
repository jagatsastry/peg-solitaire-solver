package ai;

public class Heuristics {

	public static int manhattanCost(Board b){
		
		int value=0;
		int manDistance=0;
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				if(b.get(i, j) == Hole.PEG){
					manDistance=Math.abs(i-3)+Math.abs(j-3);
					value+=manDistance;
				}
			}
		}
		return value;
			
		
	}
	
	public static int weightedCost(Board m_board) {
		   int[][] costMatrix =  new int[][]{ 
					  { 0, 0, 4, 0, 4, 0, 0 },
					  { 0, 0, 0, 0, 0, 0, 0 }, 
					  { 4, 0, 3, 0, 3, 0, 4 },
					  { 0, 0, 0, 1, 0, 0, 0 }, 
					  { 4, 0, 3, 0, 3, 0, 4 },
					  { 0, 0, 0, 0, 0, 0, 0 }, 
					  { 0, 0, 4, 0, 4, 0, 0 }}; 
         return evaluateCostMatrix(m_board, costMatrix);

	}

	public static int evaluateCostMatrix(Board board, int[][] costMatrix ) {
		int pagodaValue = 0;
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				if (board.get(i, j) == Hole.PEG) {
					pagodaValue += costMatrix[i][j];
					
				}
			}
		}
		return (pagodaValue);
	}

}
