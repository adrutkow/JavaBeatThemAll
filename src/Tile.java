public class Tile {
    public int id;
    public int[] position = new int[2];

    public Tile(int id, int x, int y){
        this.id = id;
        position[0] = x;
        position[1] = y;
    }
}
