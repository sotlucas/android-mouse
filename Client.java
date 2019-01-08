import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        int port = 1342;
        String address = "xxx.xxx.x.xx"; // Change to SERVER IP

        // Connecting to server
        Socket socket = new Socket(address, port);

        // Getting user input
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Enter a number: ");
        int number = sc1.nextInt();

        // Sending info to server
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println(number);

        // Reading info from server
        Scanner sc2 = new Scanner(socket.getInputStream());
        int temp = sc2.nextInt();
        System.out.println(temp);

    }
}
