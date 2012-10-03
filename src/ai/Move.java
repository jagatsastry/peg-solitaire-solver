package ai;

public class Move {
	int m_fromx, m_fromy, m_tox, m_toy;

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
		System.out.printf("Move (%d, %d) to (%d, %d)%n", m_fromx, m_fromy, m_tox, m_toy);
	}
}