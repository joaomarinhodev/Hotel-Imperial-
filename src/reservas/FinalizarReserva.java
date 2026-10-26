
package reservas;

import conexao.ConectionFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FinalizarReserva extends JInternalFrame {

    private JTable tabela;
    private DefaultTableModel modelo;

    private final DateTimeFormatter formatoData =
            DateTimeFormatter.ofPattern("dd/MM/uuuu");

    public FinalizarReserva() {
        super("Hotel Imperial - Finalizar Hospedagem",
                true, true, false, true);

        criarTela();
        carregarReservas();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 580);
        setVisible(true);
    }

    private void criarTela() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("FINALIZAR HOSPEDAGEM");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(JLabel.CENTER);
        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10));

        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{
                    "Código", "Hóspede", "Quarto",
                    "Check-in", "Check-out previsto",
                    "Diárias", "Valor da diária",
                    "Status"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(25);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton botaoAtualizar = new JButton("Atualizar");
        JButton botaoFinalizar = new JButton("Registrar check-out");
        JButton botaoFechar = new JButton("Fechar");

        botaoAtualizar.addActionListener(e -> carregarReservas());
        botaoFinalizar.addActionListener(e -> finalizarHospedagem());
        botaoFechar.addActionListener(e -> dispose());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoes.add(botaoAtualizar);
        botoes.add(botaoFinalizar);
        botoes.add(botaoFechar);

        add(botoes, BorderLayout.SOUTH);
    }

    private void carregarReservas() {
        modelo.setRowCount(0);

        String sql =
                "SELECT r.id_reserva, "
                + "h.nome_hospedi, h.sobrenome_hospedi, "
                + "q.numero, r.data_entrada, r.data_saida, "
                + "r.valor_diaria, r.status "
                + "FROM reserva r "
                + "INNER JOIN hospedi h ON h.id_hospedi = r.id_hospedi "
                + "INNER JOIN quarto q ON q.id_quarto = r.id_quarto "
                + "WHERE r.status IN ('Reservada', 'Em andamento') "
                + "ORDER BY r.data_entrada, r.id_reserva";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new Exception("Falha na conexão com o banco.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    LocalDate entrada =
                            rs.getDate("data_entrada").toLocalDate();

                    LocalDate saida =
                            rs.getDate("data_saida").toLocalDate();

                    long diarias =
                            java.time.temporal.ChronoUnit.DAYS
                                    .between(entrada, saida);

                    modelo.addRow(new Object[]{
                        rs.getInt("id_reserva"),
                        rs.getString("nome_hospedi") + " "
                                + rs.getString("sobrenome_hospedi"),
                        rs.getString("numero"),
                        entrada.format(formatoData),
                        saida.format(formatoData),
                        diarias,
                        java.text.NumberFormat.getCurrencyInstance(
                                new java.util.Locale("pt", "BR"))
                                .format(rs.getBigDecimal("valor_diaria")),
                        rs.getString("status")
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar hospedagens:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarHospedagem() {
        int linha = tabela.getSelectedRow();

        if (linha < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma reserva na tabela.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);

        int idReserva = Integer.parseInt(
                modelo.getValueAt(linhaModelo, 0).toString());

        String hospede =
                modelo.getValueAt(linhaModelo, 1).toString();

        String quarto =
                modelo.getValueAt(linhaModelo, 2).toString();

        String status =
                modelo.getValueAt(linhaModelo, 7).toString();

        if ("Reservada".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this,
                    "Esta reserva ainda está com status 'Reservada'.\n"
                    + "Confirme primeiro que o hóspede realizou o check-in.",
                    "Check-in pendente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Confirma o check-out desta hospedagem?\n\n"
                + "Hóspede: " + hospede + "\n"
                + "Quarto: " + quarto + "\n"
                + "Data do check-out: "
                + LocalDate.now().format(formatoData),
                "Confirmar check-out",
                JOptionPane.YES_NO_OPTION);

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "UPDATE reserva "
                + "SET status = 'Finalizada' "
                + "WHERE id_reserva = ? "
                + "AND status = 'Em andamento'";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new Exception("Falha na conexão com o banco.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idReserva);

                int alteradas = ps.executeUpdate();

                if (alteradas == 1) {
                    JOptionPane.showMessageDialog(this,
                            "Check-out registrado com sucesso!",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    carregarReservas();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "A reserva não foi finalizada. "
                            + "Atualize a lista e tente novamente.",
                            "Atenção", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao finalizar hospedagem:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
