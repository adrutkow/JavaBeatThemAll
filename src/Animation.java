import java.util.ArrayList;

public class Animation{

    int length;
    int id;
    boolean repeat;
    int[] speed;
    int currentFrame = 0;
    int timer = 0;
    Entity owner;
    public int repeatAt = 0;
    public ArrayList<String> flags = new ArrayList<>();

    public Animation(Entity owner, int len, int _id, boolean _repeat, int[] _speed){
        this.owner = owner;
        length = len;
        id = _id;
        repeat = _repeat;
        speed = _speed;
    }

    void tick(){

        if (timer > speed[currentFrame]){
            if (currentFrame == 0) onAnimationStart();
            currentFrame += 1;
            onFrameChange();
            if (currentFrame >= length){
                if (repeat){
                    currentFrame = repeatAt;
                } else{
                    currentFrame = length-1;
                    onAnimationEnd();
                }
            }
            timer = 0;
        }

        timer ++;
    }

    public void onFrameChange(){

    }

    public void reset(){
        currentFrame = 0;
        timer = 0;
    }

    public void onAnimationStart(){

    }

    public void onAnimationEnd(){

    }

    public void onAnimationCancel(){

    }
}


class AttackAnimation extends Animation{

    public AttackAnimation(Entity owner, int len, int _id, boolean _repeat, int[] _speed) {
        super(owner, len, _id, _repeat, _speed);
    }

    @Override
    public void onAnimationEnd(){
        owner.state = "idle";
        owner.changeAnimation(0);
    }

    @Override
    public void onFrameChange(){
        if (currentFrame == 3){
            int xOffset = 0;
            if (owner.flipX) xOffset = -20;
            new Hurtbox(owner, 8 + xOffset, 30, 38, 5, 5);
        }
    }
}

class ParryAnimation extends Animation{

    public ParryAnimation(Entity owner, int len, int _id, boolean _repeat, int[] _speed) {
        super(owner, len, _id, _repeat, _speed);
    }

    @Override
    public void onAnimationEnd(){
        Game.currentGame.player.state = "idle";
        Game.currentGame.player.changeAnimation(0);
    }
}

class BanditBringerAnimation extends Animation {
    public BanditBringerAnimation(Entity owner, int len, int _id, boolean _repeat, int[] _speed){
        super(owner, len, _id, _repeat, _speed);
        flags.add("banditbringer");
    }

    @Override
    public void onFrameChange(){
        if (currentFrame == 2){
            double force = 3;
            owner.velocityX = force;
            if (owner.flipX) owner.velocityX = -force;
            new Hurtbox(owner, 20, 10, 5, 20, 32);
        }
    }

    @Override
    public void onAnimationEnd(){
        owner.velocityX = 0;
        owner.state = "idle";
    }

    @Override
    public void onAnimationCancel(){
        onAnimationEnd();
    }

}

class KickAnimation extends Animation{

    public KickAnimation(Entity owner, int len, int _id, boolean _repeat, int[] _speed) {
        super(owner, len, _id, _repeat, _speed);
    }

    @Override
    public void onFrameChange() {
        super.onFrameChange();
        if (currentFrame == 1){
            int xOffset = 0;
            if (owner.flipX) xOffset = -30;
            new Hurtbox(owner, 20 + xOffset,35, 30, 10, 10);
        }
    }

    @Override
    public void onAnimationEnd(){
        owner.state = "idle";
        owner.changeAnimation(0);
    }
}