import java.awt.*;
import java.util.ArrayList;

public abstract class UIelement {
    int x;
    int y;
    int w;
    int h;
    boolean doDraw = true;
    Color defaultGUIcolor = Color.GRAY;
    ArrayList<UIelement> components = new ArrayList<>();
    UIelement parent = null;


    public UIelement(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public UIelement(int x, int y, int w, int h, UIelement parent) {
        this(x, y, w, h);
        this.parent = parent;
    }

    public void tick(){
        for (int i = 0; i < components.size(); i++) {
            UIelement c = components.get(i);
            c.tick();
        }

        if (Game.currentGame.currentlyDragging == this){
            x = Game.currentGame.panel.getScreenMouseX() - Game.currentGame.currentlyDraggingOffsetX;
            y = Game.currentGame.panel.getScreenMouseY() - Game.currentGame.currentlyDraggingOffsetY;

        }

    }

    public void draw(Graphics g){
        drawDefaultBox(g, x, y, w, h);
        drawChildren(g);
    }

    public void drawChildren(Graphics g){
        for (int i = 0; i < components.size(); i++) {
            UIelement c = components.get(i);
            c.draw(g);
        }
    }


    public boolean isMouseInElement(int mx, int my){

        int gx = x;
        int gy = y;

        if (parent != null){
            gx = x + parent.x;
            gy = y + parent.y;
        }


        double scX = Game.currentGame.panel.scaleX;
        double scY = Game.currentGame.panel.scaleY;

        return (mx > gx * scX && mx < (gx + w) * scX && my > gy * scY && my < (gy + h) * scY);
    }

    public void checkChildrenForClick(int mx, int my){

        for (int i = 0; i < components.size(); i++) {
            UIelement child = components.get(i);
            child.checkChildrenForClick(mx, my);
            if (child.isMouseInElement(mx, my)){
                child.onClick();
            }
        }
    }

    public void onClick(){
        System.out.println("clicked!");
    }

    public void drawDefaultBox(Graphics g, int x, int y, int w, int h){
        g.setColor(defaultGUIcolor);
        g.fillRect(x, y, w, h);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawLine(x, y+h, x+w, y+h);
        g.drawLine(x+w, y, x+w, y+h);
    }

    public void onButtonClicked(int id){

    }

    public void positionInCorner(int corner){
        switch (corner){
            case 0:
                x = 0;
                y = 0;
                break;
            case 1:
                x = parent.w - w;
                y = 0;
                break;
            case 2:
                x = 0;
                y = parent.h - h;
                break;
            case 3:
                x = parent.w - w;
                y = parent.h - h;
                break;
        }
    }

    public int getScreenX(){
        if (parent == null) return x;
        return x + parent.x;
    }

    public int getScreenY(){
        if (parent == null) return y;
        return y + parent.y;
    }

    public int[] getScreenCenter(){
        if (parent == null) return new int[]{x + w/2, y + h/2};
        return new int[]{getScreenX() + w/2, getScreenY() + h/2};
    }


    public void delete(){
        if (Game.currentGame.currentlyDragging == this)
            Game.currentGame.currentlyDragging = null;
        Game.currentGame.UIelements.remove(Game.currentGame.UIelements.indexOf(this));
    }
}

class UIbutton extends UIelement{

    int id;
    String text = "";

    public UIbutton(int x, int y, int w, int h, UIelement parent) {
        super(x, y, w, h);
        this.parent = parent;
        this.id = parent.components.size();
    }

    public void draw(Graphics g){
        drawDefaultBox(g,parent.x + x, parent.y + y, w, h);
        if (text != ""){
            g.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, h/2));
            g.drawString(text, getScreenX(), getScreenY() + h/2);
        }
    }

    public void onClick(){
        parent.onButtonClicked(this.id);
    }



    public void setText(String s){
        text = s;
    }


}

class UIShopButton extends UIelement{

    public UIShopButton(int x, int y, int w, int h) {
        super(x, y, w, h);
        // ID0: Shop button
        components.add(new UIbutton(0, 0, w, h, this));
    }

    public void onButtonClicked(int id){
        switch (id){
            case 0:
                Game.currentGame.UIelements.add(new UIShopMenu(50, 50, 50, 50));
                break;
        }
    }
}

class UIDragger extends UIelement{

    public UIDragger(int x, int y, int w, int h, UIelement parent) {
        super(x, y, w, h, parent);
    }

    @Override
    public void onClick() {
        super.onClick();

        if (Game.currentGame.currentlyDragging == parent){
            Game.currentGame.currentlyDragging = null;
            return;
        }


        if (Game.currentGame.currentlyDragging == null){
            Game.currentGame.currentlyDragging = parent;
            Game.currentGame.currentlyDraggingOffsetX = Game.currentGame.panel.getScreenMouseX() - parent.x;
            Game.currentGame.currentlyDraggingOffsetY = Game.currentGame.panel.getScreenMouseY() - parent.y;
        }
    }

    public void draw(Graphics g){
        drawDefaultBox(g, getScreenX(), getScreenY(), w, h);
    }

}

class UIShopMenu extends UIelement{

    public UIShopMenu(int x, int y, int w, int h) {
        super(x, y, w, h);


        // 0: Close button
        components.add(new UIbutton(0, 0, 10, 10, this));
        components.get(0).positionInCorner(1);

        // 1: Upgrades button
        UIbutton b = new UIbutton(0, 15, 30, 10, this);
        b.setText("Upgrades");
        components.add(b);

        // 3: Drag element
        components.add(new UIDragger(0, 0, w, 5, this));

    }

    public void onButtonClicked(int id){
        switch (id){
            case 0:
                delete();
                break;
            case 1:
                Game.currentGame.UIelements.add(new UIUpgradesMenu(x+5, y+5, 50, 50));

        }
    }
}

class UIUpgradesMenu extends UIelement{

    public UIUpgradesMenu(int x, int y, int w, int h) {
        super(x, y, w, h);

        components.add(new UIDragger(0, 0, w, 5, this));
    }
}
