package org.example.util;

public class Matrix2D {

    public Pos[] matrix2d = new Pos[2];
    public static final Matrix2D ANTICLOCKWISE = new Matrix2D(new Pos(0,-1), new Pos(1,0));
    public static final Matrix2D CLOCKWISE = new Matrix2D(new Pos(0,1),new Pos(-1,0));
    public static final Matrix2D FLIP = new Matrix2D(new Pos(-1,0),new Pos(0,-1));

    public Matrix2D(Pos iHat,Pos jHat){
        matrix2d[0] = iHat;
        matrix2d[1] = jHat;
    }

    public Pos multiply(Pos pos2d){
        Pos one = new Pos(matrix2d[0].x() * pos2d.x(),matrix2d[0].y() * pos2d.x());
        Pos two = new Pos(matrix2d[1].x() * pos2d.y(),matrix2d[1].y() * pos2d.y());
        return one.add(two);
    }

}
