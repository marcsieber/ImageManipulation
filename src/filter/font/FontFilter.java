package filter.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import filter.AFilter;
import util.ColorUtil;

public class FontFilter extends AFilter {

	private final Font font;
	private final String sequence;
	private final List<Point> primaryLetter;
	private final List<Point> antiAliasedEffect;
	
	private Color fillColor = Color.WHITE;

	public FontFilter(Font font, String sequence, int width, int height, List<Point> primary, List<Point> antiAliased) {
		super(width, height);
		this.font = font;
		this.sequence = sequence;

		if (primary == null || primary.isEmpty()) {
			primaryLetter = antiAliased;
			this.antiAliasedEffect = new ArrayList<>();
		} else {
			this.primaryLetter = primary;
			this.antiAliasedEffect = antiAliased;
		}
	}

	public String getSequence() {
		return sequence;
	}

	public Font getFont() {
		return font;
	}
	
	public Color getFillColor() {
		return fillColor;
	}
	
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public AFilter apply() {
		return apply(true);
	}

	@Override
	public AFilter apply(boolean antiAliased) {
		List<Point> letter   = new ArrayList<>();
		List<Point> aaPoints = new ArrayList<>();
		
		letter.addAll(primaryLetter);
		aaPoints.addAll(antiAliasedEffect);
		
		antiAliased = antiAliased ? aaPoints != null && !aaPoints.isEmpty() : false;

		for (int x = 0; x < super.width; x++) {
			for (int y = 0; y < super.height; y++) {

				Point pixel = new Point(x, y);
				if (!letter.contains(pixel)) {

					if (antiAliased && aaPoints.contains(pixel)) {
						Color color = ColorUtil.averageColor(new Color(super.image.getRGB(x, y)), fillColor);
						super.image.setRGB(x, y, color.getRGB());
						aaPoints.remove(pixel);

					} else {
						super.image.setRGB(x, y, fillColor.getRGB());
					}
				} else {
					letter.remove(pixel);
				}
			}
		}
		return this;
	}

}
