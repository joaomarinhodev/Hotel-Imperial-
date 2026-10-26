/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Pichau
 */
public class ConsultarFornecedor extends javax.swing.JInternalFrame {

    /**
     * Creates new form ConsultarFornecedor
     */
    public ConsultarFornecedor() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        pesquisarFornecedores("");
    }

    public void pesquisarFornecedores(String campo) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaConsulta.getModel();
        modelo.setRowCount(0); // Limpa os resultados anteriores da tabela

        // Note o espaço depois de 'cidade ' para não colar com o 'FROM'
        String sql = "SELECT id_fornecedor, razao_social, cnpj, tipo_fornecedor, cell, cidade "
                   + "FROM fornecedor "
                   + "WHERE CAST(id_fornecedor AS TEXT) ILIKE ? "
                   + "   OR razao_social ILIKE ? "
                   + "   OR cnpj ILIKE ? "
                   + "   OR COALESCE(tipo_fornecedor, '') ILIKE ? "
                   + "   OR COALESCE(cell, '') ILIKE ? "
                   + "   OR COALESCE(cidade, '') ILIKE ? "
                   + "ORDER BY id_fornecedor";

        try (Connection conn = conexao.ConectionFactory.conector();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // O marcador % em volta faz a busca encontrar o termo em qualquer parte do texto
            String parametro = "%" + campo.trim() + "%";

            // Preenche os 6 parâmetros da consulta
            for (int i = 1; i <= 6; i++) {
                stmt.setString(i, parametro);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_fornecedor"),
                    rs.getString("razao_social"),
                    rs.getString("cnpj"),
                    rs.getString("tipo_fornecedor"),
                    rs.getString("cell"),
                    rs.getString("cidade")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar fornecedores: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        campoPesquisar = new javax.swing.JTextField();
        botaoPesquisar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaConsulta = new javax.swing.JTable();

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel1.setText("CONSULTA DE FORNECEDORES");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel2.setText("Pesquisar:");

        campoPesquisar.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        campoPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoPesquisarKeyReleased(evt);
            }
        });

        botaoPesquisar.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        botaoPesquisar.setText("Execultar");

        tabelaConsulta.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        tabelaConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "RAZÃO SOCIAL", "CNPJ", "TIPO", "TELEFONE", "CIDADE"
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
        jScrollPane3.setViewportView(tabelaConsulta);
        if (tabelaConsulta.getColumnModel().getColumnCount() > 0) {
            tabelaConsulta.getColumnModel().getColumn(0).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(1).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(2).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(3).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(4).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(274, 274, 274)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botaoPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(887, 887, 887))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(63, 63, 63)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campoPesquisar)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(botaoPesquisar))
                .addGap(47, 47, 47)
                .addComponent(jScrollPane3))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1199, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisarKeyReleased
        pesquisarFornecedores(campoPesquisar.getText());
    }//GEN-LAST:event_campoPesquisarKeyReleased

    private void tabelaConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaConsultaMouseClicked
        if (evt.getClickCount() == 2) {

            int linha = tabelaConsulta.getSelectedRow();

            if (linha >= 0) {

                int idFornecedor = Integer.parseInt(
                        tabelaConsulta.getValueAt(linha, 0).toString()
                );

                DetalheFornecedor tela = new DetalheFornecedor(
                    idFornecedor,
                    () -> pesquisarFornecedores(campoPesquisar.getText())
                );

                tela.setLocationRelativeTo(this);
                tela.setVisible(true);
            }
        }   
    }//GEN-LAST:event_tabelaConsultaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JTextField campoPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tabelaConsulta;
    // End of variables declaration//GEN-END:variables
}
