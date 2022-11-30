public class Collectible extends Entity{

    public Collectible(double _x, double _y) {
        super(_x, _y);
    }
}

class Coin extends Collectible{

    public Coin(double _x, double _y) {
        super(_x, _y);
        int l = 8;
        currentAnimationId = 0;
        animations.add(new Animation(this, 4, 7, true, new int[]{l, l, l, l}));
        new CollisionBox(this, 0, 0, 8, 8);
    }

    @Override
    public void onCollisionBoxCollide(Entity collider) {
        super.onCollisionBoxCollide(collider);
        if (collider.getClass() == Player.class){
            die();
            ((Player) collider).coins += 1;
            Game.currentGame.ap.playSound(0);

        }
    }
}
