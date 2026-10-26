package vendasProdutos;

import conexao.ConectionFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DetalheProduto extends JFrame {

    private int idProduto;

    private Runnable aoSalvar;

    private JTextField nome;
    private JTextArea descricao;

    private JComboBox<String> categoria;
    private JComboBox<String> unidade;

    private JTextField precoCusto;
    private JTextField precoVenda;
    private JTextField estoque;
    private JTextField estoqueMinimo;

    private JCheckBox ativo;

    private JButton salvarButton;
    private JButton cancelarButton;

    // =========================================================
    // CONSTRUTORES
    // =========================================================

    public DetalheProduto(int idProduto) {
        this(idProduto, null);
    }

    public DetalheProduto(
            int idProduto,
            Runnable aoSalvar
    ) {

        this.idProduto = idProduto;
        this.aoSalvar = aoSalvar;

        criarTela();

        carregarProduto();
    }

    // =========================================================
    // CRIAR TELA
    // =========================================================

    private void criarTela() {

        setTitle(
                "Hotel Imperial - Editar Produto"
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setSize(750, 700);

        setLocationRelativeTo(null);

        // =====================================================
        // PAINEL PRINCIPAL
        // =====================================================

        JPanel principal =
                new JPanel(new BorderLayout(10, 10));

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel titulo =
                new JLabel(
                        "EDITAR PRODUTO"
                );

        titulo.setFont(
                new Font(
                        "Arial Black",
                        Font.BOLD,
                        24
                )
        );

        titulo.setHorizontalAlignment(
                JLabel.CENTER
        );

        principal.add(
                titulo,
                BorderLayout.NORTH
        );

        // =====================================================
        // FORMULÁRIO
        // =====================================================

        JPanel formulario =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(7, 7, 7, 7);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        // =====================================================
        // NOME
        // =====================================================

        JLabel labelNome =
                new JLabel("Nome *:");

        labelNome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        nome =
                new JTextField();

        nome.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelNome,
                nome,
                0
        );

        // =====================================================
        // DESCRIÇÃO
        // =====================================================

        JLabel labelDescricao =
                new JLabel("Descrição:");

        labelDescricao.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        descricao =
                new JTextArea(4, 20);

        descricao.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        descricao.setLineWrap(true);

        descricao.setWrapStyleWord(true);

        JScrollPane scrollDescricao =
                new JScrollPane(descricao);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;

        formulario.add(
                labelDescricao,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        formulario.add(
                scrollDescricao,
                gbc
        );

        // =====================================================
        // CATEGORIA
        // =====================================================

        JLabel labelCategoria =
                new JLabel("Categoria *:");

        labelCategoria.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        categoria =
                new JComboBox<>(
                        new String[]{
                                "Selecione",
                                "Alimentação",
                                "Bebidas",
                                "Higiene",
                                "Limpeza",
                                "Outros"
                        }
                );

        categoria.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelCategoria,
                categoria,
                2
        );

        // =====================================================
        // UNIDADE
        // =====================================================

        JLabel labelUnidade =
                new JLabel("Unidade *:");

        labelUnidade.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        unidade =
                new JComboBox<>(
                        new String[]{
                                "Selecione",
                                "Unidade",
                                "Kg",
                                "Litro",
                                "Caixa",
                                "Pacote"
                        }
                );

        unidade.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelUnidade,
                unidade,
                3
        );

        // =====================================================
        // PREÇO DE CUSTO
        // =====================================================

        JLabel labelPrecoCusto =
                new JLabel("Preço de custo *:");

        labelPrecoCusto.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        precoCusto =
                new JTextField();

        precoCusto.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelPrecoCusto,
                precoCusto,
                4
        );

        // =====================================================
        // PREÇO DE VENDA
        // =====================================================

        JLabel labelPrecoVenda =
                new JLabel("Preço de venda *:");

        labelPrecoVenda.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        precoVenda =
                new JTextField();

        precoVenda.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelPrecoVenda,
                precoVenda,
                5
        );

        // =====================================================
        // ESTOQUE
        // =====================================================

        JLabel labelEstoque =
                new JLabel("Estoque atual *:");

        labelEstoque.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        estoque =
                new JTextField();

        estoque.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelEstoque,
                estoque,
                6
        );

        // =====================================================
        // ESTOQUE MÍNIMO
        // =====================================================

        JLabel labelEstoqueMinimo =
                new JLabel("Estoque mínimo *:");

        labelEstoqueMinimo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        estoqueMinimo =
                new JTextField();

        estoqueMinimo.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelEstoqueMinimo,
                estoqueMinimo,
                7
        );

        // =====================================================
        // ATIVO
        // =====================================================

        ativo =
                new JCheckBox(
                        "Produto ativo"
                );

        ativo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        ativo.setSelected(true);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 1;

        formulario.add(
                ativo,
                gbc
        );

        // =====================================================
        // SCROLL DO FORMULÁRIO
        // =====================================================

        JScrollPane scrollFormulario =
                new JScrollPane(
                        formulario
                );

        scrollFormulario.setBorder(null);

        principal.add(
                scrollFormulario,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        salvarButton =
                new JButton("SALVAR");

        cancelarButton =
                new JButton("CANCELAR");

        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                10
                        )
                );

        painelBotoes.add(
                salvarButton
        );

        painelBotoes.add(
                cancelarButton
        );

        principal.add(
                painelBotoes,
                BorderLayout.SOUTH
        );

        // =====================================================
        // EVENTOS
        // =====================================================

        salvarButton.addActionListener(
                e -> salvarProduto()
        );

        cancelarButton.addActionListener(
                e -> dispose()
        );

        add(principal);
    }

    // =========================================================
    // ADICIONAR CAMPO
    // =========================================================

    private void adicionarCampo(
            JPanel painel,
            GridBagConstraints gbc,
            JLabel label,
            java.awt.Component campo,
            int linha
    ) {

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.weightx = 0;

        painel.add(
                label,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        painel.add(
                campo,
                gbc
        );
    }

    // =========================================================
    // CARREGAR PRODUTO
    // =========================================================

    private void carregarProduto() {

        String sql =
                "SELECT "
                + "nome, "
                + "descricao, "
                + "categoria, "
                + "unidade, "
                + "preco_custo, "
                + "preco_venda, "
                + "estoque, "
                + "estoque_minimo, "
                + "ativo "
                + "FROM produtos "
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

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                nome.setText(
                        rs.getString("nome")
                );

                String desc =
                        rs.getString("descricao");

                descricao.setText(
                        desc != null
                                ? desc
                                : ""
                );

                categoria.setSelectedItem(
                        rs.getString("categoria")
                );

                unidade.setSelectedItem(
                        rs.getString("unidade")
                );

                precoCusto.setText(
                        String.format(
                                "%.2f",
                                rs.getDouble(
                                        "preco_custo"
                                )
                        )
                );

                precoVenda.setText(
                        String.format(
                                "%.2f",
                                rs.getDouble(
                                        "preco_venda"
                                )
                        )
                );

                estoque.setText(
                        String.valueOf(
                                rs.getInt(
                                        "estoque"
                                )
                        )
                );

                estoqueMinimo.setText(
                        String.valueOf(
                                rs.getInt(
                                        "estoque_minimo"
                                )
                        )
                );

                ativo.setSelected(
                        rs.getBoolean("ativo")
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Produto não encontrado.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );

                dispose();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar produto:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

            dispose();
        }
    }

    // =========================================================
    // SALVAR PRODUTO
    // =========================================================

    private void salvarProduto() {

        // =====================================================
        // NOME
        // =====================================================

        String nomeProduto =
                nome.getText().trim();

        if (nomeProduto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do produto.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            nome.requestFocus();

            return;
        }

        // =====================================================
        // CATEGORIA
        // =====================================================

        if (categoria.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione a categoria do produto.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            categoria.requestFocus();

            return;
        }

        // =====================================================
        // UNIDADE
        // =====================================================

        if (unidade.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione a unidade do produto.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            unidade.requestFocus();

            return;
        }

        // =====================================================
        // PREÇOS
        // =====================================================

        double valorCusto;
        double valorVenda;

        try {

            valorCusto =
                    converterNumero(
                            precoCusto.getText()
                    );

            valorVenda =
                    converterNumero(
                            precoVenda.getText()
                    );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe valores válidos para os preços.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (valorCusto < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "O preço de custo não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            precoCusto.requestFocus();

            return;
        }

        if (valorVenda < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "O preço de venda não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            precoVenda.requestFocus();

            return;
        }

        // =====================================================
        // ESTOQUE
        // =====================================================

        int quantidadeEstoque;
        int quantidadeMinima;

        try {

            String textoEstoque =
                    estoque.getText().trim();

            String textoMinimo =
                    estoqueMinimo.getText().trim();

            if (textoEstoque.isEmpty()) {

                throw new Exception();
            }

            if (textoMinimo.isEmpty()) {

                throw new Exception();
            }

            quantidadeEstoque =
                    Integer.parseInt(
                            textoEstoque
                    );

            quantidadeMinima =
                    Integer.parseInt(
                            textoMinimo
                    );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe valores inteiros válidos para o estoque.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (quantidadeEstoque < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "O estoque não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            estoque.requestFocus();

            return;
        }

        if (quantidadeMinima < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "O estoque mínimo não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            estoqueMinimo.requestFocus();

            return;
        }

        // =====================================================
        // DESCRIÇÃO
        // =====================================================

        String descricaoProduto =
                descricao.getText().trim();

        if (descricaoProduto.isEmpty()) {

            descricaoProduto = null;
        }

        // =====================================================
        // UPDATE
        // =====================================================

        String sql =
                "UPDATE produtos SET "
                + "nome = ?, "
                + "descricao = ?, "
                + "categoria = ?, "
                + "unidade = ?, "
                + "preco_custo = ?, "
                + "preco_venda = ?, "
                + "estoque = ?, "
                + "estoque_minimo = ?, "
                + "ativo = ? "
                + "WHERE id_produto = ?";

        try (
                Connection conn =
                        ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    nomeProduto
            );

            if (descricaoProduto == null) {

                pst.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                pst.setString(
                        2,
                        descricaoProduto
                );
            }

            pst.setString(
                    3,
                    categoria
                            .getSelectedItem()
                            .toString()
            );

            pst.setString(
                    4,
                    unidade
                            .getSelectedItem()
                            .toString()
            );

            pst.setDouble(
                    5,
                    valorCusto
            );

            pst.setDouble(
                    6,
                    valorVenda
            );

            pst.setInt(
                    7,
                    quantidadeEstoque
            );

            pst.setInt(
                    8,
                    quantidadeMinima
            );

            pst.setBoolean(
                    9,
                    ativo.isSelected()
            );

            pst.setInt(
                    10,
                    idProduto
            );

            int resultado =
                    pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Produto atualizado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Atualiza a tela de consulta
                if (aoSalvar != null) {

                    aoSalvar.run();
                }

                dispose();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar produto:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CONVERTER NÚMERO
    // =========================================================

    private double converterNumero(
            String texto
    ) {

        texto =
                texto
                        .trim()
                        .replace("R$", "")
                        .replace(" ", "");

        if (texto.isEmpty()) {

            throw new NumberFormatException();
        }

        if (texto.contains(",")) {

            texto =
                    texto
                            .replace(".", "")
                            .replace(",", ".");
        }

        return Double.parseDouble(
                texto
        );
    }
}