package com.junittest;

import com.dao.DataBaseService;
import com.entities.Student;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServiceTest {
    private Connection con;
    private  DataBaseService dataBaseService;
//    @BeforeAll
//    public static void  initAll(
//    }
    @BeforeEach
    public void init() throws SQLException {
        System.out.println("init Connection Test");
        con = DataBaseService.getConnection();
    }
    //@Test
    public void testConnection() throws SQLException {

        Connection conTest = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc","root","admin1234");
        Assertions.assertEquals(conTest,con);
    }
    //@Test
    @DisplayName("Test get One Student")
    public void getOneTest() throws SQLException {
        Assertions.assertEquals("chi",DataBaseService.getOne(con,2).getName());

    }
}
