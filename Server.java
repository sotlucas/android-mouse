import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.awt.MouseInfo;
import java.awt.Point;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8080;


        new Server().startServer(port);
    }

    private void startServer(int port) {
        Thread thread = new Thread(new Runnable() {
            private float[] data = null;
            private boolean done = false;
            MoveCursor mc = new MoveCursor();

            @Override
            public void run() {
                try {
                    // Starting server
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("Started server on port " + port + "...");

                    while (!done) {
                        // Read info from client
                        Socket socket = serverSocket.accept();
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        try {
                            data = (float[])input.readObject();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Process info
                        //System.out.println("Android: " + Arrays.toString(data));
                        Point pos = MouseInfo.getPointerInfo().getLocation();
                        mc.mouseGlide((int)pos.getX() + (int)data[0] * -1,
                                      (int)pos.getY() + (int)data[1] ,
                                      150, 150); // Move cursor
                        //mc.mouseMove(xi + (int)data[0] * -1, yi + (int)data[1]); // Move cursor
                        System.out.println("PC: " + data[0] + ", " + data[1]);

                        // Send info to the client
                        PrintWriter output = new PrintWriter(socket.getOutputStream());
                        String response = "Server: " + data;
                        output.println(response);
                        output.flush(); // Para que?

                        // Para que es esto??
                        /*
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        */

                        // Close on client command
                        /*
                        if (data.equalsIgnoreCase("Q")) {
                            done = true;
                            output.close();
                            socket.close();
                            break;
                        }
                        */
                        
                        output.close();
                        socket.close();
                    }
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
