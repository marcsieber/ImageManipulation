package util;

import java.awt.Font;
import java.awt.Toolkit;

public class FontUtil {
	
	//TODO marcsieber: 05.03.2017 Currently it only matches the given height
	public static Font getFontMatchingSpace(Font f, int width, int height) {
		double fontSize = height * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0;
		return f.deriveFont((float) fontSize);
	}

}
