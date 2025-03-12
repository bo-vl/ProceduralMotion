package utils.Math;

import org.joml.Vector2f;
import java.util.ArrayList;
import utils.Renderer;

public class Fish {
    private Chain spine;

    private int bodyColor = 0xFF3A7CA5;
    private int finColor = 0xFF81C3D7;

    private float[] bodyWidth = {34, 40, 42, 41, 38, 32, 25, 19, 16, 10};

    public Fish(Vector2f origin) {
        // 10 segments for the body (smaller fish)
        spine = new Chain(origin, 10, 32, (float) (Math.PI / 8));
    }

    public void resolve(float mouseX, float mouseY) {
        Vector2f headPos = spine.joints.get(0);
        Vector2f mousePos = new Vector2f(mouseX, mouseY);

        Vector2f direction = new Vector2f(mousePos).sub(headPos).normalize();

        float offsetDistance = 50;
        Vector2f targetPos = new Vector2f(mousePos).sub(direction.mul(offsetDistance));

        spine.resolve(targetPos);
    }

    public void display() {
        ArrayList<Vector2f> joints = spine.joints;

        for (int i = 0; i < joints.size(); i++) {
            if (i < bodyWidth.length) {
                Renderer.AddFilledCircle(joints.get(i), bodyWidth[i] / 2, 16, bodyColor); // Smaller circles
            }
        }

        drawFins(joints, spine.angles);
    }

    private void drawFins(ArrayList<Vector2f> joints, ArrayList<Float> angles) {
        Vector2f pectoralFinPos = joints.get(2);
        float pectoralFinAngle = angles.get(2);
        drawFin(pectoralFinPos, pectoralFinAngle, 80, 32, (float) (Math.PI / 3), (float) (-Math.PI / 3));

        Vector2f ventralFinPos = joints.get(6);
        float ventralFinAngle = angles.get(6);
        drawFin(ventralFinPos, ventralFinAngle, 48, 16, (float) (Math.PI / 2), (float) (-Math.PI / 2));

        Vector2f tailPos = joints.get(joints.size() - 1);
        float tailAngle = angles.get(angles.size() - 1);
        drawCaudalFin(tailPos, tailAngle);
    }

    private void drawFin(Vector2f position, float angle, float width, float height, float angleOffset1, float angleOffset2) {
        Vector2f rightFinPos = getPoint(position, angle, width / 2, angleOffset1);
        Renderer.AddFilledCircle(rightFinPos, height / 2, 16, finColor);

        Vector2f leftFinPos = getPoint(position, angle, width / 2, angleOffset2);
        Renderer.AddFilledCircle(leftFinPos, height / 2, 16, finColor);
    }

    private void drawCaudalFin(Vector2f position, float angle) {
        Vector2f topFinPos = getPoint(position, angle, 10, (float) (Math.PI / 2));
        Vector2f bottomFinPos = getPoint(position, angle, 10, (float) (-Math.PI / 2));

        Renderer.AddFilledCircle(topFinPos, 10, 16, finColor);
        Renderer.AddFilledCircle(bottomFinPos, 10, 16, finColor);
    }

    private Vector2f getPoint(Vector2f joint, float angle, float lengthOffset, float angleOffset) {
        return new Vector2f(
                joint.x + (float) Math.cos(angle + angleOffset) * lengthOffset,
                joint.y + (float) Math.sin(angle + angleOffset) * lengthOffset
        );
    }
}