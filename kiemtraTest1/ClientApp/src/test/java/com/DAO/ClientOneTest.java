package com.DAO;
import com.DAO.FormatStudentDAO;
import com.chi.ClientOne;
import com.entities.FormatStudent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
public class ClientOneTest {
    //@Test
    public void testStudentDate(){

        FormatStudent formatStudent = Mockito.mock(FormatStudent.class);

        FormatStudentDAO formatStudentDAO =new FormatStudentDAO(formatStudent);

        String date = "1998-12-05";
        when(formatStudent.matches(date)).thenReturn(true);

        boolean kq = formatStudentDAO.checkStudent(date);
        Assertions.assertEquals("true",kq);
    }
}
