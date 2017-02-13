import java.util.Vector;

public class AStar {

	CellInfo[][] cells;

	int startX;

	int startY;

	int goldX;

	int goldY;

	Vector vOpen = new Vector();

	Vector vClosed = new Vector();

	public void setCells(Cells cells) {
		this.cells = cells.cells;
	}

	public void setStart(int x, int y) {
		startX = x;
		startY = y;
	}

	public void setGold(int x, int y) {
		goldX = x;
		goldY = y;
	}

	public boolean findPath() {
		vOpen.removeAllElements();
		vClosed.removeAllElements();
		cells[startX][startY].g = 0;
		int[] xy = { startX, startY };
		vOpen.addElement(xy);
		while (vOpen.size() != 0) {
			xy = (int[]) vOpen.elementAt(0);
			CellInfo cell = cells[xy[0]][xy[1]];
			if (cell.x - 1 >= 0 && !inClosed(cell.x - 1, cell.y)
					&& !cells[cell.x - 1][cell.y].exist) {
				if (cell.x - 1 == goldX && cell.y == goldY) {
					cells[cell.x - 1][cell.y].setParent(cell.x, cell.y);
					return true;
				} else {
					addOpenElement(cell.x - 1, cell.y, cell.x, cell.y);
				}
			}
			if (cell.x + 1 <= 8 && !inClosed(cell.x + 1, cell.y)
					&& !cells[cell.x + 1][cell.y].exist) {
				if (cell.x + 1 == goldX && cell.y == goldY) {
					cells[cell.x + 1][cell.y].setParent(cell.x, cell.y);
					return true;
				} else {
					addOpenElement(cell.x + 1, cell.y, cell.x, cell.y);
				}
			}
			if (cell.y - 1 >= 0 && !inClosed(cell.x, cell.y - 1)
					&& !cells[cell.x][cell.y - 1].exist) {
				if (cell.x == goldX && cell.y - 1 == goldY) {
					cells[cell.x][cell.y - 1].setParent(cell.x, cell.y);
					return true;
				} else {
					addOpenElement(cell.x, cell.y - 1, cell.x, cell.y);
				}
			}
			if (cell.y + 1 <= 8 && !inClosed(cell.x, cell.y + 1)
					&& !cells[cell.x][cell.y + 1].exist) {
				if (cell.x == goldX && cell.y + 1 == goldY) {
					cells[cell.x][cell.y + 1].setParent(cell.x, cell.y);
					return true;
				} else {
					addOpenElement(cell.x, cell.y + 1, cell.x, cell.y);
				}
			}
			vClosed.addElement(xy);
			vOpen.removeElementAt(0);
		}
		return false;
	}

	public void addOpenElement(int x, int y, int px, int py) {
		if (inOpen(x, y) && cells[x][y].g > cells[px][py].g + 1) {
			int[] xy;
			for (int i = 0; i < vOpen.size(); i++) {
				xy = (int[]) vOpen.elementAt(i);
				if (xy[0] == x && xy[1] == y) {
					vOpen.removeElementAt(i);
				}
			}
			insertVOpen(x, y, px, py);
		} else if (!inOpen(x, y)) {
			insertVOpen(x, y, px, py);
		}
	}

	public void insertVOpen(int x, int y, int px, int py) {
		int f1, f2;
		int offsetX, offsetY;
		boolean flag = false;
		int[] xy = { x, y };
		cells[x][y].g = cells[px][py].g + 1;
		cells[x][y].setParent(px, py);
		offsetX = (goldX - x) < 0 ? x - goldX : goldX - x;
		offsetY = (goldY - y) < 0 ? y - goldY : goldY - y;
		f1 = cells[x][y].g + offsetX + offsetY;
		for (int i = 0; i < vOpen.size(); i++) {
			int[] tmp = (int[]) vOpen.elementAt(i);
			CellInfo tmpCell = cells[tmp[0]][tmp[1]];
			offsetX = (goldX - tmp[0]) < 0 ? tmp[0] - goldX : goldX - tmp[0];
			offsetY = (goldY - tmp[1]) < 0 ? tmp[1] - goldY : goldY - tmp[1];
			f2 = tmpCell.g + offsetX + offsetY;
			if (f1 < f2) {
				vOpen.insertElementAt(xy, i);
				flag = true;
				break;
			}
		}
		if (!flag) {
			vOpen.addElement(xy);
		}
	}

	public boolean inClosed(int x, int y) {
		int[] xy;
		for (int i = 0; i < vClosed.size(); i++) {
			xy = (int[]) vClosed.elementAt(i);
			if (xy[0] == x && xy[1] == y) {
				return true;
			}
		}
		return false;
	}

	public boolean inOpen(int x, int y) {
		int[] xy;
		for (int i = 0; i < vOpen.size(); i++) {
			xy = (int[]) vOpen.elementAt(i);
			if (xy[0] == x && xy[1] == y) {
				return true;
			}
		}
		return false;
	}

	public Vector getPath() {
		Vector path = new Vector();
		int x = goldX;
		int y = goldY;
		int px;
		int py;
		int[] xy = { x, y };
		path.addElement(xy);
		for (int i = 0; i < 81; i++) {
			px = cells[x][y].parentX;
			py = cells[x][y].parentY;
			if (px == startX && py == startY) {
				break;
			} else {
				int[] xy1 = { px, py };
				path.insertElementAt(xy1, 0);
				x = px;
				y = py;
			}
		}
		return path;
	}

}
