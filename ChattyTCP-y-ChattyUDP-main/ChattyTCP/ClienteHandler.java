import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteHandler implements Runnable{
    private Socket socket;
    public static ArrayList<ClienteHandler> clientHandlers= new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nombreCliente;

    public ClienteHandler(Socket socket){
        try{
            this.socket=socket;
            this.bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.nombreCliente=bufferedReader.readLine();
            clientHandlers.add(this);
            mandarMensaje("SERVER: se ha unido el cliente "+nombreCliente);
        } catch (IOException e) {
            e.printStackTrace();
            cerrarComunicacion(socket,bufferedWriter,bufferedReader);
        }

    }

    public void quitarClienteHandler(){
        clientHandlers.remove(this);
        mandarMensaje("SERVER: se ha ido "+nombreCliente);
    }

    @Override
    public void run() {
        String mensajeDesdeCliente;
        while (socket.isConnected()){
            try{
                mensajeDesdeCliente=bufferedReader.readLine();
                mandarMensaje(mensajeDesdeCliente);


            } catch (IOException e) {
                e.printStackTrace();
                cerrarComunicacion(socket,bufferedWriter,bufferedReader);
            }
        }

    }
    public void mandarMensaje(String mensaje){
        for (ClienteHandler clienteHandler: clientHandlers){
            try{
                clienteHandler.bufferedWriter.write(mensaje);
                clienteHandler.bufferedWriter.newLine();
                clienteHandler.bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
                cerrarComunicacion(socket,bufferedWriter,bufferedReader);
            }
        }

    }
    public void cerrarComunicacion(Socket socket,BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        quitarClienteHandler();
        try{
            if (socket != null){
                socket.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (bufferedReader != null){
                bufferedReader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
