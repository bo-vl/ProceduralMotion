import org.joml.Vector2f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import utils.ColorUtil;
import utils.Math.Fish;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    private long window;
    float[] background = ColorUtil.RGBA(255, 255, 255, 1);

    private int windowWidth = 800;
    private int windowHeight = 600;

    private Fish fish;

    public static float mouseX = 0;
    public static float mouseY = 0;

    private boolean showChain = false;
    private boolean autonomousMovement = false;
    private boolean filledCircles = true;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        GLFWErrorCallback.createPrint(System.err).set();

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(windowWidth, windowHeight, "Fish Simulation", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);

            if (key == GLFW_KEY_C && action == GLFW_RELEASE) {
                showChain = !showChain;
            }

            if (key == GLFW_KEY_A && action == GLFW_RELEASE) {
                autonomousMovement = !autonomousMovement;
            }

            if (key == GLFW_KEY_F && action == GLFW_RELEASE) {
                filledCircles = !filledCircles;
            }
        });

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            windowWidth = width;
            windowHeight = height;
            glViewport(0, 0, width, height);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        Vector2f origin = new Vector2f(windowWidth / 2f, windowHeight - 50);
        fish = new Fish(origin);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(background[0], background[1], background[2], background[3]);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            setupProjection();

            updateMousePosition();

            if (autonomousMovement) {
                fish.autonomousMove(windowWidth, windowHeight);
            } else {
                fish.resolve(mouseX, mouseY);
            }

            fish.display(showChain, filledCircles);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void updateMousePosition() {
        try (MemoryStack stack = stackPush()) {
            DoubleBuffer xPos = stack.mallocDouble(1);
            DoubleBuffer yPos = stack.mallocDouble(1);
            glfwGetCursorPos(window, xPos, yPos);

            mouseX = (float) xPos.get(0);
            mouseY = windowHeight - (float) yPos.get(0);
        }
    }

    private void setupProjection() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0f, windowWidth, 0.0f, windowHeight, -1.0f, 1.0f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}