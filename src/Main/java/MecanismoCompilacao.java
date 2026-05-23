package Main.java;
import java.io.*;

public class MecanismoCompilacao {
    private LeitorLexicoJack leitor;
    private PrintWriter escritor;
    private int nivelIdentacao = 0;

    /////////////////////////////////////////////////Construtor///////////////////////////////////////////////
    public MecanismoCompilacao(File entrada, File saida) throws IOException {
        this.leitor = new LeitorLexicoJack(entrada);
        this.escritor = new PrintWriter(saida);

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
        Token token = leitor.tokenAtual();
        String conteudo = token.getLexeme();
        TokenType tipoEnum = token.getType();

        String tagXml;
        switch (tipoEnum) {
            case STRING_CONSTANT:
                tagXml = "stringConstant";
                conteudo = conteudo.replace("\"", "");
                break;
            case INTEGER_CONSTANT:
                tagXml = "integerConstant";
                break;
            case KEYWORD:
                tagXml = "keyword";
                break;
            case SYMBOL:
                tagXml = "symbol";
                break;
            case IDENTIFIER:
                tagXml = "identifier";
                break;
            default:
                tagXml = "unknown";
        }

        if (conteudo.equals("<")) conteudo = "&lt;";
        else if (conteudo.equals(">")) conteudo = "&gt;";
        else if (conteudo.equals("&")) conteudo = "&amp;";
        else if (conteudo.equals("\"")) conteudo = "&quot;";

        imprimirIdentacao();
        escritor.println("<" + tagXml + "> " + conteudo + " </" + tagXml + ">");
    }

    /////////////////////////////////////////////////Regras da Gramática///////////////////////////////////////
    public void compilarClasse() {
        escritor.println("<class>");
        nivelIdentacao++;

        consumir("class");
        consumir(leitor.obterLexema());
        consumir("{");

        while (leitor.obterLexema().equals("static") || leitor.obterLexema().equals("field")) {
            compilarVariavelClasse();
        }

        while (leitor.obterLexema().equals("constructor") || leitor.obterLexema().equals("function") || leitor.obterLexema().equals("method")) {
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

        consumir(leitor.obterLexema());
        consumir(leitor.obterLexema());
        consumir(leitor.obterLexema());

        while (leitor.obterLexema().equals(",")) {
            consumir(",");
            consumir(leitor.obterLexema());
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

        consumir(leitor.obterLexema());
        consumir(leitor.obterLexema());
        consumir(leitor.obterLexema());

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

        if (!leitor.obterLexema().equals(")")) {
            consumir(leitor.obterLexema());
            consumir(leitor.obterLexema());

            while (leitor.obterLexema().equals(",")) {
                consumir(",");
                consumir(leitor.obterLexema());
                consumir(leitor.obterLexema());
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

        while (leitor.obterLexema().equals("var")) {
            compilarVariavel();
        }

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
        consumir(leitor.obterLexema());
        consumir(leitor.obterLexema());

        while (leitor.obterLexema().equals(",")) {
            consumir(",");
            consumir(leitor.obterLexema());
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

        while (true) {
            String token = leitor.obterLexema();
            if (token.equals("let")) compilarLet();
            else if (token.equals("if")) compilarSe();
            else if (token.equals("while")) compilarEnquanto();
            else if (token.equals("do")) compilarFazer();
            else if (token.equals("return")) compilarRetorno();
            else break;
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
        consumir(leitor.obterLexema());
        if (leitor.obterLexema().equals("[")) {
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
        compilarExpressao();
        consumir(")");

        consumir("{");
        compilarStatements();
        consumir("}");

        if (leitor.obterLexema().equals("else")) {
            consumir("else");
            consumir("{");
            compilarStatements();
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
        compilarStatements();
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

        consumir(leitor.obterLexema());
        if (leitor.obterLexema().equals(".")) {
            consumir(".");
            consumir(leitor.obterLexema());
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
        if (!leitor.obterLexema().equals(";")) {
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

        String operadores = "+-*/&|<>=";
        while (leitor.temMaisTokens()) {
            String tokenAtual = leitor.obterLexema();
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

        if (!leitor.obterLexema().equals(")")) {
            compilarExpressao();

            while (leitor.obterLexema().equals(",")) {
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

        TokenType tipo = leitor.tokenAtual().getType();
        String tokenStr = leitor.obterLexema();

        if (tipo == TokenType.INTEGER_CONSTANT || tipo == TokenType.STRING_CONSTANT ||
                tokenStr.equals("true") || tokenStr.equals("false") || tokenStr.equals("null") || tokenStr.equals("this")) {
            consumir(tokenStr);
        }
        else if (tokenStr.equals("(")) {
            consumir("(");
            compilarExpressao();
            consumir(")");
        }
        else if (tokenStr.equals("-") || tokenStr.equals("~")) {
            consumir(tokenStr);
            compilarTermo();
        }
        else {
            consumir(tokenStr);

            String proximo = leitor.obterLexema();
            if (proximo.equals("[")) {
                consumir("[");
                compilarExpressao();
                consumir("]");
            } else if (proximo.equals("(")) {
                consumir("(");
                compilarListaArgumentos();
                consumir(")");
            } else if (proximo.equals(".")) {
                consumir(".");
                consumir(leitor.obterLexema());
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