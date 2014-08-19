/* **************************************************************************
 *
 * Copyright (c) 2014. Darach Ennis < darach at gmail dot com >.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * *************************************************************************/

package shazbat.bogons;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import shazbat.Bogon;

public class TrivialStarvation implements Bogon {
    // The total (finite) number of resources
    private static final int NR = 5;
    // The number of available processors on which to schedule resource tasks
    private static final int NP = Runtime.getRuntime().availableProcessors();

    private final ExecutorService executors = Executors.newFixedThreadPool(NP);
    final ResourceStorage locker = new ResourceStorage();
    final Task task = new Task();

    public void run() {
        for(;;) {
            executors.execute(task);
        }
    }

    class Task implements Runnable {
        public void run() {
            long token = -1;
            int resourceId = ThreadLocalRandom.current().nextInt(0,NR);
            try {
                token = locker.lock(resourceId);
                pause();
            } catch(Exception ignore) {
            } finally {
                locker.unlock(resourceId, token);
                Thread.yield();
            }
        }
    }

    class ResourceStorage {
        private final ReentrantLock lock = new ReentrantLock(true);
        private final Condition condition = lock.newCondition();
        private final Map<Integer, Long> resourceMap = new HashMap<Integer, Long>();

        public long lock(int id) {
            lock.lock();
            long tid = Thread.currentThread().getId();
            try {
                while (resourceMap.get(id) != null) {
                    resourceMap.get(id);
                    try {
                        System.out.printf("Thread %s is waiting on resource %s.\n", tid, id);
                        condition.await();
                    } catch (InterruptedException phooey) {
                        return -1;
                    }
                }

                Long prior = resourceMap.put(id, tid);
                if (prior != null) {
                    // should never overwrite a prior lock
                    throw new IllegalStateException();
                }
                System.out.printf("Thread %s has the lock on resource %s.\n", tid, id);
            }
            finally {
                lock.unlock();
            }
            return tid;
        }

        public void unlock(int id, long token) {
            try {
                lock.lock();
                if (resourceMap.get(id) == token) {
                    resourceMap.remove(id);
                } else {
                    // cannot unlock unheld lock
                    throw new IllegalStateException();
                }
                long tid = Thread.currentThread().getId();
                System.out.printf("Thread %s no longer has the lock on resource %s.\n", tid, id);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    void pause() {
        long pauseNanos = (long)(Math.random() * 1000);
        long deadline = System.nanoTime() + pauseNanos;
        while(deadline >= System.nanoTime()) { Thread.yield(); };
    }
}
