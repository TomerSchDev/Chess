
import utlity.Setting;
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

/**
 * The type Display.
 */
public class Display extends Canvas implements Runnable {
    /**
     * The Thread.
     */
    private Thread thread;
    /**
     * The Frame.
     */
    private JFrame frame;
    /**
     * The constant running.
     */
    private static boolean running = false;

    /**
     * Instantiates a new Display.
     *
     * @param width  the width
     * @param height the height
     * @param title  the title
     */
    public Display(int width, int height, String title) {
        this.frame = new JFrame();
        Dimension size = new Dimension(width, height);
        this.setPreferredSize(size);
        this.frame.setTitle(title);
        this.frame.add(this);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        running = true;
    }

    /**
     * Start.
     */
    public synchronized void start() {
        running = true;
        this.thread = new Thread(this, "render.Display");
        this.thread.start();
    }

    /**
     * Stop.
     */
    public synchronized void stop() {
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run() Thread#run()Thread#run()Thread#run()
     */
    @Override
    public void run() {
        long mspf = 1000 / Setting.FPS;
        while (running) {
            long startTime = System.currentTimeMillis();
            update();
            render();
            long end = System.currentTimeMillis();
            long work = end - startTime;
            if (mspf > work) {
                try {
                    Thread.sleep(mspf - work);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        stop();
    }

    /**
     * Update.
     */
    private void update() {
        Main.update();
    }


    /**
     * Render.
     */
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        Main.render(g);
        g.dispose();
        bs.show();
    }


}
