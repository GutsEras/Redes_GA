package Uno;

import java.io.*;
import java.net.*;
import java.security.PublicKey;
import java.util.Scanner;

public class Cliente {
	
	private Socket socket;
	private DataOutputStream out;
	private BufferedReader in;
	private BufferedReader scanner;
	
	public Cliente(String ip, int porta) {
		try {
			socket = new Socket(ip, porta);
			out = new DataOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			scanner = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException{
			socket.close();
			out.close();
			in.close();
			scanner.close();
	}
	
	public static String traduzMensagem(String mensagem) {
		
		if (mensagem.substring(0, 2).equals("00")) {
			return exibeCartaMesa(mensagem.substring(2));
		} else if (mensagem.substring(0, 2).equals("01")) {
			return exibeOpcoesJogada(mensagem);
		} else if (mensagem.substring(0, 2).equals("02")) {
			return exibeBloqueio(mensagem);
		} else if (mensagem.substring(0, 2).equals("03")) {
			return exibeInversao(mensagem);
		} else if (mensagem.substring(0, 2).equals("04")) {
			return exibeCompraCarta(mensagem);
		} else if (mensagem.substring(0, 2).equals("05")) {
			return exibeMaisDoisCartas(mensagem);
		} else if (mensagem.substring(0, 2).equals("06")) {
			return exibeMaisDoisGeral(mensagem);
		} else if (mensagem.substring(0, 2).equals("09")) {
			return exibeFimDeJogo(mensagem);
		} 
		return "";
		
	}

	private static String exibeMaisDoisGeral(String mensagem) {
		return "O jogador " + mensagem.substring(2, 3) + " fez o jogador " + mensagem.substring(3, 4) + " comprar +2\n";
	}

	private static String exibeMaisDoisCartas(String mensagem) {
		mensagem = mensagem.replace(";", "\n");
		return "O jogador " + mensagem.substring(2, 3) + " jogou um +2\nVocê comprou as seguintes cartas:\n" + mensagem.substring(3) + "\n";
	}

	private static String exibeCartaMesa(String mensagem) {
		return "A carta da mesa é: \n" + mensagem + "\n";
	}
	
	private static String exibeOpcoesJogada(String mensagem) {
		mensagem = mensagem.replace(";", "\n");
		return "Envie o index da carta que deseja jogar:\n" + mensagem.substring(2);
	}
	
	private static String exibeBloqueio(String mensagem) {
		return "O jogador " + mensagem.substring(2, 3) + " foi bloqueado pelo jogador " + mensagem.substring(3, 4) + "\n";
	}
	
	private static String exibeInversao(String mensagem) {
		return "O jogador " + mensagem.substring(2, 3) + " inverteu a ordem do jogo\n";
	}
	
	private static String exibeCompraCarta(String mensagem) {
		return "Você comprou a carta " + mensagem.substring(2) + "\n";
	}
	
	private static String exibeFimDeJogo(String mensagem) {
		mensagem = mensagem.replace(";", "\n");
		return "Fim de jogo, o jogador " + mensagem.substring(2,3) + " jogou sua ultima carta (" + mensagem.substring(3) + ")\n";
	}

	public Socket getSocket() {
		return socket;
	}

	public DataOutputStream getOut() {
		return out;
	}
	
	public BufferedReader getIn() {
		return in;
	}
	
	public BufferedReader getScanner() {
		return scanner;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("qual sua porta?");
		int portaJogador = Integer.parseInt(scanner.readLine());
		
		Cliente cliente = new Cliente("localhost", portaJogador);
		
		String mensagem = "";
		
		while(true) {
			mensagem = cliente.in.readLine();
			System.out.println(traduzMensagem(mensagem));
			
			if (mensagem.substring(0, 2).equals("01")){
				mensagem = cliente.scanner.readLine() + "\n";
				cliente.out.writeBytes(mensagem);
			}
			
			if (mensagem.substring(0, 2).equals("09")) {
				break;
			}
		}
		cliente.close();
	}
}
