package com.dao;

import com.entities.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseService {
    public static final String DB_URL ="jdbc:mysql://localhost:3306/testjdbc";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "admin1234";
    public static final Logger LOG =
            LoggerFactory.getLogger(DataBaseService.class);

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
    }
    public static void createTable(Connection con) throws SQLException {
        Statement st = null;
        String sql = "create table Student(  id INT NOT NULL AUTO_INCREMENT, name nvarchar(50),dateofbirth VARCHAR(10),PRIMARY KEY (id) )";
        try{
            st = con.createStatement();
            boolean resultCreateTable = st.execute(sql);
        }catch (Exception ex){
            LOG.error("Lỗi tạo bảng");
            ex.printStackTrace();
        }finally {
            if(st!=null){
                st.close();
            }
        }

    }
    public  static int insertOneStudent(Connection con,Student student) throws SQLException {
        PreparedStatement pr = null;
        final String INSERT_COMMAND = "insert into Student(id,name,dateofbirth) values(?,?,?)";
        int result = 0;
        try{
            pr = con.prepareStatement(INSERT_COMMAND);
            pr.setInt(1,student.getId());
            pr.setString(2,student.getName());
            pr.setString(3,student.getDateOfBirth());
            result = pr.executeUpdate();
            LOG.info("Thêm đối tượng thành công");
        } catch (SQLException e) {
            LOG.error("Thêm không thành công ,id="+student.getId());
            //e.printStackTrace();
            return 0;
        }finally {
            if(pr!=null){
                pr.close();
            }
        }
        return result;
    }

    public static List<Student> getAllData(Connection con) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Student> studentList = new ArrayList<Student>();

        try{
            statement = con.createStatement();
            resultSet = statement.executeQuery("select * from student");
            while(resultSet.next()){
                int value = resultSet.getInt(1);
                String value1 = resultSet.getString(2);
                String value2 = resultSet.getString(3);
                Student st = new Student(value,value1,value2);
                studentList.add(st);
            }

        }catch (Exception ex){
            LOG.error("Lỗi lấy danh sách student!!");
            ex.printStackTrace();
        }finally {
            if(resultSet!=null){
                resultSet.close();
            }
            if(statement!=null){
                statement.close();
            }
        }
        return studentList;
    }

    public static Student getOne(Connection con,int id) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Student st=null ;
        try{
            pst = con.prepareStatement("select * from student where id = ?");
            pst.setInt(1,id);
            rs = pst.executeQuery();
            while(rs.next()){
                int value = rs.getInt(1);
                String value1 = rs.getString(2);
                String value2 = rs.getString(3);
                st = new Student(value,value1,value2);

            }

        }catch (Exception ex){
            LOG.error("Không lấy được Student");
            ex.printStackTrace();
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(pst!=null){
                pst.close();
            }
        }
        return st;

    }
    public static List<Student> searchByName(Connection con,String name) throws SQLException {
        List<Student> list = new ArrayList<Student>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Student st=null ;
        try{
            pst = con.prepareStatement("select * from student where name = ? ");
            pst.setString(1,name);
            rs = pst.executeQuery();
            while(rs.next()){
                int value = rs.getInt(1);
                String value1 = rs.getString(2);
                String value2 = rs.getString(3);
                st = new Student(value,value1,value2);
                System.out.println(st);
                list.add(st);
            }
        }catch (Exception ex){
            LOG.error("Lỗi tìm kiếm");
            ex.printStackTrace();
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(pst!=null){
                pst.close();
            }
        }
        return list;

    }
    public static int upDateOne(Connection con,Student student) throws SQLException {
        PreparedStatement st = null;
        int kq = 0;
        try{
            st = con.prepareStatement("update student set name = ? ,dateofbirth = ? WHERE id=? ");
            st.setString(1,student.getName());
            st.setString(2,student.getDateOfBirth());
            st.setInt(3,student.getId());
            kq = st.executeUpdate();
        }catch (Exception ex){
            System.out.println("Không cập nhật được");
            ex.printStackTrace();
        }finally {
            if(st!=null){
                st.close();
            }
        }
        return kq;

    }
    public static int deleteOne(Connection con,int id) throws SQLException {
        PreparedStatement st = null;
        int kq = 0;
        try{
            st = con.prepareStatement("delete from student where id = ?");

            st.setInt(1,id);
            kq = st.executeUpdate();
            System.out.println("Số dòng áp dụng : "+kq);
        }catch (Exception ex){
            System.out.println("Không xóa student thành công được"+id);
            ex.printStackTrace();
        }finally {
            if(st!=null){
                st.close();
            }
        }
        return kq;
    }

}
