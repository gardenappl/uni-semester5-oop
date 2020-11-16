package ua.yuriih.task5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LockFreeSkipListTest {

    @Test
    void insert_singleThread() {
        LockFreeSkipList<Integer> list = new LockFreeSkipList<Integer>(3);
        assertFalse(list.contains(1));
        assertFalse(list.contains(2));
        assertFalse(list.contains(3));

        list.insert(3);
        assert(list.contains(3));

        list.insert(1);
        assert(list.contains(1));
        assert(list.contains(3));

        list.insert(2);
        assert(list.contains(1));
        assert(list.contains(2));
        assert(list.contains(1));
    }

    @Test
    void insert_multiThread() {
        LockFreeSkipList<Integer> list = new LockFreeSkipList<Integer>(3);

        //Insert odd numbers
        Thread threadOdd = new Thread(() -> {
            for (int i = 0; i < 100; i += 2)
                list.insert(i);
        });

        threadOdd.start();

        //Insert even numbers
        for (int i = 1; i < 100; i += 2)
            list.insert(i);

        assertDoesNotThrow(() -> threadOdd.join());

        for (int i = 0; i < 100; i++)
            assert(list.contains(i));
    }
}