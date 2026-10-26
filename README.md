# 🏨 Hotel Imperial

Sistema de gerenciamento hoteleiro desenvolvido em Java, com interface gráfica Swing e banco de dados PostgreSQL.

## 📌 Sobre o projeto

O **Hotel Imperial** é um projeto de software voltado ao gerenciamento de atividades de um hotel. A aplicação reúne telas para cadastros, consultas, reservas e relatórios, com o objetivo de organizar as informações e facilitar as rotinas administrativas.

> **Observação:** este README descreve a estrutura e os recursos identificados no projeto. Ajuste a lista de funcionalidades conforme cada módulo estiver concluído e testado.

## ✨ Funcionalidades

A estrutura atual do projeto inclui módulos para:

- Cadastro e consulta de hóspedes.
- Cadastro e consulta de funcionários.
- Cadastro e consulta de fornecedores.
- Cadastro e consulta de quartos.
- Criação, consulta e finalização de reservas.
- Check-in.
- Cadastro e consulta de produtos.
- Cadastro e consulta de serviços.
- Relatórios de hóspedes, funcionários, fornecedores, produtos e serviços.
- Relatórios e cadastros de contas a pagar e contas a receber.
- Controle de acesso por perfil de usuário.
- Ferramentas de backup e personalização da tela principal, conforme implementadas no código.

## 🧰 Tecnologias utilizadas

- **Java** — linguagem de programação.
- **Java Swing** — construção da interface gráfica.
- **Apache NetBeans** — ambiente de desenvolvimento do projeto.
- **PostgreSQL** — banco de dados relacional.
- **JDBC** — conexão entre a aplicação Java e o banco de dados.

## 📁 Organização do projeto

```text
Hotel---Imperial-main/
├── src/
│   ├── conexao/          # Conexão com o banco de dados
│   ├── imagens/          # Imagens e ícones da interface
│   ├── relatorios/       # Relatórios e telas financeiras
│   ├── reservas/         # Fluxos de reservas e check-in
│   ├── telas/            # Telas principais e cadastros
│   └── vendasProdutos/   # Produtos e serviços
├── nbproject/            # Configurações do projeto NetBeans
├── build.xml             # Configuração de compilação Ant
├── manifest.mf           # Manifesto do aplicativo
└── README.md             # Documentação do projeto
```

## ⚙️ Pré-requisitos

Para abrir e executar o projeto, você precisará de:

- Java JDK compatível com a versão configurada no projeto.
- Apache NetBeans com suporte a projetos Java/Ant.
- PostgreSQL instalado e em execução.
- Banco de dados e tabelas criados conforme o esquema utilizado pela aplicação.

## 🚀 Como executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU-USUARIO/Hotel-Imperial.git
   ```
2. Abra o Apache NetBeans.
3. Selecione **File > Open Project** e escolha a pasta clonada.
4. Confira a configuração de conexão com o PostgreSQL em `src/conexao/ConectionFactory.java`.
5. Verifique se o banco de dados e as tabelas necessárias estão criados.
6. Compile e execute o projeto pelo NetBeans.

### 🗄️ Configuração do banco de dados

A classe `conexao.ConectionFactory` contém os parâmetros de conexão utilizados pela aplicação. Confira o endereço do servidor, a porta, o nome do banco, o usuário e a senha para corresponderem ao seu ambiente local.

**Segurança:** não publique senhas reais nem outras credenciais no repositório. Antes de tornar o projeto público, revise os arquivos de configuração e substitua credenciais fixas por uma configuração local segura.

## 👥 Perfis de acesso

O projeto possui lógica de acesso para os seguintes perfis:

- **Admin**
- **Gerente**
- **Consultor**

As permissões disponíveis dependem das regras implementadas no sistema.

## 🖼️ Capturas de tela

Adicione imagens das telas do sistema nesta seção para ajudar visitantes a conhecer a interface. Você pode criar uma pasta `docs/screenshots/` e colocar nela capturas de tela sem dados pessoais ou informações confidenciais.

Exemplo, depois de adicionar uma captura chamada `tela-principal.png`:

```markdown
![Tela principal do Hotel Imperial](docs/screenshots/tela-principal.png)
```

## 🔐 Backup e restauração

O projeto inclui uma implementação de backup e restauração do banco de dados. Para utilizar esses recursos, o PostgreSQL e suas ferramentas de linha de comando, como `pg_dump` e `pg_restore`, precisam estar instalados e configurados no computador.

Antes de restaurar um backup, confirme que o arquivo é válido e que existe uma cópia de segurança recente do banco atual.

## 📚 Objetivo acadêmico

Este repositório documenta o desenvolvimento de um sistema de gerenciamento hoteleiro em Java. A documentação e a lista de funcionalidades devem ser atualizadas conforme o projeto evoluir.

## 📝 Status do projeto

Em desenvolvimento. Recursos e instruções podem mudar à medida que novos módulos forem implementados e testados.

---

Desenvolvido como projeto **Hotel Imperial**.
