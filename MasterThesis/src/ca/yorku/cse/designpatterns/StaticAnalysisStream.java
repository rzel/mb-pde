package ca.yorku.cse.designpatterns;

import java.io.*;

class StaticAnalysisStream extends Thread
{
    InputStream is;
    String type;
    OutputStream os;
    
    StaticAnalysisStream(InputStream is, String type)
    {
        this(is, type, null);
    }
    
    StaticAnalysisStream(InputStream is, String type, OutputStream redirect)
    {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }
    
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
