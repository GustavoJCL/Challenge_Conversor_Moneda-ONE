/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package challenge_conversor_moneda;

import static org.junit.jupiter.api.Assertions.*;

import com.conversor.CountryCurrencyAPI;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void apiGetResponse() {
        CountryCurrencyAPI classUnderTest = new CountryCurrencyAPI();
        try {
            System.out.println(classUnderTest.callAPI().toString());
            assertNotNull(classUnderTest.callAPI(), "app should have a greeting");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}