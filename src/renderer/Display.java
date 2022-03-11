package renderer;

import renderer.Point.MyPoint;
import renderer.Shapes.MyPolygon;
import renderer.Shapes.Tetrahedron;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Display extends Canvas implements Runnable {
    private static long serialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;
    private static String title = "Conquest of Nations (Early Alpha)"; // Set the title
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 800;
    private static boolean running = false;
    private Color color = Color.BLACK; //Set any background color

    private Tetrahedron tetra;
            /*
            *
            *
            *
            *@Author TweetyGuy and TheGamingDaily
            *
            *Ep 4 5:12
            *
            */
    public Display() {
        this.frame = new JFrame();

        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);
    }

    public static void main(String[] args) {
        Display display = new Display();
        display.frame.setTitle(title); // title
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null); //location when windows opens
        display.frame.setVisible(true);
        display.frame.setResizable(true); // resizable

        display.start();
    }

    public synchronized void start() {
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            this.thread.join();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;
        int frames = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
                render();
                frames++;
            }

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title + " | " + " fps");
                frames = 0;
            }
        }
        stop();
    }

    private void init() {
        int s = 100;
        MyPoint p1 = new MyPoint(s/2, -s/2, -s/2);
        MyPoint p2 = new MyPoint(s/2, s/2, -s/2);
        MyPoint p3 = new MyPoint(s/2, s/2, s/2);
        MyPoint p4 = new MyPoint(s/2, -s/2, s/2);
        MyPoint p5 = new MyPoint(-s/2, -s/2, -s/2);
        MyPoint p6 = new MyPoint(-s/2, s/2, -s/2);
        MyPoint p7 = new MyPoint(-s/2, s/2, s/2);
        MyPoint p8 = new MyPoint(-s/2, -s/2, s/2);
        this.tetra = new Tetrahedron(
                new MyPolygon(Color.RED, p1, p2, p3, p4), //try does it work?
                new MyPolygon(Color.BLUE, p5, p6, p7, p8), 
                new MyPolygon(Color.WHITE, p1, p2, p5, p6),
                new MyPolygon(Color.YELLOW, p1, p5, p8, p4),
                new MyPolygon(Color.GREEN, p2, p6, p7, p3),
                new MyPolygon(Color.ORANGE, p4, p3, p7, p8));
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(this.color);
        g.fillRect(0, 0, WIDTH*3, HEIGHT*3);

        tetra.render(g);

        g.dispose();
        bs.show();
    }

    private void update() {}
}
