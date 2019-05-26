import java.awt.Frame;
import java.io.File;
import java.io.IOException;
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
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;


public class Ex2 extends KeyAdapter implements GLEventListener {
    private float yrot = 0;        // Y Rotation ( NEW )
    private Texture texture;
    private Texture worldTexture;
    private Axis player;
    private final float playerStep = 0.2f;
    private final float cameraAngle = 2;

    static GLU glu = new GLU();
    static GLCanvas canvas = new GLCanvas();
    static Frame frame = new Frame("Ex2 Player Movement");
    static Animator animator = new Animator(canvas);


    private void makeCube(GL2 gl){
        gl.glBegin(GL2.GL_QUADS);
        // Front Face - box
        gl.glNormal3f(0,0,1);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        // Back Face
        gl.glNormal3f(0,0,-1);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        // Top Face
        gl.glNormal3f(0,1,0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        // Bottom Face
        gl.glNormal3f(0,-1,0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        // Right face
        gl.glNormal3f(1,0,0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        // Left Face
        gl.glNormal3f(-1,0,0);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
    }


    public void display(GLAutoDrawable drawable) {
        // light
        float material[] = {1.0f,1.0f,1.0f,1.0f};
        float position0[] = {10f,0f,-5f,1.0f};  // red light on the right side (light 0)
        float position1[] = {-10f,0f,-5f,1.0f};	// blue light on the left side (light 1)

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();  // Reset The View

        player.setLookAt();
        glu.gluLookAt(player.getPos()[0],player.getPos()[1],player.getPos()[2],
                player.getLookAt()[0], player.getLookAt()[1], player.getLookAt()[2],
                player.getY()[0],player.getY()[1],player.getY()[2]); //set the camera view that's the y axis

        // Light
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position1, 0);

        gl.glPushMatrix();
        gl.glTranslatef(1.0f, 10.0f, -7.0f);

        //gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, -1.0f, 0.0f);
        //gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

        gl.glTexParameteri ( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
        gl.glTexParameteri( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
        texture.bind(gl);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        makeCube(gl);

        //second cube
        gl.glPushMatrix();
        gl.glTranslatef(5.0f, 1.0f, -14.0f);

        //gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        //gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

        gl.glTexParameteri ( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
        gl.glTexParameteri( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
        texture.bind(gl);

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        makeCube(gl);

        //3th cube
        gl.glPushMatrix();
        gl.glTranslatef(10.0f, 1.0f, -14.0f);

        //gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        //gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

        gl.glTexParameteri ( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
        gl.glTexParameteri( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
        texture.bind(gl);

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        makeCube(gl);

        //world cube
        gl.glPushMatrix();

        gl.glTranslatef(0.0f, 59.0f, -58.0f);
        gl.glScalef(60.0f, 60.0f, 60.0f);

        gl.glTexParameteri ( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
        gl.glTexParameteri( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
        worldTexture.bind(gl);

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, material, 0);
        makeCube(gl);
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glPopMatrix();
        gl.glEnd();
        gl.glFlush();

        //xrot += 0.03f;
        yrot += 0.05f;
        //zrot += 0.04f;
    }


    public void init(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
        gl.glClearDepth(1.0f);                      // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);              // Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);               // The Type Of Depth Testing To Do
        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        // Texture
        gl.glEnable(GL.GL_TEXTURE_2D);
        try {
            String filename="resources/Picture1.jpg"; // the FileName to open
            String fileTexture="resources/sky.jpg";
            texture=TextureIO.newTexture(new File( filename ),true);
            worldTexture=TextureIO.newTexture(new File( fileTexture ),true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);

        // Light
        float	ambient[] = {0.1f,0.1f,0.1f,1.0f};
        float	diffuse0[] = {1f,0f,0f,1.0f};
        float	diffuse1[] = {0f,0f,1f,1.0f};


        gl.glShadeModel(GL2.GL_SMOOTH);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse0, 0);
        gl.glEnable(GL2.GL_LIGHT0);

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse1, 0);
        gl.glEnable(GL2.GL_LIGHT1);

        gl.glEnable(GL2.GL_LIGHTING);

        // Keyboard
        if (drawable instanceof Window) {
            Window window = (Window) drawable;
            window.addKeyListener(this);
        } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
            java.awt.Component comp = (java.awt.Component) drawable;
            new AWTKeyAdapter(this, drawable).addTo(comp);
        }
        //create the player axis
        player = new Axis(playerStep, cameraAngle);

    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if(height <= 0) {
            height = 1;
        }
        float h = (float)width / (float)height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, h, 1.0, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Get key and apply the right method.
     *
     * @param e key
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                exit();
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

    /**
     * Safe exit.
     */
    public static void exit(){
        animator.stop();
        frame.dispose();
        System.exit(0);
    }


    /**
     * Main.
     */
    public static void main(String[] args) {
        canvas.addGLEventListener(new Ex2());
        frame.add(canvas);
        frame.setSize(1280, 720);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.setVisible(true);
        animator.start();
        canvas.requestFocus();
    }


    @Override
    public void dispose(GLAutoDrawable arg0) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
}