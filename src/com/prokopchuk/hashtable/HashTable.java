package com.prokopchuk.hashtable;

import java.util.Objects;

/**
 * A simple implementation of the Hash Table that allows storing a generic key-value pair. The table itself is based
 * on the array of {@link Node} objects.
 * <p>
 * An initial array capacity is 16.
 * <p>
 * Every time a number of elements is equal to the array size that tables gets resized
 * (it gets replaced with a new array that it twice bigger than before). E.g. resize operation will replace array
 * of size 16 with a new array of size 32. PLEASE NOTE that all elements should be reinserted to the new table to make
 * sure that they are still accessible  from the outside by the same key.
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
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
     * Puts a new element to the table by its key. If there is an existing element by such key then it gets replaced
     * with a new one, and the old value is returned from the method. If there is no such key then it gets added and
     * null value is returned.
     *
     * @param key   element key
     * @param value element value
     * @return old value or null
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
     * Returns a string representation o  table (array) content according to the following format:
     * 0: key1:value1 -> key2:value2
     * 1:
     * 2: key3:value3
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
