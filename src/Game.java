import javax.imageio.ImageIO;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Game {

    JFrame window = new JFrame();
    int[] defaultWindowSize = {426, 240};
    GamePanel panel;
    boolean isRunning = true;
    Level level;
    Player player;
    double deltaTime = 0;
    double oldTime;
    public static Game currentGame;
    ArrayList<Entity> entities = new ArrayList<>();
    ArrayList<UIelement> UIelements = new ArrayList<>();
    UIelement currentlyDragging;
    int currentlyDraggingOffsetX;
    int currentlyDraggingOffsetY;
    Camera camera;

    ArrayList<Entity> entitiesToBeRemoved = new ArrayList<>();
    ArrayList<Hitbox> hitboxesToBeRemoved = new ArrayList<>();
    public AudioPlayer ap;



    public Game() throws IOException {
        currentGame = this;
        camera = new Camera();

        try{
            ap = new AudioPlayer();
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }

        window.addMouseListener(new CustomMouseListener());
        window.setMinimumSize(new Dimension(defaultWindowSize[0], defaultWindowSize[1]));
        window.setSize(defaultWindowSize[0], defaultWindowSize[1]);
        window.setUndecorated(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel = new GamePanel();
        panel.setBackground(Color.GRAY);
        window.add(panel);
        window.setVisible(true);

        window.addKeyListener(panel);
        level = new Level();
        player = new Player(213, 50f);
        entities.add(player);
        entities.add(new Enemy(200, 50));
        oldTime = System.currentTimeMillis();
        entities.add(new Coin(100, 50));
        UIelements.add(new UIShopButton(defaultWindowSize[0] - 12, 2, 10, 10));

        ap.playSound(1);
    }

    void tick(){

        entitiesToBeRemoved = new ArrayList<>();
        hitboxesToBeRemoved = new ArrayList<>();

        panel.scaleX = (double)Game.currentGame.window.getWidth() / defaultWindowSize[0];
        panel.scaleY = (double)Game.currentGame.window.getHeight() / defaultWindowSize[1];
        double newTime = System.currentTimeMillis();
        deltaTime = (newTime - oldTime)/1000;
        oldTime = newTime;

        tickEntities();
        tickUI();

        entities.removeAll(entitiesToBeRemoved);
        for (int i = 0; i < hitboxesToBeRemoved.size(); i++) {
            Hitbox h = hitboxesToBeRemoved.get(i);
            h.die();
        }

        Random r = new Random();
        int randomInt = r.nextInt(240);
        if (randomInt == 0) entities.add(new Enemy(-20, 146));

        if (randomInt == 1) entities.add(new Enemy(400, 146));

        panel.repaint();
    }

    public void tickEntities(){
        if (player.hasFlag("timestop")){
            player.tick();
            return;
        }



        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            if (e.hasFlag("frozen")) {
                for (int j = 0; j < e.effects.size(); j++) {
                    if (e.effects.get(j).flag == "frozen") e.effects.get(j).tick();
                }
                continue;
            }

            if (!e.dead) e.tick();
            if (e.dead) entitiesToBeRemoved.add(e);
//            for (int j = 0; j < e.hitboxes.size(); j++) {
//                Hitbox h = e.hitboxes.get(j);
//                if (h.dead) hitboxesToBeRemoved.add(h);
//            }
        }
    }

    public void tickUI(){
        for (int i = 0; i < UIelements.size(); i++) {
            UIelement u = UIelements.get(i);
            u.tick();
        }
    }

    public void freezeEntities(int duration){
        for (int i = 0; i < Game.currentGame.entities.size(); i++) {
            Entity e = Game.currentGame.entities.get(i);
            if (e == player) continue;
            e.addEffect(new FlagDurationEffect(e, duration, "frozen"));
        }
    }

}

class GamePanel extends JPanel implements ActionListener, KeyListener {

    int tileSize = 16;
    double scale = 1f;
    ArrayList<Integer> keysPressed = new ArrayList<>();
    ArrayList<Integer> mouseButtonsPressed = new ArrayList<>();
    ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
//    ArrayList<ArrayList<BufferedImage>> playerTiles = new ArrayList<>();
 //   ArrayList<ArrayList<BufferedImage>> enemyTiles = new ArrayList<>();
  //  ArrayList<ArrayList<BufferedImage>> collectibleTiles = new ArrayList<>();
    ArrayList<ArrayList<BufferedImage>> spriteAnimations = new ArrayList<>();
    public double scaleX;
    public double scaleY;


    Game gameInstance;

    public GamePanel() throws IOException {
        gameInstance = Game.currentGame;
        BufferedImage image;
        image = ImageIO.read(new File(".\\src\\assets\\tileset.png"));
        GetTilesFromTileSet(image);
        // 0
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_attack.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_idle.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_jump.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_walk.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_hurt.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        // 5
        image = ImageIO.read(new File(".\\src\\assets\\enemy\\enemy_walk.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\enemy\\enemy_hurt.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\collectibles\\coin.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 8));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_parry.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\enemy\\enemy_attack.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        // 10
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_leo_idle.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_leo_walk.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_leo_attack.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_leo_parry.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_sol_idle.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        // 15
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_sol_walk.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_sol_attack.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_sol_parry.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_sol_di.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_timestop.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        // 20
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_goofy.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_banditbringer.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));
        image = ImageIO.read(new File(".\\src\\assets\\player\\player_jump.png"));
        spriteAnimations.add(GetAnimationFromImage(image, 48));


        // 0 - p attack
        // 1 - p idle
        // 2 - p jump
        // 3 - p walk
        // 4 - p hurt
        // 5 - e walk
        // 6 - e hurt
        // 7 - coin



    }

