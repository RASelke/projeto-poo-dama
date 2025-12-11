package modelo;

public class Rota {
    private boolean valida;
    private boolean captura;
    private int lCaptura;
    private int cCaptura;

    // Construtor
    public Rota() {
        this.valida = false;
        this.captura = false;
    }

    // Getters
    public boolean isValida() {
        return valida;
    }
    public boolean isCaptura() {
        return captura;
    }
    public int getLCaptura() {
        return lCaptura;
    }
    public int getCCaptura() {
        return cCaptura;
    }

    public static Rota analisar(Tabuleiro tab, int l1, int c1, int l2, int c2, Peca p) {
        Rota r = new Rota();
        int deltaL = Math.abs(l2 - l1);
        
        if (deltaL == 0) return r;

        int dirL = (l2 - l1) / deltaL;
        int dirC = (c2 - c1) / Math.abs(c2 - c1);

        
        if (p instanceof PecaComum) {
            if (deltaL == 1) { 
                r.valida = true;
            } else if (deltaL == 2) {
                int lMeio = l1 + dirL;
                int cMeio = c1 + dirC;
                // Usa o método público do tabuleiro, não a matriz privada
                Peca alvo = tab.getPeca(lMeio, cMeio); 
                
                if (alvo != null && alvo.getCor() != p.getCor()) {
                    r.valida = true;
                    r.captura = true;
                    r.lCaptura = lMeio;
                    r.cCaptura = cMeio;
                }
            }
        } else if (p instanceof Dama) {
            int pecasNoCaminho = 0;
            int lAux = l1 + dirL;
            int cAux = c1 + dirC;

            while (lAux != l2) {
                Peca obstaculo = tab.getPeca(lAux, cAux);
                
                if (obstaculo != null) {
                    if (obstaculo.getCor() == p.getCor()) return r; // Bloqueio amigo
                    if (pecasNoCaminho > 0) return r; // Mais de uma peça no caminho
                    
                    r.captura = true;
                    r.lCaptura = lAux;
                    r.cCaptura = cAux;
                    pecasNoCaminho++;
                }
                lAux += dirL;
                cAux += dirC;
            }
            r.valida = true;
        }
        return r;
    }
}