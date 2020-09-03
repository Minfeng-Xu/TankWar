package com.feng.tankwar;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameClient extends JComponent {

    static final int WIDTH = 800, HEIGHT = 600;

    private final static Random RANDOM = new Random();

    static final GameClient INSTANCE = new GameClient();

    private static final String GAME_SAV = "game.sav";

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

    private Blood blood;

    public Blood getBlood() {
        return blood;
    }

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
        this.blood = new Blood(400, 250);
        this.walls = Arrays.asList(
                new Wall(250, 140, true, 12),
                new Wall(250, 530, true, 12),
                new Wall(100, 160, false, 12),
                new Wall(700, 160, false, 12)
        );
        initEnemyTanks();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
        g.fillRect(0, 0, WIDTH, HEIGHT);
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
            if(playerTank.isDying() && RANDOM.nextInt(3) == 1){
                blood.setLive(true);
            }
            if(blood.isLive()){
                blood.draw(g);
            }


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
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.save();
                    System.exit(0);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Failed to save current game",
                            "Oops! Error detected!", JOptionPane.ERROR_MESSAGE);
                    System.exit(4);
                }

            }
        });
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

        try {
            client.load();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to load previous game",
                    "Oops! Error detected!", JOptionPane.ERROR_MESSAGE);
        }

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

    private void load() throws IOException {
        File file = new File(GAME_SAV);
        if(file.exists() && file.isFile()) {
            String json  = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            Save save = JSON.parseObject(json, Save.class);
            if(save.isGameContinued()) {
                this.playerTank = new Tank(save.getPlayerPosition(), false);
                this.enemyTanks.clear();
                if(save.enemyPositions != null && !save.enemyPositions.isEmpty()) {
                    for (Save.Position position : save.enemyPositions) {
                        this.enemyTanks.add(new Tank(position, true));
                    }
                }

            }
        }
    }

    void save(String destination) throws IOException {
        Save save = new Save(playerTank.isLive(), playerTank.getPosition(),
                enemyTanks.stream().filter(Tank::isLive)
                        .map(Tank::getPosition).collect(Collectors.toList()));
        FileUtils.write(new File(destination), JSON.toJSONString(save, true), StandardCharsets.UTF_8);
    }

    void save() throws IOException {
        this.save(GAME_SAV);
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
