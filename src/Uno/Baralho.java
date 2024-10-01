package Uno;

import java.util.ArrayList;

public class Baralho {
	
	private final ArrayList<Carta> cartas;
	
	public Baralho() {
		this.cartas = new ArrayList<Carta>();
	}

	public Carta compraCarta() {
		Carta cartaComprada = Carta.CartaAleatoria();
		
		cartas.add(cartaComprada);
		return cartaComprada;
	}
	
	public void JogaCarta(int index) {
		cartas.remove(index);
	}
	
	public String exibeCartas() {
		
		String result = "";
		
		for(int i = 0 ; i < cartas.size() ; i++) {
			result = result + i + " - " + cartas.get(i).exibeCarta() + ";";
		}

		return result;
	}
	
	
	public ArrayList<Carta> getCartas() {
		return cartas;
	}
	
	
}
