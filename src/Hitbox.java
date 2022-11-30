import java.awt.*;

public class Hitbox {

    double x;
    double y;
    double w;
    double h;
    Entity owner;
    boolean dead = false;


    public Hitbox(Entity _owner, double _x, double _y, double _w, double _h){
        owner = _owner;
        x = _x;
        y = _y;
        w = _w;
        h = _h;
        owner.hitboxes.add(this);
    }

    public void tick(){
        for (int i = 0; i < Game.currentGame.entities.size(); i++) {
            Entity e = Game.currentGame.entities.get(i);
            if (e == owner) continue;
            for (int j = 0; j < e.hitboxes.size(); j++) {
                Hitbox h = e.hitboxes.get(j);
                if (this == h) continue;
                if (doCollide(this, h)){
                    onCollision(h);
                }
            }
        }
    }

    double getWorldX(){
        return owner.x + x;
    }

    double getWorldY(){
        return owner.y + y;
    }

    void draw(Graphics g){
        if (dead) System.out.println("what");
        g.drawRect((int)getWorldX(), (int)getWorldY(), (int)w, (int)h);
    }

    boolean doCollide(Hitbox _h1, Hitbox _h2){
        return (_h1.getWorldX() + _h1.w >= _h2.getWorldX() && _h1.getWorldX() <= _h2.getWorldX()+_h2.w && _h1.getWorldY() + _h1.h >= _h2.getWorldY() && _h1.getWorldY() <= _h2.getWorldY() + _h2.h);
    }

    public void onCollision(Hitbox collider){

    }

    public void die(){
        if (owner.hitboxes.contains(this))
            owner.hitboxes.remove(owner.hitboxes.indexOf(this));
    }

}
