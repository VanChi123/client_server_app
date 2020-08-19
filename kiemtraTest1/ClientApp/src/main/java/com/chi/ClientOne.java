package com.chi;



import com.entities.FormatStudent;
import com.entities.Student;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientOne {
    private final static String SERVER_ADDRESS = "127.0.0.1";
    private final static int PORT = 45678;

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                int luaChon;
                System.out.println("1.-------Thêm Student");
                System.out.println("2.-------Xem Danh Sách Student");
                System.out.println("3.-------Sửa Student");
                System.out.println("4.-------Tìm Kiếm Student");
                System.out.println("5.-------Xóa Student");
                System.out.println("6.Thoát");
                System.out.println("Mời bạn nhập giá trị");
                luaChon = scanner.nextInt();
                scanner.nextLine();
                switch (luaChon) {
                    case 1:
                        String id,name,dateOfBirth;
                        System.out.print("Mời bạn nhập id :");
                        id= scanner.next();
                        scanner.nextLine();
                        System.out.print("Mời bạn nhập name :");
                        name = scanner.nextLine();
                        while (true){
                            System.out.print("Mời bạn nhập ngày sinh ( dạng YYYY-MM-DD): ");
                            dateOfBirth= scanner.next();
                            scanner.nextLine();
                            FormatStudent formatStudent = new FormatStudent();
                            boolean kq = formatStudent.matches(dateOfBirth);
                            if(true==kq){
                                System.out.println("Định dạng ngày sinh đúng");
                                break;
                            }else {
                                System.out.println("Bạn nhập sai định dạng !!!");
                            }
                        }
                        int idStudent = Integer.parseInt(id);
                        Student student = new Student(idStudent,name,dateOfBirth);

                        System.out.println("------------------------");

                        dataOutputStream.writeByte(1);
                        objectOutputStream.writeObject(student);
                        objectOutputStream.flush();

                        String result = dataInputStream.readUTF();
                        System.out.println(result);

                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        break;
                    case 2:
                        dataOutputStream.writeByte(2);
                        dataOutputStream.flush();

                        List<Student> studentList = new ArrayList<Student>();
                        studentList = (List<Student>) objectInputStream.readObject();
                        for(Student st:studentList){
                            System.out.println(st.getId()+"\t ----\t"+st.getName()+"\t   ----\t"+st.getDateOfBirth());
                        }
                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        break;
                    case 3:
                        String newName;
                        String newDateOfBirth;
                        String idstudent;
                        int studentId ;
                        System.out.println("Nhập vào id của Student muốn sửa: ");
                        idstudent = scanner.next();
                        scanner.nextLine();
                        studentId = Integer.parseInt(idstudent);

                        dataOutputStream.writeByte(3);
                        dataOutputStream.writeInt(studentId);
                        dataOutputStream.flush();

                        Student student1 = new Student();
                        student1 = (Student) objectInputStream.readObject();

                        System.out.println(student1.getId()+"\t ----\t"+student1.getName()+"\t   ----\t"+student1.getDateOfBirth());
                        System.out.println("Nhập vào tên muốn sửa: ");

                        newName = scanner.nextLine();
                        System.out.println("Nhập vào ngày sinh: ");
                        newDateOfBirth = scanner.next();
                        scanner.nextLine();

                        Student student2 = new Student(studentId,newName,newDateOfBirth);
                        objectOutputStream.writeObject(student2);

                        String resultUpdate  = dataInputStream.readUTF();
                        System.out.println(resultUpdate);

                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        break;
                    case 4:
                        String loaiTimKiem;
                        while (true){
                            System.out.println("1.***************Tìm kiếm theo id********************");
                            System.out.println("2.+++++++++++Tìm kiếm theo tên+++++++++");
                            System.out.print("Bạn muốn tìm kiếm Student theo id hay tên: ");
                            loaiTimKiem = scanner.next();
                            scanner.nextLine();
                            if ("1".equals(loaiTimKiem)) {
                                String idStudentSearch;
                                System.out.print("Mời bạn nhập ID: ");
                                idStudentSearch = scanner.next();
                                scanner.nextLine();
                                int idSearch = Integer.parseInt(idStudentSearch);

                                dataOutputStream.writeByte(4);
                                dataOutputStream.writeInt(idSearch);
                                dataOutputStream.flush();

                                byte kq = dataInputStream.readByte();
                                if(kq==1){
                                    Student st = (Student) objectInputStream.readObject();
                                    System.out.println("Kết quả tìm kiếm");
                                    System.out.println(st);
                                }else {
                                    System.out.println("Không tìm thấy kết quả");
                                }


                            } else if ("2".equals(loaiTimKiem)) {
                                String phoneStudentSearch;
                                System.out.print("Mời bạn nhập tên: ");
                                phoneStudentSearch = scanner.nextLine();

                                dataOutputStream.writeByte(5);
                                dataOutputStream.writeUTF(phoneStudentSearch);
                                dataOutputStream.flush();

                                byte kq = dataInputStream.readByte();
                                if(kq==1){
                                    List<Student> list = (List<Student>) objectInputStream.readObject();
                                    System.out.println("Kết quả tìm kiếm");
                                    for(Student student3:list){
                                        System.out.println(student3);
                                    }
                                }else {
                                    System.out.println("Không tìm thấy kết quả");
                                }
                            }else {
                                int luaChonThoat;
                                System.out.println("Bạn đã nhập sai!!!");
                                System.out.print("Bạn có muốn thoát: ?1.Có    2.Không:        ");
                                luaChon = scanner.nextInt();
                                scanner.nextLine();

                                if (1 == luaChon) {
                                    break;
                                }
                            }
                        }
                        break;
                    case 5:
                        String idStudentDelete;
                        int idStDelete;
                        System.out.print("Mời bạn nhập id muốn xóa: ");
                        idStudentDelete = scanner.next();
                        scanner.nextLine();

                        idStDelete = Integer.parseInt(idStudentDelete);

                        dataOutputStream.writeByte(6);
                        dataOutputStream.writeInt(idStDelete);
                        dataOutputStream.flush();

                        String resultDelet = dataInputStream.readUTF();
                        System.out.println(resultDelet);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Số bạn nhập không đúng");
                }
                System.out.println("Hoàn tất tác vụ");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
            }
        }
    }
}

