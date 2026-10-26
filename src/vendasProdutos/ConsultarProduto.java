package vendasProdutos;

import conexao.ConectionFactory;
import telas.SessaoUsuario;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ConsultarProduto extends JInternalFrame {

    private JTextField campoPesquisar;

    private JButton pesquisarButton;
    private JButton limparButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton fecharButton;

    private JTable tabelaConsulta;

    private DefaultTableModel modeloTabela;

    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public ConsultarProduto() {

        super(
                "Hotel Imperial - Consulta de Produtos",
                true,   // pode fechar
                true,   // pode maximizar
                false,  // não pode minimizar
                true    // pode redimensionar
        );

        criarTela();

        setDefaultCloseOperation(
                JInternalFrame.DISPOSE_ON_CLOSE
        );

        setSize(1000, 650);

        aplicarPermissoes();

        pesquisarProdutos("");
    }

    // =========================================================
    // CRIAR TELA
    // =========================================================

    private void criarTela() {

        JPanel principal =
                new JPanel(new BorderLayout(10, 10));

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        // =====================================================
        // CABEÇALHO
        // =====================================================

        JPanel painelTopo =
                new JPanel(new BorderLayout(10, 10));

        JLabel titulo =
                new JLabel("CONSULTA DE PRODUTOS");

        titulo.setFont(
                new Font(
                        "Arial Black",
                        Font.BOLD,
                        26
                )
        );

        titulo.setHorizontalAlignment(
                JLabel.CENTER
        );

        painelTopo.add(
                titulo,
                BorderLayout.NORTH
        );

        // =====================================================
        // PESQUISA
        // =====================================================

        JPanel painelPesquisa =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        JLabel labelPesquisar =
                new JLabel("Pesquisar:");

        labelPesquisar.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        campoPesquisar =
                new JTextField(30);

        campoPesquisar.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        pesquisarButton =
                new JButton("PESQUISAR");

        limparButton =
                new JButton("LIMPAR");

        painelPesquisa.add(
                labelPesquisar
        );

        painelPesquisa.add(
                campoPesquisar
        );

        painelPesquisa.add(
                pesquisarButton
        );

        painelPesquisa.add(
                limparButton
        );

        painelTopo.add(
                painelPesquisa,
                BorderLayout.CENTER
        );

        principal.add(
                painelTopo,
                BorderLayout.NORTH
        );

        // =====================================================
        // TABELA
        // =====================================================

        modeloTabela =
                new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                                "ID",
                                "NOME",
                                "CATEGORIA",
                                "UNIDADE",
                                "PREÇO CUSTO",
                                "PREÇO VENDA",
                                "ESTOQUE",
                                "ESTOQUE MÍN.",
                                "ATIVO"
                        }
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tabelaConsulta =
                new JTable(modeloTabela);

        tabelaConsulta.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        tabelaConsulta.setRowHeight(25);

        tabelaConsulta.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabelaConsulta.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );

        tabelaConsulta.setAutoCreateRowSorter(true);

        JScrollPane scrollTabela =
                new JScrollPane(
                        tabelaConsulta
                );

        principal.add(
                scrollTabela,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        editarButton =
                new JButton("EDITAR");

        excluirButton =
                new JButton("EXCLUIR");

        fecharButton =
                new JButton("FECHAR");

        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                8
                        )
                );

        painelBotoes.add(
                editarButton
        );

        painelBotoes.add(
                excluirButton
        );

        painelBotoes.add(
                fecharButton
        );

        principal.add(
                painelBotoes,
                BorderLayout.SOUTH
        );

        add(principal);

        // =====================================================
        // EVENTOS
        // =====================================================

        pesquisarButton.addActionListener(
                e -> pesquisarProdutos(
                        campoPesquisar.getText()
                )
        );

        limparButton.addActionListener(
                e -> {

                    campoPesquisar.setText("");

                    pesquisarProdutos("");

                    campoPesquisar.requestFocus();
                }
        );

        fecharButton.addActionListener(
                e -> dispose()
        );

        editarButton.addActionListener(
                e -> editarProduto()
        );

        excluirButton.addActionListener(
                e -> excluirProduto()
        );

        // =====================================================
        // ENTER
        // =====================================================

        campoPesquisar.addActionListener(
                e -> pesquisarProdutos(
                        campoPesquisar.getText()
                )
        );

        // =====================================================
        // PESQUISA AUTOMÁTICA
        // =====================================================

        campoPesquisar.addKeyListener(
                new java.awt.event.KeyAdapter() {

                    @Override
                    public void keyReleased(
                            java.awt.event.KeyEvent evt
                    ) {

                        pesquisarProdutos(
                                campoPesquisar.getText()
                        );
                    }
                }
        );

        // =====================================================
        // DUPLO CLIQUE
        // =====================================================

        tabelaConsulta.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent evt
                    ) {

                        if (evt.getClickCount() == 2) {

                            editarProduto();
                        }
                    }
                }
        );
    }

        // =========================================================
        // PERMISSÕES
        // =========================================================

        private void aplicarPermissoes() {

            String grau = SessaoUsuario.getGrauAcesso();

            System.out.println(
                    "USUÁRIO: " + SessaoUsuario.getNomeUsuario()
                    + " | GRAU: [" + grau + "]"
            );

            if (SessaoUsuario.isConsultor()) {

                editarButton.setVisible(false);
                excluirButton.setVisible(false);

            } else if (SessaoUsuario.isGerente()) {

                editarButton.setVisible(true);
                excluirButton.setVisible(false);

            } else if (SessaoUsuario.isAdmin()) {

                editarButton.setVisible(true);
                excluirButton.setVisible(true);

            } else {

                // Se não houver um nível reconhecido,
                // bloqueia as ações por segurança.
                editarButton.setVisible(false);
                excluirButton.setVisible(false);
            }

            // Atualiza a interface depois de esconder os botões
            editarButton.getParent().revalidate();
            editarButton.getParent().repaint();
        }

    // =========================================================
    // PESQUISAR PRODUTOS
    // =========================================================

    public void pesquisarProdutos(
            String campo
    ) {

        modeloTabela.setRowCount(0);

        String sql =
                "SELECT "
                + "id_produto, "
                + "nome, "
                + "categoria, "
                + "unidade, "
                + "preco_custo, "
                + "preco_venda, "
                + "estoque, "
                + "estoque_minimo, "
                + "ativo "
                + "FROM produtos "
                + "WHERE CAST(id_produto AS TEXT) ILIKE ? "
                + "OR nome ILIKE ? "
                + "OR COALESCE(descricao, '') ILIKE ? "
                + "OR categoria ILIKE ? "
                + "OR unidade ILIKE ? "
                + "ORDER BY id_produto";

        try (
                Connection conn =
                        ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            String parametro =
                    "%" + campo.trim() + "%";

            for (int i = 1; i <= 5; i++) {

                pst.setString(
                        i,
                        parametro
                );
            }

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                double precoCusto =
                        rs.getDouble(
                                "preco_custo"
                        );

                double precoVenda =
                        rs.getDouble(
                                "preco_venda"
                        );

                boolean ativo =
                        rs.getBoolean(
                                "ativo"
                        );

                modeloTabela.addRow(
                        new Object[]{

                                rs.getInt(
                                        "id_produto"
                                ),

                                rs.getString(
                                        "nome"
                                ),

                                rs.getString(
                                        "categoria"
                                ),

                                rs.getString(
                                        "unidade"
                                ),

                                String.format(
                                        "R$ %.2f",
                                        precoCusto
                                ),

                                String.format(
                                        "R$ %.2f",
                                        precoVenda
                                ),

                                rs.getInt(
                                        "estoque"
                                ),

                                rs.getInt(
                                        "estoque_minimo"
                                ),

                                ativo
                                        ? "Sim"
                                        : "Não"
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Erro ao buscar produtos:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // EDITAR PRODUTO
    // =========================================================

    private void editarProduto() {

        // Segurança extra.
        // Mesmo que o botão esteja oculto,
        // o consultor não consegue editar.

        if (SessaoUsuario.isConsultor()) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Seu usuário não possui permissão para editar produtos.",
                    "Acesso negado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linha =
                tabelaConsulta.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Selecione um produto para editar.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linhaModelo =
                tabelaConsulta.convertRowIndexToModel(
                        linha
                );

        int idProduto =
                Integer.parseInt(
                        modeloTabela
                                .getValueAt(
                                        linhaModelo,
                                        0
                                )
                                .toString()
                );

        DetalheProduto tela =
                new DetalheProduto(
                        idProduto,
                        () -> pesquisarProdutos(
                                campoPesquisar.getText()
                        )
                );

        tela.setLocationRelativeTo(null);

        tela.setVisible(true);
    }

    // =========================================================
    // EXCLUIR PRODUTO
    // =========================================================

    private void excluirProduto() {

        // Somente ADMIN pode excluir.
        if (!SessaoUsuario.isAdmin()) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Somente o administrador pode excluir produtos.",
                    "Acesso negado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linha =
                tabelaConsulta.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Selecione um produto para excluir.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linhaModelo =
                tabelaConsulta.convertRowIndexToModel(
                        linha
                );

        int idProduto =
                Integer.parseInt(
                        modeloTabela
                                .getValueAt(
                                        linhaModelo,
                                        0
                                )
                                .toString()
                );

        String nomeProduto =
                modeloTabela
                        .getValueAt(
                                linhaModelo,
                                1
                        )
                        .toString();

        int resposta =
                JOptionPane.showInternalConfirmDialog(
                        this,
                        "Deseja realmente excluir o produto:\n\n"
                        + nomeProduto
                        + "?",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (resposta != JOptionPane.YES_OPTION) {

            return;
        }

        String sql =
                "DELETE FROM produtos "
                + "WHERE id_produto = ?";

        try (
                Connection conn =
                        ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    idProduto
            );

            int resultado =
                    pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showInternalMessageDialog(
                        this,
                        "Produto excluído com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                pesquisarProdutos(
                        campoPesquisar.getText()
                );
            }

        } catch (Exception e) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Erro ao excluir produto:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}