package modelo;

public class Peca {
    private Cor cor;
    private boolean isDama;

    public Peca(Cor cor) {
        this.cor = cor;
        this.isDama = false;
    }

    public Cor getCor() { return cor; }
    
    public boolean isDama() { return isDama; }
    
    public void virarDama() { this.isDama = true; }

    // Verifica apenas se o movimento é diagonal geometricamente
    public boolean movimentoValido(int l1, int c1, int l2, int c2) {
        int deltaL = Math.abs(l1 - l2);
        int deltaC = Math.abs(c1 - c2);
        
        // Regra básica: deve andar na diagonal (mudança em L igual a mudança em C)
        // e deve ter movido pelo menos uma casa.
        return deltaL == deltaC && deltaL > 0;
    }
}