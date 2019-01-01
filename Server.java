import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        int portNumber = 1342;

        // Starting server
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Started server on port " + portNumber + "...");

        // Read info from client
        Socket socket = serverSocket.accept();
        Scanner sc = new Scanner(socket.getInputStream());
        int number = sc.nextInt();
        
        // Process info
        int temp = number * 2;

        // Send info to the client
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println(temp);
    }
}
