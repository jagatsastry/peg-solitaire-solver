package ai;

public class Pagoda {

	private static int pagodaFunction[][] = { { 0, 0, 1, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 1, 0, 1, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0 }, {  1, 0, 1, 0, 1, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 1, 0, 0 } };

	public static int evaluatePagoda(Board board) {
		int pagodaValue = 0;
		for (int i = 0; i < Board.SIZE; i++) {
			for (int j = 0; j < Board.SIZE; j++) {
				if (board.get(i, j) == Hole.PEG) {
					pagodaValue += pagodaFunction[i][j];
				}
			}
		}
		return pagodaValue;
	}
	
	public static int evaluateHeutristic(int x,int y,int dx,int dy){
		
		int value=0;
		
		if(x<=2 && dx-x>0 && dy==0)
			value+=2;
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
	
	/*public static int evaluateHeutristic(Board b){
		
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
			
		
	}*/
	
	

}
