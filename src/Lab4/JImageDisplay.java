package Lab4;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class JImageDisplay extends JComponent {
    private BufferedImage img;

    // Конструктор для создания дисплея
    JImageDisplay(int width, int height){
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);
    }

    // Переоределние функции для отрисовки картинки
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, img.getWidth(),img.getHeight(), null);
    }

    // Очистить картинку
    public void clearImage()
    {
        int[] blankArray = new int[getWidth() * getHeight()];
        img.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }

    // Нарисовать пиксль по двум координатам и заданному цвету
    public void drawPixel(int x, int y, int rgbColor)
    {
        img.setRGB(x, y, rgbColor);
    }
}