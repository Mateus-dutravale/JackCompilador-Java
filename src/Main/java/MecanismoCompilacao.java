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

        // 1. Descobre quem é a variável que vai receber o valor
        String nomeVariavel = leitor.obterLexema();
        consumir(nomeVariavel);

        boolean ehArray = false;
        if (leitor.obterLexema().equals("[")) {
            ehArray = true;
            consumir("[");
            compilarExpressao(); // Avalia o índice do array
            consumir("]");
        }

        consumir("=");

        // 2. Resolve a matemática do lado direito do igual (joga o resultado na pilha)
        compilarExpressao();

        consumir(";");

        // 3. Tira o resultado da pilha e salva na variável certa usando a Tabela!
        if (!ehArray) {
            TabelaSimbolos.Kind kind = tabela.kindDe(nomeVariavel);
            int indice = tabela.indiceDe(nomeVariavel);

            switch (kind) {
                case STATIC: escritor.escreverPop("static", indice); break;
                case FIELD:  escritor.escreverPop("this", indice); break;
                case VAR:    escritor.escreverPop("local", indice); break;
                case ARG:    escritor.escreverPop("argument", indice); break;
                default: break;
            }
        } else {
            // Lógica de salvar em Arrays (a[i] = x).
        }
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
        compilarTermo(); // Empilha o primeiro número/variável

        String operadores = "+-*/&|<>=";
        while (leitor.temMaisTokens()) {
            String op = leitor.obterLexema();
            if (op.length() == 1 && operadores.contains(op)) {
                consumir(op);
                compilarTermo(); // Empilha o segundo número/variável

                // Agora que os dois estão na pilha, chamamos a operação
                switch (op) {
                    case "+": escritor.escreverAritmetica("add"); break;
                    case "-": escritor.escreverAritmetica("sub"); break;
                    case "*": escritor.escreverChamada("Math.multiply", 2); break;
                    case "/": escritor.escreverChamada("Math.divide", 2); break;
                    case "&": escritor.escreverAritmetica("and"); break;
                    case "|": escritor.escreverAritmetica("or"); break;
                    case "<": escritor.escreverAritmetica("lt"); break;
                    case ">": escritor.escreverAritmetica("gt"); break;
                    case "=": escritor.escreverAritmetica("eq"); break;
                }
            } else {
                break;
            }
        }
    }

    // Auxiliar para a Geração de Código: Busca a variável na tabela e empilha o segmento correto
    private void escreverPushDaTabela(String nome) {
        TabelaSimbolos.Kind kind = tabela.kindDe(nome);
        int indice = tabela.indiceDe(nome);

        switch (kind) {
            case STATIC: escritor.escreverPush("static", indice); break;
            case FIELD: escritor.escreverPush("this", indice); break;
            case VAR: escritor.escreverPush("local", indice); break;
            case ARG: escritor.escreverPush("argument", indice); break;
            default: break; // Se não estiver na tabela, pode ser um nome de Classe ou Função
        }
    }

    public void compilarListaArgumentos() {
        int nArgs = 0;
        if (!leitor.obterLexema().equals(")")) {
            compilarExpressao();
            nArgs++;

            while (leitor.obterLexema().equals(",")) {
                consumir(",");
                compilarExpressao();
                nArgs++;
            }
        }
    }

    /////////////////////////////////////////////////Termos (Base)/////////////////////////////////////////////

    public void compilarTermo() {
        TokenType tipo = leitor.tokenAtual().getType();
        String tokenStr = leitor.obterLexema();

        if (tipo == TokenType.INTEGER_CONSTANT) {
            escritor.escreverPush("constant", Integer.parseInt(tokenStr));
            consumir(tokenStr);
        }
        else if (tokenStr.equals("true")) {
            escritor.escreverPush("constant", 0);
            escritor.escreverAritmetica("not");
            consumir(tokenStr);
        }
        else if (tokenStr.equals("false") || tokenStr.equals("null")) {
            escritor.escreverPush("constant", 0);
            consumir(tokenStr);
        }
        else if (tokenStr.equals("this")) {
            escritor.escreverPush("pointer", 0);
            consumir(tokenStr);
        }
        else if (tokenStr.equals("(")) {
            consumir("(");
            compilarExpressao();
            consumir(")");
        }
        else if (tokenStr.equals("-") || tokenStr.equals("~")) {
            consumir(tokenStr);
            compilarTermo(); // Empilha o número primeiro
            if (tokenStr.equals("-")) escritor.escreverAritmetica("neg");
            else escritor.escreverAritmetica("not");
        }
        else if (tipo == TokenType.IDENTIFIER) {
            String nome = tokenStr;
            consumir(nome);

            String proximo = leitor.obterLexema();
            if (proximo.equals("[")) {
                consumir("[");
                compilarExpressao();
                consumir("]");
            } else if (proximo.equals("(") || proximo.equals(".")) {
                if (proximo.equals(".")) {
                    consumir(".");
                    consumir(leitor.obterLexema());
                }
                consumir("(");
                compilarListaArgumentos();
                consumir(")");
            } else {
                escreverPushDaTabela(nome);
            }
        } else {
            consumir(tokenStr);
        }
    }
}