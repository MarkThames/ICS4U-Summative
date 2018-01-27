package LifeSimulator;

import java.util.LinkedList;
import java.util.Queue;

//map class determines terrain
public class Map {
    //type is type of terrain of a square
    public int[][] type;
    //fruit is amount of food in a square
    public int[][] fruit;

    public Map(int a, int b) {
        type = new int[a][b];
        fruit = new int[a][b];
        int[] spread = new int[6];

        //approximately how frequently a terrain type occurs
        spread[0] = 4;
        spread[1] = 4;
        spread[2] = 4;
        spread[3] = 8;
        spread[4] = 2;
        spread[5] = 2;

        //generate random map
        Queue<Integer> q1 = new LinkedList<Integer>();
        Queue<Integer> q2 = new LinkedList<Integer>();
        Queue<Integer> q3 = new LinkedList<Integer>();
        Queue<Integer> q4 = new LinkedList<Integer>();

        int[][] dis = new int[a][b];
        for (int i = 0; i < a; i++) for (int j = 0; j < b; j++) dis[i][j] = 100 * a * b + 1;
        for (int i = 0; i < Math.sqrt(Math.sqrt(a * b)); i++) {
            int x = (int) (Math.random() * 2 * a / 3 + a / 6.0);
            int y = (int) (Math.random() * 2 * b / 3 + b / 6.0);
            int c = (int) (Math.random() * 5);
            if (c >= 2) c++;
            //0 = rock
            //1 = bad soil
            //2 = good soil
            //3 = water
            //4 = grass
            //5 = trees
            q1.add(x);
            q2.add(y);
            q3.add(c);
            q4.add(spread[c]);
            type[x][y] = c;
            dis[x][y] = 0;
        }

        //generate sea
        for (int i = 0; i < a; i++) {
            int x;
            int y;
            double r = Math.random();
            int spr = spread[3];
            if (r < 0.25) {
                x = (int) (Math.random() * 20);
                y = (int) (Math.random() * b);
                if (x < 10) {
                    if (y < 50 || y > b - 50) spr = (int) (Math.random() * 2) + 1;
                    else if (y < 100 || y > b - 100) spr = (int) (Math.random() * 2) + 2;
                }
            } else if (r < 0.50) {
                x = a - 1 - (int) (Math.random() * 20);
                y = (int) (Math.random() * b);
                if (x > a - 10) {
                    if (y < 50 || y > b - 50) spr = (int) (Math.random() * 2) + 1;
                    else if (y < 100 || y > b - 100) spr = (int) (Math.random() * 2) + 2;
                }
            } else if (r < 0.75) {
                x = (int) (Math.random() * a);
                y = (int) (Math.random() * 20);
                if (y < 10) {
                    if (x < 50 || x > b - 50) spr = (int) (Math.random() * 2) + 1;
                    else if (x < 100 || x > b - 100) spr = (int) (Math.random() * 2) + 2;
                }
            } else {
                x = (int) (Math.random() * a);
                y = b - 1 - (int) (Math.random() * 20);
                if (y > b - 10) {
                    if (x < 50 || x > b - 50) spr = (int) (Math.random() * 2) + 1;
                    else if (x < 100 || x > b - 100) spr = (int) (Math.random() * 2) + 2;
                }
            }
            int c = 3;
            //0 = rock
            //1 = bad soil
            //2 = good soil
            //3 = water
            //4 = grass
            //5 = trees
            q1.add(x);
            q2.add(y);
            q3.add(c);
            q4.add(spr);
            type[x][y] = c;
            dis[x][y] = 0;
        }


        int[][] dir = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!q1.isEmpty()) {
            int curx = q1.poll();
            int cury = q2.poll();
            int curc = q3.poll();
            int curs = q4.poll();
            //if(Math.random()<0.05*spread[curc])continue;
            if (curc != type[curx][cury]) continue;
            for (int k = 0; k < 4; k++) {
                int i = dir[k][0];
                int j = dir[k][1];
                if (curx + i >= 0 && curx + i < a && cury + j >= 0 && cury + j < b) {
                    if (dis[curx + i][cury + j] > dis[curx][cury] + curs) {
                        dis[curx + i][cury + j] = dis[curx][cury] + curs;
                        q1.add(curx + i);
                        q2.add(cury + j);
                        q3.add(curc);
                        q4.add(curs);
                        type[curx + i][cury + j] = curc;
                    }
                }
            }
        }

        //generate fruit values
        //only for type 4/5 terrains
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                if (type[i][j] == 4) {
                    if (Math.random() < 0.8) fruit[i][j] = 0;
                    else fruit[i][j] = 1;
                }
                if (type[i][j] == 5) {
                    fruit[i][j] = (int) (Math.random() * 5);
                }
            }
        }
    }

    //update map
    public void rebuild() {
        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < 500; j++) {
                if (type[i][j] >= 4) {
                    if (Math.random() < 0.0001) fruit[i][j]++;
                    if (fruit[i][j] < 0) fruit[i][j] = 0;
                }
            }
        }

    }
}
