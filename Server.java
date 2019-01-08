import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.MouseInfo;
import java.awt.Point;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        String ip = null;

        try (final DatagramSocket s = new DatagramSocket()) {
            s.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = s.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Server().startServer(port, ip);
    }

    private void startServer(int port, String ip) {
        Thread thread = new Thread(new Runnable() {
            private Object incoming;
            private boolean done = false;
            MoveCursor mc = new MoveCursor();

            @Override
            public void run() {
                try {
                    // Starting server
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("Started server...");
                    System.out.println("IP: " + ip);
                    System.out.println("PORT: " + port);

                    while (!done) {
                        // Read info from client
                        Socket socket = serverSocket.accept();
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                        try {
                            incoming = input.readObject();
                            if (float[].class.isInstance(incoming)) {
                                float[] data = (float[])incoming;
                                mouseMovementHandler(mc, data);
                            } else if (String.class.isInstance(incoming)) {
                                String data = (String)incoming;
                                mouseActionHandler(mc, data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Send info to the client
                        PrintWriter output = new PrintWriter(socket.getOutputStream());
                        String response = "Server: " + "hi";
                        output.println(response);
                        output.flush();

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

    private void mouseMovementHandler(MoveCursor mc, float[] data) {
        Point pos = MouseInfo.getPointerInfo().getLocation();
        mc.mouseGlide((int)pos.getX() + (int)data[0] * -1 * 5,
                      (int)pos.getY() + (int)data[1] * 5,
                      50, 10); // Move cursor
        System.out.println("PC: " + data[0] + ", " + data[1]);
    }

    private void mouseActionHandler(MoveCursor mc, String action) {
        mc.mouseClick(action);
    }
}
