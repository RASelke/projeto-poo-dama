package modelo;

public class PecaComum extends Peca {

    public PecaComum(Cor cor) {
        super(cor);
    }

    @Override
    public boolean isMovimentoBasicoValido(int deltaL, int deltaC, int sentidoLinha) {
        // Regra 1: Deve ser diagonal (L e C variam igual)
        if (deltaL != deltaC) return false;

        // Regra 2: Movimento Simples (1 casa)
        if (deltaL == 1) {
            // Só pode ir para frente. Vermelha sobe (-1), Branca desce (+1)
            int direcaoObrigatoria = (getCor() == Cor.VERMELHA) ? -1 : 1;
            return sentidoLinha == direcaoObrigatoria;
        }

        // Regra 3: Captura (2 casas) - Pode ir para qualquer lado
        if (deltaL == 2) {
            return true;
        }

        return false;
    }

    @Override
    public String getSimbolo() {
        return "●";
    }
}