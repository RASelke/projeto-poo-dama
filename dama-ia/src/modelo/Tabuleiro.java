package modelo;

public class Tabuleiro {
    private Peca[][] pecas;
    private final int TAMANHO = 8;
    private Cor vezAtual;

    public Tabuleiro() {
        pecas = new Peca[TAMANHO][TAMANHO];
        vezAtual = Cor.VERMELHA; // Vermelhas começam
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        // Preenche o tabuleiro (padrao internacional: casa escura tem peça)
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) { // Casa escura
                    if (i < 3) pecas[i][j] = new PecaDama(Cor.BRANCA);
                    if (i > 4) pecas[i][j] = new PecaDama(Cor.VERMELHA);
                }
            }
        }
    }

    public Peca getPeca(int linha, int coluna) {
        return pecas[linha][coluna];
    }

    public boolean tentarMover(int l1, int c1, int l2, int c2) {
        Peca p = pecas[l1][c1];
        
        // Validações básicas
        if (p == null) return false; // Não tem peça na origem
        if (p.getCor() != vezAtual) return false; // Não é a vez dessa cor
        if (pecas[l2][c2] != null) return false; // Destino ocupado
        
        // Validação Polimórfica (a peça sabe como se move)
        if (!p.movimentoValido(l1, c1, l2, c2)) return false;

        // Lógica de Captura (Simplificada)
        if (Math.abs(l1 - l2) == 2) {
            int lMeio = (l1 + l2) / 2;
            int cMeio = (c1 + c2) / 2;
            Peca capturada = pecas[lMeio][cMeio];
            
            if (capturada == null || capturada.getCor() == p.getCor()) {
                return false; // Não pulou peça inimiga
            }
            pecas[lMeio][cMeio] = null; // Remove a peça
        }

        // Realiza o movimento
        pecas[l2][c2] = p;
        pecas[l1][c1] = null;

        // Promoção para Dama
        if ((p.getCor() == Cor.VERMELHA && l2 == 0) || (p.getCor() == Cor.BRANCA && l2 == 7)) {
            p.virarDama();
        }

        trocarVez();
        return true;
    }

    private void trocarVez() {
        vezAtual = (vezAtual == Cor.VERMELHA) ? Cor.BRANCA : Cor.VERMELHA;
    }
    
    public Cor getVezAtual() { return vezAtual; }
}