package pim;

import java.awt.Component;
import java.awt.Graphics;

/**
 * A horizontal line.
 * 
 * @author Paul-H. Balduf, Dalby 2013
 * 
 */
public class Hline extends Component {
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {

		g.drawLine(0, 0, this.getWidth(), 0);

	}
}
