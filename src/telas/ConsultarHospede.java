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
public class ConsultarHospede extends javax.swing.JInternalFrame {

    /**
     * Creates new form ConsultarHospede
     */
    public ConsultarHospede() {
        initComponents();
         setClosable(true);
        setMaximizable(true);
        setResizable(true);
        pesquisarHospedes("");
    }

    public void pesquisarHospedes(String campo) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaConsulta.getModel();
        modelo.setRowCount(0); // Limpa os resultados anteriores da tabela

        // Note o espaço depois de 'cidade ' para não colar com o 'FROM'
        String sql = "SELECT id_hospedi, nome_hospedi, sobrenome_hospedi, cpf_hospedi, cidade_hospedi, cell_hospedi, tipo_hospedi "
                   + "FROM hospedi "
                   + "WHERE CAST(id_hospedi AS TEXT) ILIKE ? "
                   + "   OR nome_hospedi ILIKE ? "
                   + "   OR sobrenome_hospedi ILIKE ? "
                   + "   OR COALESCE(cpf_hospedi, '') ILIKE ? "
                   + "   OR COALESCE(cidade_hospedi, '') ILIKE ? "
                   + "   OR COALESCE(cell_hospedi, '') ILIKE ? "
                   + "   OR COALESCE(tipo_hospedi, '') ILIKE ? "
                   + "ORDER BY id_hospedi";

        try (Connection conn = conexao.ConectionFactory.conector();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // O marcador % em volta faz a busca encontrar o termo em qualquer parte do texto
            String parametro = "%" + campo.trim() + "%";

            // Preenche os 7 parâmetros da consulta
            for (int i = 1; i <= 7; i++) {
                stmt.setString(i, parametro);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_hospedi"),
                    rs.getString("nome_hospedi"),
                    rs.getString("sobrenome_hospedi"),
                    rs.getString("cpf_hospedi"),
                    rs.getString("cidade_hospedi"),
                    rs.getString("cell_hospedi"),
                    rs.getString("tipo_hospedi")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar hóspedes: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaConsulta = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel1.setText("CONSULTA DE HÓSPEDES");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(279, 279, 279)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(campoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botaoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(270, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(314, 314, 314))
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
                            .addComponent(campoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addComponent(botaoPesquisar))
                .addGap(53, 53, 53))
        );

        tabelaConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "NOME", "SOBRENOME", "CPF", "CIDADE", "CELULAR", "TIPO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        if (tabelaConsulta.getColumnModel().getColumnCount() > 0) {
            tabelaConsulta.getColumnModel().getColumn(0).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(1).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(2).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(3).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(4).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(5).setResizable(false);
            tabelaConsulta.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 76, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void campoPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisarKeyReleased
        pesquisarHospedes(campoPesquisar.getText());
    }//GEN-LAST:event_campoPesquisarKeyReleased

    private void tabelaConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaConsultaMouseClicked
         if (evt.getClickCount() == 2) {

        int linha = tabelaConsulta.getSelectedRow();

            if (linha >= 0) {

                int linhaModelo =
                        tabelaConsulta.convertRowIndexToModel(linha);

                int idHospede = Integer.parseInt(
                        tabelaConsulta.getModel()
                                .getValueAt(linhaModelo, 0)
                                .toString()
                );

                DetalheHospede tela =
                        new DetalheHospede(idHospede);

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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaConsulta;
    // End of variables declaration//GEN-END:variables
}
