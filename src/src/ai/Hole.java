package ai;
/**
 * 
 * Signifies a hole on the board
 *
 */
public enum Hole {
	
	INVALID('-', 0),
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
	
	int bit() {
		assert m_bit != -1;
		return m_bit;
	}
}