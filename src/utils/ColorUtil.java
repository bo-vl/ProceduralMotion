package utils;

public class ColorUtil {
    public static float[] RGBA(float r, float g, float b, float a) {
        return new float[]{r, g, b, a};
    }

    public static float[] RGBA(int r, int g, int b, int a) {
        return new float[]{
                r / 255.0f,
                g / 255.0f,
                b / 255.0f,
                a / 255.0f
        };
    }

    public static float[] fromHex(int hexColor) {
        float r = ((hexColor >> 24) & 0xFF) / 255.0f;
        float g = ((hexColor >> 16) & 0xFF) / 255.0f;
        float b = ((hexColor >> 8) & 0xFF) / 255.0f;
        float a = (hexColor & 0xFF) / 255.0f;

        return new float[]{r, g, b, a};
    }

    public static int toHex(int r, int g, int b) {
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        return (r << 16) + (g << 8) + b;
    }
}
