package tr.edu.sis.Controller;

import tr.edu.sis.Controller.FileManager;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import tr.edu.sis.Model.User;

public class FileManagerTest {

    public FileManagerTest() {
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
        FileManager instance = new FileManager();
        User result = instance.checkLogin(id, password);
        assertNull(result);
    }

    @Test
    public void testChangePassword() throws Exception {
        System.out.println("changePassword");
        int id = 0;
        String newPassword = "";
        FileManager instance = new FileManager();
        boolean expResult = false;
        boolean result = instance.changePassword(id, newPassword);
        assertEquals(expResult, result);
    }

    @Test
    public void testCourseRegistration() throws Exception {
        System.out.println("courseRegistration");
        int lecture_id = 0;
        int student_id = 0;
        FileManager instance = new FileManager();
        boolean expResult = true;
        boolean result = instance.courseRegistration(lecture_id, student_id);
        assertEquals(expResult, result);
    }

    @Test
    public void testListLectures() throws Exception {
        System.out.println("listLectures");
        int id = 0;
        FileManager instance = new FileManager();
        List result = instance.listLectures(id);
        assertNotNull(result);
    }

    @Test
    public void testListGrades() throws Exception {
        System.out.println("listGrades");
        int id = 0;
        FileManager instance = new FileManager();
        List result = instance.listGrades(id);
        assertNotNull(result);
    }

    @Test
    public void testAssignGrade() throws Exception {
        System.out.println("assignGrade");
        int lecture_id = 0;
        int student_id = 0;
        int type = 0;
        int grade = 0;
        FileManager instance = new FileManager();
        boolean expResult = true;
        boolean result = instance.assignGrade(lecture_id, student_id, type, grade);
        assertEquals(expResult, result);
    }

    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
        int id = 0;
        String name = "";
        String surname = "";
        String email = "";
        int position = 0;
        FileManager instance = Mockito.mock(FileManager.class);
        Mockito.when(instance.register(id, name, surname, email, position)).thenReturn(true);
        boolean expResult = true;
        boolean result = instance.register(id, name, surname, email, position);
        assertEquals(expResult, result);
        instance.deleteAccount(id);
    }

    @Test
    public void testDeleteAccount() throws Exception {
        System.out.println("deleteAccount");
        int id = 0;
        FileManager instance = new FileManager();
        boolean expResult = true;
        boolean result = instance.deleteAccount(id);
        assertEquals(expResult, result);
    }

    @Test
    public void testInsertLog() throws Exception {
        System.out.println("insertLog");
        String type = "TEST";
        String message = "";
        FileManager instance = new FileManager();
        boolean expResult = true;
        boolean result = instance.insertLog(type, message);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckLog() throws Exception {
        System.out.println("checkLog");
        FileManager instance = new FileManager();
        boolean expResult = true;
        boolean result = instance.checkLog();
        assertEquals(expResult, result);
    }

    @Test
    public void testSendMail() throws Exception {
        System.out.println("sendMail");
        int id = 0;
        String subject = "TEST";
        String password = "";
        FileManager instance = new FileManager();
        User result = instance.sendMail(id, subject, password);
        assertNull(result);
    }
}
