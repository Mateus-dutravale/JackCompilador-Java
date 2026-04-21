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

        // Loop para identificar qual comando processar
        while (true) {
            String token = leitor.obterToken();
            if (token.equals("let")) compilarLet();
            else if (token.equals("if")) compilarSe();
            else if (token.equals("while")) compilarEnquanto();
            else if (token.equals("do")) compilarFazer();
            else if (token.equals("return")) compilarRetorno();
            else break; // Se não for nenhum desses, os comandos acabaram
        }
        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</statements>");
    }
    public void compilarLet() {
        imprimirIdentacao();
        escritor.println("<letStatement>");
        nivelIdentacao++;
        consumir("let");
        consumir(leitor.obterToken()); // nomeVar
        if (leitor.obterToken().equals("[")) {
            consumir("[");
            compilarExpressao();
            consumir("]");
        }
        consumir("=");
        compilarExpressao();
        consumir(";");
        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</letStatement>");
    }
    public void compilarSe() {
        imprimirIdentacao();
        escritor.println("<ifStatement>");
        nivelIdentacao++;

        consumir("if");
        consumir("(");
        compilarExpressao(); // Chama a lógica da Fase 3
        consumir(")");

        consumir("{");
        compilarStatements(); // Aqui a mágica acontece: ele volta a ler comandos dentro do IF
        consumir("}");

        /////////////////////////////////////////////Tratamento do ELSE////////////////////////////////////////
        // O else é opcional, então verificamos se ele existe antes de consumir
        if (leitor.obterToken().equals("else")) {
            consumir("else");
            consumir("{");
            compilarStatements(); // Recursividade novamente para o bloco else
            consumir("}");
        }

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</ifStatement>");
    }

    public void compilarEnquanto() {
        imprimirIdentacao();
        escritor.println("<whileStatement>");
        nivelIdentacao++;
        consumir("while");
        consumir("(");
        compilarExpressao();
        consumir(")");
        consumir("{");
        compilarStatements(); // Recursividade!
        consumir("}");
        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</whileStatement>");
    }

    public void compilarFazer() {
        imprimirIdentacao();
        escritor.println("<doStatement>");
        nivelIdentacao++;
        consumir("do");
        // Aqui chamamos uma expressão de chamada de função (mesma lógica do Termo)
        consumir(leitor.obterToken());
        if (leitor.obterToken().equals(".")) {
            consumir(".");
            consumir(leitor.obterToken());
        }
        consumir("(");
        compilarListaArgumentos();
        consumir(")");
        consumir(";");
        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</doStatement>");
    }

    public void compilarRetorno() {
        imprimirIdentacao();
        escritor.println("<returnStatement>");
        nivelIdentacao++;
        consumir("return");
        if (!leitor.obterToken().equals(";")) {
            compilarExpressao();
        }
        consumir(";");
        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</returnStatement>");
    }

    /////////////////////////////////////////////////Expressões (Base)/////////////////////////////////////////

    public void compilarExpressao() {
        imprimirIdentacao();
        escritor.println("<expression>");
        nivelIdentacao++;

        compilarTermo();

        // Se houver um operador (+, -, *, / etc), ele processa o próximo termo
        String operadores = "+-*/&|<>=";
        while (leitor.temMaisTokens()) {
            String tokenAtual = leitor.obterToken();
            if (tokenAtual.length() == 1 && operadores.contains(tokenAtual)) {
                consumir(tokenAtual);
                compilarTermo();
            } else {
                break;
            }
        }

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</expression>");
    }

    public void compilarListaArgumentos() {
        imprimirIdentacao();
        escritor.println("<expressionList>");
        nivelIdentacao++;

        // Se o próximo token não for ')', significa que há argumentos (expressões)
        if (!leitor.obterToken().equals(")")) {
            compilarExpressao();

            // Enquanto houver vírgula, continua processando as expressões
            while (leitor.obterToken().equals(",")) {
                consumir(",");
                compilarExpressao();
            }
        }

        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</expressionList>");
    }

    /////////////////////////////////////////////////Termos (Base)/////////////////////////////////////////////

    public void compilarTermo() {
        imprimirIdentacao();
        escritor.println("<term>");
        nivelIdentacao++;

        String token = leitor.obterToken();
        String tipo = leitor.tipoToken();

        // 1. Constantes e Palavras-chave (true, false, null, this)
        if (tipo.equals("integerConstant") || tipo.equals("stringConstant") ||
                token.equals("true") || token.equals("false") || token.equals("null") || token.equals("this")) {
            consumir(token);
        }
        // 2. Expressões entre parênteses (Recursividade!)
        else if (token.equals("(")) {
            consumir("(");
            compilarExpressao();
            consumir(")");
        }
        // 3. Operadores Unários (-x ou ~y)
        else if (token.equals("-") || token.equals("~")) {
            consumir(token);
            compilarTermo();
        }
        // 4. Identificadores (Variáveis, Arrays ou Chamadas de Método)
        else {
            consumir(token); // Consome o nome (da var ou da classe/método)

            String proximo = leitor.obterToken();
            if (proximo.equals("[")) { // É um Array
                consumir("[");
                compilarExpressao();
                consumir("]");
            } else if (proximo.equals("(")) { // Chamada direta: metodo(args)
                consumir("(");
                compilarListaArgumentos();
                consumir(")");
            } else if (proximo.equals(".")) { // Chamada de classe: Classe.metodo(args)
                consumir(".");
                consumir(leitor.obterToken()); // nome do método
                consumir("(");
                compilarListaArgumentos();
                consumir(")");
            }
        }
        nivelIdentacao--;
        imprimirIdentacao();
        escritor.println("</term>");
    }

}
