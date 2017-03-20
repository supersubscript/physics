package pim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JComponent;

/**
 * A simple component that plots the values of an array.
 * 
 * This is free software. Published under GPL v3 or later.
 * 
 * @author Paul-H. Balduf, Dalby 2013
 * 
 */
public class ArrayPlot extends JComponent {
	private static final long serialVersionUID = 1L;

	private double[] array = null;
	private double min = -1;
	private double max = 1;

	private Font smallText = new Font(Font.SANS_SERIF, Font.PLAIN, 8);

	public ArrayPlot(double[] array, double min, double max) {
		this.setPreferredSize(new Dimension(100, 50));
		this.array = array;
		this.min = min;
		this.max = max;
	}

	public ArrayPlot() {
		this.setPreferredSize(new Dimension(100, 50));
	}

	/** Sets the array to be plotted. Does not change the y plot range */
	public void setData(double[] array) {
		this.array = array;
		repaint();
	}

	public void setMaxY(double maxY) {
		this.max = maxY;
	}

	public void setMinY(double minY) {
		this.min = minY;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int w = this.getSize().width;
		int h = this.getSize().height;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		 smallText = new Font(Font.SANS_SERIF, Font.PLAIN, h/5);

		if (array != null && array.length > 1) {
			double XperPixel = (array.length - 1) / (double) w;
			double YperPixel = (max - min) / h;

			double zeroPoint = max / (max - min) * h;
			Stroke thin = new BasicStroke(1);
			Stroke thick = new BasicStroke((int) (h * 0.05d));

			g2.clearRect(0, 0, w, h);

			g2.setColor(Color.GRAY);
			g2.setStroke(thin);
			g2.drawLine(0, (int) zeroPoint, w, (int) zeroPoint);
			g2.drawLine(0, (int) (zeroPoint + 3), 0, (int) (zeroPoint - 3));

			g2.setFont(smallText);
			g2.drawString("parallel", 0, h/5-1);
			g2.drawString("antiparallel", 0, h-1);

			int y0, y1, x;

			g2.setColor(Color.BLUE);
			y0 = (int) ((max - array[0]) / YperPixel);
			for (int index = 1; index < array.length; index++) {
				x = (int) (index / XperPixel);
				y1 = (int) ((max - array[index]) / YperPixel);
				g2.setColor(Color.BLUE);
				g2.setStroke(thick);
				g2.drawLine((int) ((index - 1) / XperPixel), y0, x, y1);
				y0 = y1;
				g2.setStroke(thin);
				g2.setColor(Color.GRAY);
				g2.drawLine(x, (int) (zeroPoint + 3), x, (int) (zeroPoint - 3));

			}

		} else {
			g2.drawString("no data", w / 2 - 30, h / 2 - 10);

		}

	}
}
