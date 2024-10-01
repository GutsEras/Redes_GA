package Uno;

import java.io.*;
import java.net.*;


public class Jogador {
	
	private Baralho baralho;
	private ServerSocket socket;
	private BufferedReader in;
	private DataOutputStream out;
	
	public Jogador(int porta) throws IOException {
		baralho = new Baralho();
		socket = new ServerSocket(porta);
		System.out.println("Socket criado com porta " + porta);
	}
	    
	public void iniciaConexao() throws IOException {
		Socket socketCliente = socket.accept();
		System.out.println("Conexao aceita");
		
		
		in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
		out = new DataOutputStream(socketCliente.getOutputStream());
	}
	
	public void close() throws IOException{
		socket.close();
		in.close();
		out.close();
		System.out.println("classes fechadas");
	}
	
	public Baralho getBaralho() {
		return baralho;
	}
	
	public DataOutputStream getOut() {
		return out;
	}
	
	public BufferedReader getIn() {
		return in;
	}
}
