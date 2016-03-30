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
	
	private static final Point2D INITIAL_MANCHE_POS = new Point2D(200, 200);
	private static final float ZOOM_SENSITIVITY = 20.0f;
	private static final float[] IN_BOX_COLOR = {1.0f, 0.0f, 1.0f};
	private static final float[] OUT_BOX_COLOR = {1.0f, 1.0f, 0.0f};
	private static final float[] EQUAL_RADIOUS_BOX_COLOR = {0.0f, 1.0f, 1.0f};
	
	private float[] axisSizes = {-400.0f, 400.0f, -400.0f, 400.0f};
	private float[] axisMaxSizes = {-1000.0f, 1000.0f, -1000.0f, 1000.0f};
	private float[] axisMinSizes = {-100.0f, 100.0f, -100.0f, 100.0f};
	
	private Circle innerCircle = new Circle(INITIAL_MANCHE_POS, 50, 5, 1.0f);
	private Circle outerCircle = new Circle(INITIAL_MANCHE_POS, 150, 5, 1.0f);
	private Box innerBox = new Box(getBoxPoint(INITIAL_MANCHE_POS, 150, 135), getBoxPoint(INITIAL_MANCHE_POS, 150, -45), 1.0f);
	private int oldMouseX = 0;
	private int oldMouseY = 0;
	private float[] currentColor = IN_BOX_COLOR;

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
		 
		if (outerCircle.isOnLimit(innerCircle.getPos())) {
			currentColor = EQUAL_RADIOUS_BOX_COLOR;
		} else if (innerBox.contains(innerCircle.getPos())) {
			currentColor = IN_BOX_COLOR;
		} else {
			currentColor = OUT_BOX_COLOR;
		}
		gl.glColor3f(currentColor[0], currentColor[1], currentColor[2]);
		innerBox.draw(gl);
		
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		outerCircle.draw(gl);
		innerCircle.draw(gl);
		
		gl.glPointSize(7.0f);
		gl.glBegin(GL.GL_POINTS);
			gl.glVertex2d(innerCircle.getPos().getX(), innerCircle.getPos().getY());
		gl.glEnd();
		gl.glFlush();
	}	
	
	private Point2D getBoxPoint(Point2D initialPos, int radius, float angle) {
 		double angleRadians = Math.toRadians(angle);
 		double x = (Math.cos(angleRadians) * radius) + initialPos.getX();
 		double y = (Math.sin(angleRadians) * radius) + initialPos.getY();
 		return new Point2D(x, y);
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
			modifyPan(0, -ZOOM_SENSITIVITY);
			modifyPan(1, -ZOOM_SENSITIVITY);
    		glDrawable.display();
		break;
		case KeyEvent.VK_D:
			modifyPan(0, ZOOM_SENSITIVITY);
			modifyPan(1, ZOOM_SENSITIVITY);
    		glDrawable.display();
		break;
		case KeyEvent.VK_C:
			modifyPan(2, ZOOM_SENSITIVITY);
			modifyPan(3, ZOOM_SENSITIVITY);
    		glDrawable.display();
		break;
		case KeyEvent.VK_B:
			modifyPan(2, -ZOOM_SENSITIVITY);
			modifyPan(3, -ZOOM_SENSITIVITY);
    		glDrawable.display();
		break;
		}
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
		if (axis == 0 || axis == 2) {
			if (axisSizes[axis] < axisMaxSizes[axis] && zoom < 0) {
				return false;
			} else if (axisSizes[axis] > axisMinSizes[axis] && zoom > 0) {
				return false;
			}
		} else if (axis == 1 || axis == 3) {
			if (axisSizes[axis] < axisMinSizes[axis] && zoom < 0) {
				return false;
			} else if (axisSizes[axis] > axisMaxSizes[axis] && zoom > 0) {
				return false;
			}
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
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		innerCircle.setPos(INITIAL_MANCHE_POS);
		glDrawable.display();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    oldMouseX = e.getX();
	    oldMouseY = e.getY();
	}
	    
	@Override
	public void mouseDragged(MouseEvent e) {
	    int movtoX = e.getX() - oldMouseX;
	    int movtoY = e.getY() - oldMouseY;
	    Point2D newPos = new Point2D(innerCircle.getPos().getX() + movtoX, innerCircle.getPos().getY() - movtoY);
	    
	    if (outerCircle.contains(newPos)) {
	    	innerCircle.setPos(newPos);
	    }
	    
	    oldMouseX = e.getX();
	    oldMouseY = e.getY();

		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
}
