package com;

import com.dao.DataBaseService;
import com.entities.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class WorkerThread implements Runnable {

    public static final Logger LOG =
            LoggerFactory.getLogger(WorkerThread.class);
    private Socket socket;

    public WorkerThread(){

    }
    public WorkerThread(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while(true){
                    byte byteHeader=0;
                    try {
                         byteHeader = dataInputStream.readByte();

                    }catch (EOFException e){
                        socket.close();
                        break;
                    }
                    Connection con =null;
                    switch (byteHeader){
                        case 1:
                            Student st = new Student();
                            st = (Student) objectInputStream.readObject();
                            try {
                                con =  DataBaseService.getConnection();
                                int ketQua = DataBaseService.insertOneStudent(con,st);
                                if(1 == ketQua){
                                    dataOutputStream.writeUTF("Insert Thành Công");
                                    dataOutputStream.flush();
                                }else {
                                    dataOutputStream.writeUTF("Insert Không Thành Công!!!");
                                    dataOutputStream.flush();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                if(con!=null){
                                    try {
                                        con.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;
                        case 2:
                            List<Student> studentList = new ArrayList<Student>();
                            try{
                                con = DataBaseService.getConnection();
                                studentList = DataBaseService.getAllData(con);
                                con.close();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            objectOutputStream.writeObject(studentList);
                            dataOutputStream.flush();
                            break;
                        case 3:
                            try{
                                int id = dataInputStream.readInt();
                                Student student = new Student();
                                con = DataBaseService.getConnection();
                                student = DataBaseService.getOne(con,id);
                                objectOutputStream.writeObject(student);
                                objectOutputStream.flush();

                                Student student1 = (Student) objectInputStream.readObject();
                                int resultUpdate = DataBaseService.upDateOne(con,student1);
                                if(1 == resultUpdate){
                                    dataOutputStream.writeUTF("Update Thành Công");
                                    dataOutputStream.flush();
                                }else {
                                    dataOutputStream.writeUTF("Update Không Thành Công!!!");
                                    dataOutputStream.flush();
                                }
                                //System.out.println(student1.getId()+"-"+student1.getName()+"-"+student1.getPhoneNumber());
                                con.close();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            int idTimKiem = dataInputStream.readInt();
                            try{
                                con = DataBaseService.getConnection();
                                Student student = DataBaseService.getOne(con,idTimKiem);
                                if(null!=student){
                                    dataOutputStream.writeByte(1);
                                    objectOutputStream.writeObject(student);
                                    dataOutputStream.flush();
                                    objectOutputStream.flush();
                                }else {
                                    dataOutputStream.writeByte(2);
                                }
                                con.close();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 5:
                            String nameTimKiem = dataInputStream.readUTF();
                            System.out.println("chuỗi tìm kiếm là : "+nameTimKiem);
                            try{
                                con = DataBaseService.getConnection();
                                List<Student> listStudent = DataBaseService.searchByName(con,nameTimKiem);

                                if(listStudent.size()>0){
                                    dataOutputStream.writeByte(1);
                                    objectOutputStream.writeObject(listStudent);
                                    dataOutputStream.flush();
                                    objectOutputStream.flush();
                                }else {
                                    dataOutputStream.writeByte(2);
                                }
                                con.close();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 6:
                            int idDelete= dataInputStream.readInt();
                            try{
                                con = DataBaseService.getConnection();
                                int resultDelete = DataBaseService.deleteOne(con,idDelete);
                                String resultString;
                                if(1==resultDelete){
                                    resultString  = "Xóa thành công.";

                                }else {
                                    resultString ="Xóa không thành công";
                                }
                                dataOutputStream.writeUTF(resultString);
                                dataOutputStream.flush();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            //System.out.println("Chương trình kết thúc");
                    }

            }
        } catch (IOException e) {
            LOG.error("IOException of WorkerThread");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOG.error("ClassNotFound of WorkerThread");
                e.printStackTrace();
        }finally {
            if(null!=socket){
                try {
                    socket.close();
                } catch (IOException e) {
                    LOG.error("Lỗi đóng Socket of WorkerThread");
                    e.printStackTrace();
                }
            }
        }
    }
}