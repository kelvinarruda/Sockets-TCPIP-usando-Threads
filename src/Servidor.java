import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class Servidor extends Thread {

	private static Vector<PrintStream> clientes;
	private Socket conexao;
	private String meuNome;

	public Servidor(Socket s) {
		conexao = s;
	}

	public static void main(String[] args) throws Exception {
		clientes = new Vector<PrintStream>();

		try (ServerSocket s = new ServerSocket(2000)) {
			while (true) {
				System.out.println("Conectando...");
				Socket conexao = s.accept();
				System.out.println("Conectado.");
				Thread t = new Servidor(conexao);
				t.start();
			}
		} catch (Exception e) {
			System.out.println("Desculpa, n�o foi poss�vel iniciar o servidor.");
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			PrintStream saida = new PrintStream(conexao.getOutputStream());
			meuNome = entrada.readLine();
			if (meuNome == null)
				return;
			clientes.add(saida);
			String linha = entrada.readLine();

			while (linha != null && !linha.trim().isEmpty()) {
				sendToAll(saida, " disse: ", linha);
				linha = entrada.readLine();
			}
			sendToAll(saida, " saiu ", "do Chat!");
			clientes.remove(saida);
			conexao.close();
		} catch (Exception e) {
		}
	}

	public void sendToAll(PrintStream saida, String acao, String linha) {
		Enumeration<PrintStream> e = clientes.elements();
		while (e.hasMoreElements()) {
			PrintStream chat = (PrintStream) e.nextElement();

			if (chat != saida)
				chat.println(meuNome + acao + linha);
			if (acao.equalsIgnoreCase(" saiu ")) {
				if (chat == saida)
			
					
					chat.println();
			}
		}
	}
}
