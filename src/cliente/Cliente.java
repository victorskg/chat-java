package cliente;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    private static final int portaPadrão = 3000;
    private static final String enderecoPadrao = "localhost";

    private Socket socket = null;
    private DataOutputStream saidaDados = null;
    private DataInputStream entradaDadosConsole = null;
    private DataInputStream entradaDadosServidor = null;

    public Cliente(String endereco, int porta) {
        System.out.println("Tentando conectar. Por favor, aguarde...");
        try {
            socket = new Socket(endereco, porta);
            System.out.println("Conectado á porta: " + socket.getLocalPort());
            iniciar();
        } catch(UnknownHostException uhe) {
            System.out.println("Máquina desconhecida: " + uhe.getMessage());
        } catch(IOException ioe) {
            System.out.println("Erro desconhecido: " + ioe.getMessage());
        }
        String textoCliente = "";
        while (!textoCliente.equals("bye")) {
            try {
                //Escreve texto do cliente
                textoCliente = entradaDadosConsole.readLine();
                saidaDados.writeUTF(textoCliente);
                saidaDados.flush();
            } catch(IOException ioe) {
                System.out.println("Ops! " + ioe.getMessage());
            }

            try {
                //Ler texto vindo do servidor
                String textoServidor = entradaDadosServidor.readUTF();
                System.out.println("Servidor diz: " + textoServidor);
            } catch (IOException ioe) {
                System.out.println("Ops! " + ioe.getMessage());
            }
        }
    }

    public void iniciar() throws IOException{
        entradaDadosConsole = new DataInputStream(System.in);
        saidaDados = new DataOutputStream(socket.getOutputStream());
        entradaDadosServidor = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public static void main(String[] args) {
        Cliente cliente = null;
        String endereco = args.length > 0 ? args[0] : enderecoPadrao;
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : portaPadrão;
        cliente = new Cliente(endereco, porta);
    }
}
