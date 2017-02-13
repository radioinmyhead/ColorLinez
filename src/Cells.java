public class Cells {
	CellInfo[][] cells = new CellInfo[9][9];

	public Cells() {
		init();
	}

	public void init() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				cells[i][j] = new CellInfo(i, j);
			}
		}
	}
}
