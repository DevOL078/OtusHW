package ru.otus;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("Starting list:");
        System.out.println(list);

        Predicate<Integer> mod3Rest1 = number -> (number % 3 == 1);
        List<Integer> listMod3Rest1 = Lists.newArrayList(Collections2.filter(list, mod3Rest1));
        System.out.println("List of numbers with rest 1 (mod 3):");
        System.out.println(listMod3Rest1);
    }

}
