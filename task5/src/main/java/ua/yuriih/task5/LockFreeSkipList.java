package ua.yuriih.task5;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeSkipList<T> {
    private static final class Node<T> {
        final T keyObject;
        final long key;
        final AtomicMarkableReference<Node<T>>[] nexts;
        
        public Node(T keyObject, int height) {
            this(keyObject, keyObject.hashCode(), height);
        }
        
        public Node(long key, int height) {
            this(null, key, height);
        }
        
        private Node(T keyObject, long key, int height) {
            this.keyObject = keyObject;
            this.key = key;
            this.nexts = new AtomicMarkableReference[height];
            for (int i = 0; i < height; i++)
                this.nexts[i] = new AtomicMarkableReference<>(null, false);
        }
    }

    
    
    private static final int NOT_FOUND = -1;
    private final int levels;
    private final Node<T> first;
    private final Node<T> last;
    
    public LockFreeSkipList(int levels) {
        if (levels < 1)
            throw new IllegalArgumentException("Skip list must have at least 1 level");

        this.levels = levels;
        first = new Node<>(Long.MIN_VALUE, levels);
        last = new Node<>(Long.MAX_VALUE, levels);
        for (int i = 0; i < levels; i++) {
            first.nexts[i].set(last, false);
        }
    }
    
    private int find(T element, Node<T>[] predecessors, Node<T>[] successors) {
        Node<T> newNode = new Node<>(element, levels);
        int key = element.hashCode();

        int levelFound = -1;
        Node<T> previous = first;

        //search for place to insert, starting from top level
        for (int level = levels - 1; level >= 0; level--) {
            Node<T> current = previous.nexts[level].getReference();
            while (key > current.key) {
                previous = current;
                current = previous.nexts[level].getReference();
            }
            //TODO: unlink marked nodes
            
            if (levelFound == -1 && current.key == key) {
                //found element; don't stop, populate predecessors and successors
                levelFound = level;
            }

            predecessors[level] = previous;
            successors[level] = current;
        }
        return levelFound;
    }

    //TODO: more efficient search method
    public boolean contains(T element) {
        Node<T>[] preds = new Node[levels];
        Node<T>[] succs = new Node[levels];
        return find(element, preds, succs) != NOT_FOUND;
    }
    
    private int getHeightForNewNode() {
        return 1 + (int)(Math.random() * levels);
    }
    
    public boolean insert(T element) {
        int newNodeHeight = getHeightForNewNode();

        Node<T>[] predecessors = new Node[levels];
        Node<T>[] successors = new Node[levels];

        //keep retrying if another element had been inserted here
        while (true) {
            int levelFound = find(element, predecessors, successors);
            if (levelFound != NOT_FOUND)
                return false;

            //create new node
            Node<T> newNode = new Node<>(element, newNodeHeight);
            for (int level = 0; level < newNodeHeight; level++) {
                newNode.nexts[level].set(successors[level], false);
            }
            Node<T> predecessor = predecessors[0];
            Node<T> successor = successors[0];

            //insert at main level
            if (!predecessor.nexts[0].compareAndSet(successor, newNode, false, false))
                continue;

            //insert references at higher levels
            for (int level = 1; level < newNodeHeight; level++) {
                while (true) {
                    if (predecessors[level].nexts[level].compareAndSet(successors[level],
                            newNode, false, false)) {
                        break;
                    } else {
                        //predecessor array is invalid, re-create
                        find(element, predecessors, successors);
                    }
                }
            }
            return true;
        }
    }
}
