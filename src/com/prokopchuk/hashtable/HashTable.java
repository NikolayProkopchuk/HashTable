package com.prokopchuk.hashtable;

import java.util.Objects;

public class HashTable<T> {

    public static void main(String[] args) {
        HashTable<String> hashTable = new HashTable<>();
        hashTable.add("Stas");
        hashTable.add("Taras");
        hashTable.add("Nikolas");
        hashTable.add("Alexiy");
        hashTable.add("Ivan");
        hashTable.add("Maxim");
        hashTable.add("Ihor");
        hashTable.add("Kiril");
        hashTable.add("Vitaliy");
        hashTable.add("Dmitriy");
        hashTable.add("Sergiy");
        hashTable.add("Vladimir");
        hashTable.add("Petr");
        hashTable.add("Miroslav");
        hashTable.add("Nikolas");

        System.out.println(hashTable.tableToString());
    }

    private int numberOfBuckets = 8;
    private final int loadFactor = 2;
    private int load;
    @SuppressWarnings({"uchecked", "rawType"})
    private Node<T>[] buckets = new Node[numberOfBuckets];

    /**
     * Adds an element to the hash table. Does not support duplicate elements.
     *
     * @param element
     * @return true if it was added
     */
    public boolean add(T element) {
        Objects.requireNonNull(element);
        var elementNode = new Node<>(element);

        return putNodeIntoBucket(elementNode, this.buckets);
    }

    private boolean putNodeIntoBucket(Node<T> elementNode, Node<T>[] buckets) {
        int position = hash(elementNode.element, buckets.length);
        var head = buckets[position];

        if (head == null) {
            buckets[position] = elementNode;
        } else if (!contains(head, elementNode.element)) {
            appendNode(head, elementNode);
        } else {
            return false;
        }
        if (load > loadFactor) {
            numberOfBuckets *= 2;
            resize(numberOfBuckets);
        }
        return true;
    }

    private void appendNode(Node<T> head, Node<T> node) {
        var current = head;
        int bucketSize =1;
        while (current.next != null) {
            current = current.next;
            bucketSize++;
        }
        current.next = node;
        load = Math.max(load, bucketSize);
    }

    private int hash(T element, int numberOfBuckets) {
        return Math.abs(element.hashCode() % numberOfBuckets);
    }

    private boolean contains(Node<T> head, T element) {
        var currentNode = head;
        while (currentNode != null) {
            if (currentNode.element.equals(element)) {
                return true;
            }
            currentNode = currentNode.next;
        }

        return false;
    }

    /**
     * Prints a hash table according to the following format
     * 0: Andrii -> Taras
     * 1: Start
     * 2: Serhii
     * ...
     */
    public String tableToString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buckets.length; i++) {
            builder.append(i).append(": ");
            var head = buckets[i];
            if (head != null) {
                builder.append(bucketToString(head));
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    private String bucketToString(Node<T> head) {
        StringBuilder builder = new StringBuilder();
        var current = head;
        while (current != null) {
            builder.append(current.element);
            current = current.next;
            if (current != null) {
                builder.append(" -> ");
            }
        }
        return builder.toString();
    }

    private void resize(int newSize) {
        load = 0;
        @SuppressWarnings({"uchecked", "rawType"})
        Node<T>[] newBuckets = new Node[newSize];

        for (var head : buckets) {
            rearrangeBucketElements(head, newBuckets);
        }

        buckets = newBuckets;
    }

    private void rearrangeBucketElements(Node<T> element, Node<T>[] newBucket) {
        while (element != null) {
            putNodeIntoBucket(element, newBucket);
            var previous = element;
            element = previous.next;
            previous.next = null;
        }
    }

    private class Node<E> {
        private final E element;
        private Node<E> next;

        public Node(E element) {
            this.element = element;
        }
    }
}
