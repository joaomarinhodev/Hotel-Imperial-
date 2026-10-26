/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author User
 */
public class CadastroHospede extends javax.swing.JInternalFrame {
        ValidarSomLetras vsl = new ValidarSomLetras();
        ValidarSomNum vsn = new ValidarSomNum();
        VerificarCampoVazio vcv = new VerificarCampoVazio();
        VerificarJboxVazio vjv = new VerificarJboxVazio();
        
        private ButtonGroup grupoHospede;
        
    public CadastroHospede() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        setDefaultCloseOperation(
            JInternalFrame.DISPOSE_ON_CLOSE
        );
        fundoTabela.setVisible(false);
        limparBuscaButtonHosp.setVisible(false);
        
        // Grupo dos RadioButtons
        grupoHospede = new ButtonGroup();

        grupoHospede.add(comumRadio);
        grupoHospede.add(vipRadio);
        grupoHospede.add(empresarioRadio);
    }

    private void limparCampos(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja limpar todos os campos?", "Confirmação",
                                                    JOptionPane.YES_NO_OPTION);
        if(resposta == JOptionPane.YES_OPTION){
        nomeHosp.setText("");
        sobreNomeHosp.setText("");
        dataNascHosp.setText("");
        cpfHosp.setText("");
        rgHosp.setText("");
        sexoHosp.setSelectedItem(0);
        emailHosp.setText("");
        celularHosp.setText("");
        telFixoHosp.setText("");
        ruaHosp.setText("");
        bairroHosp.setText("");
        complementoCasaHosp.setText("");
        cidadeHosp.setText("");
        cepHosp.setText("");
        numCasaHosp.setText("");
        estadoHosp.setSelectedItem(0);
        nomeHosp.setText("");
        obsHosp.setText("");
        }
    }
    
    
    
    private boolean validarCamposObrigatorios(){
        boolean erro = false;
        erro |= vcv.campoVazio(nomeHosp);
        erro |= vcv.campoVazio(sobreNomeHosp);
        erro |= vcv.campoVazio(dataNascHosp);
        erro |= vcv.campoVazio(cpfHosp);
        
        erro |= vjv.campoVazio(sexoHosp);
        erro |= vcv.campoVazio(emailHosp);
        erro |= vcv.campoVazio(celularHosp);
        
        erro |= vcv.campoVazio(ruaHosp);
        erro |= vcv.campoVazio(bairroHosp);
        
        erro |= vcv.campoVazio(cidadeHosp);
        erro |= vcv.campoVazio(cepHosp);
        erro |= vcv.campoVazio(numCasaHosp);
        erro |= vjv.campoVazio(estadoHosp);
        
        // VALIDAÇÃO DOS RADIO BUTTONS
        if (grupoHospede.getSelection() == null) {
            erro = true;
        }

        if (erro) {
            JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios!");
            return false;
        }

    return true;    
    }
    
    private void pesquisarHospedes(String nome) {

        javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) jTable1.getModel();

        modelo.setRowCount(0);

        String sql =
                "SELECT id_hospedi, nome_hospedi, cpf_hospedi, cell_hospedi "
                + "FROM hospedi "
                + "WHERE CAST(id_hospedi AS TEXT) ILIKE ? "
                + "OR nome_hospedi ILIKE ? "
                + "OR sobrenome_hospedi ILIKE ? "
                + "OR COALESCE(cpf_hospedi, '') ILIKE ? "
                + "OR COALESCE(cell_hospedi, '') ILIKE ? "
                + "ORDER BY id_hospedi";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            String parametro = "%" + nome.trim() + "%";

            for (int i = 1; i <= 5; i++) {
                pst.setString(i, parametro);
            }

            java.sql.ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                modelo.addRow(new Object[]{
                    rs.getInt("id_hospedi"),
                    rs.getString("nome_hospedi"),
                    rs.getString("cpf_hospedi"),
                    rs.getString("cell_hospedi")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao buscar hóspedes:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void salvarHospede(){

    try {

        Connection conn = conexao.ConectionFactory.conector();

        String sql = "INSERT INTO hospedi("
        + "nome_hospedi, sobrenome_hospedi, "
        + "datanasc_hospedi, cpf_hospedi, "
        + "rg_hospedi, sexo_hospedi, email_hospedi, "
        + "cell_hospedi, tellfixo_hospedi, "
        + "rua_hospedi, bairro_hospedi, "
        + "complemento_hospedi, cidade_hospedi, "
        + "cep_hospedi, numero_casa_hospedi, "
        + "estado_hospedi, observacoes_hospedi, tipo_hospedi"
        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);

        // DATA
        LocalDate data = LocalDate.parse(dataNascHosp.getText(),
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        java.sql.Date dataSql = java.sql.Date.valueOf(data);
        
        
        
        
        // CAMPOS
        pst.setString(1, nomeHosp.getText());
        pst.setString(2, sobreNomeHosp.getText());
        pst.setDate(3, dataSql);
        pst.setString(4, cpfHosp.getText());

        // RG (tratamento opcional)
        String rg = rgHosp.getText().replaceAll("[^0-9]", "");
        if (rg.isEmpty()) {
            pst.setNull(5, java.sql.Types.VARCHAR);
        } else {
            pst.setString(5, rg);
        }

        pst.setString(6, sexoHosp.getSelectedItem().toString());
        pst.setString(7, emailHosp.getText());
        pst.setString(8, celularHosp.getText());

        // TELEFONE FIXO (opcional)
        String telFixo = telFixoHosp.getText().replaceAll("[^0-9]", "");
        if (telFixo.isEmpty()) {
            pst.setNull(9, java.sql.Types.VARCHAR);
        } else {
            pst.setString(9, telFixo);
        }

        pst.setString(10, ruaHosp.getText());
        pst.setString(11, bairroHosp.getText());

        // COMPLEMENTO (opcional)
        String complemento = complementoCasaHosp.getText().trim();
        if (complemento.isEmpty()) {
            pst.setNull(12, java.sql.Types.VARCHAR);
        } else {
            pst.setString(12, complemento);
        }

        pst.setString(13, cidadeHosp.getText());
        pst.setString(14, cepHosp.getText());
        pst.setString(15, numCasaHosp.getText());
        pst.setString(16, estadoHosp.getSelectedItem().toString());
        pst.setString(17, obsHosp.getText());
        
        String tipoHospede = null;

            if (comumRadio.isSelected()) {
                tipoHospede = "Comum";

            } else if (vipRadio.isSelected()) {
                tipoHospede = "VIP";

            } else if (empresarioRadio.isSelected()) {
            tipoHospede = "Empresário";
            }
        
        pst.setString(18,tipoHospede);
        
        
        // EXECUTAR
        pst.executeUpdate();

        JOptionPane.showMessageDialog(null, "Hóspede cadastrado com sucesso!");

        pst.close();
        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Erro ao cadastrar hóspede:\n" + e.getMessage());
    }
}
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        emailFunc3 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        celularFunc3 = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        telFixoFunc3 = new javax.swing.JFormattedTextField();
        atualizarButtonFunc = new javax.swing.JButton();
        cancelarEdicaoButtonFunc = new javax.swing.JButton();
        salvarButtonFunc = new javax.swing.JButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        atualizarButtonFunc1 = new javax.swing.JButton();
        cancelarEdicaoButtonFunc1 = new javax.swing.JButton();
        salvarButtonFunc1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        BuscarNomeHosp = new javax.swing.JTextField();
        buscarNomeButtonHosp = new javax.swing.JButton();
        limparBuscaButtonHosp = new javax.swing.JButton();
        fundoTabela = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        editarCadButtonHosp = new javax.swing.JButton();
        excluirCadButtonHosp = new javax.swing.JButton();
        tabelaCadFunc = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nomeHosp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        sobreNomeHosp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cpfHosp = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        dataNascHosp = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        rgHosp = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        sexoHosp = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        emailHosp = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        celularHosp = new javax.swing.JFormattedTextField();
        jLabel29 = new javax.swing.JLabel();
        telFixoHosp = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        ruaHosp = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        bairroHosp = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        cidadeHosp = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        cepHosp = new javax.swing.JFormattedTextField();
        jLabel34 = new javax.swing.JLabel();
        complementoCasaHosp = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        numCasaHosp = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        estadoHosp = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        comumRadio = new javax.swing.JRadioButton();
        vipRadio = new javax.swing.JRadioButton();
        empresarioRadio = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        obsHosp = new javax.swing.JTextArea();
        cancelarEdicaoButtonHosp = new javax.swing.JButton();
        salvarButtonHosp = new javax.swing.JButton();

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONTATO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setText("*E-mail:");

        emailFunc3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setText("*Celular:");

        try {
            celularFunc3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        celularFunc3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setText("Tel.Fixo:");

        try {
            telFixoFunc3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telFixoFunc3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(emailFunc3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(85, 85, 85)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(celularFunc3, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(telFixoFunc3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailFunc3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celularFunc3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telFixoFunc3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        atualizarButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        atualizarButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-atualizar-20.png"))); // NOI18N
        atualizarButtonFunc.setText("Atualizar");
        atualizarButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        cancelarEdicaoButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cancelarEdicaoButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-saída-32.png"))); // NOI18N
        cancelarEdicaoButtonFunc.setText("Cancelar Edição");
        cancelarEdicaoButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        salvarButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salvarButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        salvarButtonFunc.setText("Salvar");
        salvarButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        atualizarButtonFunc1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        atualizarButtonFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-atualizar-20.png"))); // NOI18N
        atualizarButtonFunc1.setText("Atualizar");
        atualizarButtonFunc1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        cancelarEdicaoButtonFunc1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cancelarEdicaoButtonFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-saída-32.png"))); // NOI18N
        cancelarEdicaoButtonFunc1.setText("Cancelar Edição");
        cancelarEdicaoButtonFunc1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        salvarButtonFunc1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salvarButtonFunc1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        salvarButtonFunc1.setText("Salvar");
        salvarButtonFunc1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jScrollPane1.setHorizontalScrollBar(null);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONSULTAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel24.setText("Antes de realizar um cadastro, verifique se a pessoa já não foi cadastrada!");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel25.setText("Buscar por nome");

        BuscarNomeHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        BuscarNomeHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        BuscarNomeHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarNomeHospActionPerformed(evt);
            }
        });
        BuscarNomeHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BuscarNomeHospKeyTyped(evt);
            }
        });

        buscarNomeButtonHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        buscarNomeButtonHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-procurar-19.png"))); // NOI18N
        buscarNomeButtonHosp.setText("Buscar");
        buscarNomeButtonHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buscarNomeButtonHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarNomeButtonHospActionPerformed(evt);
            }
        });

        limparBuscaButtonHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        limparBuscaButtonHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-vassoura-20.png"))); // NOI18N
        limparBuscaButtonHosp.setText("Limpar busca");
        limparBuscaButtonHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        limparBuscaButtonHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparBuscaButtonHospActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel26.setText("Para editar ou excluir um cadastro, basta selecionar na tabela e clicar no botão desejado");

        editarCadButtonHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        editarCadButtonHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-editar-20.png"))); // NOI18N
        editarCadButtonHosp.setText("Editar");
        editarCadButtonHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editarCadButtonHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarCadButtonHospActionPerformed(evt);
            }
        });

        excluirCadButtonHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        excluirCadButtonHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-lixeira-20.png"))); // NOI18N
        excluirCadButtonHosp.setText("Excluir");
        excluirCadButtonHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        excluirCadButtonHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirCadButtonHospActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fundoTabelaLayout = new javax.swing.GroupLayout(fundoTabela);
        fundoTabela.setLayout(fundoTabelaLayout);
        fundoTabelaLayout.setHorizontalGroup(
            fundoTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fundoTabelaLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel26)
                .addGap(49, 49, 49)
                .addComponent(editarCadButtonHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(excluirCadButtonHosp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        fundoTabelaLayout.setVerticalGroup(
            fundoTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fundoTabelaLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(fundoTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(editarCadButtonHosp)
                    .addComponent(excluirCadButtonHosp))
                .addContainerGap())
        );

        tabelaCadFunc.setBorder(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "NOME", "CPF", "TELEFONE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaCadFunc.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addGap(195, 195, 195))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fundoTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabelaCadFunc))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BuscarNomeHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buscarNomeButtonHosp)
                .addGap(18, 18, 18)
                .addComponent(limparBuscaButtonHosp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(BuscarNomeHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarNomeButtonHosp)
                    .addComponent(limparBuscaButtonHosp))
                .addGap(34, 34, 34)
                .addComponent(tabelaCadFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fundoTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS PESSOAIS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("*Nome:");

        nomeHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nomeHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nomeHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeHospActionPerformed(evt);
            }
        });
        nomeHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomeHospKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("*Sobrenome:");

        sobreNomeHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sobreNomeHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sobreNomeHospKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("*CPF:");

        try {
            cpfHosp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cpfHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cpfHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cpfHospKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("*Data de Nascimento:");

        try {
            dataNascHosp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataNascHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        dataNascHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                dataNascHospKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("RG:");

        try {
            rgHosp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        rgHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("*Sexo:");

        sexoHosp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Masculino", "Feminino" }));
        sexoHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel9.setFont(new java.awt.Font("Nirmala Text", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 51));
        jLabel9.setText("O símbolo (*) indica que o campo deve ser preenchido obrigatóriamente!");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(nomeHosp)
                            .addComponent(cpfHosp, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                        .addGap(114, 114, 114)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(rgHosp, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                            .addComponent(sobreNomeHosp))
                        .addGap(137, 137, 137)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataNascHosp)
                            .addComponent(jLabel7)
                            .addComponent(sexoHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(277, 277, 277)
                        .addComponent(jLabel9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sobreNomeHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataNascHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cpfHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rgHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sexoHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 100, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONTATO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel27.setText("*E-mail:");

        emailHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel28.setText("*Celular:");

        try {
            celularHosp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        celularHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setText("Tel.Fixo:");

        try {
            telFixoHosp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telFixoHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(emailHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(85, 85, 85)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(celularHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(telFixoHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celularHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telFixoHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENDEREÇO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel30.setText("*Rua:");

        ruaHosp.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        ruaHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ruaHospKeyTyped(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel31.setText("*Bairro:");

        bairroHosp.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        bairroHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bairroHospKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel32.setText("*Cidade:");

        cidadeHosp.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cidadeHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cidadeHospActionPerformed(evt);
            }
        });
        cidadeHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cidadeHospKeyTyped(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel33.setText("*CEP:");

        try {
            cepHosp.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cepHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setText("Complemento:");

        complementoCasaHosp.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        complementoCasaHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                complementoCasaHospKeyTyped(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel35.setText("*Número:");

        numCasaHosp.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        numCasaHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numCasaHospActionPerformed(evt);
            }
        });
        numCasaHosp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numCasaHospKeyTyped(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel36.setText("*Estado:");

        estadoHosp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PE", "PR", "PB", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        estadoHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(ruaHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel32)
                        .addComponent(cidadeHosp, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(cepHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(167, 167, 167))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(numCasaHosp)
                                .addGap(73, 73, 73))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(bairroHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel34)
                            .addComponent(complementoCasaHosp)
                            .addComponent(jLabel36)
                            .addComponent(estadoHosp, 0, 142, Short.MAX_VALUE))
                        .addGap(24, 24, 24))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bairroHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jLabel30)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ruaHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                            .addComponent(jLabel34)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(complementoCasaHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel35)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(numCasaHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cepHosp))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cidadeHosp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OBSERVAÇÕES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel1.setText("*Tipo de Hóspede:");

        comumRadio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        comumRadio.setText("Comum");

        vipRadio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        vipRadio.setText("VIP");

        empresarioRadio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        empresarioRadio.setText("Empresário");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel10.setText("Observações:");

        obsHosp.setColumns(20);
        obsHosp.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        obsHosp.setRows(5);
        jScrollPane2.setViewportView(obsHosp);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(empresarioRadio)
                    .addComponent(vipRadio)
                    .addComponent(comumRadio))
                .addGap(150, 150, 150)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(comumRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vipRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(empresarioRadio))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        cancelarEdicaoButtonHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cancelarEdicaoButtonHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-vassoura-20.png"))); // NOI18N
        cancelarEdicaoButtonHosp.setText("Limpar Tudo");
        cancelarEdicaoButtonHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cancelarEdicaoButtonHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarEdicaoButtonHospActionPerformed(evt);
            }
        });

        salvarButtonHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salvarButtonHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        salvarButtonHosp.setText("Salvar");
        salvarButtonHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        salvarButtonHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarButtonHospActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(63, 63, 63))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelarEdicaoButtonHosp)
                .addGap(27, 27, 27)
                .addComponent(salvarButtonHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelarEdicaoButtonHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salvarButtonHosp, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(256, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1154, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1803, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BuscarNomeHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarNomeHospActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuscarNomeHospActionPerformed

    private void BuscarNomeHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarNomeHospKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(BuscarNomeHosp, evt);
    }//GEN-LAST:event_BuscarNomeHospKeyTyped

    private void buscarNomeButtonHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarNomeButtonHospActionPerformed
        // TODO add your handling code here:
        fundoTabela.setVisible(true);
        limparBuscaButtonHosp.setVisible(true);
        buscarNomeButtonHosp.setVisible(false);
        
        pesquisarHospedes(BuscarNomeHosp.getText());
    }//GEN-LAST:event_buscarNomeButtonHospActionPerformed

    private void limparBuscaButtonHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparBuscaButtonHospActionPerformed
        // TODO add your handling code here:
        fundoTabela.setVisible(false);
        limparBuscaButtonHosp.setVisible(false);
        buscarNomeButtonHosp.setVisible(true);
    }//GEN-LAST:event_limparBuscaButtonHospActionPerformed

    private void nomeHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeHospActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeHospActionPerformed

    private void nomeHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeHospKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(nomeHosp, evt);
    }//GEN-LAST:event_nomeHospKeyTyped

    private void sobreNomeHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sobreNomeHospKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(sobreNomeHosp, evt);
    }//GEN-LAST:event_sobreNomeHospKeyTyped

    private void cpfHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cpfHospKeyTyped
        // TODO add your handling code here:
        vsn.verificarSomenteNumeros(cpfHosp, evt);
    }//GEN-LAST:event_cpfHospKeyTyped

    private void dataNascHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataNascHospKeyTyped
        // TODO add your handling code here:
        vsn.verificarSomenteNumeros(dataNascHosp, evt);
    }//GEN-LAST:event_dataNascHospKeyTyped

    private void ruaHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ruaHospKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(ruaHosp, evt);
    }//GEN-LAST:event_ruaHospKeyTyped

    private void bairroHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bairroHospKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(bairroHosp, evt);
    }//GEN-LAST:event_bairroHospKeyTyped

    private void cidadeHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cidadeHospActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cidadeHospActionPerformed

    private void cidadeHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cidadeHospKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(cidadeHosp, evt);
    }//GEN-LAST:event_cidadeHospKeyTyped

    private void complementoCasaHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_complementoCasaHospKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(complementoCasaHosp, evt);
    }//GEN-LAST:event_complementoCasaHospKeyTyped

    private void numCasaHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numCasaHospActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_numCasaHospActionPerformed

    private void numCasaHospKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCasaHospKeyTyped
        // TODO add your handling code here:
        vsn.verificarSomenteNumeros(numCasaHosp, evt);
    }//GEN-LAST:event_numCasaHospKeyTyped

    private void salvarButtonHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarButtonHospActionPerformed
        if(validarCamposObrigatorios()){
            salvarHospede();
        }     
    }//GEN-LAST:event_salvarButtonHospActionPerformed

    private void cancelarEdicaoButtonHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarEdicaoButtonHospActionPerformed
        limparCampos();
    }//GEN-LAST:event_cancelarEdicaoButtonHospActionPerformed

    private void editarCadButtonHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarCadButtonHospActionPerformed
        int linha = jTable1.getSelectedRow();

            if (linha == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um hóspede na tabela para editar.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int linhaModelo = jTable1.convertRowIndexToModel(linha);

            int idHospede = Integer.parseInt(
                    jTable1.getModel()
                            .getValueAt(linhaModelo, 0)
                            .toString()
            );

            DetalheHospede tela = new DetalheHospede(
                    idHospede,
                    () -> pesquisarHospedes(BuscarNomeHosp.getText())
            );

            tela.setLocationRelativeTo(null);
            tela.setVisible(true);
    }//GEN-LAST:event_editarCadButtonHospActionPerformed

    private void excluirCadButtonHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirCadButtonHospActionPerformed
        int linha = jTable1.getSelectedRow();

            if (linha == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um hóspede na tabela para excluir.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int linhaModelo = jTable1.convertRowIndexToModel(linha);

            int idHospede = Integer.parseInt(
                    jTable1.getModel()
                            .getValueAt(linhaModelo, 0)
                            .toString()
            );

            String nomeHospede =
                    jTable1.getModel()
                            .getValueAt(linhaModelo, 1)
                            .toString();

            int resposta = JOptionPane.showInternalConfirmDialog(
                this,
                "Deseja realmente excluir o hóspede:\n\n"
                + nomeHospede + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (resposta != JOptionPane.YES_OPTION) {
                return;
            }

            String sql =
                    "DELETE FROM hospedi "
                    + "WHERE id_hospedi = ?";

            try (
                    Connection conn =
                            conexao.ConectionFactory.conector();
                    PreparedStatement pst =
                            conn.prepareStatement(sql)
            ) {

                pst.setInt(1, idHospede);

                int resultado =
                        pst.executeUpdate();

                if (resultado > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Hóspede excluído com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    pesquisarHospedes(
                            BuscarNomeHosp.getText()
                    );
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao excluir hóspede:\n"
                        + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
    }//GEN-LAST:event_excluirCadButtonHospActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BuscarNomeHosp;
    private javax.swing.JButton atualizarButtonFunc;
    private javax.swing.JButton atualizarButtonFunc1;
    private javax.swing.JTextField bairroHosp;
    private javax.swing.JButton buscarNomeButtonHosp;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelarEdicaoButtonFunc;
    private javax.swing.JButton cancelarEdicaoButtonFunc1;
    private javax.swing.JButton cancelarEdicaoButtonHosp;
    private javax.swing.JFormattedTextField celularFunc3;
    private javax.swing.JFormattedTextField celularHosp;
    private javax.swing.JFormattedTextField cepHosp;
    private javax.swing.JTextField cidadeHosp;
    private javax.swing.JTextField complementoCasaHosp;
    private javax.swing.JRadioButton comumRadio;
    private javax.swing.JFormattedTextField cpfHosp;
    private javax.swing.JFormattedTextField dataNascHosp;
    private javax.swing.JButton editarCadButtonHosp;
    private javax.swing.JTextField emailFunc3;
    private javax.swing.JTextField emailHosp;
    private javax.swing.JRadioButton empresarioRadio;
    private javax.swing.JComboBox<String> estadoHosp;
    private javax.swing.JButton excluirCadButtonHosp;
    private javax.swing.JPanel fundoTabela;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton limparBuscaButtonHosp;
    private javax.swing.JTextField nomeHosp;
    private javax.swing.JTextField numCasaHosp;
    private javax.swing.JTextArea obsHosp;
    private javax.swing.JFormattedTextField rgHosp;
    private javax.swing.JTextField ruaHosp;
    private javax.swing.JButton salvarButtonFunc;
    private javax.swing.JButton salvarButtonFunc1;
    private javax.swing.JButton salvarButtonHosp;
    private javax.swing.JComboBox<String> sexoHosp;
    private javax.swing.JTextField sobreNomeHosp;
    private javax.swing.JScrollPane tabelaCadFunc;
    private javax.swing.JFormattedTextField telFixoFunc3;
    private javax.swing.JFormattedTextField telFixoHosp;
    private javax.swing.JRadioButton vipRadio;
    // End of variables declaration//GEN-END:variables
}
