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

public class BurnCpu implements Bogon {
    public void run() {
        int np = Runtime.getRuntime().availableProcessors();
        Thread[] ts = new Thread[np];
        for(int i = 0; i < np; i++) {
            ts[i] = new Thread(new Burn());
            ts[i].start();
        }

        try { ts[0].join(); } catch(Exception ignore) { }
    }
}

class Burn implements Runnable {
    public void run() {
        int count = 0;
        while(true) { 
            count = ((count + 1) % 1_000_000_000);
            if (count == 1_000_000_666) break;
        }
        System.out.println(count);
    }
}
