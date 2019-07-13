package chatter;

import chatter.messaging.model.CommunicationModel;
import chatter.messaging.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import static java.lang.System.out;

public class Client {

    private static Scanner scanner = new Scanner(System.in);
    private static long userId;

    public static void main(String[] args) throws IOException {

        Socket socket = defineClient();

        new Thread(() -> {
            try {
                while (true) {
                    ObjectInputStream outputStream = new ObjectInputStream(socket.getInputStream());
                    out.println(outputStream.readObject());
                }
            } catch (IOException | ClassNotFoundException ignored) {
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

                    out.print("Message Text: ");

                    String message = scanner.next();

                    CommunicationModel communicationModel = new CommunicationModel();
                    communicationModel.setMessage(message);
                    communicationModel.setSenderUser(userId);

                    out.print("Send for user: ");
                    String userIds = scanner.next();
                    communicationModel.setUserIds(getUserId(userIds.split(",")));

                    outputStream.writeObject(communicationModel);
                }
            } catch (IOException ignored) {
            }
        }).start();

    }

    private static List<Long> getUserId(String[] split) {
        List<Long> list = new ArrayList<>();

        Arrays.stream(split).forEach(i -> list.add(Long.parseLong(i)));

        return list;
    }

    private static Socket defineClient() throws IOException {
        out.println("Define client");
        userId = scanner.nextLong();
        return createClient(userId);
    }

    private static Socket createClient(long id) throws IOException {
        Socket socket;
        out.println("Define port");
        int port = scanner.nextInt();
        socket = new Socket("localhost", port);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(createUser(id));
        return socket;
    }


    private static User createUser(long id) {
        User user = new User();
        user.setSurname("SS");
        user.setUsername("CC");
        user.setId(id);
        return user;
    }

}
