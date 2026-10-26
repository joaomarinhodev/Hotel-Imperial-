
package relatorios;

import conexao.ConectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RelatorioContasPagar extends JInternalFrame {

    private JTextField campoPesquisar;
    private JComboBox<String> comboStatus;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JLabel labelQuantidade;
    private JLabel labelTotal;

    private final DateTimeFormatter formatoBR =
            DateTimeFormatter.ofPattern("dd/MM/uuuu");

    public RelatorioContasPagar() {
        super("Hotel Imperial - Contas a Pagar",
                true, true, false, true);

        criarTela();
        pesquisarContas();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1150, 650);
    }
    
    
    


    private void criarTela() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("CONTAS A PAGAR");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitulo = new JLabel(
                "Consulte despesas, vencimentos e pagamentos");

        JPanel painelTitulo = new JPanel(
                new GridLayout(2, 1, 0, 5));

        painelTitulo.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 5, 15));

        painelTitulo.add(titulo);
        painelTitulo.add(subtitulo);

        add(painelTitulo, BorderLayout.NORTH);

        // FILTROS

        JPanel painelFiltros = new JPanel(new GridBagLayout());
        painelFiltros.setBorder(
                BorderFactory.createTitledBorder("Filtros"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFiltros.add(
                new JLabel("Pesquisar:"), gbc);

        campoPesquisar = new JTextField(25);

        gbc.gridx = 1;
        gbc.weightx = 1;
        painelFiltros.add(campoPesquisar, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        painelFiltros.add(
                new JLabel("Situação:"), gbc);

        comboStatus = new JComboBox<>(new String[]{
            "Todas",
            "Pendente",
            "Pago",
            "Atrasado"
        });

        gbc.gridx = 3;
        painelFiltros.add(comboStatus, gbc);

        JButton botaoPesquisar = new JButton("Pesquisar");
        JButton botaoLimpar = new JButton("Limpar");

        JPanel painelBotoesFiltro = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 5, 0));

        painelBotoesFiltro.add(botaoPesquisar);
        painelBotoesFiltro.add(botaoLimpar);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        painelFiltros.add(painelBotoesFiltro, gbc);

        // TABELA

        String[] colunas = {
            "ID",
            "Fornecedor",
            "Descrição",
            "Valor (R$)",
            "Emissão",
            "Vencimento",
            "Pagamento",
            "Situação"
        };

        modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(
                    int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setRowHeight(25);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollTabela = new JScrollPane(tabela);

        JPanel painelCentro = new JPanel(
                new BorderLayout(10, 10));

        painelCentro.setBorder(
                BorderFactory.createEmptyBorder(0, 15, 0, 15));

        painelCentro.add(painelFiltros, BorderLayout.NORTH);
        painelCentro.add(scrollTabela, BorderLayout.CENTER);

        add(painelCentro, BorderLayout.CENTER);

        // RODAPÉ

        JPanel painelRodape = new JPanel(
                new BorderLayout(10, 10));

        painelRodape.setBorder(
                BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JPanel painelResumo = new JPanel(
                new GridLayout(2, 1, 0, 5));

        labelQuantidade = new JLabel("Contas encontradas: 0");
        labelTotal = new JLabel("Total das contas: R$ 0,00");

        labelQuantidade.setFont(
                new Font("Arial", Font.BOLD, 13));
        labelTotal.setFont(
                new Font("Arial", Font.BOLD, 13));

        painelResumo.add(labelQuantidade);
        painelResumo.add(labelTotal);

        JPanel painelBotoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 5));

        JButton botaoNovaConta = new JButton("Nova conta");
        JButton botaoPagar = new JButton("Marcar como Pago");
        JButton botaoImprimir = new JButton("Imprimir");
        JButton botaoFechar = new JButton("Fechar");

        painelBotoes.add(botaoNovaConta);
        painelBotoes.add(botaoPagar);
        painelBotoes.add(botaoImprimir);
        painelBotoes.add(botaoFechar);
        
        

        painelRodape.add(painelResumo, BorderLayout.WEST);
        painelRodape.add(painelBotoes, BorderLayout.EAST);

        add(painelRodape, BorderLayout.SOUTH);
        

        // EVENTOS

        campoPesquisar.addActionListener(
                e -> pesquisarContas());

        campoPesquisar.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(
                    javax.swing.event.DocumentEvent e) {
                pesquisarContas();
            }

            @Override
            public void removeUpdate(
                    javax.swing.event.DocumentEvent e) {
                pesquisarContas();
            }

            @Override
            public void changedUpdate(
                    javax.swing.event.DocumentEvent e) {
                pesquisarContas();
            }
        });

        comboStatus.addActionListener(
                e -> pesquisarContas());

        botaoPesquisar.addActionListener(
                e -> pesquisarContas());

        botaoLimpar.addActionListener(e -> {
            campoPesquisar.setText("");
            comboStatus.setSelectedIndex(0);
            pesquisarContas();
        });

        botaoPagar.addActionListener(
                e -> marcarComoPago());

        botaoImprimir.addActionListener(
                e -> imprimirRelatorio());

        botaoFechar.addActionListener(
                e -> dispose());
        
        botaoNovaConta.addActionListener(e -> {
            CadastroContasPagar cadastro =
                    new CadastroContasPagar();

            cadastro.setAoSalvar(() -> pesquisarContas());
            
            getDesktopPane().add(cadastro);
            cadastro.setVisible(true);

            try {
                cadastro.setMaximum(true);
            } catch (java.beans.PropertyVetoException ex) {
                ex.printStackTrace();
            }

            cadastro.toFront();
        });
    }
    
    
    
    

    // PESQUISAR CONTAS

    private void pesquisarContas() {

        modelo.setRowCount(0);

        String busca = campoPesquisar.getText().trim();
        String filtro = comboStatus.getSelectedItem().toString();

        String sql =
                "SELECT cp.id_conta_pagar, "
              + "COALESCE(f.razao_social, 'Sem fornecedor') AS fornecedor, "
              + "cp.descricao, cp.valor, cp.data_emissao, "
              + "cp.data_vencimento, cp.data_pagamento, cp.status "
              + "FROM contas_pagar cp "
              + "LEFT JOIN fornecedor f "
              + "ON cp.id_fornecedor = f.id_fornecedor "
              + "WHERE (CAST(cp.id_conta_pagar AS TEXT) ILIKE ? "
              + "OR cp.descricao ILIKE ? "
              + "OR COALESCE(f.razao_social, '') ILIKE ?) "
              + "ORDER BY cp.data_vencimento";

        int quantidade = 0;
        BigDecimal total = BigDecimal.ZERO;
        LocalDate hoje = LocalDate.now();

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            String termo = "%" + busca + "%";

            pst.setString(1, termo);
            pst.setString(2, termo);
            pst.setString(3, termo);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    String statusBanco = rs.getString("status");

                    Date vencimentoSQL =
                            rs.getDate("data_vencimento");

                    Date pagamentoSQL =
                            rs.getDate("data_pagamento");

                    LocalDate vencimento =
                            vencimentoSQL.toLocalDate();

                    LocalDate pagamento = pagamentoSQL == null
                            ? null : pagamentoSQL.toLocalDate();

                    String situacao = statusBanco;

                    // Identifica atraso sem alterar o banco.
                    if (!"Pago".equalsIgnoreCase(statusBanco)
                            && vencimento.isBefore(hoje)) {
                        situacao = "Atrasado";
                    }

                    if (!"Todas".equals(filtro)
                            && !filtro.equalsIgnoreCase(situacao)) {
                        continue;
                    }

                    String emissao = rs.getDate("data_emissao")
                            .toLocalDate().format(formatoBR);

                    String vencimentoBR =
                            vencimento.format(formatoBR);

                    String pagamentoBR = pagamento == null
                            ? "-"
                            : pagamento.format(formatoBR);

                    BigDecimal valor = rs.getBigDecimal("valor");

                    modelo.addRow(new Object[]{
                        rs.getInt("id_conta_pagar"),
                        rs.getString("fornecedor"),
                        rs.getString("descricao"),
                        valor,
                        emissao,
                        vencimentoBR,
                        pagamentoBR,
                        situacao
                    });

                    quantidade++;

                    // Totaliza somente as contas não pagas.
                    if (!"Pago".equalsIgnoreCase(situacao)) {
                        total = total.add(valor);
                    }
                }
            }

            java.text.NumberFormat moeda =
                    java.text.NumberFormat.getCurrencyInstance(
                            new java.util.Locale("pt", "BR"));

            labelQuantidade.setText(
                    "Contas encontradas: " + quantidade);

            labelTotal.setText(
                    "Total em aberto: " + moeda.format(total));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao consultar contas a pagar:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MARCAR CONTA COMO PAGA

    private void marcarComoPago() {

        int linha = tabela.getSelectedRow();

        if (linha < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma conta na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int linhaModelo = tabela.convertRowIndexToModel(linha);

        int id = Integer.parseInt(
                modelo.getValueAt(linhaModelo, 0).toString());

        String status = modelo.getValueAt(
                linhaModelo, 7).toString();

        if ("Pago".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Essa conta já está paga.",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja registrar o pagamento desta conta hoje?",
                "Confirmar pagamento",
                JOptionPane.YES_NO_OPTION);

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "UPDATE contas_pagar "
              + "SET status = 'Pago', data_pagamento = ? "
              + "WHERE id_conta_pagar = ? "
              + "AND status <> 'Pago'";

        try (Connection conn = ConectionFactory.conector();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setDate(1, Date.valueOf(LocalDate.now()));
            pst.setInt(2, id);

            int atualizadas = pst.executeUpdate();

            if (atualizadas > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pagamento registrado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "A conta não foi atualizada. Consulte novamente.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
            }

            pesquisarContas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao registrar pagamento:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // IMPRIMIR

    private void imprimirRelatorio() {

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não há contas para imprimir.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            tabela.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat(
                            "Hotel Imperial - Contas a Pagar"),
                    new java.text.MessageFormat("Página {0}"));

        } catch (java.awt.print.PrinterException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao imprimir:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
