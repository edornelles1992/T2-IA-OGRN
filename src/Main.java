import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Qual algoritmo deseja executar?");
        System.out.println("1 - Genetico rapido");
        System.out.println("2 - Genetico detalhado");
        System.out.println("3 - A*");
        Scanner in = new Scanner(System.in);
        int option = in.nextInt();
        if (option == 1) {
            executarGenetico(1);
        } else if (option == 2) {
            executarGenetico(2);
        } else if (option == 3) {
            executarAEstrela();
        } else {
            System.out.println("Opcao invalida");
        }
    }

    public static void executarGenetico(int option) {
        Genetico genetico = new Genetico(50000, 80, 1 ,12);

        int[][] labirinto = genetico.montarLabirinto();
        Movimento[][] populacao = new Movimento[genetico.numMovimentos][genetico.numMovimentos];
        int[] aptidoes = new int[populacao.length];
        Movimento[][] populacaoIntermediaria = new Movimento[genetico.numMovimentos][genetico.numMovimentos];
        int[] aptidoesIntermediarias = new int[populacao.length];
        genetico.geraPopulacaoInicial(populacao);

        int geracao;
        for (geracao = 0; geracao < genetico.numGeracoes; geracao++) {
            genetico.atribuiAptidao(populacao, labirinto, aptidoes, option);
            genetico.atribuiPrimeiraLinhaPopulacaoIntermediaria(populacao, populacaoIntermediaria, aptidoes,
                    aptidoesIntermediarias, option);

            genetico.crossOver(populacao, populacaoIntermediaria, aptidoes);
            genetico.mutacao(populacaoIntermediaria, 5);
    		
            populacao = populacaoIntermediaria;
            aptidoes = aptidoesIntermediarias;
        }
        System.out.println(geracao + " Gerações");
        System.out.println("Solucao final nao encontrada para " + genetico.numGeracoes + " geracoes e " + genetico.numMovimentos
                + " movimentos por cromossomo");
    }

    public static void executarAEstrela() {
        AEstrela aEstrela = new AEstrela();
        aEstrela.carregaDados();
        System.out.println(aEstrela.encontraCaminho());
    }
}
