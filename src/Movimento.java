
public enum Movimento {
	C(1, "CIMA"), B(2, "BAIXO"), E(3, "ESQUERDA"), D(4, "DIREITA");

	public final double valor;
	public final String descricao;

	private Movimento(double valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public double getValue() {
		return valor;
	}

	public static Movimento getMovimentoByValue(int valor) {
		switch (valor) {
		case 1:
			return C;
		case 2:
			return B;
		case 3:
			return E;
		case 4:
			return D;
		default:
			return null;
		}
	}
}
