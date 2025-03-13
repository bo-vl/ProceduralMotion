package utils.Math;

import org.joml.Vector2f;
import java.util.ArrayList;
import utils.Renderer;
import utils.Settings;

public class Fish {
    private Chain spine;

    private int bodyColor = 0xFF3A7CA5;
    private int finColor = 0xFF81C3D7;

    private float[] bodyWidth;
    private float speed;

    private float autonomousDirection = 0;
    private float autonomousSpeed = 2.0f;

    public Fish(Vector2f origin) {
        Settings settings = Settings.getInstance();
        float sizeMultiplier = settings.getSize() / 10.0f;

        speed = settings.getOffset();

        bodyWidth = new float[]{
                34 * sizeMultiplier,
                40 * sizeMultiplier,
                42 * sizeMultiplier,
                41 * sizeMultiplier,
                38 * sizeMultiplier,
                32 * sizeMultiplier,
                25 * sizeMultiplier,
                19 * sizeMultiplier,
                16 * sizeMultiplier,
                10 * sizeMultiplier
        };

        spine = new Chain(origin, 10, (int)(32 * sizeMultiplier), (float) (Math.PI / 8));
    }

    public void resolve(float mouseX, float mouseY) {
        Vector2f headPos = spine.joints.get(0);
        Vector2f mousePos = new Vector2f(mouseX, mouseY);

        Vector2f direction = new Vector2f(mousePos).sub(headPos).normalize();

        float offsetDistance = 50 / speed;
        Vector2f targetPos = new Vector2f(mousePos).sub(direction.mul(offsetDistance));

        spine.resolve(targetPos);
    }

    public void autonomousMove(int windowWidth, int windowHeight) {
        Vector2f headPos = spine.joints.get(0);

        autonomousDirection += (float) (Math.random() - 0.5) * 0.1f;

        Vector2f direction = new Vector2f((float) Math.cos(autonomousDirection), (float) Math.sin(autonomousDirection)).mul(autonomousSpeed);
        Vector2f newPos = new Vector2f(headPos).add(direction);

        if (newPos.x < 0 || newPos.x > windowWidth) {
            autonomousDirection = (float) (Math.PI - autonomousDirection);
        }
        if (newPos.y < 0 || newPos.y > windowHeight) {
            autonomousDirection = (float) (-autonomousDirection);
        }

        direction.set((float) Math.cos(autonomousDirection), (float) Math.sin(autonomousDirection)).mul(autonomousSpeed);
        newPos.set(headPos).add(direction);

        newPos.x = Math.max(0, Math.min(windowWidth, newPos.x));
        newPos.y = Math.max(0, Math.min(windowHeight, newPos.y));

        spine.resolve(newPos);
    }

    public void display(boolean showChain, boolean filledCircles) {
        ArrayList<Vector2f> joints = spine.joints;

        if (showChain) {
            spine.display();
        }

        for (int i = 0; i < joints.size(); i++) {
            if (i < bodyWidth.length) {
                if (filledCircles) {
                    Renderer.AddFilledCircle(joints.get(i), bodyWidth[i] / 2, 16, bodyColor);
                } else {
                    Renderer.AddCircle(joints.get(i), bodyWidth[i] / 2, 16, bodyColor);
                }
            }
        }

        drawFins(joints, spine.angles, filledCircles);
    }

    private void drawFins(ArrayList<Vector2f> joints, ArrayList<Float> angles, boolean filledCircles) {
        float sizeMultiplier = bodyWidth[0] / 34;

        Vector2f pectoralFinPos = joints.get(2);
        float pectoralFinAngle = angles.get(2);
        drawFin(pectoralFinPos, pectoralFinAngle, 80 * sizeMultiplier, 32 * sizeMultiplier, (float) (Math.PI / 3), (float) (-Math.PI / 3), filledCircles);

        Vector2f ventralFinPos = joints.get(6);
        float ventralFinAngle = angles.get(6);
        drawFin(ventralFinPos, ventralFinAngle, 48 * sizeMultiplier, 16 * sizeMultiplier, (float) (Math.PI / 2), (float) (-Math.PI / 2), filledCircles);

        Vector2f tailPos = joints.get(joints.size() - 1);
        float tailAngle = angles.get(angles.size() - 1);
        drawCaudalFin(tailPos, tailAngle, sizeMultiplier, filledCircles);
    }

    private void drawFin(Vector2f position, float angle, float width, float height, float angleOffset1, float angleOffset2, boolean filledCircles) {
        Vector2f rightFinPos = getPoint(position, angle, width / 2, angleOffset1);
        if (filledCircles) {
            Renderer.AddFilledCircle(rightFinPos, height / 2, 16, finColor);
        } else {
            Renderer.AddCircle(rightFinPos, height / 2, 16, finColor);
        }

        Vector2f leftFinPos = getPoint(position, angle, width / 2, angleOffset2);
        if (filledCircles) {
            Renderer.AddFilledCircle(leftFinPos, height / 2, 16, finColor);
        } else {
            Renderer.AddCircle(leftFinPos, height / 2, 16, finColor);
        }
    }

    private void drawCaudalFin(Vector2f position, float angle, float sizeMultiplier, boolean filledCircles) {
        Vector2f topFinPos = getPoint(position, angle, 10 * sizeMultiplier, (float) (Math.PI / 2));
        Vector2f bottomFinPos = getPoint(position, angle, 10 * sizeMultiplier, (float) (-Math.PI / 2));

        if (filledCircles) {
            Renderer.AddFilledCircle(topFinPos, 10 * sizeMultiplier, 16, finColor);
            Renderer.AddFilledCircle(bottomFinPos, 10 * sizeMultiplier, 16, finColor);
        } else {
            Renderer.AddCircle(topFinPos, 10 * sizeMultiplier, 16, finColor);
            Renderer.AddCircle(bottomFinPos, 10 * sizeMultiplier, 16, finColor);
        }
    }

    private Vector2f getPoint(Vector2f joint, float angle, float lengthOffset, float angleOffset) {
        return new Vector2f(
                joint.x + (float) Math.cos(angle + angleOffset) * lengthOffset,
                joint.y + (float) Math.sin(angle + angleOffset) * lengthOffset
        );
    }
}