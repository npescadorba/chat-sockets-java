
package com.mycompany.entregajava1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ManejadorCliente implements Runnable {

    private final Socket cliente;
    private DataOutputStream salida;
    private String nombre;

    public ManejadorCliente(Socket cliente) {
        this.cliente = cliente;
    }

    // Obtener el nombre del usuario
    public String getNombre() {
        return nombre;
    }

    @Override
    public void run() {

        try (Socket conexion = cliente;
             DataInputStream entrada = new DataInputStream(
                     conexion.getInputStream());
             DataOutputStream canalSalida = new DataOutputStream(
                     conexion.getOutputStream())) {

            salida = canalSalida;

            nombre = entrada.readUTF();

            Servidor.agregarCliente(this);

            System.out.println(nombre + " se ha conectado.");

            Servidor.enviarMensaje(
                    nombre + " ha entrado al chat.");

            while (true) {

                String mensaje = entrada.readUTF();

                if (mensaje.equalsIgnoreCase("salir")) {
                    break;
                }

                // Comando para consultar usuarios
                if (mensaje.equalsIgnoreCase("usuarios")) {

                    String lista = Servidor.listarUsuarios();
                    enviarMensaje(lista);

                } else {

                    System.out.println(nombre + ": " + mensaje);

                    Servidor.enviarMensaje(
                            nombre + ": " + mensaje);
                }
            }

        } catch (IOException e) {

            System.out.println("Conexion finalizada: "
                    + e.getMessage());

        } finally {

            Servidor.eliminarCliente(this);

            if (nombre != null) {

                System.out.println(nombre + " se desconecto.");

                Servidor.enviarMensaje(
                        nombre + " ha salido del chat.");
            }
        }
    }

    // Enviar un mensaje a este cliente
    public synchronized void enviarMensaje(String mensaje) {

        try {
            if (salida != null) {
                salida.writeUTF(mensaje);
                salida.flush();
            }

        } catch (IOException e) {
            System.out.println("Error al enviar mensaje: "
                    + e.getMessage());
        }
    }
}
