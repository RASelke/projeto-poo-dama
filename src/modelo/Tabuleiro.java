package modelo;

public class Tabuleiro {
    private Peca[][] pecas;
    private final int TAMANHO = 8;
    private Cor vezAtual;
    
    // Controle de estado para captura múltipla
    private boolean emSequenciaCaptura = false;
    private int linhaFocada = -1, colFocada = -1;

    public Tabuleiro() {
        pecas = new Peca[TAMANHO][TAMANHO];
        vezAtual = Cor.VERMELHA;
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if ((i + j) % 2 != 0) {
                    if (i < 3) pecas[i][j] = new PecaComum(Cor.BRANCA);
                    if (i > 4) pecas[i][j] = new PecaComum(Cor.VERMELHA);
                }
            }
        }
    }

    public Peca getPeca(int l, int c) {
        if (!dentroDoTabuleiro(l, c)) return null;
        return pecas[l][c];
    }
    
    public Cor getVezAtual() { return vezAtual; }

    public boolean tentarMover(int l1, int c1, int l2, int c2) {
        Peca p = pecas[l1][c1];

        // 1. Validações Básicas
        if (p == null) return false;
        if (p.getCor() != vezAtual) return false;
        if (pecas[l2][c2] != null) return false; // Destino ocupado

        // CORREÇÃO DO BUG: Impede divisão por zero em movimentos retos (lado ou vertical)
        if (l1 == l2 || c1 == c2) return false; 

        if (emSequenciaCaptura) {
            if (l1 != linhaFocada || c1 != colFocada) return false;
        }

        // 2. Validação Geométrica (Polimorfismo)
        int deltaL = Math.abs(l2 - l1);
        int deltaC = Math.abs(c2 - c1);
        int sentidoLinha = (l2 - l1) / deltaL; // Agora seguro pois deltaL nunca será 0
        
        // Pergunta para a instância (Dama ou Comum) se ela aceita esse movimento
        if (!p.isMovimentoBasicoValido(deltaL, deltaC, sentidoLinha)) {
             return false;
        }

        // 3. Validação de Rota (Colisões)
        Rota rota = analisarRota(l1, c1, l2, c2, p);
        if (!rota.valida) return false;

        // 4. Execução do Movimento
        if (rota.captura) {
            pecas[l2][c2] = p;
            pecas[l1][c1] = null;
            pecas[rota.lCaptura][rota.cCaptura] = null; // Remove peça comida

            // Verifica Combo
            if (podeCapturarNovamente(l2, c2, p)) {
                emSequenciaCaptura = true;
                linhaFocada = l2;
                colFocada = c2;
            } else {
                finalizarTurno(p, l2, c2);
            }
            return true;
        } else {
            // Movimento sem captura
            if (emSequenciaCaptura) return false; // Proibido se estiver em combo
            
            pecas[l2][c2] = p;
            pecas[l1][c1] = null;
            finalizarTurno(p, l2, c2);
            return true;
        }
    }

    private void finalizarTurno(Peca p, int l, int c) {
        emSequenciaCaptura = false;
        linhaFocada = -1;
        colFocada = -1;
        
        // Verifica Promoção (Transforma Comum em Dama)
        if (p instanceof PecaComum) {
            if ((p.getCor() == Cor.VERMELHA && l == 0) || 
                (p.getCor() == Cor.BRANCA && l == 7)) {
                pecas[l][c] = new Dama(p.getCor());
            }
        }
        
        vezAtual = (vezAtual == Cor.VERMELHA) ? Cor.BRANCA : Cor.VERMELHA;
    }

    // Estrutura auxiliar interna
    private class Rota {
        boolean valida = false;
        boolean captura = false;
        int lCaptura, cCaptura;
    }

    private Rota analisarRota(int l1, int c1, int l2, int c2, Peca p) {
        Rota r = new Rota();
        int deltaL = Math.abs(l2 - l1);
        int dirL = (l2 - l1) / deltaL;
        int dirC = (c2 - c1) / Math.abs(c2 - c1);

        if (p instanceof PecaComum) {
            if (deltaL == 1) { 
                r.valida = true;
            } else if (deltaL == 2) {
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
        } 
        else if (p instanceof Dama) {
            int pecasNoCaminho = 0;
            int lAux = l1 + dirL;
            int cAux = c1 + dirC;

            while (lAux != l2) {
                Peca obstaculo = pecas[lAux][cAux];
                if (obstaculo != null) {
                    if (obstaculo.getCor() == p.getCor()) return r; // Bloqueio amigo
                    if (pecasNoCaminho > 0) return r; // Mais de uma peça
                    
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

    private boolean podeCapturarNovamente(int l, int c, Peca p) {
        int[] d = {-1, 1};
        for (int i : d) {
            for (int j : d) {
                int destL = l + (i * 2);
                int destC = c + (j * 2);
                
                if (dentroDoTabuleiro(destL, destC) && pecas[destL][destC] == null) {
                    // Reutiliza validação polimórfica para saber se a peça "sabe" capturar assim
                    if (p.isMovimentoBasicoValido(2, 2, i)) {
                        Rota r = analisarRota(l, c, destL, destC, p);
                        if (r.valida && r.captura) return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean dentroDoTabuleiro(int l, int c) {
        return l >= 0 && l < TAMANHO && c >= 0 && c < TAMANHO;
    }
}