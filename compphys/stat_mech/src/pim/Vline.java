package pim;

import java.awt.Component;
import java.awt.Graphics;

/**
 * A vertical line
 * 
 * @author Paul-H. Balduf, Dalby 2013
 * 
 */
public class Vline extends Component {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {
		g.drawLine(0, 0, 0, this.getHeight());
	}
}
