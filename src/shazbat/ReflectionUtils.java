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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

class ReflectionUtils {
    final static String PATH = System.getProperty("java.class.path");
    final static String PSEP = System.getProperty("path.separator");

    private ReflectionUtils() { }

    public static <Type> List<Class<Type>> findByType(Class<Type> clazz) throws Exception {
        final List<Class<Type>> found = new ArrayList<Class<Type>>();
        for (String e : PATH.split(PSEP)) {
            if (e.endsWith(".jar")) {
                final File jar = new File(e);
                final JarInputStream jis = new JarInputStream(new FileInputStream(jar));

                JarEntry je;
                while( (je = jis.getNextJarEntry()) != null) {
                    if(je.getName().endsWith(".class")) {
                        final String candidateClazzName = je.getName().replaceAll("/",".").replaceAll(".class","");
                        final Class<?> candidate = Class.forName(candidateClazzName);
                        if (!candidate.isInterface() && clazz.isAssignableFrom(candidate)) {
                            found.add((Class<Type>)candidate);
                        }
                    }
                }
            }
        }
        return found;
    }

}
