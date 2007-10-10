package ca.yorku.cse.designpatterns;

import java.io.*;

/**
* Handle input and output of static analysis
* 
* @author Marcel Birkner
* @version 0.1
* @since 10 October, 2007
*/
class StaticAnalysisStream extends Thread
{
    InputStream is;
    String type;
    OutputStream os;
    
    /**
     * Constructor
     * 
     * @param is
     * @param type
     */
    StaticAnalysisStream(InputStream is, String type)
    {
        this(is, type, null);
    }
    
    /**
     * Constructor
     * 
     * @param is
     * @param type
     * @param redirect
     */
    StaticAnalysisStream(InputStream is, String type, OutputStream redirect)
    {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }
    
    /**
     * Handle input/output from command line
     */
    public void run()
    {
        try
        {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);
                
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            {
                if (pw != null)
                    pw.println(line);
                System.out.println(type + ">" + line);    
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe)
            {
            ioe.printStackTrace();  
            }
    }
}
