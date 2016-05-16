package com.votafore.applicationdesigner.support;

import android.graphics.Point;

import com.votafore.applicationdesigner.model.Block;

import java.util.List;

/**
 * класс определяет поведение для того, что бы подчиненные
 * элементы блока распологались горизонтально.
 */
public class BlockPlacementHorizontal extends BlockPlacement {

    public BlockPlacementHorizontal(Block block){
        super(block);
    }

    public static void setPlacement(Block block){
        block.setPlacement(new BlockPlacementHorizontal(block));
    }

    @Override
    protected float[] calculatePoint(float[] result, int index, int childCount) {

        float width = mBlock.getWidth();

        float widthSpace = (width*(1f - Block.SPACE*2)) / childCount;

        result[0] = width/2 - Block.SPACE - index*widthSpace - widthSpace/2;

        return result;
    }
}
