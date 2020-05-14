package com.example.drawingapp;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;


public class AppView extends View {

    Paint drawPaint;
    int curColour;
    String toolSelected;
    Context context;
    float startX, startY, endX, endY;
    List shapes;
    ArrayList<Paint> paints;
    ArrayList<String> shapeTypes;
    boolean selected;
    MainActivity activity;
    String mode;
    float scaleSpacing;

    public AppView(Context c, AttributeSet attr) {
        super(c, attr);
        context = c;
        drawPaint = new Paint();
        curColour = ContextCompat.getColor(c, R.color.red);
        drawPaint.setColor(curColour);
        drawPaint.setStrokeWidth(10);
        toolSelected = "line";
        shapes = new ArrayList();
        paints = new ArrayList();
        shapeTypes = new ArrayList();
        selected = false;

    }

    public boolean getSelected() {
        return selected;
    }

    public void setActivity(MainActivity a) {
        activity = a;
    }

    public void setTool(String s) {
        toolSelected = s;
    }

    public void setPaint(String c) {
        if (c == "red") {
            curColour = ContextCompat.getColor(context, R.color.red);
        } else if (c == "orange") {
            curColour = ContextCompat.getColor(context, R.color.orange);
        } else if (c == "yellow") {
            curColour = ContextCompat.getColor(context, R.color.yellow);
        } else if (c == "green") {
            curColour = ContextCompat.getColor(context, R.color.green);
        } else if (c == "blue") {
            curColour = ContextCompat.getColor(context, R.color.blue);
        } else if (c == "purple") {
            curColour = ContextCompat.getColor(context, R.color.purple);
        } else if (c == "grey") {
            curColour = ContextCompat.getColor(context, R.color.grey);
        }
        drawPaint.setColor(curColour);
    }

    public void colourLastShape() {
        int numShapes = shapes.size();
        Paint p = new Paint(drawPaint);
        p.setStyle(Paint.Style.STROKE);
        DashPathEffect d = new DashPathEffect(new float[]{5, 5}, (float) 1.0);
        p.setPathEffect(d);
        paints.set(numShapes - 1, p);
        invalidate();
    }

    public void resetLastShape() {
        int numShapes = shapes.size();
        if (shapeTypes.get(numShapes - 1).equals("line")) {
            paints.get(numShapes - 1).setPathEffect(null);
        } else if (shapeTypes.get(numShapes - 1).equals("rectangle") ||
            shapeTypes.get(numShapes - 1).equals("circle")) {
            paints.get(numShapes - 1).setPathEffect(null);
            paints.get(numShapes - 1).setStyle(Paint.Style.FILL);
        }
    }

