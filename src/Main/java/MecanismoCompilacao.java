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

    /////////////////////////////////////////////////Regras da Gramática///////////////////////////////////////
    public void compilarClasse() {
        escritor.println("<class>");
        nivelIdentacao++;

        consumir("class");
        consumir(leitor.obterToken());  // Nome da classe (ex: Square, Main)
        consumir("{");

        /////////////////////////////////////////////Corpo da Classe///////////////////////////////////////////
        // Enquanto o token for static ou field, compila as variáveis de classe
        while (leitor.obterToken().equals("static") || leitor.obterToken().equals("field")) {
            compilarVariavelClasse();
        }

        // Enquanto o token for um início de sub-rotina (Parte que o Mateus vai implementar)
        while (leitor.obterToken().equals("constructor") || leitor.obterToken().equals("function") || leitor.obterToken().equals("method")) {
            compilarSubRotina();
        }

        consumir("}");

        nivelIdentacao--;
        escritor.println("</class>");
    }
    public void compilarVariavelClasse() {
        imprimirIdentacao();
        escritor.println("<classVarDec>");
        nivelIdentacao++;

        consumir(leitor.obterToken()); // Consome 'static' ou 'field'
        consumir(leitor.obterToken()); // Consome o tipo (int, char, boolean, etc)
        consumir(leitor.obterToken()); // Consome o primeiro nome da variável

        // Se tiver vírgula, significa que tem mais variáveis na mesma linha
        while (leitor.obterToken().equals(",")) {
            consumir(",");
            consumir(leitor.obterToken()); // Consome o próximo nome
        }

        consumir(";");

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</classVarDec>");
    }
    /////////////////////////////////////////////////Sub-rotinas///////////////////////////////////////////////
    public void compilarSubRotina() {
        imprimirIdentacao();
        escritor.println("<subroutineDec>");
        nivelIdentacao++;

        consumir(leitor.obterToken());
        consumir(leitor.obterToken());
        consumir(leitor.obterToken());

        consumir("(");
        compilarListaParametros();
        consumir(")");
        compilarCorpoSubrotina();

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</subroutineDec>");
    }

    public void compilarListaParametros() {
        imprimirIdentacao();
        escritor.println("<parameterList>");
        nivelIdentacao++;

        // Se o próximo token não for ')', então existem parâmetros
        if (!leitor.obterToken().equals(")")) {
            consumir(leitor.obterToken()); // tipo
            consumir(leitor.obterToken()); // nomeVar

            // Se houver vírgula, existem mais parâmetros
            while (leitor.obterToken().equals(",")) {
                consumir(",");
                consumir(leitor.obterToken()); // tipo
                consumir(leitor.obterToken()); // nomeVar
            }
        }

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</parameterList>");
    }

    public void compilarCorpoSubrotina() {
        imprimirIdentacao();
        escritor.println("<subroutineBody>");
        nivelIdentacao++;

        consumir("{");

        // Primeiro, trata as declarações de variáveis locais (var int x;)
        while (leitor.obterToken().equals("var")) {
            compilarVariavel();
        }

        // Depois, trata os comandos (let, if, while, etc.)
        compilarStatements();

        consumir("}");

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</subroutineBody>");
    }

    public void compilarVariavel() {
        imprimirIdentacao();
        escritor.println("<varDec>");
        nivelIdentacao++;

        consumir("var");
        consumir(leitor.obterToken()); // tipo
        consumir(leitor.obterToken()); // varName

        while (leitor.obterToken().equals(",")) {
            consumir(",");
            consumir(leitor.obterToken()); // outro varName
        }
        consumir(";");

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</varDec>");
    }

    public void compilarStatements() {
        imprimirIdentacao();
        escritor.println("<statements>");
        nivelIdentacao++;

        // Aqui você vai construir o grande loop dos comandos na próxima etapa
        // Por enquanto, deixamos as tags prontas para o código compilar

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</statements>");
    }
}
