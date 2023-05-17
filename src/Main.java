import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame implements KeyListener {
    public int p1Pos;
    public int p2Pos;
    public double ballX;
    public double ballY;

    public double ballSpeedX;
    public double ballSpeedY;
    public boolean p1SpeedLeft = false;
    public boolean p1SpeedRight = false;
    public boolean p2SpeedLeft = false;
    public boolean p2SpeedRight = false;
    public int p1Score = 0;
    public int p2Score = 0;

    public long continueTimestamp = 0;

    public Main() {
        super("Pong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        resetBall();

        Panel p = new Panel();
        p.setSize(600, 600);
        p.setPreferredSize(new Dimension(600, 600));
        addKeyListener(this);

        add(p);
        pack();
    }

    public void run() throws InterruptedException {
        while (true) {
            Thread.sleep(16);
            // move players
            int PLAYER_SPEED = 5;
            p1Pos += (p1SpeedLeft ? -PLAYER_SPEED : 0) + (p1SpeedRight ? PLAYER_SPEED : 0);
            p2Pos += (p2SpeedLeft ? -PLAYER_SPEED : 0) + (p2SpeedRight ? PLAYER_SPEED : 0);
            p1Pos = Math.max(Math.min(p1Pos, 400), 0);
            p2Pos = Math.max(Math.min(p2Pos, 400), 0);

            // move ball
            if (System.currentTimeMillis() > continueTimestamp) {
                ballX += ballSpeedX;
                ballY += ballSpeedY;
            }

            if (ballX + ballRadius > 600) {
//                ballX = 600 - (ballX - 600);
                ballSpeedX = -ballSpeedX;
            }
            if (ballX - ballRadius < 0) {
//                ballX = -ballX;
                ballSpeedX = -ballSpeedX;
            }

            final double randomness = 5.0;
            // p1 ball collision
            if (ballSpeedY < 0) {
                if (ballY - ballRadius < 25 && ballX > p1Pos && ballX < p1Pos + 200) {
                    ballSpeedY = -ballSpeedY;
                    ballSpeedY += 1/ballSpeedY * Math.random() * randomness;
                    ballSpeedX = ballSpeedX + Math.random() * randomness - randomness / 2.0;
                }
            }

            if (ballSpeedY > 0) {
                if (ballY + ballRadius > 600 - 10 && ballX > p2Pos && ballX < p2Pos + 200) {
                    ballSpeedY = -ballSpeedY;
                    ballSpeedY -= 1 / ballSpeedY * Math.random() * randomness;
                    ballSpeedX = ballSpeedX + Math.random() * randomness - randomness / 2.0;
                }
            }

            if (ballY < 0) {
                p1Score += 1;
                resetBall();
            } else if (ballY > 600) {
                p2Score += 1;
                resetBall();
            }

            this.repaint();
        }
    }

    public void resetBall() {
        ballX = 300;
        ballY = 300;
        ballSpeedX = (int) (Math.random() * 3 - 1.5);
        ballSpeedY = (int) (Math.random() * 12 - 6.0);

        if (Math.abs(ballSpeedY) < 4.0) {
            resetBall();
        }

        continueTimestamp = System.currentTimeMillis() + 1500;
    }

    public static void main(String[] args) {
        Main m = new Main();
        try {
            m.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            p1SpeedLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            p1SpeedRight = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            p2SpeedLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            p2SpeedRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            p1SpeedLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            p1SpeedRight = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            p2SpeedLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            p2SpeedRight = false;
        }
    }

    final int ballRadius = 20;

    class Panel extends JPanel {
        @Override
        public void paint(Graphics g) {

            // clear last frame
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 600, 600);

            // draw ball
            g.setColor(Color.WHITE);
            g.fillRect((int) ballX - ballRadius, (int) ballY - ballRadius, ballRadius, ballRadius);

            // draw player 1
            g.fillRect(p1Pos, 10, 200, 15);

            // draw player 1
            g.fillRect(p2Pos, 600 - 10 - 15, 200, 15);

            // draw scores
            Font font = new Font("Serif", Font.PLAIN, 30);
            g.setFont(font);
            g.drawString("" + p1Score, 550, 280);
            g.drawString("" + p2Score, 550, 320);
        }
    }
}