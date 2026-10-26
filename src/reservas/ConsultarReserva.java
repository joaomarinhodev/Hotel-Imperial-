
package reservas;

import conexao.ConectionFactory;
import telas.SessaoUsuario;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ConsultarReserva extends JInternalFrame {

    private JTextField campoPesquisa;
    private JComboBox<String> comboStatus;
    private JTable tabela;
    private DefaultTableModel modelo;

    public ConsultarReserva() {
        super("Hotel Imperial - Consultar Reservas",
                true, true, false, true);

        criarTela();
        carregarReservas();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 600);
        setVisible(true);
    }

    private void criarTela() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("CONSULTA DE RESERVAS");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(JLabel.CENTER);
        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 5, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel filtros = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10));

        campoPesquisa = new JTextField(22);
        campoPesquisa.setToolTipText(
                "Pesquisar por hóspede, quarto ou código");

        comboStatus = new JComboBox<>(new String[]{
            "Todas",
            "Reservada",
            "Em andamento",
            "Finalizada",
            "Cancelada"
        });

        JButton pesquisar = new JButton("Pesquisar");
        JButton limpar = new JButton("Limpar");
        JButton atualizar = new JButton("Atualizar");
        JButton imprimir = new JButton("Imprimir");
        JButton excluir = new JButton("Excluir Reserva");

        filtros.add(new JLabel("Buscar:"));
        filtros.add(campoPesquisa);
        filtros.add(new JLabel("Status:"));
        filtros.add(comboStatus);
        filtros.add(pesquisar);
        filtros.add(limpar);
        filtros.add(atualizar);
        filtros.add(imprimir);
        filtros.add(excluir);

        add(filtros, BorderLayout.BEFORE_FIRST_LINE);

        modelo = new DefaultTableModel(
                new Object[]{
                    "Código",
                    "Hóspede",
                    "Quarto",
                    "Tipo",
                    "Check-in",
                    "Check-out",
                    "Diárias",
                    "Valor diária",
                    "Total",
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
                ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));

        JButton fechar = new JButton("Fechar");
        rodape.add(fechar);

        add(rodape, BorderLayout.SOUTH);

        pesquisar.addActionListener(e -> carregarReservas());
        campoPesquisa.addActionListener(e -> carregarReservas());
        comboStatus.addActionListener(e -> carregarReservas());

        limpar.addActionListener(e -> {
            campoPesquisa.setText("");
            comboStatus.setSelectedIndex(0);
            carregarReservas();
        });

        atualizar.addActionListener(e -> carregarReservas());
        imprimir.addActionListener(e -> imprimirRelatorio());
        excluir.addActionListener(e -> excluirReserva());
        fechar.addActionListener(e -> dispose());
    }

    private void carregarReservas() {
        modelo.setRowCount(0);

        String busca = campoPesquisa.getText().trim();
        String status = (String) comboStatus.getSelectedItem();

        String sql =
                "SELECT r.id_reserva, "
                + "h.nome_hospedi, h.sobrenome_hospedi, "
                + "q.numero, q.tipo, "
                + "r.data_entrada, r.data_saida, "
                + "r.valor_diaria, r.status "
                + "FROM reserva r "
                + "INNER JOIN hospedi h "
                + "ON h.id_hospedi = r.id_hospedi "
                + "INNER JOIN quarto q "
                + "ON q.id_quarto = r.id_quarto "
                + "WHERE (CAST(r.id_reserva AS TEXT) ILIKE ? "
                + "OR h.nome_hospedi ILIKE ? "
                + "OR h.sobrenome_hospedi ILIKE ? "
                + "OR CAST(q.numero AS TEXT) ILIKE ?) "
                + "AND (? = 'Todas' OR r.status = ?) "
                + "ORDER BY r.data_entrada DESC, "
                + "r.id_reserva DESC";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new SQLException(
                        "Não foi possível conectar ao banco.");
            }

            try (PreparedStatement ps =
                    conn.prepareStatement(sql)) {

                String termo = "%" + busca + "%";

                ps.setString(1, termo);
                ps.setString(2, termo);
                ps.setString(3, termo);
                ps.setString(4, termo);
                ps.setString(5, status);
                ps.setString(6, status);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        java.time.LocalDate entrada =
                                rs.getDate("data_entrada")
                                        .toLocalDate();

                        java.time.LocalDate saida =
                                rs.getDate("data_saida")
                                        .toLocalDate();

                        long diarias =
                                java.time.temporal.ChronoUnit.DAYS
                                        .between(entrada, saida);

                        java.math.BigDecimal diaria =
                                rs.getBigDecimal("valor_diaria");

                        java.math.BigDecimal total =
                                diaria.multiply(
                                        java.math.BigDecimal.valueOf(
                                                diarias));

                        modelo.addRow(new Object[]{
                            rs.getInt("id_reserva"),
                            rs.getString("nome_hospedi") + " "
                                    + rs.getString("sobrenome_hospedi"),
                            rs.getString("numero"),
                            rs.getString("tipo"),
                            entrada.format(
                                    java.time.format.DateTimeFormatter
                                            .ofPattern("dd/MM/uuuu")),
                            saida.format(
                                    java.time.format.DateTimeFormatter
                                            .ofPattern("dd/MM/uuuu")),
                            diarias,
                            moeda(diaria),
                            moeda(total),
                            rs.getString("status")
                        });
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao consultar reservas:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirReserva() {
        // Verifica se o usuário tem acesso de administrador.
        if (!SessaoUsuario.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                    "Somente o administrador pode excluir reservas.",
                    "Acesso negado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int linhaSelecionada = tabela.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma reserva na tabela para excluir.",
                    "Nenhuma reserva selecionada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Considera a ordenação aplicada pelo usuário na tabela.
        int linhaModelo =
                tabela.convertRowIndexToModel(linhaSelecionada);

        int codigo = Integer.parseInt(
                modelo.getValueAt(linhaModelo, 0).toString());

        String hospede =
                modelo.getValueAt(linhaModelo, 1).toString();

        String quarto =
                modelo.getValueAt(linhaModelo, 2).toString();

        String entrada =
                modelo.getValueAt(linhaModelo, 4).toString();

        String saida =
                modelo.getValueAt(linhaModelo, 5).toString();

        String status =
                modelo.getValueAt(linhaModelo, 9).toString();

        // Impede a exclusão de reservas em andamento ou finalizadas.
        if ("Em andamento".equalsIgnoreCase(status)
                || "Finalizada".equalsIgnoreCase(status)) {

            JOptionPane.showMessageDialog(this,
                    "Não é permitido excluir uma reserva com status "
                    + status + ".\n"
                    + "Preserve o histórico da hospedagem.",
                    "Exclusão não permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir esta reserva?\n\n"
                + "Código: " + codigo
                + "\nHóspede: " + hospede
                + "\nQuarto: " + quarto
                + "\nEntrada: " + entrada
                + "\nSaída: " + saida
                + "\nStatus: " + status
                + "\n\nEsta operação não poderá ser desfeita.",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM reserva "
                + "WHERE id_reserva = ? "
                + "AND status IN ('Reservada', 'Cancelada')";

        try (Connection conn = ConectionFactory.conector()) {
            if (conn == null) {
                throw new SQLException(
                        "Não foi possível conectar ao banco.");
            }

            try (PreparedStatement ps =
                    conn.prepareStatement(sql)) {

                ps.setInt(1, codigo);

                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas == 0) {
                    JOptionPane.showMessageDialog(this,
                            "A reserva não foi excluída.\n"
                            + "Ela pode ter sido alterada ou "
                            + "não estar mais disponível para exclusão.",
                            "Operação não realizada",
                            JOptionPane.WARNING_MESSAGE);
                    carregarReservas();
                    return;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Reserva " + codigo
                    + " excluída com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            carregarReservas();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível excluir a reserva.\n"
                    + "Verifique se existem registros vinculados "
                    + "a ela.\n\n"
                    + "Detalhes: " + e.getMessage(),
                    "Erro ao excluir",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String moeda(java.math.BigDecimal valor) {
        return java.text.NumberFormat.getCurrencyInstance(
                new java.util.Locale("pt", "BR"))
                .format(valor);
    }

    private void imprimirRelatorio() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Não há reservas para imprimir.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            tabela.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat(
                            "Hotel Imperial - Relatório de Reservas"),
                    new java.text.MessageFormat("Página {0}"));

        } catch (java.awt.print.PrinterException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao imprimir:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
