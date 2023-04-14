package br.nnpe;

import java.awt.Point;

/**
 * @author Paulo Sobreira Criado Em 21/12/2005
 */
public class ThreadLogo extends Thread {
	private LogoPanel panel;
	private boolean morta;
	private boolean pausada;
	private SowLogo logo;

	public ThreadLogo(LogoPanel logoPanel, SowLogo logo) {
		super();
		this.panel = logoPanel;
		this.logo = logo;
	}

	private void desenhar() {
		try {
			panel.repaint();
			sleep(60);

			while (pausada || !logo.isShowing()) {
				sleep(2000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		super.run();

		int efeitoCont = 3;
		int efeito = 0;
		int ultimoEfeito = 0;

		while (!morta) {

			while (efeito == ultimoEfeito) {
				efeito = (int) (1 + (Math.random() * 16));
			}
			ultimoEfeito = efeito;

			if (efeitoCont-- == 0) {
				irPara(new Point(10, 10));
				panel.efeitoFonte = false;
				pausada = true;
				continue;
			}

			switch (efeito) {
			case 1:
				zoomOutRotate();
				if (!panel.paintComponet)
					panel.paintComponet = true;

				break;

			case 2:
				panel.paintComponet = !panel.paintComponet;

				break;

			case 3:
				irPara(panel.sortearPonto());
				if (!panel.paintComponet)
					panel.paintComponet = true;

				break;

			case 4:
				encolherEsticarLado();
				if (!panel.paintComponet)
					panel.paintComponet = true;

				break;

			case 5:
				panel.paintComponet = !panel.paintComponet;

				break;

			case 6:
				superZoomInOutRotate();
				if (!panel.paintComponet)
					panel.paintComponet = true;

				break;

			case 7:
				zoomOutRotate();
				if (!panel.paintComponet)
					panel.paintComponet = true;
				break;
			case 8:
				litleZoomAndDoubleRotate();
				if (!panel.paintComponet)
					panel.paintComponet = true;

				break;

			case 9:
				panel.paintComponet = !panel.paintComponet;

				break;

			case 10:
				encolherEsticarCima();
				if (!panel.paintComponet)
					panel.paintComponet = true;

				break;

			case 11:
				panel.efeitoFonte = true;

				break;
			case 12:
				zoomAndMove(panel.sortearPonto());
				if (!panel.paintComponet)
					panel.paintComponet = true;

				break;
			case 13:
				encolherEsticarLado();
				if (!panel.paintComponet)
					panel.paintComponet = true;
				break;
			case 14:
				zoomOutRotate();
				if (!panel.paintComponet)
					panel.paintComponet = true;
				break;
			case 15:

				superZoomIn();
				if (!panel.paintComponet)
					panel.paintComponet = true;
				break;
			case 16:
				panel.efeitoFonte = true;

				break;
			default:
				break;
			}
		}
	}

	public void setMorta(boolean morta) {
		this.morta = morta;
		System.out.println("Theread Logo Finalizada!");
	}

	public boolean isPausada() {
		return pausada;
	}

	public void setPausada(boolean pausada) {
		this.pausada = pausada;
	}

	private void litleZoomAndDoubleRotate() {
		irPara(new Point(panel.local.x, 5));

		while (panel.zoom < 1.6) {
			panel.zoomIn();
			desenhar();
		}

		while (panel.angle < 180) {
			panel.increasseAngle();
			desenhar();
		}

		while (panel.angle > 0) {
			panel.decreasseAngle();
			desenhar();
		}

		panel.decreasseAngle();
		desenhar();

		while (panel.angle > 180) {
			panel.decreasseAngle();
			desenhar();
		}

		while (panel.zoom > 1) {
			panel.zoomOut();
			panel.increasseAngle();
			desenhar();
		}

		panel.rotate(0);
	}

	private void zoomAndMove(Point p) {
		while (!((panel.local.x == p.x) && (panel.local.y == p.y))) {
			if (panel.local.x > p.x) {
				panel.local.x--;
			} else if (panel.local.x < p.x) {
				panel.local.x++;
			}

			if (panel.local.y > p.y) {
				panel.local.y--;
			} else if (panel.local.y < p.y) {
				panel.local.y++;
			}

			panel.zoomIn();
			desenhar();
		}

		while (panel.zoom > 1) {
			panel.zoomOut();
			desenhar();
		}
	}

	private void superZoomInOutRotate() {
		irPara(new Point(panel.local.x, 5));

		while (panel.zoom < 3) {
			panel.zoomIn();
			panel.decreasseAngle();
			desenhar();
		}

		while (panel.zoom > 1) {
			panel.zoomOut();
			panel.increasseAngle();
			desenhar();
		}
	}

	private void irPara(Point p) {
		while (!((panel.local.x == p.x) && (panel.local.y == p.y))) {
			if (panel.local.x > p.x) {
				panel.local.x--;
			} else if (panel.local.x < p.x) {
				panel.local.x++;
			}

			if (panel.local.y > p.y) {
				panel.local.y--;
			} else if (panel.local.y < p.y) {
				panel.local.y++;
			}

			desenhar();
		}
	}

	private void encolherEsticarLado() {
		double fator = 1;

		while (fator > .9) {
			fator -= 0.001;
			panel.afZoom.scale(fator, 1);
			desenhar();
		}

		fator = 1.1;

		while (fator < 1.145) {
			fator += 0.001;
			panel.afZoom.scale(fator, 1);
			desenhar();
		}
	}

	private void encolherEsticarCima() {
		double fator = 1;

		while (fator > .9) {
			fator -= 0.001;
			panel.afZoom.scale(1, fator);
			desenhar();
		}

		fator = 1.1;

		while (fator < 1.145) {
			fator += 0.001;
			panel.afZoom.scale(1, fator);
			desenhar();
		}
	}

	private void superZoomIn() {
		while (panel.zoom < 5) {
			if (panel.local.x > 0) {
				panel.local.x--;
			}

			if (panel.local.y > 0) {
				panel.local.y--;
			}

			panel.zoomIn();
			desenhar();
		}

		while (panel.zoom > 1) {
			panel.zoomOut();
			desenhar();
		}
	}

	public void zoomOutRotate() {
		irPara(new Point(panel.local.x, 5));

		while (panel.zoom > 0) {
			panel.local.x++;
			panel.zoomOut();
			panel.increasseAngle();
			desenhar();
		}

		while (panel.zoom <= 1) {
			panel.local.x--;
			panel.zoomIn();
			panel.decreasseAngle();
			desenhar();
		}

		panel.rotate(0);
	}
}
