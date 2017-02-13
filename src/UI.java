import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

public class UI {

	private int canW;

	private int canH;

	private Graphics g;

	LayerManager lm = new LayerManager();

	Cells cells = new Cells();

	RandomBalls rb = new RandomBalls();

	AStar astar = new AStar();

	RMS rms = new RMS();

	Image[] imgSmallBalls = new Image[7];

	Image[] imgBigBalls = new Image[7];

	Image[] imgNums = new Image[10];

	Image[] imgZb = new Image[10];

	Sprite[] spriteBigBalls = new Sprite[7];

	Image[] imgAim = new Image[2];

	Image imgBG;

	Image imgMenu;

	Image imgGameover;

	Image imgCell;

	Image imgLogo;

	Image imgConfirm;

	boolean bgflag = true;

	boolean gameover = false;

	boolean inputflag = true;

	boolean flushball = false;

	boolean menuflag = false;

	boolean menuflagchoose = false;

	int[] s = { 0, 1, 2, 1 };

	int offset_x = 0;

	int offset_y = 0;

	int bgW = 225;

	int bgH = 288;

	int offset_cell_x = 0;

	int offset_cell_y = 38;

	int offset_next_x = 75;

	int offset_next_y = 10;

	int offset_bestscore_x = 65;

	int offset_bestscore_y = 18;

	int offset_nowscore_x = 215;

	int offset_nowscore_y = 18;

	int offset_focus_x = 121;

	int offset_focus_y = 268;

	int aim_x = 0;

	int aim_y = 0;

	int bestscore = 100;

	int nowscore = 0;

	int[] threenewballs = new int[3];

	int sum = 0;

	int currentball_x = -1;

	int currentball_y = -1;

	int moveIndex = 0;

	int center_x;

	int center_y;

	int temp_x;

	int temp_y;

	int status = 0;

	int input_x = -1;

	int input_y = -1;

	Vector path;

	public UI(Graphics g, int canW, int canH) {
		this.g = g;
		this.canW = canW;
		this.canH = canH;
		init();
	}

