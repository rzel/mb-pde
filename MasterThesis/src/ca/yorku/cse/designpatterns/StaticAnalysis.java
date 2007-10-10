package ca.yorku.cse.designpatterns;

import java.io.*;

public class StaticAnalysis
{
    String cmd = "";
    String out = "";
    
    StaticAnalysis(String cmd, String output_file )
    {
	this.cmd = cmd;
	this.out = output_file;
    }   
	
//        if (args.length < 3)
//        {
//            System.out.println("USAGE: java GoodWinRedirect <output> <directory> <project_name> ");
//            System.exit(1);
//        }
       
// 	String sh = "./run.sh ";
//        String arg1 = args[1] + " ";               // "code ";
//        String arg2 = args[2] + " ";               // "ajp_code ";
//        String arg3 = "compile_ajp ";
//
//        String cmd = sh + arg1 + arg2;	
 
    public void runStaticAnalysis() {    
    
	System.out.println("Cmd: " + this.cmd );

        try
        {            
            FileOutputStream fos = new FileOutputStream( this.out );
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(this.cmd + "\n" );
            // any error message?
            StaticAnalysisStream errorGobbler = new 
            	StaticAnalysisStream(proc.getErrorStream(), "ERROR");            
            
            // any output?
            StaticAnalysisStream outputGobbler = new 
            	StaticAnalysisStream(proc.getInputStream(), "OUTPUT", fos);
                
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
