import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteUDP {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre: ");
        String nombre = scanner.nextLine();

        //Crear el socket
        DatagramSocket socket = new DatagramSocket();

        // Configuración del puerto
        InetAddress direccionServidor = InetAddress.getByName("localhost");
        int puertoServidor = 5000;

        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    // Preparar paquete vacío para recibir
                    DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paqueteRecibido);

                    // Convertir bytes a String
                    String mensajeServidor = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                    System.out.println(mensajeServidor);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Enviamos un primer mensaje para registrarnos en el servidor
        String saludo = nombre + " se ha unido al chat.";
        byte[] bufferSaludo = saludo.getBytes();
        DatagramPacket paqueteSaludo = new DatagramPacket(bufferSaludo, bufferSaludo.length, direccionServidor, puertoServidor);
        socket.send(paqueteSaludo);

        // Bucle para enviar mensajes
        while (true) {
            String texto = scanner.nextLine();
            String mensajeCompleto = nombre + ": " + texto;
            byte[] bufferMensaje = mensajeCompleto.getBytes();

            // Crear paquete con dirección destino
            DatagramPacket paqueteEnvio = new DatagramPacket(
                    bufferMensaje,
                    bufferMensaje.length,
                    direccionServidor,
                    puertoServidor
            );
            socket.send(paqueteEnvio);
        }
    }
}
