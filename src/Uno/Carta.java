package Uno;

public class Carta {
	
	public static final int AMARELO = 0;
	public static final int AZUL = 1;
	public static final int VERDE = 2;
	public static final int VERMELHO = 3;
	
	public static final int BLOQUEIO = 10;
	public static final int INVERTER = 11;
	public static final int MAIS_DOIS = 12;
	
	private final int cor;
	private final int valor;
	
	private Carta(int cor, int valor) {
		super();
		this.cor = cor;
		this.valor = valor;
	}
	
	public static Carta CartaAleatoria() {
		
		int valor = (int) (Math.random() * 13);		
		
		int cor = (int) (Math.random() * 4);
		
		
		return new Carta(cor, valor);
	}
	
	
	public String exibeCarta() { 
		return getStringValor() + " " + getStringCor();
	}

	private String getStringValor() {
		
		if (valor == BLOQUEIO) {
			return "Bloqueio";
		} else if (valor == INVERTER) {
			return "Inverter";
		} else if (valor == MAIS_DOIS) {
			return "+2";
		}
		
		return Integer.toString(valor);
	}
	
	public String getStringCor(){
		if (cor == AMARELO) {
			return "amarelo";
		} else if (cor == AZUL) {
			return "azul";
		} else if (cor == VERDE) {
			return "verde";
		} else {
			return "vermelho";
		}
	}
	
	public int getCor() {
		return cor;
	}

	public int getValor() {
		return valor;
	}
	
	
}
