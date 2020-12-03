import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Genetico {

	int numGeracoes;
	int numPesos;
	int parede;
	int tamanho;
	int MOEDA = 8;
	int ENTRADA = 9;
	int SAIDA = 9;
	int PAREDE = 1;
	int CAMINHO = 0;
	int numCromossomos;
	boolean stopAtFirst;
	Random rng;

	public Genetico(int numGeracoes, int numCromossomos,  int numPesos, int parede, int tamanho, boolean stopAtFirst) {
		this.numGeracoes = numGeracoes;
		this.numPesos = numPesos;
		this.numCromossomos = numCromossomos;
		this.parede = parede;
		this.tamanho = tamanho;
		this.stopAtFirst = stopAtFirst;
		this.rng = new Random();
	}

	public void mutacao(double[][] populacaoIntermediaria, int taxaMutacao) {

		for (int i = 0; i < numPesos * taxaMutacao; i++) {
			int linha = rng.nextInt(((numCromossomos - 1) - 1) + 1) + 1;
			int coluna = rng.nextInt(numPesos);

			double randomNum = rng.nextDouble();
			if(rng.nextBoolean()) randomNum = randomNum * -1;
			populacaoIntermediaria[linha][coluna] = randomNum;
		}
	}

	public void crossOver(double[][] populacao, double[][] populacaoIntermediaria, double[] aptidoes) {

		int i = 1; // pula primeira linha
		int pai;
		int mae;

		for (int j = 0; j < (numCromossomos / 2); j++) {

			pai = torneio(aptidoes);
			mae = torneio(aptidoes);
			double[] filho = geraVetorMedio(pai, mae, populacao);

			for (int coluna = 0; coluna < (numPesos / 2); coluna++) {
				populacaoIntermediaria[i][coluna] = filho[j];
				if (i != numCromossomos - 1)
					populacaoIntermediaria[i + 1][coluna] = filho[j];
			}

			for (int coluna = numPesos / 2; coluna < numPesos; coluna++) {
				populacaoIntermediaria[i][coluna] = filho[j];
				if (i != numCromossomos - 1)
					populacaoIntermediaria[i + 1][coluna] = filho[j];
			}

			i = i + 2;
		}
	}

	private double[] geraVetorMedio(int pai, int mae, double[][] populacao) {
		double[] cromossomoPai = populacao[pai];
		double[] cromossomoMae = populacao[mae];
		double[] vetorMedio = new double[cromossomoPai.length];
		for (int i = 0 ; i < vetorMedio.length ; i++) {
			vetorMedio[i] = (cromossomoPai[i] + cromossomoMae[i]) / 2;
		}
		return vetorMedio;
	}

	/**
	 * A linha gerada randomicamente eh selecionada com base na que teve melhor
	 * aptidao
	 */
	public int torneio(double[] aptidoes) {
		int linhaUm = rng.nextInt(numCromossomos);
		int linhaDois = rng.nextInt(numCromossomos);

		if (aptidoes[linhaUm] > aptidoes[linhaDois]) {
			return linhaUm;
		}
		return linhaDois;
	}

	public void atribuiPrimeiraLinhaPopulacaoIntermediaria(double[][] populacao,
			double[][] populacaoIntermediaria, double[] aptidoes, double[] aptidoesIntermediarias, int option, int geracao) {
		int melhorLinha = identificaMelhorLinha(aptidoes); //elitismo
		aptidoesIntermediarias[0] = aptidoes[melhorLinha]; 
		for (int i = 0; i < populacao[0].length; i++) {
			populacaoIntermediaria[0][i] = populacao[melhorLinha][i];
		}
		aptidoes[0] = aptidoes[melhorLinha];
//		if (option == 1) {
//			System.out.println("Geração "+ geracao + " Melhor cromossomo: " + aptidoes[0] + " " + Arrays.toString(populacaoIntermediaria[0]));
//		}
	}

	private int identificaMelhorLinha(double[] aptidoes) {
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
			Scanner in = new Scanner(new FileReader("labirinto.txt"));
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
				} else if (labirinto[i][j] == 8) {
					System.out.print("M ");
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
		for (int i = 0; i < numCromossomos; i++) {
			for (int j = 0; j < numPesos; j++) {
				double randomNum = rng.nextDouble();
				if(rng.nextBoolean()) randomNum = randomNum * -1; //TODO: validar se é necessario esse passo
				populacao[i][j] = randomNum;
			}
		}
	}
	
	public void carregaMelhorEncontrado(double[][] populacao, int[][] labirinto, int maxGeracoes ) {
		int[] posicaoAtual = { 0, 0 };// inicio labirinto
		int[] posicaoAux;
		
		ArrayList<int[]> movimentacao = new ArrayList<>();
		movimentacao.add(posicaoAtual);
		
		double totalPontos = 0;
		int nroMoedas = 0;
		int qtdCelulasCaminhadas = 0;
		posicaoAux = new int[] { 0, 0 };
		posicaoAtual = new int[] { 0, 0 };
		RedeNeural rede = RedeNeural.carregaCromossomo(labirinto, populacao[0]);
		
		for (int gene = 0; gene < populacao.length; gene++) { // realiza movimentos
	        double[] percepcao = rede.entorno(posicaoAtual[0], posicaoAtual[1]);
			int melhorNeuronio = rede.executaPropagacao(rede, labirinto, percepcao); //executa RN pra ver o movimento escolhido
			Movimento mvtoEscolhido = Movimento.getMovimentoByNeuronio(melhorNeuronio); //mvto escolhido				
			posicaoAux = realizaMovimento(posicaoAtual, mvtoEscolhido, labirinto); //realiza movimento
			//System.out.println("movimento escolhido: " + mvtoEscolhido.descricao.toString());
			if (!posicaoAux.equals(posicaoAtual) && !movimentoJaRealizado(posicaoAux, movimentacao)) { 
				//consegue andar
				qtdCelulasCaminhadas++;
				nroMoedas = nroMoedas + (labirinto[posicaoAtual[0]][posicaoAtual[1]] == MOEDA ? 1 : 0);
				posicaoAtual = posicaoAux;
				movimentacao.add(new int[] { posicaoAtual[0], posicaoAtual[1] });
				totalPontos = this.calculaPontos(nroMoedas, qtdCelulasCaminhadas, CAMINHO, maxGeracoes, 0, maxGeracoes); 	
					this.validaResultado(posicaoAtual, movimentacao, labirinto, totalPontos, nroMoedas);
				
			} else {
				//nao conseguiu andar logo mata essa tentativa, gravando a aptidão alcançada
				totalPontos = this.calculaPontos(nroMoedas, qtdCelulasCaminhadas, PAREDE, maxGeracoes, 0, maxGeracoes); 				
				break;
			}
		}
		
		System.out.println(" Resultado com melhor pontuação:");
		System.out.println("Total pontos: " + totalPontos);
		System.out.println("Nro Moedas: " + nroMoedas);
		guardaResultado(movimentacao, labirinto);
		System.exit(1);
	}
	

	public void atribuiAptidao(double[][] populacao, int[][] labirinto, double[] aptidoes, int option, int geracao, int maxGeracoes) {
		int[] posicaoAtual = { 0, 0 };// inicio labirinto
		int[] posicaoAux;
		
		ArrayList<int[]> movimentacao = new ArrayList<>();
		movimentacao.add(posicaoAtual);
		for (int cromossomo = 0; cromossomo < populacao.length; cromossomo++) { //cada cromossomo da população
			double totalPontos = 0;
			int nroMoedas = 0;
			int qtdCelulasCaminhadas = 0;
			posicaoAux = new int[] { 0, 0 };
			posicaoAtual = new int[] { 0, 0 };
			RedeNeural rede = RedeNeural.carregaCromossomo(labirinto, populacao[cromossomo]);  //carrega cromossomo
			
			for (int gene = 0; gene < populacao.length; gene++) { // realiza movimentos
		        double[] percepcao = rede.entorno(posicaoAtual[0], posicaoAtual[1]);
				int melhorNeuronio = rede.executaPropagacao(rede, labirinto, percepcao); //executa RN pra ver o movimento escolhido
				Movimento mvtoEscolhido = Movimento.getMovimentoByNeuronio(melhorNeuronio); //mvto escolhido				
				posicaoAux = realizaMovimento(posicaoAtual, mvtoEscolhido, labirinto); //realiza movimento
				System.out.println("movimento escolhido: " + mvtoEscolhido.descricao.toString());
				if (!posicaoAux.equals(posicaoAtual) && !movimentoJaRealizado(posicaoAux, movimentacao)) { 
					//consegue andar
					qtdCelulasCaminhadas++;
					nroMoedas = nroMoedas + (labirinto[posicaoAtual[0]][posicaoAtual[1]] == MOEDA ? 1 : 0);
					posicaoAtual = posicaoAux;
					movimentacao.add(new int[] { posicaoAtual[0], posicaoAtual[1] });
					totalPontos = this.calculaPontos(nroMoedas, qtdCelulasCaminhadas, CAMINHO, geracao, 0, maxGeracoes); 					
					aptidoes[cromossomo] = totalPontos;
					if (stopAtFirst) {
						this.validaResultado(posicaoAtual, movimentacao, labirinto, totalPontos, nroMoedas);
					} 
				} else {
					//nao conseguiu andar logo mata essa tentativa, gravando a aptidão alcançada
					totalPontos = this.calculaPontos(nroMoedas, qtdCelulasCaminhadas, PAREDE, geracao, 0, maxGeracoes); 
					aptidoes[cromossomo] = totalPontos;
					break;
				}
			}
			System.out.println("Geração "+ geracao + " Cromossomo " + cromossomo + " Total pontos: " + totalPontos);
			movimentacao = new ArrayList<>();
		}

		if (option == 2) {
			printPopulacao(populacao, aptidoes);
		}
	}

	private double calculaPontos(int nroMoedas, int qtdCelulasCaminhadas, int parede, int ciclos, int achouSaida, int maxGeracoes) {
		//parede -> 1 bateu / 0 n bateu
		//ciclos -> quanto menos melhor -> 		
		//achouSaida -> -1 n achou / 1 achou
		
		return (nroMoedas * 40) + (qtdCelulasCaminhadas * 20) + (parede * -99999) + (achouSaida * 100) + ((maxGeracoes - ciclos) * 0.001);
	}

	public boolean movimentoJaRealizado(int[] posicaoAtual, ArrayList<int[]> movimentacao) {
		for (int[] mov : movimentacao) {
			if (mov[0] == posicaoAtual[0] && mov[1] == posicaoAtual[1]) {
				return true;
			}
		}
		return false;
	}

	private void validaResultado(int[] posicaoAtual, ArrayList<int[]> movimentacao, int[][] labirinto, double totalPontos, int nroMoedas) {
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
			System.out.println();
			System.out.println("Total pontos: " + totalPontos);
			System.out.println("Nro Moedas: " + nroMoedas);
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
			buffWrite = new BufferedWriter(new FileWriter("resultado.txt"));

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

	/**
	 * Tenta realizar o movimento, caso consiga ele devolta a nova posição, caso não consiga,
	 * ele devolve a mesma posição, indicando que o agente não conseguiu andar devido a uma parede ou limite do labirinto.
	 */
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
