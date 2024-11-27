package view;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class FontManager {

    private static final String SANS_REGULAR = "/fonts/Inter-Regular.ttf";
    private static final String SANS_BOLD = "/fonts/Inter-Bold.ttf";
    private static final String SANS_ITALIC = "/fonts/Inter-Italic.ttf";

    private static FontManager instance = null;
    private final Map<String, Font> fontCache;

    private FontManager() {
        fontCache = new HashMap<>();
        loadFonts();
    }

    public static synchronized FontManager Instance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    private void loadFonts() {
        loadFont(SANS_REGULAR, "Sans Regular");
        loadFont(SANS_BOLD, "Sans Bold");
        loadFont(SANS_ITALIC, "Sans Italic");
    }

    private void loadFont(String path, String name) {
        try {
            InputStream fontPath = getClass().getResourceAsStream(path);
            if (fontPath != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontPath);
                fontCache.put(name, font);
            } else {
                System.err.println("Failed to locate the font folder.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load font.");
        }
    }

    public Font getRegular(float size) {
        return getFont("Sans Regular", size);
    }

    public Font getBold(float size) {
        return getFont("Sans Bold", size);
    }

    public Font getItalic(float size) {
        return getFont("Sans Italic", size);
    }

    private Font getFont(String name, float size) {
        Font baseFont = fontCache.get(name);
        if (baseFont == null) {
            System.err.println("Failed to find font.");
            return new JLabel().getFont().deriveFont(size);
        }
        return baseFont.deriveFont(size);
    }

    public void useRegular(JComponent component, float size) {
        component.setFont(getRegular(size));
    }

    public void useBold(JComponent component, float size) {
        component.setFont(getBold(size));
    }

    public void useItalic(JComponent component, float size) {
        component.setFont(getItalic(size));
    }
}
