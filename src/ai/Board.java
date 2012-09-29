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
	
	private Hole[][] m_board;
	
	public long bitMap() {
		long bitMap = 0; 
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++)
					bitMap = ((bitMap<<1) | m_board[i][j].bit());
		}
		return bitMap;
	}
	
	private List<Long> getRotateConfigs() {
		List<Long> rotations = new ArrayList<Long>(3);
		for(int i = 0; i < 3; i++) {
			rotate();
			rotations.add(bitMap());
		}
		rotate();
		return rotations;
	}
	
	private void rotate() {
		for(int i = 0; i < (SIZE)/2; i++) {
			for(int j = 0; j <= (SIZE-1)/2; j++) {
				
				Hole temp = m_board[i][j];
				m_board[i][j] = m_board[SIZE-j-1][i];
				m_board[SIZE-j-1][i] = m_board[SIZE-i-1][SIZE-j-1];
				m_board[SIZE-i-1][SIZE-j-1] = m_board[j][SIZE-i-1];
				m_board[j][SIZE-i-1] =temp; 
//				
//				Hole src = m_board[SIZE - j - 1][i];
//				m_board[SIZE - j - 1][i] = m_board[i][j];
//				Hole dest = m_board[SIZE - i - 1][SIZE - j - 1];
//				m_board[SIZE - i - 1][SIZE - j - 1] = src;
//				src = dest;
//				dest = m_board[SIZE -j - 1][SIZE - i - 1];
//				m_board[SIZE - j - 1][SIZE - i - 1] = src;
//				src = dest;
//				m_board[i][j] = src;
//			
				}
		}
	}
	
	private void verticalReflect() {
		 	for(int j = 0; j < SIZE/2; j++) 
				for(int i = 0; i < SIZE; i++){
				Hole temp = m_board[i][j];
				m_board[i][j] = m_board[i][SIZE - j -1];
				m_board[i][SIZE - j - 1] = temp;
			}
	}
	
	public List<Long> getSymmetricConfigs() { 
		List<Long> configs = new ArrayList<Long>();
		configs.add(bitMap());
		List<Long> rotations = getRotateConfigs();
		configs.addAll(rotations);
		verticalReflect();
		configs.add(bitMap());
		List<Long> reflectRotations = getRotateConfigs();
		configs.addAll(reflectRotations);
		verticalReflect();
		return configs;
		
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
