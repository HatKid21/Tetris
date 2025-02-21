package org.example.game;

import org.example.util.Pos;

import java.awt.*;
import java.util.List;

public interface Shapes {

    Shape T_SHAPE = new Shape(Color.MAGENTA,List.of(
            new Pos(0,0),
            new Pos(0,-1),
            new Pos(-1,0),
            new Pos(1,0)
    ));

    Shape I_SHAPE = new Shape(Color.CYAN,List.of(
            new Pos(0,0),
            new Pos(-1,0),
            new Pos(1,0),
            new Pos(2,0)
    ));

    Shape O_SHAPE = new Shape(Color.YELLOW,List.of(
            new Pos(0,0),
            new Pos(0,1),
            new Pos(1,0),
            new Pos(1,1)
    ));

    Shape L_SHAPE = new Shape(Color.ORANGE,List.of(
            new Pos(0,0),
            new Pos(-1,0),
            new Pos(1,0),
            new Pos(1,-1)
    ));

    Shape J_SHAPE = new Shape(Color.BLUE,List.of(
            new Pos(0,0),
            new Pos(1,0),
            new Pos(-1,0),
            new Pos(-1,-1)
    ));

    Shape Z_SHAPE = new Shape(Color.RED,List.of(
            new Pos(0,0),
            new Pos(0,-1),
            new Pos(-1,-1),
            new Pos(1,0)
    ));

    Shape S_SHAPE = new Shape(Color.GREEN,List.of(
            new Pos(0,0),
            new Pos(-1,0),
            new Pos(0,-1),
            new Pos(1,-1)
    ));



}
