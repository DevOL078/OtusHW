package ru.otus.gc.analysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogAnalysis {

    private static final String logsPath = "./hw05-gc/logs/";

    public static void main(String[] args) {
        try {
            System.out.println("================G1 analysis====================");
            analyzeLog("gc1.log");
            System.out.println("================Parallel analysis====================");
            analyzeLog("parallel_collector.log");
            System.out.println("================Serial analysis====================");
            analyzeLog("serial_collector.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void analyzeLog(String fileName) throws IOException {
        Stream<String> lines = Files.lines(Paths.get(logsPath + fileName));
        List<Double> youngReport = new ArrayList<>();
        List<Double> fullReport = new ArrayList<>();
        List<Double> remarkReport = new ArrayList<>();
        lines.forEach(line -> {
            if (line.contains("Young")) {
                youngReport.add(getTimeFromString(line));
            } else if (line.contains("Full")) {
                fullReport.add(getTimeFromString(line));
            } else if (line.contains("Remark")) {
                remarkReport.add(getTimeFromString(line));
            }
        });

        System.out.println("Young: " + youngReport.size());
        for (Double d : youngReport) {
            System.out.print(d + " ");
        }
        System.out.println();
        double midYoung = youngReport.stream().reduce((a, b) -> a + b).get() / youngReport.size();
        System.out.println("Young middle value: " + midYoung);

        System.out.println("Full: " + fullReport.size());
        for (Double d : fullReport) {
            System.out.print(d + " ");
        }
        System.out.println();
        double midFull = fullReport.stream().reduce((a, b) -> a + b).get() / fullReport.size();
        System.out.println("Full middle value: " + midFull);

        System.out.println("Remark: " + remarkReport.size());
        for (Double d : remarkReport) {
            System.out.print(d + " ");
        }
        System.out.println();
        if (!remarkReport.isEmpty()) {
            double midRemark = remarkReport.stream().reduce((a, b) -> a + b).get() / remarkReport.size();
            System.out.println("Remark middle value: " + midRemark);
        }
    }

    private static double getTimeFromString(String str) {
        String[] arr = str.split(" ");
        String time = arr[arr.length - 1];
        return Double.parseDouble(time.substring(0, time.length() - 2));
    }

}
