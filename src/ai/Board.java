package ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ai.Board.SlotState;


public class Board {
    
	public static enum SlotState { INVALID, EMPTY, PEG};
	
	private SlotState[][] m_board;
	public static final int SIZE = 7;
	
	private Board(SlotState[][] slots) {
		m_board = slots;
	}
	
	public static Board inputBoard() throws IOException {
		Board board = new Board(new SlotState[7][7]);
		List<String> boardList = new ArrayList<String>(7);
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		for(int i = 0; i < SIZE; i ++) {
			for(int j = 0; j < SIZE; j++) {
				boardList.add(buff.readLine());
			}
		}
		board.setBoard(boardList);
		
		return board;
	}

	private void setBoard(List<String> boardList) {
		// TODO Auto-generated method stub
		for(int i = 0; i < SIZE ; i++) {
			char[] row = boardList.get(i).toCharArray();
			for(int j = 0; j < SIZE; j++) {
				m_board[i][j] = SlotState.fromChar(row[j]);
			}
		}
				
		
	}

}
