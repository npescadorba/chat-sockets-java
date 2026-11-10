
package com.mycompany.entregajava1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        try (Socket cliente = new Socket("127.0.0.1", 5000);
             DataInputStream entrada = new DataInputStream(
                     cliente.getInputStream());
             DataOutputStream salida = new DataOutputStream(
                     cliente.getOutputStream());
             Scanner teclado = new Scanner(System.in)) {

            System.out.println("Conectado al servidor.");
            System.out.print("Escribe tu nombre: ");

            String nombre = teclado.nextLine();
            salida.writeUTF(nombre);
            salida.flush();

            Thread receptor = new Thread(() -> {

                try {
                    while (true) {
                        String mensaje = entrada.readUTF();
                        System.out.println();
                        System.out.println(mensaje);
                        System.out.print("Tu mensaje: ");
                    }
                } catch (IOException e) {
                    System.out.println(
                            "\nConexion con el servidor finalizada.");
                }
            });

            receptor.setDaemon(true);
            receptor.start();

            System.out.println("Bienvenido al chat, " + nombre);
            System.out.println("Escribe salir para desconectarte.");

            while (true) {

                System.out.print("Tu mensaje: ");
                String mensaje = teclado.nextLine();

                salida.writeUTF(mensaje);
                salida.flush();

                if (mensaje.equalsIgnoreCase("salir")) {
                    System.out.println("Desconectando...");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
