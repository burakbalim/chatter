package chat;

import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.System.out;

public class Client {

    public static void main(String[] args) {
        Socket socket = client1();
        new Thread(() -> {
            try {
                while (true) {
                    ObjectInputStream outputStream = new ObjectInputStream(socket.getInputStream());
                    out.println(outputStream.readObject());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
                    scanner.nextLine();
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    CommunicationModel communicationModel = new CommunicationModel();
                    communicationModel.setMessage("Hello World");
                    communicationModel.setSenderUser(2);
                    communicationModel.setUsers(Collections.singletonList(1));
                    outputStream.writeObject(communicationModel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();


    }

    private static Socket client1() {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        out.println("Define client");
        return createClient(scanner.nextInt(), "CC", "SS");
    }


    private static Socket createClient(long id, String username, String surname) {
        try {
            Socket socket = new Socket("localhost", 2001);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(createUser(id, username, surname));
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static User createUser(long id, String username, String surname) {
        User user = new User();
        user.setSurname(surname);
        user.setUsername(username);
        user.setId(id);
        return user;
    }

}
