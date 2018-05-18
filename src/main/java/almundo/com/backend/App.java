package almundo.com.backend;

import almundo.com.backend.model.Director;
import almundo.com.backend.model.Employee;
import almundo.com.backend.model.Operator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Employee op1 = new Operator("Sabrina Dana");
    	Employee op2 = new Operator("Alicia Lavagnino");
    	Employee di1 = new Director("Damian Conde");
        op1.log();
        op2.log();
        di1.log();
    }
}