    public void removeLast() {
        int numShapes = shapes.size();
        shapes.remove(numShapes - 1);
        paints.remove(numShapes - 1);
        shapeTypes.remove(numShapes - 1);
        selected = false;
        if (numShapes == 1) {
            activity.eraserButton.setEnabled(false);
        }
        activity.lineButton.setEnabled(true);
        activity.rectangleButton.setEnabled(true);
        activity.circleButton.setEnabled(true);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                // Set the end to prevent initial jump
                endX = event.getX();
                endY = event.getY();
                int numShapes = shapes.size();
                if (toolSelected.equals("selection")) {
                    mode = "drag";
                    if (selected) {
                        resetLastShape();
                        selected = false;
                    }
                    for (int i = numShapes - 1; i >= 0; --i) {
                        if (shapes.get(i).getClass().getName().equals("com.example.drawingapp.Line")) {
                            Line line = (Line) shapes.get(i);
                            if (line.collision(startX, startY)) {
                                shapes.add(shapes.get(i));
                                Paint p = new Paint(drawPaint);
                                p.setColor(paints.get(i).getColor());
                                DashPathEffect d = new DashPathEffect(new float[]{5, 5}, (float) 1.0);
                                p.setPathEffect(d);
                                p.setStyle(Paint.Style.STROKE);
                                paints.add(p);
                                shapeTypes.add(shapeTypes.get(i));
                                setActivityColour(paints.get(i));
                                shapes.remove(i);
                                paints.remove(i);
                                shapeTypes.remove(i);
                                selected = true;
                                activity.lineButton.setEnabled(false);
                                activity.circleButton.setEnabled(false);
                                activity.rectangleButton.setEnabled(false);
                                activity.eraserButton.setEnabled(true);
                                line.backup.set(line.matrix);
                                break;
                            }
                        } else if (shapes.get(i).getClass().getName().equals("com.example.drawingapp.Rectangle")) {
                            Rectangle rect = (Rectangle) shapes.get(i);
                            if (rect.collision(startX, startY)) {
                                shapes.add(shapes.get(i));
                                Paint p = new Paint(drawPaint);
                                p.setColor(paints.get(i).getColor());
                                DashPathEffect d = new DashPathEffect(new float[]{5, 5}, (float) 1.0);
                                p.setPathEffect(d);
                                p.setStyle(Paint.Style.STROKE);
                                paints.add(p);
                                shapeTypes.add(shapeTypes.get(i));
                                setActivityColour(paints.get(i));
                                shapes.remove(i);
                                paints.remove(i);
                                shapeTypes.remove(i);
                                selected = true;
                                activity.lineButton.setEnabled(false);
                                activity.circleButton.setEnabled(false);
                                activity.rectangleButton.setEnabled(false);
                                activity.eraserButton.setEnabled(true);
                                rect.backup.set(rect.matrix);
                                break;
                            }
                        } else if (shapes.get(i).getClass().getName().equals("com.example.drawingapp.Circle")) {
                            Circle circ = (Circle) shapes.get(i);
                            if (circ.collision(startX, startY)) {
                                shapes.add(shapes.get(i));
                                Paint p = new Paint(drawPaint);
                                p.setColor(paints.get(i).getColor());
                                DashPathEffect d = new DashPathEffect(new float[]{5, 5}, (float) 1.0);
                                p.setPathEffect(d);
                                p.setStyle(Paint.Style.STROKE);
                                paints.add(p);
                                shapeTypes.add(shapeTypes.get(i));
                                setActivityColour(paints.get(i));
                                shapes.remove(i);
                                paints.remove(i);
                                shapeTypes.remove(i);
                                selected = true;
                                activity.lineButton.setEnabled(false);
                                activity.circleButton.setEnabled(false);
                                activity.rectangleButton.setEnabled(false);
                                activity.eraserButton.setEnabled(true);
                                circ.backup.set(circ.matrix);
                                break;
                            }
                        }
                        if (i == 0) {
                            selected = false;
                            activity.lineButton.setEnabled(true);
                            activity.circleButton.setEnabled(true);
                            activity.rectangleButton.setEnabled(true);
                            activity.eraserButton.setEnabled(false);
                            resetLastShape();
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (toolSelected.equals("selection") && getSelected()) {
                    mode = "scale";
                    scaleSpacing = (float) Math.sqrt(pow(event.getX(1) - event.getX(0), 2)
                            + pow(event.getY(1) - event.getY(0), 2));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                numShapes = shapes.size();
                if (getSelected() && toolSelected.equals("selection")) {
                    // get selected shape
                    if (mode.equals("drag")) {
                        if (shapes.get(numShapes - 1).getClass().getName().equals("com.example.drawingapp.Line")) {
                            Line line = (Line) shapes.get(numShapes - 1);
                            line.translate(endX - startX, endY - startY);
                        } else if (shapes.get(numShapes - 1).getClass().getName().equals("com.example.drawingapp.Rectangle")) {
                            Rectangle rect = (Rectangle) shapes.get(numShapes - 1);
                            rect.translate(endX - startX, endY - startY);
                        } else if (shapes.get(numShapes - 1).getClass().getName().equals("com.example.drawingapp.Circle")) {
                            Circle circ = (Circle) shapes.get(numShapes - 1);
                            circ.translate(endX - startX, endY - startY);
                        }
                    } else if (mode.equals("scale")) {
                        float newSpacing = (float) Math.sqrt(pow(event.getX(1) - event.getX(0), 2)
                                + pow(event.getY(1) - event.getY(0), 2));
                        float midX = (event.getX(1) + event.getX(0)) / 2;
                        float midY = (event.getY(1) + event.getY(0)) / 2;
                        float scale = newSpacing / scaleSpacing;
                        if (shapes.get(numShapes - 1).getClass().getName().equals("com.example.drawingapp.Line")) {
                            Line line = (Line) shapes.get(numShapes - 1);
                            line.scale(scale, scale, midX, midY);
                        } else if (shapes.get(numShapes - 1).getClass().getName().equals("com.example.drawingapp.Rectangle")) {
                            Rectangle rect = (Rectangle) shapes.get(numShapes - 1);
                            rect.scale(scale, scale, midX, midY);
                        } else if (shapes.get(numShapes - 1).getClass().getName().equals("com.example.drawingapp.Circle")) {
                            Circle circ = (Circle) shapes.get(numShapes - 1);
                            circ.scale(scale, scale, midX, midY);
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                invalidate();
                if (toolSelected == "line") {
                    shapes.add(new Line(startX, startY, endX, endY));
                    Paint p = new Paint(drawPaint);
                    p.setPathEffect(null);
                    p.setStyle(Paint.Style.STROKE);
                    paints.add(p);
                    shapeTypes.add("line");
                } else if (toolSelected == "rectangle") {
                    shapes.add(new Rectangle(startX, startY, endX, endY));
                    Paint p = new Paint(drawPaint);
                    p.setPathEffect(null);
                    p.setStyle(Paint.Style.FILL);
                    paints.add(p);
                    shapeTypes.add("rectangle");
                } else if (toolSelected == "circle") {
                    shapes.add(new Circle(startX, startY, endX, endY));
                    Paint p = new Paint(drawPaint);
                    p.setPathEffect(null);
                    p.setStyle(Paint.Style.FILL);
                    paints.add(p);
                    shapeTypes.add("circle");
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        redraw(canvas);
        if (toolSelected == "line") {
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setPathEffect(null);
            Line line = new Line(startX, startY, endX, endY);
            line.draw(canvas, drawPaint);
        } else if (toolSelected == "rectangle") {
            drawPaint.setStyle(Paint.Style.FILL);
            drawPaint.setPathEffect(null);
            Rectangle rectangle = new Rectangle(startX, startY, endX, endY);
            rectangle.draw(canvas, drawPaint);
        } else if (toolSelected == "circle") {
            drawPaint.setStyle(Paint.Style.FILL);
            drawPaint.setPathEffect(null);
            Circle circle = new Circle(startX, startY, endX, endY);
            circle.draw(canvas, drawPaint);
        }
    }

    private void redraw(Canvas canvas) {
        int numShapes = shapes.size();
        for (int i = 0; i < numShapes; ++i) {
            if (shapeTypes.get(i).equals("line")) {
                Line line = (Line) shapes.get(i);
                line.draw(canvas, paints.get(i));
            } else if (shapeTypes.get(i).equals("rectangle")) {
                Rectangle rect = (Rectangle) shapes.get(i);
                rect.draw(canvas, paints.get(i));
            } else if (shapeTypes.get(i).equals("circle")) {
                Circle circ = (Circle) shapes.get(i);
                circ.draw(canvas, paints.get(i));
            }
        }
    }

    private void setActivityColour(Paint paint) {
        if (paint.getColor() == ContextCompat.getColor(context, R.color.red)) {
            activity.setRedClicked();
        } else if (paint.getColor() == ContextCompat.getColor(context, R.color.orange)) {
            activity.setOrangeClicked();
        } else if (paint.getColor() == ContextCompat.getColor(context, R.color.yellow)) {
            activity.setYellowClicked();
        } else if (paint.getColor() == ContextCompat.getColor(context, R.color.green)) {
            activity.setGreenClicked();
        } else if (paint.getColor() == ContextCompat.getColor(context, R.color.blue)) {
            activity.setBlueClicked();
        } else if (paint.getColor() == ContextCompat.getColor(context, R.color.purple)) {
            activity.setPurpleClicked();
        } else if (paint.getColor() == ContextCompat.getColor(context, R.color.grey)) {
            activity.setGreyClicked();
        }
    }
}
