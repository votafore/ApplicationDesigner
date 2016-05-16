package com.votafore.applicationdesigner.support;


import com.votafore.applicationdesigner.model.Block;

import java.util.List;

/**
 *  класс задает набор функций, необходимых для размещения блоков
 *  внутри друг друга.
 *  Функций, которые будут размещать подчиненные блоки
 *  - вертикально
 *  - горизонтально
 *  - еще какой-нибудь вариант.
 */

public abstract class BlockPlacement {

    protected Block mBlock;

    public BlockPlacement(Block block){
        mBlock = block;
    }

    protected abstract float[] calculatePoint(float[] result, int index, int childCount);

    /**
     * процедура возвращает координаты указанного блока в родителе
     * т.е. относительно центра родителя
     */
    public final float[] getPoint(Block block){

        // по умолчанию центр блока совпадает с центром родителя
        float[] result = new float[]{0.0f, 0.0f, 0.0f};

        // проверяем пинадлежит ли блок текущему "родителю"
        List<Block> childs = mBlock.getChilds();
        int index = childs.indexOf(block);
        // если не принадлежит.... надо бы выкинуть исключение
        // т.к. координаты возвращать нельзя, но пока не будем этого делать
        // TODO: разобраться что тут надо сделать если в параметры передан "левый" объект. Хотя, вполне возможно что такой ситуации не будет
        if (index < 0)
            return result;

        if (childs.size() == 1)
            return result;

        return calculatePoint(result, index, childs.size());
    }
}
