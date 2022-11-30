public class Hurtbox extends Hitbox{

    int lifetime;
    int timer;

    public Hurtbox(Entity _owner, double _x, double _y, double _w, double _h, int _lifetime) {
        super(_owner, _x, _y, _w, _h);
        lifetime = _lifetime;
    }

    public void tick(){

        super.tick();

        if (timer >= lifetime){
            dead = true;
        }
        timer += 1;
    }

    @Override
    public void onCollision(Hitbox collider){
        if (owner != null && collider.owner != null){
            owner.attackTarget(collider.owner);
            dead = true;
        }
    }

}
