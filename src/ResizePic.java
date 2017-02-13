import javax.microedition.lcdui.Image;

public class ResizePic {

	public static Image ZoomImage(Image src, int desW, int desH) {
		Image desImg = null;
		int srcW = src.getWidth(); // 原始图像宽
		int srcH = src.getHeight(); // 原始图像高
		int[] srcBuf = new int[srcW * srcH]; // 原始图片像素信息缓存

		src.getRGB(srcBuf, 0, srcW, 0, 0, srcW, srcH);

		// 计算插值表
		int[] tabY = new int[desH];
		int[] tabX = new int[desW];

		int sb = 0;
		int db = 0;
		int tems = 0;
		int temd = 0;
		int distance = srcH > desH ? srcH : desH;
		for (int i = 0; i <= distance; i++) { /* 垂直方向 */
			tabY[db] = sb;
			tems += srcH;
			temd += desH;
			if (tems > distance) {
				tems -= distance;
				sb++;
			}
			if (temd > distance) {
				temd -= distance;
				db++;
			}
		}

		sb = 0;
		db = 0;
		tems = 0;
		temd = 0;
		distance = srcW > desW ? srcW : desW;
		for (int i = 0; i <= distance; i++) { /* 水平方向 */
			tabX[db] = (short) sb;
			tems += srcW;
			temd += desW;
			if (tems > distance) {
				tems -= distance;
				sb++;
			}
			if (temd > distance) {
				temd -= distance;
				db++;
			}
		}

		// 生成放大缩小后图形像素buf
		int[] desBuf = new int[desW * desH];
		int dx = 0;
		int dy = 0;
		int sy = 0;
		int oldy = -1;
		for (int i = 0; i < desH; i++) {
			if (oldy == tabY[i]) {
				System.arraycopy(desBuf, dy - desW, desBuf, dy, desW);
			} else {
				dx = 0;
				for (int j = 0; j < desW; j++) {
					desBuf[dy + dx] = srcBuf[sy + tabX[j]];
					dx++;
				}
				sy += (tabY[i] - oldy) * srcW;
			}
			oldy = tabY[i];
			dy += desW;
		}

		// 生成图片
		desImg = Image.createRGBImage(desBuf, desW, desH, false);
		return desImg;
	}

	private static int[] getPixels(Image src) {
		int w = src.getWidth();
		int h = src.getHeight();
		int[] pixels = new int[w * h];
		src.getRGB(pixels, 0, w, 0, 0, w, h);
		return pixels;
	}

	private static Image drawPixels(int[] pixels, int w, int h) {
		Image image = Image.createRGBImage(pixels, w, h, true);
		pixels = null;
		return image;
	}

	public static Image resizeImage(Image src, int destW, int destH) {
		int srcW = src.getWidth();
		int srcH = src.getHeight();

		int[] destPixels = new int[destW * destH];

		int[] srcPixels = getPixels(src);

		for (int destY = 0; destY < destH; ++destY) {
			for (int destX = 0; destX < destW; ++destX) {
				int srcX = (destX * srcW) / destW;
				int srcY = (destY * srcH) / destH;
				destPixels[destX + destY * destW] = srcPixels[srcX + srcY
						* srcW];
			}
		}
		return drawPixels(destPixels, destW, destH);
	}

}
