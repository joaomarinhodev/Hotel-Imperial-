
package reservas;

import conexao.ConectionFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class RealizarCheckin extends JInternalFrame {

    private JTable tabela;
    private DefaultTableModel modelo;
    private JButton btnAtualizar;
    private JButton btnCheckin;
    private JButton btnFechar;

    public RealizarCheckin() {
        super("Hotel Imperial - Realizar Check-in",
              true, true, true, true);

        criarTela();
        carregarReservas();

        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
    }

    private void criarTela() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
            "REALIZAR CHECK-IN", SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
            new Object[]{
                "Código", "Hóspede", "Quarto",
                "Check-in previsto", "Check-out previsto",
                "Diárias", "Valor da diária", "Status"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setRowHeight(24);
        tabela.setAutoCreateRowSorter(true);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnAtualizar = new JButton("Atualizar");
        btnCheckin = new JButton("Realizar Check-in");
        btnFechar = new JButton("Fechar");

        btnAtualizar.addActionListener(e -> carregarReservas());
        btnCheckin.addActionListener(e -> realizarCheckin());
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnCheckin);
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarReservas() {
        modelo.setRowCount(0);

        String sql =
            "SELECT r.id_reserva, " +
            "h.nome_hospedi || ' ' || h.sobrenome_hospedi AS hospede, " +
            "q.numero, r.data_entrada, r.data_saida, " +
            "r.valor_diaria, r.status " +
            "FROM reserva r " +
            "INNER JOIN hospedi h ON h.id_hospedi = r.id_hospedi " +
            "INNER JOIN quarto q ON q.id_quarto = r.id_quarto " +
            "WHERE r.status = 'Reservada' " +
            "ORDER BY r.data_entrada, r.id_reserva";

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LocalDate entrada = rs.getDate("data_entrada").toLocalDate();
                LocalDate saida = rs.getDate("data_saida").toLocalDate();

                long diarias = java.time.temporal.ChronoUnit.DAYS
                        .between(entrada, saida);

                modelo.addRow(new Object[]{
                    rs.getInt("id_reserva"),
                    rs.getString("hospede"),
                    rs.getString("numero"),
                    entrada.format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    ),
                    saida.format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    ),
                    diarias,
                    String.format("R$ %.2f", rs.getBigDecimal("valor_diaria")),
                    rs.getString("status")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar reservas:\n" + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void realizarCheckin() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                this, "Selecione uma reserva para realizar o check-in."
            );
            return;
        }

        linha = tabela.convertRowIndexToModel(linha);

        int idReserva = (int) modelo.getValueAt(linha, 0);
        String hospede = modelo.getValueAt(linha, 1).toString();
        String dataTexto = modelo.getValueAt(linha, 3).toString();

        LocalDate dataEntrada = LocalDate.parse(
            dataTexto,
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu")
        );

        if (LocalDate.now().isBefore(dataEntrada)) {
            JOptionPane.showMessageDialog(
                this,
                "O check-in ainda não pode ser realizado.\n" +
                "A data prevista é " + dataTexto + ".",
                "Check-in antecipado",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Confirmar o check-in do hóspede " + hospede + "?",
            "Confirmar Check-in",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
            "UPDATE reserva SET status = 'Em andamento' " +
            "WHERE id_reserva = ? AND status = 'Reservada' " +
            "AND data_entrada <= CURRENT_DATE";

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReserva);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Check-in realizado com sucesso!"
                );
                carregarReservas();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível realizar o check-in. " +
                    "Verifique a data e o status da reserva.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
                );
                carregarReservas();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao realizar check-in:\n" + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    
    private boolean verificarDisponibilidade(
            Connection conn,
            int idQuarto,
            java.time.LocalDate entrada,
            java.time.LocalDate saida) throws SQLException {

        String bloquearQuarto =
                "SELECT id_quarto FROM quarto " +
                "WHERE id_quarto = ? FOR UPDATE";

        try (PreparedStatement ps = conn.prepareStatement(bloquearQuarto)) {
            ps.setInt(1, idQuarto);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Quarto não encontrado.");
                }
            }
        }

        String sql =
                "SELECT COUNT(*) FROM reserva " +
                "WHERE id_quarto = ? " +
                "AND status IN ('Reservada', 'Em andamento') " +
                "AND data_entrada < ? " +
                "AND data_saida > ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idQuarto);
            ps.setDate(2, java.sql.Date.valueOf(saida));
            ps.setDate(3, java.sql.Date.valueOf(entrada));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

}
