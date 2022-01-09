import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;
    Scanner in;
    PrintStream out;
    Server server;
    String name;

    public Client(Socket socket, Server server){

        this.socket = socket;
        this.server = server;
        // запускаем поток
        new Thread(this).start();
    }

    void receive(String message){
        out.println(message);
    }

    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to Chat!");
            out.println("Enter your name:");
            String input = in.nextLine();
            name = input;
            out.println("Name saved: " + name + "\nYou can start chatting.");
            input = in.nextLine();
            while (!input.equals("bye")) {
                server.sendAll(name + ": " + input);
                input = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
