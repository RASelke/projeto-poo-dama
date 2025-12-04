package Dama;

public class Main {
    public static void imprimirTabuleiro(Tabuleiro tabuleiro) {
        for (int i=0; i < tabuleiro.getLinha(); i++) {
           for (int j=0; j < tabuleiro.getColuna(); j++) {
                Peca peca = tabuleiro.getPeca(i, j);
                if (peca == null) {
                    System.out.print("- ");
                } else if (peca != null) {
                    System.out.print("P ");
                }
           }
           System.out.println(); 
        }
    }

    public static void main(String[] args) {
        Tabuleiro tabuleiro = new Tabuleiro(8,8);
        new Pedra(null, new Posicao(7,6), Cor.BRANCO);

        Main.imprimirTabuleiro(tabuleiro);
    }
}
