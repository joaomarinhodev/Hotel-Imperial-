package telas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DetalheFuncionario extends JFrame {

    private int idFuncionario;
    private Runnable aoSalvar;

    private JTextField nomeFunc;
    private JTextField sobreNomeFunc;
    private JTextField cpfFunc;
    private JTextField rgFunc;
    private JTextField dataNascFunc;
    private JTextField cargoFunc;

    private JComboBox<String> sexoFunc;
    private JComboBox<String> grauAcessoFunc;

    private JTextField nomeUsuarioFunc;
    private JPasswordField senhaFunc;

    private JTextField emailFunc;
    private JTextField celularFunc;
    private JTextField telFixoFunc;

    private JTextField ruaFunc;
    private JTextField bairroFunc;
    private JTextField complementoCasaFunc;
    private JTextField cidadeFunc;
    private JTextField cepFunc;
    private JTextField numCasaFunc;
    private JComboBox<String> estadoFunc;

    private String senhaAtual;

    public DetalheFuncionario(int idFuncionario) {
        this(idFuncionario, null);
    }

    public DetalheFuncionario(int idFuncionario, Runnable aoSalvar) {

        this.idFuncionario = idFuncionario;
        this.aoSalvar = aoSalvar;

        criarTela();
        carregarFuncionario();
    }

    private void criarTela() {

        setTitle("Hotel Imperial - Detalhes do Funcionário");
        setSize(850, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        nomeFunc = new JTextField(25);
        sobreNomeFunc = new JTextField(25);
        cpfFunc = new JTextField(25);
        rgFunc = new JTextField(25);
        dataNascFunc = new JTextField(25);
        cargoFunc = new JTextField(25);

        sexoFunc = new JComboBox<>(
                new String[]{
                    "Selecione",
                    "Masculino",
                    "Feminino"
                }
        );

        grauAcessoFunc = new JComboBox<>(
                new String[]{
                    "Selecione",
                    "Consultor",
                    "Gerente",
                    "Admin"
                }
        );

        nomeUsuarioFunc = new JTextField(25);
        senhaFunc = new JPasswordField(25);

        emailFunc = new JTextField(25);
        celularFunc = new JTextField(25);
        telFixoFunc = new JTextField(25);

        ruaFunc = new JTextField(25);
        bairroFunc = new JTextField(25);
        complementoCasaFunc = new JTextField(25);
        cidadeFunc = new JTextField(25);
        cepFunc = new JTextField(25);
        numCasaFunc = new JTextField(25);

        estadoFunc = new JComboBox<>(
                new String[]{
                    "Selecione",
                    "AC", "AL", "AP", "AM", "BA",
                    "CE", "DF", "ES", "GO", "MA",
                    "MT", "MS", "MG", "PA", "PE",
                    "PR", "PB", "PI", "RJ", "RN",
                    "RS", "RO", "RR", "SC", "SP",
                    "SE", "TO"
                }
        );

        int linha = 0;

        adicionarCampo(painel, c, linha++, "Nome:", nomeFunc);
        adicionarCampo(painel, c, linha++, "Sobrenome:", sobreNomeFunc);
        adicionarCampo(painel, c, linha++, "CPF:", cpfFunc);
        adicionarCampo(painel, c, linha++, "RG:", rgFunc);
        adicionarCampo(painel, c, linha++, "Data de nascimento:", dataNascFunc);
        adicionarCampo(painel, c, linha++, "Cargo:", cargoFunc);

        adicionarCampo(painel, c, linha++, "Sexo:", sexoFunc);
        adicionarCampo(painel, c, linha++, "Grau de acesso:", grauAcessoFunc);

        adicionarCampo(painel, c, linha++, "Nome de usuário:", nomeUsuarioFunc);
        adicionarCampo(painel, c, linha++, "Senha:", senhaFunc);

        adicionarCampo(painel, c, linha++, "E-mail:", emailFunc);
        adicionarCampo(painel, c, linha++, "Celular:", celularFunc);
        adicionarCampo(painel, c, linha++, "Telefone fixo:", telFixoFunc);

        adicionarCampo(painel, c, linha++, "Rua:", ruaFunc);
        adicionarCampo(painel, c, linha++, "Bairro:", bairroFunc);
        adicionarCampo(painel, c, linha++, "Complemento:", complementoCasaFunc);
        adicionarCampo(painel, c, linha++, "Cidade:", cidadeFunc);
        adicionarCampo(painel, c, linha++, "CEP:", cepFunc);
        adicionarCampo(painel, c, linha++, "Número:", numCasaFunc);
        adicionarCampo(painel, c, linha++, "Estado:", estadoFunc);

        JButton salvar = new JButton("Salvar alterações");
        JButton cancelar = new JButton("Cancelar");

        c.gridx = 0;
        c.gridy = linha;
        c.gridwidth = 1;
        painel.add(cancelar, c);

        c.gridx = 1;
        painel.add(salvar, c);

        cancelar.addActionListener(e -> dispose());

        salvar.addActionListener(e -> atualizarFuncionario());

        add(painel, BorderLayout.CENTER);
    }

    private void adicionarCampo(
            JPanel painel,
            GridBagConstraints c,
            int linha,
            String texto,
            java.awt.Component campo) {

        c.gridy = linha;

        c.gridx = 0;
        c.gridwidth = 1;
        painel.add(new JLabel(texto), c);

        c.gridx = 1;
        c.gridwidth = 2;
        painel.add(campo, c);
    }

    private void carregarFuncionario() {

        String sql =
                "SELECT "
                + "nome_funcionario, "
                + "sobrenome_funcionario, "
                + "datanasc_funcionario, "
                + "cpf_funcionario, "
                + "rg_funcionario, "
                + "cargo_funcionario, "
                + "nome_usuario_func, "
                + "senha_usuario_func, "
                + "grauacesso_usuario_func, "
                + "sexo_funcionario, "
                + "email_funcionario, "
                + "cell_funcionario, "
                + "tellfixo_funcionario, "
                + "rua_fucionario, "
                + "bairro_funcionario, "
                + "complemento_fu, "
                + "cidade_funcionario, "
                + "cep_funcionario, "
                + "numero_casa_funcionario, "
                + "estado_funcionario "
                + "FROM funcionarios "
                + "WHERE id_funcionario = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            pst.setInt(1, idFuncionario);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                nomeFunc.setText(
                        rs.getString("nome_funcionario")
                );

                sobreNomeFunc.setText(
                        rs.getString("sobrenome_funcionario")
                );

                cpfFunc.setText(
                        rs.getString("cpf_funcionario")
                );

                rgFunc.setText(
                        rs.getString("rg_funcionario")
                );

                Date data = rs.getDate(
                        "datanasc_funcionario"
                );

                if (data != null) {

                    LocalDate dataLocal =
                            data.toLocalDate();

                    dataNascFunc.setText(
                            dataLocal.format(
                                    DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy"
                                    )
                            )
                    );
                }

                cargoFunc.setText(
                        rs.getString("cargo_funcionario")
                );

                selecionarCombo(
                        sexoFunc,
                        rs.getString("sexo_funcionario")
                );

                selecionarCombo(
                        grauAcessoFunc,
                        rs.getString(
                                "grauacesso_usuario_func"
                        )
                );

                nomeUsuarioFunc.setText(
                        rs.getString("nome_usuario_func")
                );

                senhaAtual =
                        rs.getString("senha_usuario_func");

                // Não mostramos a senha real no campo.
                senhaFunc.setText("");

                emailFunc.setText(
                        rs.getString("email_funcionario")
                );

                celularFunc.setText(
                        rs.getString("cell_funcionario")
                );

                telFixoFunc.setText(
                        rs.getString("tellfixo_funcionario")
                );

                ruaFunc.setText(
                        rs.getString("rua_fucionario")
                );

                bairroFunc.setText(
                        rs.getString("bairro_funcionario")
                );

                complementoCasaFunc.setText(
                        rs.getString("complemento_fu")
                );

                cidadeFunc.setText(
                        rs.getString("cidade_funcionario")
                );

                cepFunc.setText(
                        rs.getString("cep_funcionario")
                );

                numCasaFunc.setText(
                        rs.getString("numero_casa_funcionario")
                );

                selecionarCombo(
                        estadoFunc,
                        rs.getString("estado_funcionario")
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar funcionário:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void selecionarCombo(
            JComboBox<String> combo,
            String valor) {

        if (valor == null) {
            return;
        }

        for (int i = 0; i < combo.getItemCount(); i++) {

            if (valor.equals(combo.getItemAt(i))) {

                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void atualizarFuncionario() {

        String sql =
                "UPDATE funcionarios SET "
                + "nome_funcionario = ?, "
                + "sobrenome_funcionario = ?, "
                + "datanasc_funcionario = ?, "
                + "cpf_funcionario = ?, "
                + "rg_funcionario = ?, "
                + "cargo_funcionario = ?, "
                + "nome_usuario_func = ?, "
                + "senha_usuario_func = ?, "
                + "grauacesso_usuario_func = ?, "
                + "sexo_funcionario = ?, "
                + "email_funcionario = ?, "
                + "cell_funcionario = ?, "
                + "tellfixo_funcionario = ?, "
                + "rua_fucionario = ?, "
                + "bairro_funcionario = ?, "
                + "complemento_fu = ?, "
                + "cidade_funcionario = ?, "
                + "cep_funcionario = ?, "
                + "numero_casa_funcionario = ?, "
                + "estado_funcionario = ? "
                + "WHERE id_funcionario = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();

                PreparedStatement pst =
                        conn.prepareStatement(sql)
        ) {

            LocalDate data =
                    LocalDate.parse(
                            dataNascFunc.getText().trim(),
                            DateTimeFormatter.ofPattern(
                                    "dd/MM/yyyy"
                            )
                    );

            pst.setString(1, nomeFunc.getText().trim());
            pst.setString(2, sobreNomeFunc.getText().trim());
            pst.setDate(3, Date.valueOf(data));
            pst.setString(4, cpfFunc.getText().trim());
            pst.setString(5, rgFunc.getText().trim());
            pst.setString(6, cargoFunc.getText().trim());

            pst.setString(
                    7,
                    nomeUsuarioFunc.getText().trim()
            );

            String novaSenha =
                    new String(senhaFunc.getPassword());

            if (novaSenha.trim().isEmpty()) {

                pst.setString(8, senhaAtual);

            } else {

                pst.setString(8, novaSenha);
            }

            pst.setString(
                    9,
                    grauAcessoFunc
                            .getSelectedItem()
                            .toString()
            );

            pst.setString(
                    10,
                    sexoFunc
                            .getSelectedItem()
                            .toString()
            );

            pst.setString(11, emailFunc.getText().trim());
            pst.setString(12, celularFunc.getText().trim());
            pst.setString(13, telFixoFunc.getText().trim());
            pst.setString(14, ruaFunc.getText().trim());
            pst.setString(15, bairroFunc.getText().trim());
            pst.setString(16, complementoCasaFunc.getText().trim());
            pst.setString(17, cidadeFunc.getText().trim());
            pst.setString(18, cepFunc.getText().trim());
            pst.setString(19, numCasaFunc.getText().trim());
            pst.setString(
                    20,
                    estadoFunc
                            .getSelectedItem()
                            .toString()
            );

            pst.setInt(21, idFuncionario);

            int resultado = pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Funcionário atualizado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (aoSalvar != null) {
                    aoSalvar.run();
                }

                dispose();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar funcionário:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}