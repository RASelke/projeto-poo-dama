package visualizacao;

import modelo.Cor;
import modelo.Peca;
import modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class JogoDamasGUI extends JFrame {
    private Tabuleiro tabuleiro;
    private JPanel painelTabuleiro;
    private JButton[][] botoes;
    
    private int linhaOrigem = -1, colOrigem = -1;
    private boolean primeiraSelecao = true;

    public JogoDamasGUI() {
        tabuleiro = new Tabuleiro();
        botoes = new JButton[8][8];

        setTitle("Jogo de Damas - Projeto POO");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        
        painelTabuleiro = new JPanel(new GridLayout(8, 8));
        inicializarInterface();
        add(painelTabuleiro);
        
        setVisible(true);
    }

    private void inicializarInterface() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = new JButton();
                btn.setOpaque(true);
                btn.setBorderPainted(false);
                
                // Cores do Tabuleiro
                if ((i + j) % 2 == 0) btn.setBackground(new Color(240, 230, 140)); // Bege claro
                else btn.setBackground(new Color(101, 67, 33)); // Marrom escuro

                final int r = i;
                final int c = j;

                btn.addActionListener(e -> processarClique(r, c));
                
                botoes[i][j] = btn;
                painelTabuleiro.add(btn);
            }
        }
        atualizarVisualizacao();
    }

    private void processarClique(int linha, int col) {
        if (primeiraSelecao) {
            Peca p = tabuleiro.getPeca(linha, col);
            if (p != null && p.getCor() == tabuleiro.getVezAtual()) {
                linhaOrigem = linha;
                colOrigem = col;
                primeiraSelecao = false;
                botoes[linha][col].setBackground(Color.YELLOW); // Highlight na seleção
            }
        } else {
            // Tenta mover
            boolean sucesso = tabuleiro.tentarMover(linhaOrigem, colOrigem, linha, col);
            
            // Reseta cores
            recolorirTabuleiro();
            
            if (!sucesso) {
                // Se clicou na própria peça de novo, cancela seleção. Se clicou errado, avisa.
                if(linha != linhaOrigem || col != colOrigem) {
                    JOptionPane.showMessageDialog(this, "Movimento Inválido!");
                }
            }
            
            primeiraSelecao = true;
            linhaOrigem = -1;
            colOrigem = -1;
            atualizarVisualizacao();
        }
    }
    
    private void recolorirTabuleiro() {
         for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) botoes[i][j].setBackground(new Color(240, 230, 140));
                else botoes[i][j].setBackground(new Color(101, 67, 33));
            }
         }
    }

    private void atualizarVisualizacao() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca p = tabuleiro.getPeca(i, j);
                if (p != null) {
                    // Renderização visual da peça
                    String simbolo = p.isDama() ? "♛" : "●"; 
                    botoes[i][j].setText(simbolo);
                    
                    if (p.getCor() == Cor.BRANCA) {
                        botoes[i][j].setForeground(Color.WHITE);
                    } else {
                        botoes[i][j].setForeground(Color.RED);
                    }
                    botoes[i][j].setFont(new Font("SansSerif", Font.BOLD, 32));
                } else {
                    botoes[i][j].setText("");
                }
            }
        }
        // Atualiza título com a vez
        setTitle("Damas - Vez: " + tabuleiro.getVezAtual());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JogoDamasGUI::new);
    }
}