package com.prokopchuk.hashtable;

import java.util.Objects;

public class HashTable<K, V> {

    public static void main(String[] args) {
        HashTable<String, String> hashTable = new HashTable<>();
        hashTable.put("Stas", "stas@test.com");
        hashTable.put("Taras", "taras@test.com");
        hashTable.put("Nikolas", "nikolas@test.com");
        hashTable.put("Alexiy", "Alexiy@test.com");
        hashTable.put("Ivan", "Ivan@test.com");
        hashTable.put("Maxim", "Maxim@test.com");
        hashTable.put("Ihor", "Ihor@test.com");
        hashTable.put("Kiril", "Kiril@test.com");
        hashTable.put("Vitaliy", "Vitaliy@test.com");
        hashTable.put("Dmitriy", "Dmitriy@test.com");
        hashTable.put("Sergiy", "Sergiy@test.com");
        hashTable.put("Vladimir", "Vladimir@test.com");
        hashTable.put("Petr", "Petr@test.com");
        hashTable.put("Miroslav", "Miroslav@test.com");
        hashTable.put("Nikolas", "NewNikolas@test.com");

        System.out.println(hashTable.tableToString());
    }

    private int numberOfBuckets = 16;
    private int size;
    @SuppressWarnings({"uchecked", "rawType"})
    private Node<K, V>[] buckets = new Node[numberOfBuckets];

    /**
     * Adds an element to the hash table. Does not support duplicate elements.
     *
     * @param key
     * @param value
     * @return true if it was added
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key);
        int position = hash(key, buckets.length);
        var node = findByKey(buckets[position], key);
        V oldValue = null;
        if (node != null) {
            oldValue = node.value;
            node.value = value;
        } else {
            putNodeIntoBucket(new Node<>(key, value), buckets, position);
            size++;
            if (size >= numberOfBuckets) {
                numberOfBuckets *= 2;
                resize(numberOfBuckets);
            }
        }
        return oldValue;
    }

    private void putNodeIntoBucket(Node<K, V> node, Node<K, V>[] buckets, int position) {
        var head = buckets[position];

        if (head == null) {
            buckets[position] = node;
        } else {
            appendNode(head, node);
        }
    }

    private void appendNode(Node<K,V> head, Node<K, V> node) {
        var current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = node;
    }

    private int hash(K key, int numberOfBuckets) {
        return Math.abs(key.hashCode() % numberOfBuckets);
    }

    private Node<K, V> findByKey(Node<K, V> head, K key) {
        var currentNode = head;
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }

        return null;
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

    private String bucketToString(Node<K, V> head) {
        StringBuilder builder = new StringBuilder();
        var current = head;
        while (current != null) {
            builder.append(String.format("%s:%s", current.key, current.value));
            current = current.next;
            if (current != null) {
                builder.append(" -> ");
            }
        }
        return builder.toString();
    }

    private void resize(int newSize) {
        @SuppressWarnings({"uchecked", "rawType"})
        Node<K, V>[] newBuckets = new Node[newSize];

        for (var head : buckets) {
            rearrangeBucketElements(head, newBuckets);
        }

        buckets = newBuckets;
    }

    private void rearrangeBucketElements(Node<K, V> node, Node<K, V>[] newBucket) {
        while (node != null) {
            int position = hash(node.key, numberOfBuckets);
            putNodeIntoBucket(node, newBucket, position);
            var previous = node;
            node = previous.next;
            previous.next = null;
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
