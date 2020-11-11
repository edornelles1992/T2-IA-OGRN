
public class Nodo {

	public int linha;
	public int coluna;
	private Nodo ant;
	private boolean visitado;

	public Nodo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
		ant = null;
		visitado = false;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	public void setAnt(Nodo n) {
		ant = n;
	}

	public Nodo getAnt() {
		return ant;
	}


	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}

}
