package com.ullink.rx;

import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * This Scheduler purpose is to be used in integration test. it differs from rxjava's TestScheduler by the fact it is threadsafe and so
 * can be accessed by multiple threads, which is convenient when it is embedded into a live running application.
 */
public class RxDemoScheduler extends Scheduler
{

    private TestScheduler delegate;

    public RxDemoScheduler() {
        delegate = Schedulers.test();
        substituteQueues();
    }

    @Override
    public Worker createWorker()
    {
        return delegate.createWorker();
    }

    private void substituteQueues()
    {
        try
        {
            Field field = delegate.getClass().getDeclaredField("queue");
            field.setAccessible(true);
            PriorityQueue<Object> originalQueue = (PriorityQueue<Object>)field.get(delegate);
            Comparator<Object> comparator = originalQueue.comparator();
            field.set(delegate, new PriorityBlockingQueue<>(11,comparator));
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public long now()
    {
        return delegate.now();
    }

    /**
     * Moves the Scheduler's clock forward by a specified amount of time.
     *
     * @param delayTime
     *          the amount of time to move the Scheduler's clock forward
     * @param unit
     *          the units of time that {@code delayTime} is expressed in
     */
    public void advanceTimeBy(long delayTime, TimeUnit unit)
    {
        delegate.advanceTimeBy(delayTime,unit);
    }

    /**
     * Moves the Scheduler's clock to a particular moment in time.
     *
     * @param delayTime
     *          the point in time to move the Scheduler's clock to
     * @param unit
     *          the units of time that {@code delayTime} is expressed in
     */
    public void advanceTimeTo(long delayTime, TimeUnit unit)
    {
        delegate.advanceTimeTo(delayTime,unit);
    }
}
