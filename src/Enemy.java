import java.awt.*;

public class Enemy extends Entity{

    double speed = 0.1f;
    int nearEnemyTimer;

    public Enemy(double _x, double _y) {
        super(_x, _y);
        int l = 12;
        // Idle animation
        animations.add(new Animation(this, 6, 5, true, new int[]{l, l, l, l, l, l}));

        // Hitstun animation
        animations.add(new Animation(this, 1, 6, true, new int[]{1}));

        l = 16;
        // Attack animation
        animations.add(new AttackAnimation(this, 6, 9, false, new int[]{l/4, l, l/2, l/4, l*2, l}));

        new Hitbox(this, 12, 20, 10, 25);
        groundPoints.add(new Point(this, 12, 45));
        maxHealth = 10;
        health = maxHealth;
        currentAnimationId = 0;
    }

    public void tick(){
        super.tick();


        applyForces();

        if (!hasFlag("stunned") && currentAnimationId != 2){
            if (Game.currentGame.player.x > x) move(speed, 0);
            if (Game.currentGame.player.x < x) move(-speed, 0);
        }

        if (Math.abs(Game.currentGame.player.x - x) < 35){
            nearEnemyTimer++;
        } else {
            nearEnemyTimer = 0;
        }

        if (nearEnemyTimer >= 35){
            changeAnimation(2);
            nearEnemyTimer = 0;
        }


    }

    public void draw(Graphics g){
        if (flags.contains("stunned")) changeAnimation(1);
        super.draw(g);
    }

    @Override
    public void move(double x, double y){
        super.move(x, y);
        if (x > 0) flipX = false;
        if (x < 0) flipX = true;
    }

    @Override
    public void onGottenHit(Entity attacker){
        super.onGottenHit(attacker);
        addEffect(new FlagDurationEffect(this, 24, "stunned"));
    }

    public void onDeath(){
        Game.currentGame.entities.add(new Coin(x+10, y + 20));
    }

    public void onAddedFlag(String flag){
        if (flag == "stunned") changeAnimation(1);
    }

    public void onRemovedFlag(String flag){
        if (flag == "stunned"){
            changeAnimation(0);
            nearEnemyTimer = 0;
        }
    }
}
