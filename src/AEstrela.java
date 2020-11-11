import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AEstrela {
	private ArrayList<Nodo> conexoes;
	private Nodo[][] labirinto;
	private int[][] labirintoArquivo;

	private int quantNos;
	private Nodo noInicial;
	private Nodo noFinal;
	private Nodo noCorrente;

	public AEstrela() {
		conexoes = new ArrayList<>();
		labirinto = new Nodo[12][12]; 
		labirintoArquivo = montarLabirinto();
	}

	public void carregaDados() {
		quantNos = 12; // 12x12

		for (int i = 0; i < quantNos; i++)
			for (int j = 0; j < quantNos; j++)
				labirinto[i][j] = new Nodo(i, j);

		noInicial = containsNo(labirinto, labirinto[0][0]);
		noFinal = containsNo(labirinto, labirinto[11][11]);
		labirinto[0][0].setVisitado(true); // inicial ja é visitado
		noCorrente = noInicial;
	}

	public String encontraCaminho() {
		Nodo aux;
		String caminho = "";
		while (noFinal != noCorrente) {
			noCorrente.setVisitado(true);
			conexoes.remove(noCorrente);
			insereconexoes(noCorrente);
			try {
				noCorrente = proximoNo();
			} catch (Exception e) {
				//qualquer erro ocorrido signifca que não tem caminho valido.
				return "Nao Existe caminho valido para esse labirinto.";
			}
		}

		aux = noFinal;
		while (aux != null) {
			caminho = " (" + aux.getLinha() + "," + aux.getColuna() + ") " + caminho;
			aux = aux.getAnt();
		}
		gravaLabirintoResultado(noFinal);
		return "Caminho: " + caminho;
	}

	private Nodo proximoNo() {
		Nodo prox;
		int atual;
		int aux;

		prox = conexoes.get(0);
		atual = heuristica(prox, noFinal);

		for (Nodo cur : conexoes) {
			aux = heuristica(prox, noFinal);
			if (aux < atual) {// se atual melhor que menor
				atual = aux;
				prox = cur;
			}
		}
		return prox;
	}

	private int heuristica(Nodo prox, Nodo noFinal) {
		return (noFinal.linha + noFinal.coluna) - (prox.linha + prox.coluna);
	}

	private void insereconexoes(Nodo noCorrente) {

		Nodo acima, abaixo, esquerda, direita;

		for (int i = 0; i < 4; i++) {
			try {
				if (i == 0) { // acima
					acima = labirinto[noCorrente.linha - 1][noCorrente.coluna];
					int novoPeso = acima.linha + acima.coluna;
					atribuiPeso(noCorrente, acima, novoPeso);
				} else if (i == 1) { // abaixo
					abaixo = labirinto[noCorrente.linha + 1][noCorrente.coluna];
					int novoPeso = abaixo.linha + abaixo.coluna;
					atribuiPeso(noCorrente, abaixo, novoPeso);
				} else if (i == 2) { // esquerda
					esquerda = labirinto[noCorrente.linha][noCorrente.coluna - 1];
					int novoPeso = esquerda.linha + esquerda.coluna;
					atribuiPeso(noCorrente, esquerda, novoPeso);
				} else { // direita
					direita = labirinto[noCorrente.linha][noCorrente.coluna + 1];
					int novoPeso = direita.linha + direita.coluna;
					atribuiPeso(noCorrente, direita, novoPeso);
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				// Caiu fora do array ignora movimento
				continue;
			}
		}

	}

	private void atribuiPeso(Nodo noCorrente, Nodo nodo, int novoPeso) {
		if (nodo != null && labirintoArquivo[nodo.linha][nodo.coluna] == 0) {
			if (!nodo.isVisitado()) {
				nodo.setAnt(noCorrente);
				conexoes.add(nodo);
				labirinto[nodo.linha][nodo.coluna].setVisitado(true);
			}
		}
	}

	public int[][] montarLabirinto() {
		int[][] labirinto = new int[12][12];
		try {
			Scanner in = new Scanner(new FileReader("labirinto.txt"));
			int linha = 0;
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] valores = line.split(" ");
				for (int i = 0; i < labirinto[0].length; i++) {
					labirinto[linha][i] = Integer.parseInt(valores[i]);
				}
				linha++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo Nao Encontrado!");
			return null;
		}
		printLabirinto(labirinto);
		return labirinto;
	}

	public void printLabirinto(int[][] labirinto) {
		System.out.println("Labirinto Carregado:");
		for (int i = 0; i < labirinto.length; i++) {
			for (int j = 0; j < labirinto[0].length; j++) {
				if (labirinto[i][j] == 2) {
					System.out.print("x ");
				} else {
					System.out.print(labirinto[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	private void gravaLabirintoResultado(Nodo nodo) {
		BufferedWriter buffWrite;
		try {
			buffWrite = new BufferedWriter(new FileWriter("resultadoAStar.txt"));

			Nodo aux = nodo;
			while (aux != null) {
				labirintoArquivo[aux.linha][aux.coluna] = 2;
				aux = aux.getAnt();
			}

			for (int i = 0; i < labirintoArquivo.length; i++) {
				for (int j = 0; j < labirintoArquivo.length; j++) {
					if (labirintoArquivo[i][j] == 2) {
						buffWrite.append("x ");
					} else
						buffWrite.append(labirintoArquivo[i][j] + " ");
				}
				buffWrite.append("\n");
			}

			buffWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Nodo containsNo(Nodo[][] nodos, Nodo nodoAlvo) {
		for (Nodo[] linhaNodos : nodos) {
			for (Nodo nodoAtual : linhaNodos) {
				if (nodoAtual == nodoAlvo) {
					return nodoAtual;
				}
			}
		}
		return null;
	}

}
