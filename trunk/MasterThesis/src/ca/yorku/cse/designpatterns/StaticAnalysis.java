package ca.yorku.cse.designpatterns;

import java.io.*;

/**
* Handle static analysis
* 
* @author Marcel Birkner
* @version 0.1
* @since 10 October, 2007
*/
public class StaticAnalysis
{
    String cmd = "";
    String out = "";
    
    /**
     * Constructor
     * 
     * @param cmd Commandline argument that executes the static analysis using javex, grok and ql
     * @param output_file Filename that stores results of static analysis
     */
    StaticAnalysis(String cmd, String output_file )
    {
	this.cmd = cmd;
	this.out = output_file;
    }   
	
 
    /**
     * Execute static analysis
     */
    public void runStaticAnalysis() {    
	System.out.println("Run Static analysis: " + this.cmd );

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
        } 
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
}
