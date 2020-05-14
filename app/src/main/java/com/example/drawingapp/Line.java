package com.example.drawingapp;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Line {
    float x1, y1, x2, y2;
    Matrix matrix = new Matrix();
    Matrix backup = new Matrix();

    Line(float _x1, float _y1, float _x2, float _y2) {
        x1 = _x1;
        y1 = _y1;
        x2 = _x2;
        y2 = _y2;
    }

    Line(Line line) {
        x1 = line.x1;
        y1 = line.y1;
        x2 = line.x2;
        y2 = line.y2;
        matrix = line.matrix;
    }

    // Translate by dx, dy
    void translate(float dx, float dy) {
        matrix.set(backup);
        matrix.postTranslate(dx, dy);
    }

    // Scale by sx, sy
    void scale(float sx, float sy, float pX, float pY) {
        matrix.set(backup);
        matrix.postScale(sx, sy, pX, pY);
    }

    // Draw using the current matrix
    void draw(Canvas canvas, Paint paint) {
        Matrix oldMatrix = canvas.getMatrix();
        canvas.setMatrix(matrix);
        canvas.drawLine(x1, y1, x2, y2, paint);
        canvas.setMatrix(oldMatrix);
    }

    // Perform collision detection with a point
    boolean collision(float pX, float pY) {
        float [] values = new float[9];
        matrix.getValues(values);
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];
        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];
        float d1 = (float)sqrt(pow(x1 * scaleX + transX - pX, 2) + pow(y1 * scaleY + transY - pY, 2));
        float d2 = (float)sqrt(pow(x2 * scaleX + transX - pX, 2) + pow(y2 * scaleY + transY - pY, 2));
        float lineLen = (float) sqrt(pow(x1 * scaleX - x2 * scaleX, 2) + pow(y1 * scaleY - y2 * scaleY, 2));
        float buffer = (float) 1;
        if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
            return true;
        }
        return false;
    }
}
