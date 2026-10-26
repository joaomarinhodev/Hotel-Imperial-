/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JInternalFrame;

/**
 *
 * @author User
 */
public class CadastroFunc extends javax.swing.JInternalFrame {
    ValidarSomLetras vsl = new ValidarSomLetras();
    ValidarSomNum vsn = new ValidarSomNum();
    VerificarCampoVazio vcv = new VerificarCampoVazio();
    VerificarJboxVazio vjv = new VerificarJboxVazio();
            
    public CadastroFunc() {
        initComponents();
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(
            JInternalFrame.DISPOSE_ON_CLOSE
                
        );
        
        if (!SessaoUsuario.isAdmin()) {
            editarCadButtonFunc.setVisible(false);
            excluirCadButtonFunc.setVisible(false);
        }
        fundoTabela.setVisible(false);
        limparBuscaButtonFunc.setVisible(false);
    }
    
    private void pesquisarFuncionarios(String nome) {

        javax.swing.table.DefaultTableModel modelo =
                (javax.swing.table.DefaultTableModel) tabelaCad.getModel();

        modelo.setRowCount(0);

        String sql =
                "SELECT id_funcionario, nome_funcionario, "
                + "sobrenome_funcionario, cpf_funcionario "
                + "FROM funcionarios "
                + "WHERE nome_funcionario ILIKE ? "
                + "ORDER BY nome_funcionario";

            try (
                    Connection conn =
                            conexao.ConectionFactory.conector();

                    PreparedStatement pst =
                            conn.prepareStatement(sql)
            ) {

                pst.setString(1, "%" + nome.trim() + "%");

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {

                    modelo.addRow(new Object[]{
                        rs.getInt("id_funcionario"),
                        rs.getString("nome_funcionario"),
                        rs.getString("sobrenome_funcionario"),
                        rs.getString("cpf_funcionario")
                    });
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao pesquisar funcionários:\n"
                        + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
    }

    private void limparCampos() {
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja limpar todos os campos?", "Confirmação",
                                                    JOptionPane.YES_NO_OPTION);
        if(resposta == JOptionPane.YES_OPTION){
            nomeFunc.setText("");
            sobreNomeFunc.setText("");
            cpfFunc.setText("");
            rgFunc.setText("");
            dataNascFunc.setText("");
            cargoFunc.setText("");

            sexoFunc.setSelectedIndex(0);
            grauAcessoFunc.setSelectedIndex(0);

            nomeUsuarioFunc.setText("");
            senhaFunc.setText("");

            emailFunc.setText("");
            celularFunc.setText("");
            telFixoFunc.setText("");

            ruaFunc.setText("");
            bairroFunc.setText("");
            complementoCasaFunc.setText("");
            cidadeFunc.setText("");
            cepFunc.setText("");
            numCasaFunc.setText("");

            estadoFunc.setSelectedIndex(0);
        }
}
    
    private boolean validarCamposObrigatorios() {

        boolean erro = false; 
        erro |= vcv.campoVazio(nomeFunc);
        erro |= vcv.campoVazio(sobreNomeFunc);
        erro |= vcv.campoVazio(cpfFunc);
        erro |= vcv.campoVazio(dataNascFunc);
        erro |= vcv.campoVazio(cargoFunc);
        erro |= vjv.campoVazio(sexoFunc);

        erro |= vcv.campoVazio(nomeUsuarioFunc);
        erro |= vcv.campoVazio(senhaFunc);
        erro |= vjv.campoVazio(grauAcessoFunc);

        erro |= vcv.campoVazio(emailFunc);
        erro |= vcv.campoVazio(celularFunc);
        erro |= vcv.campoVazio(ruaFunc);
        erro |= vcv.campoVazio(bairroFunc);
        erro |= vcv.campoVazio(cidadeFunc);
        erro |= vcv.campoVazio(cepFunc);
        erro |= vcv.campoVazio(numCasaFunc);

        erro |= vjv.campoVazio(estadoFunc);

            if (erro) {
                JOptionPane.showMessageDialog(null,"Preencha os campos obrigatórios!");
                return false;
            }

    return true;
    }
    // método para pegar os dados dos campos:
    private void salvarFuncionario(){

    try {

        Connection conn = conexao.ConectionFactory.conector();

        String sql =
        "INSERT INTO funcionarios("
        + "nome_funcionario, sobrenome_funcionario, "
        + "datanasc_funcionario, cpf_funcionario, "
        + "rg_funcionario, cargo_funcionario, "
        + "nome_usuario_func, senha_usuario_func, grauacesso_usuario_func,"       
        + "sexo_funcionario, email_funcionario, "
        + "cell_funcionario, tellfixo_funcionario, "
        + "rua_fucionario, bairro_funcionario, "
        + "complemento_fu, cidade_funcionario, "
        + "cep_funcionario, numero_casa_funcionario, "
        + "estado_funcionario"
        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);

        LocalDate data = LocalDate.parse(
            dataNascFunc.getText(),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        java.sql.Date dataSql = java.sql.Date.valueOf(data);
        
        
        pst.setString(1, nomeFunc.getText());
        pst.setString(2, sobreNomeFunc.getText());
        pst.setDate(3, dataSql);
        pst.setString(4, cpfFunc.getText());
        String rg = rgFunc.getText().replaceAll("[^0-9]", "");
            if (rg.isEmpty()) {
                pst.setNull(5, java.sql.Types.VARCHAR);
            } else {
                pst.setString(5, rgFunc.getText());
            }
        
        pst.setString(6, cargoFunc.getText());
        pst.setString(7, nomeUsuarioFunc.getText());
        String senha = new String(senhaFunc.getPassword());
        pst.setString(8, senha);
        pst.setString(9, grauAcessoFunc.getSelectedItem()
                                        .toString());
        pst.setString(10, sexoFunc.getSelectedItem()    
                                        .toString());
        pst.setString(11, emailFunc.getText());
        pst.setString(12, celularFunc.getText());
        String telFixo = telFixoFunc.getText().replaceAll("[^0-9]", "");
            if (telFixo.isEmpty()) {
                pst.setNull(13, java.sql.Types.VARCHAR);
            } else {
                pst.setString(13, telFixoFunc.getText());
            }
            
        pst.setString(14, ruaFunc.getText());
        pst.setString(15, bairroFunc.getText());
        String complemento = complementoCasaFunc.getText().trim();
            if (complemento.isEmpty()) {
                pst.setNull(16, java.sql.Types.VARCHAR);
            } else {
                pst.setString(16, complemento);
            }
            
        pst.setString(17, cidadeFunc.getText());
        pst.setString(18, cepFunc.getText());
        pst.setString(19, numCasaFunc.getText());
        pst.setString(20,estadoFunc.getSelectedItem()
                                   .toString());

        pst.executeUpdate();

        JOptionPane.showMessageDialog(null,"Funcionário cadastrado com sucesso!");

        pst.close();
        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,"Erro ao cadastrar:\n" + e.getMessage());
    }
}

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField8 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        scrollPaneCadFun = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        BuscarNomeFunc = new javax.swing.JTextField();
        buscarNomeButtonFunc = new javax.swing.JButton();
        limparBuscaButtonFunc = new javax.swing.JButton();
        fundoTabela = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        editarCadButtonFunc = new javax.swing.JButton();
        excluirCadButtonFunc = new javax.swing.JButton();
        tabelaCadFunc = new javax.swing.JScrollPane();
        tabelaCad = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        fotoFunc = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nomeFunc = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        sobreNomeFunc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cpfFunc = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        dataNascFunc = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        rgFunc = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        sexoFunc = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cargoFunc = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        nomeUsuarioFunc = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        grauAcessoFunc = new javax.swing.JComboBox<>();
        senhaFunc = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        emailFunc = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        celularFunc = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        telFixoFunc = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        ruaFunc = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        bairroFunc = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cidadeFunc = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        cepFunc = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        complementoCasaFunc = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        numCasaFunc = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        estadoFunc = new javax.swing.JComboBox<>();
        salvarButtonFunc = new javax.swing.JButton();
        limparButtonFunc = new javax.swing.JButton();

