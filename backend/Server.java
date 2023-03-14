import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        System.out.println("Server is running...");

        try{
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket workerSocket = serverSocket.accept();

            System.out.println("[Server] connected to " + workerSocket.getInetAddress().getHostAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(workerSocket.getInputStream()));
            PrintWriter out = new PrintWriter(workerSocket.getOutputStream(), true);

            String inputLine;
            while(null != (inputLine = in.readLine())){
                System.out.println("[Server] received: " + inputLine);
                inputLine = inputLine.toUpperCase();
                System.out.println("[Server] sending back: " + inputLine);
                out.println(inputLine);
            }
            System.out.printf("[Server] closing connection to %s%n", workerSocket.getInetAddress().getHostAddress());

        }catch(IOException e){
            System.err.println("[Server] error: ");
            e.printStackTrace();
        }
    }
}
