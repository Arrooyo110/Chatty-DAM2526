import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;

public class ServerUDP {
    public static void main(String[] args) throws IOException {
        // 1. Creamos el socket en el puerto 5000 (Igual que en tu TCP) [cite: 898]
        DatagramSocket socket = new DatagramSocket(5000);
        System.out.println("Servidor UDP iniciado en puerto 5000");

        // Usamos un Set para guardar las direcciones de los clientes (IP + Puerto)
        // Esto sustituye a la lista de "ClienteHandler" del TCP
        Set<SocketAddress> clientesConectados = new HashSet<>();

        byte[] buffer = new byte[1024]; // Búfer para recibir datos [cite: 882]

        while (true) {
            // 2. Preparar el paquete para recibir [cite: 899]
            DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
            socket.receive(paqueteRecibido); // Se queda esperando datos

            // 3. Obtener datos del remitente
            String mensaje = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
            SocketAddress direccionCliente = paqueteRecibido.getSocketAddress();

            // Si es la primera vez que este cliente escribe, lo guardamos
            if (!clientesConectados.contains(direccionCliente)) {
                clientesConectados.add(direccionCliente);
                System.out.println("Nuevo cliente registrado: " + direccionCliente);
            }

            System.out.println("Mensaje recibido: " + mensaje);

            // 4. BROADCAST: Reenviar el mensaje a TODOS los clientes registrados [cite: 900]
            for (SocketAddress cliente : clientesConectados) {
                // No reenviar al que lo envió (opcional, pero queda mejor en chats)
                if (!cliente.equals(direccionCliente)) {
                    byte[] mensajeBytes = mensaje.getBytes();
                    DatagramPacket paqueteEnviado = new DatagramPacket(
                            mensajeBytes,
                            mensajeBytes.length,
                            cliente // Envia a la dirección guardada
                    );
                    socket.send(paqueteEnviado);
                }
            }
        }
    }
}
