package gui;

import java.awt.*;
import java.awt.event.*;

public class FillPainter extends WatermarkPainter {

    /** The image to paint in the background */
    private Image bgImage;

    public FillPainter() {
        bgImage = getImage(getClass().getResource("icons/Conference_map1.jpg"));
    }

    public String[] getCommands() {
        return new String[] {"Conference_map1.jpg"};
    }

    public void paint(Graphics g) {
        // if a background image exists, paint it
        if (bgImage != null) {
            int width = getComponent().getWidth();
            int height = getComponent().getHeight();
            int imageW = bgImage.getWidth(null);
            int imageH = bgImage.getHeight(null);

            // we'll tile the image to fill our area
            for (int x = 0; x < width; x += imageW) {
                for (int y = 0; y < height; y += imageH) {
                    g.drawImage(bgImage, x, y, getComponent());
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        bgImage = getImage(getClass().getResource(ae.getActionCommand()));
        getComponent().repaint();
    }

}
