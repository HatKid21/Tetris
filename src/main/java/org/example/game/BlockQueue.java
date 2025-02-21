package org.example.game;

import java.util.*;

public class BlockQueue {

    private final Queue<Block> blockQueue = new ArrayDeque<>();
    private final List<Block> blockBag = new ArrayList<>(BlockUtils.ALL_SHAPES);
    private final Random random = new Random();

    private static final int BAGS_TO_KEEP = 2;

    public BlockQueue(){
        refillQueue();

    }

    private void refillQueue(){
        while (blockQueue.size() < BAGS_TO_KEEP * blockBag.size()){
            Collections.shuffle(blockBag, random);
            for (Block block : blockBag){
                blockQueue.add(block.cloneBlock());
            }
        }
    }

    public Block nextBlock(){
        if (blockQueue.size() < BAGS_TO_KEEP * blockBag.size()){
            refillQueue();
        }
        return blockQueue.poll();
    }

    public List<Block> peekBlock(){
        List<Block> upcomingBlocks = new ArrayList<>();
        Iterator<Block> iterator = blockQueue.iterator();

        while (iterator.hasNext() && upcomingBlocks.size() < 7){
            upcomingBlocks.add(iterator.next());
        }
        return upcomingBlocks;

    }


}
