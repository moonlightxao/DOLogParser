package edu.dolp.util;

//将按行读取的日志文本进行预处理
public class ParseUtil {
    /*
     * 通常日志文件可以被划分成四个部分(月，日，时间戳，主机，日志信息)
     * 为了提高分类的效率，将除了日志信息之外的三个部分进行预处理删除
     * */
    public static String parse(String str){
        String[] ss = str.split(" ", 5);
        return ss[ss.length - 1];
    }
}