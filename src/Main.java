import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Qual algoritmo deseja executar?");
        System.out.println("1 - Genetico rapido");
        System.out.println("2 - Genetico detalhado"); 
   //     Scanner in = new Scanner(System.in);
        int option = 1; //provisorio pra facilitar os testes
        if (option == 1) {
            executarGenetico(1);
        } else if (option == 2) {
            executarGenetico(2);
        } else {
            System.out.println("Opcao invalida");
        }
    }

    public static void executarGenetico(int option) {
        Genetico genetico = new Genetico(50000, 108, 1 ,10);

        int[][] labirinto = genetico.montarLabirinto();
        double[][] populacao = new double[genetico.numMovimentos][genetico.numMovimentos];
        double[] aptidoes = new double[populacao.length];
        double[][] populacaoIntermediaria = new double[genetico.numMovimentos][genetico.numMovimentos];
        double[] aptidoesIntermediarias = new double[populacao.length];
        genetico.geraPopulacaoInicial(populacao);
//
//        int geracao;
//        for (geracao = 0; geracao < genetico.numGeracoes; geracao++) {
            genetico.atribuiAptidao(populacao, labirinto, aptidoes, option);
//            genetico.atribuiPrimeiraLinhaPopulacaoIntermediaria(populacao, populacaoIntermediaria, aptidoes,
//                    aptidoesIntermediarias, option);
//
//            genetico.crossOver(populacao, populacaoIntermediaria, aptidoes);
//            genetico.mutacao(populacaoIntermediaria, 5);
//    		
//            populacao = populacaoIntermediaria;
//            aptidoes = aptidoesIntermediarias;
//        }
//        System.out.println(geracao + " Gerações");
//        System.out.println("Solucao final nao encontrada para " + genetico.numGeracoes + " geracoes e " + genetico.numMovimentos
//                + " movimentos por cromossomo");
    }
}
