package modelo;

public class Dama extends Peca {

    public Dama(Cor cor) {
        super(cor);
    }

    @Override
    public boolean isMovimentoBasicoValido(int deltaL, int deltaC, int sentidoLinha) {
        // Dama move-se em diagonal livre (qualquer distância)
        return deltaL == deltaC && deltaL > 0;
    }

    @Override
    public String getSimbolo() {
        return "♛";
    }
}