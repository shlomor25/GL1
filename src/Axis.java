import java.lang.Math;

public class Axis {

    //player coordinate
    private double x[];
    private double y[];
    private double z[];
    private double step;
    private double angle;
    private double pos[];
    private double lookAt[];
    // Trans Matrix
    private double TM[][];
    final int LEN = 3;

    /**
     * CTOR.
     *
     * @param step trans units per key_press
     * @param angle rotate units per key_press
     *        build trans matrix
     */
    public Axis(double step, double angle) {
        this.step = step;
        this.angle = angle;
        this.x = new double[3];
        this.y= new double[3];
        this.z= new double[3];
        this.pos = new double[3];
        this.lookAt = new double[3];
        this.TM = new double[3][3];
        x[0] = 1;
        y[1] = 1;
        z[2] = -1;
        setTM();
    }
    
    /**
     * Normalize coordinates.
     */
    private void normalize(){
        double x_len = norm(this.x);
        double y_len = norm(this.y);
        double z_len = norm(this.z);

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
    private double norm(double[] v){
        return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }
    
    /**
     * set trans matrix.
     */
    public void setTM() {
        for (int i = 0; i < LEN; i++) {
            this.TM[i][0] = this.x[i];
            this.TM[i][1] = this.y[i];
            this.TM[i][2] = this.z[i];
        }
    }

    /**
     * Set look at.
     */
    public void setLookAt() {
        for (int i=0; i<LEN; i++){
            lookAt[i] = this.pos[i] + this.z[i];
        }
    }

    /**
     * Add two vectors.
     * @param x vec1
     * @param y vec2
     * @return addition.
     */
    private double[] addVectors(double[] x, double[] y) {
        double[] result = new double[3];
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
    private double[] subVectors(double[] x, double[] y) {
        double[] result = new double[3];
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
    private double[] multScalar(double[] x, double s) {
        double[] result = new double[3];
        for (int i =0; i< LEN; i++){
            result[i] = s * x[i];
        }
        return result;
    }

    private static double[] multiplyMatrixInVector(double[][] a, double[] x) {
        int LEN = 3;
        if (x.length != LEN || a[0].length != LEN) throw new RuntimeException("bad matrix.");
        double[] res = new double[LEN];
        for (int i = 0; i < LEN; i++)
            for (int j = 0; j < LEN; j++)
                res[i] += a[i][j] * x[j];
        return res;
    }
    
    
    /**
     * Transpose
     *
     * @param old old matrix
     * @return transpose matrix
     */
//    private double[][] transpose(double[][] old){
//        double[][] t = new double[3][3];
//        for (int i =0; i<3; i++) {
//            for (int j = 0; j < 3; j++) {
//                t[i][j]=old[j][i];
//            }
//        }
//
//        return t;
//    }

    private double SIN(double x) {
        return (double)java.lang.Math.sin((double)Math.toRadians(x));
    }

    private double COS(double x) {
        return (double)java.lang.Math.cos((double)Math.toRadians(x));
    }
}