        jTextField8.setText("jTextField8");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setTitle("Hotel Imperial - Cadastro de funcionários");

        scrollPaneCadFun.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONSULTAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel24.setText("Antes de realizar um cadastro, verifique se a pessoa já não foi cadastrada!");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel25.setText("Buscar por nome");

        BuscarNomeFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        BuscarNomeFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        BuscarNomeFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarNomeFuncActionPerformed(evt);
            }
        });
        BuscarNomeFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BuscarNomeFuncKeyTyped(evt);
            }
        });

        buscarNomeButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        buscarNomeButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-procurar-19.png"))); // NOI18N
        buscarNomeButtonFunc.setText("Buscar");
        buscarNomeButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buscarNomeButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarNomeButtonFuncActionPerformed(evt);
            }
        });

        limparBuscaButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        limparBuscaButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-vassoura-20.png"))); // NOI18N
        limparBuscaButtonFunc.setText("Limpar busca");
        limparBuscaButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        limparBuscaButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparBuscaButtonFuncActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel26.setText("Para editar ou excluir um cadastro, basta selecionar na tabela e clicar no botão desejado");

        editarCadButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        editarCadButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-editar-20.png"))); // NOI18N
        editarCadButtonFunc.setText("Editar");
        editarCadButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editarCadButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarCadButtonFuncActionPerformed(evt);
            }
        });

        excluirCadButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        excluirCadButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-lixeira-20.png"))); // NOI18N
        excluirCadButtonFunc.setText("Excluir");
        excluirCadButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        excluirCadButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirCadButtonFuncActionPerformed(evt);
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
                .addComponent(editarCadButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(excluirCadButtonFunc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        fundoTabelaLayout.setVerticalGroup(
            fundoTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fundoTabelaLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(fundoTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(editarCadButtonFunc)
                    .addComponent(excluirCadButtonFunc))
                .addContainerGap())
        );

        tabelaCadFunc.setBorder(null);

        tabelaCad.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "NOME", "SOBRENOME", "CPF"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaCadFunc.setViewportView(tabelaCad);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addGap(195, 195, 195))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fundoTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabelaCadFunc))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BuscarNomeFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buscarNomeButtonFunc)
                .addGap(18, 18, 18)
                .addComponent(limparBuscaButtonFunc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(BuscarNomeFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscarNomeButtonFunc)
                    .addComponent(limparBuscaButtonFunc))
                .addGap(34, 34, 34)
                .addComponent(tabelaCadFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fundoTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS PESSOAIS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        fotoFunc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fotoFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-avatar-96.png"))); // NOI18N
        fotoFunc.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Foto", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 18))); // NOI18N
        fotoFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("*Nome:");

        nomeFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nomeFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nomeFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeFuncActionPerformed(evt);
            }
        });
        nomeFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomeFuncKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("*Sobrenome:");

        sobreNomeFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sobreNomeFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sobreNomeFuncKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("*CPF:");

        try {
            cpfFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cpfFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cpfFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cpfFuncKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("*Data de Nascimento:");

        try {
            dataNascFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataNascFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        dataNascFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                dataNascFuncKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("RG:");

        try {
            rgFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        rgFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("*Sexo:");

        sexoFunc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Masculino", "Feminino" }));
        sexoFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("*Cargo:");

        cargoFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cargoFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargoFuncActionPerformed(evt);
            }
        });
        cargoFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cargoFuncKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Nirmala Text", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 51));
        jLabel9.setText("O símbolo (*) indica que o campo deve ser preenchido obrigatóriamente!");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(fotoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(nomeFunc)
                            .addComponent(cpfFunc, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(sexoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sobreNomeFunc)
                            .addComponent(rgFunc, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                        .addGap(64, 64, 64)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dataNascFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)
                            .addComponent(cargoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(nomeFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sobreNomeFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dataNascFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cpfFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rgFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cargoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sexoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(fotoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CADASTRO DE USUÁRIO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("*Nome de Usuário:");

        nomeUsuarioFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nomeUsuarioFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeUsuarioFuncActionPerformed(evt);
            }
        });
        nomeUsuarioFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomeUsuarioFuncKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("*Senha:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("*Grau de Acesso:");

        grauAcessoFunc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Consultor", "Gerente", "Admin" }));
        grauAcessoFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(104, 104, 104))
                    .addComponent(nomeUsuarioFunc))
                .addGap(89, 89, 89)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(103, 103, 103))
                    .addComponent(senhaFunc))
                .addGap(88, 88, 88)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(grauAcessoFunc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(26, 26, 26))
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(197, 197, 197))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 874, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(senhaFunc))
                    .addComponent(nomeUsuarioFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grauAcessoFunc))
                .addGap(121, 121, 121))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONTATO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("*E-mail:");

        emailFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("*Celular:");

        try {
            celularFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        celularFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setText("Tel.Fixo:");

        try {
            telFixoFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telFixoFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(emailFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(85, 85, 85)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(celularFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(telFixoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celularFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telFixoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENDEREÇO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setText("*Rua:");

        ruaFunc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        ruaFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ruaFuncKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("*Bairro:");

        bairroFunc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        bairroFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bairroFuncKeyTyped(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setText("*Cidade:");

        cidadeFunc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cidadeFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cidadeFuncActionPerformed(evt);
            }
        });
        cidadeFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cidadeFuncKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setText("*CEP:");

        try {
            cepFunc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cepFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setText("Complemento:");

        complementoCasaFunc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        complementoCasaFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                complementoCasaFuncKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setText("*Número:");

        numCasaFunc.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        numCasaFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numCasaFuncActionPerformed(evt);
            }
        });
        numCasaFunc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numCasaFuncKeyTyped(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setText("*Estado:");

        estadoFunc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PE", "PR", "PB", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        estadoFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(ruaFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19)
                        .addComponent(cidadeFunc, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(cepFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(167, 167, 167))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(numCasaFunc)
                                .addGap(73, 73, 73))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(bairroFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21)
                            .addComponent(complementoCasaFunc)
                            .addComponent(jLabel23)
                            .addComponent(estadoFunc, 0, 142, Short.MAX_VALUE))
                        .addGap(24, 24, 24))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bairroFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ruaFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(complementoCasaFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel22)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(numCasaFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cepFunc))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cidadeFunc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        salvarButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salvarButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        salvarButtonFunc.setText("Salvar");
        salvarButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        salvarButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarButtonFuncActionPerformed(evt);
            }
        });

        limparButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        limparButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-vassoura-20.png"))); // NOI18N
        limparButtonFunc.setText("Limpar Tudo");
        limparButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        limparButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparButtonFuncActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(limparButtonFunc)
                        .addGap(18, 18, 18)
                        .addComponent(salvarButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(226, 226, 226))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(203, 203, 203))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(limparButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salvarButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(89, Short.MAX_VALUE))
        );

        scrollPaneCadFun.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneCadFun, javax.swing.GroupLayout.DEFAULT_SIZE, 1152, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneCadFun, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nomeFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeFuncActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeFuncActionPerformed

    private void nomeUsuarioFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeUsuarioFuncActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeUsuarioFuncActionPerformed

    private void buscarNomeButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarNomeButtonFuncActionPerformed
        // TODO add your handling code here:
        fundoTabela.setVisible(true);
        limparBuscaButtonFunc.setVisible(true);
        buscarNomeButtonFunc.setVisible(false);
        pesquisarFuncionarios(BuscarNomeFunc.getText());
    }//GEN-LAST:event_buscarNomeButtonFuncActionPerformed

    private void limparBuscaButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparBuscaButtonFuncActionPerformed
        // TODO add your handling code here:
        fundoTabela.setVisible(false);
        limparBuscaButtonFunc.setVisible(false);
        buscarNomeButtonFunc.setVisible(true);
        
        BuscarNomeFunc.setText("");

    javax.swing.table.DefaultTableModel modelo =
            (javax.swing.table.DefaultTableModel) tabelaCad.getModel();

    modelo.setRowCount(0);
    }//GEN-LAST:event_limparBuscaButtonFuncActionPerformed

    private void numCasaFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numCasaFuncActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_numCasaFuncActionPerformed

    private void numCasaFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCasaFuncKeyTyped
        // TODO add your handling code here:
        vsn.verificarSomenteNumeros(numCasaFunc, evt);
    }//GEN-LAST:event_numCasaFuncKeyTyped

    private void BuscarNomeFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarNomeFuncActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuscarNomeFuncActionPerformed

    private void BuscarNomeFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarNomeFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(BuscarNomeFunc, evt);
    }//GEN-LAST:event_BuscarNomeFuncKeyTyped

    private void nomeFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(nomeFunc, evt);
    }//GEN-LAST:event_nomeFuncKeyTyped

    private void sobreNomeFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sobreNomeFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(sobreNomeFunc, evt);
    }//GEN-LAST:event_sobreNomeFuncKeyTyped

    private void cargoFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargoFuncActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cargoFuncActionPerformed

    private void cargoFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cargoFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(cargoFunc, evt);
    }//GEN-LAST:event_cargoFuncKeyTyped

    private void nomeUsuarioFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeUsuarioFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(nomeUsuarioFunc, evt);
    }//GEN-LAST:event_nomeUsuarioFuncKeyTyped

    private void ruaFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ruaFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(ruaFunc, evt);
    }//GEN-LAST:event_ruaFuncKeyTyped

    private void bairroFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bairroFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(bairroFunc, evt);
    }//GEN-LAST:event_bairroFuncKeyTyped

    private void complementoCasaFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_complementoCasaFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(complementoCasaFunc, evt);
        
    }//GEN-LAST:event_complementoCasaFuncKeyTyped

    private void cidadeFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cidadeFuncActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cidadeFuncActionPerformed

    private void cidadeFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cidadeFuncKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(cidadeFunc, evt);
    }//GEN-LAST:event_cidadeFuncKeyTyped

    private void cpfFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cpfFuncKeyTyped
        // TODO add your handling code here:
        vsn.verificarSomenteNumeros(cpfFunc, evt);
    }//GEN-LAST:event_cpfFuncKeyTyped

    private void dataNascFuncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataNascFuncKeyTyped
        // TODO add your handling code here:
        vsn.verificarSomenteNumeros(dataNascFunc, evt);
    }//GEN-LAST:event_dataNascFuncKeyTyped

    private void limparButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparButtonFuncActionPerformed
        limparCampos();
    }//GEN-LAST:event_limparButtonFuncActionPerformed

    private void salvarButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarButtonFuncActionPerformed
        if(validarCamposObrigatorios()){
            salvarFuncionario();
        };
    }//GEN-LAST:event_salvarButtonFuncActionPerformed

    private void editarCadButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarCadButtonFuncActionPerformed
        int linha = tabelaCad.getSelectedRow();

    if (linha == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Selecione um funcionário na tabela para editar.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int linhaModelo = tabelaCad.convertRowIndexToModel(linha);

    int idFuncionario = Integer.parseInt(
            tabelaCad.getModel()
                    .getValueAt(linhaModelo, 0)
                    .toString()
    );

    DetalheFuncionario tela = new DetalheFuncionario(
            idFuncionario,
            () -> pesquisarFuncionarios(BuscarNomeFunc.getText())
    );

    tela.setLocationRelativeTo(null);
    tela.setVisible(true);
    }//GEN-LAST:event_editarCadButtonFuncActionPerformed

    private void excluirCadButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirCadButtonFuncActionPerformed
        int linha = tabelaCad.getSelectedRow();

            if (linha == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Selecione um funcionário na tabela para excluir.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int linhaModelo = tabelaCad.convertRowIndexToModel(linha);

            int idFuncionario = Integer.parseInt(
                    tabelaCad.getModel()
                            .getValueAt(linhaModelo, 0)
                            .toString()
            );

            // Não permite excluir o próprio usuário logado
            if (idFuncionario == SessaoUsuario.getIdUsuario()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Você não pode excluir o usuário que está logado.",
                        "Operação não permitida",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String nomeFuncionario =
                    tabelaCad.getModel()
                            .getValueAt(linhaModelo, 1)
                            .toString();

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir o funcionário:\n\n"
                    + nomeFuncionario
                    + "?",
                    "Confirmar exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (resposta != JOptionPane.YES_OPTION) {
                return;
            }

            String sql =
                    "DELETE FROM funcionarios "
                    + "WHERE id_funcionario = ?";

            try (
                    Connection conn =
                            conexao.ConectionFactory.conector();

                    PreparedStatement pst =
                            conn.prepareStatement(sql)
            ) {

                pst.setInt(1, idFuncionario);

                int resultado = pst.executeUpdate();

                if (resultado > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Funcionário excluído com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Atualiza a tabela
                    pesquisarFuncionarios(
                            BuscarNomeFunc.getText()
                    );
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao excluir funcionário:\n"
                        + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
    }//GEN-LAST:event_excluirCadButtonFuncActionPerformed
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BuscarNomeFunc;
    private javax.swing.JTextField bairroFunc;
    private javax.swing.JButton buscarNomeButtonFunc;
    private javax.swing.JTextField cargoFunc;
    private javax.swing.JFormattedTextField celularFunc;
    private javax.swing.JFormattedTextField cepFunc;
    private javax.swing.JTextField cidadeFunc;
    private javax.swing.JTextField complementoCasaFunc;
    private javax.swing.JFormattedTextField cpfFunc;
    private javax.swing.JFormattedTextField dataNascFunc;
    private javax.swing.JButton editarCadButtonFunc;
    private javax.swing.JTextField emailFunc;
    private javax.swing.JComboBox<String> estadoFunc;
    private javax.swing.JButton excluirCadButtonFunc;
    private javax.swing.JLabel fotoFunc;
    private javax.swing.JPanel fundoTabela;
    private javax.swing.JComboBox<String> grauAcessoFunc;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JButton limparBuscaButtonFunc;
    private javax.swing.JButton limparButtonFunc;
    private javax.swing.JTextField nomeFunc;
    private javax.swing.JTextField nomeUsuarioFunc;
    private javax.swing.JTextField numCasaFunc;
    private javax.swing.JFormattedTextField rgFunc;
    private javax.swing.JTextField ruaFunc;
    private javax.swing.JButton salvarButtonFunc;
    private javax.swing.JScrollPane scrollPaneCadFun;
    private javax.swing.JPasswordField senhaFunc;
    private javax.swing.JComboBox<String> sexoFunc;
    private javax.swing.JTextField sobreNomeFunc;
    private javax.swing.JTable tabelaCad;
    private javax.swing.JScrollPane tabelaCadFunc;
    private javax.swing.JFormattedTextField telFixoFunc;
    // End of variables declaration//GEN-END:variables
}