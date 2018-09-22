package servidor;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int portaPadrão = 3000;

    private Socket socket = null;
    private boolean terminarConexao;
    private ServerSocket socketServidor = null;
    private DataInputStream entradaDadosCliente = null;


    public Servidor(int porta) {
        try {
            System.out.println("Concetando-se a porta " + porta + ". Por favor, aguarde...");
            socketServidor = new ServerSocket(porta);
            System.out.println("Servidor aberto na porta: " + porta);
            System.out.println("Aguardando por clientes...");
            socket = socketServidor.accept();
            System.out.println("Cliente " + socket.getInetAddress().getHostName() + " conectado.");
            abrirConexao();
            terminarConexao = false;
            while (!terminarConexao) {
                try {
                    String textoCliente = entradaDadosCliente.readUTF();
                    System.out.println(socket.getInetAddress().getHostAddress() + " diz: " + textoCliente);
                    terminarConexao = textoCliente.equals("bye");
                } catch (IOException e) {
                    terminarConexao = true;
                }
            }
            fecharConexao();
        } catch (IOException e) {
            System.out.println("Ops! " + e.getMessage());
        }
    }

    public void abrirConexao() throws IOException {
        entradaDadosCliente = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void fecharConexao() throws IOException{
        if (socket != null) socket.close();
        if (entradaDadosCliente != null) entradaDadosCliente.close();
    }

    public static void main(String[] args) {
        Servidor servidor = null;
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : portaPadrão;
        servidor = new Servidor(porta);
    }
}
