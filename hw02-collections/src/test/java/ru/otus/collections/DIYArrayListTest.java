package ru.otus.collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;

class DIYArrayListTest {

    private DIYArrayList list;

    @BeforeEach
    void beforeEach() {
        list = new DIYArrayList<Integer>();
    }

    @Test
    void testGet() {
        Collections.addAll(list, 1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14, 15,
                16, 17, 18, 19, 20,
                21, 22, 23, 24, 25);
        assertEquals(list.get(12), 13);
    }

    @Test
    void testSize() {
        Collections.addAll(list, 1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14, 15,
                16, 17, 18, 19, 20,
                21, 22, 23, 24, 25);
        assertEquals(list.size(), 25);
    }

    @Test
    void testAddAll() {
        Collections.addAll(list, 1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14, 15,
                16, 17, 18, 19, 20,
                21, 22, 23, 24, 25);
        assertEquals(list.size(), 25);
        for(int i = 0; i < 25; ++i) {
            assertEquals(list.get(i), i + 1);
        }
    }

    @Test
    void testSort() {
        Collections.addAll(list, 25, 24, 23, 22, 21,
                20, 19, 18, 17, 16,
                15, 14, 13, 12, 11,
                10, 9, 8, 7, 6,
                5, 4, 3, 2, 1);
        Collections.sort(list);
        for(int i = 0; i < 25; ++i) {
            assertEquals(list.get(i), i + 1);
        }
    }

    @Test
    void testSortWithComp() {
        Collections.addAll(list, 25, 24, 23, 22, 21,
                20, 19, 18, 17, 16,
                15, 14, 13, 12, 11,
                10, 9, 8, 7, 6,
                5, 4, 3, 2, 1);
        Collections.sort(list, (a, b) -> {  //сортировка по возрастанию с группировкой по четности:
            int aInt = (Integer) a;         //сначала четные, потом нечетные
            int bInt = (Integer) b;
            if(aInt % 2 == bInt % 2) {
                return Integer.compare(aInt, bInt);
            }
            else {
                return Integer.compare(aInt % 2, bInt % 2);
            }
        });
        int index = 0;
        while(index < list.size()) {
            if(index <= 11) {
                assertEquals(list.get(index), 2 * (index + 1));
            }
            else {
                assertEquals(list.get(index), 2 * (index - 11) - 1);
            }
            index++;
        }
    }

    @Test
    void testCopy() {
        Collections.addAll(list, 1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14, 15,
                16, 17, 18, 19, 20,
                21, 22, 23, 24, 25);
        ArrayList<Integer> listForCopy = new ArrayList<>();
        for(int i = 0; i < 25; ++i) {
            listForCopy.add(0);
        }
        Collections.copy(listForCopy, list);
        for(int i = 0; i < 25; ++i) {
            int el = listForCopy.get(i);
            assertEquals(el, i + 1);
        }
        listForCopy.clear();
    }

    @AfterEach
    void afterEach() {
        list = null;
    }

}