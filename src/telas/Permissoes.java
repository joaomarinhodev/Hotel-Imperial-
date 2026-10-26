package telas;

public class Permissoes {

    public static boolean temPermissao(String permissao) {

        String grau = SessaoUsuario.getGrauAcesso();

        if (grau == null) {
            return false;
        }

        // =========================
        // ADMIN
        // =========================
        if (grau.equalsIgnoreCase("Admin")) {
            return true;
        }

        // =========================
        // GERENTE
        // =========================
        if (grau.equalsIgnoreCase("Gerente")) {

            switch (permissao) {

                // HÓSPEDES
                case "HOSPEDE_CONSULTAR":
                case "HOSPEDE_CADASTRAR":
                case "HOSPEDE_EDITAR":

                // QUARTOS
                case "QUARTO_CONSULTAR":

                // FORNECEDORES
                case "FORNECEDOR_CONSULTAR":
                    
                //FUNCIONÁRIOS
                

                // RESERVAS
                case "RESERVA_CONSULTAR":
                case "RESERVA_CADASTRAR":
                case "RESERVA_EDITAR":

                // VENDAS
                case "VENDA_CONSULTAR":
                case "VENDA_CADASTRAR":
                case "VENDA_EDITAR":

                // RELATÓRIOS
                case "RELATORIO_CONSULTAR":

                    return true;

                default:
                    return false;
            }
        }

        // =========================
        // CONSULTOR / RECEPÇÃO
        // =========================
        if (grau.equalsIgnoreCase("Consultor")) {

            switch (permissao) {

                // HÓSPEDES
                case "HOSPEDE_CONSULTAR":
                case "HOSPEDE_CADASTRAR":
                case "HOSPEDE_EDITAR":

                // QUARTOS
                case "QUARTO_CONSULTAR":

                // RESERVAS
                case "RESERVA_CONSULTAR":
                case "RESERVA_CADASTRAR":
                case "RESERVA_EDITAR":

                // VENDAS
                case "VENDA_CONSULTAR":
                case "VENDA_CADASTRAR":

                    return true;

                default:
                    return false;
            }
        }

        return false;
    }
}