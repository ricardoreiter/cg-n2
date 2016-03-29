/// \file Exemplo_N2_Jogl_Eclipse.java
/// \brief Exemplo_N2_Jogl_Eclipse: desenha uma linha na diagonal.
/// \version $Revision: 1.0 $
/// \author Dalton Reis.
/// \date 03/05/13.
/// Obs.: variaveis globais foram usadas por questoes didaticas mas nao sao recomendas para aplicacoes reais.

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javafx.geometry.Point2D;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	
	private static final float ZOOM_SENSITIVITY = 20.0f;
	private static final float MAX_PAN = 500.0f;
	private static final float MIN_PAN = -500.0f;
	
	private float totalPan = 0.0f;
	private float[] axisSizes = {-400.0f, 400.0f, -400.0f, 400.0f};
	private float[] axisMaxSizes = {-1000.0f, 1000.0f, -1000.0f, 1000.0f};
	private float[] axisMinSizes = {-100.0f, 100.0f, -100.0f, 100.0f};
	
	private Point2D[] points = {new Point2D(-100, -100), new Point2D(-100, 100), new Point2D(100, 100), new Point2D(100, -100)};
	private int currentPointMoving = 0;
	private int oldMouseX = 0;
	private int oldMouseY = 0;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1.0f);		
	}
	
	//exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluOrtho2D(axisSizes[0], axisSizes[1], axisSizes[2], axisSizes[3]);

		SRU();
		 
		 // seu desenho ...
		 gl.glColor3f(0.0f, 0.0f, 0.0f);
		 gl.glLineWidth(2.0f);
		 gl.glBegin(GL.GL_LINE_STRIP);
		 	gl.glColor3f(0.0f, 1.0f, 1.0f);
		 	gl.glVertex2d(points[0].getX(), points[0].getY());
		 	gl.glVertex2d(points[1].getX(), points[1].getY());
		 	gl.glVertex2d(points[2].getX(), points[2].getY());
		 	gl.glVertex2d(points[3].getX(), points[3].getY());
		 gl.glEnd();
		 gl.glPointSize(2.0f);
		 gl.glBegin(GL.GL_LINE_STRIP);
		 	gl.glColor3f(1.0f, 1.0f, 0.0f);
		 	LinkedList<Point2D> splinePoints = spline(points, 100);
		 	for (Point2D point : splinePoints) {
		 		gl.glVertex2d(point.getX(), point.getY());
		 	}
		 gl.glEnd();
		 gl.glPointSize(6.0f);
		 gl.glBegin(GL.GL_POINTS);
		 	gl.glColor3f(1.0f, 0.0f, 0.0f);
		 	gl.glVertex2d(points[currentPointMoving].getX(), points[currentPointMoving].getY());
		 gl.glEnd();

		 gl.glFlush();
	}	
	
	private LinkedList<Point2D> spline(Point2D[] points, int curveDensity) {
		LinkedList<Point2D> result = new LinkedList<>();
		double step = 1.0d / curveDensity;
		for (double i = 0; i <= 1f; i += step) {
			result.add(getCurvePoint(points, i));
		}
		return result;
	}
	
	private Point2D getCurvePoint(Point2D[] points2, double t) {
		Queue<Point2D> pointList = new LinkedList<>();
		for (int i = 0; i < points2.length - 1; i++) {
			pointList.add(getPointCalculated(points2[i], points2[i + 1], t));
		}
		return reduceCurvePoints(t, pointList);
	}

	private Point2D reduceCurvePoints(double t, Queue<Point2D> pointList) {
		Queue<Point2D> newPointList = new LinkedList<>();
		while (pointList.size() > 1) {
			newPointList.add(getPointCalculated(pointList.poll(), pointList.peek(), t));
		}
		if (newPointList.size() > 1) {
			return reduceCurvePoints(t, newPointList);
		}
		return newPointList.poll();
	}

	private Point2D getPointCalculated(Point2D p1, Point2D p2, double t) {
		double px = p1.getX() + ((p2.getX() - p1.getX()) * t);
		double py = p1.getY() + ((p2.getY() - p1.getY()) * t);
		return new Point2D(px, py);
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_I:
			if (canModifyAxis(0, ZOOM_SENSITIVITY)
					&& canModifyAxis(1, -ZOOM_SENSITIVITY)
					&& canModifyAxis(2, ZOOM_SENSITIVITY)
					&& canModifyAxis(3, -ZOOM_SENSITIVITY)) {
				modifyAxis(0, ZOOM_SENSITIVITY);
				modifyAxis(1, -ZOOM_SENSITIVITY);
				modifyAxis(2, ZOOM_SENSITIVITY);
				modifyAxis(3, -ZOOM_SENSITIVITY);
			}
    		glDrawable.display();
		break;
		case KeyEvent.VK_O:
			if (canModifyAxis(0, -ZOOM_SENSITIVITY)
					&& canModifyAxis(1, ZOOM_SENSITIVITY)
					&& canModifyAxis(2, -ZOOM_SENSITIVITY)
					&& canModifyAxis(3, ZOOM_SENSITIVITY)) {
				modifyAxis(0, -ZOOM_SENSITIVITY);
				modifyAxis(1, ZOOM_SENSITIVITY);
				modifyAxis(2, -ZOOM_SENSITIVITY);
				modifyAxis(3, ZOOM_SENSITIVITY);
			}
    		glDrawable.display();
		break;
		case KeyEvent.VK_E:
			if (canPan(-ZOOM_SENSITIVITY)) {
				modifyPan(0, -ZOOM_SENSITIVITY);
				modifyPan(1, -ZOOM_SENSITIVITY);
			}
    		glDrawable.display();
		break;
		case KeyEvent.VK_D:
			if (canPan(ZOOM_SENSITIVITY)) {
				modifyPan(0, ZOOM_SENSITIVITY);
				modifyPan(1, ZOOM_SENSITIVITY);
			}
    		glDrawable.display();
		break;
		case KeyEvent.VK_C:
			if (canPan(ZOOM_SENSITIVITY)) {
				modifyPan(2, ZOOM_SENSITIVITY);
				modifyPan(3, ZOOM_SENSITIVITY);
			}
    		glDrawable.display();
		break;
		case KeyEvent.VK_B:
			if (canPan(-ZOOM_SENSITIVITY)) {
				modifyPan(2, -ZOOM_SENSITIVITY);
				modifyPan(3, -ZOOM_SENSITIVITY);
			}
    		glDrawable.display();
		break;
		case KeyEvent.VK_1:
			currentPointMoving = 0;
			glDrawable.display();
			break;
		case KeyEvent.VK_2:
			currentPointMoving = 1;
			glDrawable.display();
			break;
		case KeyEvent.VK_3:
			currentPointMoving = 2;
			glDrawable.display();
			break;
		case KeyEvent.VK_4:
			currentPointMoving = 3;
			glDrawable.display();
			break;
		}
	}

	private boolean canPan(float zoom) {
		if (zoom > 0) {
			totalPan += zoom;
			if (totalPan > MAX_PAN) {
				totalPan = MAX_PAN;
				return false;
			}
		} else {
			totalPan += zoom;
			if (totalPan < MIN_PAN) {
				totalPan = MIN_PAN;
				return false;
			}
		}
		return true;
	}
	
	private void modifyPan(int axis, float zoom) {
		axisMaxSizes[axis] += zoom;
		axisMinSizes[axis] += zoom;
		
		axisSizes[axis] += zoom;
		System.out.println(String.format("Max = {%s, %s, %s, %s}", axisMaxSizes[0], axisMaxSizes[1], axisMaxSizes[2], axisMaxSizes[3]));
		System.out.println(String.format("Min = {%s, %s, %s, %s}", axisMinSizes[0], axisMinSizes[1], axisMinSizes[2], axisMinSizes[3]));
	}
	
	private void modifyAxis(int axis, float zoom) {
		axisSizes[axis] += zoom;
		
		System.out.println(String.format("{%s} = {%s}", axis, axisSizes[axis]));
	}
	
	private boolean canModifyAxis(int axis, float zoom) {
		if (axisMaxSizes[axis] > 0 && axisSizes[axis] > axisMaxSizes[axis] && zoom > 0) {
			return false;
		} else if (axisMaxSizes[axis] < 0 && axisSizes[axis] < axisMaxSizes[axis] && zoom < 0) {
			return false;
		}
		if (axisMinSizes[axis] > 0 && axisSizes[axis] < axisMinSizes[axis] && zoom < 0) {
			return false;
		} else if (axisMinSizes[axis] < 0 && axisSizes[axis] > axisMinSizes[axis] && zoom > 0) {
			return false;
		}
		return true;
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println(" --- reshape ---");
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}
	
	public void SRU() {
//		gl.glDisable(gl.GL_TEXTURE_2D);
//		gl.glDisableClientState(gl.GL_TEXTURE_COORD_ARRAY);
//		gl.glDisable(gl.GL_LIGHTING); //TODO: [D] FixMe: check if lighting and texture is enabled

		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin( GL.GL_LINES );
			gl.glVertex2f( -200.0f, 0.0f );
			gl.glVertex2f(  200.0f, 0.0f );
			gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin( GL.GL_LINES);
			gl.glVertex2f(  0.0f, -200.0f);
			gl.glVertex2f(  0.0f, 200.0f );
		gl.glEnd();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
	    oldMouseX = e.getX();
	    oldMouseY = e.getY();
	}
	    
	@Override
	public void mouseDragged(MouseEvent e) {
	    int movtoX = e.getX() - oldMouseX;
	    int movtoY = e.getY() - oldMouseY;
	    points[currentPointMoving] = new Point2D(points[currentPointMoving].getX() + movtoX, points[currentPointMoving].getY() - movtoY);
	    
	    oldMouseX = e.getX();
	    oldMouseY = e.getY();

		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
}
