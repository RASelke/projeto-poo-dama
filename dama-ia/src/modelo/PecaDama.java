package modelo;

public class PecaDama extends Peca {

    public PecaDama(Cor cor) {
        super(cor);
    }

    // Em PecaDama.java ou Peca.java
    @Override
    public boolean movimentoValido(int l1, int c1, int l2, int c2) {
        int deltaL = Math.abs(l1 - l2);
        int deltaC = Math.abs(c1 - c2);
        return deltaL == deltaC && deltaL > 0; // Apenas garante que é diagonal e moveu algo
}
}