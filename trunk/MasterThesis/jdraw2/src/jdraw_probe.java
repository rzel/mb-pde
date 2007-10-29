// generated source from Probekit compiler
/* probekit \jdraw\src\jdraw.probe
*/
// "imports" specifications for probes (if any):
import java.io.*; // from unnamed_probe
class jdraw_probe {
  // Class for probe unnamed_probe
  public static class Probe_0 {
    // Fragment at class scope
static public String outputFilename = "";
static public int callDepth = 0;

    public static void _exit (
      String /*className*/ className,
      String /*methodName*/ methodName,
      Object /*thisObject*/ thisObject      ) {
      // Internal signature for this method: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)V
//------------------ begin user-written fragment code ----------------
callDepth = callDepth - 1;

if( methodName.contains("<init>") || methodName.contains("<clinit>")  )
	methodName = "Constructor";
	
String output = "<exit"
	+ " className=\"" + className 
	+ "\" methodName=\"" + methodName 
	+ "\" thisObject=\"" + className + "@" + System.identityHashCode(thisObject)
	+ "\" calledByClass=\"\" calledByMethod=\"\" calledByObject=\"\" "
	+ " callDepth=\"" + callDepth + "\" ></exit></entry>";	
	
try {
	BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename,true));
        out.write( output );
        out.newLine();
        out.close();
} catch (IOException e) {
	System.out.println("PROBEKIT: Exception in BufferedWriter, exit");
}

//------------------- end user-written fragment code -----------------
    }
    public static void _entry (
      String /*className*/ className,
      String /*methodName*/ methodName,
      Object /*thisObject*/ thisObject,
      Object[] /*args*/ args      ) {
      // Internal signature for this method: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)V
//------------------ begin user-written fragment code ----------------
if( methodName.contains("<init>") || methodName.contains("<clinit>")  )
	methodName = "Constructor";

callDepth = callDepth + 1;
String arguments = "";

for (int i=0; i<args.length;i++){
	// only take those arguments that are Objects; throw out Strings that might mess up the XML file
 	if(args[i] != null && args[i].toString().contains("@")) {
		arguments = arguments + "|" + args[i];
	}
}

String output = "<entry args=\"" + arguments 
	+ "\" className=\"" + className 
	+ "\" methodName=\"" + methodName 
	+ "\" thisObject=\"\" "
	+ " calledByClass=\"\" "
	+ " calledByMethod=\"\" "
	+ " calledByObject=\"\" "
	+ " callDepth=\"" + callDepth + "\" >";
	

if ( outputFilename.equals("") ) {
	outputFilename = className.replaceAll("/",".");
	outputFilename = "dynamicfacts/" + outputFilename + ".dynamicfacts" ;
}
	
try {
	BufferedWriter out = new BufferedWriter(new FileWriter(outputFilename, true));
        out.write( output );
        out.newLine();
        out.close();
} catch (IOException e) {
	System.out.println("PROBEKIT: Exception in BufferedWriter, entry");
}

//------------------- end user-written fragment code -----------------
    }
  }
}
