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

import javax.swing.BorderFactory;
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

public class CadastroServico extends JInternalFrame {

    private JTextField nome;
    private JTextArea descricao;

    private JComboBox<String> categoria;

    private JTextField preco;

    private JCheckBox ativo;

    private JButton salvarButton;
    private JButton limparButton;
    private JButton fecharButton;

    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public CadastroServico() {

        super(
                "Hotel Imperial - Cadastro de Serviço",
                true,   // pode fechar
                true,   // pode maximizar
                false,  // não pode minimizar
                true    // pode redimensionar
        );

        criarTela();

        setDefaultCloseOperation(
                JInternalFrame.DISPOSE_ON_CLOSE
        );

        setSize(750, 600);
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
        // TÍTULO
        // =====================================================

        JLabel titulo =
                new JLabel(
                        "CADASTRO DE SERVIÇO"
                );

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
        // FORMULÁRIO
        // =====================================================

        JPanel formulario =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

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
                new JTextArea(
                        4,
                        20
                );

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
                new JScrollPane(
                        descricao
                );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

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
                                "Limpeza",
                                "Lavanderia",
                                "Transporte",
                                "Hospedagem",
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
        // PREÇO
        // =====================================================

        JLabel labelPreco =
                new JLabel("Preço *:");

        labelPreco.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        preco =
                new JTextField();

        preco.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        adicionarCampo(
                formulario,
                gbc,
                labelPreco,
                preco,
                3
        );

        // =====================================================
        // ATIVO
        // =====================================================

        ativo =
                new JCheckBox(
                        "Serviço ativo"
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
        gbc.gridy = 4;
        gbc.weightx = 1;

        formulario.add(
                ativo,
                gbc
        );

        // =====================================================
        // FORMULÁRIO NO CENTRO
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

        limparButton =
                new JButton("LIMPAR");

        fecharButton =
                new JButton("FECHAR");

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
                e -> salvarServico()
        );

        limparButton.addActionListener(
                e -> limparCampos()
        );

        fecharButton.addActionListener(
                e -> dispose()
        );
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
    // SALVAR SERVIÇO
    // =========================================================

    private void salvarServico() {

        // =====================================================
        // NOME
        // =====================================================

        String nomeServico =
                nome.getText().trim();

        if (nomeServico.isEmpty()) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Informe o nome do serviço.",
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

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Selecione a categoria do serviço.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            categoria.requestFocus();

            return;
        }

        // =====================================================
        // PREÇO
        // =====================================================

        double valor;

        try {

            valor =
                    converterNumero(
                            preco.getText()
                    );

        } catch (Exception e) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Informe um preço válido.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            preco.requestFocus();

            return;
        }

        if (valor < 0) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "O preço não pode ser negativo.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            preco.requestFocus();

            return;
        }

        // =====================================================
        // DESCRIÇÃO
        // =====================================================

        String descricaoServico =
                descricao.getText().trim();

        if (descricaoServico.isEmpty()) {

            descricaoServico = null;
        }

        // =====================================================
        // SQL
        // =====================================================

        String sql =
                "INSERT INTO servicos ("
                + "nome, "
                + "descricao, "
                + "categoria, "
                + "preco, "
                + "ativo"
                + ") VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn =
                        ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    nomeServico
            );

            if (descricaoServico == null) {

                pst.setNull(
                        2,
                        java.sql.Types.VARCHAR
                );

            } else {

                pst.setString(
                        2,
                        descricaoServico
                );
            }

            pst.setString(
                    3,
                    categoria
                            .getSelectedItem()
                            .toString()
            );

            pst.setDouble(
                    4,
                    valor
            );

            pst.setBoolean(
                    5,
                    ativo.isSelected()
            );

            int resultado =
                    pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showInternalMessageDialog(
                        this,
                        "Serviço cadastrado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limparCampos();
            }

        } catch (Exception e) {

            JOptionPane.showInternalMessageDialog(
                    this,
                    "Erro ao cadastrar serviço:\n"
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

    // =========================================================
    // LIMPAR CAMPOS
    // =========================================================

    private void limparCampos() {

        nome.setText("");

        descricao.setText("");

        categoria.setSelectedIndex(0);

        preco.setText("");

        ativo.setSelected(true);

        nome.requestFocus();
    }
}