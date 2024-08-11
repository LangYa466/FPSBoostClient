package com.fpsboost.util;

/**
 * A buffer which stores it's contents in an array.
 * You can only add contents to it. If you add Fire elements than it can hold it will overflow and
 * overwrite the first element. Made to improve performance for time measurements.
 *
 * @author superblaubeere27
 */
public class RollingArrayLongBuffer {
    /**
     * -- GETTER --
     *
     * @return The contents of the buffer
     */
    private final long[] contents;
    private int currentIndex = 0;

    public long[] getContents() {
        return contents;
    }

    public RollingArrayLongBuffer(int length) {
        this.contents = new long[length];
    }

    /**
     * Adds an element to the buffer
     *
     * @param l The element to be added
     */
    public void add(long l) {
        currentIndex = (currentIndex + 1) % contents.length;
        contents[currentIndex] = l;
    }

    /**
     * Gets the count of elements added in a row
     * which are higher than l
     *
     * @param l The threshold timestamp
     * @return The count
     */
    public int getTimestampsSince(long l) {
        for (int i = 0; i < contents.length; i++) {
            if (contents[currentIndex < i ? contents.length - i + currentIndex : currentIndex - i] < l) {
                return i;
            }
        }

        // If every element is lower than l, return the array length
        return contents.length;
    }
}
