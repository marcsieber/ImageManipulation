package filter;

import java.awt.Color;

import util.ColorUtil;

public class BlurredFilter extends AFilter {

	public BlurredFilter(int width, int height) {
		super(width, height);
	}

	@Override
	public AFilter apply() {
		Color averageColor = ColorUtil.averageColorFromImage(image);
		
		for (int x = 0; x < super.image.getWidth(); x++) {
			for (int y = 0; y < super.image.getHeight(); y++) {
				super.image.setRGB(x,y, averageColor.getRGB());
			}
		}
		return this;
	}

	@Override
	public AFilter apply(boolean antiAliased) {
		return apply();
	}

}
