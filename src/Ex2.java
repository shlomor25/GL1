import java.awt.*;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.opengl.util.Animator;


public class Ex2 extends KeyAdapter implements GLEventListener {
    private float yrot = 0;
    private Axis player;
    static GLU glu = new GLU();
    static GLCanvas canvas = new GLCanvas();
    static Frame frame = new Frame("World");
    static Animator animator = new Animator(canvas);

    private void setKeyboard(GLAutoDrawable drawable) {
        if (drawable instanceof Window) {
            Window window = (Window) drawable;
            window.addKeyListener(this);
        } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
            java.awt.Component comp = (java.awt.Component) drawable;
            new AWTKeyAdapter(this, drawable).addTo(comp);
        }
    }

    public void display(GLAutoDrawable drawable) {
        // light
        float material[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float position0[] = {10f, -10f, -10f, 1.0f};
        float position1[] = {-10f, 10f, -10f, 1.0f};
        float position2[] = {-10f, -10f, 10f, 1.0f};

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        player.setLookAt();
        glu.gluLookAt(player.getPos()[0], player.getPos()[1], player.getPos()[2],
                player.getLookAt()[0], player.getLookAt()[1], player.getLookAt()[2],
                player.getUp()[0], player.getUp()[1], player.getUp()[2]);

        // lights
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position1, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, position2, 0);

        // first cube
        gl.glPushMatrix();
        gl.glTranslatef(5.0f, 1.0f, -15.0f);
        gl.glRotatef(yrot, 1.0f, 0.0f, 0.0f);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        Utils.getTexture("stone").bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        Utils.makeCube(gl);
        gl.glPopMatrix();

        // second cube
        gl.glPushMatrix();
        gl.glTranslatef(-5.0f, 1.0f, -15.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        Utils.getTexture("ice").bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        Utils.makeCube(gl);
        gl.glPopMatrix();

        // third cube
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.0f, -15.0f);
        gl.glRotatef(yrot, 0.0f, 0.0f, 1.0f);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        Utils.getTexture("wood").bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        Utils.makeCube(gl);

        gl.glPopMatrix();

        // world cube
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 29.0f, -30.0f);
        gl.glScalef(30.0f, 30.0f, 30.0f);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        Utils.getTexture("world").bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        Utils.makeCube(gl);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glPopMatrix();

        gl.glEnd();
        gl.glFlush();

        this.yrot += 0.05f;
    }


    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        final float playerStep = 0.5f;
        final float cameraAngle = 2;

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor((float)(135.0 / 255.0), (float)(206.0 / 255.0), (float)(250.0 / 255.0), 0.5f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        // textures
        gl.glEnable(GL.GL_TEXTURE_2D);
        Utils.initTextures();
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);

        // lights
        Utils.initLights(gl);

        // keyboard
        this.setKeyboard(drawable);

        // axis logic
        this.player = new Axis(playerStep, cameraAngle);
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }
        float h = (float) width / (float) height;
        gl.glViewport(0, 0, 2 * width, 2 * height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // view angle in y, ratio, d between viewer to near plane and far plane
        glu.gluPerspective(50.0f, h, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                this.exit();
                break;
            case KeyEvent.VK_W:
                player.playerMoving(0, 0, 1);
                break;
            case KeyEvent.VK_S:
                player.playerMoving(0, 0, -1);
                break;
            case KeyEvent.VK_A:
                player.playerMoving(-1, 0, 0);
                break;
            case KeyEvent.VK_D:
                player.playerMoving(1, 0, 0);
                break;
            case KeyEvent.VK_E:
                player.playerMoving(0, 1, 0);
                break;
            case KeyEvent.VK_Q:
                player.playerMoving(0, -1, 0);
                break;
            case KeyEvent.VK_I:
                player.cameraMoving(1, 'x');
                break;
            case KeyEvent.VK_K:
                player.cameraMoving(-1, 'x');
                break;
            case KeyEvent.VK_L:
                player.cameraMoving(-1, 'y');
                break;
            case KeyEvent.VK_J:
                player.cameraMoving(1, 'y');
                break;
            case KeyEvent.VK_O:
                player.cameraMoving(-1, 'z');
                break;
            case KeyEvent.VK_U:
                player.cameraMoving(1, 'z');
                break;
        }
    }
    private void exit() {
        animator.stop();
        frame.dispose();
        System.exit(0);
    }


    public static void main(String[] args) {
        canvas.addGLEventListener(new Ex2());
        frame.add(canvas);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setUndecorated(true);
        frame.setVisible(true);
        animator.start();
        canvas.requestFocus();
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
