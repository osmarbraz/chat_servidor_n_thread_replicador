
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Servidor implements Runnable, ConfiguracoesServidor {

    private Thread[] threads = new Thread[ConfiguracoesServidor.MAXIMO];
    private Replicador[] replicadores = new Replicador[ConfiguracoesServidor.MAXIMO];
    public int qtdeClientes = 0;
    private ServerSocket escuta;
    public Socket socket;

    public void executa() {
        try {
            escuta = new ServerSocket(ConfiguracoesServidor.PORTA);
            System.out.println(">>> Servidor no ar! <<<");
            System.out.println("Maximo de clientes: " + MAXIMO);
            System.out.println("Escutando a porta: " + escuta.getLocalPort());
            System.out.println("Aguardando clientes!");
            Thread t1 = new Thread(this);
            t1.start();
            while (true) {
                socket = escuta.accept(); // espera
                if (qtdeClientes < MAXIMO) {
                    //cria um replicador e coloca em um vetor
                    replicadores[qtdeClientes] = new Replicador(this);
                    //cria um thread com o replicador e coloca em outro vetor
                    threads[qtdeClientes] = new Thread(replicadores[qtdeClientes]);
                    threads[qtdeClientes].start();
                    qtdeClientes++;
                } else {
                    System.out.println("Numero maximo de conexoes atingidas");
                }
            }
        } catch (UnknownHostException uhe) {
            System.out.println("Conexao Terminada!");
        } catch (IOException ioe) {
            System.out.println("Excecao: " + ioe.getMessage());
        }
        Scanner input = new Scanner(System.in);
        System.out.println("Pressione qualquer tecla para terminar. . .");
        String mensagem = input.nextLine();
    }

    //envia a mensagem para todos os clientes
    public void envia_mensagem(String mensagem) {
        for (int i = 0; i < qtdeClientes; i++) {
            replicadores[i].pw.println(mensagem);
        }
    }

    //espera por mensagem no prompt do server e manda para os clientes
    public void run() {
        while (true) {
            Scanner input = new Scanner(System.in);
            String mensagem = ("::Servidor:: " + input.nextLine());
            envia_mensagem(mensagem);
        }
    }
}
