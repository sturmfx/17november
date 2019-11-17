package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main
{
    public static Color choosen_color = Color.RED;
    public static boolean pressed = false;
    public static int brush_radius = 10;
    public static Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, new Color(128, 0, 128)};
    public static int mode = 0;
    public static int color = 0;
    public static Random r = new Random();
    public static JFrame gui = new JFrame();
    public static JButton load = new JButton("LOAD PICTURE");
    public static JButton shuffle = new JButton("SHUFFLE");
    public static JTextField pixeln = new JTextField(10);
    public static JButton reset = new JButton("RESET");
    public static JButton save = new JButton("SAVE");
    public static JButton modeb = new JButton("CIRCLE");
    public static JButton colorb = new JButton("COLOR");
    public static JButton choose = new JButton("CHOOSE COLOR");

    public static JFrame image = new JFrame();
    public static JLabel picLabel;
    public static BufferedImage myPicture;
    public static BufferedImage myPicture2;
    static String image_path = "";
    final static JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    public static void main(String[] args)
    {
        load.addActionListener(e ->
        {
            load();
        });

        shuffle.addActionListener(e ->
        {
            shuffle();
        });

        reset.addActionListener(e ->
        {
            reset();
        });

        save.addActionListener(e ->
        {
            save();
        });

        modeb.addActionListener(e ->
        {
            modeb();
        });

        colorb.addActionListener(e ->
        {
            colorb();
        });

        choose.addActionListener(e ->
        {
            choose();
        });


        gui.setSize(new Dimension(250, 500));
        gui.setLayout(new FlowLayout());
        gui.add(load);
        gui.add(save);
        gui.add(shuffle);
        gui.add(pixeln);
        gui.add(reset);
        gui.add(modeb);
        gui.add(colorb);
        gui.add(choose);
        colorb.setBackground(colors[0]);

        gui.setVisible(true);
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void load()
    {
        int returnValue = fc.showOpenDialog(null);
        image_path = fc.getSelectedFile().getAbsolutePath();
        try
        {
            load_image();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void load_image() throws IOException
    {
        image.dispose();
        myPicture = ImageIO.read(new File(image_path));
        myPicture2 = ImageIO.read(new File(image_path));
        image = new JFrame();
        image.setSize(new Dimension(myPicture.getWidth(), myPicture.getHeight()));
        picLabel = new JLabel(new ImageIcon(myPicture));
        image.add(picLabel);
        image.setVisible(true);
        add_action();
    }

    public static void shuffle()
    {
        String n = pixeln.getText();
        int n1 = 10;
        if(n.matches("\\d{2}") || n.matches("\\d"))
        {
            n1 = Integer.parseInt(n);
        }

        Color[] colors = new Color[5];
        colors[0] = Color.RED;
        colors[1] = Color.GREEN;
        colors[2] = Color.BLUE;
        colors[3] = Color.PINK;
        colors[4] = Color.WHITE;
        for(int i = 0; i < myPicture.getWidth(); i++)
        {
            for(int j = 0; j < myPicture.getHeight(); j++)
            {
                if(r.nextInt(n1) == 1)
                {
                    myPicture.setRGB(i, j, colors[r.nextInt(5)].getRGB());
                }
            }
        }
        picLabel.setIcon(new ImageIcon(myPicture));
    }

    public static void reset()
    {
        myPicture = deepCopy(myPicture2);
        picLabel.setIcon(new ImageIcon(myPicture));
    }

    public static void save()
    {

        String path = FileSystemView.getFileSystemView().getHomeDirectory() + "\\saved_image" + r.nextInt(1000) + ".png";
        try
        {
            File file = new File(path);
            ImageIO.write(myPicture, "png", file);
            System.out.println("Saved to " + file);
        }
        catch (IOException ex)
        {

        }
    }

    public static void add_action()
    {
        image.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                pressed = true;
                String n = pixeln.getText();
                if(n.matches("\\d{2}") || n.matches("\\d"))
                {
                    brush_radius = Integer.parseInt(n);
                }

                if(mode == 0)
                {
                    for(int i = 0; i < myPicture.getWidth(); i++)
                    {
                        for(int j = 0; j < myPicture.getHeight(); j++)
                        {
                            if((i - e.getX())*(i - e.getX())+(j - e.getY())*(j - e.getY()) < brush_radius*brush_radius)
                            {
                                myPicture.setRGB(i, j,choosen_color.getRGB());
                            }
                        }
                    }
                    picLabel.setIcon(new ImageIcon(myPicture));
                }

                if(mode == 2)
                {
                    for(int i = 0; i < myPicture.getWidth(); i++)
                    {
                        for(int j = 0; j < myPicture.getHeight(); j++)
                        {
                            if((i > e.getX()) && (i < e.getX() + 2*brush_radius) && (j > e.getY()) && (j < e.getY() + 2*brush_radius))
                            {
                                myPicture.setRGB(i, j,choosen_color.getRGB());
                            }
                        }
                    }
                    picLabel.setIcon(new ImageIcon(myPicture));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                pressed = false;
            }
        });
        image.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if((mode == 1) && (pressed))
                {
                    for(int i = 0; i < myPicture.getWidth(); i++)
                    {
                        for(int j = 0; j < myPicture.getHeight(); j++)
                        {
                            if((i - e.getX())*(i - e.getX())+(j - e.getY())*(j - e.getY()) < brush_radius*brush_radius)
                            {
                                myPicture.setRGB(i, j, choosen_color.getRGB());
                            }
                        }
                    }
                    picLabel.setIcon(new ImageIcon(myPicture));
                }
            }
        });
    }
    public static void modeb()
    {
        if(mode == 0)
        {
            mode = 1;
            modeb.setText("DRAW");
        }
        else
        {
            if(mode == 1)
            {
                mode = 2;
                modeb.setText("SQUARE");
            }
            else
            {
                mode = 0;
                modeb.setText("CIRCLE");
            }
        }
    }
    public static void colorb()
    {
        int temp = colors.length;
        if(color < temp-1)
        {
            color++;
        }
        else
        {
            color = 0;
        }
        choosen_color = colors[color];
        colorb.setBackground(choosen_color);
    }

    public static void choose()
    {
        choosen_color = JColorChooser.showDialog(null, "CHOOSE COLOR", Color.RED);
        colorb.setBackground(choosen_color);
    }

    public static BufferedImage deepCopy(BufferedImage b)
    {
        ColorModel c = b.getColorModel();
        boolean is = c.isAlphaPremultiplied();
        WritableRaster raster = b.copyData(null);
        return new BufferedImage(c, raster, is, null);
    }
}
