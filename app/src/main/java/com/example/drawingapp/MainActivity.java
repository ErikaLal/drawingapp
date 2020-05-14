package com.example.drawingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppView appView;

    ToggleButton lineButton;
    ToggleButton rectangleButton;
    ToggleButton circleButton;
    ToggleButton selectButton;
    ImageButton eraserButton;

    ToggleButton redButton;
    ToggleButton orangeButton;
    ToggleButton yellowButton;
    ToggleButton greenButton;
    ToggleButton blueButton;
    ToggleButton purpleButton;
    ToggleButton greyButton;

    @Override
    protected void onPause() {
        super.onPause();
        try {
            File cache = new File(getApplicationContext().getFilesDir(), "file.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(cache));
            bw.write(appView.toolSelected + '\n');
            bw.write(Integer.toString(appView.curColour) + '\n');
            int numShapes = appView.shapes.size();
            for (int i = 0; i < numShapes; ++i) {
                bw.write(appView.shapeTypes.get(i) + '\n');
                if (appView.shapeTypes.get(i).equals("line")){
                    Line line = (Line) appView.shapes.get(i);
                    bw.write(Integer.toString(appView.paints.get(i).getColor()) + '\n');
                    bw.write(Float.toString(line.x1) + '\n');
                    bw.write(Float.toString(line.y1) + '\n');
                    bw.write(Float.toString(line.x2) + '\n');
                    bw.write(Float.toString(line.y2) + '\n');
                    float [] values = new float[9];
                    line.matrix.getValues(values);
                    for (int j = 0; j < 9; ++j) {
                        bw.write(Float.toString(values[j]) + '\n');
                    }
                } else if (appView.shapeTypes.get(i).equals("rectangle")) {
                    Rectangle rect = (Rectangle) appView.shapes.get(i);
                    bw.write(Integer.toString(appView.paints.get(i).getColor()) + '\n');
                    bw.write(Float.toString(rect.startX) + '\n');
                    bw.write(Float.toString(rect.startY) + '\n');
                    bw.write(Float.toString(rect.endX) + '\n');
                    bw.write(Float.toString(rect.endY) + '\n');
                    float [] values = new float[9];
                    rect.matrix.getValues(values);
                    for (int j = 0; j < 9; ++j) {
                        bw.write(Float.toString(values[j]) + '\n');
                    }
                } else if (appView.shapeTypes.get(i).equals("circle")) {
                    Circle circ = (Circle) appView.shapes.get(i);
                    bw.write(Integer.toString(appView.paints.get(i).getColor()) + '\n');
                    bw.write(Float.toString(circ.startX) + '\n');
                    bw.write(Float.toString(circ.startY) + '\n');
                    bw.write(Float.toString(circ.radius) + '\n');
                    float [] values = new float[9];
                    circ.matrix.getValues(values);
                    for (int j = 0; j < 9; ++j) {
                        bw.write(Float.toString(values[j]) + '\n');
                    }
                }
            }
            bw.close();
        } catch (IOException e) {

        }
    }

    @Override
    protected void onResume() {
        appView.selected = false;
        super.onResume();
        try {
            File cache = new File(getApplicationContext().getFilesDir(), "file.txt");
            BufferedReader br = new BufferedReader(new FileReader(cache));
            if (cache.length() != 0) {
                String line = br.readLine();
                if (line != null) appView.toolSelected = line;
                setToolSelected();
                line = br.readLine();
                if (line != null) appView.curColour = Integer.parseInt(line);
                setColourPicked();
                line = br.readLine();
                while (line != null) {
                    if (line.equals("line")) {
                        appView.shapeTypes.add("line");
                        Paint p = new Paint();
                        p.setStrokeWidth(10);
                        line = br.readLine();
                        p.setColor(Integer.parseInt(line));
                        appView.paints.add(p);
                        float x1 = Float.parseFloat(br.readLine());
                        float y1 = Float.parseFloat(br.readLine());
                        float x2 = Float.parseFloat(br.readLine());
                        float y2 = Float.parseFloat(br.readLine());
                        Line ln = new Line(x1, y1, x2, y2);
                        float [] values = new float [9];
                        for (int i = 0; i < 9; ++i) {
                            values[i] = Float.parseFloat(br.readLine());
                        }
                        ln.matrix.setValues(values);
                        ln.backup.setValues(values);
                        appView.shapes.add(ln);
                    } else if (line.equals("rectangle")) {
                        appView.shapeTypes.add("rectangle");
                        Paint p = new Paint();
                        line = br.readLine();
                        p.setColor(Integer.parseInt(line));
                        appView.paints.add(p);
                        float startX = Float.parseFloat(br.readLine());
                        float startY = Float.parseFloat(br.readLine());
                        float endX = Float.parseFloat(br.readLine());
                        float endY = Float.parseFloat(br.readLine());
                        Rectangle rect = new Rectangle(startX, startY, endX, endY);
                        float [] values = new float [9];
                        for (int i = 0; i < 9; ++i) {
                            values[i] = Float.parseFloat(br.readLine());
                        }
                        rect.matrix.setValues(values);
                        rect.backup.setValues(values);
                        appView.shapes.add(rect);
                    } else if (line.equals("circle")) {
                        appView.shapeTypes.add("circle");
                        Paint p = new Paint();
                        line = br.readLine();
                        p.setColor(Integer.parseInt(line));
                        appView.paints.add(p);
                        float startX = Float.parseFloat(br.readLine());
                        float startY = Float.parseFloat(br.readLine());
                        float radius = Float.parseFloat(br.readLine());
                        Circle circ = new Circle(startX, startY, radius);
                        float [] values = new float [9];
                        for (int i = 0; i < 9; ++i) {
                            values[i] = Float.parseFloat(br.readLine());
                        }
                        circ.matrix.setValues(values);
                        circ.backup.setValues(values);
                        appView.shapes.add(circ);
                    }
                    line = br.readLine();
                }
            }
            br.close();
        } catch (FileNotFoundException e) {

        } catch (IOException f) {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appView = findViewById(R.id.mainView);
        appView.setActivity(this);
        // toolbar buttons
        lineButton = findViewById(R.id.lineButton);
        rectangleButton = findViewById(R.id.rectangleButton);
        circleButton = findViewById(R.id.circleButton);
        selectButton = findViewById(R.id.selectButton);
        eraserButton = findViewById(R.id.eraserButton);
        if (appView.toolSelected.equals("line")) {
            lineButton.setChecked(true);
        } else if (appView.toolSelected.equals("rectangle")) {
            rectangleButton.setChecked(true);
        } else if (appView.toolSelected.equals("circle")) {
            circleButton.setChecked(true);
        } else if (appView.toolSelected.equals("selection")) {
            selectButton.setChecked(true);
        }
        eraserButton.setEnabled(false);
        lineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setLineClicked();
            }
        });
        rectangleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setRectangleClicked();
            }
        });
        circleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCircleClicked();
            }
        });
        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setSelectClicked();
            }
        });
        eraserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (appView.getSelected()) {
                    appView.removeLast();
                }
            }
        });

        // colour picker buttons
        redButton = findViewById(R.id.redButton);
        orangeButton = findViewById(R.id.orangeButton);
        yellowButton = findViewById(R.id.yellowButton);
        greenButton = findViewById(R.id.greenButton);
        blueButton = findViewById(R.id.blueButton);
        purpleButton = findViewById(R.id.purpleButton);
        greyButton = findViewById(R.id.greyButton);
        setColourPicked();

        redButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setRedClicked();
                if (appView.getSelected()) {
                    appView.colourLastShape();
                }
            }
        });
        orangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setOrangeClicked();
                if (appView.getSelected()) {
                    appView.colourLastShape();
                }
            }
        });
        yellowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setYellowClicked();
                if (appView.getSelected()) {
                    appView.colourLastShape();
                }
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setGreenClicked();
                if (appView.getSelected()) {
                    appView.colourLastShape();
                }
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setBlueClicked();
                if (appView.getSelected()) {
                    appView.colourLastShape();
                }
            }
        });
        purpleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPurpleClicked();
                if (appView.getSelected()) {
                    appView.colourLastShape();
                }
            }
        });
        greyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setGreyClicked();
                if (appView.getSelected()) {
                    appView.colourLastShape();
                }
            }
        });

    }

    public void setLineClicked() {
        setAllToolsUnClicked();
        lineButton.setChecked(true);
        appView.setTool("line");
        appView.setSelected(false);
    }

    public void setRectangleClicked() {
        setAllToolsUnClicked();
        rectangleButton.setChecked(true);
        appView.setTool("rectangle");
        appView.setSelected(false);
    }

    public void setCircleClicked() {
        setAllToolsUnClicked();
        circleButton.setChecked(true);
        appView.setTool("circle");
        appView.setSelected(false);
    }

    public void setSelectClicked() {
        setAllToolsUnClicked();
        selectButton.setChecked(true);
        appView.setTool("selection");
    }

    private void setAllToolsUnClicked() {
        lineButton.setChecked(false);
        rectangleButton.setChecked(false);
        circleButton.setChecked(false);
        selectButton.setChecked(false);
    }

    public void setToolSelected() {
        if (appView.toolSelected.equals("line")) {
            setLineClicked();
        } else if (appView.toolSelected.equals("rectangle")) {
            setRectangleClicked();
        } else if (appView.toolSelected.equals("circle")) {
            setCircleClicked();
        } else if (appView.toolSelected.equals("selection")) {
            setSelectClicked();
        }
    }

    public void setRedClicked() {
        setAllColoursUnClicked();
        redButton.setChecked(true);
        appView.setPaint("red");
    }

    public void setOrangeClicked() {
        setAllColoursUnClicked();
        orangeButton.setChecked(true);
        appView.setPaint("orange");
    }

    public void setYellowClicked() {
        setAllColoursUnClicked();
        yellowButton.setChecked(true);
        appView.setPaint("yellow");
    }

    public void setGreenClicked() {
        setAllColoursUnClicked();
        greenButton.setChecked(true);
        appView.setPaint("green");
    }

    public void setBlueClicked() {
        setAllColoursUnClicked();
        blueButton.setChecked(true);
        appView.setPaint("blue");
    }

    public void setPurpleClicked() {
        setAllColoursUnClicked();
        purpleButton.setChecked(true);
        appView.setPaint("purple");
    }

    public void setGreyClicked() {
        setAllColoursUnClicked();
        greyButton.setChecked(true);
        appView.setPaint("grey");
    }

    private void setAllColoursUnClicked() {
        redButton.setChecked(false);
        orangeButton.setChecked(false);
        yellowButton.setChecked(false);
        greenButton.setChecked(false);
        blueButton.setChecked(false);
        purpleButton.setChecked(false);
        greyButton.setChecked(false);
    }

    public void setColourPicked() {
        if (appView.curColour == ContextCompat.getColor(appView.context, R.color.red)) {
            setRedClicked();
        } else if (appView.curColour == ContextCompat.getColor(appView.context, R.color.orange)) {
            setOrangeClicked();
        } else if (appView.curColour == ContextCompat.getColor(appView.context, R.color.yellow)) {
            setYellowClicked();
        } else if (appView.curColour == ContextCompat.getColor(appView.context, R.color.green)) {
            setGreenClicked();
        } else if (appView.curColour == ContextCompat.getColor(appView.context, R.color.blue)) {
            setBlueClicked();
        } else if (appView.curColour == ContextCompat.getColor(appView.context, R.color.purple)) {
            setPurpleClicked();
        } else if (appView.curColour == ContextCompat.getColor(appView.context, R.color.grey)) {
            setGreyClicked();
        }
        if (appView.getSelected()) {
            appView.colourLastShape();
        }
    }
}
