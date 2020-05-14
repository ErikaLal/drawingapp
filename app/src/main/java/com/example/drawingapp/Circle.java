package com.example.drawingapp;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Circle {
    float startX, startY;
    float radius;
    Matrix matrix = new Matrix(); // identity matrix
    Matrix backup = new Matrix();


    // Construct a circle with the given dimensions
    Circle(float _startX, float _startY, float _endX, float _endY) {
        startX = _startX;
        startY = _startY;
        radius = (float)sqrt(pow(_endX - _startX, 2) + pow(_endY - _startY, 2));
    }

    Circle(float _startX, float _startY, float _radius) {
        startX = _startX;
        startY = _startY;
        radius = _radius;
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
        canvas.drawCircle(startX, startY, radius, paint);
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
        if (sqrt(pow(startX * scaleX + transX - pX, 2) + pow(startY * scaleY + transY - pY, 2)) <= radius * scaleX) {
            return true;
        }
        return false;
    }

}
