/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author User
 */
public class CadastroQuarto extends javax.swing.JInternalFrame {
    
    ValidarSomLetras vsl = new ValidarSomLetras();
    ValidarSomNum vsn = new ValidarSomNum();
    VerificarCampoVazio vcv = new VerificarCampoVazio();
    VerificarJboxVazio vjv = new VerificarJboxVazio();
    VerificarJCheckBox vjcb = new VerificarJCheckBox();
    
    public CadastroQuarto() {
        initComponents();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

        setDefaultCloseOperation(
            JInternalFrame.DISPOSE_ON_CLOSE
        );

        valorDiariaField.setFormatterFactory(null);
        valorDiariaField.setValue(null);
    }
    
    private void limparCampos(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja limpar todos os campos?", "Confirmação",
                                                    JOptionPane.YES_NO_OPTION);
        if(resposta == JOptionPane.YES_OPTION){
            num.setText("");
            tipo.setSelectedItem(0);
            valorDiariaField.setText("");
            capacidade.setSelectedItem(0);
            andar.setSelectedItem(0);
            descricao.setText("");
            wifi.setSelected(false);
            arCondicionado.setSelected(false);
            vistaMar.setSelected(false);
            frigobar.setSelected(false);
            tv.setSelected(false);
            vistaPiscina.setSelected(false);
            banheira.setSelected(false);
            sauna.setSelected(false);
            vistaPanoramica.setSelected(false);
            ferroDePassar.setSelected(false);
            sofa.setSelected(false);
            secadorDeCabelo.setSelected(false);
            varanda.setSelected(false);
            aquecedor.setSelected(false);
        };
    };

    private boolean validarCamposObrigatorios(){
        boolean erro = false;
        erro |= vcv.campoVazio(num);
        erro |= vjv.campoVazio(tipo);
        erro |= vcv.campoVazio(valorDiariaField);
        erro |= vjv.campoVazio(capacidade);
        erro |= vjv.campoVazio(andar);
        
        if(erro){
            JOptionPane.showMessageDialog(null, "Preencher os campos obrigatórios!");
            return false;
        }
        return true;
    };
    
    private void salvarQuarto() {
    String sqlQuarto = "INSERT INTO quarto ("
            + "numero, tipo, valor_diaria, capacidade, andar, descricao"
            + ") VALUES (?, ?, ?, ?, ?, ?)";

    String sqlComodidade = "INSERT INTO quarto_comodidade (id_quarto, id_comodidade) VALUES (?, ?)";

    try (Connection conn = conexao.ConectionFactory.conector()) {
        // Desativa o auto-commit para garantir transação única
        conn.setAutoCommit(false);

        int idQuartoGerado = -1;

        // 1. INSERIR QUARTO
        try (PreparedStatement pstQuarto = conn.prepareStatement(sqlQuarto, Statement.RETURN_GENERATED_KEYS)) {
            pstQuarto.setString(1, num.getText());
            pstQuarto.setString(2, tipo.getSelectedItem().toString());
            // 1. Pega o texto do campo
            String textoValor = valorDiariaField.getText().trim();

                if (!textoValor.isEmpty()) {

                    textoValor = textoValor
                            .replace("R$", "")
                            .replace(" ", "");

                    if (textoValor.contains(",")) {
                        textoValor = textoValor
                                .replace(".", "")
                                .replace(",", ".");
                    }

                    double valor = Double.parseDouble(textoValor);

                    pstQuarto.setDouble(3, valor);

                } else {

                    pstQuarto.setNull(3, java.sql.Types.DOUBLE);
                }
                
            String textoCapacidade = capacidade.getSelectedItem().toString();

                int quantidadePessoas = Integer.parseInt(
                        textoCapacidade.split(" ")[0]
                );

                pstQuarto.setInt(4, quantidadePessoas);
                
            pstQuarto.setString(5, andar.getSelectedItem().toString());
            pstQuarto.setString(6, descricao.getText());

            pstQuarto.executeUpdate();

            // Obter o ID gerado para o quarto
            try (ResultSet rs = pstQuarto.getGeneratedKeys()) {
                if (rs.next()) {
                    idQuartoGerado = rs.getInt(1);
                }
            }
        }

        if (idQuartoGerado == -1) {
            throw new Exception("Falha ao obter o ID do quarto criado.");
        }

        // 2. INSERIR COMODIDADES SELECCIONADAS
        try (PreparedStatement pstComodidade = conn.prepareStatement(sqlComodidade)) {
            
            // Mapeamento dos checkboxes para os IDs da tabela 'comodidade'
            Map<JCheckBox, Integer> comodidadesMap = new HashMap<>();
            comodidadesMap.put(wifi, 1);
            comodidadesMap.put(frigobar, 2);
            comodidadesMap.put(banheira, 3);
            comodidadesMap.put(ferroDePassar, 4);
            comodidadesMap.put(varanda, 5);
            comodidadesMap.put(arCondicionado, 6);
            comodidadesMap.put(tv, 7);
            comodidadesMap.put(sauna, 8);
            comodidadesMap.put(sofa, 9);
            comodidadesMap.put(aquecedor, 10);
            comodidadesMap.put(vistaMar, 11);
            comodidadesMap.put(vistaPiscina, 12);
            comodidadesMap.put(vistaPanoramica, 13);
            comodidadesMap.put(secadorDeCabelo, 14);

            for (Map.Entry<JCheckBox, Integer> entry : comodidadesMap.entrySet()) {
                JCheckBox checkBox = entry.getKey();
                int idComodidade = entry.getValue();

                if (checkBox.isSelected()) {
                    pstComodidade.setInt(1, idQuartoGerado);
                    pstComodidade.setInt(2, idComodidade);
                    pstComodidade.addBatch(); // Agrupa para salvar juntas
                }
            }

            pstComodidade.executeBatch(); // Executa o lote de inserções
        }

        // Confirma a transação
        conn.commit();
        JOptionPane.showMessageDialog(null, "Quarto e comodidades guardados com sucesso!");

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Verifique se o valor da diária e a capacidade contêm números válidos.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Erro ao guardar quarto:\n" + e.getMessage());
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        num = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        capacidade = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        andar = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        descricao = new javax.swing.JTextArea();
        valorDiariaField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        wifi = new javax.swing.JCheckBox();
        arCondicionado = new javax.swing.JCheckBox();
        frigobar = new javax.swing.JCheckBox();
        tv = new javax.swing.JCheckBox();
        banheira = new javax.swing.JCheckBox();
        sauna = new javax.swing.JCheckBox();
        ferroDePassar = new javax.swing.JCheckBox();
        sofa = new javax.swing.JCheckBox();
        varanda = new javax.swing.JCheckBox();
        vistaMar = new javax.swing.JCheckBox();
        vistaPiscina = new javax.swing.JCheckBox();
        vistaPanoramica = new javax.swing.JCheckBox();
        secadorDeCabelo = new javax.swing.JCheckBox();
        aquecedor = new javax.swing.JCheckBox();
        salvarbutton = new javax.swing.JButton();
        limparTudo = new javax.swing.JButton();

        jScrollPane1.setHorizontalScrollBar(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DO QUARTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Nirmala Text", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 51));
        jLabel9.setText("O símbolo (*) indica que o campo deve ser preenchido obrigatóriamente!");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("*Número:");

        num.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        num.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("*Tipo:");

        tipo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Solteiro", "Casal", "Luxo", "Suíte" }));
        tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("*Valor diária:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("*Capacidade:");

        capacidade.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        capacidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Pessoa", "2 Pessoa", "3 Pessoa", "4 Pessoa", "5 Pessoa", "6 Pessoa" }));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("*Andar:");

        andar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        andar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1° Andar", "2° Andar", "3° Andar", "4° Andar", "5° Andar", "6° Andar" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Descrição:");

        descricao.setColumns(20);
        descricao.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        descricao.setRows(5);
        jScrollPane2.setViewportView(descricao);

        valorDiariaField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        valorDiariaField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        valorDiariaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valorDiariaFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(220, 220, 220)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(213, 213, 213))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(75, 75, 75))
                                    .addComponent(num))
                                .addGap(108, 108, 108)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(66, 66, 66))
                                    .addComponent(tipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(109, 109, 109)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(5, 5, 5))
                                    .addComponent(valorDiariaField))
                                .addGap(108, 108, 108)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(capacidade, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(108, 108, 108)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(andar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(43, 43, 43)))
                                .addGap(20, 20, 20))
                            .addComponent(jLabel3))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(num, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(capacidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(valorDiariaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(andar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel2))
                                        .addGap(35, 35, 35)))))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "COMODIDADES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 20))); // NOI18N

        wifi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        wifi.setText("Wi-Fi");
        wifi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wifiActionPerformed(evt);
            }
        });

        arCondicionado.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        arCondicionado.setText("Ar Condicionado");
        arCondicionado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arCondicionadoActionPerformed(evt);
            }
        });

        frigobar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        frigobar.setText("Frigobar");
        frigobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frigobarActionPerformed(evt);
            }
        });

        tv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tv.setText("TV");
        tv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tvActionPerformed(evt);
            }
        });

        banheira.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        banheira.setText("Banheira");
        banheira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                banheiraActionPerformed(evt);
            }
        });

        sauna.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sauna.setText("Sauna");
        sauna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saunaActionPerformed(evt);
            }
        });

        ferroDePassar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ferroDePassar.setText("Ferro de Passar");
        ferroDePassar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ferroDePassarActionPerformed(evt);
            }
        });

        sofa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sofa.setText("Sofá");
        sofa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sofaActionPerformed(evt);
            }
        });

        varanda.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        varanda.setText("Varanda");
        varanda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                varandaActionPerformed(evt);
            }
        });

        vistaMar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        vistaMar.setText("Vista para o Mar");
        vistaMar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vistaMarActionPerformed(evt);
            }
        });

        vistaPiscina.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        vistaPiscina.setText("Vista para a Piscina");
        vistaPiscina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vistaPiscinaActionPerformed(evt);
            }
        });

        vistaPanoramica.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        vistaPanoramica.setText("Vista panorâmica");
        vistaPanoramica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vistaPanoramicaActionPerformed(evt);
            }
        });

        secadorDeCabelo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        secadorDeCabelo.setText("Secador de Cabelo");
        secadorDeCabelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secadorDeCabeloActionPerformed(evt);
            }
        });

        aquecedor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        aquecedor.setText("Aquecedor");
        aquecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aquecedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(arCondicionado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(wifi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(vistaMar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frigobar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(44, 44, 44))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(vistaPiscina)
                        .addGap(30, 30, 30)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(banheira, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sauna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(46, 46, 46))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(vistaPanoramica)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(secadorDeCabelo)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ferroDePassar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sofa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(varanda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(22, 22, 22))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(aquecedor)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wifi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frigobar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(banheira, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ferroDePassar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(varanda, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(arCondicionado, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tv, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sauna, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sofa, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(aquecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vistaMar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vistaPiscina, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vistaPanoramica, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(secadorDeCabelo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        salvarbutton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        salvarbutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        salvarbutton.setText("Salvar");
        salvarbutton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        salvarbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarbuttonActionPerformed(evt);
            }
        });

        limparTudo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        limparTudo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-vassoura-20.png"))); // NOI18N
        limparTudo.setText("Limpar Tudo");
        limparTudo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        limparTudo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparTudoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(limparTudo)
                        .addGap(34, 34, 34)
                        .addComponent(salvarbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(70, 70, 70))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salvarbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(limparTudo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1173, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aquecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aquecedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_aquecedorActionPerformed

    private void secadorDeCabeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secadorDeCabeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_secadorDeCabeloActionPerformed

    private void vistaPanoramicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vistaPanoramicaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vistaPanoramicaActionPerformed

    private void vistaPiscinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vistaPiscinaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vistaPiscinaActionPerformed

    private void vistaMarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vistaMarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vistaMarActionPerformed

    private void varandaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_varandaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_varandaActionPerformed

    private void sofaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sofaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sofaActionPerformed

    private void ferroDePassarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ferroDePassarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ferroDePassarActionPerformed

    private void saunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saunaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saunaActionPerformed

    private void banheiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_banheiraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_banheiraActionPerformed

    private void tvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tvActionPerformed

    private void frigobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frigobarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_frigobarActionPerformed

    private void arCondicionadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arCondicionadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arCondicionadoActionPerformed

    private void wifiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wifiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_wifiActionPerformed

    private void tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoActionPerformed

    private void numActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numActionPerformed

    private void valorDiariaFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valorDiariaFieldActionPerformed
    
    }//GEN-LAST:event_valorDiariaFieldActionPerformed

    private void salvarbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarbuttonActionPerformed
        if(validarCamposObrigatorios()){
            salvarQuarto();
        }
    }//GEN-LAST:event_salvarbuttonActionPerformed

    private void limparTudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_limparTudoActionPerformed
        limparCampos();
    }//GEN-LAST:event_limparTudoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> andar;
    private javax.swing.JCheckBox aquecedor;
    private javax.swing.JCheckBox arCondicionado;
    private javax.swing.JCheckBox banheira;
    private javax.swing.JComboBox<String> capacidade;
    private javax.swing.JTextArea descricao;
    private javax.swing.JCheckBox ferroDePassar;
    private javax.swing.JCheckBox frigobar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton limparTudo;
    private javax.swing.JTextField num;
    private javax.swing.JButton salvarbutton;
    private javax.swing.JCheckBox sauna;
    private javax.swing.JCheckBox secadorDeCabelo;
    private javax.swing.JCheckBox sofa;
    private javax.swing.JComboBox<String> tipo;
    private javax.swing.JCheckBox tv;
    private javax.swing.JFormattedTextField valorDiariaField;
    private javax.swing.JCheckBox varanda;
    private javax.swing.JCheckBox vistaMar;
    private javax.swing.JCheckBox vistaPanoramica;
    private javax.swing.JCheckBox vistaPiscina;
    private javax.swing.JCheckBox wifi;
    // End of variables declaration//GEN-END:variables
}
