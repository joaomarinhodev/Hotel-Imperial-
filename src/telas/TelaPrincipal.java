/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import vendasProdutos.CadastroProduto;
import vendasProdutos.CadastroServico;
import vendasProdutos.ConsultarProduto;
import vendasProdutos.ConsultarServico;

import relatorios.RelatorioProdutos;
import relatorios.RelatorioServicos;
import relatorios.RelatorioHospedes;
import relatorios.RelatorioFuncionarios;
import relatorios.RelatorioFornecedores;
import relatorios.CadastroContasPagar;
import relatorios.RelatorioContasPagar;
import relatorios.CadastroContasReceber;

import reservas.ConsultarReserva;
import reservas.CriarReserva;
import reservas.FinalizarReserva;


/**
 *
 * @author User
 */
public class TelaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        iniciarRelogio();
        carregarDadosUsuario();
        aplicarPermissoes();
        
        // Troque jMenuItemXX pelo nome real do item "Sobre o sistema"
        jMenuItem21.addActionListener(e -> mostrarSobreSistema());
        
        jMenuItem22.addActionListener(e -> adicionarImagemFundo());
        jMenuItem23.addActionListener(e -> removerImagemFundo());
        jMenuItem24.addActionListener(e -> restaurarImagemPadrao());

        carregarImagemFundoSalva();
        jMenuItem19.addActionListener(e -> fazerBackup());
        jMenuItem20.addActionListener(e -> restaurarBackup());
    }
    
    
    
private boolean usuarioAdmin() {
    if (SessaoUsuario.isAdmin()) {
        return true;
    }

    JOptionPane.showMessageDialog(this,
            "Somente o administrador pode acessar esta função.",
            "Acesso negado",
            JOptionPane.WARNING_MESSAGE);
    return false;
}

