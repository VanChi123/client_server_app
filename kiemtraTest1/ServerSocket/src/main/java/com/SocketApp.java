package com;

import com.dao.DataBaseService;
import com.entities.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketApp {
    private final static int PORT = 45678;
    private final static int NUM_OF_THREAD = 5;
    public static final Logger LOG =
            LoggerFactory.getLogger(SocketApp.class);
    public static void main(String[] args) throws IOException, SQLException {

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREAD);
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            while(true){
                System.out.println("Wait a client.....");
                Socket socket = null;
                try{
                    socket = server.accept();
                }catch (SocketTimeoutException socketTimeoutException){
                    LOG.warn("Socket timeout Exception");
                    socketTimeoutException.printStackTrace();
                }
                System.out.println("Server accepted!" + socket.getRemoteSocketAddress());
                WorkerThread w = new WorkerThread(socket);
                executorService.execute(w);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(server != null)
                server.close();
        }
        System.out.println("Server Died");

   }

}
