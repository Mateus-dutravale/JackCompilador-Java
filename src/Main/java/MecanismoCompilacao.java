import java.io.*;

public class MecanismoCompilacao {
    private LeitorLexicoJack leitor;
    private PrintWriter escritor;
    private int nivelIdentacao = 0;

    /////////////////////////////////////////////////Construtor///////////////////////////////////////////////
    public MecanismoCompilacao(File entrada, File saida) throws IOException {
        this.leitor = new LeitorLexicoJack(entrada);
        this.escritor = new PrintWriter(saida);

        // Posiciona no primeiro token para iniciar a análise
        if (leitor.temMaisTokens()) {
            leitor.avancar();
        }
    }

    /////////////////////////////////////////////////Finalização///////////////////////////////////////////////
    public void fechar() {
        escritor.close();
    }
    /////////////////////////////////////////////////Escrita XML///////////////////////////////////////////////
    private void imprimirIdentacao() {
        for (int i = 0; i < nivelIdentacao; i++) escritor.print("  ");
    }

    private void consumir(String esperado) {
        escreverToken();
        if (leitor.temMaisTokens()) {
            leitor.avancar();
        }
    }

    private void escreverToken() {
        String tipo = leitor.tipoToken();
        String conteudo = leitor.obterToken();

        /////////////////////////////////////////////Escapamento XML///////////////////////////////////////////
        if (conteudo.equals("<")) conteudo = "&lt;";
        else if (conteudo.equals(">")) conteudo = "&gt;";
        else if (conteudo.equals("&")) conteudo = "&amp;";
        else if (conteudo.equals("\"")) conteudo = "&quot;";

        imprimirIdentacao();
        String tag = tipo.toLowerCase().replace("_", "");
        escritor.println("<" + tag + "> " + conteudo + " </" + tag + ">");
    }
}