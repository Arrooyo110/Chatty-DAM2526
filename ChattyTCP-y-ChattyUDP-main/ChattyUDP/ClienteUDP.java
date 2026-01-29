import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteUDP {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre: ");
        String nombre = scanner.nextLine();

        // 1. Crear el socket (sin puerto específico, el sistema asigna uno libre) [cite: 898]
        DatagramSocket socket = new DatagramSocket();

        // Configuración del servidor destino (Localhost:5000)
        InetAddress direccionServidor = InetAddress.getByName("localhost");
        int puertoServidor = 5000;

        // --- HILO PARA ESCUCHAR (RECEPCIÓN) ---
        // Implementamos la lógica de recibir en segundo plano
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    // Preparar paquete vacío para recibir
                    DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paqueteRecibido); // Bloqueante [cite: 899]

                    // Convertir bytes a String [cite: 892]
                    String mensajeServidor = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                    System.out.println(mensajeServidor);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // --- HILO PRINCIPAL (ENVÍO) ---
        // Enviamos un primer mensaje para "registrarnos" en el servidor
        String saludo = nombre + " se ha unido al chat.";
        byte[] bufferSaludo = saludo.getBytes();
        DatagramPacket paqueteSaludo = new DatagramPacket(bufferSaludo, bufferSaludo.length, direccionServidor, puertoServidor);
        socket.send(paqueteSaludo); // [cite: 900]

        // Bucle para enviar mensajes
        while (true) {
            String texto = scanner.nextLine();
            String mensajeCompleto = nombre + ": " + texto;
            byte[] bufferMensaje = mensajeCompleto.getBytes();

            // Crear paquete con dirección destino [cite: 884]
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
