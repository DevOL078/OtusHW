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

public class OutMemoryMain {

    public static void main(String[] args) throws InterruptedException {
        doExperiment();
    }

    private static void doExperiment() throws InterruptedException {
        final int limit = Integer.MAX_VALUE;

        List<Integer> list = new ArrayList<>();
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

}
