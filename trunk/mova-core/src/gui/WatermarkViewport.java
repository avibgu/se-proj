package gui;

import javax.swing.*;
import java.awt.*;

public class WatermarkViewport extends JViewport {
    
	private static final long serialVersionUID = -5244050429737456503L;
	
    private WatermarkPainter bgPainter;

    /**
     * Creates a WatermarkViewport with the specified background
     * and foreground painters.
     *
     * @param  bgPainter   the painter that will paint the background
     * @param  fgPainter   the painter that will paint the foreground
     */
    public WatermarkViewport(WatermarkPainter bgPainter) {
        setBackgroundPainter(bgPainter);
        //setForegroundPainter(fgPainter);
        setBackground(Color.WHITE);
        //setOpaque(false);
    }

    /**
     * overriding paintComponent allows us to paint
     * the custom background below the scrolling content.
     */
    public void paintComponent(Graphics g) {
        // do the superclass behavior first
        super.paintComponent(g);
        
        // any custom background painting should occur here
        // we'll call the background painter to paint the custom background
        if (bgPainter != null) {
            bgPainter.paint(g);
        }
    }
    /**
     * Set the painter to use to paint the background.
     *
     * @param  painter  the painter that will paint the background
     */
    public void setBackgroundPainter(WatermarkPainter painter) {
        if (bgPainter != null) {
            bgPainter.setComponent(null);
        }
        
        bgPainter = painter;
        
        if (bgPainter != null) {
            bgPainter.setComponent(this);
        }

        repaint();
    }

    /**
     * Return the painter that paints the background.
     *
     * @return the painter used for the background
     */
    public WatermarkPainter getBackgroundPainter() {
        return bgPainter;
    }
}