	public void init() {
		offset_x = (canW - bgW) / 2;
		center_x = canW / 2;
		center_y = canH / 2;
		try {
			imgBG = Image.createImage("/bg.png");
			imgMenu = Image.createImage("/menu.png");
			imgGameover = Image.createImage("/gameover.png");
			imgCell = Image.createImage("/cell.png");
			imgLogo = Image.createImage("/logo.png");
			imgConfirm = Image.createImage("/restart.png");
			imgBigBalls[0] = Image.createImage("/red.png");
			imgBigBalls[1] = Image.createImage("/purple.png");
			imgBigBalls[2] = Image.createImage("/coffee.png");
			imgBigBalls[3] = Image.createImage("/yellow.png");
			imgBigBalls[4] = Image.createImage("/green.png");
			imgBigBalls[5] = Image.createImage("/skyblue.png");
			imgBigBalls[6] = Image.createImage("/blue.png");
			for (int i = 0; i < 2; i++) {
				imgAim[i] = Image.createImage(Image.createImage("/aim.png"), 0,
						i * 25, 25, 25, 0);
			}
			for (int i = 0; i < 7; i++) {
				imgSmallBalls[i] = Image.createImage(Image
						.createImage("/sballs.png"), i * 11, 0, 11, 11, 0);
			}
			for (int i = 0; i < 10; i++) {
				imgZb[i] = Image.createImage(Image.createImage("/zb.png"),
						i * 7, 0, 7, 7, 0);
			}
			for (int i = 0; i < 10; i++) {
				imgNums[i] = Image.createImage(Image
						.createImage("/numbers.png"), i * 8, 0, 8, 9, 0);
			}
			for (int i = 0; i < 7; i++) {
				spriteBigBalls[i] = new Sprite(imgBigBalls[i], 25, 25);
				spriteBigBalls[i].setFrameSequence(s);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void createUI() {
		g.setColor(0x33670a);
		g.fillRect(0, 0, canW, canH);
		g.setColor(0xffffff);

		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
				Font.SIZE_SMALL));
		g.drawString("QQ:7527372", center_x, canH, Graphics.HCENTER
				| Graphics.BOTTOM);
		if (imgBG != null) {
			g.drawImage(imgBG, offset_x, 0, Graphics.TOP | Graphics.LEFT);
		}
		if (bgflag) {
			drawAllZb();
		}
		if (imgMenu != null) {
			g.drawImage(Image.createImage(imgMenu, 0, 0, 63, 19, 0), 0, canH,
					Graphics.BOTTOM | Graphics.LEFT);
			g.drawImage(Image.createImage(imgMenu, 0, 19, 63, 19, 0), canW,
					canH, Graphics.BOTTOM | Graphics.RIGHT);
		}
	}

	public void startGame() {
		cells.init();
		rb.init();
		aim_x = 0;
		aim_y = 0;
		nowscore = 0;
		status = 0;
		gameover = false;
		inputflag = true;
		createUI();
		drawSmallBall();
		int[] tmp = rb.getFirstRandomBalls();
		for (int i = 0; i < tmp.length; i++) {
			drawBall(tmp[i]);
		}
		drawFocus();
		drawBestscore();
		drawNowscore();
		drawAim();
		initRMS();
	}

	public void loadGame() {
		SaveInfo si = rms.load();
		if (si != null) {
			bestscore = si.bestscore;
			bgflag = si.bgflag;
			if (si.f) {
				inputflag = true;
				nowscore = si.nowscroe;
				threenewballs = si.threenewballs;
				cells = si.cells;
				gameover = false;
				rb.init();
				status = 0;
				createUI();

				for (int i = 0; i < threenewballs.length; i++) {
					g.drawImage(imgSmallBalls[threenewballs[i]], offset_x
							+ offset_next_x + 7 + i * 25, offset_next_y + 7,
							Graphics.TOP | Graphics.LEFT);
				}
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if (cells.cells[i][j].exist) {
							drawBall(cells.cells[i][j].color, i, j, false);
							rb.removePosition(i, j);
						}
					}
				}
				drawFocus();
				drawBestscore();
				drawNowscore();
				drawAim();
			} else {
				startGame();
			}
		} else {
			startGame();
		}
	}

	public void initRMS() {
		SaveInfo si = new SaveInfo();
		si.bestscore = bestscore;
		si.f = false;
		si.nowscroe = nowscore;
		si.bgflag = bgflag;
		si.cells = new Cells();
		si.threenewballs = rb.getRandomBalls();
		rms.save(si);
	}

	public void saveRMS() {
		SaveInfo si = new SaveInfo();
		si.bestscore = bestscore;
		si.f = true;
		si.nowscroe = nowscore;
		si.bgflag = bgflag;
		si.threenewballs = threenewballs;
		si.cells = cells;
		rms.save(si);
	}

	public void updateBg() {
		if (bgflag) {
			drawAllZb();
		} else {
			drawAllCell();
		}
		saveRMS();
		drawAim();
	}

	public void updateGame() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				g.drawImage(imgCell, j * 25 + offset_cell_x + offset_x, i * 25
						+ offset_cell_y + offset_y + 1, Graphics.TOP
						| Graphics.LEFT);
				if (bgflag) {
					g.drawImage(imgZb[i + 1], j * 25 + offset_cell_x + offset_x
							+ 7, i * 25 + offset_cell_y + offset_y + 5,
							Graphics.TOP | Graphics.LEFT);
					g.drawImage(imgZb[j + 1], j * 25 + offset_cell_x + offset_x
							+ 7 + 7, i * 25 + offset_cell_y + offset_y + 5,
							Graphics.TOP | Graphics.LEFT);
				}
				if (cells.cells[i][j].exist) {
					if (!(status == 2 && i == currentball_x && j == currentball_y)) {
						drawBall(cells.cells[i][j].color, i, j, false);
					}
				}
			}
		}
		drawAim();
	}

	public void gameOverUI() {
		g.drawImage(imgGameover, center_x, center_y, Graphics.HCENTER
				| Graphics.BOTTOM);
	}

	public void drawRestart() {
		menuflag = true;
		if (menuflagchoose) {
			g.drawImage(Image.createImage(imgConfirm, 0, 0, 180, 60, 0),
					center_x, center_y, Graphics.HCENTER | Graphics.BOTTOM);
		} else {
			g.drawImage(Image.createImage(imgConfirm, 0, 60, 180, 60, 0),
					center_x, center_y, Graphics.HCENTER | Graphics.BOTTOM);
		}
	}

	public void drawLogo() {
		g.setColor(0x000000);
		g.fillRect(0, 0, canW, canH);
		g.drawImage(imgLogo, center_x, center_y + 40, Graphics.HCENTER
				| Graphics.BOTTOM);
	}

	public void drawCell(int x, int y) {
		g.drawImage(imgCell, y * 25 + offset_cell_x + offset_x, x * 25
				+ offset_cell_y + offset_y + 1, Graphics.TOP | Graphics.LEFT);
		if (bgflag) {
			drawZb(x, y);
		}
	}

	public void drawAllCell() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!cells.cells[i][j].exist) {
					g.drawImage(imgCell, j * 25 + offset_cell_x + offset_x, i
							* 25 + offset_cell_y + offset_y + 1, Graphics.TOP
							| Graphics.LEFT);
				}
			}
		}
	}

	public void drawZb(int x, int y) {
		g.drawImage(imgZb[x + 1], y * 25 + offset_cell_x + offset_x + 7, x * 25
				+ offset_cell_y + offset_y + 5, Graphics.TOP | Graphics.LEFT);
		g.drawImage(imgZb[y + 1], y * 25 + offset_cell_x + offset_x + 7 + 7, x
				* 25 + offset_cell_y + offset_y + 5, Graphics.TOP
				| Graphics.LEFT);
	}

	public void drawAllZb() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!cells.cells[i][j].exist) {
					g.drawImage(imgZb[i + 1], j * 25 + offset_cell_x + offset_x
							+ 7, i * 25 + offset_cell_y + offset_y + 5,
							Graphics.TOP | Graphics.LEFT);
					g.drawImage(imgZb[j + 1], j * 25 + offset_cell_x + offset_x
							+ 7 + 7, i * 25 + offset_cell_y + offset_y + 5,
							Graphics.TOP | Graphics.LEFT);
				}
			}
		}
	}

	public void drawSmallBall() {
		threenewballs = rb.getRandomBalls();
		for (int i = 0; i < threenewballs.length; i++) {
			g.drawImage(imgSmallBalls[threenewballs[i]], offset_x
					+ offset_next_x + 7 + i * 25, offset_next_y + 7,
					Graphics.TOP | Graphics.LEFT);
		}
	}

	public void drawBall(int color) {

		int[] position = rb.getPosition();
		int x = position[0];
		int y = position[1];
		cells.cells[x][y].exist = true;
		cells.cells[x][y].color = color;
		drawBall(color, x, y, true);
		rb.removePosition();

	}

	public void drawBall(int color, int x, int y, boolean f) {
		// g.drawImage(Image.createImage(imgBigBalls[color], 0, 0, 25, 25, 0), y
		// * 25 + offset_cell_x + offset_x, x * 25 + offset_cell_y
		// + offset_y + 1, Graphics.TOP | Graphics.LEFT);

		spriteBigBalls[color].setFrame(0);
		spriteBigBalls[color].setPosition(y * 25 + offset_cell_x + offset_x, x
				* 25 + offset_cell_y + offset_y + 1);
		spriteBigBalls[color].paint(g);
		if (f) {
			checkScore(false, x, y);
		}
	}

	public void chooseBall() {
		if (flushball) {
			drawBall(cells.cells[temp_x][temp_y].color, temp_x, temp_y, false);
			flushball = false;
		}
		drawCell(currentball_x, currentball_y);
		if (aim_x == currentball_x && aim_y == currentball_y) {
			drawAim();
		}
		spriteBigBalls[cells.cells[currentball_x][currentball_y].color]
				.nextFrame();
		spriteBigBalls[cells.cells[currentball_x][currentball_y].color]
				.setPosition(currentball_y * 25 + offset_cell_x + offset_x,
						currentball_x * 25 + offset_cell_y + offset_y + 1);
		spriteBigBalls[cells.cells[currentball_x][currentball_y].color]
				.paint(g);

	}

	public void drawFocus() {
		input_x = aim_x;
		input_y = aim_y;
		g.drawImage(imgNums[aim_x + 1], offset_x + offset_focus_x - 1 - 16,
				offset_y + offset_focus_y + 3, Graphics.TOP | Graphics.LEFT);
		g.drawImage(imgNums[aim_y + 1], offset_x + offset_focus_x - 1 - 8,
				offset_y + offset_focus_y + 3, Graphics.TOP | Graphics.LEFT);
	}

	public void drawInput(int keyCode) {
		if (inputflag) {
			input_x = keyCode - 49;
			g.drawImage(imgNums[keyCode - 48], offset_x + offset_focus_x - 1
					- 16, offset_y + offset_focus_y + 3, Graphics.TOP
					| Graphics.LEFT);
			inputflag = false;
		} else {
			input_y = keyCode - 49;
			inputflag = true;
			g.drawImage(imgNums[keyCode - 48], offset_x + offset_focus_x - 1
					- 8, offset_y + offset_focus_y + 3, Graphics.TOP
					| Graphics.LEFT);
		}
	}

	public void drawAim() {
		g.drawImage(imgAim[0], aim_y * 25 + offset_cell_x + offset_x, aim_x
				* 25 + offset_cell_y + offset_y + 1, Graphics.TOP
				| Graphics.LEFT);
	}

	public void delAim() {
		g.drawImage(imgAim[1], aim_y * 25 + offset_cell_x + offset_x, aim_x
				* 25 + offset_cell_y + offset_y + 1, Graphics.TOP
				| Graphics.LEFT);
	}

	public void drawBestscore() {
		int[] b = Util.IntToIntArray(bestscore);
		for (int i = 0; i < b.length; i++) {
			g.drawImage(imgNums[b[i]], offset_x + offset_bestscore_x - 1
					- (b.length - i) * 8, offset_y + offset_bestscore_y + 3,
					Graphics.TOP | Graphics.LEFT);
		}
	}

	public void drawNowscore() {
		int[] b = Util.IntToIntArray(nowscore);
		for (int i = 0; i < b.length; i++) {
			g.drawImage(imgNums[b[i]], offset_x + offset_nowscore_x - 1
					- (b.length - i) * 8, offset_y + offset_nowscore_y + 3,
					Graphics.TOP | Graphics.LEFT);
		}
	}

	public void moveFocus(int keyCode) {
		if (keyCode == ButtonCode.UP) {
			delAim();
			aim_x--;
			if (aim_x < 0) {
				aim_x = 8;
			}
			drawAim();
			drawFocus();
		}
		if (keyCode == ButtonCode.DOWN) {
			delAim();
			aim_x++;
			if (aim_x > 8) {
				aim_x = 0;
			}
			drawAim();
			drawFocus();
		}
		if (keyCode == ButtonCode.LEFT) {
			delAim();
			aim_y--;
			if (aim_y < 0) {
				aim_y = 8;
			}
			drawAim();
			drawFocus();
		}
		if (keyCode == ButtonCode.RIGHT) {
			delAim();
			aim_y++;
			if (aim_y > 8) {
				aim_y = 0;
			}
			drawAim();
			drawFocus();
		}

	}

	public void moveFocus() {
		if (input_x != -1 && input_y != -1) {
			delAim();
			aim_x = input_x;
			aim_y = input_y;
			drawAim();
		}
	}

	public void moveFocus(int x, int y) {
		input_x = x;
		input_y = y;
		delAim();
		aim_x = input_x;
		aim_y = input_y;
		drawAim();
	}

	public void getBallStatus() {
		inputflag = true;
		status = 0;
		if (cells.cells[aim_x][aim_y].exist) {
			if ((currentball_x != aim_x || currentball_y != aim_y)
					&& currentball_x != -1 && currentball_y != -1
					&& cells.cells[currentball_x][currentball_y].exist) {
				flushball = true;
				temp_x = currentball_x;
				temp_y = currentball_y;
			}
			currentball_x = aim_x;
			currentball_y = aim_y;
			status = 1;
		} else if (!cells.cells[aim_x][aim_y].exist && currentball_x != -1
				&& currentball_y != -1) {
			astar.setCells(cells);
			astar.setStart(currentball_x, currentball_y);
			astar.setGold(aim_x, aim_y);
			if (astar.findPath()) {
				path = astar.getPath();
				moveIndex = 0;
				temp_x = currentball_x;
				temp_y = currentball_y;
				status = 2;
			} else {
				status = 1;
			}
		}
	}

	public void moveBall() {
		if (moveIndex < path.size()) {
			int[] xy = (int[]) path.elementAt(moveIndex);
			drawCell(temp_x, temp_y);
			drawBall(cells.cells[currentball_x][currentball_y].color, xy[0],
					xy[1], false);
			temp_x = xy[0];
			temp_y = xy[1];

			moveIndex++;
			status = 2;
		} else {
			cells.cells[currentball_x][currentball_y].exist = false;
			cells.cells[aim_x][aim_y].exist = true;
			cells.cells[aim_x][aim_y].color = cells.cells[currentball_x][currentball_y].color;
			rb.removePosition(aim_x, aim_y);
			rb.addPosition(currentball_x, currentball_y);
			currentball_x = currentball_y = -1;
			checkScore(true, aim_x, aim_y);
			saveRMS();
			if (rb.v.size() == 0) {
				initRMS();
			}
			status = 0;
		}
	}

	public void checkScore(boolean f, int x, int y) {

		Vector v1 = new Vector();
		Vector v2 = new Vector();
		Vector v3 = new Vector();
		Vector v4 = new Vector();
		int color = cells.cells[x][y].color;
		for (int i = y - 1; i >= 0; i--) {
			if (cells.cells[x][i].exist && cells.cells[x][i].color == color) {
				int[] p = { x, i };
				v1.addElement(p);
			} else {
				break;
			}
		}
		for (int i = y + 1; i <= 8; i++) {
			if (cells.cells[x][i].exist && cells.cells[x][i].color == color) {
				int[] p = { x, i };
				v1.addElement(p);
			} else {
				break;
			}
		}
		for (int i = x - 1; i >= 0; i--) {
			if (cells.cells[i][y].exist && cells.cells[i][y].color == color) {
				int[] p = { i, y };
				v2.addElement(p);
			} else {
				break;
			}
		}
		for (int i = x + 1; i <= 8; i++) {
			if (cells.cells[i][y].exist && cells.cells[i][y].color == color) {
				int[] p = { i, y };
				v2.addElement(p);
			} else {
				break;
			}
		}

		for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
			if (cells.cells[i][j].exist && cells.cells[i][j].color == color) {
				int[] p = { i, j };
				v3.addElement(p);
			} else {
				break;
			}
		}
		for (int i = x + 1, j = y + 1; i <= 8 && j <= 8; i++, j++) {
			if (cells.cells[i][j].exist && cells.cells[i][j].color == color) {
				int[] p = { i, j };
				v3.addElement(p);
			} else {
				break;
			}
		}

		for (int i = x + 1, j = y - 1; i <= 8 && j >= 0; i++, j--) {
			if (cells.cells[i][j].exist && cells.cells[i][j].color == color) {
				int[] p = { i, j };
				v4.addElement(p);
			} else {
				break;
			}
		}
		for (int i = x - 1, j = y + 1; i >= 0 && j <= 8; i--, j++) {
			if (cells.cells[i][j].exist && cells.cells[i][j].color == color) {
				int[] p = { i, j };
				v4.addElement(p);
			} else {
				break;
			}
		}

		if (v1.size() > 3 || v2.size() > 3 || v3.size() > 3 || v4.size() > 3) {

			int a = 1;
			int b = 0;
			boolean[] bz = { false, false, false, false };
			if (v1.size() > 3) {
				a += v1.size();
				b++;
				bz[0] = true;
			}
			if (v2.size() > 3) {
				a += v2.size();
				b++;
				bz[1] = true;
			}
			if (v3.size() > 3) {
				a += v3.size();
				b++;
				bz[2] = true;
			}
			if (v4.size() > 3) {
				a += v4.size();
				b++;
				bz[3] = true;
			}
			if (bz[0]) {
				for (int i = 0; i < v1.size(); i++) {
					int[] p = (int[]) v1.elementAt(i);
					cells.cells[p[0]][p[1]].exist = false;
					rb.addPosition(p[0], p[1]);
					drawCell(p[0], p[1]);
				}
			}
			if (bz[1]) {
				for (int i = 0; i < v2.size(); i++) {
					int[] p = (int[]) v2.elementAt(i);
					cells.cells[p[0]][p[1]].exist = false;
					rb.addPosition(p[0], p[1]);
					drawCell(p[0], p[1]);
				}
			}
			if (bz[2]) {
				for (int i = 0; i < v3.size(); i++) {
					int[] p = (int[]) v3.elementAt(i);
					cells.cells[p[0]][p[1]].exist = false;
					rb.addPosition(p[0], p[1]);
					drawCell(p[0], p[1]);
				}
			}
			if (bz[3]) {
				for (int i = 0; i < v4.size(); i++) {
					int[] p = (int[]) v4.elementAt(i);
					cells.cells[p[0]][p[1]].exist = false;
					rb.addPosition(p[0], p[1]);
					drawCell(p[0], p[1]);
				}
			}
			cells.cells[x][y].exist = false;
			rb.addPosition(x, y);
			drawCell(x, y);
			drawAim();
			if (f) {
				nowscore += 10 + 2 * (a + b - 6) * (a + b - 6);
				drawNowscore();
				if (nowscore >= bestscore) {
					bestscore = nowscore;
					drawBestscore();
				}
			} else {
				drawBall(rb.getRandomColor());
				drawSmallBall();
			}
		} else {
			if (f) {
				int times = 3;
				if (rb.v.size() < 3) {
					times = rb.v.size();
				}
				for (int i = 0; i < times; i++) {
					drawBall(threenewballs[i]);
				}
				drawSmallBall();
				if (rb.v.size() == 0) {
					gameover = true;
					gameOverUI();
				}
			}
		}
	}
}
