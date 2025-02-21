package org.example.game;

import java.util.Set;

public class BlockUtils {

    public static final Set<Block> ALL_SHAPES = Set.of(
            new Block(Shapes.T_SHAPE),
            new Block(Shapes.I_SHAPE),
            new Block(Shapes.O_SHAPE),
            new Block(Shapes.L_SHAPE),
            new Block(Shapes.J_SHAPE),
            new Block(Shapes.Z_SHAPE),
            new Block(Shapes.S_SHAPE)
    );

}
