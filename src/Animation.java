public class Animation{

    int length;
    int id;
    boolean repeat;
    int[] speed;
    int currentFrame = 0;
    int timer = 0;
    Entity owner;
    public int repeatAt = 0;

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
