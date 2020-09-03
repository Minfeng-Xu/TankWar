package com.feng.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameClient extends JComponent {

    static final GameClient INSTANCE = new GameClient();

    static GameClient getInstance(){
        return INSTANCE;
    }

    private Tank playerTank;

    public Tank getPlayerTank() {
        return playerTank;
    }



    private List<Tank> enemyTanks;

    private final AtomicInteger enemyKilled = new AtomicInteger(0);

    private List<Wall> walls;

    private List<Missile> missiles;

    private List<Explosion> explosions;

    void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    List<Wall> getWalls() {
        return walls;
    }

    List<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    List<Missile> getMissiles() {
        return missiles;
    }



    private GameClient() {
        this.playerTank = new Tank(400,100, Direction.DOWN);
        this.explosions = new ArrayList<>();
        this.missiles = new CopyOnWriteArrayList<>();
        this.walls = Arrays.asList(
                new Wall(200, 140, true, 15),
                new Wall(200, 530, true, 15),
                new Wall(100, 160, false, 12),
                new Wall(700, 160, false, 12)
        );
        initEnemyTanks();
        this.setPreferredSize(new Dimension(800, 600));
    }

    private void initEnemyTanks() {
        this.enemyTanks = new CopyOnWriteArrayList<>();
        for (int i= 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                this.enemyTanks.add(new Tank(200+ 120*j, 400 +40*i, true, Direction.UP));
            }

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);
        if(!playerTank.isLive()) {
            g.setColor(Color.RED);
            g.setFont(new Font(null, Font.BOLD, 100));
            g.drawString("GAME OVER", 100, 300);
            g.setFont(new Font(null, Font.BOLD, 50));
            g.drawString("PRESS R TO RESTART", 120, 380);
        }else {

            g.setColor(Color.white);
            g.setFont(new Font(null, Font.BOLD, 16));
            g.drawString("Missiles: " + missiles.size(), 10, 30);
            g.drawString("Explosion: " + explosions.size(), 10, 50);
            g.drawString("MPlayer Tank HP: " + playerTank.getHp(), 10, 70);
            g.drawString("Enemy left: " + enemyTanks.size(), 10, 90);
            g.drawString("Enemy killed: " + enemyKilled.get(), 10, 110);
            g.drawImage(utils.getImage("tree.png"),720, 10, null);
            g.drawImage(utils.getImage("tree.png"),10, 510, null);

            playerTank.draw(g);

            int count = enemyTanks.size();
            enemyTanks.removeIf(t -> !t.isLive());
            enemyKilled.addAndGet(count - enemyTanks.size());
            if(enemyTanks.isEmpty()) {
                this.initEnemyTanks();
            }
            for (Tank tank : enemyTanks) {
                tank.draw(g);
            }
            for (Wall wall : walls){
                wall.draw(g);
            }


            missiles.removeIf(m -> !m.isLive());
            for (Missile missile: missiles) {
                missile.draw(g);
            }

            explosions.removeIf(e -> !e.isLive());
            for (Explosion explosion : explosions) {
                explosion.draw(g);
            }
        }


    }

    public static void main(String[] args) {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        JFrame frame = new JFrame();
        frame.setTitle("Tank War-- Feng");
        frame.setIconImage(new ImageIcon("assets/images/icon.png").getImage());
        GameClient client = GameClient.getInstance();
        frame.add(client);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                client.playerTank.keyPressed(e);

            }

            @Override
            public void keyReleased(KeyEvent e) {
                client.playerTank.keyReleased(e);
            }
        });
        frame.setVisible(true);

        while (true) {

            try {
                client.repaint();
                if (client.playerTank.isLive()){
                    for(Tank tank : client.enemyTanks) {
                        tank.actRandomly();
                    }
                }

                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void restart() {
        if(!playerTank.isLive()) {
            playerTank = new Tank(400,100, Direction.DOWN);
            enemyKilled.set(0);
            missiles.clear();
            explosions.clear();
            this.initEnemyTanks();
        }

    }
}
