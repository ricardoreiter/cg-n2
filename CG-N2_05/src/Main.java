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
	
	private int[] geometricPrimitives = {GL.GL_POINTS, GL.GL_LINES, GL.GL_LINE_LOOP, GL.GL_LINE_STRIP, GL.GL_TRIANGLES, GL.GL_TRIANGLE_FAN, GL.GL_TRIANGLE_STRIP, GL.GL_QUADS, GL.GL_QUAD_STRIP, GL.GL_POLYGON};
	private int currentGeometric = 0;
	
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
		glu.gluOrtho2D(-400, 400, -400, 400);

		SRU();
		 
		 // seu desenho ...
		 gl.glColor3f(0.0f, 0.0f, 0.0f);
		 gl.glLineWidth(3f);
		 gl.glPointSize(3f);
		 gl.glBegin(geometricPrimitives[currentGeometric]);
		 	gl.glColor3f(0.0f, 1.0f, 0.0f);
	 		gl.glVertex2d(200, 200);
	 		
	 		gl.glColor3f(1.0f, 0.0f, 0.0f);
	 		gl.glVertex2d(200, -200);
	 		
	 		gl.glColor3f(0.0f, 0.0f, 1.0f);
	 		gl.glVertex2d(-200, -200);
	 		
	 		gl.glColor3f(1.0f, 0.0f, 1.0f);
	 		gl.glVertex2d(-200, 200);
		 gl.glEnd();

		 gl.glFlush();
	}	

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			currentGeometric++;
			if (currentGeometric >= geometricPrimitives.length) {
				currentGeometric = 0;
			}
    		glDrawable.display();
		break;
		}
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