private File escolherExecutavel(String nome) {
    JFileChooser seletor = new JFileChooser();
    seletor.setDialogTitle("Selecione o executável " + nome);

    if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
        return null;
    }

    File arquivo = seletor.getSelectedFile();

    if (!arquivo.isFile()) {
        JOptionPane.showMessageDialog(this,
                "O arquivo selecionado não existe.",
                "Arquivo inválido",
                JOptionPane.WARNING_MESSAGE);
        return null;
    }

    return arquivo;
}

    private void fazerBackup() {
        if (!usuarioAdmin()) {
            return;
        }

        File pgDump = escolherExecutavel("pg_dump.exe");
        if (pgDump == null) {
            return;
        }

        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Salvar backup do Hotel Imperial");
        seletor.setSelectedFile(new File("HotelImperial.backup"));
        seletor.setFileFilter(new FileNameExtensionFilter(
                "Backup PostgreSQL (*.backup)", "backup"));

        if (seletor.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File destino = seletor.getSelectedFile();

        if (!destino.getName().toLowerCase().endsWith(".backup")) {
            destino = new File(destino.getAbsolutePath() + ".backup");
        }

        if (destino.exists()) {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "O arquivo já existe. Deseja substituí-lo?",
                    "Confirmar substituição",
                    JOptionPane.YES_NO_OPTION);

            if (resposta != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    pgDump.getAbsolutePath(),
                    "-h", "localhost",
                    "-p", "5432",
                    "-U", "postgres",
                    "-F", "c",
                    "-b",
                    "-v",
                    "-f", destino.getAbsolutePath(),
                    "hotelImperial");

            Map<String, String> ambiente = pb.environment();
            ambiente.put("PGPASSWORD", "postgres");
            pb.redirectErrorStream(true);

            Process processo = pb.start();
            StringBuilder saida = new StringBuilder();

            try (BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(processo.getInputStream()))) {
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    saida.append(linha).append("\n");
                }
            }

            int codigo = processo.waitFor();

            if (codigo == 0 && destino.isFile() && destino.length() > 0) {
                JOptionPane.showMessageDialog(this,
                        "Backup criado com sucesso!\n\n"
                        + destino.getAbsolutePath(),
                        "Backup concluído",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível concluir o backup.\n\n"
                        + saida,
                        "Erro no backup",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao executar o pg_dump.\n"
                    + "Confira se selecionou o pg_dump.exe correto.\n\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(this,
                    "A operação de backup foi interrompida.",
                    "Operação interrompida",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void restaurarBackup() {
        if (!usuarioAdmin()) {
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "ATENÇÃO!\n\n"
                + "A restauração pode substituir objetos e dados "
                + "existentes no banco hotelImperial.\n\n"
                + "Faça um backup atual antes de continuar.\n\n"
                + "Deseja prosseguir?",
                "Confirmar restauração",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        JFileChooser seletorBackup = new JFileChooser();
        seletorBackup.setDialogTitle("Selecione o arquivo de backup");
        seletorBackup.setFileFilter(new FileNameExtensionFilter(
                "Backup PostgreSQL (*.backup)", "backup"));

        if (seletorBackup.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File backup = seletorBackup.getSelectedFile();

        if (!backup.isFile() || backup.length() == 0) {
            JOptionPane.showMessageDialog(this,
                    "O arquivo selecionado não existe ou está vazio.",
                    "Arquivo inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        File pgRestore = escolherExecutavel("pg_restore.exe");
        if (pgRestore == null) {
            return;
        }

        int confirmarFinal = JOptionPane.showConfirmDialog(this,
                "Confirma a restauração do arquivo?\n\n"
                + backup.getAbsolutePath()
                + "\n\nOs dados existentes podem ser substituídos.",
                "Confirmação final",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmarFinal != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    pgRestore.getAbsolutePath(),
                    "-h", "localhost",
                    "-p", "5432",
                    "-U", "postgres",
                    "-d", "hotelImperial",
                    "--clean",
                    "--if-exists",
                    "--no-owner",
                    backup.getAbsolutePath());

            Map<String, String> ambiente = pb.environment();
            ambiente.put("PGPASSWORD", "postgres");
            pb.redirectErrorStream(true);

            Process processo = pb.start();
            StringBuilder saida = new StringBuilder();

            try (BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(processo.getInputStream()))) {
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    saida.append(linha).append("\n");
                }
            }

            int codigo = processo.waitFor();

            if (codigo == 0) {
                JOptionPane.showMessageDialog(this,
                        "Restauração concluída.\n"
                        + "Recomenda-se conferir os dados do sistema.",
                        "Restauração concluída",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "A restauração terminou com avisos ou erros.\n\n"
                        + saida,
                        "Verificar restauração",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao executar o pg_restore.\n"
                    + "Confira se selecionou o executável correto.\n\n"
                    + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(this,
                    "A restauração foi interrompida.",
                    "Operação interrompida",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarPermissoes() {

        // CONSULTOR: somente consulta
        if (SessaoUsuario.isConsultor()) {

            menuFuncionarios.setVisible(false);
            menuFornecedores.setVisible(false);
            menuQuartoCadastrar.setVisible(false);

            // Produtos
            CadastrarProduto.setVisible(false);
            ConsultarProduto.setVisible(true);

            // Serviços
            CadastrarServicos.setVisible(false);
            ConsultarSevicos.setVisible(true);
        }

        // GERENTE: consulta e edição
        if (SessaoUsuario.isGerente()) {

            menuFuncionarios.setVisible(false);
            menuFornecedores.setVisible(false);
            menuQuartoCadastrar.setVisible(false);

            // Produtos
            CadastrarProduto.setVisible(false);
            ConsultarProduto.setVisible(true);

            // Serviços
            CadastrarServicos.setVisible(false);
            ConsultarSevicos.setVisible(true);
        }

        // ADMIN: acesso completo
        if (SessaoUsuario.isAdmin()) {

            // Produtos
            CadastrarProduto.setVisible(true);
            ConsultarProduto.setVisible(true);

            // Serviços
            CadastrarServicos.setVisible(true);
            ConsultarSevicos.setVisible(true);
        }
    }


    private void carregarDadosUsuario() {

    nomeUsuario.setText(SessaoUsuario.getNomeUsuario());
    cargoUsuario.setText(SessaoUsuario.getCargo());
    grauAcessoUsuario.setText(SessaoUsuario.getGrauAcesso());
}
    
    public void iniciarRelogio(){
    
        Timer timer = new Timer(1000, e -> {
        Date agora = new Date();

        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        String data = formatoData.format(agora);
        String hora = formatoHora.format(agora);

        jRelogio.setText("<html><center>" + data + "<br>" + hora + "</center></html>");

    });

    timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        grauAcessoUsuario = new javax.swing.JLabel();
        cargoUsuario = new javax.swing.JLabel();
        nomeUsuario = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jRelogio = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        telaFundo = new javax.swing.JDesktopPane();
        jLabel10 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuHospedes = new javax.swing.JMenu();
        menuHospedeCadastrar = new javax.swing.JMenuItem();
        menuHospedeConsultar = new javax.swing.JMenuItem();
        menuQuartos = new javax.swing.JMenu();
        menuQuartoCadastrar = new javax.swing.JMenuItem();
        menuQuartoConsultar = new javax.swing.JMenuItem();
        menuFuncionarios = new javax.swing.JMenu();
        menuFuncionarioCadastrar = new javax.swing.JMenuItem();
        menuFuncionarioConsultar = new javax.swing.JMenuItem();
        menuFornecedores = new javax.swing.JMenu();
        menuFornecedorCadastrar = new javax.swing.JMenuItem();
        menuFornecedorConsultar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        CadastrarProduto = new javax.swing.JMenuItem();
        ConsultarProduto = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        CadastrarServicos = new javax.swing.JMenuItem();
        ConsultarSevicos = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        RelatorioContasPagar = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        RelatorioProduto = new javax.swing.JMenuItem();
        RelatorioServico = new javax.swing.JMenuItem();
        RelatorioHosp = new javax.swing.JMenuItem();
        RelatorioFunc = new javax.swing.JMenuItem();
        RelatorioFornecedores = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu16 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        bttSair = new javax.swing.JMenu();
        sairMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("@ Sistema Hotel Royal - Desenvolvido por João Eduardo / Nathan Alves / Vitor Eduardo - Direitos Autorais Reservados 2026");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(763, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(764, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/image-removebg-preview.png"))); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("Usuário:");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setText("Cargo:");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("Grau de acesso:");

        grauAcessoUsuario.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        grauAcessoUsuario.setText("Nome");

        cargoUsuario.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cargoUsuario.setText("Nome");

        nomeUsuario.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        nomeUsuario.setText("Nome");

        jRelogio.setFont(new java.awt.Font("Dialog", 1, 28)); // NOI18N
        jRelogio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRelogio.setText("");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-pessoa-20.png"))); // NOI18N
        jButton1.setText("Logoff");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator3)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cargoUsuario)))
                        .addGap(18, 18, 18)
                        .addComponent(grauAcessoUsuario)
                        .addGap(119, 119, 119))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(162, 162, 162))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(nomeUsuario)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRelogio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(nomeUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cargoUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(grauAcessoUsuario))
                .addGap(19, 19, 19)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRelogio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/nordseher-hotel-6862159_1920.jpg"))); // NOI18N

        telaFundo.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout telaFundoLayout = new javax.swing.GroupLayout(telaFundo);
        telaFundo.setLayout(telaFundoLayout);
        telaFundoLayout.setHorizontalGroup(
            telaFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(telaFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(telaFundoLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        telaFundoLayout.setVerticalGroup(
            telaFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(telaFundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(telaFundoLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-caderno-32.png"))); // NOI18N
        jMenu1.setText("Cadastro   |");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        menuHospedes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-pessoa-20.png"))); // NOI18N
        menuHospedes.setText("Hóspedes");

        menuHospedeCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-cadastro-20.png"))); // NOI18N
        menuHospedeCadastrar.setText("Cadastrar");
        menuHospedeCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHospedeCadastrarActionPerformed(evt);
            }
        });
        menuHospedes.add(menuHospedeCadastrar);

        menuHospedeConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Pesquisa-20.png"))); // NOI18N
        menuHospedeConsultar.setText("Consultar");
        menuHospedeConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHospedeConsultarActionPerformed(evt);
            }
        });
        menuHospedes.add(menuHospedeConsultar);

        jMenu1.add(menuHospedes);

        menuQuartos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-quartos-20.png"))); // NOI18N
        menuQuartos.setText("Quartos");

        menuQuartoCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-cadastro-20.png"))); // NOI18N
        menuQuartoCadastrar.setText("Cadastrar");
        menuQuartoCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuQuartoCadastrarActionPerformed(evt);
            }
        });
        menuQuartos.add(menuQuartoCadastrar);

        menuQuartoConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Pesquisa-20.png"))); // NOI18N
        menuQuartoConsultar.setText("Consultar");
        menuQuartoConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuQuartoConsultarActionPerformed(evt);
            }
        });
        menuQuartos.add(menuQuartoConsultar);

        jMenu1.add(menuQuartos);

        menuFuncionarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        menuFuncionarios.setText("Funcionários");
        menuFuncionarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFuncionariosActionPerformed(evt);
            }
        });

        menuFuncionarioCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-cadastro-20.png"))); // NOI18N
        menuFuncionarioCadastrar.setText("Cadastrar");
        menuFuncionarioCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFuncionarioCadastrarActionPerformed(evt);
            }
        });
        menuFuncionarios.add(menuFuncionarioCadastrar);

        menuFuncionarioConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Pesquisa-20.png"))); // NOI18N
        menuFuncionarioConsultar.setText("Consultar");
        menuFuncionarioConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFuncionarioConsultarActionPerformed(evt);
            }
        });
        menuFuncionarios.add(menuFuncionarioConsultar);

        jMenu1.add(menuFuncionarios);

        menuFornecedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-fornecedor-20.png"))); // NOI18N
        menuFornecedores.setText("Fornecedores");

        menuFornecedorCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-cadastro-20.png"))); // NOI18N
        menuFornecedorCadastrar.setText("Cadastrar");
        menuFornecedorCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFornecedorCadastrarActionPerformed(evt);
            }
        });
        menuFornecedores.add(menuFornecedorCadastrar);

        menuFornecedorConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Pesquisa-20.png"))); // NOI18N
        menuFornecedorConsultar.setText("Consultar");
        menuFornecedorConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFornecedorConsultarActionPerformed(evt);
            }
        });
        menuFornecedores.add(menuFornecedorConsultar);

        jMenu1.add(menuFornecedores);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Agendar-32.png"))); // NOI18N
        jMenu2.setText(" Reservas   |");

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-documento-20.png"))); // NOI18N
        jMenuItem9.setText("Criar");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Pesquisa-20.png"))); // NOI18N
        jMenuItem10.setText("Consultar");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Correto-20.png"))); // NOI18N
        jMenuItem11.setText("Check - out");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Correto-20.png"))); // NOI18N
        jMenuItem1.setText("Check - in");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Cifrão-32.png"))); // NOI18N
        jMenu3.setText("Vendas |");

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Produto-20.png"))); // NOI18N
        jMenu4.setText("Produtos");

        CadastrarProduto.setText("Cadastrar");
        CadastrarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CadastrarProdutoActionPerformed(evt);
            }
        });
        jMenu4.add(CadastrarProduto);

        ConsultarProduto.setText("Consultar");
        ConsultarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConsultarProdutoActionPerformed(evt);
            }
        });
        jMenu4.add(ConsultarProduto);

        jMenu3.add(jMenu4);

        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Serviço-20.png"))); // NOI18N
        jMenu8.setText("Serviços");

        CadastrarServicos.setText("Cadastrar");
        CadastrarServicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CadastrarServicosActionPerformed(evt);
            }
        });
        jMenu8.add(CadastrarServicos);

        ConsultarSevicos.setText("Consultar");
        ConsultarSevicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConsultarSevicosActionPerformed(evt);
            }
        });
        jMenu8.add(ConsultarSevicos);

        jMenu3.add(jMenu8);

        jMenuBar1.add(jMenu3);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Fatiar-32.png"))); // NOI18N
        jMenu5.setText(" Relatórios   |");

        jMenu13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Financeiro-20.png"))); // NOI18N
        jMenu13.setText("Financeiro");

        RelatorioContasPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-gráfico-20 (2).png"))); // NOI18N
        RelatorioContasPagar.setText("Contas a pagar");
        RelatorioContasPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelatorioContasPagarActionPerformed(evt);
            }
        });
        jMenu13.add(RelatorioContasPagar);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-estatísticas-20.png"))); // NOI18N
        jMenuItem15.setText("Contas a receber");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem15);

        jMenu5.add(jMenu13);

        jMenu15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-cifrão-20.png"))); // NOI18N
        jMenu15.setText("Vendas");

        RelatorioProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Produto-20.png"))); // NOI18N
        RelatorioProduto.setText("Produtos");
        RelatorioProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelatorioProdutoActionPerformed(evt);
            }
        });
        jMenu15.add(RelatorioProduto);

        RelatorioServico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Serviço-20.png"))); // NOI18N
        RelatorioServico.setText("Serviços");
        RelatorioServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelatorioServicoActionPerformed(evt);
            }
        });
        jMenu15.add(RelatorioServico);

        jMenu5.add(jMenu15);

        RelatorioHosp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-pessoa-20.png"))); // NOI18N
        RelatorioHosp.setText("Hóspedes");
        RelatorioHosp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelatorioHospActionPerformed(evt);
            }
        });
        jMenu5.add(RelatorioHosp);

        RelatorioFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-construtor-20.png"))); // NOI18N
        RelatorioFunc.setText("Funcionários");
        RelatorioFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelatorioFuncActionPerformed(evt);
            }
        });
        jMenu5.add(RelatorioFunc);

        RelatorioFornecedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-fornecedor-20.png"))); // NOI18N
        RelatorioFornecedores.setText("Fornecedores");
        RelatorioFornecedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelatorioFornecedoresActionPerformed(evt);
            }
        });
        jMenu5.add(RelatorioFornecedores);

        jMenuBar1.add(jMenu5);

        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-cadeado-32.png"))); // NOI18N
        jMenu6.setText(" Segurança   |");

        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-baixar-atualizações-20 (1).png"))); // NOI18N
        jMenuItem19.setText("Fazer Backup");
        jMenu6.add(jMenuItem19);

        jMenuItem20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-baixar-atualizações-20.png"))); // NOI18N
        jMenuItem20.setText("Restaurar Backup");
        jMenu6.add(jMenuItem20);

        jMenuBar1.add(jMenu6);

        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-engrenagem-32.png"))); // NOI18N
        jMenu7.setText(" Ferramentas   |");

        jMenu16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-paisagem-20.png"))); // NOI18N
        jMenu16.setText("Tela de Fundo");

        jMenuItem22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-paisagem-20 (1).png"))); // NOI18N
        jMenuItem22.setText("Add Imagem");
        jMenu16.add(jMenuItem22);

        jMenuItem23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-paisagem-20 (2).png"))); // NOI18N
        jMenuItem23.setText("Remover Imagem");
        jMenu16.add(jMenuItem23);

        jMenuItem24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-paisagem-20.png"))); // NOI18N
        jMenuItem24.setText("Restaurar padrão");
        jMenu16.add(jMenuItem24);

        jMenu7.add(jMenu16);

        jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-informações-20.png"))); // NOI18N
        jMenuItem21.setText("Sobre o sistema");
        jMenu7.add(jMenuItem21);

        jMenuBar1.add(jMenu7);

        bttSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/icons8-saída-32.png"))); // NOI18N
        bttSair.setText(" Sair");
        bttSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttSairActionPerformed(evt);
            }
        });

        sairMenu.setText("Sair");
        sairMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairMenuActionPerformed(evt);
            }
        });
        bttSair.add(sairMenu);

        jMenuBar1.add(bttSair);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(telaFundo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(telaFundo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuHospedeCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHospedeCadastrarActionPerformed
        CadastroHospede telaCadHosp = new CadastroHospede();
            telaFundo.add(telaCadHosp);
            telaCadHosp.setVisible(true);
    }//GEN-LAST:event_menuHospedeCadastrarActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        reservas.ConsultarReserva tela = new ConsultarReserva();
            telaFundo.add(tela);

            try {
                tela.setMaximum(true);
            } catch (java.beans.PropertyVetoException e) {
                // Continua aberta mesmo se não maximizar.
            }

            tela.setVisible(true);
            tela.toFront();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void menuFuncionarioCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFuncionarioCadastrarActionPerformed
        CadastroFunc telaCadFunc = new CadastroFunc();
            telaFundo.add(telaCadFunc);
            telaCadFunc.setVisible(true);
    
    }//GEN-LAST:event_menuFuncionarioCadastrarActionPerformed

    private void menuQuartoCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuQuartoCadastrarActionPerformed
        CadastroQuarto telaCadQuar = new CadastroQuarto();
            telaFundo.add(telaCadQuar);
            telaCadQuar.setVisible(true);
    }//GEN-LAST:event_menuQuartoCadastrarActionPerformed

    private void menuFornecedorCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFornecedorCadastrarActionPerformed
        CadastroFornecedor telaCadForn = new CadastroFornecedor();
            telaFundo.add(telaCadForn);
            telaCadForn.setVisible(true);
    }//GEN-LAST:event_menuFornecedorCadastrarActionPerformed

    private void bttSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttSairActionPerformed
        
    }//GEN-LAST:event_bttSairActionPerformed

    private void sairMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sairMenuActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente sair do sistema?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);
        if(resposta == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_sairMenuActionPerformed

    private void menuFuncionarioConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFuncionarioConsultarActionPerformed
        ConsultarFunc TelaConsFunc = new ConsultarFunc();
            telaFundo.add(TelaConsFunc);
            TelaConsFunc.setVisible(true);
    }//GEN-LAST:event_menuFuncionarioConsultarActionPerformed

    private void menuFornecedorConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFornecedorConsultarActionPerformed
        ConsultarFornecedor telaConsForn = new ConsultarFornecedor();
        telaFundo.add(telaConsForn);
        telaConsForn.setVisible(true);
        try {
            // Faz a janela abrir maximizada dentro do JDesktopPane
            telaConsForn.setMaximum(true); 
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
}
    }//GEN-LAST:event_menuFornecedorConsultarActionPerformed

    private void menuHospedeConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHospedeConsultarActionPerformed
        ConsultarHospede TelaConsHosp = new ConsultarHospede();
            telaFundo.add(TelaConsHosp);
            TelaConsHosp.setVisible(true);
    }//GEN-LAST:event_menuHospedeConsultarActionPerformed

    private void menuQuartoConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuQuartoConsultarActionPerformed
        ConsultarQuarto telaConsQua = new ConsultarQuarto();
            telaFundo.add(telaConsQua);
            telaConsQua.setVisible(true);
    }//GEN-LAST:event_menuQuartoConsultarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair da sua conta?",
            "Confirmar Logoff",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
    );

    if (resposta == JOptionPane.YES_OPTION) {

        // Encerra a sessão atual
        SessaoUsuario.encerrarSessao();

        // Fecha a TelaPrincipal
        this.dispose();

        // Abre novamente a tela de login
        LoginTela login = new LoginTela();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void menuFuncionariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFuncionariosActionPerformed
        
    }//GEN-LAST:event_menuFuncionariosActionPerformed

    private void CadastrarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CadastrarProdutoActionPerformed
        vendasProdutos.CadastroProduto cadProd = new CadastroProduto();
            telaFundo.add(cadProd);
            cadProd.setVisible(true);
    }//GEN-LAST:event_CadastrarProdutoActionPerformed

    private void ConsultarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConsultarProdutoActionPerformed
        vendasProdutos.ConsultarProduto consProd = new ConsultarProduto();
            telaFundo.add(consProd);
            consProd.setVisible(true);
    }//GEN-LAST:event_ConsultarProdutoActionPerformed

    private void CadastrarServicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CadastrarServicosActionPerformed
        vendasProdutos.CadastroServico CadServicos = new CadastroServico();
            telaFundo.add(CadServicos);
            CadServicos.setVisible(true);
    }//GEN-LAST:event_CadastrarServicosActionPerformed

    private void ConsultarSevicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConsultarSevicosActionPerformed
        vendasProdutos.ConsultarServico ConsServicos = new ConsultarServico();
            telaFundo.add(ConsServicos);
            ConsServicos.setVisible(true);
    }//GEN-LAST:event_ConsultarSevicosActionPerformed

    private void RelatorioProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelatorioProdutoActionPerformed

    RelatorioProdutos relatorio = new RelatorioProdutos();

    telaFundo.add(relatorio);

    relatorio.setVisible(true);

    try {
        relatorio.setMaximum(true);
    } catch (java.beans.PropertyVetoException e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_RelatorioProdutoActionPerformed

    private void RelatorioServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelatorioServicoActionPerformed
        RelatorioServicos relatorio =
        new RelatorioServicos();

    telaFundo.add(relatorio);

    relatorio.setVisible(true);

    try {
        relatorio.setMaximum(true);
    } catch (java.beans.PropertyVetoException e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_RelatorioServicoActionPerformed

    private void RelatorioHospActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelatorioHospActionPerformed
         RelatorioHospedes relatorio =
        new RelatorioHospedes();

    telaFundo.add(relatorio);

    relatorio.setVisible(true);

    try {
        relatorio.setMaximum(true);
    } catch (java.beans.PropertyVetoException e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_RelatorioHospActionPerformed

    private void RelatorioFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelatorioFuncActionPerformed
        RelatorioFuncionarios relatorio =
        new RelatorioFuncionarios();

    telaFundo.add(relatorio);

    relatorio.setVisible(true);

    try {
        relatorio.setMaximum(true);
    } catch (java.beans.PropertyVetoException e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_RelatorioFuncActionPerformed

    private void RelatorioFornecedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelatorioFornecedoresActionPerformed
        RelatorioFornecedores relatorio =
        new RelatorioFornecedores();

    telaFundo.add(relatorio);

    relatorio.setVisible(true);

    try {
        relatorio.setMaximum(true);
    } catch (java.beans.PropertyVetoException e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_RelatorioFornecedoresActionPerformed

    private void RelatorioContasPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelatorioContasPagarActionPerformed
    RelatorioContasPagar tela = new RelatorioContasPagar();

    telaFundo.add(tela);
    tela.setVisible(true);

    try {
        tela.setMaximum(true);
    } catch (java.beans.PropertyVetoException e) {
        e.printStackTrace();
    }

    }//GEN-LAST:event_RelatorioContasPagarActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        CadastroContasReceber tela = new CadastroContasReceber();
            telaFundo.add(tela);
            tela.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        reservas.CriarReserva tela = new CriarReserva();
            telaFundo.add(tela);

            try {
                tela.setMaximum(true);
            } catch (java.beans.PropertyVetoException e) {
                // A tela continua disponível mesmo se não maximizar.
            }

            tela.setVisible(true);
            tela.toFront();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        reservas.FinalizarReserva tela = new FinalizarReserva();
            telaFundo.add(tela);

            try {
                tela.setMaximum(true);
            } catch (java.beans.PropertyVetoException e) {
                // Continua aberta mesmo se não maximizar.
            }

            tela.setVisible(true);
            tela.toFront();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        reservas.RealizarCheckin tela = new reservas.RealizarCheckin();

            telaFundo.add(tela);
            tela.setVisible(true);

            try {
                tela.setMaximum(true);
            } catch (java.beans.PropertyVetoException e) {
                // Ignora
            }

            tela.toFront();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CadastrarProduto;
    private javax.swing.JMenuItem CadastrarServicos;
    private javax.swing.JMenuItem ConsultarProduto;
    private javax.swing.JMenuItem ConsultarSevicos;
    private javax.swing.JMenuItem RelatorioContasPagar;
    private javax.swing.JMenuItem RelatorioFornecedores;
    private javax.swing.JMenuItem RelatorioFunc;
    private javax.swing.JMenuItem RelatorioHosp;
    private javax.swing.JMenuItem RelatorioProduto;
    private javax.swing.JMenuItem RelatorioServico;
    private javax.swing.JMenu bttSair;
    private javax.swing.JLabel cargoUsuario;
    private javax.swing.JLabel grauAcessoUsuario;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jRelogio;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem menuFornecedorCadastrar;
    private javax.swing.JMenuItem menuFornecedorConsultar;
    private javax.swing.JMenu menuFornecedores;
    private javax.swing.JMenuItem menuFuncionarioCadastrar;
    private javax.swing.JMenuItem menuFuncionarioConsultar;
    private javax.swing.JMenu menuFuncionarios;
    private javax.swing.JMenuItem menuHospedeCadastrar;
    private javax.swing.JMenuItem menuHospedeConsultar;
    private javax.swing.JMenu menuHospedes;
    private javax.swing.JMenuItem menuQuartoCadastrar;
    private javax.swing.JMenuItem menuQuartoConsultar;
    private javax.swing.JMenu menuQuartos;
    private javax.swing.JLabel nomeUsuario;
    private javax.swing.JMenuItem sairMenu;
    private javax.swing.JDesktopPane telaFundo;
    // End of variables declaration//GEN-END:variables

    
    private static final String CHAVE_IMAGEM_FUNDO = "imagemFundoHotel";

    private void adicionarImagemFundo() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Escolher imagem de fundo");

        seletor.setFileFilter(new FileNameExtensionFilter(
                "Imagens (JPG, PNG, JPEG)",
                "jpg", "jpeg", "png"
        ));

        int resultado = seletor.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = seletor.getSelectedFile();

            try {
                ImageIcon icone = new ImageIcon(arquivo.getAbsolutePath());

                if (icone.getIconWidth() <= 0 || icone.getIconHeight() <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Não foi possível carregar essa imagem.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                java.util.prefs.Preferences preferencias =
                        java.util.prefs.Preferences.userNodeForPackage(
                                TelaPrincipal.class
                        );

                preferencias.put(
                        CHAVE_IMAGEM_FUNDO,
                        arquivo.getAbsolutePath()
                );

                aplicarImagemFundo(arquivo.getAbsolutePath());

                JOptionPane.showMessageDialog(
                        this,
                        "Imagem de fundo alterada com sucesso!"
                );

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erro ao carregar a imagem: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void removerImagemFundo() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja remover a imagem personalizada?",
                "Remover imagem",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            java.util.prefs.Preferences preferencias =
                    java.util.prefs.Preferences.userNodeForPackage(
                            TelaPrincipal.class
                    );

            preferencias.remove(CHAVE_IMAGEM_FUNDO);

            jLabel10.setIcon(null);
            jLabel10.repaint();

            JOptionPane.showMessageDialog(
                    this,
                    "Imagem removida. Para voltar à imagem original, use Restaurar padrão."
            );
        }
    }

    private void restaurarImagemPadrao() {
        java.util.prefs.Preferences preferencias =
                java.util.prefs.Preferences.userNodeForPackage(
                        TelaPrincipal.class
                );

        preferencias.remove(CHAVE_IMAGEM_FUNDO);

        java.net.URL recurso = getClass().getResource(
                "/imagens/nordseher-hotel-6862159_1920.jpg"
        );

        if (recurso == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "A imagem padrão não foi encontrada na pasta imagens.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        jLabel10.setIcon(new ImageIcon(recurso));
        jLabel10.repaint();

        JOptionPane.showMessageDialog(
                this,
                "Imagem padrão restaurada!"
        );
    }

    private void aplicarImagemFundo(String caminho) {
        ImageIcon original = new ImageIcon(caminho);

        int largura = jLabel10.getWidth();
        int altura = jLabel10.getHeight();

        if (largura > 0 && altura > 0) {
            Image imagemRedimensionada = original.getImage().getScaledInstance(
                    largura,
                    altura,
                    Image.SCALE_SMOOTH
            );

            jLabel10.setIcon(new ImageIcon(imagemRedimensionada));
        } else {
            jLabel10.setIcon(original);
        }

        jLabel10.repaint();
    }

    private void carregarImagemFundoSalva() {
        java.util.prefs.Preferences preferencias =
                java.util.prefs.Preferences.userNodeForPackage(
                        TelaPrincipal.class
                );

        String caminho = preferencias.get(CHAVE_IMAGEM_FUNDO, null);

        if (caminho != null && new File(caminho).isFile()) {
            aplicarImagemFundo(caminho);
        }
    }

    
    private void mostrarSobreSistema() {
        String mensagem =
                "HOTEL IMPERIAL\n\n"
                + "Sistema de Gerenciamento Hoteleiro\n"
                + "Versão: 1.0\n\n"
                + "Funcionalidades:\n"
                + "• Cadastro e gerenciamento de dados\n"
                + "• Controle de reservas e check-in\n"
                + "• Vendas e serviços\n"
                + "• Relatórios gerenciais\n\n"
                + "Projeto acadêmico.";

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Sobre o Sistema",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}