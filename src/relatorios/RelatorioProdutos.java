
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
import java.text.NumberFormat;
import java.util.Locale;

public class RelatorioProdutos extends JInternalFrame {

    private JTextField campoPesquisar;
    private JComboBox<String> comboCategoria;
    private JCheckBox checkEstoqueBaixo;
    private JTable tabela;
    private DefaultTableModel modelo;

    private JLabel labelTotal;
    private JLabel labelEstoqueBaixo;

    private JButton botaoPesquisar;
    private JButton botaoLimpar;
    private JButton botaoImprimir;
    private JButton botaoFechar;

    private final NumberFormat formatoMoeda =
        NumberFormat.getCurrencyInstance(
            new Locale("pt", "BR")
        );

    public RelatorioProdutos() {

        super(
            "Hotel Imperial - Relatório de Produtos",
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

        pesquisarProdutos();
    }

    // =====================================================
    // CRIAR TELA
    // =====================================================

    private void criarTela() {

        setLayout(new BorderLayout(10, 10));

        // CABEÇALHO
        JPanel painelTitulo = new JPanel(
            new BorderLayout()
        );

        painelTitulo.setBorder(
            BorderFactory.createEmptyBorder(
                15, 15, 5, 15
            )
        );

        JLabel titulo = new JLabel(
            "RELATÓRIO DE PRODUTOS"
        );

        titulo.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        JLabel subtitulo = new JLabel(
            "Consulta de produtos, preços e estoque"
        );

        subtitulo.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        JPanel textosTitulo = new JPanel(
            new GridLayout(2, 1, 0, 5)
        );

        textosTitulo.add(titulo);
        textosTitulo.add(subtitulo);

        painelTitulo.add(
            textosTitulo,
            BorderLayout.WEST
        );

        add(
            painelTitulo,
            BorderLayout.NORTH
        );

        // PAINEL DE FILTROS
        JPanel painelFiltros = new JPanel(
            new GridBagLayout()
        );

        painelFiltros.setBorder(
            BorderFactory.createTitledBorder(
                "Filtros do relatório"
            )
        );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pesquisa
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        painelFiltros.add(
            new JLabel("Pesquisar produto:"),
            gbc
        );

        campoPesquisar = new JTextField(20);

        gbc.gridx = 1;
        gbc.weightx = 1;

        painelFiltros.add(
            campoPesquisar,
            gbc
        );

        // Categoria
        gbc.gridx = 2;
        gbc.weightx = 0;

        painelFiltros.add(
            new JLabel("Categoria:"),
            gbc
        );

        comboCategoria = new JComboBox<>(
            new String[]{
                "Todas",
                "Alimentação",
                "Bebidas",
                "Higiene",
                "Limpeza",
                "Outros"
            }
        );

        gbc.gridx = 3;
        gbc.weightx = 0.5;

        painelFiltros.add(
            comboCategoria,
            gbc
        );

        // Estoque baixo
        checkEstoqueBaixo = new JCheckBox(
            "Somente estoque baixo"
        );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;

        painelFiltros.add(
            checkEstoqueBaixo,
            gbc
        );

        // Botões de pesquisa
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
            "PRODUTO",
            "CATEGORIA",
            "UNIDADE",
            "PREÇO CUSTO",
            "PREÇO VENDA",
            "ESTOQUE",
            "ESTOQUE MÍN.",
            "STATUS"
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

        tabela.getTableHeader().setReorderingAllowed(false);

        tabela.setFillsViewportHeight(true);

        tabela.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(45);

        tabela.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(220);

        tabela.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(110);

        tabela.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(75);

        JScrollPane scrollTabela =
            new JScrollPane(tabela);

        // CENTRO
        JPanel painelCentro = new JPanel(
            new BorderLayout(10, 10)
        );

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

        add(
            painelCentro,
            BorderLayout.CENTER
        );

        // RODAPÉ
        JPanel painelRodape = new JPanel(
            new BorderLayout(10, 10)
        );

        painelRodape.setBorder(
            BorderFactory.createEmptyBorder(
                5, 15, 15, 15
            )
        );

        JPanel painelResumo = new JPanel(
            new FlowLayout(
                FlowLayout.LEFT,
                15,
                5
            )
        );

        labelTotal = new JLabel(
            "Produtos encontrados: 0"
        );

        labelEstoqueBaixo = new JLabel(
            "Estoque baixo: 0"
        );

        labelTotal.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        labelEstoqueBaixo.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        painelResumo.add(labelTotal);
        painelResumo.add(labelEstoqueBaixo);

        JPanel painelBotoesRodape = new JPanel(
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
            painelResumo,
            BorderLayout.WEST
        );

        painelRodape.add(
            painelBotoesRodape,
            BorderLayout.EAST
        );

        add(
            painelRodape,
            BorderLayout.SOUTH
        );

        // EVENTOS

        campoPesquisar.addKeyListener(
            new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    pesquisarProdutos();
                }
            }
        );

