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

import shazbat.Bogon;

public class TrivialDeadlock implements Bogon {
    private Object l1 = new Object();
    private Object l2 = new Object();

    public void run() {
        Thread t1 = new T1();
        Thread t2 = new T2();
        t1.start();
        t2.start();
        try { t1.join(); } catch(Exception ignore) { }
        try { t2.join(); } catch(Exception ignore) { }
    }

    class T1 extends Thread {
        public void run() {
            while(true) {
                pause();
                synchronized(l1) {
                    System.out.println("T1 acquired L1");
                    pause();
                    synchronized(l2) {
                        System.out.println("T1 acquired L2");
                        pause();
                    }
                    System.out.println("T1 released L2");
                    pause();
                }
                System.out.println("T1 released L1");
                pause();
            }
        }
    }

    class T2 extends Thread {
        public void run() {
            try { Thread.sleep(100); } catch(Exception ignore) { }
            while(true) {
                pause();
                synchronized(l2) {
                    System.out.println("T2 acquired L2");
                    pause();
                    synchronized(l1) {
                        System.out.println("T2 acquired L1");
                        pause();
                    }
                    System.out.println("T2 released L1");
                    pause();
                }
                System.out.println("T2 released L2");
                pause();
            }
        }
    }

    void pause() {
        long pauseNanos = (long)(Math.random() * 10000);
        long deadline = System.nanoTime() + pauseNanos;
        while(deadline >= System.nanoTime()) { Thread.yield(); };
    }
}
