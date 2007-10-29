/*******************************************************************
 *
 *  $Author: Michaela $
 *  $Date: 2004/08/02 08:39:24 $
 *  $Revision: 1.2 $
 *
 *******************************************************************/

package jdomain.util;

public interface LogListener {

	//////////////////////////////////////////// Method Declarations

	public void debug(String message);
	public void info(String message);
	public void warning(String message);
	public void warning(String message, Object o);
	public void error(String message);
	public void exception(Throwable e);
	public void close();

}
