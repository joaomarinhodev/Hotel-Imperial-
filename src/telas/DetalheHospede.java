package telas;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DetalheHospede extends JFrame {

    private int idHospede;
    private Runnable aoSalvar;

    private JTextField nomeHosp;
    private JTextField sobreNomeHosp;
    private JTextField dataNascHosp;
    private JTextField cpfHosp;
    private JTextField rgHosp;
    private JTextField sexoHosp;
    private JTextField emailHosp;
    private JTextField celularHosp;
    private JTextField telFixoHosp;
    private JTextField ruaHosp;
    private JTextField bairroHosp;
    private JTextField complementoCasaHosp;
    private JTextField cidadeHosp;
    private JTextField cepHosp;
    private JTextField numCasaHosp;
    private JTextField estadoHosp;

    private JRadioButton comumRadio;
    private JRadioButton vipRadio;
    private JRadioButton empresarioRadio;

    private JTextArea obsHosp;

    private ButtonGroup grupoHospede;

    public DetalheHospede(int idHospede) {
        this(idHospede, null);
    }

    public DetalheHospede(int idHospede, Runnable aoSalvar) {
        this.idHospede = idHospede;
        this.aoSalvar = aoSalvar;

        criarTela();
        carregarHospede();
    }

    private void criarTela() {

        setTitle("Hotel Imperial - Detalhes do Hóspede");
        setSize(850, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nomeHosp = new JTextField(25);
        sobreNomeHosp = new JTextField(25);
        dataNascHosp = new JTextField(25);
        cpfHosp = new JTextField(25);
        rgHosp = new JTextField(25);
        sexoHosp = new JTextField(25);
        emailHosp = new JTextField(25);
        celularHosp = new JTextField(25);
        telFixoHosp = new JTextField(25);
        ruaHosp = new JTextField(25);
        bairroHosp = new JTextField(25);
        complementoCasaHosp = new JTextField(25);
        cidadeHosp = new JTextField(25);
        cepHosp = new JTextField(25);
        numCasaHosp = new JTextField(25);
        estadoHosp = new JTextField(25);

        comumRadio = new JRadioButton("Comum");
        vipRadio = new JRadioButton("VIP");
        empresarioRadio = new JRadioButton("Empresário");

        grupoHospede = new ButtonGroup();
        grupoHospede.add(comumRadio);
        grupoHospede.add(vipRadio);
        grupoHospede.add(empresarioRadio);

        obsHosp = new JTextArea(4, 25);
        obsHosp.setLineWrap(true);
        obsHosp.setWrapStyleWord(true);

        JScrollPane scrollObs = new JScrollPane(obsHosp);

        int linha = 0;

        adicionarCampo(painel, gbc, linha++, "Nome:", nomeHosp);
        adicionarCampo(painel, gbc, linha++, "Sobrenome:", sobreNomeHosp);
        adicionarCampo(painel, gbc, linha++, "Data de nascimento:", dataNascHosp);
        adicionarCampo(painel, gbc, linha++, "CPF:", cpfHosp);
        adicionarCampo(painel, gbc, linha++, "RG:", rgHosp);
        adicionarCampo(painel, gbc, linha++, "Sexo:", sexoHosp);
        adicionarCampo(painel, gbc, linha++, "E-mail:", emailHosp);
        adicionarCampo(painel, gbc, linha++, "Celular:", celularHosp);
        adicionarCampo(painel, gbc, linha++, "Telefone fixo:", telFixoHosp);
        adicionarCampo(painel, gbc, linha++, "Rua:", ruaHosp);
        adicionarCampo(painel, gbc, linha++, "Bairro:", bairroHosp);
        adicionarCampo(painel, gbc, linha++, "Complemento:", complementoCasaHosp);
        adicionarCampo(painel, gbc, linha++, "Cidade:", cidadeHosp);
        adicionarCampo(painel, gbc, linha++, "CEP:", cepHosp);
        adicionarCampo(painel, gbc, linha++, "Número:", numCasaHosp);
        adicionarCampo(painel, gbc, linha++, "Estado:", estadoHosp);

        gbc.gridx = 0;
        gbc.gridy = linha;
        painel.add(new JLabel("Tipo de hóspede:"), gbc);

        JPanel painelRadio = new JPanel();
        painelRadio.add(comumRadio);
        painelRadio.add(vipRadio);
        painelRadio.add(empresarioRadio);

        gbc.gridx = 1;
        painel.add(painelRadio, gbc);

        linha++;

        gbc.gridx = 0;
        gbc.gridy = linha;
        painel.add(new JLabel("Observações:"), gbc);

        gbc.gridx = 1;
        painel.add(scrollObs, gbc);

        linha++;

        JButton salvarButton = new JButton("Salvar alterações");
        JButton cancelarButton = new JButton("Cancelar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(salvarButton);
        painelBotoes.add(cancelarButton);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        painel.add(painelBotoes, gbc);

        salvarButton.addActionListener(e -> atualizarHospede());

        cancelarButton.addActionListener(e -> dispose());

        JScrollPane scrollPrincipal = new JScrollPane(painel);
        add(scrollPrincipal);
    }

    private void adicionarCampo(
            JPanel painel,
            GridBagConstraints gbc,
            int linha,
            String texto,
            JTextField campo) {

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;

        painel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;

        painel.add(campo, gbc);
    }

    private void carregarHospede() {

        String sql =
                "SELECT nome_hospedi, sobrenome_hospedi, "
                + "datanasc_hospedi, cpf_hospedi, rg_hospedi, "
                + "sexo_hospedi, email_hospedi, cell_hospedi, "
                + "tellfixo_hospedi, rua_hospedi, bairro_hospedi, "
                + "complemento_hospedi, cidade_hospedi, cep_hospedi, "
                + "numero_casa_hospedi, estado_hospedi, "
                + "observacoes_hospedi, tipo_hospedi "
                + "FROM hospedi "
                + "WHERE id_hospedi = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();
                PreparedStatement pst =
                        conn.prepareStatement(sql)) {

            pst.setInt(1, idHospede);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                nomeHosp.setText(
                        rs.getString("nome_hospedi"));

                sobreNomeHosp.setText(
                        rs.getString("sobrenome_hospedi"));

                Date data = rs.getDate("datanasc_hospedi");

                if (data != null) {
                    SimpleDateFormat formato =
                            new SimpleDateFormat("dd/MM/yyyy");

                    dataNascHosp.setText(
                            formato.format(data));
                }

                cpfHosp.setText(
                        rs.getString("cpf_hospedi"));

                rgHosp.setText(
                        rs.getString("rg_hospedi"));

                sexoHosp.setText(
                        rs.getString("sexo_hospedi"));

                emailHosp.setText(
                        rs.getString("email_hospedi"));

                celularHosp.setText(
                        rs.getString("cell_hospedi"));

                telFixoHosp.setText(
                        rs.getString("tellfixo_hospedi"));

                ruaHosp.setText(
                        rs.getString("rua_hospedi"));

                bairroHosp.setText(
                        rs.getString("bairro_hospedi"));

                complementoCasaHosp.setText(
                        rs.getString("complemento_hospedi"));

                cidadeHosp.setText(
                        rs.getString("cidade_hospedi"));

                cepHosp.setText(
                        rs.getString("cep_hospedi"));

                numCasaHosp.setText(
                        rs.getString("numero_casa_hospedi"));

                estadoHosp.setText(
                        rs.getString("estado_hospedi"));

                obsHosp.setText(
                        rs.getString("observacoes_hospedi"));

                String tipo =
                        rs.getString("tipo_hospedi");

                if ("Comum".equalsIgnoreCase(tipo)) {
                    comumRadio.setSelected(true);

                } else if ("VIP".equalsIgnoreCase(tipo)) {
                    vipRadio.setSelected(true);

                } else if ("Empresário".equalsIgnoreCase(tipo)
                        || "Empresario".equalsIgnoreCase(tipo)) {

                    empresarioRadio.setSelected(true);
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar hóspede:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void atualizarHospede() {

        String tipoHospede = null;

        if (comumRadio.isSelected()) {
            tipoHospede = "Comum";

        } else if (vipRadio.isSelected()) {
            tipoHospede = "VIP";

        } else if (empresarioRadio.isSelected()) {
            tipoHospede = "Empresário";
        }

        if (tipoHospede == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione o tipo do hóspede.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql =
                "UPDATE hospedi SET "
                + "nome_hospedi = ?, "
                + "sobrenome_hospedi = ?, "
                + "datanasc_hospedi = ?, "
                + "cpf_hospedi = ?, "
                + "rg_hospedi = ?, "
                + "sexo_hospedi = ?, "
                + "email_hospedi = ?, "
                + "cell_hospedi = ?, "
                + "tellfixo_hospedi = ?, "
                + "rua_hospedi = ?, "
                + "bairro_hospedi = ?, "
                + "complemento_hospedi = ?, "
                + "cidade_hospedi = ?, "
                + "cep_hospedi = ?, "
                + "numero_casa_hospedi = ?, "
                + "estado_hospedi = ?, "
                + "observacoes_hospedi = ?, "
                + "tipo_hospedi = ? "
                + "WHERE id_hospedi = ?";

        try (
                Connection conn =
                        conexao.ConectionFactory.conector();
                PreparedStatement pst =
                        conn.prepareStatement(sql)) {

            pst.setString(1, nomeHosp.getText().trim());

            pst.setString(2,
                    sobreNomeHosp.getText().trim());

            String dataTexto =
                    dataNascHosp.getText().trim();

            if (!dataTexto.isEmpty()) {

                try {

                    SimpleDateFormat formato =
                            new SimpleDateFormat("dd/MM/yyyy");

                    formato.setLenient(false);

                    Date data =
                            formato.parse(dataTexto);

                    pst.setDate(
                            3,
                            new java.sql.Date(
                                    data.getTime()));

                } catch (ParseException e) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Data de nascimento inválida.\n"
                            + "Use o formato dd/MM/yyyy.",
                            "Atenção",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

            } else {

                pst.setNull(
                        3,
                        java.sql.Types.DATE);
            }

            pst.setString(4,
                    cpfHosp.getText().trim());

            if (rgHosp.getText().trim().isEmpty()) {
                pst.setNull(
                        5,
                        java.sql.Types.VARCHAR);
            } else {
                pst.setString(
                        5,
                        rgHosp.getText().trim());
            }

            pst.setString(6,
                    sexoHosp.getText().trim());

            pst.setString(7,
                    emailHosp.getText().trim());

            pst.setString(8,
                    celularHosp.getText().trim());

            if (telFixoHosp.getText().trim().isEmpty()) {
                pst.setNull(
                        9,
                        java.sql.Types.VARCHAR);
            } else {
                pst.setString(
                        9,
                        telFixoHosp.getText().trim());
            }

            pst.setString(10,
                    ruaHosp.getText().trim());

            pst.setString(11,
                    bairroHosp.getText().trim());

            if (complementoCasaHosp.getText().trim().isEmpty()) {
                pst.setNull(
                        12,
                        java.sql.Types.VARCHAR);
            } else {
                pst.setString(
                        12,
                        complementoCasaHosp.getText().trim());
            }

            pst.setString(13,
                    cidadeHosp.getText().trim());

            pst.setString(14,
                    cepHosp.getText().trim());

            pst.setString(15,
                    numCasaHosp.getText().trim());

            pst.setString(16,
                    estadoHosp.getText().trim());

            if (obsHosp.getText().trim().isEmpty()) {
                pst.setNull(
                        17,
                        java.sql.Types.VARCHAR);
            } else {
                pst.setString(
                        17,
                        obsHosp.getText().trim());
            }

            pst.setString(18,
                    tipoHospede);

            pst.setInt(19,
                    idHospede);

            int resultado =
                    pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Hóspede atualizado com sucesso!",
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
                    "Erro ao atualizar hóspede:\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}