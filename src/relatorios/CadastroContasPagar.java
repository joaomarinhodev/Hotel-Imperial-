
package relatorios;

import conexao.ConectionFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class CadastroContasPagar extends JInternalFrame {

    private JTextField campoDescricao;
    private JTextField campoValor;
    private JTextField campoDataEmissao;
    private JTextField campoDataVencimento;
    private JTextField campoDataPagamento;
    private JTextArea campoObservacao;

    private JComboBox<FornecedorItem> comboFornecedor;
    private JComboBox<String> comboStatus;

    private JButton botaoSalvar;
    private JButton botaoLimpar;
    private JButton botaoFechar;
    
    private Runnable aoSalvar;
 
    public void setAoSalvar(Runnable aoSalvar) {
        this.aoSalvar = aoSalvar;
    }

    public CadastroContasPagar() {

        super("Hotel Imperial - Cadastro de Contas a Pagar",
                true, true, false, true);

        criarTela();
        carregarFornecedores();
        limparCampos();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 600);
    }

    private void criarTela() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("CADASTRO DE CONTAS A PAGAR");
        titulo.setFont(new Font("Arial", Font.BOLD, 21));

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTitulo.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 0, 10));
        painelTitulo.add(titulo);

        add(painelTitulo, BorderLayout.NORTH);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        comboFornecedor = new JComboBox<>();
        campoDescricao = new JTextField(25);
        campoValor = new JTextField(15);

        campoDataEmissao = new JTextField(12);
        campoDataVencimento = new JTextField(12);
        campoDataPagamento = new JTextField(12);

        aplicarMascaraData(campoDataEmissao);
        aplicarMascaraData(campoDataVencimento);
        aplicarMascaraData(campoDataPagamento);
        
        comboStatus = new JComboBox<>(
                new String[]{"Pendente", "Pago", "Atrasado"});

        campoObservacao = new JTextArea(4, 25);
        campoObservacao.setLineWrap(true);
        campoObservacao.setWrapStyleWord(true);

        adicionarCampo(painelFormulario, gbc, 0,
                "Fornecedor:", comboFornecedor);

        adicionarCampo(painelFormulario, gbc, 1,
                "Descrição da despesa:", campoDescricao);

        adicionarCampo(painelFormulario, gbc, 2,
                "Valor (R$):", campoValor);

        adicionarCampo(painelFormulario, gbc, 3,
                "Data de emissão (AAAA-MM-DD):", campoDataEmissao);

        adicionarCampo(painelFormulario, gbc, 4,
                "Data de vencimento (AAAA-MM-DD):", campoDataVencimento);

        adicionarCampo(painelFormulario, gbc, 5,
                "Status:", comboStatus);

        adicionarCampo(painelFormulario, gbc, 6,
                "Data de pagamento (AAAA-MM-DD):", campoDataPagamento);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;

        painelFormulario.add(new JLabel("Observações:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;

        painelFormulario.add(
                new JScrollPane(campoObservacao), gbc);

        add(new JScrollPane(painelFormulario), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 10));

        botaoSalvar = new JButton("Salvar");
        botaoLimpar = new JButton("Limpar");
        botaoFechar = new JButton("Fechar");

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoFechar);

        add(painelBotoes, BorderLayout.SOUTH);

        botaoSalvar.addActionListener(e -> salvarConta());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoFechar.addActionListener(e -> dispose());

        comboStatus.addActionListener(e -> atualizarDataPagamento());
    }
    
    private void aplicarMascaraData(JTextField campo) {

        campo.setDocument(new javax.swing.text.PlainDocument() {

            private boolean atualizando = false;

            @Override
            public void insertString(
                    int offset,
                    String texto,
                    javax.swing.text.AttributeSet attr)
                    throws javax.swing.text.BadLocationException {

                if (texto == null || atualizando) {
                    return;
                }

                String atual = getText(0, getLength());
                String novo = atual.substring(0, offset)
                        + texto
                        + atual.substring(offset);

                String numeros = novo.replaceAll("[^0-9]", "");

                if (numeros.length() > 8) {
                    return;
                }

                StringBuilder formatado = new StringBuilder();

                for (int i = 0; i < numeros.length(); i++) {
                    if (i == 2 || i == 4) {
                        formatado.append('/');
                    }
                    formatado.append(numeros.charAt(i));
                }

                atualizando = true;

                try {
                    super.remove(0, getLength());
                    super.insertString(0, formatado.toString(), attr);
                } finally {
                    atualizando = false;
                }
            }

            @Override
            public void remove(
                    int offset,
                    int length)
                    throws javax.swing.text.BadLocationException {

                super.remove(offset, length);
            }
        });
    }

    private void adicionarCampo(
            JPanel painel,
            GridBagConstraints gbc,
            int linha,
            String texto,
            JComponent campo) {

        gbc.gridy = linha;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        painel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        painel.add(campo, gbc);
    }

    private void carregarFornecedores() {

        comboFornecedor.removeAllItems();
        comboFornecedor.addItem(
                new FornecedorItem(null, "Sem fornecedor vinculado"));

        String sql =
                "SELECT id_fornecedor, razao_social "
              + "FROM fornecedor ORDER BY razao_social";

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                comboFornecedor.addItem(
                        new FornecedorItem(
                                rs.getInt("id_fornecedor"),
                                rs.getString("razao_social")));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar fornecedores:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarDataPagamento() {

        if (comboStatus.getSelectedItem() == null) {
            return;
        }

        String status = comboStatus.getSelectedItem().toString();

        if ("Pago".equals(status)
                && campoDataPagamento.getText().trim().isEmpty()) {

            campoDataPagamento.setText(LocalDate.now().toString());

        } else if (!"Pago".equals(status)) {

            campoDataPagamento.setText("");
        }
    }

    
    private void salvarConta() {

        String descricao = campoDescricao.getText().trim();

        String valorTexto = campoValor.getText().trim()
                .replace(",", ".");

        String emissaoTexto = campoDataEmissao.getText().trim();
        String vencimentoTexto = campoDataVencimento.getText().trim();
        String pagamentoTexto = campoDataPagamento.getText().trim();
        String observacao = campoObservacao.getText().trim();

        if (descricao.isEmpty()
                || valorTexto.isEmpty()
                || emissaoTexto.isEmpty()
                || vencimentoTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha a descrição, o valor e as datas de emissão e vencimento.",
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double valor;

        try {
            valor = Double.parseDouble(valorTexto);

            if (!Double.isFinite(valor) || valor <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um valor válido e maior que zero.",
                    "Valor inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DateTimeFormatter formatoBR =
                DateTimeFormatter.ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT);

        Date dataEmissao;
        Date dataVencimento;
        Date dataPagamento = null;

        try {
            dataEmissao = Date.valueOf(
                    LocalDate.parse(emissaoTexto, formatoBR));

            dataVencimento = Date.valueOf(
                    LocalDate.parse(vencimentoTexto, formatoBR));

            if (!pagamentoTexto.isEmpty()) {
                dataPagamento = Date.valueOf(
                        LocalDate.parse(pagamentoTexto, formatoBR));
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Confira as datas. Use o formato DD/MM/AAAA.\n"
                    + "Exemplo: 12/10/2026",
                    "Data inválida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = comboStatus.getSelectedItem().toString();

        if ("Pago".equals(status) && dataPagamento == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe a data de pagamento para uma conta marcada como paga.",
                    "Data de pagamento",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!"Pago".equals(status) && dataPagamento != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "A data de pagamento deve ficar vazia enquanto a conta não estiver paga.",
                    "Data de pagamento",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        FornecedorItem fornecedor =
                (FornecedorItem) comboFornecedor.getSelectedItem();

        String sql =
                "INSERT INTO contas_pagar "
              + "(id_fornecedor, descricao, valor, data_emissao, "
              + "data_vencimento, data_pagamento, status, observacao) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (fornecedor == null || fornecedor.id == null) {
                pst.setNull(1, java.sql.Types.INTEGER);
            } else {
                pst.setInt(1, fornecedor.id);
            }

            pst.setString(2, descricao);

            pst.setBigDecimal(
                    3, java.math.BigDecimal.valueOf(valor));

            pst.setDate(4, dataEmissao);
            pst.setDate(5, dataVencimento);

            if (dataPagamento == null) {
                pst.setNull(6, java.sql.Types.DATE);
            } else {
                pst.setDate(6, dataPagamento);
            }

            pst.setString(7, status);

            if (observacao.isEmpty()) {
                pst.setNull(8, java.sql.Types.VARCHAR);
            } else {
                pst.setString(8, observacao);
            }

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Conta a pagar cadastrada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

            limparCampos();

            if (aoSalvar != null) {
                aoSalvar.run();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar conta a pagar:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limparCampos() {

        campoDescricao.setText("");
        campoValor.setText("");
        campoDataEmissao.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
        campoDataVencimento.setText("");
        campoDataPagamento.setText("");
        campoObservacao.setText("");

        comboStatus.setSelectedItem("Pendente");

        if (comboFornecedor.getItemCount() > 0) {
            comboFornecedor.setSelectedIndex(0);
        }

        campoDescricao.requestFocus();
    }

    private static class FornecedorItem {

        private final Integer id;
        private final String nome;

        FornecedorItem(Integer id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }
}
