
package relatorios;

import conexao.ConectionFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class CadastroContasReceber extends JInternalFrame {

    private JComboBox<HospedeItem> comboHospede;
    private JTextField campoDescricao;
    private JTextField campoValor;
    private JTextField campoDataEmissao;
    private JTextField campoDataVencimento;
    private JTextField campoDataRecebimento;
    private JComboBox<String> comboStatus;
    private JTextArea campoObservacao;

    private Runnable aoSalvar;

    private final DateTimeFormatter formatoBR =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    public CadastroContasReceber() {

        super("Hotel Imperial - Cadastro de Contas a Receber",
                true, true, false, true);

        criarTela();
        carregarHospedes();
        limparCampos();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 600);
    }

    public void setAoSalvar(Runnable aoSalvar) {
        this.aoSalvar = aoSalvar;
    }

    private void criarTela() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("CADASTRO DE CONTAS A RECEBER");
        titulo.setFont(new Font("Arial", Font.BOLD, 21));

        JPanel painelTitulo = new JPanel(
                new FlowLayout(FlowLayout.LEFT));

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

        comboHospede = new JComboBox<>();
        campoDescricao = new JTextField(25);
        campoValor = new JTextField(15);

        campoDataEmissao = new JTextField(12);
        campoDataVencimento = new JTextField(12);
        campoDataRecebimento = new JTextField(12);

        aplicarMascaraData(campoDataEmissao);
        aplicarMascaraData(campoDataVencimento);
        aplicarMascaraData(campoDataRecebimento);

        comboStatus = new JComboBox<>(
                new String[]{"Pendente", "Recebido", "Atrasado"});

        campoObservacao = new JTextArea(4, 25);
        campoObservacao.setLineWrap(true);
        campoObservacao.setWrapStyleWord(true);

        adicionarCampo(painelFormulario, gbc, 0,
                "Hóspede:", comboHospede);

        adicionarCampo(painelFormulario, gbc, 1,
                "Descrição:", campoDescricao);

        adicionarCampo(painelFormulario, gbc, 2,
                "Valor (R$):", campoValor);

        adicionarCampo(painelFormulario, gbc, 3,
                "Data de emissão (DD/MM/AAAA):",
                campoDataEmissao);

        adicionarCampo(painelFormulario, gbc, 4,
                "Data de vencimento (DD/MM/AAAA):",
                campoDataVencimento);

        adicionarCampo(painelFormulario, gbc, 5,
                "Status:", comboStatus);

        adicionarCampo(painelFormulario, gbc, 6,
                "Data de recebimento (DD/MM/AAAA):",
                campoDataRecebimento);

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

        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoLimpar = new JButton("Limpar");
        JButton botaoFechar = new JButton("Fechar");

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoFechar);

        add(painelBotoes, BorderLayout.SOUTH);

        botaoSalvar.addActionListener(e -> salvarConta());

        botaoLimpar.addActionListener(e -> limparCampos());

        botaoFechar.addActionListener(e -> dispose());

        comboStatus.addActionListener(e -> atualizarDataRecebimento());
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

    private void carregarHospedes() {

        comboHospede.removeAllItems();

        comboHospede.addItem(
                new HospedeItem(null, "Sem hóspede vinculado"));

        String sql =
                "SELECT id_hospedi, nome_hospedi, sobrenome_hospedi "
              + "FROM hospedi "
              + "ORDER BY nome_hospedi";

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                String nome = rs.getString("nome_hospedi")
                        + " " + rs.getString("sobrenome_hospedi");

                comboHospede.addItem(
                        new HospedeItem(
                                rs.getInt("id_hospedi"), nome));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar hóspedes:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarDataRecebimento() {

        if (comboStatus.getSelectedItem() == null) {
            return;
        }

        String status = comboStatus.getSelectedItem().toString();

        if ("Recebido".equals(status)
                && campoDataRecebimento.getText().trim().isEmpty()) {

            campoDataRecebimento.setText(
                    LocalDate.now().format(formatoBR));

        } else if (!"Recebido".equals(status)) {
            campoDataRecebimento.setText("");
        }
    }

    private void salvarConta() {

        String descricao = campoDescricao.getText().trim();
        String valorTexto = campoValor.getText().trim()
                .replace(".", "")
                .replace(",", ".");

        String emissaoTexto = campoDataEmissao.getText().trim();
        String vencimentoTexto = campoDataVencimento.getText().trim();
        String recebimentoTexto = campoDataRecebimento.getText().trim();
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

        java.math.BigDecimal valor;

        try {
            valor = new java.math.BigDecimal(valorTexto);

            if (valor.compareTo(java.math.BigDecimal.ZERO) <= 0) {
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

        Date dataEmissao;
        Date dataVencimento;
        Date dataRecebimento = null;

        try {
            dataEmissao = Date.valueOf(
                    LocalDate.parse(emissaoTexto, formatoBR));

            dataVencimento = Date.valueOf(
                    LocalDate.parse(vencimentoTexto, formatoBR));

            if (!recebimentoTexto.isEmpty()) {
                dataRecebimento = Date.valueOf(
                        LocalDate.parse(recebimentoTexto, formatoBR));
            }

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe as datas no formato DD/MM/AAAA.\n"
                    + "Exemplo: 14/10/2026",
                    "Data inválida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = comboStatus.getSelectedItem().toString();

        if ("Recebido".equals(status) && dataRecebimento == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe a data de recebimento para uma conta recebida.",
                    "Data de recebimento",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!"Recebido".equals(status) && dataRecebimento != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "A data de recebimento deve ficar vazia enquanto a conta não for recebida.",
                    "Data de recebimento",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        HospedeItem hospede =
                (HospedeItem) comboHospede.getSelectedItem();

        String sql =
                "INSERT INTO contas_receber "
              + "(id_hospedi, descricao, valor, data_emissao, "
              + "data_vencimento, data_recebimento, status, observacao) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (hospede == null || hospede.id == null) {
                pst.setNull(1, java.sql.Types.INTEGER);
            } else {
                pst.setInt(1, hospede.id);
            }

            pst.setString(2, descricao);
            pst.setBigDecimal(3, valor);
            pst.setDate(4, dataEmissao);
            pst.setDate(5, dataVencimento);

            if (dataRecebimento == null) {
                pst.setNull(6, java.sql.Types.DATE);
            } else {
                pst.setDate(6, dataRecebimento);
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
                    "Conta a receber cadastrada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            limparCampos();

            if (aoSalvar != null) {
                aoSalvar.run();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar conta a receber:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {

        campoDescricao.setText("");
        campoValor.setText("");

        campoDataEmissao.setText(
                LocalDate.now().format(formatoBR));

        campoDataVencimento.setText("");
        campoDataRecebimento.setText("");
        campoObservacao.setText("");

        comboStatus.setSelectedItem("Pendente");

        if (comboHospede.getItemCount() > 0) {
            comboHospede.setSelectedIndex(0);
        }

        campoDescricao.requestFocus();
    }

    private static class HospedeItem {

        private final Integer id;
        private final String nome;

        HospedeItem(Integer id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }
}
