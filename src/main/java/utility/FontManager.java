package utility;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final String INTER_REGULAR = "/fonts/Inter-Regular.ttf";
    private static final String INTER_BOLD = "/fonts/Inter-Bold.ttf";
    private static final String INTER_ITALIC = "/fonts/Inter-Italic.ttf";

    private static FontManager instance = null;
    private final Map<String, Font> fontCache;

    private FontManager() {
        fontCache = new HashMap<>();
        loadFonts();
    }

    public static FontManager Instance() {
        if (instance == null) {
            synchronized (FontManager.class) {
                if (instance == null) {
                    instance = new FontManager();
                }
            }
        }
        return instance;
    }

    private void loadFonts() {
        loadFont(INTER_REGULAR, "Inter Regular");
        loadFont(INTER_BOLD, "Inter Bold");
        loadFont(INTER_ITALIC, "Inter Italic");
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
        return getFont("Inter Regular", size);
    }

    public Font getBold(float size) {
        return getFont("Inter Bold", size);
    }

    public Font getItalic(float size) {
        return getFont("Inter Italic", size);
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
