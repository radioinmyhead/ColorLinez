public class CellInfo {

	boolean exist = false;

	int color = -1;

	int parentX = -1;

	int parentY = -1;

	int x;

	int y;

	int g;

	public CellInfo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setParent(int parentX, int parentY) {
		this.parentX = parentX;
		this.parentY = parentY;
	}

}
