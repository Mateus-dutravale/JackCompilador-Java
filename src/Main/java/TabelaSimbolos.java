package Main.java;

import java.util.HashMap;
import java.util.Map;

public class TabelaSimbolos {

    public enum Kind {
        STATIC, FIELD, ARG, VAR, NONE
    }

    private class Simbolo {
        String tipo;
        Kind kind;
        int indice;

        public Simbolo(String tipo, Kind kind, int indice) {
            this.tipo = tipo;
            this.kind = kind;
            this.indice = indice;
        }
    }

    private Map<String, Simbolo> escopoClasse;
    private Map<String, Simbolo> escopoSubrotina;
    private Map<Kind, Integer> contadores;

    public TabelaSimbolos() {
        escopoClasse = new HashMap<>();
        escopoSubrotina = new HashMap<>();
        contadores = new HashMap<>();

        contadores.put(Kind.STATIC, 0);
        contadores.put(Kind.FIELD, 0);
        contadores.put(Kind.ARG, 0);
        contadores.put(Kind.VAR, 0);
    }

    public void iniciarSubrotina() {
        escopoSubrotina.clear();
        contadores.put(Kind.ARG, 0);
        contadores.put(Kind.VAR, 0);
    }

    public void definir(String nome, String tipo, Kind kind) {
        int indiceAtual = contadores.get(kind);
        Simbolo novoSimbolo = new Simbolo(tipo, kind, indiceAtual);

        if (kind == Kind.STATIC || kind == Kind.FIELD) {
            escopoClasse.put(nome, novoSimbolo);
        } else if (kind == Kind.ARG || kind == Kind.VAR) {
            escopoSubrotina.put(nome, novoSimbolo);
        }

        contadores.put(kind, indiceAtual + 1);
    }

    public int contagemVariaveis(Kind kind) {
        return contadores.get(kind);
    }

    public Kind kindDe(String nome) {
        if (escopoSubrotina.containsKey(nome)) return escopoSubrotina.get(nome).kind;
        if (escopoClasse.containsKey(nome)) return escopoClasse.get(nome).kind;
        return Kind.NONE;
    }

    public String tipoDe(String nome) {
        if (escopoSubrotina.containsKey(nome)) return escopoSubrotina.get(nome).tipo;
        if (escopoClasse.containsKey(nome)) return escopoClasse.get(nome).tipo;
        return null;
    }

    public int indiceDe(String nome) {
        if (escopoSubrotina.containsKey(nome)) return escopoSubrotina.get(nome).indice;
        if (escopoClasse.containsKey(nome)) return escopoClasse.get(nome).indice;
        return -1;
    }
}