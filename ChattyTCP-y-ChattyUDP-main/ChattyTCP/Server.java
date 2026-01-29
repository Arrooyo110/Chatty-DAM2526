import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public void iniciarServer(){
        try {
            while (! serverSocket.isClosed()){
                Socket socket= serverSocket.accept();
                System.out.println("se ha conectado un nuevo cliente");
                ClienteHandler clienteHandler = new ClienteHandler(socket);
                Thread thread= new Thread(clienteHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            cerrarServer();
        }
    }

    public void cerrarServer(){
        try{
            if(!serverSocket.isClosed()){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Server(ServerSocket socket){
        this.serverSocket= socket;
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket= new ServerSocket(5000);
        Server server = new Server(serverSocket);
        server.iniciarServer();
    }




}