        campoPesquisar.addActionListener(
            e -> pesquisarProdutos()
        );

        comboCategoria.addActionListener(
            e -> pesquisarProdutos()
        );

        checkEstoqueBaixo.addActionListener(
            e -> pesquisarProdutos()
        );

        botaoPesquisar.addActionListener(
            e -> pesquisarProdutos()
        );

        botaoLimpar.addActionListener(e -> {

            campoPesquisar.setText("");
            comboCategoria.setSelectedIndex(0);
            checkEstoqueBaixo.setSelected(false);

            pesquisarProdutos();

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
    // PESQUISAR PRODUTOS
    // =====================================================

    private void pesquisarProdutos() {

        modelo.setRowCount(0);

        String texto =
            campoPesquisar.getText().trim();

        String categoria =
            comboCategoria.getSelectedItem().toString();

        boolean somenteEstoqueBaixo =
            checkEstoqueBaixo.isSelected();

        String sql =
            "SELECT id_produto, nome, categoria, unidade, "
          + "preco_custo, preco_venda, estoque, "
          + "estoque_minimo, ativo "
          + "FROM produtos "
          + "WHERE (CAST(id_produto AS TEXT) ILIKE ? "
          + "OR nome ILIKE ? "
          + "OR COALESCE(descricao, '') ILIKE ?) ";

        if (!categoria.equals("Todas")) {
            sql += "AND categoria = ? ";
        }

        if (somenteEstoqueBaixo) {
            sql += "AND estoque <= estoque_minimo ";
        }

        sql += "ORDER BY nome";

        int total = 0;
        int totalEstoqueBaixo = 0;

        try (
            Connection conn =
                ConectionFactory.conector();

            PreparedStatement pst =
                conn.prepareStatement(sql)
        ) {

            String busca = "%" + texto + "%";

            pst.setString(1, busca);
            pst.setString(2, busca);
            pst.setString(3, busca);

            int indice = 4;

            if (!categoria.equals("Todas")) {
                pst.setString(indice, categoria);
            }

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    int estoque =
                        rs.getInt("estoque");

                    int estoqueMinimo =
                        rs.getInt("estoque_minimo");

                    boolean estoqueBaixo =
                        estoque <= estoqueMinimo;

                    modelo.addRow(
                        new Object[]{
                            rs.getInt("id_produto"),
                            rs.getString("nome"),
                            rs.getString("categoria"),
                            rs.getString("unidade"),
                            formatoMoeda.format(
                                rs.getBigDecimal("preco_custo")
                            ),
                            formatoMoeda.format(
                                rs.getBigDecimal("preco_venda")
                            ),
                            estoque,
                            estoqueMinimo,
                            rs.getBoolean("ativo")
                                ? "Ativo"
                                : "Inativo"
                        }
                    );

                    total++;

                    if (estoqueBaixo) {
                        totalEstoqueBaixo++;
                    }
                }
            }

            labelTotal.setText(
                "Produtos encontrados: " + total
            );

            labelEstoqueBaixo.setText(
                "Estoque baixo: " + totalEstoqueBaixo
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao gerar relatório de produtos:\n"
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
                "Não existem produtos para imprimir.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            boolean impresso = tabela.print(
                JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat(
                    "Hotel Imperial - Relatório de Produtos"
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
