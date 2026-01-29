import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

public class ServerUDP {
    public static void main(String[] args) throws IOException {
        //Creamos el socket en el puerto 5000
        DatagramSocket socket = new DatagramSocket(5000);
        System.out.println("Servidor UDP iniciado en puerto 5000");

        // Usamos un Set para guardar las direcciones de los clientes
        // Esto sustituye a la lista de "ClienteHandler" del TCP
        Set<SocketAddress> clientesConectados = new HashSet<>();

        byte[] buffer = new byte[1024]; // Búfer para recibir datos

        while (true) {
            //Preparar el paquete para recibir
            DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
            socket.receive(paqueteRecibido);

            //Obtener datos
            String mensaje = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
            SocketAddress direccionCliente = paqueteRecibido.getSocketAddress();

            // Si es la primera vez que este cliente escribe, lo guardamos
            if (!clientesConectados.contains(direccionCliente)) {
                clientesConectados.add(direccionCliente);
                System.out.println("Nuevo cliente registrado: " + direccionCliente);
            }

            System.out.println("Mensaje recibido: " + mensaje);

            //Reenviar el mensaje a todos los clientes registrados
            for (SocketAddress cliente : clientesConectados) {
                // No reenviar al que lo envió
                if (!cliente.equals(direccionCliente)) {
                    byte[] mensajeBytes = mensaje.getBytes();
                    DatagramPacket paqueteEnviado = new DatagramPacket(
                            mensajeBytes,
                            mensajeBytes.length,
                            cliente
                    );
                    socket.send(paqueteEnviado);
                }
            }
        }
    }
}
