package com.gyb.questionnaire.config;

import org.springframework.util.ClassUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂
 *
 * @author geng
 * 2020/12/15
 */
public class CustomThreadFactory implements ThreadFactory {
    private String threadNamePrefix;
    private int threadPriority = Thread.NORM_PRIORITY;
    private boolean daemon = false;
    private ThreadGroup threadGroup;

    private final AtomicInteger threadCount = new AtomicInteger(0);

    public CustomThreadFactory() {
        this.threadNamePrefix = this.getDefaultThreadNamePrefix();
    }

    public CustomThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix != null ? threadNamePrefix : this.getDefaultThreadNamePrefix();
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix != null ? threadNamePrefix : this.getDefaultThreadNamePrefix();
    }

    public String getThreadNamePrefix() {
        return this.threadNamePrefix;
    }

    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public int getThreadPriority() {
        return this.threadPriority;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean isDaemon() {
        return this.daemon;
    }

    public void setThreadGroupName(String name) {
        this.threadGroup = new ThreadGroup(name);
    }

    public void setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
    }

    public ThreadGroup getThreadGroup() {
        return this.threadGroup;
    }

    protected Thread createThread(Runnable runnable) {
        Thread thread = new Thread(this.getThreadGroup(), runnable, this.nextThreadName());
        thread.setPriority(this.getThreadPriority());
        thread.setDaemon(this.isDaemon());
        return thread;
    }

    @Override
    public Thread newThread(Runnable r) {
        return createThread(r);
    }

    protected String nextThreadName() {
        return this.getThreadNamePrefix() + this.threadCount.incrementAndGet();
    }

    protected String getDefaultThreadNamePrefix() {
        return ClassUtils.getShortName(this.getClass()) + "-";
    }
}
