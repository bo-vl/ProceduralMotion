package utils;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    public static void Add2DLine(Vector2f start, Vector2f end, int color) {
        float[] rgba = ColorUtil.fromHex(color);

        glColor3f(rgba[0], rgba[1], rgba[2]);
        glBegin(GL_LINES);
        glVertex2f(start.x, start.y);
        glVertex2f(end.x, end.y);
        glEnd();
    }

    public static void AddCircle(Vector2f center, float radius, int segments, int color) {
        float[] rgba = ColorUtil.fromHex(color);

        glColor3f(rgba[0], rgba[1], rgba[2]);

        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < segments; i++) {
            double angle = (i * 2 * Math.PI) / segments;
            Vector2f point = new Vector2f(
                    center.x + (float) (radius * Math.cos(angle)),
                    center.y + (float) (radius * Math.sin(angle))
            );
            glVertex2f(point.x, point.y);
        }
        glEnd();
    }

    public static void AddFilledCircle(Vector2f center, float radius, int segments, int color) {
        float[] rgba = ColorUtil.fromHex(color);

        glColor3f(rgba[0], rgba[1], rgba[2]);

        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(center.x, center.y);

        for (int i = 0; i <= segments; i++) {
            double angle = (i * 2 * Math.PI) / segments;
            Vector2f point = new Vector2f(
                    center.x + (float) (radius * Math.cos(angle)),
                    center.y + (float) (radius * Math.sin(angle))
            );
            glVertex2f(point.x, point.y);
        }

        glEnd();
    }
}