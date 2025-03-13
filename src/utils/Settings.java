package utils;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {
    private static Settings instance;

    private int size = 10;
    private float offset = 1.0f;

    private Settings() {
        loadSettings();
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private void loadSettings() {
        Path settingsPath = Paths.get("Settings.json");
        try {
            if (Files.exists(settingsPath)) {
                String content = new String(Files.readAllBytes(settingsPath));
                JSONObject json = new JSONObject(content);

                for (String key : json.keySet()) {
                    if (key.equalsIgnoreCase("Size")) {
                        size = json.getInt(key);
                    } else if (key.equalsIgnoreCase("Offset")) {
                        offset = json.getFloat(key);
                    }
                }

                System.out.println("Settings loaded: Size=" + size + ", Offset=" + offset);
            } else {
                try {
                    var inputStream = Settings.class.getResourceAsStream("/Settings.json");
                    if (inputStream != null) {
                        StringBuilder contentBuilder = new StringBuilder();
                        try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                contentBuilder.append(line);
                            }
                        }

                        String content = contentBuilder.toString();
                        JSONObject json = new JSONObject(content);

                        for (String key : json.keySet()) {
                            if (key.equalsIgnoreCase("Size")) {
                                size = json.getInt(key);
                            } else if (key.equalsIgnoreCase("Offset")) {
                                offset = json.getFloat(key);
                            }
                        }

                        System.out.println("Settings loaded from resources: Size=" + size + ", Offset=" + offset);
                    } else {
                        System.out.println("Settings.json not found in resources, using default settings");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading settings from resources: " + e.getMessage());
                    System.out.println("Using default settings");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading settings.json: " + e.getMessage());
            System.out.println("Using default settings");
        }
    }

    public int getSize() {
        return size;
    }

    public float getOffset() {
        return offset;
    }
}
