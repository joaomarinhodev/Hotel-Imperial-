package vendasProdutos;

import conexao.ConectionFactory;
import telas.SessaoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConsultarServico extends JInternalFrame {

    private JTextField campoPesquisar;
    private JTable tabelaServicos;
    private DefaultTableModel modeloTabela;

    private JButton editarButton;
    private JButton excluirButton;
    private JButton pesquisarButton;
    private JButton limparButton;

    public ConsultarServico() {

        super(
            "Hotel Imperial - Consulta de Serviços",
            true,
            true,
            false,
            true
        );

        criarTela();

        setDefaultCloseOperation(
            JInternalFrame.DISPOSE_ON_CLOSE
        );

        setSize(950, 600);

        aplicarPermissoes();

        pesquisarServicos("");
    }

    private void criarTela() {

        setLayout(new BorderLayout(10, 10));

        // =====================================================
        // PAINEL DE PESQUISA
        // =====================================================

        JPanel painelPesquisa = new JPanel(
            new BorderLayout(10, 10)
        );

        painelPesquisa.setBorder(
            BorderFactory.createEmptyBorder(
                10, 10, 0, 10
            )
        );

        JLabel labelPesquisar = new JLabel(
            "Pesquisar serviço:"
        );

        campoPesquisar = new JTextField();

        pesquisarButton = new JButton("Pesquisar");
        limparButton = new JButton("Limpar");

        JPanel botoesPesquisa = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                5,
                0
            )
        );

        botoesPesquisa.add(pesquisarButton);
        botoesPesquisa.add(limparButton);

        painelPesquisa.add(
            labelPesquisar,
            BorderLayout.WEST
        );

        painelPesquisa.add(
            campoPesquisar,
            BorderLayout.CENTER
        );

        painelPesquisa.add(
            botoesPesquisa,
            BorderLayout.EAST
        );

        add(
            painelPesquisa,
            BorderLayout.NORTH
        );

        // =====================================================
        // TABELA
        // =====================================================

        String[] colunas = {
            "ID",
            "NOME",
            "CATEGORIA",
            "PREÇO",
            "ATIVO"
        };

        modeloTabela = new DefaultTableModel(
            colunas,
            0
        ) {

            @Override
            public boolean isCellEditable(
                int row,
                int column
            ) {
                return false;
            }
        };

        tabelaServicos = new JTable(modeloTabela);

        tabelaServicos.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabelaServicos.setAutoCreateRowSorter(true);

        tabelaServicos.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(60);

        tabelaServicos.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(250);

        tabelaServicos.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(150);

        tabelaServicos.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(100);

        tabelaServicos.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(80);

        JScrollPane scrollTabela =
            new JScrollPane(tabelaServicos);

        add(
            scrollTabela,
            BorderLayout.CENTER
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        JPanel painelBotoes = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                10,
                10
            )
        );

        editarButton = new JButton("Editar");
        excluirButton = new JButton("Excluir");

        painelBotoes.add(editarButton);
        painelBotoes.add(excluirButton);

        add(
            painelBotoes,
            BorderLayout.SOUTH
        );

        // =====================================================
        // EVENTOS
        // =====================================================

        pesquisarButton.addActionListener(e ->
            pesquisarServicos(
                campoPesquisar.getText()
            )
        );

        limparButton.addActionListener(e -> {

            campoPesquisar.setText("");

            pesquisarServicos("");

            campoPesquisar.requestFocus();
        });

        campoPesquisar.addKeyListener(
            new KeyAdapter() {

                @Override
                public void keyReleased(
                    KeyEvent e
                ) {

                    pesquisarServicos(
                        campoPesquisar.getText()
                    );
                }
            }
        );

        campoPesquisar.addActionListener(e ->
            pesquisarServicos(
                campoPesquisar.getText()
            )
        );

        editarButton.addActionListener(e ->
            editarServico()
        );

        excluirButton.addActionListener(e ->
            excluirServico()
        );

        // Duplo clique na tabela
        tabelaServicos.addMouseListener(
            new MouseAdapter() {

                @Override
                public void mouseClicked(
                    MouseEvent e
                ) {

                    if (e.getClickCount() == 2) {
                        editarServico();
                    }
                }
            }
        );
    }

    // =========================================================
    // PERMISSÕES
    // =========================================================

    private void aplicarPermissoes() {

        if (SessaoUsuario.isConsultor()) {

            editarButton.setVisible(false);
            excluirButton.setVisible(false);

        } else if (SessaoUsuario.isGerente()) {

            editarButton.setVisible(true);
            excluirButton.setVisible(false);

        } else if (SessaoUsuario.isAdmin()) {

            editarButton.setVisible(true);
            excluirButton.setVisible(true);
        }
    }

    // =========================================================
    // PESQUISAR SERVIÇOS
    // =========================================================

    private void pesquisarServicos(String texto) {

        modeloTabela.setRowCount(0);

        String sql =
            "SELECT id_servico, nome, categoria, preco, ativo "
          + "FROM servicos "
          + "WHERE CAST(id_servico AS TEXT) ILIKE ? "
          + "OR nome ILIKE ? "
          + "OR COALESCE(descricao, '') ILIKE ? "
          + "OR categoria ILIKE ? "
          + "ORDER BY id_servico";

        try (
            Connection conn =
                ConectionFactory.conector();

            PreparedStatement pst =
                conn.prepareStatement(sql)
        ) {

            String busca =
                "%" + texto.trim() + "%";

            pst.setString(1, busca);
            pst.setString(2, busca);
            pst.setString(3, busca);
            pst.setString(4, busca);

            ResultSet rs =
                pst.executeQuery();

            while (rs.next()) {

                modeloTabela.addRow(
                    new Object[]{

                        rs.getInt("id_servico"),

                        rs.getString("nome"),

                        rs.getString("categoria"),

                        String.format(
                            "R$ %.2f",
                            rs.getDouble("preco")
                        ),

                        rs.getBoolean("ativo")
                            ? "Sim"
                            : "Não"
                    }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao pesquisar serviços:\n"
                + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // EDITAR SERVIÇO
    // =========================================================

    private void editarServico() {

        if (SessaoUsuario.isConsultor()) {

            JOptionPane.showMessageDialog(
                this,
                "Seu perfil possui apenas permissão para consultar serviços.",
                "Acesso negado",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linha =
            tabelaServicos.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Selecione um serviço para editar.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linhaModelo =
            tabelaServicos.convertRowIndexToModel(
                linha
            );

        int idServico =
            Integer.parseInt(
                modeloTabela
                    .getValueAt(linhaModelo, 0)
                    .toString()
            );

        DetalheServico tela =
            new DetalheServico(
                idServico,
                () ->
                    pesquisarServicos(
                        campoPesquisar.getText()
                    )
            );

        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // =========================================================
    // EXCLUIR SERVIÇO
    // =========================================================

    private void excluirServico() {

        if (!SessaoUsuario.isAdmin()) {

            JOptionPane.showMessageDialog(
                this,
                "Somente o administrador pode excluir serviços.",
                "Acesso negado",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linha =
            tabelaServicos.getSelectedRow();

        if (linha == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Selecione um serviço para excluir.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int linhaModelo =
            tabelaServicos.convertRowIndexToModel(
                linha
            );

        int idServico =
            Integer.parseInt(
                modeloTabela
                    .getValueAt(linhaModelo, 0)
                    .toString()
            );

        String nomeServico =
            modeloTabela
                .getValueAt(linhaModelo, 1)
                .toString();

        int resposta =
            JOptionPane.showInternalConfirmDialog(
                this,
                "Deseja realmente excluir o serviço:\n\n"
                + nomeServico
                + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
            "DELETE FROM servicos "
          + "WHERE id_servico = ?";

        try (
            Connection conn =
                ConectionFactory.conector();

            PreparedStatement pst =
                conn.prepareStatement(sql)
        ) {

            pst.setInt(1, idServico);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Serviço excluído com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            );

            pesquisarServicos(
                campoPesquisar.getText()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao excluir serviço:\n"
                + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}