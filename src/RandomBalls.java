import java.util.Random;
import java.util.Vector;

public class RandomBalls {

	Random r;

	Vector v;

	int index;

	public RandomBalls() {

	}

	public void init() {
		r = new Random();
		v = new Vector();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int[] p = { i, j };
				v.addElement(p);
			}
		}
	}

	public int getRandomColor() {
		return r.nextInt() % 7;
	}

	public int[] getRandomBalls() {
		return getRandomNums(3, 6);
	}

	public int[] getFirstRandomBalls() {
		return getRandomNums(5, 6);
	}

	public int[] getPosition() {
		int i = r.nextInt() % v.size();
		if (i < 0) {
			i = -i;
		}
		index = i;

		return (int[]) v.elementAt(i);
	}

	public void removePosition() {

		v.removeElementAt(index);
	}

	public void removePosition(int x, int y) {

		int[] p;
		for (int i = 0; i < v.size(); i++) {
			p = (int[]) v.elementAt(i);
			if (p[0] == x && p[1] == y) {
				v.removeElementAt(i);
			}
		}
	}

	public void addPosition(int x, int y) {
		int[] p = { x, y };
		v.addElement(p);
	}

	public int[] getRandomNums(int s, int max) {
		int[] b = new int[s];
		for (int i = 0; i < s; i++) {
			b[i] = r.nextInt() % (max + 1);
			if (b[i] < 0) {
				b[i] = -b[i];
			}
		}
		return b;
	}
}
