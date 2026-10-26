package vendasProdutos;

import conexao.ConectionFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CadastroProduto extends JInternalFrame {

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
    private JButton limparButton;
    private JButton fecharButton;

    public CadastroProduto() {

        super(
                "Hotel Imperial - Cadastro de Produto",
                true,   // pode fechar
                true,   // pode maximizar
                false,  // não pode minimizar
                true    // pode redimensionar
        );

        criarTela();

        setDefaultCloseOperation(
                JInternalFrame.DISPOSE_ON_CLOSE
        );

        setSize(750, 650);
    }

    // =========================================================
    // CRIAÇÃO DA TELA
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
        // TÍTULO
        // =====================================================

        JLabel titulo =
                new JLabel("CADASTRO DE PRODUTO");

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

        principal.add(
                titulo,
                BorderLayout.NORTH
        );

        // =====================================================
        // PAINEL DOS CAMPOS
        // =====================================================

        JPanel painelCampos =
                new JPanel(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(7, 7, 7, 7);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        // =====================================================
        // CAMPOS
        // =====================================================

        nome = new JTextField();

        descricao =
                new JTextArea(3, 20);

        descricao.setLineWrap(true);
        descricao.setWrapStyleWord(true);

        JScrollPane scrollDescricao =
                new JScrollPane(descricao);

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

        precoCusto =
                new JTextField();

        precoVenda =
                new JTextField();

        estoque =
                new JTextField();

        estoqueMinimo =
                new JTextField();

        ativo =
                new JCheckBox("Produto ativo");

        ativo.setSelected(true);

        int linha = 0;

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Nome *:",
                nome
        );

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Descrição:",
                scrollDescricao
        );

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Categoria *:",
                categoria
        );

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Unidade *:",
                unidade
        );

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Preço de custo *:",
                precoCusto
        );

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Preço de venda *:",
                precoVenda
        );

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Estoque atual *:",
                estoque
        );

        adicionarCampo(
                painelCampos,
                gbc,
                linha++,
                "Estoque mínimo *:",
                estoqueMinimo
        );

        // =====================================================
        // PRODUTO ATIVO
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;

        painelCampos.add(
                ativo,
                gbc
        );

        principal.add(
                new JScrollPane(painelCampos),
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        salvarButton =
                new JButton("SALVAR");

        limparButton =
                new JButton("LIMPAR");

        fecharButton =
                new JButton("FECHAR");

        // =====================================================
        // ÍCONES
        // =====================================================

        ImageIcon iconeSalvar =
                carregarIcone(
                        "icons8-cadastro-20.png"
                );

        if (iconeSalvar != null) {
            salvarButton.setIcon(iconeSalvar);
        }

        ImageIcon iconeLimpar =
                carregarIcone(
                        "icons8-atualizar-20.png"
                );

        if (iconeLimpar != null) {
            limparButton.setIcon(iconeLimpar);
        }

        ImageIcon iconeFechar =
                carregarIcone(
                        "icons8-saida-32.png"
                );

        if (iconeFechar != null) {
            fecharButton.setIcon(iconeFechar);
        }

        // =====================================================
        // FONTE DOS BOTÕES
        // =====================================================

        salvarButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        limparButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        fecharButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        // =====================================================
        // PAINEL DOS BOTÕES
        // =====================================================

        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                5
                        )
                );

        painelBotoes.add(
                salvarButton
        );

        painelBotoes.add(
                limparButton
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

        salvarButton.addActionListener(
                e -> salvarProduto()
        );

        limparButton.addActionListener(
                e -> limparCampos()
        );

        fecharButton.addActionListener(
                e -> dispose()
        );
    }

    // =========================================================
    // CARREGAR ÍCONE
    // =========================================================

    private ImageIcon carregarIcone(
            String nomeArquivo) {

        URL url =
                getClass().getResource(
                        "/imagens/" + nomeArquivo
                );

        if (url == null) {

            System.out.println(
                    "Ícone não encontrado: "
                    + "/imagens/"
                    + nomeArquivo
            );

            return null;
        }

        return new ImageIcon(url);
    }

    // =========================================================
    // ADICIONAR CAMPO
    // =========================================================

    private void adicionarCampo(
            JPanel painel,
            GridBagConstraints gbc,
            int linha,
            String texto,
            java.awt.Component componente
    ) {

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        painel.add(
                label,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        painel.add(
                componente,
                gbc
        );
    }

    // =========================================================
    // SALVAR PRODUTO
    // =========================================================

    private void salvarProduto() {

        String nomeTexto =
                nome.getText().trim();

        String descricaoTexto =
                descricao.getText().trim();

        String categoriaTexto =
                categoria.getSelectedItem()
                        .toString();

        String unidadeTexto =
                unidade.getSelectedItem()
                        .toString();

        String custoTexto =
                precoCusto.getText().trim();

        String vendaTexto =
                precoVenda.getText().trim();

        String estoqueTexto =
                estoque.getText().trim();

        String estoqueMinimoTexto =
                estoqueMinimo.getText().trim();

        // =====================================================
        // NOME
        // =====================================================

        if (nomeTexto.isEmpty()) {

            JOptionPane.showInternalMessageDialog(
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

        if (categoriaTexto.equals("Selecione")) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Selecione uma categoria.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            categoria.requestFocus();
            return;
        }

        // =====================================================
        // UNIDADE
        // =====================================================

        if (unidadeTexto.equals("Selecione")) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Selecione uma unidade.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            unidade.requestFocus();
            return;
        }

        // =====================================================
        // PREÇO DE CUSTO
        // =====================================================

        if (custoTexto.isEmpty()) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Informe o preço de custo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            precoCusto.requestFocus();
            return;
        }

        // =====================================================
        // PREÇO DE VENDA
        // =====================================================

        if (vendaTexto.isEmpty()) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Informe o preço de venda.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            precoVenda.requestFocus();
            return;
        }

        // =====================================================
        // ESTOQUE
        // =====================================================

        if (estoqueTexto.isEmpty()) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Informe o estoque atual.\n"
                    + "Se não houver estoque, informe 0.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            estoque.requestFocus();
            return;
        }

        // =====================================================
        // ESTOQUE MÍNIMO
        // =====================================================

        if (estoqueMinimoTexto.isEmpty()) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Informe o estoque mínimo.\n"
                    + "Se não houver mínimo, informe 0.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            estoqueMinimo.requestFocus();
            return;
        }

        // =====================================================
        // CONVERSÃO
        // =====================================================

        double precoCustoValor;
        double precoVendaValor;

        int estoqueValor;
        int estoqueMinimoValor;

        try {

            precoCustoValor =
                    converterNumero(
                            custoTexto
                    );

            precoVendaValor =
                    converterNumero(
                            vendaTexto
                    );

            estoqueValor =
                    Integer.parseInt(
                            estoqueTexto
                    );

            estoqueMinimoValor =
                    Integer.parseInt(
                            estoqueMinimoTexto
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Verifique os valores informados.\n\n"
                    + "Preços: use, por exemplo, 10,50\n"
                    + "Estoque: use números inteiros.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =====================================================
        // VALORES NEGATIVOS
        // =====================================================

        if (precoCustoValor < 0) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "O preço de custo não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            precoCusto.requestFocus();
            return;
        }

        if (precoVendaValor < 0) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "O preço de venda não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            precoVenda.requestFocus();
            return;
        }

        if (estoqueValor < 0) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "O estoque não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            estoque.requestFocus();
            return;
        }

        if (estoqueMinimoValor < 0) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "O estoque mínimo não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            estoqueMinimo.requestFocus();
            return;
        }

        // =====================================================
        // SQL
        // =====================================================

        String sql =
                "INSERT INTO produtos ("
                + "nome, "
                + "descricao, "
                + "categoria, "
                + "unidade, "
                + "preco_custo, "
                + "preco_venda, "
                + "estoque, "
                + "estoque_minimo, "
                + "ativo"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn =
                        ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            // Nome
            pst.setString(
                    1,
                    nomeTexto
            );

            // Descrição
            if (descricaoTexto.isEmpty()) {

                pst.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                pst.setString(
                        2,
                        descricaoTexto
                );
            }

            // Categoria
            pst.setString(
                    3,
                    categoriaTexto
            );

            // Unidade
            pst.setString(
                    4,
                    unidadeTexto
            );

            // Preço de custo
            pst.setDouble(
                    5,
                    precoCustoValor
            );

            // Preço de venda
            pst.setDouble(
                    6,
                    precoVendaValor
            );

            // Estoque
            pst.setInt(
                    7,
                    estoqueValor
            );

            // Estoque mínimo
            pst.setInt(
                    8,
                    estoqueMinimoValor
            );

            // Ativo
            pst.setBoolean(
                    9,
                    ativo.isSelected()
            );

            int resultado =
                    pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showInternalMessageDialog(
                        this,
                        "Produto cadastrado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limparCampos();
            }

        } catch (Exception e) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Erro ao cadastrar produto:\n"
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
            String texto) {

        texto = texto
                .replace("R$", "")
                .replace(" ", "")
                .trim();

        if (texto.contains(",")) {

            texto = texto
                    .replace(".", "")
                    .replace(",", ".");
        }

        return Double.parseDouble(texto);
    }

    // =========================================================
    // LIMPAR
    // =========================================================

    private void limparCampos() {

        nome.setText("");

        descricao.setText("");

        categoria.setSelectedIndex(0);

        unidade.setSelectedIndex(0);

        precoCusto.setText("");

        precoVenda.setText("");

        estoque.setText("");

        estoqueMinimo.setText("");

        ativo.setSelected(true);

        nome.requestFocus();
    }
}