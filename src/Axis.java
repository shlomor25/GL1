/**
 * shlomo rabinovich 308432517
 * ori fogler 318732484
 */


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


    public Axis(float step, float angle) {
        this.step = step;
        this.angle = angle;
        this.x = new float[LEN];
        this.y = new float[LEN];
        this.z = new float[LEN];
        this.pos = new float[LEN];
        this.lookAt = new float[LEN];
        this.TM = new float[LEN][LEN];
        this.x[0] = 1;
        this.y[1] = 1;
        this.z[2] = -1;
        this.setTM();
    }

    public float[] getPos() {
        return this.pos;
    }

    public float[] getLookAt() {
        return this.lookAt;
    }

    public float[] getUp() {
        return this.y;
    }


    private void setTM() {
        for (int i = 0; i < LEN; i++) {
            this.TM[i][0] = this.x[i];
            this.TM[i][1] = this.y[i];
            this.TM[i][2] = this.z[i];
        }
    }

    public void setLookAt() {
        for (int i = 0; i < LEN; i++) {
            this.lookAt[i] = this.pos[i] + this.z[i];
        }
    }

    private void normalize() {
        float x_len = norm(this.x);
        float y_len = norm(this.y);
        float z_len = norm(this.z);

        for (int i = 0; i < LEN; i++) {
            this.x[i] /= x_len;
            this.y[i] /= y_len;
            this.z[i] /= z_len;
        }
    }

    private float norm(float[] v) {
        return (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    private float[] addVectors(float[] x, float[] y) {
        float[] result = new float[3];
        for (int i = 0; i < LEN; i++) {
            result[i] = x[i] + y[i];
        }
        return result;
    }

    private float[] subVectors(float[] x, float[] y) {
        float[] result = new float[3];
        for (int i = 0; i < LEN; i++) {
            result[i] = x[i] - y[i];
        }
        return result;
    }

    private float[] multScalar(float[] x, float s) {
        float[] result = new float[3];
        for (int i = 0; i < LEN; i++) {
            result[i] = s * x[i];
        }
        return result;
    }

    public void cameraMoving(float angle_step, char axis) {
        float[] new_x = this.x;
        float[] new_y = this.y;
        float[] new_z = this.z;
        float alpha = angle_step * this.angle;

        switch (axis) {
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

    public void playerMoving(float x_move, float y_move, float z_move) {
        float[] move = new float[]{x_move, y_move, z_move};
        setTM();
        float[] trans_move = multiplyMatrixInVector(this.TM, move);
        for (int i = 0; i < LEN; i++) {
            this.pos[i] += this.step * trans_move[i];
        }
    }

    private float[] multiplyMatrixInVector(float[][] a, float[] x) {
        if (x.length != LEN || a[0].length != LEN) throw new RuntimeException("bad matrix.");
        float[] res = new float[LEN];
        for (int i = 0; i < LEN; i++)
            for (int j = 0; j < LEN; j++)
                res[i] += a[i][j] * x[j];
        return res;
    }

    private float SIN(float x) {
        return (float) java.lang.Math.sin((float) Math.toRadians(x));
    }

    private float COS(float x) {
        return (float) java.lang.Math.cos((float) Math.toRadians(x));
    }
}
