package ua.yuriih.task6;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ConcurrentQueue<T> {
    private static final class Node<T> {
        //next is an object reference which can be updated atomically.
        public final AtomicReference<Node<T>> next;
        public final T value;

        Node(Node<T> next, T value) {
            this.next = new AtomicReference<>(next);
            this.value = value;
        }
    }

    private AtomicReference<Node<T>> head;
    private AtomicReference<Node<T>> tail;

    public ConcurrentQueue() {
        Node<T> dummy = new Node<>(null, null);
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void enqueue(T value) {
        Node<T> node = new Node<>(null, value);
        AtomicReference<Node<T>> currentTail;
        while (true) {
            currentTail = tail;
            AtomicReference<Node<T>> next = currentTail.get().next;

            if (currentTail.get() == tail.get()) {
                if (next.get() == null) {
                    // compareAndSet is an atomic operation.
                    // We compare the value stored at a memory location
                    // with some expectedValue.
                    // If they are equal, we set the contents of that memory location
                    // to a new value, and return true.
                    // Otherwise we refuse to modify it and return false.
                    // The operation is atomic: another thread cannot modify that memory
                    // between the "compare" and "set" steps.
                    if (currentTail.get().next.compareAndSet(null, node))
                        break;
                } else {
                    //Tail was not pointing to the last node,
                    //try to swing tail to the next node
                    tail.compareAndSet(currentTail.get(), next.get());
                }
            }
        }
        //Done, try to swing tail to the inserted node
        tail.compareAndSet(currentTail.get(), node);
    }

    public T dequeue() {
        T returnedValue;

        while (true) {
            AtomicReference<Node<T>> currentHead = head;
            AtomicReference<Node<T>> currentTail = tail;
            AtomicReference<Node<T>> next = currentHead.get().next;

            if (currentHead.get() == head.get()) {
                //is queue empty or is tail falling behind?
                if (currentHead.get() == tail.get()) {
                    if (next.get() == null) {
                        //Queue is empty
                        return null;
                    }
                    //Tail is falling behind, advance it
                    tail.compareAndSet(currentTail.get(), next.get());
                } else {
                    returnedValue = next.get().value;
                    //Try to swing Head to the next node
                    if (head.compareAndSet(currentHead.get(), next.get()))
                        break;
                }
            }
        }
        return returnedValue;
    }
}
