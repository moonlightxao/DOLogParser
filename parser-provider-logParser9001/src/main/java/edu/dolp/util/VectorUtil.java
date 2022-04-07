package edu.dolp.util;

import org.apache.commons.math3.linear.ArrayRealVector;

//向量工具类
public class VectorUtil {
    /**
     * @Description: 将数组转换为向量
     * @Param:  arr: 要转换的数组   flag: 是否复制一个新的数组来创建向量，flag==false表示不创建新的数组
     * @return:  ArrayRealVector向量
     * @Author: Liu ZhiTian
     * @Date: 2022/2/13
     */
    public static ArrayRealVector arrayToVector(double[] arr, boolean flag){
        return new ArrayRealVector(arr, flag);
    }
}