import java.awt.*;

public class Point {

    Entity owner;
    double x;
    double y;

    public Point(Entity _owner, double _x, double _y){
        owner = _owner;
        x = _x;
        y = _y;
    }

    double getWorldX(){
        return owner.x + x;
    }

    double getWorldY(){
        return owner.y + y;
    }

    public void draw(Graphics g){
        g.setColor(Color.GREEN);
        g.drawRect((int)getWorldX(), (int)getWorldY(), 0, 0);
    }

}
