/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class CadastroFornecedor extends javax.swing.JInternalFrame {
    //INSTANCIANDO CLASSES À OBJETOS
    ValidarSomLetras vsl = new ValidarSomLetras();
    ValidarSomNum vsn = new ValidarSomNum();
    VerificarCampoVazio vcv = new VerificarCampoVazio();
    VerificarJboxVazio vjv = new VerificarJboxVazio();
    
    //VÁRIAVEL GLOBAL
    private ButtonGroup grupoFornecedor;
    
    //MÉTODO CONSTRUTOR
    public CadastroFornecedor() {
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
        grupoFornecedor = new ButtonGroup();

        grupoFornecedor.add(alimentacao);
        grupoFornecedor.add(bebidas);
        grupoFornecedor.add(limpeza);
        grupoFornecedor.add(moveis);
        grupoFornecedor.add(servicos);
    }

    //MÉTODO LIMPAR TODOS OS CAMPOS
    private void limparCampos(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja limpar todos os campos?", "Confirmação",
                                                    JOptionPane.YES_NO_OPTION);
        if(resposta == JOptionPane.YES_OPTION){
        razaoSocial.setText("");
        nomeFantasia.setText("");
        cnpjFornecedor.setText("");
        inscEstadual.setText("");
        responsFornecedor.setText("");
        emailFornecedor.setText("");
        celFornecedor.setText("");
        telFixoFornecedor.setText("");
        ruaFornecedor.setText("");
        bairroFornecedor.setText("");
        complementoCasaFornecedor.setText("");
        cidadeFornecedor.setText("");
        cepFornecedor.setText("");
        numCasaFornecedor.setText("");
        estadoFornecedor.setSelectedItem(0);
        obsFornecedor.setText("");
        }
    }
    
    //MÉTODO CAMPOS OBRIGATÓRIOS
    private boolean ValidarCamposObrigatorios(){
        boolean erro = false;
        erro |= vcv.campoVazio(razaoSocial);
        erro |= vcv.campoVazio(nomeFantasia);
        erro |= vcv.campoVazio(cnpjFornecedor);
        erro |= vcv.campoVazio(responsFornecedor);
        erro |= vcv.campoVazio(emailFornecedor);
        erro |= vcv.campoVazio(celFornecedor);
        erro |= vcv.campoVazio(ruaFornecedor);
        erro |= vcv.campoVazio(bairroFornecedor);
        erro |= vcv.campoVazio(cidadeFornecedor);
        erro |= vcv.campoVazio(cepFornecedor);
        erro |= vcv.campoVazio(numCasaFornecedor);
        erro |= vjv.campoVazio(estadoFornecedor);
        
        if(grupoFornecedor.getSelection() == null){
            erro = true;
        }
        if (erro) {
            JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios!");
            return false;
        }
        return true;
    }
    
    //MÉTODO SALVAR
    private void salvarHospede(){
        
        try {
            // CHAMANDO A CLASSE ONDE ESTÁ OS DADOS PARA A CONEXÃO COM BD
            Connection conn = conexao.ConectionFactory.conector();
            
            // INSERT PARA TABELA FORNECEDOR
            String sql = "INSERT INTO fornecedor("
                + "razao_social, cnpj, "
                + "nome_fantasia, incricao_estadual, "
                + "responsavel, cell, email, "
                + "tellfixo, rua, "
                + "bairro,cidade, cep, numero, "
                + "estado,complemento_endereco, observacao, tipo_fornecedor "
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            
            // FAZENDO O INSERT
            pst.setString(1,razaoSocial.getText());
            pst.setString(2,cnpjFornecedor.getText());
            pst.setString(3,nomeFantasia.getText());
            // INSCRIÇÃO ESTADUAL (OPCIONAL)
            String inscEstadual = this.inscEstadual.getText().trim();
            
                if(inscEstadual.isEmpty()){
                    pst.setNull(4,java.sql.Types.VARCHAR);
                }else {
                    pst.setString(4,inscEstadual);
                }
                
            pst.setString(5,responsFornecedor.getText());
            pst.setString(6,celFornecedor.getText());
            pst.setString(7,emailFornecedor.getText());
            // TELEFONE FIXO (OPCIONAL)
            String telFixo = telFixoFornecedor.getText().replaceAll("[^0-9]", "");
                if (telFixo.isEmpty()) {
                    pst.setNull(8, java.sql.Types.VARCHAR);
                } else {
                    pst.setString(8, telFixo);
                }
            
            pst.setString(9,ruaFornecedor.getText());
            pst.setString(10,bairroFornecedor.getText());
            pst.setString(11,cidadeFornecedor.getText());
            pst.setString(12,cepFornecedor.getText());
            pst.setString(13,numCasaFornecedor.getText());
            pst.setString(14,estadoFornecedor.getSelectedItem().toString());
            // COMPLEMENTO (OPCIONAL)
            String complemento = complementoCasaFornecedor.getText().trim();
                if (complemento.isEmpty()) {
                    pst.setNull(15, java.sql.Types.VARCHAR);
                } else {
                    pst.setString(15, complemento);
                }
                
            pst.setString(16,obsFornecedor.getText());
            
            // INSERINDO RADIOS BUTTONS
            String radioButton = null;
                if(alimentacao.isSelected()){
                    radioButton = "Alimentação";
                }else if(bebidas.isSelected()){
                    radioButton = "Bebidas";
                }else if(limpeza.isSelected()){
                    radioButton = "Limpeza";
                }else if(moveis.isSelected()){
                    radioButton = "Móveis";
                }else if(servicos.isSelected()){
                    radioButton = "Serviços";
                }
            pst.setString(17,radioButton);
            
            // EXECUTAR
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Fornecedor cadastrado com sucesso!");
            //ENCERRANDO
            pst.close();
            conn.close();
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar fornecedor:\n" + e.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        razaoSocial = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nomeFantasia = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cnpjFornecedor = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        inscEstadual = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        responsFornecedor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        emailFornecedor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        celFornecedor = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        telFixoFornecedor = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        ruaFornecedor = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cidadeFornecedor = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        bairroFornecedor = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        cepFornecedor = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        numCasaFornecedor = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        estadoFornecedor = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        complementoCasaFornecedor = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        alimentacao = new javax.swing.JRadioButton();
        bebidas = new javax.swing.JRadioButton();
        limpeza = new javax.swing.JRadioButton();
        moveis = new javax.swing.JRadioButton();
        servicos = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        obsFornecedor = new javax.swing.JTextArea();
        limparButtonFunc = new javax.swing.JButton();
        salvarButtonFunc = new javax.swing.JButton();

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

        excluirCadButtonHosp.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        excluirCadButtonHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-lixeira-20.png"))); // NOI18N
        excluirCadButtonHosp.setText("Excluir");
        excluirCadButtonHosp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DA EMPRESA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("*Razão Social:");

        razaoSocial.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        razaoSocial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                razaoSocialActionPerformed(evt);
            }
        });
        razaoSocial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                razaoSocialKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("*Nome Fantasia:");

        nomeFantasia.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nomeFantasia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeFantasiaActionPerformed(evt);
            }
        });
        nomeFantasia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nomeFantasiaKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("*CNPJ:");

        try {
            cnpjFornecedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cnpjFornecedor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cnpjFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cnpjFornecedorActionPerformed(evt);
            }
        });
        cnpjFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cnpjFornecedorKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Inscrição Estadual:");

        inscEstadual.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        inscEstadual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inscEstadualKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Nirmala Text", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 51, 51));
        jLabel11.setText("O símbolo (*) indica que o campo deve ser preenchido obrigatóriamente!");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(razaoSocial)
                    .addComponent(jLabel1)
                    .addComponent(nomeFantasia, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(cnpjFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(inscEstadual))
                .addGap(197, 197, 197))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(209, 209, 209)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(244, 244, 244))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(razaoSocial)
                    .addComponent(cnpjFornecedor))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inscEstadual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CONTATO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("*Responsável:");

        responsFornecedor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        responsFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                responsFornecedorKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("*E-mail:");

        emailFornecedor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("*Celular:");

        try {
            celFornecedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        celFornecedor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        celFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                celFornecedorKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Tel. Fixo");

        try {
            telFixoFornecedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telFixoFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                telFixoFornecedorKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(responsFornecedor)
                    .addComponent(emailFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(celFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addComponent(telFixoFornecedor))
                .addGap(213, 213, 213))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(responsFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telFixoFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENDEREÇO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel17.setText("*Rua:");

        ruaFornecedor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        ruaFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ruaFornecedorActionPerformed(evt);
            }
        });
        ruaFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ruaFornecedorKeyTyped(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setText("*Cidade:");

        cidadeFornecedor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        cidadeFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cidadeFornecedorActionPerformed(evt);
            }
        });
        cidadeFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cidadeFornecedorKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("*Bairro:");

        bairroFornecedor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        bairroFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bairroFornecedorKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setText("*CEP:");

        try {
            cepFornecedor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        cepFornecedor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cepFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cepFornecedorKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setText("*Número:");

        numCasaFornecedor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        numCasaFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numCasaFornecedorActionPerformed(evt);
            }
        });
        numCasaFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numCasaFornecedorKeyTyped(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setText("*Estado:");

        estadoFornecedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PE", "PR", "PB", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        estadoFornecedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setText("Complemento:");

        complementoCasaFornecedor.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        complementoCasaFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                complementoCasaFornecedorKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(ruaFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19)
                        .addComponent(cidadeFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(cepFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(167, 167, 167))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(numCasaFornecedor)
                                .addGap(73, 73, 73))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(bairroFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21)
                            .addComponent(complementoCasaFornecedor)
                            .addComponent(jLabel23)
                            .addComponent(estadoFornecedor, 0, 142, Short.MAX_VALUE))
                        .addGap(24, 24, 24))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bairroFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ruaFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(complementoCasaFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel22)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(numCasaFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cepFornecedor))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cidadeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OBSERVAÇÕES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel9.setText("*Tipo de Fornecedor:");

        alimentacao.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        alimentacao.setText("Alimentação");

        bebidas.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        bebidas.setText("Bebidas");

        limpeza.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        limpeza.setText("Limpeza");

        moveis.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        moveis.setText("Móveis");

        servicos.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        servicos.setText("Serviços");
        servicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servicosActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel10.setText("Observações:");

        obsFornecedor.setColumns(20);
        obsFornecedor.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        obsFornecedor.setRows(5);
        obsFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                obsFornecedorKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(obsFornecedor);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(limpeza)
                    .addComponent(bebidas)
                    .addComponent(alimentacao)
                    .addComponent(servicos)
                    .addComponent(moveis))
                .addGap(300, 300, 300)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(alimentacao)
                        .addGap(18, 18, 18)
                        .addComponent(bebidas)
                        .addGap(18, 18, 18)
                        .addComponent(limpeza)
                        .addGap(18, 18, 18)
                        .addComponent(moveis)
                        .addGap(18, 18, 18)
                        .addComponent(servicos))
                    .addComponent(jScrollPane2))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        limparButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        limparButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-vassoura-20.png"))); // NOI18N
        limparButtonFunc.setText("Limpar");
        limparButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        limparButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparButtonFuncActionPerformed(evt);
            }
        });

        salvarButtonFunc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salvarButtonFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        salvarButtonFunc.setText("Salvar");
        salvarButtonFunc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        salvarButtonFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarButtonFuncActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(limparButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(salvarButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(limparButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salvarButtonFunc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(157, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1938, Short.MAX_VALUE)
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
    }//GEN-LAST:event_buscarNomeButtonHospActionPerformed

    private void limparBuscaButtonHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparBuscaButtonHospActionPerformed
        // TODO add your handling code here:
        fundoTabela.setVisible(false);
        limparBuscaButtonHosp.setVisible(false);
        buscarNomeButtonHosp.setVisible(true);
    }//GEN-LAST:event_limparBuscaButtonHospActionPerformed

    private void nomeFantasiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeFantasiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeFantasiaActionPerformed

    private void ruaFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ruaFornecedorKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(ruaFornecedor, evt);
    }//GEN-LAST:event_ruaFornecedorKeyTyped

    private void cidadeFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cidadeFornecedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cidadeFornecedorActionPerformed

    private void cidadeFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cidadeFornecedorKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(cidadeFornecedor, evt);
    }//GEN-LAST:event_cidadeFornecedorKeyTyped

    private void bairroFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bairroFornecedorKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(bairroFornecedor, evt);
    }//GEN-LAST:event_bairroFornecedorKeyTyped

    private void numCasaFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numCasaFornecedorActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_numCasaFornecedorActionPerformed

    private void numCasaFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numCasaFornecedorKeyTyped
        // TODO add your handling code here:
        vsn.verificarSomenteNumeros(numCasaFornecedor, evt);
    }//GEN-LAST:event_numCasaFornecedorKeyTyped

    private void complementoCasaFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_complementoCasaFornecedorKeyTyped
        // TODO add your handling code here:
        vsl.validarSomenteLetras(complementoCasaFornecedor, evt);

    }//GEN-LAST:event_complementoCasaFornecedorKeyTyped

    private void ruaFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ruaFornecedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ruaFornecedorActionPerformed

    private void servicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servicosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_servicosActionPerformed

    private void cnpjFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cnpjFornecedorActionPerformed
        
    }//GEN-LAST:event_cnpjFornecedorActionPerformed

    private void razaoSocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_razaoSocialActionPerformed
        
    }//GEN-LAST:event_razaoSocialActionPerformed

    private void razaoSocialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_razaoSocialKeyTyped
        vsl.validarSomenteLetras(razaoSocial, evt);
    }//GEN-LAST:event_razaoSocialKeyTyped

    private void nomeFantasiaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeFantasiaKeyTyped
        vsl.validarSomenteLetras(nomeFantasia, evt);
    }//GEN-LAST:event_nomeFantasiaKeyTyped

    private void cnpjFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cnpjFornecedorKeyTyped
        vsn.verificarSomenteNumeros(cnpjFornecedor, evt);
    }//GEN-LAST:event_cnpjFornecedorKeyTyped

    private void inscEstadualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inscEstadualKeyTyped
        vsn.verificarSomenteNumeros(inscEstadual, evt);
    }//GEN-LAST:event_inscEstadualKeyTyped

    private void responsFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_responsFornecedorKeyTyped
        vsl.validarSomenteLetras(responsFornecedor, evt);
    }//GEN-LAST:event_responsFornecedorKeyTyped

    private void celFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_celFornecedorKeyTyped
        vsn.verificarSomenteNumeros(celFornecedor, evt);
    }//GEN-LAST:event_celFornecedorKeyTyped

    private void telFixoFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telFixoFornecedorKeyTyped
        vsn.verificarSomenteNumeros(telFixoFornecedor, evt);
    }//GEN-LAST:event_telFixoFornecedorKeyTyped

    private void limparButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparButtonFuncActionPerformed
        limparCampos();
    }//GEN-LAST:event_limparButtonFuncActionPerformed

    private void salvarButtonFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarButtonFuncActionPerformed
        if(ValidarCamposObrigatorios()){
            salvarHospede();
        }
    }//GEN-LAST:event_salvarButtonFuncActionPerformed

    private void cepFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cepFornecedorKeyTyped
        vsn.verificarSomenteNumeros(cepFornecedor, evt);
    }//GEN-LAST:event_cepFornecedorKeyTyped

    private void obsFornecedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_obsFornecedorKeyTyped
        
    }//GEN-LAST:event_obsFornecedorKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BuscarNomeHosp;
    private javax.swing.JRadioButton alimentacao;
    private javax.swing.JTextField bairroFornecedor;
    private javax.swing.JRadioButton bebidas;
    private javax.swing.JButton buscarNomeButtonHosp;
    private javax.swing.JFormattedTextField celFornecedor;
    private javax.swing.JFormattedTextField cepFornecedor;
    private javax.swing.JTextField cidadeFornecedor;
    private javax.swing.JFormattedTextField cnpjFornecedor;
    private javax.swing.JTextField complementoCasaFornecedor;
    private javax.swing.JButton editarCadButtonHosp;
    private javax.swing.JTextField emailFornecedor;
    private javax.swing.JComboBox<String> estadoFornecedor;
    private javax.swing.JButton excluirCadButtonHosp;
    private javax.swing.JPanel fundoTabela;
    private javax.swing.JTextField inscEstadual;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton limparBuscaButtonHosp;
    private javax.swing.JButton limparButtonFunc;
    private javax.swing.JRadioButton limpeza;
    private javax.swing.JRadioButton moveis;
    private javax.swing.JTextField nomeFantasia;
    private javax.swing.JTextField numCasaFornecedor;
    private javax.swing.JTextArea obsFornecedor;
    private javax.swing.JTextField razaoSocial;
    private javax.swing.JTextField responsFornecedor;
    private javax.swing.JTextField ruaFornecedor;
    private javax.swing.JButton salvarButtonFunc;
    private javax.swing.JRadioButton servicos;
    private javax.swing.JScrollPane tabelaCadFunc;
    private javax.swing.JFormattedTextField telFixoFornecedor;
    // End of variables declaration//GEN-END:variables
}
