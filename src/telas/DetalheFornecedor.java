package telas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DetalheFornecedor extends JFrame {

    // ============================================================
    // ID DO FORNECEDOR
    // ============================================================

    private int idFornecedor;
    private Runnable aoSalvar;

    // ============================================================
    // CAMPOS
    // ============================================================

    private JTextField txtIdFornecedor;
    private JTextField txtRazaoSocial;
    private JTextField txtNomeFantasia;
    private JTextField txtCnpj;
    private JTextField txtInscricaoEstadual;

    private JTextField txtResponsavel;
    private JTextField txtCelular;
    private JTextField txtEmail;
    private JTextField txtTelefoneFixo;

    private JTextField txtRua;
    private JTextField txtBairro;
    private JTextField txtCidade;
    private JTextField txtCep;
    private JTextField txtNumero;
    private JTextField txtComplemento;

    private JComboBox<String> comboEstado;

    private javax.swing.JRadioButton radioAlimentacao;
    private javax.swing.JRadioButton radioBebidas;
    private javax.swing.JRadioButton radioLimpeza;
    private javax.swing.JRadioButton radioMoveis;
    private javax.swing.JRadioButton radioServicos;

    private ButtonGroup grupoFornecedor;

    private JTextArea txtObservacao;

    private JButton btnSalvar;
    private JButton btnFechar;

    // ============================================================
    // CONSTRUTOR
    // ============================================================

    public DetalheFornecedor(int idFornecedor) {

        this(idFornecedor, null);
    }

    public DetalheFornecedor(int idFornecedor, Runnable aoSalvar) {

        this.idFornecedor = idFornecedor;
        this.aoSalvar = aoSalvar;

        criarTela();

        carregarFornecedor();
        aplicarPermissaoEdicao();
    }

    private void aplicarPermissaoEdicao() {

        if (!SessaoUsuario.isAdmin()) {

            txtRazaoSocial.setEditable(false);
            txtNomeFantasia.setEditable(false);
            txtCnpj.setEditable(false);
            txtInscricaoEstadual.setEditable(false);

            txtResponsavel.setEditable(false);
            txtCelular.setEditable(false);
            txtEmail.setEditable(false);
            txtTelefoneFixo.setEditable(false);

            txtRua.setEditable(false);
            txtBairro.setEditable(false);
            txtCidade.setEditable(false);
            txtCep.setEditable(false);
            txtNumero.setEditable(false);
            txtComplemento.setEditable(false);

            comboEstado.setEnabled(false);

            radioAlimentacao.setEnabled(false);
            radioBebidas.setEnabled(false);
            radioLimpeza.setEnabled(false);
            radioMoveis.setEnabled(false);
            radioServicos.setEnabled(false);

            txtObservacao.setEditable(false);

            btnSalvar.setVisible(false);
        }
    }
    
    // ============================================================
    // CRIA A INTERFACE
    // ============================================================

    private void criarTela() {

        setTitle("Dados do Fornecedor");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --------------------------------------------------------
        // PAINEL PRINCIPAL
        // --------------------------------------------------------

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));

        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // --------------------------------------------------------
        // TÍTULO
        // --------------------------------------------------------

        JLabel titulo = new JLabel(
                "DADOS DO FORNECEDOR",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new java.awt.Font("Arial Black", java.awt.Font.BOLD, 24)
        );

        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // --------------------------------------------------------
        // PAINEL DOS DADOS
        // --------------------------------------------------------

        JPanel painelDados = new JPanel();

        painelDados.setLayout(
                new GridLayout(0, 1, 10, 10)
        );

        // ========================================================
        // DADOS DA EMPRESA
        // ========================================================

        JPanel painelEmpresa = new JPanel(
                new GridBagLayout()
        );

        painelEmpresa.setBorder(
                BorderFactory.createTitledBorder(
                        "DADOS DA EMPRESA"
                )
        );

        txtIdFornecedor = new JTextField();
        txtRazaoSocial = new JTextField();
        txtNomeFantasia = new JTextField();
        txtCnpj = new JTextField();
        txtInscricaoEstadual = new JTextField();

        txtIdFornecedor.setEditable(false);

        adicionarCampo(
                painelEmpresa,
                "ID:",
                txtIdFornecedor,
                0,
                0
        );

        adicionarCampo(
                painelEmpresa,
                "Razão Social:",
                txtRazaoSocial,
                0,
                1
        );

        adicionarCampo(
                painelEmpresa,
                "Nome Fantasia:",
                txtNomeFantasia,
                0,
                2
        );

        adicionarCampo(
                painelEmpresa,
                "CNPJ:",
                txtCnpj,
                0,
                3
        );

        adicionarCampo(
                painelEmpresa,
                "Inscrição Estadual:",
                txtInscricaoEstadual,
                0,
                4
        );

        painelDados.add(painelEmpresa);

        // ========================================================
        // CONTATO
        // ========================================================

        JPanel painelContato = new JPanel(
                new GridBagLayout()
        );

        painelContato.setBorder(
                BorderFactory.createTitledBorder(
                        "CONTATO"
                )
        );

        txtResponsavel = new JTextField();
        txtCelular = new JTextField();
        txtEmail = new JTextField();
        txtTelefoneFixo = new JTextField();

        adicionarCampo(
                painelContato,
                "Responsável:",
                txtResponsavel,
                0,
                0
        );

        adicionarCampo(
                painelContato,
                "Celular:",
                txtCelular,
                0,
                1
        );

        adicionarCampo(
                painelContato,
                "E-mail:",
                txtEmail,
                0,
                2
        );

        adicionarCampo(
                painelContato,
                "Telefone Fixo:",
                txtTelefoneFixo,
                0,
                3
        );

        painelDados.add(painelContato);

        // ========================================================
        // ENDEREÇO
        // ========================================================

        JPanel painelEndereco = new JPanel(
                new GridBagLayout()
        );

        painelEndereco.setBorder(
                BorderFactory.createTitledBorder(
                        "ENDEREÇO"
                )
        );

        txtRua = new JTextField();
        txtBairro = new JTextField();
        txtCidade = new JTextField();
        txtCep = new JTextField();
        txtNumero = new JTextField();
        txtComplemento = new JTextField();

        comboEstado = new JComboBox<>(
                new String[]{
                    "Selecione",
                    "AC", "AL", "AP", "AM",
                    "BA", "CE", "DF", "ES",
                    "GO", "MA", "MT", "MS",
                    "MG", "PA", "PB", "PR",
                    "PE", "PI", "RJ", "RN",
                    "RS", "RO", "RR", "SC",
                    "SP", "SE", "TO"
                }
        );

        adicionarCampo(
                painelEndereco,
                "Rua:",
                txtRua,
                0,
                0
        );

        adicionarCampo(
                painelEndereco,
                "Número:",
                txtNumero,
                0,
                1
        );

        adicionarCampo(
                painelEndereco,
                "Bairro:",
                txtBairro,
                0,
                2
        );

        adicionarCampo(
                painelEndereco,
                "Cidade:",
                txtCidade,
                0,
                3
        );

        adicionarCampo(
                painelEndereco,
                "CEP:",
                txtCep,
                0,
                4
        );

        adicionarCampo(
                painelEndereco,
                "Estado:",
                comboEstado,
                0,
                5
        );

        adicionarCampo(
                painelEndereco,
                "Complemento:",
                txtComplemento,
                0,
                6
        );

        painelDados.add(painelEndereco);

        // ========================================================
        // TIPO DE FORNECEDOR
        // ========================================================

        JPanel painelTipo = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        painelTipo.setBorder(
                BorderFactory.createTitledBorder(
                        "TIPO DE FORNECEDOR"
                )
        );

        radioAlimentacao =
                new javax.swing.JRadioButton(
                        "Alimentação"
                );

        radioBebidas =
                new javax.swing.JRadioButton(
                        "Bebidas"
                );

        radioLimpeza =
                new javax.swing.JRadioButton(
                        "Limpeza"
                );

        radioMoveis =
                new javax.swing.JRadioButton(
                        "Móveis"
                );

        radioServicos =
                new javax.swing.JRadioButton(
                        "Serviços"
                );

        grupoFornecedor = new ButtonGroup();

        grupoFornecedor.add(radioAlimentacao);
        grupoFornecedor.add(radioBebidas);
        grupoFornecedor.add(radioLimpeza);
        grupoFornecedor.add(radioMoveis);
        grupoFornecedor.add(radioServicos);

        painelTipo.add(radioAlimentacao);
        painelTipo.add(radioBebidas);
        painelTipo.add(radioLimpeza);
        painelTipo.add(radioMoveis);
        painelTipo.add(radioServicos);

        painelDados.add(painelTipo);

        // ========================================================
        // OBSERVAÇÕES
        // ========================================================

        JPanel painelObservacao = new JPanel(
                new BorderLayout(5, 5)
        );

        painelObservacao.setBorder(
                BorderFactory.createTitledBorder(
                        "OBSERVAÇÕES"
                )
        );

        txtObservacao = new JTextArea();

        txtObservacao.setRows(5);
        txtObservacao.setLineWrap(true);
        txtObservacao.setWrapStyleWord(true);

        JScrollPane scrollObservacao =
                new JScrollPane(txtObservacao);

        painelObservacao.add(
                scrollObservacao,
                BorderLayout.CENTER
        );

        painelDados.add(painelObservacao);

        // --------------------------------------------------------
        // SCROLL PRINCIPAL
        // --------------------------------------------------------

        JScrollPane scrollPrincipal =
                new JScrollPane(painelDados);

        painelPrincipal.add(
                scrollPrincipal,
                BorderLayout.CENTER
        );

        // ========================================================
        // BOTÕES
        // ========================================================

        JPanel painelBotoes =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        btnFechar = new JButton(
                "Fechar"
        );

        btnSalvar = new JButton(
                "Salvar Alterações"
        );

        btnFechar.addActionListener(
                e -> dispose()
        );

        btnSalvar.addActionListener(
                e -> salvarAlteracoes()
        );

        painelBotoes.add(btnFechar);
        painelBotoes.add(btnSalvar);

        painelPrincipal.add(
                painelBotoes,
                BorderLayout.SOUTH
        );

        // --------------------------------------------------------
        // MOSTRAR TELA
        // --------------------------------------------------------

        setContentPane(painelPrincipal);
    }

    // ============================================================
    // MÉTODO PARA ADICIONAR CAMPOS
    // ============================================================

    private void adicionarCampo(
            JPanel painel,
            String texto,
            java.awt.Component componente,
            int coluna,
            int linha
    ) {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(5, 5, 5, 5);

        gbc.anchor =
                GridBagConstraints.WEST;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        JLabel label =
                new JLabel(texto);

        gbc.gridx = coluna;
        gbc.gridy = linha;
        gbc.weightx = 0;

        painel.add(
                label,
                gbc
        );

        gbc.gridx = coluna + 1;
        gbc.weightx = 1;

        painel.add(
                componente,
                gbc
        );
    }

    // ============================================================
    // CARREGAR FORNECEDOR
    // ============================================================

    private void carregarFornecedor() {

        String sql =
                "SELECT "
                + "id_fornecedor, "
                + "razao_social, "
                + "cnpj, "
                + "nome_fantasia, "
                + "incricao_estadual, "
                + "responsavel, "
                + "cell, "
                + "email, "
                + "tellfixo, "
                + "rua, "
                + "bairro, "
                + "cidade, "
                + "cep, "
                + "numero, "
                + "estado, "
                + "complemento_endereco, "
                + "observacao, "
                + "tipo_fornecedor "
                + "FROM fornecedor "
                + "WHERE id_fornecedor = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    idFornecedor
            );

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                txtIdFornecedor.setText(
                        String.valueOf(
                                rs.getInt(
                                        "id_fornecedor"
                                )
                        )
                );

                txtRazaoSocial.setText(
                        valor(rs.getString(
                                "razao_social"
                        ))
                );

                txtNomeFantasia.setText(
                        valor(rs.getString(
                                "nome_fantasia"
                        ))
                );

                txtCnpj.setText(
                        valor(rs.getString(
                                "cnpj"
                        ))
                );

                txtInscricaoEstadual.setText(
                        valor(rs.getString(
                                "incricao_estadual"
                        ))
                );

                txtResponsavel.setText(
                        valor(rs.getString(
                                "responsavel"
                        ))
                );

                txtCelular.setText(
                        valor(rs.getString(
                                "cell"
                        ))
                );

                txtEmail.setText(
                        valor(rs.getString(
                                "email"
                        ))
                );

                txtTelefoneFixo.setText(
                        valor(rs.getString(
                                "tellfixo"
                        ))
                );

                txtRua.setText(
                        valor(rs.getString(
                                "rua"
                        ))
                );

                txtBairro.setText(
                        valor(rs.getString(
                                "bairro"
                        ))
                );

                txtCidade.setText(
                        valor(rs.getString(
                                "cidade"
                        ))
                );

                txtCep.setText(
                        valor(rs.getString(
                                "cep"
                        ))
                );

                txtNumero.setText(
                        valor(rs.getString(
                                "numero"
                        ))
                );

                comboEstado.setSelectedItem(
                        rs.getString("estado")
                );

                txtComplemento.setText(
                        valor(rs.getString(
                                "complemento_endereco"
                        ))
                );

                txtObservacao.setText(
                        valor(rs.getString(
                                "observacao"
                        ))
                );

                selecionarTipoFornecedor(
                        rs.getString(
                                "tipo_fornecedor"
                        )
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar fornecedor:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ============================================================
    // EVITA APARECER "NULL"
    // ============================================================

    private String valor(String texto) {

        if (texto == null) {
            return "";
        }

        return texto;
    }

    // ============================================================
    // SELECIONAR TIPO DE FORNECEDOR
    // ============================================================

    private void selecionarTipoFornecedor(
            String tipo
    ) {

        if (tipo == null) {
            return;
        }

        switch (tipo) {

            case "Alimentação":
                radioAlimentacao.setSelected(true);
                break;

            case "Bebidas":
                radioBebidas.setSelected(true);
                break;

            case "Limpeza":
                radioLimpeza.setSelected(true);
                break;

            case "Móveis":
                radioMoveis.setSelected(true);
                break;

            case "Serviços":
                radioServicos.setSelected(true);
                break;
        }
    }

    // ============================================================
    // SALVAR ALTERAÇÕES
    // ============================================================

    private void salvarAlteracoes() {

        // --------------------------------------------------------
        // TIPO DE FORNECEDOR
        // --------------------------------------------------------

        String tipoFornecedor = null;

        if (radioAlimentacao.isSelected()) {

            tipoFornecedor = "Alimentação";

        } else if (radioBebidas.isSelected()) {

            tipoFornecedor = "Bebidas";

        } else if (radioLimpeza.isSelected()) {

            tipoFornecedor = "Limpeza";

        } else if (radioMoveis.isSelected()) {

            tipoFornecedor = "Móveis";

        } else if (radioServicos.isSelected()) {

            tipoFornecedor = "Serviços";
        }

        if (tipoFornecedor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione o tipo de fornecedor!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // --------------------------------------------------------
        // CONFIRMAÇÃO
        // --------------------------------------------------------

        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja salvar as alterações?",
                        "Confirmar alteração",
                        JOptionPane.YES_NO_OPTION
                );

        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        // --------------------------------------------------------
        // SQL UPDATE
        // --------------------------------------------------------

        String sql =
                "UPDATE fornecedor SET "
                + "razao_social = ?, "
                + "cnpj = ?, "
                + "nome_fantasia = ?, "
                + "incricao_estadual = ?, "
                + "responsavel = ?, "
                + "cell = ?, "
                + "email = ?, "
                + "tellfixo = ?, "
                + "rua = ?, "
                + "bairro = ?, "
                + "cidade = ?, "
                + "cep = ?, "
                + "numero = ?, "
                + "estado = ?, "
                + "complemento_endereco = ?, "
                + "observacao = ?, "
                + "tipo_fornecedor = ? "
                + "WHERE id_fornecedor = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setString(
                    1,
                    txtRazaoSocial.getText()
            );

            pst.setString(
                    2,
                    txtCnpj.getText()
            );

            pst.setString(
                    3,
                    txtNomeFantasia.getText()
            );

            // ----------------------------------------------------
            // INSCRIÇÃO ESTADUAL
            // ----------------------------------------------------

            String inscricao =
                    txtInscricaoEstadual
                            .getText()
                            .trim();

            if (inscricao.isEmpty()) {

                pst.setNull(
                        4,
                        java.sql.Types.VARCHAR
                );

            } else {

                pst.setString(
                        4,
                        inscricao
                );
            }

            pst.setString(
                    5,
                    txtResponsavel.getText()
            );

            pst.setString(
                    6,
                    txtCelular.getText()
            );

            pst.setString(
                    7,
                    txtEmail.getText()
            );

            // ----------------------------------------------------
            // TELEFONE FIXO
            // ----------------------------------------------------

            String telefone =
                    txtTelefoneFixo
                            .getText()
                            .trim();

            if (telefone.isEmpty()) {

                pst.setNull(
                        8,
                        java.sql.Types.VARCHAR
                );

            } else {

                pst.setString(
                        8,
                        telefone
                );
            }

            pst.setString(
                    9,
                    txtRua.getText()
            );

            pst.setString(
                    10,
                    txtBairro.getText()
            );

            pst.setString(
                    11,
                    txtCidade.getText()
            );

            pst.setString(
                    12,
                    txtCep.getText()
            );

            pst.setString(
                    13,
                    txtNumero.getText()
            );

            // ----------------------------------------------------
            // ESTADO
            // ----------------------------------------------------

            pst.setString(
                    14,
                    comboEstado
                            .getSelectedItem()
                            .toString()
            );

            // ----------------------------------------------------
            // COMPLEMENTO
            // ----------------------------------------------------

            String complemento =
                    txtComplemento
                            .getText()
                            .trim();

            if (complemento.isEmpty()) {

                pst.setNull(
                        15,
                        java.sql.Types.VARCHAR
                );

            } else {

                pst.setString(
                        15,
                        complemento
                );
            }

            // ----------------------------------------------------
            // OBSERVAÇÃO
            // ----------------------------------------------------

            pst.setString(
                    16,
                    txtObservacao.getText()
            );

            // ----------------------------------------------------
            // TIPO
            // ----------------------------------------------------

            pst.setString(
                    17,
                    tipoFornecedor
            );

            // ----------------------------------------------------
            // ID
            // ----------------------------------------------------

            pst.setInt(
                    18,
                    idFornecedor
            );

            // ----------------------------------------------------
            // EXECUTA UPDATE
            // ----------------------------------------------------

            int resultado =
                    pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Fornecedor atualizado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (aoSalvar != null) {
                    aoSalvar.run();
                }

                dispose();
            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Nenhuma alteração foi realizada.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar fornecedor:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}