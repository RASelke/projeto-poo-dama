package visualizacao;

import modelo.Cor;
import modelo.Peca;
import modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JogoDamasGUI extends JFrame {
    private Tabuleiro tabuleiro;
    private JPanel painelTabuleiro;
    private JButton[][] botoes; // Usaremos botões para representar as casas
    
    // Controle de cliques
    private int linhaOrigem = -1, colOrigem = -1;
    private boolean primeiraSelecao = true;

    public JogoDamasGUI() {
        tabuleiro = new Tabuleiro();
        botoes = new JButton[8][8];

        setTitle("Jogo de Damas - Projeto POO");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        painelTabuleiro = new JPanel(new GridLayout(8, 8));
        inicializarInterface();
        add(painelTabuleiro);
        
        setVisible(true);
    }

    private void inicializarInterface() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = new JButton();
                // Define a cor do fundo (tabuleiro de xadrez)
                if ((i + j) % 2 == 0) btn.setBackground(Color.LIGHT_GRAY);
                else btn.setBackground(new Color(139, 69, 19)); // Marrom escuro

                final int r = i;
                final int c = j;

                btn.addActionListener(e -> processarClique(r, c));
                
                botoes[i][j] = btn;
                painelTabuleiro.add(btn);
            }
        }
        atualizarTabuleiroGrafico();
    }

    private void processarClique(int linha, int col) {
        if (primeiraSelecao) {
            // Selecionando a peça para mover
            Peca p = tabuleiro.getPeca(linha, col);
            if (p != null && p.getCor() == tabuleiro.getVezAtual()) {
                linhaOrigem = linha;
                colOrigem = col;
                primeiraSelecao = false;
                botoes[linha][col].setBackground(Color.YELLOW); // Highlight
            }
        } else {
            // Tentando mover para o destino
            boolean moveu = tabuleiro.tentarMover(linhaOrigem, colOrigem, linha, col);
            if (moveu) {
                atualizarTabuleiroGrafico();
            } else {
                JOptionPane.showMessageDialog(this, "Movimento Inválido!");
                // Reseta a cor da seleção anterior
                if ((linhaOrigem + colOrigem) % 2 != 0) 
                    botoes[linhaOrigem][colOrigem].setBackground(new Color(139, 69, 19));
            }
            primeiraSelecao = true;
            linhaOrigem = -1;
            colOrigem = -1;
            recolorirTabuleiro();
        }
    }
    
    private void recolorirTabuleiro() {
         for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) botoes[i][j].setBackground(Color.LIGHT_GRAY);
                else botoes[i][j].setBackground(new Color(139, 69, 19));
            }
         }
    }

    private void atualizarTabuleiroGrafico() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca p = tabuleiro.getPeca(i, j);
                if (p != null) {
                    // Desenhando a peça como texto (●) para simplificar, 
                    // mas mudando a cor da fonte
                    botoes[i][j].setText(p.isDama() ? "👑" : "●");
                    botoes[i][j].setForeground(p.getCor() == Cor.BRANCA ? Color.WHITE : Color.RED);
                    botoes[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                } else {
                    botoes[i][j].setText("");
                }
            }
        }
    }

    public static void main(String[] args) {
        // SwingUtilities garante que a GUI rode na Thread correta
        SwingUtilities.invokeLater(() -> new JogoDamasGUI());
    }
}