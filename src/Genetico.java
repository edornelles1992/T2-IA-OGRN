import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Genetico {

	int numGeracoes;
	int numMovimentos;
	int parede;
	int tamanho;
	int MOEDA = 8;
	int ENTRADA = 9;
	int SAIDA = 9;
	int PAREDE = 1;
	int CAMINHO = 0;
	Random rng;

	public Genetico(int numGeracoes, int numMovimentos, int parede, int tamanho) {
		this.numGeracoes = numGeracoes;
		this.numMovimentos = numMovimentos;
		this.parede = parede;
		this.tamanho = tamanho;
		this.rng = new Random();
	}

	public void mutacao(Movimento[][] populacaoIntermediaria, int taxaMutacao) {

		for (int i = 0; i < numMovimentos * taxaMutacao; i++) {
			int linha = rng.nextInt(((numMovimentos - 1) - 1) + 1) + 1;
			int coluna = rng.nextInt(numMovimentos);

			Movimento sorteado = Movimento.getMovimentoByValue(rng.nextInt((4 - 1) + 1) + 1);

			if (populacaoIntermediaria[linha][coluna] == Movimento.B) {
				while (sorteado == Movimento.B) {
					sorteado = Movimento.getMovimentoByValue(rng.nextInt((4 - 1) + 1) + 1);
				}
				populacaoIntermediaria[linha][coluna] = sorteado;
			}
			if (populacaoIntermediaria[linha][coluna] == Movimento.C) {
				while (sorteado == Movimento.C) {
					sorteado = Movimento.getMovimentoByValue(rng.nextInt((4 - 1) + 1) + 1);
				}
				populacaoIntermediaria[linha][coluna] = sorteado;
			}
			if (populacaoIntermediaria[linha][coluna] == Movimento.E) {
				while (sorteado == Movimento.E) {
					sorteado = Movimento.getMovimentoByValue(rng.nextInt((4 - 1) + 1) + 1);
				}
				populacaoIntermediaria[linha][coluna] = sorteado;
			}
			if (populacaoIntermediaria[linha][coluna] == Movimento.D) {
				while (sorteado == Movimento.D) {
					sorteado = Movimento.getMovimentoByValue(rng.nextInt((4 - 1) + 1) + 1);
				}
				populacaoIntermediaria[linha][coluna] = sorteado;
			}
		}
	}

	public void crossOver(Movimento[][] populacao, Movimento[][] populacaoIntermediaria, int[] aptidoes) {

		int i = 1; // pula primeira linha
		int pai;
		int mae;

		for (int j = 0; j < (numMovimentos / 2); j++) {

			pai = torneio(aptidoes);
			mae = torneio(aptidoes);

			for (int coluna = 0; coluna < (numMovimentos / 2); coluna++) {
				populacaoIntermediaria[i][coluna] = populacao[pai][coluna];
				if (i != numMovimentos - 1)
					populacaoIntermediaria[i + 1][coluna] = populacao[mae][coluna];
			}

			for (int coluna = numMovimentos / 2; coluna < numMovimentos; coluna++) {
				populacaoIntermediaria[i][coluna] = populacao[mae][coluna];
				if (i != numMovimentos - 1)
					populacaoIntermediaria[i + 1][coluna] = populacao[pai][coluna];
			}

			i = i + 2;
		}

	}

	/**
	 * A linha gerada randomicamente eh selecionada com base na que teve melhor
	 * aptidao
	 */
	public int torneio(int[] aptidoes) {
		int linhaUm = rng.nextInt(numMovimentos);
		int linhaDois = rng.nextInt(numMovimentos);

		if (aptidoes[linhaUm] > aptidoes[linhaDois]) {
			return linhaUm;
		}
		return linhaDois;
	}

	public void atribuiPrimeiraLinhaPopulacaoIntermediaria(Movimento[][] populacao,
			Movimento[][] populacaoIntermediaria, int[] aptidoes, int[] aptidoesIntermediarias, int option) {
		int melhorLinha = identificaMelhorLinha(aptidoes);
		aptidoesIntermediarias[0] = aptidoes[melhorLinha];
		for (int i = 0; i < populacao[0].length; i++) {
			populacaoIntermediaria[0][i] = populacao[melhorLinha][i];
		}
		aptidoes[0] = aptidoes[melhorLinha];
		if (option == 1) {
			System.out.println("Melhor cromossomo: " + aptidoes[0] + " " + Arrays.toString(populacaoIntermediaria[0]));
		}
	}

	private int identificaMelhorLinha(int[] aptidoes) {
		int melhorLinha = 0;
		for (int i = 1; i < aptidoes.length; i++) {
			if (aptidoes[i] > aptidoes[melhorLinha])
				melhorLinha = i;
		}
		return melhorLinha;
	}

	public int[][] montarLabirinto() {
		//// 9 : entrada/saida; 8: moeda; 1:parede; 0:caminho livre
		int[][] labirinto = new int[10][10];
		try {
			Scanner in = new Scanner(new FileReader("labirintoNovo.txt"));
			int linha = 0;
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] valores = line.split(" ");
				for (int i = 0; i < labirinto[0].length; i++) {
					if (valores[i].equals("E")) {
						labirinto[linha][i] = ENTRADA;
					} else if (valores[i].equals("S")) {
						labirinto[linha][i] = SAIDA;
					} else if (valores[i].equals("M")) { // moeda
						labirinto[linha][i] = MOEDA;
					} else { // caminho ou parede
						labirinto[linha][i] = Integer.parseInt(valores[i]);
					}
				}
				linha++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo nao encontrado!");
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

	public void printPopulacao(double[][] populacao, double[] aptidoes) {
		System.out.println("Populacao gerada:");
		for (int i = 0; i < populacao.length; i++) {
			System.out.println(i + " " + Arrays.toString(populacao[i]) + " Aptidao = " + aptidoes[i]);

		}
	}

	public void geraPopulacaoInicial(double[][] populacao) {
		System.out.println();
		for (int i = 0; i < populacao.length; i++) {
			for (int j = 0; j < populacao.length; j++) {
				double randomNum = rng.nextDouble();
				if(rng.nextBoolean()) randomNum = randomNum * -1; //TODO: validar se é necessario esse passo
				populacao[i][j] = randomNum;
			}
		}
	}

	public void atribuiAptidao(double[][] populacao, int[][] labirinto, double[] aptidoes, int option, int geracao) {
		int[] posicaoAtual = { 0, 0 };// inicio labirinto
		int[] posicaoAux;

		ArrayList<int[]> movimentacao = new ArrayList<>();
		movimentacao.add(posicaoAtual);
		for (int i = 0; i < populacao.length; i++) { //cada cromossomo da população
			int nroMoedas = 0;
			int qtdCelulasCaminhadas = 0;
			posicaoAux = new int[] { 0, 0 };
			posicaoAtual = new int[] { 0, 0 };
			TestaRede rede = TestaRede.carregaCromossomo(labirinto, populacao[i]);  //carrega cromossomo
			
			for (int j = 0; j < populacao.length; j++) { // realiza movimentos
		        double[] percepcao = rede.entorno(posicaoAtual[0], posicaoAtual[1]);
				int melhorNeuronio = rede.executaMovimento(rede, labirinto, percepcao); //executa RN pra ver o movimento escolhido
				Movimento mvtoEscolhido = Movimento.getMovimentoByNeuronio(melhorNeuronio); //mvto escolhido				
				posicaoAux = realizaMovimento(posicaoAtual, mvtoEscolhido, labirinto); //realiza movimento
				System.out.println("movimento escolhido: " + mvtoEscolhido.descricao.toString());
				if (!posicaoAux.equals(posicaoAtual) && !movimentoJaRealizado(posicaoAux, movimentacao)) { 
					//consegue andar
					qtdCelulasCaminhadas++;
					nroMoedas = nroMoedas + (labirinto[posicaoAtual[0]][posicaoAtual[1]] == MOEDA ? 1 : 0);
					posicaoAtual = posicaoAux;
					movimentacao.add(new int[] { posicaoAtual[0], posicaoAtual[1] });
					double totalPontos = this.calculaPontos(nroMoedas, qtdCelulasCaminhadas, CAMINHO, geracao, 0); 
					System.out.println("Total pontos" + totalPontos);
					aptidoes[i] = totalPontos;
					this.validaResultado(posicaoAtual, movimentacao, labirinto, totalPontos);
				} else {
					//nao conseguiu andar logo mata essa tentativa, gravando a aptidão alcançada
					aptidoes[i] = this.calculaPontos(nroMoedas, qtdCelulasCaminhadas, PAREDE, geracao, 0); 
					break;
				}
			}
			movimentacao = new ArrayList<>();

		}

		if (option == 2) {
			printPopulacao(populacao, aptidoes);
		}
	}

	private double calculaPontos(int nroMoedas, int qtdCelulasCaminhadas, int parede, int ciclos, int achouSaida) {
		//parede -> 1 bateu / 0 n bateu
		//ciclos -> quanto menos melhor -> 		
		//achouSaida -> -1 n achou / 1 achou
		
		return (nroMoedas * 40) + (qtdCelulasCaminhadas * 20) + (parede * -1000) + (achouSaida * 1000) + (achouSaida
				== 1 ? ciclos * 0.001 : 0);
	}

	public boolean movimentoJaRealizado(int[] posicaoAtual, ArrayList<int[]> movimentacao) {
		for (int[] mov : movimentacao) {
			if (mov[0] == posicaoAtual[0] && mov[1] == posicaoAtual[1]) {
				return true;
			}
		}
		return false;
	}

	private void validaResultado(int[] posicaoAtual, ArrayList<int[]> movimentacao, int[][] labirinto, double totalPontos) {
		if (posicaoAtual[0] == 9 && posicaoAtual[1] == 9) {
			movimentacao.add(0, new int[] { 0, 0 });
			System.out.println("Encontrou a saida do labirinto!");
			System.out.println("Total de pontos: "+ totalPontos);
			for (int i = 0; i < new HashSet<>(movimentacao).size(); i++) {
				if (i % 20 == 0)
					System.out.println(Arrays.toString(movimentacao.get(i)));
				else
					System.out.print(Arrays.toString(movimentacao.get(i)));
			}

			guardaResultado(movimentacao, labirinto);
			System.exit(1);
		}

	}

	private void guardaResultado(ArrayList<int[]> movimentacao, int[][] labirinto) {
		for (int i = 0; i < movimentacao.size(); i++) {
			labirinto[movimentacao.get(i)[0]][movimentacao.get(i)[1]] = 2;
		}

		System.out.println();
		System.out.println("Labirinto Resultado!");
		printLabirinto(labirinto);
		gravaLabirintoResultado(labirinto);
	}

	private void gravaLabirintoResultado(int[][] labirintoResultado) {
		BufferedWriter buffWrite;
		try {
			buffWrite = new BufferedWriter(new FileWriter("resultadoGenetico.txt"));

			for (int i = 0; i < labirintoResultado.length; i++) {
				for (int j = 0; j < labirintoResultado.length; j++) {
					if (labirintoResultado[i][j] == 2) {
						buffWrite.append("x ");
					} else
						buffWrite.append(labirintoResultado[i][j] + " ");
				}
				buffWrite.append("\n");
			}

			buffWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int[] realizaMovimento(int[] posicaoAtual, Movimento movimento, int[][] labirinto) {
		int[] posicaoNova = { 0, 0 };
		switch (movimento) {
		case C: {
			if (posicaoAtual[0] - 1 >= 0 && labirinto[posicaoAtual[0] - 1][posicaoAtual[1]] != parede) {
				posicaoNova[0] = posicaoAtual[0] - 1;
				posicaoNova[1] = posicaoAtual[1];
				return posicaoNova;
			}
		}
			break;
		case B: {
			if (posicaoAtual[0] + 1 < tamanho && labirinto[posicaoAtual[0] + 1][posicaoAtual[1]] != parede) {
				posicaoNova[0] = posicaoAtual[0] + 1;
				posicaoNova[1] = posicaoAtual[1];
				return posicaoNova;
			}
		}
			break;
		case E: {
			if (posicaoAtual[1] - 1 >= 0 && labirinto[posicaoAtual[0]][posicaoAtual[1] - 1] != parede) {
				posicaoNova[0] = posicaoAtual[0];
				posicaoNova[1] = posicaoAtual[1] - 1;
				return posicaoNova;
			}
		}
			break;
		case D: {
			if (posicaoAtual[1] + 1 < tamanho && labirinto[posicaoAtual[0]][posicaoAtual[1] + 1] != parede) {
				posicaoNova[0] = posicaoAtual[0];
				posicaoNova[1] = posicaoAtual[1] + 1;
				return posicaoNova;
			}
		}
			break;
		default:
			return posicaoAtual;
		}
		return posicaoAtual;
	}
}
