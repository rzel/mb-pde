/*******************************************************************************
 * 
 * $Author: Michaela $ $Date: 2004/08/02 08:39:24 $ $Revision: 1.2 $
 *  
 ******************************************************************************/

package jdomain.util;

import java.io.*;

public final class ConsoleLogListener implements LogListener {

    public ConsoleLogListener() {
    }

    //////////////////////////////////////////// Methods

    private void print(String message, PrintStream out) {
        out.println(message);
    }

    public void debug(String message) {
        print(message, System.out);
    }

    public void info(String message) {
        print(message, System.out);
    }

    public void warning(String message) {
        print("[warning] " + message, System.err);
    }

    public void warning(String message, Object o) {
        print("[warning] " + message, System.err);
        print("[" + o.toString() + "]", System.err);
    }

    public void error(String message) {
        print("[error] " + message, System.err);
    }

    public void exception(Throwable e) {
        print("[exception] " + e.getMessage(), System.err);
        e.printStackTrace(System.err);
    }

    public void close() {
        System.out.flush();
        System.err.flush();
    }

}