package telas;

public class SessaoUsuario {

    private static int idUsuario;
    private static String nomeUsuario;
    private static String cargo;
    private static String grauAcesso;

    public static void iniciarSessao(
            int id,
            String nome,
            String cargoFuncionario,
            String grau) {

        idUsuario = id;
        nomeUsuario = nome;
        cargo = cargoFuncionario;
        grauAcesso = grau;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static String getNomeUsuario() {
        return nomeUsuario;
    }

    public static String getCargo() {
        return cargo;
    }

    public static String getGrauAcesso() {
        return grauAcesso;
    }

    public static boolean isAdmin() {
        return "Admin".equalsIgnoreCase(grauAcesso);
    }

    public static boolean isGerente() {
        return "Gerente".equalsIgnoreCase(grauAcesso);
    }

    public static boolean isConsultor() {
        return "Consultor".equalsIgnoreCase(grauAcesso);
    }

    public static void encerrarSessao() {
        idUsuario = 0;
        nomeUsuario = null;
        cargo = null;
        grauAcesso = null;
    }
}