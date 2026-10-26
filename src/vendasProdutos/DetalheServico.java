package vendasProdutos;

import conexao.ConectionFactory;
import telas.SessaoUsuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;

public class DetalheServico extends JFrame {

    private int idServico;
    private Runnable aoSalvar;

    private JTextField campoNome;
    private JTextArea campoDescricao;
    private JComboBox<String> campoCategoria;
    private JTextField campoPreco;
    private JCheckBox campoAtivo;

    private JButton salvarButton;
    private JButton cancelarButton;

    public DetalheServico(int idServico) {
        this(idServico, null);
    }

    public DetalheServico(
            int idServico,
            Runnable aoSalvar) {

        this.idServico = idServico;
        this.aoSalvar = aoSalvar;

        criarTela();
        carregarServico();
    }

    private void criarTela() {

        setTitle(
            "Hotel Imperial - Detalhes do Serviço"
        );

        setSize(750, 600);

        setResizable(true);
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

        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel titulo =
            new JLabel("Editar Serviço");

        titulo.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                22
            )
        );

        painelPrincipal.add(
            titulo,
            BorderLayout.NORTH
        );

        // =====================================================
        // FORMULÁRIO
        // =====================================================

        JPanel formulario =
            new JPanel(
                new GridBagLayout()
            );

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets =
            new Insets(6, 6, 6, 6);

        gbc.fill =
            GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;

        // -----------------------------------------------------
        // NOME
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        formulario.add(
            new JLabel("Nome:*"),
            gbc
        );

        campoNome =
            new JTextField();

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        formulario.add(
            campoNome,
            gbc
        );

        // -----------------------------------------------------
        // DESCRIÇÃO
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        formulario.add(
            new JLabel("Descrição:"),
            gbc
        );

        campoDescricao =
            new JTextArea(5, 20);

        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);

        JScrollPane scrollDescricao =
            new JScrollPane(campoDescricao);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill =
            GridBagConstraints.BOTH;

        formulario.add(
            scrollDescricao,
            gbc
        );

        gbc.fill =
            GridBagConstraints.HORIZONTAL;

        // -----------------------------------------------------
        // CATEGORIA
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        formulario.add(
            new JLabel("Categoria:*"),
            gbc
        );

        campoCategoria =
            new JComboBox<>(
                new String[]{
                    "Selecione",
                    "Alimentação",
                    "Limpeza",
                    "Lavanderia",
                    "Transporte",
                    "Conforto",
                    "Outros"
                }
            );

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        formulario.add(
            campoCategoria,
            gbc
        );

        // -----------------------------------------------------
        // PREÇO
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;

        formulario.add(
            new JLabel("Preço:*"),
            gbc
        );

        campoPreco =
            new JTextField();

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        formulario.add(
            campoPreco,
            gbc
        );

        // -----------------------------------------------------
        // ATIVO
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;

        formulario.add(
            new JLabel("Status:"),
            gbc
        );

        campoAtivo =
            new JCheckBox("Serviço ativo");

        gbc.gridx = 1;
        gbc.weightx = 1.0;

        formulario.add(
            campoAtivo,
            gbc
        );

        painelPrincipal.add(
            formulario,
            BorderLayout.CENTER
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        JPanel painelBotoes =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    10,
                    5
                )
            );

        salvarButton =
            new JButton("Salvar");

        cancelarButton =
            new JButton("Cancelar");

        painelBotoes.add(
            salvarButton
        );

        painelBotoes.add(
            cancelarButton
        );

        painelPrincipal.add(
            painelBotoes,
            BorderLayout.SOUTH
        );

        setContentPane(
            painelPrincipal
        );

        // =====================================================
        // EVENTOS
        // =====================================================

        salvarButton.addActionListener(
            e -> salvarServico()
        );

        cancelarButton.addActionListener(
            e -> dispose()
        );
    }

    // =========================================================
    // CARREGAR SERVIÇO
    // =========================================================

    private void carregarServico() {

        String sql =
            "SELECT nome, descricao, categoria, "
          + "preco, ativo "
          + "FROM servicos "
          + "WHERE id_servico = ?";

        try (
            Connection conn =
                ConectionFactory.conector();

            PreparedStatement pst =
                conn.prepareStatement(sql)
        ) {

            pst.setInt(
                1,
                idServico
            );

            ResultSet rs =
                pst.executeQuery();

            if (rs.next()) {

                campoNome.setText(
                    rs.getString("nome")
                );

                campoDescricao.setText(
                    rs.getString("descricao") != null
                        ? rs.getString("descricao")
                        : ""
                );

                campoCategoria.setSelectedItem(
                    rs.getString("categoria")
                );

                campoPreco.setText(
                    formatarPreco(
                        rs.getDouble("preco")
                    )
                );

                campoAtivo.setSelected(
                    rs.getBoolean("ativo")
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Serviço não encontrado.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
                );

                dispose();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao carregar serviço:\n"
                + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );

            dispose();
        }
    }

    // =========================================================
    // SALVAR
    // =========================================================

    private void salvarServico() {

        if (SessaoUsuario.isConsultor()) {

            JOptionPane.showMessageDialog(
                this,
                "Seu perfil não possui permissão para editar serviços.",
                "Acesso negado",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nome =
            campoNome.getText().trim();

        String descricao =
            campoDescricao.getText().trim();

        String categoria =
            campoCategoria
                .getSelectedItem()
                .toString();

        String precoTexto =
            campoPreco.getText().trim();

        // -----------------------------------------------------
        // VALIDAÇÕES
        // -----------------------------------------------------

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Informe o nome do serviço.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            campoNome.requestFocus();
            return;
        }

        if ("Selecione".equals(categoria)) {

            JOptionPane.showMessageDialog(
                this,
                "Selecione uma categoria.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            campoCategoria.requestFocus();
            return;
        }

        if (precoTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Informe o preço do serviço.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            campoPreco.requestFocus();
            return;
        }

        double preco;

        try {

            preco =
                converterNumero(precoTexto);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Informe um preço válido.\n"
                + "Exemplo: 35,00",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            campoPreco.requestFocus();
            return;
        }

        if (preco < 0) {

            JOptionPane.showMessageDialog(
                this,
                "O preço não pode ser negativo.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );

            campoPreco.requestFocus();
            return;
        }

        // =====================================================
        // UPDATE
        // =====================================================

        String sql =
            "UPDATE servicos SET "
          + "nome = ?, "
          + "descricao = ?, "
          + "categoria = ?, "
          + "preco = ?, "
          + "ativo = ? "
          + "WHERE id_servico = ?";

        try (
            Connection conn =
                ConectionFactory.conector();

            PreparedStatement pst =
                conn.prepareStatement(sql)
        ) {

            pst.setString(
                1,
                nome
            );

            if (descricao.isEmpty()) {

                pst.setNull(
                    2,
                    java.sql.Types.VARCHAR
                );

            } else {

                pst.setString(
                    2,
                    descricao
                );
            }

            pst.setString(
                3,
                categoria
            );

            pst.setDouble(
                4,
                preco
            );

            pst.setBoolean(
                5,
                campoAtivo.isSelected()
            );

            pst.setInt(
                6,
                idServico
            );

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Serviço atualizado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
            );

            if (aoSalvar != null) {
                aoSalvar.run();
            }

            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Erro ao atualizar serviço:\n"
                + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CONVERTER PREÇO
    // =========================================================

    private double converterNumero(
        String valor
    ) {

        valor =
            valor.replace(
                "R$",
                ""
            ).trim();

        valor =
            valor.replace(
                ".",
                ""
            );

        valor =
            valor.replace(
                ",",
                "."
            );

        return Double.parseDouble(
            valor
        );
    }

    // =========================================================
    // FORMATAR PREÇO
    // =========================================================

    private String formatarPreco(
        double valor
    ) {

        NumberFormat formato =
            NumberFormat.getNumberInstance(
                new Locale("pt", "BR")
            );

        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);

        return formato.format(valor);
    }
}