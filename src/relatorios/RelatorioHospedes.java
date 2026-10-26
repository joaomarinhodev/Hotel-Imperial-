
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

public class RelatorioHospedes extends JInternalFrame {

    private JTextField campoPesquisar;
    private JComboBox<String> comboTipo;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JLabel labelTotal;

    private JButton botaoPesquisar;
    private JButton botaoLimpar;
    private JButton botaoImprimir;
    private JButton botaoFechar;

    public RelatorioHospedes() {

        super(
            "Hotel Imperial - Relatório de Hóspedes",
            true,
            true,
            false,
            true
        );

        criarTela();

        setDefaultCloseOperation(
            JInternalFrame.DISPOSE_ON_CLOSE
        );

        setSize(1100, 650);

        pesquisarHospedes();
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
            new JLabel("RELATÓRIO DE HÓSPEDES");

        titulo.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        JLabel subtitulo =
            new JLabel(
                "Consulta dos hóspedes cadastrados no sistema"
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
            new JLabel("Pesquisar hóspede:"),
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
            new JLabel("Tipo de hóspede:"),
            gbc
        );

        comboTipo = new JComboBox<>(
            new String[]{
                "Todos",
                "Comum",
                "VIP",
                "Empresário"
            }
        );

        gbc.gridx = 3;
        gbc.weightx = 0.5;

        painelFiltros.add(
            comboTipo,
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
            "CIDADE",
            "CELULAR",
            "TIPO"
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
            .setPreferredWidth(45);

        tabela.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(160);

        tabela.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(160);

        tabela.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(130);

        tabela.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(140);

        tabela.getColumnModel()
            .getColumn(5)
            .setPreferredWidth(130);

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
            new JLabel("Hóspedes encontrados: 0");

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
                    pesquisarHospedes();
                }
            }
        );

        campoPesquisar.addActionListener(
            e -> pesquisarHospedes()
        );

        comboTipo.addActionListener(
            e -> pesquisarHospedes()
        );

        botaoPesquisar.addActionListener(
            e -> pesquisarHospedes()
        );

        botaoLimpar.addActionListener(e -> {

            campoPesquisar.setText("");
            comboTipo.setSelectedIndex(0);

            pesquisarHospedes();

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
    // PESQUISAR HÓSPEDES
    // =====================================================

    private void pesquisarHospedes() {

        modelo.setRowCount(0);

        String texto =
            campoPesquisar.getText().trim();

        String tipo =
            comboTipo.getSelectedItem().toString();

        String sql =
            "SELECT id_hospedi, nome_hospedi, "
          + "sobrenome_hospedi, cpf_hospedi, "
          + "cidade_hospedi, cell_hospedi, tipo_hospedi "
          + "FROM hospedi "
          + "WHERE (CAST(id_hospedi AS TEXT) ILIKE ? "
          + "OR nome_hospedi ILIKE ? "
          + "OR sobrenome_hospedi ILIKE ? "
          + "OR COALESCE(cpf_hospedi, '') ILIKE ? "
          + "OR COALESCE(cidade_hospedi, '') ILIKE ? "
          + "OR COALESCE(cell_hospedi, '') ILIKE ?) ";

        if (!tipo.equals("Todos")) {
            sql += "AND tipo_hospedi = ? ";
        }

        sql += "ORDER BY nome_hospedi";

        int total = 0;

        try (
            Connection conn =
                ConectionFactory.conector();

            PreparedStatement pst =
                conn.prepareStatement(sql)
        ) {

            String busca = "%" + texto + "%";

            for (int i = 1; i <= 6; i++) {
                pst.setString(i, busca);
            }

            if (!tipo.equals("Todos")) {
                pst.setString(7, tipo);
            }

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    modelo.addRow(
                        new Object[]{
                            rs.getInt("id_hospedi"),
                            rs.getString("nome_hospedi"),
                            rs.getString("sobrenome_hospedi"),
                            rs.getString("cpf_hospedi"),
                            rs.getString("cidade_hospedi"),
                            rs.getString("cell_hospedi"),
                            rs.getString("tipo_hospedi")
                        }
                    );

                    total++;
                }
            }

            labelTotal.setText(
                "Hóspedes encontrados: " + total
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao gerar relatório de hóspedes:\n"
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
                "Não existem hóspedes para imprimir.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            boolean impresso = tabela.print(
                JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat(
                    "Hotel Imperial - Relatório de Hóspedes"
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
