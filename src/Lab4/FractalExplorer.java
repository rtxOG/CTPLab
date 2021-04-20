package Lab4;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.awt.image.*;

public class FractalExplorer
{
    private int displaySize,
            rowsRemaining;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;

    private JButton saveButton = new JButton();
    private JButton resetButton = new JButton();
    private JComboBox myComboBox = new JComboBox();

    public FractalExplorer(int size) {
        displaySize = size;

        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

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

        FractalGenerator tricornFractal = new Tricorn();
        FractalGenerator burningShipFractal = new BurningShip();
        FractalGenerator mandelbrotFractal = new Mandelbrot();

        myComboBox.addItem(mandelbrotFractal);
        myComboBox.addItem(tricornFractal);
        myComboBox.addItem(burningShipFractal);

        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        JPanel upprePanel = new JPanel();
        JLabel myLabel = new JLabel("Фрактал:");
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
        enableUI(false);
        rowsRemaining = displaySize;

        for (int x=0; x<displaySize; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }
    }

    private void enableUI(boolean val) {
        myComboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
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

            if (rowsRemaining != 0) {
                return;
            }

            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }


    private class FractalWorker extends SwingWorker<Object, Object>
    {
        int yCoordinate;
        int[] computedRGBValues;

        private FractalWorker(int row) {
            yCoordinate = row;
        }

        // Функция для работы в фоне
        protected Object doInBackground() {
            // Считает строку символов
            computedRGBValues = new int[displaySize];
            for (int i = 0; i < computedRGBValues.length; i++) {
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, i);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, yCoordinate);
                int iteration = fractal.numIterations(xCoord, yCoord);

                if (iteration == -1){
                    computedRGBValues[i] = 0;
                }

                else {

                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    computedRGBValues[i] = rgbColor;
                }
            }
            return null;

        }

        // Вызывается после того, как doInBackground закончилась
        // рисует отдельную строки пикселей
        protected void done() {
            for (int i = 0; i < computedRGBValues.length; i++) {
                display.drawPixel(i, yCoordinate, computedRGBValues[i]);
            }
            display.repaint(0, 0, yCoordinate, displaySize, 1);

            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }
    }

    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}