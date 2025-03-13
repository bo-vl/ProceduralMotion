package utils.Math;

import org.joml.Vector2f;
import utils.Renderer;

import java.util.ArrayList;

public class Chain {
    ArrayList<Vector2f> joints;
    int linkSize;

    ArrayList<Float> angles;
    float angleConstraint;

    public Chain(Vector2f origin, int jointCount, int linkSize) {
        this(origin, jointCount, linkSize, (float) (Math.PI * 2));
    }

    public Chain(Vector2f origin, int jointCount, int linkSize, float angleConstraint) {
        this.linkSize = linkSize;
        this.angleConstraint = angleConstraint;
        this.joints = new ArrayList<>();
        this.angles = new ArrayList<>();

        joints.add(new Vector2f(origin));
        angles.add(0f);

        for (int i = 1; i < jointCount; i++) {
            Vector2f prevJoint = joints.get(i - 1);
            Vector2f newJoint = new Vector2f(prevJoint.x, prevJoint.y + linkSize);
            joints.add(newJoint);
            angles.add(0f);
        }
    }

    public void resolve(Vector2f targetPos) {
        joints.set(0, new Vector2f(targetPos));

        for (int i = 1; i < joints.size(); i++) {
            Vector2f prevJoint = joints.get(i - 1);
            Vector2f curJoint = joints.get(i);

            Vector2f direction = new Vector2f(prevJoint).sub(curJoint).normalize();

            Vector2f newJoint = new Vector2f(prevJoint).sub(direction.mul(linkSize));
            joints.set(i, newJoint);
        }
    }

    public void fabrikResolve(Vector2f pos, Vector2f anchor) {
        joints.set(0, new Vector2f(pos));
        for (int i = 1; i < joints.size(); i++) {
            Vector2f prevJoint = joints.get(i - 1);
            Vector2f curJoint = joints.get(i);
            joints.set(i, constrainDistance(curJoint, prevJoint, linkSize));
        }

        joints.set(joints.size() - 1, new Vector2f(anchor));
        for (int i = joints.size() - 2; i >= 0; i--) {
            Vector2f nextJoint = joints.get(i + 1);
            Vector2f curJoint = joints.get(i);
            joints.set(i, constrainDistance(curJoint, nextJoint, linkSize));
        }
    }

    private float constrainAngle(float angle, float reference, float constraint) {
        float delta = angle - reference;
        if (Math.abs(delta) > constraint) {
            return reference + Math.signum(delta) * constraint;
        }
        return angle;
    }

    private Vector2f constrainDistance(Vector2f point, Vector2f target, float distance) {
        Vector2f direction = new Vector2f(target).sub(point);
        float length = direction.length();
        if (length == 0) {
            return new Vector2f(target);
        }
        direction.normalize();
        return new Vector2f(target).sub(direction.mul(distance));
    }

    public void display() {
        for (int i = 0; i < joints.size() - 1; i++) {
            Vector2f startJoint = joints.get(i);
            Vector2f endJoint = joints.get(i + 1);
            Renderer.Add2DLine(startJoint, endJoint, 0x000000);
        }

        for (Vector2f joint : joints) {
            Renderer.AddFilledCircle(joint, 8, 32, 0xFF0000);
        }
    }
}