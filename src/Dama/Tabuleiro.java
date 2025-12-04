package Dama;

public class Tabuleiro {
    private int linha;
    private int coluna;
    private Peca[][] matriz;
    
    // Constructor
    public Tabuleiro(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        this.matriz = new Peca[this.linha][this.coluna];
    }

    // Getters
    public int getLinha() {
        return linha;
    }
    public int getColuna() {
        return coluna;
    }

    public Peca getPeca (int linha, int coluna) {
        return matriz[linha][coluna];
    }


    public void colocarPeca(Peca peca, Posicao posicao) {
        int linhaPosicao = posicao.getLinha();
        int colunaPosicao = posicao.getColuna();

        this.matriz[linhaPosicao][colunaPosicao] = peca;
    }

    public void moverPeca(Posicao origem, Posicao destino) {
        
    }

}