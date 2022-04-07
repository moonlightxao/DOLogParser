package edu.dolp.entity;


import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Component
public class FileTransQueue {
    private static Deque<FileEntity> queue = new LinkedList<>();

    public static void offer(FileEntity entity){
        queue.offer(entity);
    }

    public static FileEntity poll(){
        return queue.poll();
    }

    public static boolean isEmpty(){
        return queue.isEmpty();
    }
}
