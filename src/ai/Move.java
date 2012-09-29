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

	public void printMove() {
		System.out.printf("Move (%d, %d) to (%d, %d)%n", m_fromx, m_fromy, m_tox, m_toy);
	}
}