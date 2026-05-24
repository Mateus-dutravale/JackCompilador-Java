package Main.java;
import java.io.*;

public class MecanismoCompilacao {
    private LeitorLexicoJack leitor;
    private EscritorVM escritor;
    private TabelaSimbolos tabela;
    private String nomeClasseAtual;

    /////////////////////////////////////////////////Construtor///////////////////////////////////////////////
    public MecanismoCompilacao(File entrada, File saida) throws IOException {
        this.leitor = new LeitorLexicoJack(entrada);
        this.escritor = new EscritorVM(saida);
        this.tabela = new TabelaSimbolos();

        if (leitor.temMaisTokens()) {
            leitor.avancar();
        }
    }

    /////////////////////////////////////////////////Finalização///////////////////////////////////////////////
    public void fechar() {
        escritor.fechar();
    }

    /////////////////////////////////////////////////Auxiliares///////////////////////////////////////////////
    private void consumir(String esperado) {
        if (leitor.temMaisTokens()) {
            leitor.avancar();
        }
    }

    /////////////////////////////////////////////////Regras da Gramática///////////////////////////////////////
    public void compilarClasse() {
        consumir("class");

        nomeClasseAtual = leitor.obterLexema();
        consumir(nomeClasseAtual);
        consumir("{");

        while (leitor.obterLexema().equals("static") || leitor.obterLexema().equals("field")) {
            compilarVariavelClasse();
        }

        while (leitor.obterLexema().equals("constructor") || leitor.obterLexema().equals("function") || leitor.obterLexema().equals("method")) {
            compilarSubRotina();
        }

        consumir("}");
    }

    public void compilarVariavelClasse() {
        String kindStr = leitor.obterLexema();
        TabelaSimbolos.Kind kind = kindStr.equals("static") ? TabelaSimbolos.Kind.STATIC : TabelaSimbolos.Kind.FIELD;
        consumir(kindStr);

        String tipo = leitor.obterLexema();
        consumir(tipo);

        String nome = leitor.obterLexema();
        consumir(nome);

        tabela.definir(nome, tipo, kind); // Salva na tabela!

        while (leitor.obterLexema().equals(",")) {
            consumir(",");
            nome = leitor.obterLexema();
            consumir(nome);
            tabela.definir(nome, tipo, kind); // Salva as extras na tabela!
        }

        consumir(";");
    }

    /////////////////////////////////////////////////Sub-rotinas///////////////////////////////////////////////
    public void compilarSubRotina() {
        tabela.iniciarSubrotina(); // Limpa a tabela para a nova sub-rotina

        String tipoSubrotina = leitor.obterLexema(); // constructor, function ou method
        consumir(tipoSubrotina);

        // Se for um método, o 'this' é implicitamente o primeiro argumento (índice 0)
        if (tipoSubrotina.equals("method")) {
            tabela.definir("this", nomeClasseAtual, TabelaSimbolos.Kind.ARG);
        }

        String tipoRetorno = leitor.obterLexema();
        consumir(tipoRetorno);

        String nomeSubrotina = leitor.obterLexema();
        consumir(nomeSubrotina);

        consumir("(");
        compilarListaParametros();
        consumir(")");

        compilarCorpoSubrotina();
    }

    public void compilarListaParametros() {
        if (!leitor.obterLexema().equals(")")) {
            String tipo = leitor.obterLexema();
            consumir(tipo);

            String nome = leitor.obterLexema();
            consumir(nome);
            tabela.definir(nome, tipo, TabelaSimbolos.Kind.ARG);

            while (leitor.obterLexema().equals(",")) {
                consumir(",");
                tipo = leitor.obterLexema();
                consumir(tipo);

                nome = leitor.obterLexema();
                consumir(nome);
                tabela.definir(nome, tipo, TabelaSimbolos.Kind.ARG);
            }
        }
    }

    public void compilarCorpoSubrotina() {
        consumir("{");

        while (leitor.obterLexema().equals("var")) {
            compilarVariavel();
        }

        compilarStatements();

        consumir("}");
    }

    public void compilarVariavel() {
        consumir("var");

        String tipo = leitor.obterLexema();
        consumir(tipo);

        String nome = leitor.obterLexema();
        consumir(nome);
        tabela.definir(nome, tipo, TabelaSimbolos.Kind.VAR);

        while (leitor.obterLexema().equals(",")) {
            consumir(",");
            nome = leitor.obterLexema();
            consumir(nome);
            tabela.definir(nome, tipo, TabelaSimbolos.Kind.VAR);
        }
        consumir(";");
    }

    public void compilarStatements() {
        while (true) {
            String token = leitor.obterLexema();
            if (token.equals("let")) compilarLet();
            else if (token.equals("if")) compilarSe();
            else if (token.equals("while")) compilarEnquanto();
            else if (token.equals("do")) compilarFazer();
            else if (token.equals("return")) compilarRetorno();
            else break;
        }
    }

    public void compilarLet() {
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
    }

    public void compilarSe() {
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
    }

    public void compilarEnquanto() {
        consumir("while");
        consumir("(");
        compilarExpressao();
        consumir(")");
        consumir("{");
        compilarStatements();
        consumir("}");
    }

    public void compilarFazer() {
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
    }

    public void compilarRetorno() {
        consumir("return");
        if (!leitor.obterLexema().equals(";")) {
            compilarExpressao();
        }
        consumir(";");
    }

    /////////////////////////////////////////////////Expressões (Base)/////////////////////////////////////////
    public void compilarExpressao() {
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
    }

    public void compilarListaArgumentos() {
        if (!leitor.obterLexema().equals(")")) {
            compilarExpressao();

            while (leitor.obterLexema().equals(",")) {
                consumir(",");
                compilarExpressao();
            }
        }
    }

    /////////////////////////////////////////////////Termos (Base)/////////////////////////////////////////////
    public void compilarTermo() {
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
    }
}