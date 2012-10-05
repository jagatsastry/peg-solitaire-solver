package ai;

import java.text.MessageFormat;
import java.util.Formatter;
/**
 * Used to store a jump on the board.
 */
public class Move {
	int m_fromx, m_fromy, m_tox, m_toy; 
	
    @Override public String toString() {
    	return MessageFormat.format("{0}({1}, {2}) -> {3}({4}, {5})", 
    			Board.getPosIndex(m_fromx, m_fromy), m_fromx, m_fromy,
    			Board.getPosIndex(m_tox, m_toy), m_tox, m_toy);
    }
    
	public Move(int fromx, int fromy, int tox, int toy) {
		super();
		this.m_fromx = fromx;
		this.m_fromy = fromy;
		this.m_tox = tox;
		this.m_toy = toy;
	}

	int fromx() {
		return m_fromx;
	}

	int fromy() {
		return m_fromy;
	}

	int tox() {
		return m_tox;
	}

	int toy() {
		return m_toy;
	}
	
	int dx() {
		return m_tox - m_fromx;
	}
	
	int dy() {
		return m_toy - m_fromy;
	}

	public void printMove() {
		System.out.println(this.toString());
	//	System.out.printf("Move (%d, %d) to (%d, %d)%n", m_fromx, m_fromy, m_tox, m_toy);
	}
	
	
}