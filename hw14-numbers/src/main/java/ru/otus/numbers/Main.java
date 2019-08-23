package ru.otus.numbers;

public class Main {

    private static int number = 1;
    private static final String firstThreadName = "Thread 1";
    private static final String secondThreadName = "Thread 2";
    private static boolean isEnabledFirst = true;
    private static boolean isEnabledSecond = false;
    private static boolean direction = true;
    private static boolean isFinish = false;

    private static synchronized void print() {
        String name = Thread.currentThread().getName();
        if(number > 0) {
            if(name.equals(firstThreadName)) {
                if(isEnabledFirst) {
                    System.out.println(firstThreadName + ": " + number);

                    isEnabledFirst = false;
                    isEnabledSecond = true;
                }
            } else if(name.equals(secondThreadName)) {
                if(isEnabledSecond) {
                    System.out.println(secondThreadName + ": " + number);
                    System.out.println("----------------------");

                    if(number == 10) direction = false;
                    if(direction) {
                        number++;
                    } else {
                        number--;
                    }
                    if(number == 0) {
                        isFinish = true;
                    }

                    isEnabledFirst = true;
                    isEnabledSecond = false;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            while(!isFinish) {
                print();
            }
        });
        Thread thread2 = new Thread(() -> {
            while(!isFinish) {
                print();
            }
        });
        thread1.setName(firstThreadName);
        thread2.setName(secondThreadName);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Finish");
    }

}
