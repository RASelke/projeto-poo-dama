package Dama;

public class Posicao {
    private int linha;
    private int coluna;

    // Constructor
    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    // Getters and Setters
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


    // toString
    @Override
    public String toString() {
        return "Linha: " + linha + "\nColuna: " + coluna;
    }
}