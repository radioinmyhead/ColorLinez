import javax.microedition.lcdui.game.GameCanvas;

public class ColorLinezGameCanvas extends GameCanvas implements Runnable {
	private int canW = getWidth();

	private int canH = getHeight();

	int delay = 30;

	UI ui;

	int tmp = 10;

	boolean logoflag = true;

	public ColorLinezGameCanvas() {
		super(false);
		setFullScreenMode(true);
		canW = getWidth();
		canH = getHeight();
		ui = new UI(getGraphics(), canW, canH);
	}

	public void start() {
		ui.drawLogo();
		flushGraphics();
		Thread r = new Thread(this);
		r.start();
	}

	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
		}
		ui.loadGame();
		logoflag = false;
		flushGraphics();
		while (true) {

			if (ui.status == 1) {
				if (!ui.menuflag) {
					ui.chooseBall();
					flushGraphics();
				}
			}
			if (ui.status == 2) {
				ui.moveBall();
				flushGraphics();
			}

			if (ui.status == 0) {
				delay = 30;
			}
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ex) {
			}
		}

	}

	protected void keyReleased(int keyCode) {
		if (!logoflag) {
			if (keyCode == ButtonCode.RESTART) {
				if (ui.gameover) {
					ui.startGame();
				} else {
					if (!ui.menuflag) {
						tmp = ui.status;
						ui.status = 10;
						ui.drawRestart();
					}
				}
			}
			if (!ui.gameover) {
				if (ui.status != 2) {
					if (keyCode == ButtonCode.CHANGEBG && !ui.menuflag) {
						if (ui.bgflag) {
							ui.bgflag = false;
						} else {
							ui.bgflag = true;
						}
						ui.updateBg();
					} else if (keyCode == ButtonCode.CONFIRM
							|| keyCode == ButtonCode.STAR) {
						if (ui.menuflag) {
							if (ui.menuflagchoose) {
								ui.status = 0;
								ui.startGame();
								ui.menuflag = false;
								ui.menuflagchoose = false;
							} else {
								ui.menuflag = false;
								ui.menuflagchoose = false;
								ui.status = tmp;
								ui.updateGame();

							}

						} else {
							ui.getBallStatus();
							if (ui.status == 1) {
								delay = 90;
							} else if (ui.status == 2) {
								delay = 25;
							}
						}
					} else if (keyCode <= ButtonCode.UP
							&& keyCode >= ButtonCode.RIGHT) {
						if (ui.menuflag) {
							if (!ui.menuflagchoose
									&& keyCode == ButtonCode.LEFT) {
								ui.menuflagchoose = true;
								ui.drawRestart();
							}
							if (ui.menuflagchoose
									&& keyCode == ButtonCode.RIGHT) {
								ui.menuflagchoose = false;
								ui.drawRestart();
							}
						} else {
							ui.moveFocus(keyCode);
						}
					} else if (keyCode >= ButtonCode.NUM1
							&& keyCode <= ButtonCode.NUM9 && !ui.menuflag) {
						ui.drawInput(keyCode);
						ui.moveFocus();
					}
				}
			}
			flushGraphics();
		}
	}

	protected void keyRepeated(int keyCode) {
		if (!logoflag) {
			if (!ui.gameover) {
				if (ui.status != 2) {
					if (keyCode <= ButtonCode.UP && keyCode >= ButtonCode.RIGHT
							&& !ui.menuflag) {
						ui.moveFocus(keyCode);
					}
				}
			}
			flushGraphics();
		}
	}

	protected void pointerReleased(int x, int y) {
		if (!logoflag) {
			if (getKey(x, y) == ButtonCode.RESTART) {
				if (ui.gameover) {
					ui.startGame();
				} else {
					if (!ui.menuflag) {
						tmp = ui.status;
						ui.status = 10;
						ui.drawRestart();
					}
				}
			}
			if (!ui.gameover) {
				if (ui.status != 2) {
					if (getKey(x, y) == ButtonCode.CHANGEBG && !ui.menuflag) {
						if (ui.bgflag) {
							ui.bgflag = false;
						} else {
							ui.bgflag = true;
						}
						ui.updateBg();
					} else if (getKey(x, y) == ButtonCode.LEFT) {

						ui.menuflagchoose = true;

						ui.status = 0;
						ui.startGame();
						ui.menuflag = false;
						ui.menuflagchoose = false;

					} else if (getKey(x, y) == ButtonCode.RIGHT) {

						ui.menuflagchoose = false;

						ui.menuflag = false;
						ui.menuflagchoose = false;
						ui.status = tmp;
						ui.updateGame();
					} else if (getKey(x, y) == ButtonCode.CONFIRM) {
						int tmpX = (x - ui.offset_x) / 25;
						int tmpY = (y - ui.offset_cell_y) / 25;
						ui.moveFocus(tmpY, tmpX);
						ui.drawFocus();
						ui.getBallStatus();
						if (ui.status == 1) {
							delay = 90;
						} else if (ui.status == 2) {
							delay = 25;
						}
					}
				}
			}
			flushGraphics();
		}
	}

	private int getKey(int x, int y) {

		if (ui.menuflag && x > ui.center_x - 90 + 27
				&& x < ui.center_x - 90 + 79 && y > ui.center_y - 60 + 39
				&& y < ui.center_y - 60 + 54) {
			return ButtonCode.LEFT;
		} else if (ui.menuflag && x > ui.center_x - 90 + 108
				&& x < ui.center_x - 90 + 151 && y > ui.center_y - 60 + 39
				&& y < ui.center_y - 60 + 54) {
			return ButtonCode.RIGHT;
		}
		if (x < 63 && y > canH - 19) {
			return ButtonCode.RESTART;
		}
		if (x > canW - 63 && y > canH - 19) {
			return ButtonCode.CHANGEBG;
		}
		if (!ui.menuflag && x > ui.offset_x && x < ui.offset_x + 225
				&& y > ui.offset_cell_y && y < ui.offset_cell_y + 225) {
			return ButtonCode.CONFIRM;
		}
		return ButtonCode.POUND;
	}
}
