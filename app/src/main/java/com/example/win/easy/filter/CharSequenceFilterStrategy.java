package com.example.win.easy.filter;

import java.util.ArrayList;
import java.util.List;
import dagger.Module;

/**
 * 以输入的字符序列作为依据，对字符序列列表过滤，得到通过过滤的字符序列下标<br/>
 * <br/>
 * 如:<br/>
 * {@code basis}为{@code [A,B,C,D]}<br/>
 * {@code sequenceList}为:<br/>
 * {@code [[A,B,C],}<br/>
 * {@code [A,C,D],}<br/>
 * {@code [A,B,C,D],}<br/>
 * {@code [A,B,C,D,E]]}<br/>
 * 则返回结果为{@code [0,2,3]}<br/>
 */
@Module
public class CharSequenceFilterStrategy implements FilterStrategy<List<Character>> {

    @Override
    public List<Integer> filter(List<Character> basis, List<List<Character>> sequenceList) {
        final int sequenceSize=sequenceList.size();//获取序列数
        int basisSize=basis.size();//获取依据序列的字符个数
        List<Integer> result=new ArrayList<>(sequenceSize);//返回结果

        List<Character> localSequence=null;//遍历过程中当前操作的序列
        int localSize=0;//遍历过程中当前序列的长度

        for (int sequenceIndex = 0; sequenceIndex <sequenceSize ; sequenceIndex++) {
            localSequence=sequenceList.get(sequenceIndex);//获取当前序列
            localSize=localSequence.size();//获取当前序列长度
            //匹配原则
            if((basisSize>localSize&&basis.subList(0,localSize).equals(localSequence))||//basis比sequence长时，比较basis的前缀与sequence
                    (basisSize==localSize&&basis.equals(localSequence)) ||//basis与sequence等长时，直接比较两者
                    (basisSize<localSize&&basis.equals(localSequence.subList(0,basisSize)))){//basis比sequence短时，比较basis与sequence的前缀
                result.add(sequenceIndex);//匹配时将索引加入结果
            }
        }
        return result;
    }
}
