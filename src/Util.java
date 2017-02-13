import javax.microedition.lcdui.Image;

public class Util {
	public static int[] IntToIntArray(int a) {
		String sTemp = "" + a;
		int[] b = new int[sTemp.length()];
		for (int i = 0; i < sTemp.length(); i++) {
			b[i] = Integer.parseInt(sTemp.substring(i, i + 1));
		}
		return b;
	}

	public static Image splitImage(Image image, int x, int y, int w, int h) {
		Image image1 = Image.createImage(w, h);
		image1.getGraphics().drawImage(image, -x, -y, 20);
		return Image.createImage(image1);
	}
}
