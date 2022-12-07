import java.util.ArrayList;

public class Player extends Entity {

    double speed = 2;
    double speedX;
    double speedY;
    double coins;
    double baseDamage = 3.5;
    public Weapon weapon;
    public ArrayList<Animation> extraAnimations = new ArrayList<>();



    public Player(double _x, double _y){
        super(_x, _y);
        int l = 8;
//        // Idle animation
//        animations.add(new Animation(this, 4, 14, true, new int[]{l, l, l, l}));
//        // Walk animation
//        animations.add(new Animation(this, 6, 15, true, new int[]{l, l, l, l, l, l}));
//        // Attack animation 0
//        // old
//        //         animations.add(new AttackAnimation(this, 6, 16, false, new int[]{l/4, l, l/2, l/4, l*2, l}));
//        animations.add(new AttackAnimation(this, 6, 16, false, new int[]{l, l, l/2, l, l/2, l/4}));
//        // Hitstun animation
//        animations.add(new Animation(this, 1, 4, true, new int[]{1}));
//        // Parry animation
//        animations.add(new ParryAnimation(this, 1, 17, false, new int[]{30}));
        extraAnimations.add(new Animation(this, 2, 18, true, new int[]{4, 2}));

        Animation timestop = new Animation(this, 4, 19, true, new int[]{8, 32, 16, 12});
        timestop.repeatAt = 2;
        extraAnimations.add(timestop);


        Animation goof = new Animation(this, 6, 20, true, new int[]{32, 32, 8, 8, 8, 8});
        goof.repeatAt = 2;
        extraAnimations.add(goof);

        Animation banditbringer = new BanditBringerAnimation(this, 3, 21, false, new int[]{4, 12, 48});
        extraAnimations.add(banditbringer);

        Animation falling = new Animation(this, 6, 22, false, new int[]{8, 8, 8, 8, 8, 8});
        extraAnimations.add(falling);

        Animation kick = new KickAnimation(this, 4, 23, false, new int[]{4, 2, 8, 4});
        extraAnimations.add(kick);

        weapon = new AxeWeapon(this);
        animations = weapon.animations;
        animations.addAll(extraAnimations);

        new Hitbox(this, 12, 20, 10, 25);
        new CollisionBox(this, 14, 22, 8, 23);

        groundPoints.add(new Point(this, 12, 45));
        groundPoints.add(new Point(this, 22, 45));
        state = "idle";
        health = 100;
    }

    public void tick(){
        super.tick();
        applyForces();
        handleInputs();
        //handleMovement();
    }



    public void handleInputs(){
        ArrayList keysPressed = Game.currentGame.panel.keysPressed;
        ArrayList mouseButtonsPressed = Game.currentGame.panel.mouseButtonsPressed;


        int attack1Key = 65; // A
        int attack2Key = 83; // S
        int blockKey = 68; // D
        int ability1Key = 90; // Z
        int ability2Key = 88; // X
        int ability3Key = 67; // C


        if (hasFlag("DIanimation") || hasFlag("dontChangeAnimation") || hasFlag("stunned")) return;

        // left
        if (keysPressed.contains(37) && state != "parry"){
            move(-speed, 0);
            flipX = true;
            if (state != "jump") {
                state = "walk";
                changeAnimation(1);
            }

        }

        // right
        if (keysPressed.contains(39) && state != "parry"){
            move(speed, 0);
            flipX = false;
            if (state != "jump") {
                state = "walk";
                changeAnimation(1);
            }
        }


        // attack
        if (keysPressed.contains(attack1Key) || mouseButtonsPressed.contains(1)) {
            if (!(keysPressed.contains(39) || keysPressed.contains(37))){
                attack();
            }
        }

        // attack 2
        if (keysPressed.contains(attack2Key)){
            if (!(keysPressed.contains(39) || keysPressed.contains(37))){
                attack2();
            }
        }

        // RIGHT CLICK
        if (keysPressed.contains(blockKey) ||mouseButtonsPressed.contains(3)){
            parry();
        }

        // W
        if (keysPressed.contains(38) || keysPressed.contains(32)) {
            jump();
            changeAnimation(9);
            state = "jump";
        }

        if (!keysPressed.contains(39) && !keysPressed.contains(37)){
            if (state != "attack" && state != "parry" && state != "jump" && !flags.contains("stunned")) changeAnimation(0);
        }

        // 1
        if (keysPressed.contains(49)){
            changeWeapon(new AxeWeapon(this));
        }

        // 2
        if (keysPressed.contains(50)){
            changeWeapon(new LeoWeapon(this));

        }

        // 3
        if (keysPressed.contains(51)){
            changeWeapon(new SolWeapon(this));
        }

        // 4
        if (keysPressed.contains(ability3Key)){
            Game.currentGame.freezeEntities(80);
            changeAnimation(5);
            this.addEffect(new FlagDurationEffect(this, 80, "DIanimation"));
            Game.currentGame.ap.stopSound(2);
        }

        // 5
        if (keysPressed.contains(ability2Key)){
            changeAnimation(6);
            this.addEffect(new FlagDurationEffect(this, 120, "DIanimation"));
            this.addEffect(new FlagDurationEffect(this, 300, "timestop"));
        }

        // 6
        if (keysPressed.contains(54)){
            changeAnimation(7);
            this.addEffect(new FlagDurationEffect(this, 300, "dontChangeAnimation"));
        }

        // 7
        if (keysPressed.contains(ability1Key)){
            changeAnimation(8);
            state = "attack";

        }


    }


    public void handleMovement(){
        double dt = Game.currentGame.deltaTime;
        this.x += speedX * dt;
        y += speedY * dt;
    }

    public double getDamage(){
        return baseDamage;
    }

    public void onGottenHit(Entity attacker){

        if (hasFlag("invincible")) return;

        if (state == "parry"){
            addEffect(new FlagDurationEffect(this, 16, "invincible"));
            removeFlag("parryOnCooldown");
            addEffect(new FlagDurationEffect(this, 16, "parryOnCooldown"));
            addEffect(new FlagDurationEffect(this, 16, "timestop"));

            attack();
            return;
        }

        super.onGottenHit(attacker);
        addEffect(new FlagDurationEffect(this, 16, "stunned"));
    }

    public void onAddedFlag(String flag){
        if (flag == "stunned"){
            changeAnimation(3);
        }
    }

    public void onRemovedFlag(String flag){
        if (flag == "stunned"){
            state = "idle";
        }
    }

    public void attack(){
        state = "attack";
        changeAnimation(2);
    }

    public void attack2(){
        state = "attack";
        changeAnimation(10);
    }

    public void parry(){

        if (hasFlag("parryOnCooldown")) return;

        state = "parry";
        addEffect(new FlagDurationEffect(this, 85, "parryOnCooldown"));
        changeAnimation(4);
    }

    public void changeWeapon(Weapon w){
        weapon = w;
        animations = w.animations;
        animations.addAll(extraAnimations);
    }

    public void jump(){
        if (!isOnGround()) return;
        velocityY = -3.6;
    }

    public void onLand(){
        state = "idle";
    }

    public void onHitTarget(Entity t){
        if (animations.get(currentAnimationId).flags.contains("banditbringer")){
            velocityX = 0;
            changeAnimation(0);
        }
    }

}
