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
        vezAtual = Cor.VERMELHA; // ou a cor que começa
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
        if (l1 == l2 || c1 == c2) return false; 

        if (emSequenciaCaptura) {
            if (l1 != linhaFocada || c1 != colFocada) return false;
        }

        // 2. Validação Geométrica (Polimorfismo na Peça)
        int deltaL = Math.abs(l2 - l1);
        int deltaC = Math.abs(c2 - c1);
        
        // Proteção contra divisão por zero caso o movimento não seja diagonal
        if (deltaL == 0) return false;
        
        int sentidoLinha = (l2 - l1) / deltaL; 
        
        if (!p.isMovimentoBasicoValido(deltaL, deltaC, sentidoLinha)) {
             return false;
        }

        // 3. Validação de Rota (Delegado para a classe Rota)
        // Passamos 'this' para que a Rota possa ler este tabuleiro
        Rota rota = Rota.analisar(this, l1, c1, l2, c2, p);
        
        // Acesso via Getters (Encapsulamento)
        if (!rota.isValida()) return false;

        // 4. Execução do Movimento
        if (rota.isCaptura()) {
            pecas[l2][c2] = p;
            pecas[l1][c1] = null;
            pecas[rota.getLCaptura()][rota.getCCaptura()] = null; // Remove peça comida

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
        
        // Verifica Promoção
        if (p instanceof PecaComum) {
            if ((p.getCor() == Cor.VERMELHA && l == 0) || 
                (p.getCor() == Cor.BRANCA && l == 7)) {
                pecas[l][c] = new Dama(p.getCor());
            }
        }
        
        vezAtual = (vezAtual == Cor.VERMELHA) ? Cor.BRANCA : Cor.VERMELHA;
    }

    private boolean podeCapturarNovamente(int l, int c, Peca p) {
        int[] d = {-1, 1};
        for (int i : d) {
            for (int j : d) {
                int destL = l + (i * 2);
                int destC = c + (j * 2);
                
                if (dentroDoTabuleiro(destL, destC) && pecas[destL][destC] == null) {
                    if (p.isMovimentoBasicoValido(2, 2, i)) {
                        // Chama a classe Rota externa novamente
                        Rota r = Rota.analisar(this, l, c, destL, destC, p);
                        if (r.isValida() && r.isCaptura()) return true;
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