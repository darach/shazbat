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

package shazbat;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class NanuNanu {
    private static final Map<String,Runnable> bogons = new HashMap<String,Runnable>();
    
    static {
        List<Class<Bogon>> ohnoes = null;
        try {
            ohnoes = ReflectionUtils.findByType(Bogon.class);
        } catch(Exception e) {
            e.printStackTrace();
        }

        for(Class<Bogon> bogon : ohnoes) {
            try {
                bogons.put(bogon.getName().toUpperCase(),bogon.newInstance());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        if (args.length != 1 || !bogons.containsKey(args[0])) {
            usageThenExit();
        }

        bogons.get(args[0].toUpperCase()).run();

        // Some kind of long-lived (preferably infinite) concurrency issue should
        // result above. The following seems right, although probably useless
        //
        System.exit(0);
    }

    public static void usageThenExit() {
        final String cn = NanuNanu.class.getName();
        System.out.printf("usage:%n%n");
        System.out.printf("    java -cp /path/to/shazbat.jar %s <name> %n", cn);
        System.out.printf("    java -jar shazbat.jar <name> %n");
        System.out.printf("    java -jar shazbat.jar <name> %n");
        System.out.println();
        System.out.printf("  where <name> is one of:%n%n");
        for (String bogon : bogons.keySet()) {
            System.out.printf("    %s\n", bogon);
        }
        System.out.println();
        System.out.printf("  then good hunting!%n");
        System.exit(1);
    }

}
