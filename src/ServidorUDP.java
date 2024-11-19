
import java.net.*;
import java.util.Scanner;
import java.io.*;

class ServidorUDP1 {
    private DatagramSocket socketUDP;
    private DatagramPacket recibido;

     // Este es el constructor de la clase ServidorUDP1.
     // Inicializa un socket UDP en el puerto especificado para escuchar los mensajes
     // entrantes. El puerto en el que el servidor escuchará las solicitudes.
    public ServidorUDP1(int puerto) {
        try {
            this.socketUDP = new DatagramSocket(puerto);
            this.recibido = null;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    
     // Aquí pongo este método que recibe un mensaje del cliente a través del socket UDP.
     // El método lee el mensaje enviado por el cliente, lo almacena en el paquete
     // y lo devuelve como una cadena de texto. Después devuelve el mensaje recibido del cliente.
    public String recibirMsg() {
        try {
            byte[] buffer = new byte[1000];
            recibido = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(recibido);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(recibido.getData()).trim();
    }

    // Después creo el método que envía un mensaje de vuelta al cliente. 
    // El servidor responde al cliente enviando el mensaje recibido previamente
    // como un "eco". El servidor enviará el mensaje de vuelta al cliente.
    public void enviarMsg(String msg) {
        try {
            DatagramPacket respuesta = new DatagramPacket(msg.getBytes(), msg.length(), recibido.getAddress(),
                    recibido.getPort());
            socketUDP.send(respuesta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Aquí cierro el socketUDP y termino el servidor 
    public void closeServidorUDP() {
        socketUDP.close();
        System.out.println("-> Servidor Terminado");
    }
}


// Por último creo la clase principal que se encarga de de la recepción y envío de los 
// mensajes. Esta clase recibe los mensajes del cliente, los muestra y responde con un "eco"
// del mensaje. El servidor seguiría funcionando hasta que el cliente envía "Fin".
public class ServidorUDP {
    public static void main(String[] args) {
        int puerto = 5555;
        System.out.println("Servidor escucha por el puerto " + puerto);
        ServidorUDP1 canal = new ServidorUDP1(puerto);
        Scanner sc = new Scanner(System.in);
        String mensaje, respuesta;

        do {
            mensaje = canal.recibirMsg();
            System.out.println("John: " + mensaje);

            if (!mensaje.equals("Fin")) {
                System.out.print("Bob: ");
                respuesta = sc.nextLine();
                canal.enviarMsg(respuesta);
            }
        } while (!mensaje.equals("Fin"));

        canal.closeServidorUDP();
    }
}
