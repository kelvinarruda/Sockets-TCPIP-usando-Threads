import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Cliente extends Thread {

	private static boolean done = false;
	private Socket conexao;

	public Cliente(Socket s) {
		conexao = s;
	}

	public static void main(String[] args) throws Exception {
		Socket conexao = new Socket("localhost", 2000);
		PrintStream saida = new PrintStream(conexao.getOutputStream());

		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Por gentileza digite o seu nome: ");
		String meuNome = teclado.readLine();
		saida.println(meuNome);

		Thread t = new Cliente(conexao);
		t.start();
		String linha;
		while (true) {
			if (done)
				break;
			System.out.println("> ");
			linha = teclado.readLine();
			saida.println(linha);
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			String linha;
			while (true) {
				linha = entrada.readLine();
				if (linha.trim().isEmpty()) {
					System.out.println("\nConex�o finalizada!");
					break;
					
					
				}
				System.out.println();
				System.out.println(linha);
				System.out.print("...> ");
			}
			done = true;
		} catch (Exception e) {
		}
	}
}
