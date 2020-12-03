import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Qual algoritmo deseja executar?");
        System.out.println("1 - Genetico rapido");
        System.out.println("2 - Genetico detalhado");
        boolean stopAtFirst = true; //true = parar na primeira resolução
        Scanner in = new Scanner(System.in);
        int option = in.nextInt();
        if (option == 1) {
            executarGenetico(1, stopAtFirst);
        } else if (option == 2) {
            executarGenetico(2, stopAtFirst);
        } else {
            System.out.println("Opcao invalida");
        }
        in.close();
    }

    public static void executarGenetico(int option, boolean stopAtFirst) {
        Genetico genetico = new Genetico(10000, 100, 108, 1 ,10, stopAtFirst);

        int[][] labirinto = genetico.montarLabirinto();
        double[][] populacao = new double[genetico.numCromossomos][genetico.numPesos];
        double[] aptidoes = new double[populacao.length];
        double[][] populacaoIntermediaria = new double[genetico.numCromossomos][genetico.numPesos];
        double[] aptidoesIntermediarias = new double[populacao.length];
        genetico.geraPopulacaoInicial(populacao);

        int geracao;
        for (geracao = 0; geracao < genetico.numGeracoes; geracao++) {
            genetico.atribuiAptidao(populacao, labirinto, aptidoes, option, geracao, genetico.numGeracoes);
            genetico.atribuiPrimeiraLinhaPopulacaoIntermediaria(populacao, populacaoIntermediaria, aptidoes,
                    aptidoesIntermediarias, option, geracao);
            
            genetico.crossOver(populacao, populacaoIntermediaria, aptidoes);
            genetico.mutacao(populacaoIntermediaria, 20);
    		
            populacao = populacaoIntermediaria;
            aptidoes = aptidoesIntermediarias;
            System.out.println("Geração " + geracao + " Melhor Aptidão: " + aptidoes[0]);
        }
        
        if (stopAtFirst) {
        System.out.println(geracao + " Gerações");
        System.out.println("Solucao final nao encontrada para " + genetico.numGeracoes + " geracoes, " + genetico.numPesos
                + " movimentos por cromossomo, e " + genetico.numCromossomos + " cromossomos");
        } else {        	
        	genetico.carregaMelhorEncontrado(populacao, labirinto, genetico.numGeracoes);
        }
    }
}
