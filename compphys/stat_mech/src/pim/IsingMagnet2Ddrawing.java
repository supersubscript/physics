package pim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * This class represents a drawing of a 2D Ising model.
 * 
 * This is free software. Published under GPL v3 or later.
 * 
 * @author Paul-H. Balduf, 2013
 * 
 */
public class IsingMagnet2Ddrawing extends JPanel {
	private static final long serialVersionUID = 1L;

	private int[][] spins = null;
	private double[][] meanfield = null;
	private int drawSize;
	private int spinSize;
	private int states;
	private int drawStyle;
	private double meanFieldMagnitude;

	private boolean drawSpins;
	private boolean drawMeanField;
	private boolean drawBoundaries;

	/**
	 * Represent spins by arrow-like lines with a small box as an arrowhead
	 */
	public final static int LINES = 44;
	/**
	 * Represent spins by brightness of a filled box that is smaller than the
	 * cell
	 */
	public final static int BOXES = -5;

	/**
	 * Represents spins by brightness of a totally filled cell
	 */
	public final static int BIG_BOXES = 88;

	public IsingMagnet2Ddrawing(int[][] spins, double[][] meanfield,
			int states, int size, int drawStyle, boolean drawSpins,
			boolean drawMeanField, boolean drawBoundaries,
			double meanFieldMagnitude) {
		this.spins = spins;
		this.meanfield = meanfield;
		this.drawSize = size;
		this.states = states;
		this.spinSize = spins.length;
		this.drawSpins = drawSpins;
		this.drawMeanField = drawMeanField;
		this.drawBoundaries = drawBoundaries;
		this.meanFieldMagnitude = meanFieldMagnitude;

		this.setPreferredSize(new Dimension(size, size));
		this.drawStyle = drawStyle;

	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		double PixelPerSpin = drawSize / (double) spins.length;

		Dimension d = this.getSize();
		double scalefaktor = Math.min(d.getHeight() / drawSize, d.getWidth()
				/ drawSize);
		g2.scale(scalefaktor, scalefaktor);
		g2.setBackground(Color.WHITE);

		g2.clearRect(0, 0, drawSize, drawSize);

		g2.setStroke(new BasicStroke((int) (PixelPerSpin * .1f)));

		if (drawMeanField) {

			int COLORS = 32;
			Color[] colors = new Color[COLORS];
			for (int index = 0; index < COLORS; index++) {
				colors[index] = new Color(
						(int) (255d * index / (double) (COLORS - 1)), 0,
						(int) (255d * (1 - index / (double) (COLORS - 1))));
			}

			for (int x = 0; x < spinSize; x++) {
				for (int y = 0; y < spinSize; y++) {

					g2.setColor(colors[(int) ((meanfield[x][y]
							/ meanFieldMagnitude / 2 + .5) * (COLORS - 1))]);
					g2.fillRect((int) (x * PixelPerSpin),
							(int) (y * PixelPerSpin), (int) (PixelPerSpin + 1),
							(int) (PixelPerSpin + 1));
				}
			}
		}
		if (drawSpins) {
			if (drawStyle == LINES) {
				double BALLSIZE = .4;
				g2.setColor(Color.BLACK);

				for (int x = 0; x < spinSize; x++) {
					for (int y = 0; y < spinSize; y++) {

						g2.drawLine((int) ((x + .5f) * PixelPerSpin),
								(int) ((y + .2f) * PixelPerSpin),
								(int) ((x + .5f) * PixelPerSpin),
								(int) ((y + .8f) * PixelPerSpin));
						g2.fillOval(
								(int) ((x + .5 - BALLSIZE / 2f) * PixelPerSpin),
								(int) ((y + .9 - BALLSIZE - (.8 - BALLSIZE)
										* (double) spins[x][y] / ((states - 1))) * PixelPerSpin),
								(int) (BALLSIZE * PixelPerSpin),
								(int) (BALLSIZE * PixelPerSpin));
					}
				}
			} else if (drawStyle == BOXES) {

				double BOXSIZE = .7;
				Color[] colors = new Color[states];
				for (int index = 0; index < states; index++) {
					colors[index] = new Color(
							(int) (255d * index / (double) (states - 1)),
							(int) (255d * index / (double) (states - 1)),
							(int) (255d * index / (double) (states - 1)));
				}

				for (int x = 0; x < spinSize; x++) {
					for (int y = 0; y < spinSize; y++) {
						g2.setColor(colors[spins[x][y]]);
						g2.fillRect(
								(int) ((x + (1 - BOXSIZE) / 2) * PixelPerSpin),
								(int) ((y + (1 - BOXSIZE) / 2) * PixelPerSpin),
								(int) (BOXSIZE * PixelPerSpin),
								(int) (BOXSIZE * PixelPerSpin));
					}
				}
			} else if (drawStyle == BIG_BOXES) {

				Color[] colors = new Color[states];
				for (int index = 0; index < states; index++) {
					colors[index] = new Color(
							(int) (255d * index / (double) (states - 1)),
							(int) (255d * index / (double) (states - 1)),
							(int) (255d * index / (double) (states - 1)));
				}

				for (int x = 0; x < spinSize; x++) {
					for (int y = 0; y < spinSize; y++) {
						g2.setColor(colors[spins[x][y]]);
						g2.fillRect((int) (x * PixelPerSpin),
								(int) (y * PixelPerSpin), (int) (PixelPerSpin),
								(int) (PixelPerSpin));
					}
				}
			}
		}
		if (drawBoundaries) {
			g2.setColor(Color.GREEN);

			for (int x = 0; x < spinSize - 1; x++) {
				for (int y = 0; y < spinSize - 1; y++) {
					if (spins[x][y] != spins[x + 1][y]) {
						g2.drawLine((int) ((x + 1) * PixelPerSpin),
								(int) (y * PixelPerSpin),
								(int) ((x + 1) * PixelPerSpin),
								(int) ((y + 1) * PixelPerSpin));
					}
					if (spins[x][y] != spins[x][y + 1]) {
						g2.drawLine((int) (x * PixelPerSpin),
								(int) ((y + 1) * PixelPerSpin),
								(int) ((x + 1) * PixelPerSpin),
								(int) ((y + 1) * PixelPerSpin));
					}
				}
				if (spins[x][spinSize - 1] != spins[x + 1][spinSize - 1]) {
					g2.drawLine((int) ((x + 1) * PixelPerSpin),
							(int) ((spinSize - 1) * PixelPerSpin),
							(int) ((x + 1) * PixelPerSpin),
							(int) ((spinSize) * PixelPerSpin));
				}
				if (spins[x][spinSize - 1] != spins[x][0]) {
					g2.drawLine((int) (x * PixelPerSpin),
							(int) ((spinSize) * PixelPerSpin),
							(int) ((x + 1) * PixelPerSpin),
							(int) ((spinSize) * PixelPerSpin));
					g2.drawLine((int) (x * PixelPerSpin), 0,
							(int) ((x + 1) * PixelPerSpin), 0);

				}
			}
			for (int y = 0; y < spinSize - 1; y++) {
				if (spins[spinSize - 1][y] != spins[spinSize - 1][y + 1]) {
					g2.drawLine((int) ((spinSize - 1) * PixelPerSpin),
							(int) ((y + 1) * PixelPerSpin),
							(int) ((spinSize) * PixelPerSpin),
							(int) ((y + 1) * PixelPerSpin));
				}
				if (spins[spinSize - 1][y] != spins[0][y]) {
					g2.drawLine((int) (spinSize * PixelPerSpin),
							(int) (y * PixelPerSpin),
							(int) (spinSize * PixelPerSpin),
							(int) ((y + 1) * PixelPerSpin));
					g2.drawLine(0, (int) (y * PixelPerSpin), 0,
							(int) ((y + 1) * PixelPerSpin));
				}
			}
			if (spins[spinSize - 1][spinSize - 1] != spins[spinSize - 1][0]) {
				g2.drawLine((int) ((spinSize - 1) * PixelPerSpin),
						(int) (spinSize * PixelPerSpin),
						(int) ((spinSize) * PixelPerSpin),
						(int) (spinSize * PixelPerSpin));
				g2.drawLine((int) ((spinSize - 1) * PixelPerSpin), 0,
						(int) ((spinSize) * PixelPerSpin), 0);
			}
			if (spins[spinSize - 1][spinSize - 1] != spins[0][spinSize - 1]) {
				g2.drawLine((int) (spinSize * PixelPerSpin),
						(int) ((spinSize - 1) * PixelPerSpin),
						(int) (spinSize * PixelPerSpin),
						(int) (spinSize * PixelPerSpin));
				g2.drawLine(0, (int) ((spinSize - 1) * PixelPerSpin), 0,
						(int) ((spinSize) * PixelPerSpin));
			}

		}
	}
}
