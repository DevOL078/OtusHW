package ru.otus.gc;

/*
-Xms256m
-Xmx256m
-Xlog:gc=debug:file=./hw05-gc/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./hw05-gc/logs/dump
-XX:+UseG1GC
-XX:+UseSerialGC
-XX:+UseParallelGC
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OutMemoryMain {

    private static List<Integer> list;

    public static void main(String[] args) throws InterruptedException {
        calculateObjectsPerSecond();
        doExperiment();
    }

    private static void doExperiment() throws InterruptedException {
        final int limit = Integer.MAX_VALUE;

        list = new ArrayList<>();
        for (int i = 1; i < limit; ++i) {
            list.add(Integer.valueOf(i));

            if (i % 1000000 == 0) {
                for (int j = 0; j < list.size() / 2; ++j) {
                    list.set(j, null);
                }
            }

            if (i % 50000 == 0) {
                Thread.sleep(1000);
            }
        }
    }

    private static void calculateObjectsPerSecond() {
        AtomicInteger oldSize = new AtomicInteger(0);
        new Thread(() -> {
            while(true) {
                int listSize = list.size();
                int delta = listSize - oldSize.get();
                oldSize.set(listSize);
                System.out.println(delta);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

}
