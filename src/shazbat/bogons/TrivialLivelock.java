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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import shazbat.Bogon;

public class TrivialLivelock implements Bogon {
    public void run() {
        final Subject evilBill = new Subject("EvilBill");
        final Subject evilTed = new Subject("EvilTed");
        final TxResource knife = new TxResource("Knife",evilTed);

        final Thread billKillTed = new Thread(new Runnable() {
            public void run() {
                evilBill.use(knife,evilTed);
            }
        });

        final Thread tedKillBill = new Thread(new Runnable() {
            public void run() {
                evilTed.use(knife,evilBill);
            }
        });

        billKillTed.start();
        tedKillBill.start();

        try { billKillTed.join(); } catch(Exception ignore) { }
        try { tedKillBill.join(); } catch(Exception ignore) { }
    }

    class TxResource {
        private final String name;
        private Subject owner;
        public TxResource(String name, Subject owner) {
            this.name = name;
            this.owner = owner;
        }
        public Subject owner() {
            return owner;
        }
        public synchronized void owner(Subject o) {
            this.owner = o;
        }
        public synchronized void use() {
            System.out.printf("%s has used", owner.name);
        }
    }

    class Subject {
        final String name;
        boolean isHurt;

        public Subject(final String name) {
            this.name= name;
        }

        public boolean isHurt() {
            return isHurt;
        }

        public void use(TxResource weapon, Subject target) {
            while (!isHurt) {
                if (weapon.owner() != this) {
                    System.out.printf("%s: dodge!\n", name);
                    pause();
                    continue;
                }

                if (!target.isHurt()) {
                    System.out.printf("%s: fumble!\n", name);
                    weapon.owner(target); // Aagh, snot!
                    continue;
                }

                // target is hurt, can't bear to live, sepaku!
                System.out.printf("%s: sepaku!\n", name);
                weapon.use();
                isHurt = true;
                weapon.owner(target);
            }
        }
    }

    void pause() {
        long pauseNanos = (long)(Math.random() * 1000);
        long deadline = System.nanoTime() + pauseNanos;
        while(deadline >= System.nanoTime()) { Thread.yield(); };
    }
}
