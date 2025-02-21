package org.example.util;

public class Pos {

    public static final Pos DOWN = new Pos(0,1);
    public static final Pos UP = new Pos(0,-1);
    public static final Pos LEFT = new Pos(-1,0);
    public static final Pos RIGHT = new Pos(1,0);

    private final int x,y;

    public Pos(int x,int y){
        this.x = x;
        this.y = y;
    }

    public Pos add(Pos pos){
        return new Pos(this.x + pos.x(),this.y + pos.y());
    }

    public Pos add(int dx, int dy){
        return new Pos(this.x + dx, this.y + dy);
    }

    public Pos subtract(Pos pos){
        return new Pos(this.x - pos.x(),this.y - pos.y());
    }

    public Pos subtract(int dx, int dy){
        return new Pos(this.x + dx, this.y + dy);
    }

    public int x(){
        return x;
    }

    public int y(){
        return y;
    }

    @Override
    public String toString() {
        return "Pos: x = " + x + "; y = " + y;
    }
}