    public void GetTilesFromTileSet(BufferedImage image) throws IOException {

        for (int y = 0; y < 8; y++){
            for (int x = 0; x < 8; x++){
                BufferedImage img = image.getSubimage(x*tileSize, y*tileSize, tileSize, tileSize);
                tiles.add(img);
            }
        }
    }



    public ArrayList<BufferedImage> GetAnimationFromImage(BufferedImage image, int tileSize){
        ArrayList<BufferedImage> list = new ArrayList<>();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        for (int y = 0; y < imageHeight / tileSize; y++){
            for (int x = 0; x < imageWidth / tileSize; x++){
                BufferedImage img = image.getSubimage(x*tileSize, y*tileSize, tileSize, tileSize);
                list.add(img);
            }
        }
        return list;
    }

    @Override
    public void paintComponent(Graphics g){
        Camera c = Game.currentGame.camera;
        Player p = Game.currentGame.player;
        ((Graphics2D) g).scale((double)scaleX, (double)scaleY);
        g.setColor(new Color(124, 148, 161));


        if (p.hasFlag("DIanimation")){
            double v = 1.7;
            ((Graphics2D) g).scale(v, v);
            ((Graphics2D) g).translate(-Game.currentGame.player.x, -Game.currentGame.player.y);
            ((Graphics2D) g).translate(100, 50);
            g.setColor(new Color(96, 59, 58));

        }

        if (p.hasFlag("timestop")){
            g.setColor(new Color(161, 159, 124));
        }

        if (p.hasFlag("stunned")){
            g.setColor(new Color(96, 59, 58));
        }




        g.fillRect(0, 0, this.getWidth(), this.getHeight());


        for (int i = 0; i < gameInstance.entities.size(); i++){
            Entity e = gameInstance.entities.get(i);
            e.draw(g);
            for (int j = 0; j < e.hitboxes.size(); j++) {
                e.hitboxes.get(j).draw(g);
            }
            for (int j = 0; j < e.groundPoints.size(); j++) {
                e.groundPoints.get(j).draw(g);
            }
        }

        for (int x = 0; x < 28; x++){
            for (int y = 0; y < 16; y++){
                int tile = gameInstance.level.tileLayout[x][y];
                if (tile == -1) {continue;}
                g.drawImage(tiles.get(tile), (int)(x * scale * tileSize), (int)(y * scale * tileSize), null);
            }
        }

        for (int i = 0; i < gameInstance.UIelements.size(); i++) {
            UIelement u = gameInstance.UIelements.get(i);
            u.draw(g);
        }

        g.setFont(new Font("TimesRoman", Font.PLAIN, 5));
        g.drawString("fps: "+Main.fps, 0, 5);

        if (getMousePosition() == null) return;
        g.drawString("mouse: " + getMousePosition().getX() / scaleX + " " + getMousePosition().getY() / scaleY, 0, 10);
        g.drawString("coins: "+Game.currentGame.player.coins, 0, 15);
        g.drawString("onGround: "+Game.currentGame.player.isOnGround(), 0, 20);



        g.drawRect(1, 1, 425, 239);
    }

    public int getScreenMouseX(){
        return (int) (getMousePosition().x / scaleX);
    }

    public int getScreenMouseY(){
        return (int) (getMousePosition().y / scaleY);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == 'f'){
            JFrame window = gameInstance.window;
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            //window.setVisible(true);
        }
        if (keyEvent.getKeyChar() == 'h'){
            Game.currentGame.window.setUndecorated(false);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (!keysPressed.contains(keyEvent.getKeyCode())){
            keysPressed.add(keyEvent.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keysPressed.contains(keyEvent.getKeyCode())){
            int index = keysPressed.indexOf(keyEvent.getKeyCode());
            keysPressed.remove(index);
        }
    }


//    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
//        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics2D = resizedImage.createGraphics();
//        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
//        graphics2D.dispose();
//        return resizedImage;
//    }

}

class CustomMouseListener implements MouseListener{

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        ArrayList<Integer> m = Game.currentGame.panel.mouseButtonsPressed;
        if (!m.contains(mouseEvent.getButton())){
            m.add(mouseEvent.getButton());
        }

        for (int i = 0; i < Game.currentGame.UIelements.size(); i++) {
            UIelement u = Game.currentGame.UIelements.get(i);
            if (u.isMouseInElement(mouseEvent.getX(), mouseEvent.getY())){
                u.onClick();
                u.checkChildrenForClick(mouseEvent.getX(), mouseEvent.getY());
            }
        }





    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        ArrayList<Integer> m = Game.currentGame.panel.mouseButtonsPressed;
        if (m.contains(mouseEvent.getButton())){
            int index = m.indexOf(mouseEvent.getButton());
            m.remove(index);
        }

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

