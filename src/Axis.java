import java.lang.Math;

public class Axis {

    //player coordinate
    private float x[];
    private float y[];
    private float z[];
    private float step;
    private float angle;
    private float pos[];
    private float lookAt[];
    // Trans Matrix
    private float TM[][];
    private final int LEN = 3;


    /**
     * CTOR.
     *
     * @param step trans units per key_press
     * @param angle rotate units per key_press
     *        build trans matrix
     */
    public Axis(float step, float angle) {
        this.step = step;
        this.angle = angle;
        this.x = new float[3];
        this.y= new float[3];
        this.z= new float[3];
        this.pos = new float[3];
        this.lookAt = new float[3];
        this.TM = new float[3][3];
        x[0] = 1;
        y[1] = 1;
        z[2] = -1;
        setTM();
    }


    /**
     * Normalize coordinates.
     */
    private void normalize(){
        float x_len = norm(this.x);
        float y_len = norm(this.y);
        float z_len = norm(this.z);

        for (int i = 0; i < LEN; i++) {
            this.x[i] /= x_len;
            this.y[i] /= y_len;
            this.z[i] /= z_len;
        }
    }


    /**
     * Calculate Vector length
     * 
     * @param v vector
     * @return length
     */
    private float norm(float[] v){
        return (float) Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }


    /**
     * set trans matrix.
     */
    private void setTM() {
        for (int i = 0; i < LEN; i++) {
            this.TM[i][0] = this.x[i];
            this.TM[i][1] = this.y[i];
            this.TM[i][2] = this.z[i];
        }
    }


    /**
     * Set look at.
     */
    private void setLookAt() {
        for (int i=0; i<LEN; i++){
            this.lookAt[i] = this.pos[i] + this.z[i];
        }
    }


    /**
     * Add two vectors.
     * @param x vec1
     * @param y vec2
     * @return addition.
     */
    private float[] addVectors(float[] x, float[] y) {
        float[] result = new float[3];
        for (int i =0; i< LEN; i++){
            result[i] = x[i] + y[i];
        }
        return result;
    }


    /**
     * subtract two vectors.
     * @param x vec1
     * @param y vec2
     * @return difference.
     */
    private float[] subVectors(float[] x, float[] y) {
        float[] result = new float[3];
        for (int i =0; i< LEN; i++){
            result[i] = x[i] - y[i];
        }
        return result;
    }


    /**
     * multiply vector by scalar.
     * @param x vec
     * @param s scalar
     * @return product.
     */
    private float[] multScalar(float[] x, float s) {
        float[] result = new float[3];
        for (int i =0; i< LEN; i++){
            result[i] = s * x[i];
        }
        return result;
    }


    private static float[] multiplyMatrixInVector(float[][] a, float[] x) {
        int LEN = 3;
        if (x.length != LEN || a[0].length != LEN) throw new RuntimeException("bad matrix.");
        float[] res = new float[LEN];
        for (int i = 0; i < LEN; i++)
            for (int j = 0; j < LEN; j++)
                res[i] += a[i][j] * x[j];
        return res;
    }
    

    /**
     * Rotate player's axis.
     *
     * @param angle_step rotate unit per key_press
     * @param axis axis
     */
    public void cameraMoving(float angle_step, char axis) {

        float[] new_x = this.x;
        float[] new_y = this.y;
        float[] new_z = this.z;
        float alpha = angle_step * this.angle;

        switch(axis) {
            case 'x':
                new_z = addVectors(multScalar(this.z, COS(alpha)), multScalar(this.y, SIN(alpha)));
                new_y = subVectors(multScalar(this.y, COS(alpha)), multScalar(this.z, SIN(alpha)));
                break;
            case 'y':
                new_x = addVectors(multScalar(this.x, COS(alpha)), multScalar(this.z, SIN(alpha)));
                new_z = subVectors(multScalar(this.z, COS(alpha)), multScalar(this.x, SIN(alpha)));
                break;
            case 'z':
                new_x = addVectors(multScalar(this.x, COS(alpha)), multScalar(this.y, SIN(alpha)));
                new_y = subVectors(multScalar(this.y, COS(alpha)), multScalar(this.x, SIN(alpha)));
        }
        this.x = new_x;
        this.y = new_y;
        this.z = new_z;
        normalize();
    }


    /**
     * Getting world coordinate and move player by his coordinate.
     *
     * @param x_move x position on world coordinate
     * @param y_move y position on world coordinate
     * @param z_move z position on world coordinate
     */
    public void playerMoving(float x_move, float y_move, float z_move) {
        float[] move = new float[] {x_move, y_move, z_move};
        setTM();
        float[] trans_move = multiplyMatrixInVector(this.TM, move);
        for (int i=0; i<LEN; i++){
            this.pos[i] += this.step * trans_move[i];
        }
    }


    private float SIN(float x) {
        return (float)java.lang.Math.sin((float)Math.toRadians(x));
    }

    private float COS(float x) {
        return (float)java.lang.Math.cos((float)Math.toRadians(x));
    }
}
