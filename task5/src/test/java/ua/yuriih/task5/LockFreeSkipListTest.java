package ua.yuriih.task5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LockFreeSkipListTest {

    @Test
    void singleThread() {
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
}