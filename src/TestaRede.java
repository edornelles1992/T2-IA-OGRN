
import java.util.Random;
public class TestaRede
{
    private int[][]labirinto;
    private Rede rn;
    private int linhaSaida, colunaSaida;
    
    public TestaRede(int[][] labirinto, double[] movimentosLabirinto){
    	//9 : entrada/saida;   8: moeda;    1:parede;  0:caminho livre
        this.labirinto = labirinto;
        linhaSaida = colunaSaida = 9;                          //Coordenadas da célula de saída
        
        //Configurações da rede
        configuraRede(movimentosLabirinto);
    }
    
    public void configuraRede(double[] movimentosLabirinto){
         rn = new Rede(8,4);  //topologia da rede: 8 neurônios na camada oculta e 4 na de saída
        
        //Simulando um cromossomo - gerando pesos randomicamente
        Random gera = new Random(); //TODO: aqui entraria os cromossomos do algoritmo genetico
        int totalPesos = 9 * 8 + 9 * 4;  //9= 8 entradas + bias , 8 neuronios na camada oculta e 4 na camada de saída
        double []cromossomo = new double[totalPesos];
        
        for(int i=0; i<cromossomo.length; i++){
            cromossomo[i]=gera.nextDouble();
            if(gera.nextBoolean()) cromossomo[i] = cromossomo[i] * -1;
           // System.out.print(cromossomo[i] + " ");
        }
       // System.out.println();
        
        //Setando os pesos na rede
        rn.setPesosNaRede(8, cromossomo);
    }
    
    public double[] entorno(int linhaAgente, int colunaAgente){
        double [] visao = new double[8];
        int ind=0;
        //buscando percepção
        if(linhaAgente-1 <0) visao[ind] = 1;                        //em cima
        else visao[ind] = labirinto[linhaAgente-1][colunaAgente];    //conteúdo célula
        ind++;
        visao[ind] = distancia(linhaAgente-1,colunaAgente,linhaSaida,colunaSaida);  //distancia da saída
        ind++;
        
        if(colunaAgente-1 <0) visao[ind] = 1;                        //esquerda
        else visao[ind] = labirinto[linhaAgente][colunaAgente-1];    //conteúdo célula
        ind++;
        visao[ind] = distancia(linhaAgente,colunaAgente-1,linhaSaida,colunaSaida);  //distancia da saída
        ind++;
        
        if(linhaAgente+1 >= labirinto.length) visao[ind] = 1;                        //abaixo
        else visao[ind] = labirinto[linhaAgente+1][colunaAgente];    //conteúdo célula
        ind++;
        visao[ind] = distancia(linhaAgente+1,colunaAgente,linhaSaida,colunaSaida);  //distancia da saída
        ind++;
        
        if(colunaAgente+1 >= labirinto[0].length) visao[ind] = 1;                        //direita
        else visao[ind] = labirinto[linhaAgente][colunaAgente+1];    //conteúdo célula
        ind++;
        visao[ind] = distancia(linhaAgente,colunaAgente+1,linhaSaida,colunaSaida);  //distancia da saída
        ind++;
        return visao;
    }
    
    public int distancia(int linhaOrigem, int colunaOrigem, int linhaDestino, int colunaDestino){
        return Math.abs(linhaOrigem - linhaDestino) + Math.abs(colunaOrigem-colunaDestino);
    }
    
    public static TestaRede carregaCromossomo(int[][] labirinto, double[] cromossomo){
    	return new TestaRede(labirinto, cromossomo);
    }
    
    public int executaMovimento(TestaRede teste, int[][] labirinto, double[] percepcao){
        
        System.out.println("Teste da percepção do agente - O que ele vê e a distância");
        //Exibe o que o agente está vendo
        for(int i=0; i<percepcao.length; i++){
            System.out.print(percepcao[i]+ " ");
        }
        System.out.println();
        
        //Exibe e testa a rede neural
        System.out.println("Rede Neural - Pesos: ");
        System.out.println(teste.rn);
        
        //Exibe um exemplo de propagação : saida dos neurônios da camada de saída
        double[]saida = teste.rn.propagacao(percepcao);
        System.out.println("Rede Neural - Camada de Saida: Valor de Y");
        double melhorSaida = saida[0];
        int melhorNeuronio = 0;
        for(int i=0; i<saida.length; i++){
            System.out.println("Neuronio " + i + " : " + saida[i]);
            if (saida[i] > melhorSaida) {
            	melhorSaida = saida[i];
            	melhorNeuronio = i;
            }
        }
        return melhorNeuronio;
    }
    
    
}
