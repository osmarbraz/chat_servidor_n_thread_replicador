
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;


public class Replicador implements Runnable, ConfiguracoesServidor {

    public int qtdeClientes = 0;
    public Servidor servidor;
    public BufferedReader br;
    public PrintWriter pw;
    public String nome;

    /**
     * Construtor do replicador Recebe o servidor a ser replicado
     */
    public Replicador(Servidor servidor) {
        try {
            this.servidor = servidor;
            br = new BufferedReader(new InputStreamReader(servidor.socket.getInputStream()));
            pw = new PrintWriter(servidor.socket.getOutputStream(), true);
        } catch (IOException ioe) {
            System.out.println("ioe=" + ioe);
        }
    }

    @Override
    public void run() {
        try {
            pw.println("Bem vindo!");
            nome = br.readLine();
            System.out.println("-----------------------------------------");
            System.out.println("Conectando o usuario:" + servidor.qtdeClientes);
            System.out.println("Usuario:" + nome + " conectado!");
            System.out.println("-----------------------------------------");
            if (qtdeClientes < ConfiguracoesServidor.MAXIMO) {
                while (true) {
                    String mensagem = br.readLine();
                    System.out.println("Mensagem de " + nome + ": " + mensagem);
                    if (mensagem.startsWith("fim")) {
                        if (servidor.qtdeClientes >= 0) {
                            servidor.qtdeClientes--;
                        }
                    }
                    servidor.envia_mensagem("Mensagem de " + nome + ": " + mensagem);
                }
            } else {
                pw.println("Numero maximo de conexoes atingidas");
            }
        } catch (UnknownHostException uhe) {
            System.out.println("Conexao Terminada!");
        } catch (IOException ioe) {
            if (servidor.qtdeClientes >= 0) {
                servidor.qtdeClientes--;
            }
            System.out.println("ioe=" + ioe);
        }
    }
}
