/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 *
 * @author Pichau
 */
public class ConsultarQuarto extends javax.swing.JInternalFrame {
    private DefaultTableModel modeloTabela;

    public ConsultarQuarto() {

        initComponents();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        setDefaultCloseOperation(
                javax.swing.JInternalFrame.DISPOSE_ON_CLOSE
        );

        configurarTabela();

        pesquisarQuartos("");
    }

    // =========================================================
    // CONFIGURAR TABELA
    // =========================================================

    private void configurarTabela() {

        modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID",
                    "NÚMERO",
                    "TIPO",
                    "VALOR DIÁRIA",
                    "CAPACIDADE",
                    "ANDAR"
                }
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaConsulta.setModel(modeloTabela);

        tabelaConsulta.setAutoCreateRowSorter(true);
    }

    // =========================================================
    // PESQUISAR QUARTOS
    // =========================================================

    private void pesquisarQuartos(String pesquisa) {

        modeloTabela.setRowCount(0);

        String sql =
                "SELECT id_quarto, numero, tipo, valor_diaria, "
                + "capacidade, andar "
                + "FROM quarto "
                + "WHERE CAST(numero AS TEXT) ILIKE ? "
                + "OR tipo ILIKE ? "
                + "OR CAST(capacidade AS TEXT) ILIKE ? "
                + "OR andar ILIKE ? "
                + "ORDER BY numero";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            String filtro = "%" + pesquisa.trim() + "%";

            pst.setString(1, filtro);
            pst.setString(2, filtro);
            pst.setString(3, filtro);
            pst.setString(4, filtro);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                modeloTabela.addRow(new Object[]{
                    rs.getInt("id_quarto"),
                    rs.getString("numero"),
                    rs.getString("tipo"),
                    rs.getDouble("valor_diaria"),
                    rs.getInt("capacidade"),
                    rs.getString("andar")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao consultar quartos:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        campoPesquisar = new javax.swing.JTextField();
        botaoPesquisar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaConsulta = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel1.setText("CONSULTA DE QUARTOS");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel2.setText("Pesquisar:");

        campoPesquisar.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        campoPesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                campoPesquisarMouseClicked(evt);
            }
        });
        campoPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoPesquisarKeyReleased(evt);
            }
        });

        botaoPesquisar.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        botaoPesquisar.setText("Execultar");
        botaoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisarActionPerformed(evt);
            }
        });

        tabelaConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NÚMERO", "TIPO", "VALOR DA DIÁRIA", "CAPACIDADE", "ANDAR"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaConsultaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaConsulta);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(campoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botaoPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                .addGap(175, 175, 175))
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(247, 247, 247))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campoPesquisar)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(botaoPesquisar))
                .addGap(67, 67, 67)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoPesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoPesquisarMouseClicked

    }//GEN-LAST:event_campoPesquisarMouseClicked

    private void campoPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisarKeyReleased
        pesquisarQuartos(campoPesquisar.getText());
    }//GEN-LAST:event_campoPesquisarKeyReleased

    private void tabelaConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaConsultaMouseClicked
        if (evt.getClickCount() == 2) {

        int linha = tabelaConsulta.getSelectedRow();

            if (linha >= 0) {

                int linhaModelo =
                        tabelaConsulta.convertRowIndexToModel(linha);

                int idQuarto = Integer.parseInt(
                        modeloTabela
                                .getValueAt(linhaModelo, 0)
                                .toString()
                );

                DetalheQuarto tela = new DetalheQuarto(
                        idQuarto,
                        () -> pesquisarQuartos(
                                campoPesquisar.getText()
                        )
                );

                tela.setLocationRelativeTo(null);
                tela.setVisible(true);
            }
        }
    }//GEN-LAST:event_tabelaConsultaMouseClicked

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        pesquisarQuartos(campoPesquisar.getText());
    }//GEN-LAST:event_botaoPesquisarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JTextField campoPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaConsulta;
    // End of variables declaration//GEN-END:variables
}
