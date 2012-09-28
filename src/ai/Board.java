package ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board {
    
	public Hole get(int i, int j) {
		return m_board[i][j];
	}
	
	@Override public String toString() {
		StringBuilder build = new StringBuilder();
		for(Hole[] row: m_board)
			build.append(Arrays.toString(row) + "\n");
		return build.toString();
	}
	
	public static enum Hole {
		
		INVALID('-', -1),
		EMPTY('0', 0),
		PEG('X', 1);
		
		private char m_inpchar;
		private int m_bit;
		
		private Hole(char inpchar, int bit) {
			m_inpchar = inpchar;
			m_bit = bit;
		}
		
		static Hole fromChar(char c) {
			for(Hole s: values()) 
				if(s.getInpChar() == c)
					return s;
			return null;
		}

		private char getInpChar() {
			// TODO Auto-generated method stub
			return m_inpchar;
		}
		
		@Override public String toString() {
			return String.valueOf(m_inpchar);
		}
		
		private int bit() {
			assert m_bit != -1;
			return m_bit;
		}
	};
	
	private Hole[][] m_board;
	
	public long bitMap() {
		long bitMap = 0; 
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++)
				if(m_board[i][j] != Hole.INVALID)
					bitMap = ((bitMap<<1) | m_board[i][j].bit());
		}
		return bitMap;
	}
	
	public List<Long> getSymmetricConfigs() { 
		//TODO: Perform rotations and reflections
		List<Long> retVal = new ArrayList<Long>();
		retVal.add(bitMap());
		return retVal;
//		
//		for(int i = 0; i < SIZE; i++) 
//			for(int j = 0; j < SIZE; j++) {
//              				
//			}
	}
	
	public static final int SIZE = 7;
	
	private Board(Hole[][] slots) {
		m_board = slots;
	}
	
    public static Board inputBoard() throws IOException {
		List<String> boardList = new ArrayList<String>(7);
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		for(int i = 0; i < SIZE; i ++) {
			for(int j = 0; j < SIZE; j++) {
				boardList.add(buff.readLine());
			}
		}
		
		return getBoard(boardList);
	}

	public static Board getBoard(List<String> boardList) {
		// TODO Auto-generated method stub
		Hole[][] arrayBoard = new Hole[SIZE][SIZE];
		for(int i = 0; i < SIZE ; i++) {
			char[] row = boardList.get(i).toCharArray();
			for(int j = 0; j < SIZE; j++) {
				arrayBoard[i][j] = Hole.fromChar(row[j]);
			}
		}
		return new Board(arrayBoard);
	}

	//Assumption: The move has been verified as valid
	public void move(int x, int y, int dx, int dy) {
		m_board[x][y] = Hole.EMPTY;
		m_board[x + dx/2][y + dy/2] = Hole.EMPTY;
		m_board[x + dx][y + dy] = Hole.PEG;
	}

	public void restore(int x, int y, int dx, int dy) {
		m_board[x + dx][y + dy] = Hole.EMPTY;
		m_board[x + dx/2][y + dy/2] = Hole.PEG;
		m_board[x][y] = Hole.PEG;
	}

}
