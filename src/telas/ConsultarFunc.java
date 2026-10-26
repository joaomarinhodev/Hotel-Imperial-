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
public class ConsultarFunc extends javax.swing.JInternalFrame {

    /**
     * Creates new form ConsultarFunc
     */
    public ConsultarFunc() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        pesquisarFuncionarios("");
    }

    
    public void pesquisarFuncionarios(String campo) {
    DefaultTableModel modelo = (DefaultTableModel) tabelaConsulta.getModel();
    modelo.setRowCount(0); // Limpa os resultados anteriores da tabela

    // Note o espaço depois de 'cargo_funcionario ' para não colar com o 'FROM'
    String sql = "SELECT id_funcionario, nome_funcionario, sobrenome_funcionario, cpf_funcionario, cargo_funcionario "
               + "FROM funcionarios "
               + "WHERE CAST(id_funcionario AS TEXT) ILIKE ? "
               + "   OR nome_funcionario ILIKE ? "
               + "   OR sobrenome_funcionario ILIKE ? "
               + "   OR COALESCE(cpf_funcionario, '') ILIKE ? "
               + "   OR COALESCE(cargo_funcionario, '') ILIKE ? "
               + "ORDER BY id_funcionario";

    try (Connection conn = conexao.ConectionFactory.conector();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        // O marcador % em volta faz a busca encontrar o termo em qualquer parte do texto
        String parametro = "%" + campo.trim() + "%";

        // Preenche os 5 parâmetros da consulta
        for (int i = 1; i <= 5; i++) {
            stmt.setString(i, parametro);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id_funcionario"),
                rs.getString("nome_funcionario"),
                rs.getString("sobrenome_funcionario"),
                rs.getString("cpf_funcionario"),
                rs.getString("cargo_funcionario"),
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Erro ao buscar funcionários: " + e.getMessage(), 
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        campoPesquisar = new javax.swing.JTextField();
        botaoPesquisar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaConsulta = new javax.swing.JTable();

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

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel1.setText("CONSULTA DE FUNCIONÁRIOS");

        tabelaConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "NOME", "SOBRENOME", "CPF", "CARGO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botaoPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                .addGap(257, 257, 257))
            .addComponent(jScrollPane1)
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
                .addGap(54, 54, 54)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
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

    private void campoPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisarKeyReleased
        pesquisarFuncionarios(campoPesquisar.getText());
    }//GEN-LAST:event_campoPesquisarKeyReleased

    private void campoPesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoPesquisarMouseClicked
        
    }//GEN-LAST:event_campoPesquisarMouseClicked

    private void tabelaConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaConsultaMouseClicked
        if (evt.getClickCount() == 2) {

            int linha = tabelaConsulta.getSelectedRow();

            if (linha >= 0) {

                int linhaModelo =
                        tabelaConsulta.convertRowIndexToModel(linha);

                int idFuncionario = Integer.parseInt(
                        tabelaConsulta.getModel()
                                .getValueAt(linhaModelo, 0)
                                .toString()
                );

                DetalheFuncionario tela = new DetalheFuncionario(
                        idFuncionario,
                        () -> pesquisarFuncionarios(campoPesquisar.getText())
                );

                tela.setLocationRelativeTo(null);
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
    private javax.swing.JTable tabelaConsulta;
    // End of variables declaration//GEN-END:variables
}
