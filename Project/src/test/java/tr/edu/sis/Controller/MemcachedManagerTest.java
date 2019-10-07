package tr.edu.sis.Controller;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import tr.edu.sis.Model.User;

public class MemcachedManagerTest {

    public MemcachedManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCheckLogin() throws Exception {
        System.out.println("checkLogin");
        int id = 0;
        String password = "";
        MemcachedManager instance = new MemcachedManager();
        User result = instance.checkLogin(id, password);
        assertNull(result);
    }

    @Test
    public void testChangePassword() throws Exception {
        System.out.println("changePassword");
        int id = 0;
        String newPassword = "";
        MemcachedManager instance = new MemcachedManager();
        boolean expResult = false;
        boolean result = instance.changePassword(id, newPassword);
        assertEquals(expResult, result);
    }

    @Test
    public void testListLectures() throws Exception {
        System.out.println("listLectures");
        int id = 0;
        MemcachedManager instance = new MemcachedManager();
        List result = instance.listLectures(id);
        assertNull(result);
    }

    @Test
    public void testListGrades() throws Exception {
        System.out.println("listGrades");
        int id = 0;
        MemcachedManager instance = new MemcachedManager();
        List result = instance.listGrades(id);
        assertNull(result);
    }

    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
        int id = 0;
        String name = "";
        String surname = "";
        String email = "";
        int position = 0;
        MemcachedManager instance = Mockito.mock(MemcachedManager.class);
        Mockito.when(instance.register(id, name, surname, email, position)).thenReturn(true);
        boolean expResult = true;
        boolean result = instance.register(id, name, surname, email, position);
        assertEquals(expResult, result);
    }

    @Test
    public void testDeleteAccount() throws Exception {
        System.out.println("deleteAccount");
        int id = 0;
        MemcachedManager instance = new MemcachedManager();
        boolean expResult = true;
        boolean result = instance.deleteAccount(id);
        assertEquals(expResult, result);
    }

    @Test
    public void testSendMail() throws Exception {
        System.out.println("sendMail");
        int id = 0;
        String subject = "TEST";
        String pasword = "";
        MemcachedManager instance = new MemcachedManager();
        User result = instance.sendMail(id, subject, pasword);
        assertNull(result);
    }

    @Test
    public void testInsertData() throws Exception {
        System.out.println("insertData");
        int key = 0;
        String type = "test";
        Object obj = new User();
        MemcachedManager instance = new MemcachedManager();
        boolean expResult = true;
        boolean result = instance.insertData(key, type, obj);
        assertEquals(expResult, result);
    }
}
