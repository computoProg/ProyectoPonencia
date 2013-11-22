/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testpruebas;

import junit.framework.TestCase;
import net.sourceforge.jwebunit.junit.WebTester;
/**
 *
 * @author Aux de Programación
 */
public class NewEmptyJUnitTest extends TestCase {
    
    private WebTester tester;
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
        tester = new WebTester();
        
        tester.getTestContext().setBaseUrl("http://localhost:8080/ponencias/");
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
   
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    public void testLogin(){
         tester.beginAt("/faces/login.jsp"); 
         //tester.clickButton("submit");
        //tester.clickLink("login");
        //tester.assertTitleEquals("Login");
       // tester.setTextField("username", "test");
         tester.clickButton("submit");
         tester.setTextField("username", "123");
        //tester.setTextField("password", "test123");
        tester.setTextField("password", "1230abcd");
       // tester.assertTitleEquals("Welcome, test!");
//        try{
//        tester.beginAt("/faces/login.jsp");
//        tester.assertButtonPresent("submit");
//        tester.assertFormElementPresent("cédula");
//        tester.setTextField("cédula", "123");
//        tester.assertFormElementPresent("contraseña");
//        tester.setTextField("contraseña", "1230abcd");
//        tester.clickButton("submit");}
//        catch(AssertionError e){
//
//        }
                //navegamos a la página principal
                //tester.assertTextPresent("Bienvenido, usuario");
    }
}
