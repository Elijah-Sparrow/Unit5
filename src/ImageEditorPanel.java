import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ImageEditorPanel extends JPanel implements KeyListener{
    
    Color[][] pixels;
    int radius = 1;
    
    public ImageEditorPanel() {
        addKeyListener(this);
        BufferedImage imageIn = null;
        try {
            // the image should be in the main project folder, not in \src or \bin
            imageIn = ImageIO.read(new File("image.png"));
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        pixels = makeColorArray(imageIn);
        setPreferredSize(new Dimension(pixels[0].length, pixels.length));
        setBackground(Color.BLACK);
    }

    public void paintComponent(Graphics g) {
        // paints the array pixels onto the screen
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col]);
                g.fillRect(col, row, 1, 1);
            }
        }
    }

    public void run() {
        // call your image-processing methods here OR call them from keyboard event
        // handling methods
        // write image-processing methods as pure functions - for example: pixels =
        // flip(pixels);
        repaint();
    }

    public Color[][] hflip(Color[][] orig){
        Color[][] changed = new Color[orig.length][orig[0].length];
        for (int r = 0; r < orig.length; r++) {
            int rowLength = orig[0].length - 1;
            for (int c = 0; c < orig[0].length; c++){
                changed[r][c] = orig[r][rowLength];
                rowLength--;
            }
            
        }
        return changed;
    }

    public Color[][] vflip(Color[][] orig){
        Color[][] changed = new Color[orig.length][orig[0].length];
            for (int c = 0; c < orig[0].length; c++){
                int rowLength = orig.length -1;
                for (int r = 0; r < orig.length; r++){
                    changed[r][c] = orig[rowLength][c];
                    rowLength--;
                }
            }
        return changed;
    }

    public Color[][] blur(Color[][] orig, int radius){
        Color[][] changed = new Color[orig.length][orig[0].length];
            for (int r = 0; r < orig.length; r++) {
                for (int c = 0; c < orig[0].length; c++){
                    int red = 0;
                    int blue = 0;
                    int green = 0;
                    int pixelsNear = 0;
                    for (int i = r-radius; i <= r+radius; i++) {
                        for (int j = c-radius; j <= c+radius; j++) {
                            if((i >= 0 && i < orig.length) &&(j >= 0 && j < orig[0].length)){
                                red += orig[i][j].getRed();
                                blue += orig[i][j].getBlue();
                                green += orig[i][j].getGreen();
                                pixelsNear++;
                            }
                        }
                    }
                    Color newColor = new Color(red/pixelsNear, green/pixelsNear, blue/pixelsNear);
                    changed[r][c] = newColor;
                }
            }
        return changed;
    }

    public Color[][] greyscale(Color[][] orig){
        Color[][] changed = new Color[orig.length][orig[0].length];
        for (int r = 0; r < orig.length; r++) {
            for (int c = 0; c < orig[0].length; c++){
                Color grey = new Color(orig[r][c].getRed(),orig[r][c].getRed(),orig[r][c].getRed());
                changed[r][c] = grey;
            }
        }
        return changed;
    }

    public Color[][] colorShift(Color[][] orig){
        Color[][] changed = new Color[orig.length][orig[0].length];
        int red = 0;
        int green = 0; 
        int blue = 0;
        for (int r = 0; r < orig.length; r++) {
            for (int c = 0; c < orig[0].length; c++){
            red = orig[r][c].getBlue();
            green = orig[r][c].getRed();
            blue = orig[r][c].getGreen();
            Color shift = new Color(red,green,blue);
            changed[r][c] = shift;
            }
        }
        return changed;
    }

    public Color[][] makeColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] result = new Color[height][width];
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        // System.out.println("Loaded image: width: " +width + " height: " + height);
        return result;
    }

    public void keyTyped(KeyEvent e) {
        // note the difference between getKeyChar and getKeyCode
        if (e.getKeyChar() == 'q'){
            pixels = hflip(pixels);
            repaint();
        }

        if (e.getKeyChar() == 'w'){
            pixels = vflip(pixels);
            repaint();
        }

        if (e.getKeyChar() == 'e'){
            pixels = blur(pixels, radius);
            repaint();
        }

        if (e.getKeyChar() == 'r'){
            pixels = greyscale(pixels);
            repaint();
        }

        if (e.getKeyChar() == 't'){
            pixels = colorShift(pixels);
            repaint();
        }

        if (e.getKeyChar() == '+'){
            radius++;
        }

        if (e.getKeyChar() == '-'){
            if (radius > 1){
                radius--;
            } else{
                System.out.println("Blur radius minimum reached");
            }
        }
    }

    
    public void keyPressed(KeyEvent e) {
        
    }

    
    public void keyReleased(KeyEvent e) {
        
    }
}
