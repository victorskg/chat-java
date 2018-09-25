package servidor;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int portaPadrão = 3000;

    private Socket socket = null;
    private boolean terminarConexao;
    private ServerSocket socketServidor = null;
    private DataInputStream entradaDadosCliente = null;

    private DataOutputStream saidaDados = null;
    private DataInputStream entradaDadosConsole = null;


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
            String textoServidor = "";
            while (!terminarConexao) {
                try {
                    //Ler texto vindo do cliente
                    String textoCliente = entradaDadosCliente.readUTF();
                    System.out.println("Cliente diz: " + textoCliente);
                    terminarConexao = textoCliente.equals("bye");
                } catch (IOException e) {
                    terminarConexao = true;
                }

                try {
                    //Escreve e envia texto do servidor
                    textoServidor = entradaDadosConsole.readLine();
                    saidaDados.writeUTF(textoServidor);
                    saidaDados.flush();
                } catch (IOException e) {
                    System.out.println("Ops! " + e.getMessage());
                }
            }
            fecharConexao();
        } catch (IOException e) {
            System.out.println("Ops! " + e.getMessage());
        }
    }

    public void abrirConexao() throws IOException {
        entradaDadosCliente = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        entradaDadosConsole = new DataInputStream(System.in);
        saidaDados = new DataOutputStream(socket.getOutputStream());
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
