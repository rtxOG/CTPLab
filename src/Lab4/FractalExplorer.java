package Lab4;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;

public class FractalExplorer {
    private int displaySize;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;

    public FractalExplorer(int size) {
        displaySize = size;

        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    // Cоздание дисплея и кнопки
    public void createAndShowGUI()
    {
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Отрисовка фрактала");

        myFrame.add(display, BorderLayout.CENTER);
        JButton resetButton = new JButton("Сбросить");

        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox myComboBox = new JComboBox();

        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        JPanel upprePanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        upprePanel.add(myLabel);
        upprePanel.add(myComboBox);
        myFrame.add(upprePanel, BorderLayout.NORTH);

        JButton saveButton = new JButton("Сохранить");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveButton);
        bottomPanel.add(resetButton);
        myFrame.add(bottomPanel, BorderLayout.SOUTH);

        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);
    }

    private void drawFractal()
    {
        /** Loop through every pixel in the display **/
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){

                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                int iteration = fractal.numIterations(xCoord, yCoord);

                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        display.repaint();
    }

    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();

            if (e.getSource() instanceof JComboBox) {
                JComboBox src = (JComboBox) e.getSource();
                fractal = (FractalGenerator) src.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();

            } else if (command.equals("Сбросить")) {
                fractal.getInitialRange(range);
                drawFractal();
            } else if (command.equals("Сохранить")) {
                // Сохранение изображения
                JFileChooser chooser = new JFileChooser();

                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);

                int userSelection = chooser.showSaveDialog(display);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    java.io.File file = chooser.getSelectedFile();
                    java.io.File savingFile = new java.io.File(file.getPath() + file.getName() + ".png");

                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "PNG", savingFile);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Не удалось сохранить изображение",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }


    private class MouseHandler extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }


    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}