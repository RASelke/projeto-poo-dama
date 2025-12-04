package Dama;

public abstract class Peca {
    protected Cor cor;
    protected Posicao posicao;
    protected Tabuleiro tabuleiro;

    public Peca(Tabuleiro tabuleiro, Posicao posicao, Cor cor) {
        this.tabuleiro = tabuleiro;
        this.posicao = posicao;
        this.cor = cor;
        tabuleiro.colocarPeca(this, posicao);
    }

    public abstract boolean[][] movimentosPossiveis();
}
