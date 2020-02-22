package verdantium.kluges;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

//$$strtCprt
/*
     Verdantium compound-document framework by Thorn Green
	Copyright (C) 2005 Thorn Green

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
//$$endCprt

/**
*
* --- SOURCE MODIFICATION LIST ---
*
* Please document all changes to this source file here.
* Feel free to add rows if needed.
*
*
*    |-----------------------|-------------------------------------------------|----------------------------------------------------------------------|---------------------------------------------------------------...
*    | Date of Modification  |    Author of Modification                       |    Reason for Modification                                           |    Description of Modification (use multiple rows if needed)  ... 
*    |-----------------------|-------------------------------------------------|----------------------------------------------------------------------|---------------------------------------------------------------...
*    |                       |                                                 |                                                                      |
*    | 9/24/2000             | Thorn Green (viridian_1138@yahoo.com)           | Needed to provide a standard way to document source file changes.    | Added a souce modification list to the documentation so that changes to the souce could be recorded. 
*    | 10/22/2000            | Thorn Green (viridian_1138@yahoo.com)           | Methods did not have names that followed standard Java conventions.  | Performed a global modification to bring the names within spec.
*    | 08/07/2004            | Thorn Green (viridian_1138@yahoo.com)           | Establish baseline for all changes in the last year.                 | Establish baseline for all changes in the last year.
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*    |                       |                                                 |                                                                      |
*
*
*/

/**
* Java2D as of the current version (Java 2 Platform) does not clip
* line segments larger outside [ 32K x 32K ] in some modes.  This
* is a klugs class creating a graphics context that performs the
* additional clipping.  Note: this class is very slow.  This class
* will be deprecated when a future version of Java solves the problem.
* 
* Note: in a true AOP environment this might have been a good candidate for an Aspect.
* 
* @author Thorn Green
*/
public class ClipGraphics extends Graphics2D {
	
	/**
	 * The clipping rectangle with which to clip the rendering.
	 */
	protected Rectangle2D ClipRect = null;
	
	/**
	 * The original un-clipped graphics context.
	 */
	protected Graphics2D xy = null;

	/**
	 * Constructor.
	 * @param g The original un-clipped graphics context.
	 * @param in The clipping rectangle with which to clip the rendering.
	 */
	public ClipGraphics(Graphics2D g, Rectangle2D in) {
		xy = g;
		ClipRect = in;
	}

	/**
	 * Gets the clipping rectangle with which to clip the rendering.
	 * @return The clipping rectangle with which to clip the rendering.
	 */
	public Rectangle2D getClippingObject() {
		return (ClipRect);
	}

	/**
	 * Sets the clipping rectangle with which to clip the rendering.
	 * @param in The clipping rectangle with which to clip the rendering.
	 */
	public void setClippingObject(Rectangle2D in) {
		ClipRect = in;
	}

	@Override
	public void addRenderingHints(Map hints) {
		xy.addRenderingHints(hints);
	}

	@Override
	public void clip(Shape s) {
		xy.clip(s);
	}

	@Override
	public void draw(Shape s) {
		Shape s2 = getStroke().createStrokedShape(s);
		fill(s2);
	}

	@Override
	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		xy.draw3DRect(x, y, width, height, raised);
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		xy.drawGlyphVector(g, x, y);
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		xy.drawImage(img, op, x, y);
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return (xy.drawImage(img, xform, obs));
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		xy.drawRenderableImage(img, xform);
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		xy.drawRenderedImage(img, xform);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		xy.drawString(iterator, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		xy.drawString(iterator, x, y);
	}

	@Override
	public void drawString(String s, float x, float y) {
		xy.drawString(s, x, y);
	}

	@Override
	public void drawString(String str, int x, int y) {
		xy.drawString(str, x, y);
	}

	@Override
	public void fill(Shape s) {
		Rectangle2D sbd = s.getBounds2D();
		double wid = sbd.getWidth();
		double hei = sbd.getHeight();
		int i1 = ClipRect.outcode(sbd.getX(), sbd.getY());
		int i2 = ClipRect.outcode(sbd.getX() + wid, sbd.getY());
		int i3 = ClipRect.outcode(sbd.getX() + wid, sbd.getY() + hei);
		int i4 = ClipRect.outcode(sbd.getX(), sbd.getY() + hei);

		if ((i1 | i2 | i3 | i4) == 0)
			xy.fill(s);
		else {
			if ((i1 & i2 & i3 & i4) == 0) {
				Area MyA = new Area(ClipRect);
				MyA.intersect(new Area(s));
				xy.fill(MyA);
			}
		}
	}

