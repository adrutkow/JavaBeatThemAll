public class Level {

    public int[][] tileLayout = new int[128][32];

    public Level() {
        for (int i = 0; i < 128; i++){
            for (int j = 0; j < 32; j++){
                tileLayout[i][j] = -1;
            }
        }

        for (int i = 0; i < 128; i++){
            tileLayout[i][12] = 1;
            tileLayout[i][13] = 2;
            tileLayout[i][14] = 2;
            //tileLayout[i][2] = 3;

        }

    }

    int getTile(double x, double y){
        // pixel size 16
        return tileLayout[(int)(x / 16)][(int)(y / 16)];
    }

}
