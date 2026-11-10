
package com.mycompany.entregajava1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {

    // Lista de clientes conectados
    private static final List<ManejadorCliente> clientes =
            new ArrayList<>();

    // Agregar un cliente
    public static synchronized void agregarCliente(
            ManejadorCliente cliente) {

        clientes.add(cliente);
    }

    // Eliminar un cliente
    public static synchronized void eliminarCliente(
            ManejadorCliente cliente) {

        clientes.remove(cliente);
    }

    // Enviar mensajes a todos los clientes
    public static synchronized void enviarMensaje(
            String mensaje) {

        for (ManejadorCliente cliente : clientes) {
            cliente.enviarMensaje(mensaje);
        }
    }

    // Obtener la lista de usuarios conectados
    public static synchronized String listarUsuarios() {

        StringBuilder lista = new StringBuilder();

        lista.append("Usuarios conectados:\n");

        int numero = 1;

        for (ManejadorCliente cliente : clientes) {

            lista.append(numero)
                 .append(". ")
                 .append(cliente.getNombre())
                 .append("\n");

            numero++;
        }

        lista.append("Total: ").append(clientes.size());

        return lista.toString();
    }

    public static void main(String[] args) {

        try (ServerSocket servidor = new ServerSocket(5000)) {

            System.out.println("Servidor de chat iniciado.");
            System.out.println("Esperando usuarios...");

            while (true) {

                Socket socket = servidor.accept();

                System.out.println("Nueva conexion recibida.");

                ManejadorCliente manejador =
                        new ManejadorCliente(socket);

                Thread hilo = new Thread(manejador);
                hilo.start();
            }

        } catch (IOException e) {

            System.out.println("Error del servidor: "
                    + e.getMessage());
        }
    }
}
