
import java.net.*;
import java.util.Scanner;
import java.io.*;

class ClienteUDP1 {
    private DatagramSocket socketUDP;

    //Aquí creo la clase del cliente con el datagram para transmitir los emnsajes mediante paquetes.
    public ClienteUDP1() {
        try {
            socketUDP = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // Después creo enviarMsg y declaro el mensaje, la dirección ip y el puerto usado.
    // Obtengo la ip con el nombre del host, después convierto el mensaje en bytes.
    // Creo el paquete y inicializo el socket UDP. Por último enviaría el mensaje, cierro
    // el socket y manejo los errores.
    public void enviarMsg(String msg, String ip, int puerto) {
        InetAddress hostServidor;
        try {
            hostServidor = InetAddress.getByName(ip);
            byte[] mensaje = msg.getBytes();
            DatagramPacket peticion = new DatagramPacket(mensaje, mensaje.length, hostServidor, puerto);
            socketUDP.send(peticion);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // Este método recibe un mensaje desde el servidor. Después creo un buffer para almacenar los datos
    // recibidos, se encapsulan en un DatagramPacket, después  se devuelve el mensaje como texto.
    public String recibirMsg() {
        DatagramPacket respuesta = null; 

        try {
            byte[] bufer = new byte[1000];

            respuesta = new DatagramPacket(bufer, bufer.length);

            socketUDP.receive(respuesta);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(respuesta.getData()).trim();
    }

    // Este método sirve para cerrar la conexión del cliente.
    // Finalizo el uso del socket UDP, liberando recursos del sistema.
    public void closeClienteUDP() {
        socketUDP.close(); 
        System.out.println("-> Cliente Terminado"); 
    }
}

//En esta última clase gestiona la interacción del usuario con el servidor mediante el protocolo UDP.
// El cliente envía y recibe mensajes al servidor (que actúa como eco) y alterna entre el envío de mensajes 
// y la recepción de respuestas. El proceso termina cuando el usuario ingresa el mensaje "Fin".
// La comunicación con el servidor se realiza mediante un socket UDP, y la conexión se cierra automáticamente
// cuando el cliente envía "Fin".
public class ClienteUDP {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String mensaje, respuesta;
        ClienteUDP1 canal = new ClienteUDP1();
        System.out.println("Comience la conversación ('Fin') para terminar");
        System.out.println("John: ");
        mensaje = sc.nextLine();

        canal.enviarMsg(mensaje, "localhost", 5555);

        do {
            if (!mensaje.equals("Fin")) {
                respuesta = canal.recibirMsg();
                System.out.println("Bob: " + respuesta);
            }

            if (!mensaje.equals("Fin")) {
                System.out.print("john: ");
                mensaje = sc.nextLine();
                canal.enviarMsg(mensaje, "localhost", 5555);
            }
        } while (!mensaje.equals("Fin"));

        canal.closeClienteUDP();
    }
}
