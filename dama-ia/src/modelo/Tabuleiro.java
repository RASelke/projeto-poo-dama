package modelo;

public class Tabuleiro {
    private Peca[][] pecas;
    private final int TAMANHO = 8;
    private Cor vezAtual;
    
    // Variáveis de Estado para Captura Múltipla
    private boolean emSequenciaCaptura = false;
    private int linhaFocada = -1, colFocada = -1; // Coordenadas da peça obrigada a jogar

    public Tabuleiro() {
        pecas = new Peca[TAMANHO][TAMANHO];
        vezAtual = Cor.VERMELHA; // Vermelhas começam
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) { // Casas escuras
                    if (i < 3) pecas[i][j] = new Peca(Cor.BRANCA);
                    if (i > 4) pecas[i][j] = new Peca(Cor.VERMELHA);
                }
            }
        }
    }

    public Peca getPeca(int l, int c) {
        if (!dentroDoTabuleiro(l, c)) return null;
        return pecas[l][c];
    }
    
    public Cor getVezAtual() { return vezAtual; }

    // --- LÓGICA PRINCIPAL ---

    public boolean tentarMover(int l1, int c1, int l2, int c2) {
        Peca p = pecas[l1][c1];

        // 1. Validações Iniciais
        if (p == null) return false;
        if (p.getCor() != vezAtual) return false;
        if (pecas[l2][c2] != null) return false; // Destino ocupado
        if (!p.movimentoValido(l1, c1, l2, c2)) return false; // Não é diagonal

        // 2. Trava de Captura Múltipla
        // Se estiver no meio de um combo, SÓ pode mexer a peça focada
        if (emSequenciaCaptura) {
            if (l1 != linhaFocada || c1 != colFocada) return false;
        }

        // 3. Análise do Caminho (Distinguir Movimento de Captura)
        Rota rota = analisarRota(l1, c1, l2, c2, p);

        if (!rota.valida) return false;

        // --- EXECUÇÃO ---

        if (rota.captura) {
            // Realiza Captura
            pecas[l2][c2] = p;
            pecas[l1][c1] = null;
            pecas[rota.lCaptura][rota.cCaptura] = null; // Remove peça inimiga

            // Verifica se pode capturar de novo
            if (podeCapturarNovamente(l2, c2, p)) {
                emSequenciaCaptura = true;
                linhaFocada = l2;
                colFocada = c2;
                // NÃO troca a vez, o jogador deve jogar de novo
            } else {
                finalizarTurno(p, l2);
            }
            return true;
        } else {
            // Movimento Simples (Sem captura)
            if (emSequenciaCaptura) return false; // Não pode desistir do combo no meio
            
            // Regra: Peça comum só anda para frente
            if (!p.isDama()) {
                int direcao = (p.getCor() == Cor.VERMELHA) ? -1 : 1;
                if ((l2 - l1) != direcao) return false; // Tentou voltar sem ser dama
            }
            
            pecas[l2][c2] = p;
            pecas[l1][c1] = null;
            finalizarTurno(p, l2);
            return true;
        }
    }

    private void finalizarTurno(Peca p, int linhaDestino) {
        emSequenciaCaptura = false;
        linhaFocada = -1;
        colFocada = -1;
        
        // Promoção
        if (!p.isDama()) {
            if ((p.getCor() == Cor.VERMELHA && linhaDestino == 0) || 
                (p.getCor() == Cor.BRANCA && linhaDestino == 7)) {
                p.virarDama();
            }
        }
        
        vezAtual = (vezAtual == Cor.VERMELHA) ? Cor.BRANCA : Cor.VERMELHA;
    }

    // --- MÉTODOS AUXILIARES ---

    private class Rota {
        boolean valida = false;
        boolean captura = false;
        int lCaptura, cCaptura;
    }

    private Rota analisarRota(int l1, int c1, int l2, int c2, Peca p) {
        Rota r = new Rota();
        int deltaL = Math.abs(l2 - l1);
        int deltaC = Math.abs(c2 - c1);
        int dirL = (l2 - l1) / deltaL;
        int dirC = (c2 - c1) / deltaC;
        
        // CASO 1: Peça Comum
        if (!p.isDama()) {
            if (deltaL == 1) { // Movimento simples
                r.valida = true;
                r.captura = false;
            } else if (deltaL == 2) { // Tentativa de captura
                int lMeio = l1 + dirL;
                int cMeio = c1 + dirC;
                Peca alvo = pecas[lMeio][cMeio];
                if (alvo != null && alvo.getCor() != p.getCor()) {
                    r.valida = true;
                    r.captura = true;
                    r.lCaptura = lMeio;
                    r.cCaptura = cMeio;
                }
            }
            return r;
        }

        // CASO 2: Dama (Varredura do caminho)
        int obstaculos = 0;
        int lAux = l1 + dirL;
        int cAux = c1 + dirC;

        while (lAux != l2) {
            Peca obstaculo = pecas[lAux][cAux];
            if (obstaculo != null) {
                if (obstaculo.getCor() == p.getCor()) return r; // Bloqueado por amigo
                if (obstaculos > 0) return r; // Não pode pular duas peças
                
                r.captura = true;
                r.lCaptura = lAux;
                r.cCaptura = cAux;
                obstaculos++;
            }
            lAux += dirL;
            cAux += dirC;
        }
        
        // Se chegou aqui, o caminho está livre ou tem 1 captura válida
        r.valida = true;
        return r;
    }

    private boolean podeCapturarNovamente(int l, int c, Peca p) {
        // Verifica as 4 direções diagonais
        int[] dL = {-1, -1, 1, 1};
        int[] dC = {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++) {
            // Simplificação: Verifica captura a 2 casas de distância
            // (Cobre 99% dos casos, inclusive para Dama em combos curtos)
            int destL = l + (dL[i] * 2);
            int destC = c + (dC[i] * 2);
            
            if (dentroDoTabuleiro(destL, destC) && pecas[destL][destC] == null) {
                Rota r = analisarRota(l, c, destL, destC, p);
                if (r.valida && r.captura) return true;
            }
        }
        return false;
    }

    private boolean dentroDoTabuleiro(int l, int c) {
        return l >= 0 && l < TAMANHO && c >= 0 && c < TAMANHO;
    }
}