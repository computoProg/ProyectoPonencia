/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testpruebaShale;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.bcel.generic.Select;
import org.apache.shale.test.base.AbstractViewControllerTestCase;

/**
 *
 * @author Aux de Programación
 */
public class NewEmptyJUnitTest extends AbstractViewControllerTestCase {
    
    // The instance to be tested
Select vc = null;
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
         // Configure the supported locales for this application
    List list = new ArrayList();
    list.add(new Locale("en"));
    list.add(new Locale("fr"));
    list.add(new Locale("de"));
    list.add(new Locale("es"));
    application.setSupportedLocales(list);

    // Construct a new ViewController instance
    vc = new Select();
    }
    
    @Override
    protected void tearDown() throws Exception {
         vc = null;
        super.tearDown();
    }
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
}
