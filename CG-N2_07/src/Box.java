import javafx.geometry.Point2D;

import javax.media.opengl.GL;


public class Box {

	private Point2D posTopLeft;
	private Point2D posTopRight;
	private Point2D posBottomLeft;
	private Point2D posBottomRight;
	private float lineWidth;
	
	public Box (Point2D posTopLeft, Point2D posBottomRight, float lineWidth) {
		this.posTopLeft = posTopLeft;
		this.posBottomRight = posBottomRight;
		this.posBottomLeft = new Point2D(posTopLeft.getX(), posBottomRight.getY());
		this.posTopRight = new Point2D(posBottomRight.getX(), posTopLeft.getY());
		this.lineWidth = lineWidth;
	}
	
	public void draw(GL gl) {
		gl.glLineWidth(lineWidth); 
		gl.glBegin(GL.GL_LINE_LOOP);
	 		gl.glVertex2d(posTopLeft.getX(), posTopLeft.getY());
	 		gl.glVertex2d(posTopRight.getX(), posTopRight.getY());
	 		gl.glVertex2d(posBottomRight.getX(), posBottomRight.getY());
	 		gl.glVertex2d(posBottomLeft.getX(), posBottomLeft.getY());
	 	gl.glEnd();
	}
	
	public boolean contains(Point2D point) {
		return (point.getX() <= posTopRight.getX() && point.getX() >= posTopLeft.getX() && //
				point.getY() <= posTopLeft.getY() && point.getY() >= posBottomLeft.getY());
	}
	
}
