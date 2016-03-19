/// \file Exemplo_N2_Jogl_Eclipse.java
/// \brief Exemplo_N2_Jogl_Eclipse: desenha uma linha na diagonal.
/// \version $Revision: 1.0 $
/// \author Dalton Reis.
/// \date 03/05/13.
/// Obs.: variaveis globais foram usadas por questoes didaticas mas nao sao recomendas para aplicacoes reais.

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener {
	
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

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaco de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);		
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
		 gl.glPointSize(1.5f);
		 gl.glBegin(GL.GL_POINTS);
		 	for (int i = 0; i < 72; i++) {
		 		double angle = Math.toRadians(5 * i);
		 		double x = Math.cos(angle) * 100;
		 		double y = Math.sin(angle) * 100;
		 		gl.glVertex2d(x, y);
		 	}
		 gl.glEnd();

		 gl.glFlush();
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

}
