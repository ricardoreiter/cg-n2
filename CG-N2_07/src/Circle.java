import javafx.geometry.Point2D;

import javax.media.opengl.GL;


public class Circle {

	private Point2D pos;
	private int radius;
	private int pointAngleDistance;
	private float lineWidth;
	
	public Circle (Point2D pos, int radius, int pointAngleDistance, float lineWidth) {
		this.pos = pos;
		this.radius = radius;
		this.pointAngleDistance = pointAngleDistance;
		this.lineWidth = lineWidth;
	}
	
	public Point2D getPos() {
		return pos;
	}

	public void setPos(Point2D pos) {
		this.pos = pos;
	}

	public void draw(GL gl) {
		gl.glLineWidth(lineWidth); 
		gl.glBegin(GL.GL_LINE_LOOP);
			for (int i = 0; i < 72; i++) {
		 		double angle = Math.toRadians(pointAngleDistance * i);
		 		double x = (Math.cos(angle) * radius) + pos.getX();
		 		double y = (Math.sin(angle) * radius) + pos.getY();
		 		gl.glVertex2d(x, y);
		 	}
		 gl.glEnd();
	}
	
	public boolean contains(Point2D point) {
		double distance = getDistanceFromRadius(point);
	    return distance <= radius;
	}
	
	public boolean isOnLimit(Point2D point) {
		double distance = getDistanceFromRadius(point);
	    return distance <= radius && distance >= radius -1.5;
	}

	private double getDistanceFromRadius(Point2D point) {
		return Math.abs(Math.sqrt(Math.pow(point.getX() - pos.getX(), 2) + Math.pow(point.getY() - pos.getY(), 2)));
	}
	
}
