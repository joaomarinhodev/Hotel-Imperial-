
package relatorios;

import conexao.ConectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RelatorioFuncionarios extends JInternalFrame {

    private JTextField campoPesquisar;
    private JComboBox<String> comboCargo;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JLabel labelTotal;

    private JButton botaoPesquisar;
    private JButton botaoLimpar;
    private JButton botaoImprimir;
    private JButton botaoFechar;

    public RelatorioFuncionarios() {

        super(
            "Hotel Imperial - Relatório de Funcionários",
            true,
            true,
            false,
            true
        );

        criarTela();

        setDefaultCloseOperation(
            JInternalFrame.DISPOSE_ON_CLOSE
        );

        setSize(1050, 620);

        pesquisarFuncionarios();
    }

    // =====================================================
    // CRIAR TELA
    // =====================================================

    private void criarTela() {

        setLayout(new BorderLayout(10, 10));

        // CABEÇALHO

        JPanel painelTitulo =
            new JPanel(new BorderLayout());

        painelTitulo.setBorder(
            BorderFactory.createEmptyBorder(
                15, 15, 5, 15
            )
        );

        JLabel titulo =
            new JLabel("RELATÓRIO DE FUNCIONÁRIOS");

        titulo.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        JLabel subtitulo =
            new JLabel(
                "Consulta dos funcionários cadastrados"
            );

        subtitulo.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        JPanel textosTitulo =
            new JPanel(new GridLayout(2, 1, 0, 5));

        textosTitulo.add(titulo);
        textosTitulo.add(subtitulo);

        painelTitulo.add(
            textosTitulo,
            BorderLayout.WEST
        );

        add(painelTitulo, BorderLayout.NORTH);

        // FILTROS

        JPanel painelFiltros =
            new JPanel(new GridBagLayout());

        painelFiltros.setBorder(
            BorderFactory.createTitledBorder(
                "Filtros do relatório"
            )
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        painelFiltros.add(
            new JLabel("Pesquisar funcionário:"),
            gbc
        );

        campoPesquisar = new JTextField(20);

        gbc.gridx = 1;
        gbc.weightx = 1;

        painelFiltros.add(
            campoPesquisar,
            gbc
        );

        gbc.gridx = 2;
        gbc.weightx = 0;

        painelFiltros.add(
            new JLabel("Cargo:"),
            gbc
        );

        comboCargo = new JComboBox<>(
            new String[]{
                "Todos",
                "Admin",
                "Gerente",
                "Consultor"
            }
        );

        gbc.gridx = 3;
        gbc.weightx = 0.5;

        painelFiltros.add(
            comboCargo,
            gbc
        );

        JPanel painelBotoesPesquisa =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    5,
                    0
                )
            );

        botaoPesquisar = new JButton("Pesquisar");
        botaoLimpar = new JButton("Limpar");

        painelBotoesPesquisa.add(botaoPesquisar);
        painelBotoesPesquisa.add(botaoLimpar);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        painelFiltros.add(
            painelBotoesPesquisa,
            gbc
        );

        // TABELA

        String[] colunas = {
            "ID",
            "NOME",
            "SOBRENOME",
            "CPF",
            "CARGO"
        };

        modelo = new DefaultTableModel(
            colunas,
            0
        ) {
            @Override
            public boolean isCellEditable(
                int linha,
                int coluna
            ) {
                return false;
            }
        };

        tabela = new JTable(modelo);

        tabela.setRowHeight(25);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabela.getTableHeader()
            .setReorderingAllowed(false);

        tabela.setFillsViewportHeight(true);

        tabela.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(50);

        tabela.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(200);

        tabela.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(200);

        tabela.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(150);

        tabela.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(150);

        JScrollPane scrollTabela =
            new JScrollPane(tabela);

        JPanel painelCentro =
            new JPanel(new BorderLayout(10, 10));

        painelCentro.setBorder(
            BorderFactory.createEmptyBorder(
                0, 15, 0, 15
            )
        );

        painelCentro.add(
            painelFiltros,
            BorderLayout.NORTH
        );

        painelCentro.add(
            scrollTabela,
            BorderLayout.CENTER
        );

        add(painelCentro, BorderLayout.CENTER);

        // RODAPÉ

        JPanel painelRodape =
            new JPanel(new BorderLayout(10, 10));

        painelRodape.setBorder(
            BorderFactory.createEmptyBorder(
                5, 15, 15, 15
            )
        );

        labelTotal =
            new JLabel("Funcionários encontrados: 0");

        labelTotal.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        painelRodape.add(
            labelTotal,
            BorderLayout.WEST
        );

        JPanel painelBotoesRodape =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    8,
                    5
                )
            );

        botaoImprimir = new JButton("Imprimir");
        botaoFechar = new JButton("Fechar");

        painelBotoesRodape.add(botaoImprimir);
        painelBotoesRodape.add(botaoFechar);

        painelRodape.add(
            painelBotoesRodape,
            BorderLayout.EAST
        );

        add(painelRodape, BorderLayout.SOUTH);

        // EVENTOS

        campoPesquisar.addKeyListener(
            new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    pesquisarFuncionarios();
                }
            }
        );

        campoPesquisar.addActionListener(
            e -> pesquisarFuncionarios()
        );

        comboCargo.addActionListener(
            e -> pesquisarFuncionarios()
        );

        botaoPesquisar.addActionListener(
            e -> pesquisarFuncionarios()
        );

        botaoLimpar.addActionListener(e -> {

            campoPesquisar.setText("");
            comboCargo.setSelectedIndex(0);

            pesquisarFuncionarios();

            campoPesquisar.requestFocus();
        });

        botaoImprimir.addActionListener(
            e -> imprimirRelatorio()
        );

        botaoFechar.addActionListener(
            e -> dispose()
        );
    }

    // =====================================================
    // PESQUISAR FUNCIONÁRIOS
    // =====================================================

    private void pesquisarFuncionarios() {

        modelo.setRowCount(0);

        String texto =
            campoPesquisar.getText().trim();

        String cargo =
            comboCargo.getSelectedItem().toString();

        String sql =
            "SELECT id_funcionario, nome_funcionario, "
          + "sobrenome_funcionario, cpf_funcionario, "
          + "cargo_funcionario "
          + "FROM funcionarios "
          + "WHERE (CAST(id_funcionario AS TEXT) ILIKE ? "
          + "OR nome_funcionario ILIKE ? "
          + "OR sobrenome_funcionario ILIKE ? "
          + "OR COALESCE(cpf_funcionario, '') ILIKE ?) ";

        if (!cargo.equals("Todos")) {
            sql += "AND cargo_funcionario = ? ";
        }

        sql += "ORDER BY nome_funcionario";

        int total = 0;

        try (
            Connection conn =
                ConectionFactory.conector();

            PreparedStatement pst =
                conn.prepareStatement(sql)
        ) {

            String busca = "%" + texto + "%";

            for (int i = 1; i <= 4; i++) {
                pst.setString(i, busca);
            }

            if (!cargo.equals("Todos")) {
                pst.setString(5, cargo);
            }

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    modelo.addRow(
                        new Object[]{
                            rs.getInt("id_funcionario"),
                            rs.getString("nome_funcionario"),
                            rs.getString("sobrenome_funcionario"),
                            rs.getString("cpf_funcionario"),
                            rs.getString("cargo_funcionario")
                        }
                    );

                    total++;
                }
            }

            labelTotal.setText(
                "Funcionários encontrados: " + total
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao gerar relatório de funcionários:\n"
                + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // IMPRIMIR RELATÓRIO
    // =====================================================

    private void imprimirRelatorio() {

        if (modelo.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                this,
                "Não existem funcionários para imprimir.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            boolean impresso = tabela.print(
                JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat(
                    "Hotel Imperial - Relatório de Funcionários"
                ),
                new java.text.MessageFormat(
                    "Página {0}"
                )
            );

            if (impresso) {

                JOptionPane.showMessageDialog(
                    this,
                    "Impressão enviada com sucesso.",
                    "Impressão",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (PrinterException e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao imprimir relatório:\n"
                + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
