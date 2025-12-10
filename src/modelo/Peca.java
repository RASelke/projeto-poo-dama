package modelo;

public abstract class Peca {
    private Cor cor;

    public Peca(Cor cor) {
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    // ABSTRAÇÃO: Obriga as filhas a definirem sua regra de movimento
    public abstract boolean isMovimentoBasicoValido(int deltaL, int deltaC, int sentidoLinha);
    
    // Polimorfismo visual
    public abstract String getSimbolo();
}