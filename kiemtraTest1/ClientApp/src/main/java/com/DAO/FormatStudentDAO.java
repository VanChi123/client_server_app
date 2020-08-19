package com.DAO;

import com.entities.FormatStudent;
import com.entities.Student;

public class FormatStudentDAO {
    private FormatStudent formatStudent ;
    public FormatStudentDAO(FormatStudent formatStudent){
        this.formatStudent = formatStudent;
    }
    public FormatStudentDAO(){
    }
    public boolean checkStudent(String date){
        boolean result = false;
        if(formatStudent.matches(date)){
            result=true;
        }else {
            result = false;
        }
        return result;
    }
}
