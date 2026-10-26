
package reservas;
import java.time.format.DateTimeFormatter;

import conexao.ConectionFactory;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CriarReserva extends JInternalFrame {

    private final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    
    private JComboBox<HospedeItem> comboHospede;
    private JComboBox<QuartoItem> comboQuarto;

    private JTextField campoEntrada;
    private JTextField campoSaida;
    private JLabel labelValor;
    private JLabel labelTotal;

    public CriarReserva() {
        super("Hotel Imperial - Criar Reserva",
                true, true, false, true);

        criarTela();
        prepararTabela();
        carregarHospedes();
        carregarQuartos();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(760, 500);
        setVisible(true);
    }

    private void criarTela() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("CRIAR RESERVA");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(JLabel.CENTER);
        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10));

        add(titulo, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(
                BorderFactory.createEmptyBorder(15, 25, 15, 25));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(9, 8, 9, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        comboHospede = new JComboBox<>();
        comboQuarto = new JComboBox<>();

        campoEntrada = new JTextField(12);
        campoSaida = new JTextField(12);
        aplicarMascaraData(campoEntrada);
        aplicarMascaraData(campoSaida);

        campoEntrada.setToolTipText("Exemplo: 25/10/2026");
        campoSaida.setToolTipText("Exemplo: 28/10/2026");

        labelValor = new JLabel("R$ 0,00");
        labelTotal = new JLabel("R$ 0,00");

        adicionarCampo(painel, c, 0, "Hóspede:", comboHospede);
        adicionarCampo(painel, c, 1, "Quarto:", comboQuarto);
        adicionarCampo(painel, c, 2,
        "Check-in (DD/MM/AAAA):", campoEntrada);
        adicionarCampo(painel, c, 3,
        "Check-out (DD/MM/AAAA):", campoSaida);
        adicionarCampo(painel, c, 4, "Valor da diária:", labelValor);
        adicionarCampo(painel, c, 5, "Total previsto:", labelTotal);

        comboQuarto.addActionListener(e -> atualizarTotal());
        campoEntrada.addActionListener(e -> atualizarTotal());
        campoSaida.addActionListener(e -> atualizarTotal());

        add(painel, BorderLayout.CENTER);

        JButton botaoSalvar = new JButton("Salvar reserva");
        JButton botaoLimpar = new JButton("Limpar");
        JButton botaoFechar = new JButton("Fechar");

        botaoSalvar.addActionListener(e -> salvarReserva());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoFechar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel();
        botoes.add(botaoSalvar);
        botoes.add(botaoLimpar);
        botoes.add(botaoFechar);

        add(botoes, BorderLayout.SOUTH);
    }

    private void adicionarCampo(
            JPanel painel,
            GridBagConstraints c,
            int linha,
            String texto,
            java.awt.Component campo) {

        c.gridx = 0;
        c.gridy = linha;
        c.weightx = 0;
        painel.add(new JLabel(texto), c);

        c.gridx = 1;
        c.weightx = 1;
        painel.add(campo, c);
    }

    private void prepararTabela() {
        String sql =
                "CREATE TABLE IF NOT EXISTS reserva ("
                + "id_reserva SERIAL PRIMARY KEY, "
                + "id_hospedi INTEGER NOT NULL REFERENCES hospedi(id_hospedi), "
                + "id_quarto INTEGER NOT NULL REFERENCES quarto(id_quarto), "
                + "data_entrada DATE NOT NULL, "
                + "data_saida DATE NOT NULL, "
                + "valor_diaria NUMERIC(10,2) NOT NULL, "
                + "status VARCHAR(30) NOT NULL DEFAULT 'Reservada', "
                + "data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "CONSTRAINT reserva_datas_validas "
                + "CHECK (data_saida > data_entrada), "
                + "CONSTRAINT reserva_valor_valido "
                + "CHECK (valor_diaria >= 0)"
                + ")";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new Exception("Falha na conexão com o banco.");
            }

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível preparar a tabela de reservas:\n"
                    + e.getMessage(),
                    "Erro no banco",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarHospedes() {
        comboHospede.removeAllItems();

        String sql =
                "SELECT id_hospedi, nome_hospedi, sobrenome_hospedi "
                + "FROM hospedi ORDER BY nome_hospedi";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new Exception("Falha na conexão com o banco.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String nome =
                            rs.getString("nome_hospedi") + " "
                            + rs.getString("sobrenome_hospedi");

                    comboHospede.addItem(new HospedeItem(
                            rs.getInt("id_hospedi"), nome.trim()));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar hóspedes:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarQuartos() {
        comboQuarto.removeAllItems();

        String sql =
                "SELECT id_quarto, numero, tipo, valor_diaria, capacidade "
                + "FROM quarto ORDER BY numero";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new Exception("Falha na conexão com o banco.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    comboQuarto.addItem(new QuartoItem(
                            rs.getInt("id_quarto"),
                            rs.getString("numero"),
                            rs.getString("tipo"),
                            rs.getBigDecimal("valor_diaria"),
                            rs.getInt("capacidade")));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar quartos:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

        atualizarTotal();
    }

    
    private void atualizarTotal() {
        QuartoItem quarto = (QuartoItem) comboQuarto.getSelectedItem();

        if (quarto == null) {
            labelValor.setText("R$ 0,00");
            labelTotal.setText("R$ 0,00");
            return;
        }

        labelValor.setText(moeda(quarto.valorDiaria));

        try {
            LocalDate entrada = LocalDate.parse(
                    campoEntrada.getText().trim(), formatoData);

            LocalDate saida = LocalDate.parse(
                    campoSaida.getText().trim(), formatoData);

            long noites = java.time.temporal.ChronoUnit.DAYS
                    .between(entrada, saida);

            if (noites > 0) {
                labelTotal.setText(moeda(
                        quarto.valorDiaria.multiply(
                                BigDecimal.valueOf(noites))));
            } else {
                labelTotal.setText("Confira as datas");
            }

        } catch (Exception e) {
            labelTotal.setText("Informe as datas");
        }
    }


    
    private void salvarReserva() {
        HospedeItem hospede = (HospedeItem) comboHospede.getSelectedItem();
        QuartoItem quarto = (QuartoItem) comboQuarto.getSelectedItem();

        if (hospede == null || quarto == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um hóspede e um quarto.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate entrada;
        LocalDate saida;

        try {
            entrada = LocalDate.parse(campoEntrada.getText().trim(), formatoData);
            saida = LocalDate.parse(campoSaida.getText().trim(), formatoData);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Informe as datas no formato DD/MM/AAAA.",
                    "Datas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (entrada.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this,
                    "A data de entrada não pode estar no passado.",
                    "Data inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!saida.isAfter(entrada)) {
            JOptionPane.showMessageDialog(this,
                    "A saída deve ser posterior à entrada.",
                    "Datas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long noites = java.time.temporal.ChronoUnit.DAYS.between(entrada, saida);
        BigDecimal total = quarto.valorDiaria.multiply(BigDecimal.valueOf(noites));

        int confirmar = JOptionPane.showConfirmDialog(this,
                "Hóspede: " + hospede.nome
                + "\nQuarto: " + quarto.numero + " - " + quarto.tipo
                + "\nEntrada: " + entrada.format(formatoData)
                + "\nSaída: " + saida.format(formatoData)
                + "\nDiárias: " + noites
                + "\nTotal previsto: " + moeda(total)
                + "\n\nConfirma a reserva?",
                "Confirmar reserva", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        String sqlBloquearQuarto =
                "SELECT id_quarto FROM quarto "
                + "WHERE id_quarto = ? FOR UPDATE";

        String sqlVerificar =
                "SELECT COUNT(*) FROM reserva "
                + "WHERE id_quarto = ? "
                + "AND status IN ('Reservada', 'Em andamento') "
                + "AND data_entrada < ? "
                + "AND data_saida > ?";

        String sqlInserir =
                "INSERT INTO reserva "
                + "(id_hospedi, id_quarto, data_entrada, data_saida, "
                + "valor_diaria, status) "
                + "VALUES (?, ?, ?, ?, ?, 'Reservada')";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new SQLException("Falha na conexão com o banco.");
            }

            conn.setAutoCommit(false);

            try {
                // Bloqueia o quarto até o término da transação.
                try (PreparedStatement ps =
                        conn.prepareStatement(sqlBloquearQuarto)) {
                    ps.setInt(1, quarto.id);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Quarto não encontrado.");
                        }
                    }
                }

                // Verifica reservas que se cruzam com o período solicitado.
                try (PreparedStatement ps =
                        conn.prepareStatement(sqlVerificar)) {
                    ps.setInt(1, quarto.id);
                    ps.setDate(2, Date.valueOf(saida));
                    ps.setDate(3, Date.valueOf(entrada));

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            conn.rollback();

                            JOptionPane.showMessageDialog(this,
                                    "O quarto " + quarto.numero
                                    + " já está reservado nesse período.\n"
                                    + "Escolha outro quarto ou outras datas.",
                                    "Quarto indisponível",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }

                // Só grava se o período estiver livre.
                try (PreparedStatement ps = conn.prepareStatement(sqlInserir)) {
                    ps.setInt(1, hospede.id);
                    ps.setInt(2, quarto.id);
                    ps.setDate(3, Date.valueOf(entrada));
                    ps.setDate(4, Date.valueOf(saida));
                    ps.setBigDecimal(5, quarto.valorDiaria);
                    ps.executeUpdate();
                }

                conn.commit();

                JOptionPane.showMessageDialog(this,
                        "Reserva cadastrada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                limparCampos();

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar reserva:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String moeda(BigDecimal valor) {
        return java.text.NumberFormat.getCurrencyInstance(
                new java.util.Locale("pt", "BR")).format(valor);
    }

    private void limparCampos() {
        campoEntrada.setText("");
        campoSaida.setText("");

        if (comboHospede.getItemCount() > 0) {
            comboHospede.setSelectedIndex(0);
        }

        if (comboQuarto.getItemCount() > 0) {
            comboQuarto.setSelectedIndex(0);
        }

        atualizarTotal();
    }

    private static class HospedeItem {
        final int id;
        final String nome;

        HospedeItem(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return id + " - " + nome;
        }
    }

    private static class QuartoItem {
        final int id;
        final String numero;
        final String tipo;
        final BigDecimal valorDiaria;
        final int capacidade;

        QuartoItem(int id, String numero, String tipo,
                BigDecimal valorDiaria, int capacidade) {
            this.id = id;
            this.numero = numero;
            this.tipo = tipo;
            this.valorDiaria = valorDiaria;
            this.capacidade = capacidade;
        }

        @Override
        public String toString() {
            return "Quarto " + numero + " - " + tipo
                    + " - " + moedaTexto(valorDiaria)
                    + " - capacidade: " + capacidade;
        }

        private String moedaTexto(BigDecimal valor) {
            return java.text.NumberFormat.getCurrencyInstance(
                    new java.util.Locale("pt", "BR")).format(valor);
        }
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
        });
    }

}
