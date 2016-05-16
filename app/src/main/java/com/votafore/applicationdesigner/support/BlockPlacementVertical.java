package com.votafore.applicationdesigner.support;

import com.votafore.applicationdesigner.model.Block;

import java.util.List;

/**
 * класс определяет поведение для того, что бы подчиненные
 * элементы блока распологались вертикально.
 */
public class BlockPlacementVertical extends BlockPlacement {

    public BlockPlacementVertical(Block block) {
        super(block);
    }

    public static void setPlacement(Block block){
        block.setPlacement(new BlockPlacementVertical(block));
    }

    @Override
    protected float[] calculatePoint(float[] result, int index, int childCount) {

        float height = mBlock.getHeight();

        float heightSpace = (height * (1f - Block.SPACE * 2)) / childCount;

        result[2] = height/2 - Block.SPACE - index * heightSpace - heightSpace / 2;

        return result;
    }
}
