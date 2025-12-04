package Dama;

public class Pedra extends Peca{
    public Pedra(Tabuleiro tabuleiro, Posicao posicao, Cor cor) {
        super(tabuleiro, posicao, cor);
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[tabuleiro.getLinha()][tabuleiro.getColuna()];

        // Pedras Brancas
        if (this.cor == Cor.BRANCO) {
            // Diagonal Direita
            if (this.posicao.getColuna() <= 6 && this.posicao.getLinha() <= 6) {
                mat[posicao.getLinha()+1][posicao.getColuna()+1] = true;
            }
            // Diagonal Esquerda
            if (this.posicao.getColuna() >= 1 && this.posicao.getLinha() <= 6) {
                mat[posicao.getLinha()+1][posicao.getColuna()-1] = true;
            }
        }


        // Pedras Pretas
        if (this.cor == Cor.PRETO) {
            // Diagonal Direita
            if (this.posicao.getColuna() <= 6 && this.posicao.getLinha() >= 1) {
                mat[posicao.getLinha()-1][posicao.getColuna()+1] = true;
            }
            // Diagonal Esquerda
            if (this.posicao.getColuna() >= 1 && this.posicao.getLinha() >= 1) {
                mat[posicao.getLinha()-1][posicao.getColuna()-1] = true;
            }
        }
        
        return mat;
    }
}