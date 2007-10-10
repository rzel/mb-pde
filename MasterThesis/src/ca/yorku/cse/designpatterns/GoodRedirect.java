import java.io.*;

public class GoodRedirect
{
    public static void main(String args[])
    {
        if (args.length < 3)
        {
            System.out.println("USAGE: java GoodWinRedirect <output> <directory> <project_name> ");
            System.exit(1);
        }
       
 	String sh = "./run.sh ";
        String arg1 = args[1] + " ";               // "code ";
        String arg2 = args[2] + " ";               // "ajp_code ";
        String arg3 = "compile_ajp ";

        String cmd = sh + arg1 + arg2;	
 
	System.out.println("Cmd: " + cmd );

        try
        {            
            FileOutputStream fos = new FileOutputStream(args[0]);
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd + "\n" );
            // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "ERROR");            
            
            // any output?
            StreamGobbler outputGobbler = new 
                StreamGobbler(proc.getInputStream(), "OUTPUT", fos);
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                    
            // any error???
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);
            fos.flush();
            fos.close();        
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
    }
}
