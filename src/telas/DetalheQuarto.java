package telas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DetalheQuarto extends JFrame {

    // ============================================================
    // ID DO QUARTO
    // ============================================================

    private int idQuarto;

    // ============================================================
    // CALLBACK PARA ATUALIZAR A CONSULTA
    // ============================================================

    private Runnable aoSalvar;

    // ============================================================
    // CAMPOS
    // ============================================================

    private JTextField txtIdQuarto;
    private JTextField txtNumero;
    private JComboBox<String> comboTipo;
    private JTextField txtValorDiaria;
    private JComboBox<String> comboCapacidade;
    private JComboBox<String> comboAndar;
    private JTextArea txtDescricao;

    // ============================================================
    // COMODIDADES
    // ============================================================

    private JCheckBox checkWifi;
    private JCheckBox checkFrigobar;
    private JCheckBox checkBanheira;
    private JCheckBox checkFerroDePassar;
    private JCheckBox checkVaranda;

    private JCheckBox checkArCondicionado;
    private JCheckBox checkTv;
    private JCheckBox checkSauna;
    private JCheckBox checkSofa;
    private JCheckBox checkAquecedor;

    private JCheckBox checkVistaMar;
    private JCheckBox checkVistaPiscina;
    private JCheckBox checkVistaPanoramica;
    private JCheckBox checkSecadorDeCabelo;

    // ============================================================
    // BOTÕES
    // ============================================================

    private JButton btnSalvar;
    private JButton btnFechar;

    // ============================================================
    // CONSTRUTOR
    // ============================================================

    public DetalheQuarto(int idQuarto) {

        this(idQuarto, null);
    }

    public DetalheQuarto(int idQuarto, Runnable aoSalvar) {

        this.idQuarto = idQuarto;
        this.aoSalvar = aoSalvar;

        criarTela();

        carregarQuarto();

        aplicarPermissaoEdicao();
    }

    // ============================================================
    // CRIAR TELA
    // ============================================================

    private void criarTela() {

        setTitle("Dados do Quarto");

        setSize(900, 750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        JPanel painelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // ========================================================
        // TÍTULO
        // ========================================================

        JLabel titulo = new JLabel(
                "DADOS DO QUARTO",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new java.awt.Font(
                        "Arial Black",
                        java.awt.Font.BOLD,
                        24
                )
        );

        painelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        // ========================================================
        // PAINEL DOS DADOS
        // ========================================================

        JPanel painelDados = new JPanel(
                new GridBagLayout()
        );

        painelDados.setBorder(
                BorderFactory.createTitledBorder(
                        "DADOS DO QUARTO"
                )
        );

        // ========================================================
        // CAMPOS
        // ========================================================

        txtIdQuarto = new JTextField();
        txtNumero = new JTextField();

        comboTipo = new JComboBox<>(
                new String[]{
                    "Solteiro",
                    "Casal",
                    "Duplo",
                    "Triplo",
                    "Quádruplo",
                    "Luxo",
                    "Suíte"
                }
        );

        txtValorDiaria = new JTextField();

        comboCapacidade = new JComboBox<>(
                new String[]{
                    "1 Pessoa",
                    "2 Pessoas",
                    "3 Pessoas",
                    "4 Pessoas",
                    "5 Pessoas",
                    "6 Pessoas"
                }
        );

        comboAndar = new JComboBox<>(
                new String[]{
                    "1º Andar",
                    "2º Andar",
                    "3º Andar",
                    "4º Andar",
                    "5º Andar",
                    "6º Andar",
                    "7º Andar",
                    "8º Andar",
                    "9º Andar",
                    "10º Andar"
                }
        );

        txtDescricao = new JTextArea();
        txtDescricao.setRows(4);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);

        adicionarCampo(
                painelDados,
                "ID:",
                txtIdQuarto,
                0,
                0
        );

        adicionarCampo(
                painelDados,
                "Número:",
                txtNumero,
                0,
                1
        );

        adicionarCampo(
                painelDados,
                "Tipo:",
                comboTipo,
                0,
                2
        );

        adicionarCampo(
                painelDados,
                "Valor diária:",
                txtValorDiaria,
                0,
                3
        );

        adicionarCampo(
                painelDados,
                "Capacidade:",
                comboCapacidade,
                0,
                4
        );

        adicionarCampo(
                painelDados,
                "Andar:",
                comboAndar,
                0,
                5
        );

        // ========================================================
        // DESCRIÇÃO
        // ========================================================

        JLabel labelDescricao =
                new JLabel("Descrição:");

        GridBagConstraints gbcDescricao =
                new GridBagConstraints();

        gbcDescricao.insets =
                new Insets(5, 5, 5, 5);

        gbcDescricao.gridx = 0;
        gbcDescricao.gridy = 6;

        gbcDescricao.anchor =
                GridBagConstraints.NORTHWEST;

        painelDados.add(
                labelDescricao,
                gbcDescricao
        );

        JScrollPane scrollDescricao =
                new JScrollPane(txtDescricao);

        GridBagConstraints gbcScroll =
                new GridBagConstraints();

        gbcScroll.gridx = 1;
        gbcScroll.gridy = 6;

        gbcScroll.weightx = 1;
        gbcScroll.weighty = 0.5;

        gbcScroll.fill =
                GridBagConstraints.BOTH;

        gbcScroll.insets =
                new Insets(5, 5, 5, 5);

        painelDados.add(
                scrollDescricao,
                gbcScroll
        );

        // ========================================================
        // COMODIDADES
        // ========================================================

        JPanel painelComodidades =
                new JPanel(
                        new GridBagLayout()
                );

        painelComodidades.setBorder(
                BorderFactory.createTitledBorder(
                        "COMODIDADES"
                )
        );

        checkWifi =
                new JCheckBox("Wi-Fi");

        checkFrigobar =
                new JCheckBox("Frigobar");

        checkBanheira =
                new JCheckBox("Banheira");

        checkFerroDePassar =
                new JCheckBox("Ferro de Passar");

        checkVaranda =
                new JCheckBox("Varanda");

        checkArCondicionado =
                new JCheckBox("Ar Condicionado");

        checkTv =
                new JCheckBox("TV");

        checkSauna =
                new JCheckBox("Sauna");

        checkSofa =
                new JCheckBox("Sofá");

        checkAquecedor =
                new JCheckBox("Aquecedor");

        checkVistaMar =
                new JCheckBox("Vista para o Mar");

        checkVistaPiscina =
                new JCheckBox("Vista para a Piscina");

        checkVistaPanoramica =
                new JCheckBox("Vista panorâmica");

        checkSecadorDeCabelo =
                new JCheckBox("Secador de Cabelo");

        JCheckBox[] checks = {

            checkWifi,
            checkFrigobar,
            checkBanheira,
            checkFerroDePassar,
            checkVaranda,

            checkArCondicionado,
            checkTv,
            checkSauna,
            checkSofa,
            checkAquecedor,

            checkVistaMar,
            checkVistaPiscina,
            checkVistaPanoramica,
            checkSecadorDeCabelo
        };

        for (int i = 0; i < checks.length; i++) {

            GridBagConstraints gbc =
                    new GridBagConstraints();

            gbc.gridx = i % 5;
            gbc.gridy = i / 5;

            gbc.anchor =
                    GridBagConstraints.WEST;

            gbc.insets =
                    new Insets(8, 15, 8, 15);

            painelComodidades.add(
                    checks[i],
                    gbc
            );
        }

        // ========================================================
        // PAINEL CENTRAL
        // ========================================================

        JPanel painelCentro =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        painelCentro.add(
                painelDados,
                BorderLayout.NORTH
        );

        painelCentro.add(
                painelComodidades,
                BorderLayout.CENTER
        );

        painelPrincipal.add(
                painelCentro,
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

        btnFechar =
                new JButton("Fechar");

        btnSalvar =
                new JButton(
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

        setContentPane(painelPrincipal);
    }

    // ============================================================
    // ADICIONAR CAMPO
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
    // CARREGAR QUARTO
    // ============================================================

    private void carregarQuarto() {

        String sql =
                "SELECT id_quarto, numero, tipo, "
                + "valor_diaria, capacidade, andar, descricao "
                + "FROM quarto "
                + "WHERE id_quarto = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    idQuarto
            );

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                txtIdQuarto.setText(
                        String.valueOf(
                                rs.getInt(
                                        "id_quarto"
                                )
                        )
                );

                txtNumero.setText(
                        valor(
                                rs.getString(
                                        "numero"
                                )
                        )
                );

                comboTipo.setSelectedItem(
                        rs.getString("tipo")
                );

                double valorDiaria =
                        rs.getDouble(
                                "valor_diaria"
                        );

                txtValorDiaria.setText(
                        String.format(
                                java.util.Locale.US,
                                "%.2f",
                                valorDiaria
                        )
                );

                int capacidade =
                        rs.getInt(
                                "capacidade"
                        );

                selecionarCapacidade(
                        capacidade
                );

                comboAndar.setSelectedItem(
                        rs.getString("andar")
                );

                txtDescricao.setText(
                        valor(
                                rs.getString(
                                        "descricao"
                                )
                        )
                );
            }

            carregarComodidades();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar quarto:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ============================================================
    // CARREGAR COMODIDADES
    // ============================================================

    private void carregarComodidades() {

        String sql =
                "SELECT id_comodidade "
                + "FROM quarto_comodidade "
                + "WHERE id_quarto = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setInt(
                    1,
                    idQuarto
            );

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                int idComodidade =
                        rs.getInt(
                                "id_comodidade"
                        );

                marcarComodidade(
                        idComodidade
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar comodidades:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ============================================================
    // MARCAR COMODIDADE
    // ============================================================

    private void marcarComodidade(
            int id
    ) {

        switch (id) {

            case 1:
                checkWifi.setSelected(true);
                break;

            case 2:
                checkFrigobar.setSelected(true);
                break;

            case 3:
                checkBanheira.setSelected(true);
                break;

            case 4:
                checkFerroDePassar.setSelected(true);
                break;

            case 5:
                checkVaranda.setSelected(true);
                break;

            case 6:
                checkArCondicionado.setSelected(true);
                break;

            case 7:
                checkTv.setSelected(true);
                break;

            case 8:
                checkSauna.setSelected(true);
                break;

            case 9:
                checkSofa.setSelected(true);
                break;

            case 10:
                checkAquecedor.setSelected(true);
                break;

            case 11:
                checkVistaMar.setSelected(true);
                break;

            case 12:
                checkVistaPiscina.setSelected(true);
                break;

            case 13:
                checkVistaPanoramica.setSelected(true);
                break;

            case 14:
                checkSecadorDeCabelo.setSelected(true);
                break;
        }
    }

    // ============================================================
    // CAPACIDADE
    // ============================================================

    private void selecionarCapacidade(
            int capacidade
    ) {

        if (capacidade >= 1 &&
                capacidade <= 6) {

            comboCapacidade.setSelectedIndex(
                    capacidade - 1
            );
        }
    }

    // ============================================================
    // VALOR
    // ============================================================

    private String valor(
            String texto
    ) {

        if (texto == null) {
            return "";
        }

        return texto;
    }

    // ============================================================
    // PERMISSÃO DE EDIÇÃO
    // ============================================================

    private void aplicarPermissaoEdicao() {

        if (!SessaoUsuario.isAdmin()) {

            txtNumero.setEditable(false);
            comboTipo.setEnabled(false);
            txtValorDiaria.setEditable(false);
            comboCapacidade.setEnabled(false);
            comboAndar.setEnabled(false);
            txtDescricao.setEditable(false);

            checkWifi.setEnabled(false);
            checkFrigobar.setEnabled(false);
            checkBanheira.setEnabled(false);
            checkFerroDePassar.setEnabled(false);
            checkVaranda.setEnabled(false);

            checkArCondicionado.setEnabled(false);
            checkTv.setEnabled(false);
            checkSauna.setEnabled(false);
            checkSofa.setEnabled(false);
            checkAquecedor.setEnabled(false);

            checkVistaMar.setEnabled(false);
            checkVistaPiscina.setEnabled(false);
            checkVistaPanoramica.setEnabled(false);
            checkSecadorDeCabelo.setEnabled(false);

            btnSalvar.setVisible(false);
        }
    }

    // ============================================================
    // SALVAR ALTERAÇÕES
    // ============================================================

    private void salvarAlteracoes() {

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

        String sql =
                "UPDATE quarto SET "
                + "numero = ?, "
                + "tipo = ?, "
                + "valor_diaria = ?, "
                + "capacidade = ?, "
                + "andar = ?, "
                + "descricao = ? "
                + "WHERE id_quarto = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector()
        ) {

            conn.setAutoCommit(false);

            // ====================================================
            // ATUALIZA QUARTO
            // ====================================================

            try (
                    PreparedStatement pst =
                            conn.prepareStatement(sql)
            ) {

                pst.setString(
                        1,
                        txtNumero.getText().trim()
                );

                pst.setString(
                        2,
                        comboTipo
                                .getSelectedItem()
                                .toString()
                );

                String textoValor =
                        txtValorDiaria
                                .getText()
                                .trim();

                textoValor =
                        textoValor
                                .replace("R$", "")
                                .replace(" ", "")
                                .replace(".", "")
                                .replace(",", ".");

                double valorDiaria =
                        Double.parseDouble(
                                textoValor
                        );

                pst.setDouble(
                        3,
                        valorDiaria
                );

                int capacidade =
                        comboCapacidade
                                .getSelectedIndex()
                                + 1;

                pst.setInt(
                        4,
                        capacidade
                );

                pst.setString(
                        5,
                        comboAndar
                                .getSelectedItem()
                                .toString()
                );

                pst.setString(
                        6,
                        txtDescricao.getText()
                );

                pst.setInt(
                        7,
                        idQuarto
                );

                pst.executeUpdate();
            }

            // ====================================================
            // REMOVE COMODIDADES ANTIGAS
            // ====================================================

            String sqlDelete =
                    "DELETE FROM quarto_comodidade "
                    + "WHERE id_quarto = ?";

            try (
                    PreparedStatement pstDelete =
                            conn.prepareStatement(
                                    sqlDelete
                            )
            ) {

                pstDelete.setInt(
                        1,
                        idQuarto
                );

                pstDelete.executeUpdate();
            }

            // ====================================================
            // INSERE NOVAS COMODIDADES
            // ====================================================

            String sqlInsert =
                    "INSERT INTO quarto_comodidade "
                    + "(id_quarto, id_comodidade) "
                    + "VALUES (?, ?)";

            try (
                    PreparedStatement pst =
                            conn.prepareStatement(
                                    sqlInsert
                            )
            ) {

                adicionarComodidade(
                        pst,
                        checkWifi,
                        1
                );

                adicionarComodidade(
                        pst,
                        checkFrigobar,
                        2
                );

                adicionarComodidade(
                        pst,
                        checkBanheira,
                        3
                );

                adicionarComodidade(
                        pst,
                        checkFerroDePassar,
                        4
                );

                adicionarComodidade(
                        pst,
                        checkVaranda,
                        5
                );

                adicionarComodidade(
                        pst,
                        checkArCondicionado,
                        6
                );

                adicionarComodidade(
                        pst,
                        checkTv,
                        7
                );

                adicionarComodidade(
                        pst,
                        checkSauna,
                        8
                );

                adicionarComodidade(
                        pst,
                        checkSofa,
                        9
                );

                adicionarComodidade(
                        pst,
                        checkAquecedor,
                        10
                );

                adicionarComodidade(
                        pst,
                        checkVistaMar,
                        11
                );

                adicionarComodidade(
                        pst,
                        checkVistaPiscina,
                        12
                );

                adicionarComodidade(
                        pst,
                        checkVistaPanoramica,
                        13
                );

                adicionarComodidade(
                        pst,
                        checkSecadorDeCabelo,
                        14
                );
            }

            conn.commit();

            JOptionPane.showMessageDialog(
                    this,
                    "Quarto atualizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Atualiza a tela de consulta
            if (aoSalvar != null) {
                aoSalvar.run();
            }

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar quarto:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ============================================================
    // ADICIONAR COMODIDADE
    // ============================================================

    private void adicionarComodidade(
            PreparedStatement pst,
            JCheckBox checkBox,
            int idComodidade
    ) throws Exception {

        if (checkBox.isSelected()) {

            pst.setInt(
                    1,
                    idQuarto
            );

            pst.setInt(
                    2,
                    idComodidade
            );

            pst.executeUpdate();
        }
    }
}