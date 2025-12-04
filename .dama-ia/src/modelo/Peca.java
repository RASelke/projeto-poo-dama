package modelo;

public abstract class Peca {
    private Cor cor;
    private boolean isDama;

    public Peca(Cor cor) {
        this.cor = cor;
        this.isDama = false;
    }

    public Cor getCor() { return cor; }
    
    public boolean isDama() { return isDama; }
    
    public void virarDama() { this.isDama = true; }

    // Método abstrato: Força as classes filhas a implementarem sua própria regra
    public abstract boolean movimentoValido(int linhaOrigem, int colOrigem, int linhaDestino, int colDestino);
}