	@Override
	public Color getBackground() {
		return (xy.getBackground());
	}

	@Override
	public Composite getComposite() {
		return (xy.getComposite());
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return (xy.getDeviceConfiguration());
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return (xy.getFontRenderContext());
	}

	@Override
	public Paint getPaint() {
		return (xy.getPaint());
	}

	@Override
	public Object getRenderingHint(RenderingHints.Key hintKey) {
		return (xy.getRenderingHint(hintKey));
	}

	@Override
	public RenderingHints getRenderingHints() {
		return (xy.getRenderingHints());
	}

	@Override
	public Stroke getStroke() {
		return (xy.getStroke());
	}

	@Override
	public AffineTransform getTransform() {
		return (xy.getTransform());
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return (xy.hit(rect, s, onStroke));
	}

	@Override
	public void rotate(double theta) {
		xy.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		xy.rotate(theta, x, y);
	}

	@Override
	public void scale(double sx, double sy) {
		xy.scale(sx, sy);
	}

	@Override
	public void setBackground(Color color) {
		xy.setBackground(color);
	}

	@Override
	public void setComposite(Composite comp) {
		xy.setComposite(comp);
	}

	@Override
	public void setPaint(Paint paint) {
		xy.setPaint(paint);
	}

	@Override
	public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
		xy.setRenderingHint(hintKey, hintValue);
	}

	@Override
	public void setRenderingHints(Map hints) {
		xy.setRenderingHints(hints);
	}

	@Override
	public void setStroke(Stroke s) {
		xy.setStroke(s);
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		xy.setTransform(Tx);
	}

	@Override
	public void shear(double shx, double shy) {
		xy.shear(shx, shy);
	}

	@Override
	public void transform(AffineTransform Tx) {
		xy.transform(Tx);
	}

	@Override
	public void translate(double tx, double ty) {
		xy.translate(tx, ty);
	}

	@Override
	public void translate(int x, int y) {
		xy.translate(x, y);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return (xy.drawImage(img, x, y, bgcolor, observer));
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return (xy.drawImage(img, x, y, observer));
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		return (xy.drawImage(img, x, y, width, height, bgcolor, observer));
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		return (xy.drawImage(img, x, y, width, height, observer));
	}

	@Override
	public boolean drawImage(
		Image img,
		int dx1,
		int dy1,
		int dx2,
		int dy2,
		int sx1,
		int sy1,
		int sx2,
		int sy2,
		Color bgcolor,
		ImageObserver observer) {
		return (xy.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer));
	}

	@Override
	public boolean drawImage(
		Image img,
		int dx1,
		int dy1,
		int dx2,
		int dy2,
		int sx1,
		int sy1,
		int sx2,
		int sy2,
		ImageObserver observer) {
		return (xy.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer));
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		xy.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		xy.drawOval(x, y, width, height);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		xy.drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		xy.clearRect(x, y, width, height);
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		xy.clipRect(x, y, width, height);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		xy.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public Graphics create() {
		return (xy.create());
	}

	@Override
	public Graphics create(int x, int y, int width, int height) {
		return (xy.create(x, y, width, height));
	}

	@Override
	public void dispose() {
		xy.dispose();
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		xy.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		xy.drawPolyline(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		xy.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		xy.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		xy.fillOval(x, y, width, height);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		xy.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		xy.fillRect(x, y, width, height);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		xy.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void finalize() {
		xy.finalize();
	}

	@Override
	public Shape getClip() {
		return (xy.getClip());
	}

	@Override
	public Rectangle getClipBounds() {
		return (xy.getClipBounds());
	}

	@Override
	public Rectangle getClipBounds(Rectangle r) {
		return (xy.getClipBounds(r));
	}

	@Override
	public Rectangle getClipRect() {
		return (xy.getClipRect());
	}

	@Override
	public Color getColor() {
		return (xy.getColor());
	}

	@Override
	public Font getFont() {
		return (xy.getFont());
	}

	@Override
	public FontMetrics getFontMetrics() {
		return (xy.getFontMetrics());
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return (xy.getFontMetrics(f));
	}

	@Override
	public boolean hitClip(int x, int y, int width, int height) {
		return (hitClip(x, y, width, height));
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		xy.setClip(x, y, width, height);
	}

	@Override
	public void setClip(Shape clip) {
		xy.setClip(clip);
	}

	@Override
	public void setColor(Color c) {
		xy.setColor(c);
	}

	@Override
	public void setFont(Font font) {
		xy.setFont(font);
	}

	@Override
	public void setPaintMode() {
		xy.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		xy.setXORMode(c1);
	}

}


