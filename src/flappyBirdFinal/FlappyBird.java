package flappyBirdFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;
    public int WIDTH = 800, HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList <Rectangle> columns;
    public boolean started, gameOver;
    public Random rand;
    JButton bStart = new JButton("start");
    public int ticks, yMotion, score;


    public FlappyBird()
    {
        JFrame jframe = new JFrame("FlappyBird");
        JPanel jPanel = new JPanel();
        Timer timer = new Timer(20, this);
        rand = new Random();

        renderer = new Renderer();

        jframe.add(renderer);
        jframe.setSize(HEIGHT,WIDTH);
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //add Button to JPanels
        jPanel.add(bStart);
        this.renderer.add(jPanel);
        bStart.addActionListener(this);


        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

        columns = new ArrayList<>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();

    }

    public void addColumn(boolean start){
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);
        if (start)      //Paint column bottom
        {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else           //Paint colum top
        {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }
    public void paintColumn(Graphics g, Rectangle column)
    {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump()
    {

        if (!started){
            started = true;
        }
        if (gameOver)           //restart game if gameOver
        {
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }
        else if (!gameOver)
        {
            if (yMotion > 0)
            {
                yMotion = 0;            //Bird gravitiy
            }

            yMotion -= 10;
        }
    }

    public void repaint(Graphics g){
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns)        //for-each loop for every colum
        {
            paintColumn(g, column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 75));


        if (gameOver)                   //gameOver screen when you died
        {
            g.drawString("Game Over!", WIDTH / 4, HEIGHT / 2 - 50);
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 500);

        }
        if (!gameOver && started)           //paints Score
        {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 700);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;
        int speed;

        if (e.getSource()==bStart) {            //Funktion for bStart
            bStart.setText("Spiel läuft!");
            jump();

            if (FlappyBird.flappyBird.bStart.getText().equals(" Restart! "))
            {
                bStart.setText("Spiel läuft!");
                jump();
            }
        }

        if (started && !gameOver) {         //Movement for Gamefield
            speed = 10;

                bird.y += yMotion;
                for (int i = 0; i < columns.size(); i++) {
                    Rectangle column = columns.get(i);
                    if (gameOver) {
                        speed = 0;
                    }
                    column.x -= speed;
                }
            }
        if (ticks % 2 == 0 && yMotion < 15)
        {
            yMotion += 2;
        }
        for (int i = 0; i < columns.size(); i++)
        {
            Rectangle column = columns.get(i);

            if (column.x + column.width < 0)
            {
                columns.remove(column);

                if (column.y == 0)          //column hit y side
                {
                    addColumn(false);
                }
            }
        }
        for (Rectangle column : columns)
        {
            if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10)
            {
                score++;
            }

            if (column.intersects(bird))
            {
                gameOver = true;

                if (bird.x <= column.x)
                {
                    bird.x = column.x - bird.width;

                }
                else
                {
                    if (column.y != 0)
                    {
                        bird.y = column.y - bird.height;
                    }
                    else if (bird.y < column.height)
                    {
                        bird.y = column.height;
                    }
                }
            }
        }
        if (bird.y > HEIGHT - 120 || bird.y < 0)
        {
            gameOver = true;
        }

        if (bird.y + yMotion >= HEIGHT - 120)
        {
            bird.y = HEIGHT - 120 - bird.height;
            gameOver = true;
        }
        if (gameOver)
        {
            bStart.setText("Restart");
        }
        renderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            jump();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

