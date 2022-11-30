import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Entity {

    double x;
    double y;
    double velocityX;
    double velocityY;
    double maxHealth = 2;
    double health = 2;
    double attackDamage = 1;
    ArrayList<Hitbox> hitboxes = new ArrayList<>();
    ArrayList<Point> groundPoints = new ArrayList<>();
    Boolean dead = false;
    int currentAnimationId = -1;
    boolean flipX;
    ArrayList<Animation> animations = new ArrayList<>();
    ArrayList<Effect> effects = new ArrayList<>();
    ArrayList<String> flags = new ArrayList<>();
    String state;

    public Entity(double _x, double _y) {
        x = _x;
        y = _y;
    }

    public void tick(){



        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).tick();
        }

        for (int i = 0; i < hitboxes.size(); i++) {
            Hitbox h = hitboxes.get(i);
            h.tick();
            if (h.dead) Game.currentGame.hitboxesToBeRemoved.add(h);
        }

        if (currentAnimationId != -1) {
            animations.get(currentAnimationId).tick();
        }
    }


    public void move(double dx, double dy){
        double newX = x + dx;
        double newY = y + dy;

        x = newX;
        y = newY;






//        if (!isOnGround()) velocityY += 0.1;
//
//        applyGravity();
//
//
//        if (!isOnGround() && isOnGround(newX, newY)){
//            System.out.println("land");
//            x = newX;
//            y = ((int)(newY / 16)) * 16;
//        } else {
//            x = newX;
//            y = newY;
//        }

    }

    public void applyForces(){
        double gravityForce = 0.16;


        double newX = x + velocityX;
        double newY = y + velocityY;

        if (!isOnGround()) velocityY += gravityForce;
        if (isOnGround() && !(velocityY < 0)) velocityY = 0;



        boolean wasOnGround = isOnGround();

        x = newX;
        y = newY;


        // on land

        if (!wasOnGround && isOnGround()) {
            //y = (int)(((int)((y) / 16)) * 16);
            y = 147;
            velocityY = 0;
            onLand();
        }

    }

    public boolean isOnGround(double _x, double _y){
        for (Point p : groundPoints) {
            if (Game.currentGame.level.getTile(_x + p.x, _y + p.y) != -1) return true;
        }
        return false;
    }

    public boolean isOnGround(){
        for (Point p : groundPoints) {
            if (Game.currentGame.level.getTile(x + p.x, y + p.y) != -1) return true;
        }
        return false;
    }

    public void onLand(){

    }

    public void draw(Graphics g){
        if (currentAnimationId == -1) return;
        Animation tempAnimation = animations.get(currentAnimationId);
        BufferedImage image = Game.currentGame.panel.spriteAnimations.get(tempAnimation.id).get(tempAnimation.currentFrame);
        int xOffset = 0;

        if (flipX){
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            xOffset = image.getWidth()/4;
        }

        g.drawImage(image, (int)x - xOffset, (int)y, null);
        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y, 1, 1);
        //animations.get(currentAnimationId).tick();
    }

    public void attackTarget(Entity target){
        onHitTarget(target);
        target.onGottenHit(this);
    }

    public void onHitTarget(Entity target){

    }

    public void onGottenHit(Entity attacker){
        addHealth(-attacker.getDamage());
    }

    public void addHealth(double value){
        health += value;
        if (health <= 0) die();
    }

    public void onCollisionBoxCollide(Entity collider){

    }

    public void die(){
        onDeath();
            dead = true;
        //Game.currentGame.entities.remove(Game.currentGame.entities.indexOf(this));
    }

    public void onDeath(){

    }

    public double getDamage(){
        return 1;
    }

    public void changeAnimation(int id){
        if (currentAnimationId == id) return;
        currentAnimationId = id;
        animations.get(currentAnimationId).reset();
    }

    public void addFlag(String flag){
        onAddedFlag(flag);
        if (!flags.contains(flag)) flags.add(flag);
        System.out.println(flag);
    }

    public void onAddedFlag(String flag){

    }

    public void removeFlag(String flag){
        onRemovedFlag(flag);
        if (flags.contains(flag)) flags.remove(flag);
    }

    public void onRemovedFlag(String flag){

    }

    public void addEffect(Effect effect){
        effects.add(effect);
    }

    public boolean hasFlag(String flag){
        return flags.contains(flag);
    }



}
