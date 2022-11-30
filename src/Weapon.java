import java.util.ArrayList;

public abstract class Weapon {
    public ArrayList<Animation> animations = new ArrayList<>();
    public Player owner;
    public Animation idleAnimation;
    public Animation walkAnimation;
    public Animation attackAnimation;
    public Animation hitstunAnimation;
    public Animation parryAnimation;

    public Weapon(Player owner){
        this.owner = owner;
    }

}

class SolWeapon extends Weapon{
    public SolWeapon(Player owner){
        super(owner);
        int l = 8;
        animations.add(new Animation(owner, 4, 14, true, new int[]{l, l, l, l}));
        animations.add(new Animation(owner, 6, 15, true, new int[]{l, l, l, l, l, l}));
        animations.add(new AttackAnimation(owner, 6, 16, false, new int[]{l, l, l/2, l, l/2, l/4}));
        animations.add(new Animation(owner, 1, 4, true, new int[]{1}));
        animations.add(new ParryAnimation(owner, 1, 17, false, new int[]{30}));
    }
}

class LeoWeapon extends Weapon{
    public LeoWeapon(Player owner){
        super(owner);
        int l = 8;
        animations.add(new Animation(owner, 4, 10, true, new int[]{l, l, l, l}));
        animations.add(new Animation(owner, 6, 11, true, new int[]{l, l, l, l, l, l}));
        animations.add(new AttackAnimation(owner, 6, 12, false, new int[]{l/4, l, l/2, l/4, l*2, l}));
        animations.add(new Animation(owner, 1, 4, true, new int[]{1}));
        animations.add(new ParryAnimation(owner, 1, 13, false, new int[]{30}));
    }
}

class AxeWeapon extends Weapon{
    public AxeWeapon(Player owner){
        super(owner);
        int l = 8;
        animations.add(new Animation(owner, 4, 1, true, new int[]{l, l, l, l}));
        animations.add(new Animation(owner, 6, 3, true, new int[]{l, l, l, l, l, l}));
        animations.add(new AttackAnimation(owner, 6, 0, false, new int[]{l/4, l, l/2, l/4, l*2, l}));
        animations.add(new Animation(owner, 1, 4, true, new int[]{1}));
        animations.add(new ParryAnimation(owner, 1, 8, false, new int[]{30}));
    }
}
