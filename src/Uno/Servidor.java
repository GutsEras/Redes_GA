package Uno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class Servidor {
	
	public Jogador jogadores[];
	public Carta cartaAtual;
	public int indexJogador;
		
	public Servidor(int numeroJogadores) throws IOException {
		indexJogador = 0;
		jogadores = new Jogador[numeroJogadores];
		cartaAtual = Carta.CartaAleatoria();
		for(int i = 0 ; i < jogadores.length ; i++) {
			jogadores[i] = new Jogador(i + 1);
		}
	}
	
	private void aceitaJogadores() throws IOException {
		for(int i = 0 ; i < jogadores.length ; i++) {
			jogadores[i].iniciaConexao();
		}
	}
	
	private void closeJogadores() throws IOException {
		for(int i = 0 ; i < jogadores.length ; i++) {
			jogadores[i].close();
		}
	}
	
	private void distribuiCartasIniciais() {
		for(int i = 0 ; i < jogadores.length ; i++) {
			for(int j = 0 ; j < 7 ; j++) {
				jogadores[i].getBaralho().compraCarta();
			}
		}
	}
	
	private void enviaCartaAtual() throws IOException {
		
		String message = "00" + cartaAtual.exibeCarta() + "\n";
		
		for(int i = 0 ; i < jogadores.length ; i++) {
			jogadores[i].getOut().writeBytes(message);
		}
	}
	
	private void enviaFimDeJogo() throws IOException {
		
		String message = "09" + (indexJogador - 1) + cartaAtual.exibeCarta() + "\n";
		
		for(int i = 0 ; i < jogadores.length ; i++) {
			jogadores[i].getOut().writeBytes(message);
		}
	}
	
	private void enviaOpcoesJogada() throws IOException {
		String message = "01" + jogadores[indexJogador].getBaralho().exibeCartas() + ";99 - comprar carta\n";
		jogadores[indexJogador].getOut().writeBytes(message);
	}
	
	private int recebeOpcaoJogada() throws IOException {
		String opcao = jogadores[indexJogador].getIn().readLine();
		return Integer.parseInt(opcao);
	}
	
	private void compraCarta() throws IOException {
		
		Carta carta =  jogadores[indexJogador].getBaralho().compraCarta();
		
		String message = "04" + carta.exibeCarta() + "\n";
		jogadores[indexJogador].getOut().writeBytes(message);
		
	}
	
	private void verificaCartaEspecial(Carta cartaJogada) throws IOException {

		if(cartaJogada.getValor() == Carta.BLOQUEIO) {
			proximoJogador();
			
			int jogadorBloquado = indexJogador + 1;
			
			if(jogadorBloquado >= jogadores.length ) {
				jogadorBloquado = 0;
			}
			
			String message = "02" + indexJogador + "" + jogadorBloquado + "\n";
			for(int i = 0 ; i < jogadores.length ; i++) {
				jogadores[i].getOut().writeBytes(message);
			}
			
		} else if (cartaJogada.getValor() == Carta.INVERTER) {
			inverteOrdem();	
		} else if (cartaJogada.getValor() == Carta.MAIS_DOIS) {
			maisDois();
		}
			
	}
	
	private void proximoJogador() throws IOException {
		indexJogador += 1;
		
		if(indexJogador >= jogadores.length ) {
			indexJogador = 0;
		}
		
	}

	private void inverteOrdem() throws IOException {
			
		Jogador ivertido[] = new Jogador[jogadores.length];
	
		for(int i = 0 ; i < jogadores.length ; i++) {
				ivertido[i] = jogadores[jogadores.length - 1 - i];
		}
			
		
		jogadores = ivertido;
		
		String message = "03" + indexJogador + "\n";
		
		for(int i = 0 ; i < jogadores.length ; i++) {
			jogadores[i].getOut().writeBytes(message);
		}
	}
	
	private void maisDois() throws IOException {
		
		int jogadorCompra = indexJogador + 1;
		
		if(jogadorCompra >= jogadores.length ) {
			jogadorCompra = 0;
		}
		
		String message = "06" + indexJogador + jogadorCompra;
		for(int i = 0 ; i < jogadores.length ; i++) {
			if (i == jogadorCompra)
				continue;	
			jogadores[i].getOut().writeBytes(message);
		}
		
		Carta carta1 =  jogadores[jogadorCompra].getBaralho().compraCarta();
		Carta carta2 =  jogadores[jogadorCompra].getBaralho().compraCarta();
		
		message = "05" + indexJogador + carta1.exibeCarta() + ";" + carta2.exibeCarta() + "\n";
		jogadores[jogadorCompra].getOut().writeBytes(message);
			
	}	

	private boolean verificaGanhador() {
		for(int i = 0 ; i < jogadores.length ; i++) {
			if(jogadores[i].getBaralho().getCartas().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("quantos jogadores vão jogar?");
		int numeroJogadores = Integer.parseInt(scanner.readLine());
		
		Servidor servidor = new Servidor(numeroJogadores);
		int opcaoJogada;
		Carta cartaJogada;
		
		servidor.aceitaJogadores();
		
		servidor.distribuiCartasIniciais();
		
		while(true) {
			if(servidor.verificaGanhador()) {
				servidor.enviaFimDeJogo();
			} else {
				servidor.enviaCartaAtual();
			}
			
			servidor.enviaOpcoesJogada();
	
			opcaoJogada = servidor.recebeOpcaoJogada();
			
			if(opcaoJogada == 99) {
				servidor.compraCarta();
			} else {
				cartaJogada = servidor.jogadores[servidor.indexJogador].getBaralho().getCartas().get(opcaoJogada);
				servidor.cartaAtual = cartaJogada;
				servidor.jogadores[servidor.indexJogador].getBaralho().JogaCarta(opcaoJogada);
				
				servidor.verificaCartaEspecial(cartaJogada);
					
			}
			
			if(servidor.verificaGanhador()) {
				servidor.enviaFimDeJogo();
				break;
			}
			
			servidor.proximoJogador();
		}
		
		servidor.closeJogadores();
		
	}
		
}


