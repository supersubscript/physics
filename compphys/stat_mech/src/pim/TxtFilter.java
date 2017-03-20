package pim;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * a File filter which only accepts png files
 * 
 * @author Paul-H. Balduf, Dalby 2013
 * 
 */
public class TxtFilter extends FileFilter {

	@Override
	public boolean accept(File arg0) {
		return arg0.getName().toLowerCase().endsWith(".txt");
	}

	@Override
	public String getDescription() {
		return ".txt plain text files";
	}

}
