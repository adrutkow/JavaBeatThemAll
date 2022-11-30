import java.awt.*;

public class CollisionBox extends Hitbox{
    public CollisionBox(Entity _owner, float _x, float _y, float _w, float _h) {
        super(_owner, _x, _y, _w, _h);
    }

    public void tick(){
        for (int i = 0; i < Game.currentGame.entities.size(); i++) {
            Entity e = Game.currentGame.entities.get(i);
            if (e == owner) continue;
            for (int j = 0; j < e.hitboxes.size(); j++) {
                Hitbox h = e.hitboxes.get(j);
                if (h.getClass() == CollisionBox.class) continue;
                if (doCollide(this, h)){
                    owner.onCollisionBoxCollide(h.owner);
                }
            }
        }
    }

    public void draw(Graphics g){
        Color oldColor = g.getColor();
        g.setColor(Color.BLUE);
        super.draw(g);
        g.setColor(oldColor);
    }

}
