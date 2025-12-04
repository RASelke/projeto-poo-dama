package modelo;

public class PecaDama extends Peca {

    public PecaDama(Cor cor) {
        super(cor);
    }

    @Override
    public boolean movimentoValido(int l1, int c1, int l2, int c2) {
        // Lógica BÁSICA: Move 1 casa na diagonal
        int deltaL = Math.abs(l1 - l2);
        int deltaC = Math.abs(c1 - c2);
        
        // Se for captura (pula 2 casas), a lógica é tratada no Tabuleiro para validar a peça no meio
        // Aqui validamos apenas a geometria do movimento
        
        if (isDama()) {
            // Dama move-se quantas casas quiser na diagonal (simplificado)
            return deltaL == deltaC; 
        } else {
            // Peça comum: Apenas 1 casa para frente (depende da cor)
            // Vermelho "sobe" (index diminui), Branco "desce" (index aumenta)
            int direcao = (getCor() == Cor.VERMELHA) ? -1 : 1;
            
            // Movimento simples
            if (deltaL == 1 && deltaC == 1 && (l2 - l1) == direcao) return true;
            
            // Movimento de captura (pula 2)
            if (deltaL == 2 && deltaC == 2 && (l2 - l1) == (direcao * 2)) return true;
        }
        return false;
    }
}