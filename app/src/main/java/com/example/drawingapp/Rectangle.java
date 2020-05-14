package com.example.drawingapp;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Rectangle {
    float startX, startY, endX, endY;
    Matrix matrix = new Matrix(); // identity matrix
    Matrix backup = new Matrix();

    // Construct a rectangle with the given dimensions
    Rectangle(float _startX, float _startY, float _endX, float _endY) {
        startX = _startX;
        startY = _startY;
        endX = _endX;
        endY = _endY;
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
        canvas.drawRect(startX, startY, endX, endY, paint);
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
        if ((startX * scaleX + transX <= pX && pX <= endX * scaleX + transX ||
                endX * scaleX + transX <= pX && pX <= startX * scaleX + transX) &&
                (startY * scaleY + transY <= pY && pY <= endY * scaleY + transY ||
                        endY * scaleY + transY <= pY && pY <= startY * scaleY + transY)){
            return true;
        }
        return false;
    }
}
