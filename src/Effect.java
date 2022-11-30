public abstract class Effect {

    Entity owner;
    int duration;
    String flag;

    public Effect(Entity owner, int duration){
        this.owner = owner;
        this.duration = duration;
    }

    public void tick(){

        duration -= 1;
        if (duration <= 0){
            onEffectEnd();
            die();
        }
    }

    public void onEffectStart(){
    }

    public void onEffectEnd(){

    }

    public void die(){
        owner.effects.remove(owner.effects.indexOf(this));
    }
}

class FlagDurationEffect extends Effect{

    public FlagDurationEffect(Entity owner, int duration, String flag) {
        super(owner, duration);
        this.flag = flag;
        owner.addFlag(flag);
    }

    @Override
    public void onEffectEnd() {
        super.onEffectEnd();
        owner.removeFlag(flag);
    }

}
