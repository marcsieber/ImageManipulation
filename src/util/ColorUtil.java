package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorUtil {
	
	public static Color averageColorFromImage(BufferedImage image) {
		List<Color> colors = new ArrayList<>();
		
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color c = new Color(image.getRGB(x, y));
				colors.add(c);
			}
		}
		return averageColor(colors);	
	}
	
	public static Color averageColor(List<Color> colors) {
		return averageColor(colors.toArray(new Color[colors.size()]));
	}
	
	public static Color averageColor(Color... colors) {
		long r = 0;
		long g = 0;
		long b = 0;
		long pixelCounter = 0;
		
		for (Color c : colors) {
			r += c.getRed();
			g += c.getGreen();
			b += c.getBlue();
			pixelCounter++;
		}
		
		r = r / pixelCounter;
		g = g / pixelCounter;
		b = b / pixelCounter;
		return new Color((int) r, (int) g, (int) b);	
	}

}
