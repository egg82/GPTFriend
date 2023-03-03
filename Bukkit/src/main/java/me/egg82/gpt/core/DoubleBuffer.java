package me.egg82.gpt.core;

import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DoubleBuffer<T> {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private Queue<T> currentBuffer = new ConcurrentLinkedQueue<>();
    private Queue<T> backBuffer = new ConcurrentLinkedQueue<>();

    public @NotNull Queue<T> getReadBuffer() {
        lock.readLock().lock();
        try {
            return backBuffer;
        } finally {
            lock.readLock().unlock();
        }
    }

    public @NotNull Queue<T> getWriteBuffer() {
        lock.readLock().lock();
        try {
            return currentBuffer;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void swapBuffers() {
        lock.writeLock().lock();
        try {
            Queue<T> currentBuffer = this.currentBuffer;
            this.currentBuffer = this.backBuffer;
            this.backBuffer = currentBuffer;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
