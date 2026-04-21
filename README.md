# 🏛️ Analisador Léxico - Linguagem Jack (Nand2Tetris)

Projeto desenvolvido para a disciplina de **Compiladores** - Universidade Federal do Maranhão (UFMA).

## 👥 Integrantes e Matrículas
* **Gabryella Cruz Sousa** - Matrícula: 20250013701
* **Mateus Dutra Vale** - Matrícula: 20250071302

## 💻 Linguagem e Tecnologias
* **Linguagem:** Java
* **Ferramentas:** IntelliJ IDEA / Git
* **Projeto Base:** Projeto 10 do curso Nand2Tetris

---

## 📂 Estrutura do Projeto

A organização do repositório foi projetada para garantir que todos os scripts de teste funcionem de forma relativa, mantendo as ferramentas de validação integradas:

```text
JackCompilador-Java/
├── nand2tetris/         # Ferramentas e gabaritos oficiais
├── src/main/java/       # Código-fonte (Analisador e Leitor)
├── test/                # Pastas de teste (Array, Square, etc.)
├── .gitignore           # Arquivos ignorados pelo Git
├── LICENSE              # Licença do projeto
├── README.md            # Documentação
└── testa_tudo.bat       # Script de validação automática            
````

## 🚀 Instruções para Compilar e Executar

### 1. Pré-requisitos
* Java JDK instalado (versão 11 ou superior recomendada).
* Sistema Operacional Windows (necessário para executar o script `.bat` de validação).

### 2. Compilação
A partir da raiz do projeto, compile todas as classes localizadas na pasta `src` utilizando o comando:
```bash
javac src/*.java
````
### 3. Execução Manual
O analisador aceita tanto um arquivo individual quanto um diretório completo. O programa gerará arquivos T.xml contendo os tokens identificados no mesmo diretório de origem.

````Bash
# Processar um único arquivo
java -cp src Main.java.AnalisadorJack test/ArrayTest/Main.jack

# Processar todos os arquivos .jack de uma pasta (Recomendado)
java -cp src Main.java.AnalisadorJack test/ArrayTest/
````
## ✅ Validação e Testes
Para facilitar a correção e garantir que a saída está rigorosamente dentro do padrão exigido pelo curso, utilizamos um script de automação localizado na raiz do projeto.

### Utilizando o `testa_tudo.bat`
Este arquivo é o validador oficial do nosso projeto. Ele automatiza o processo de compilação, execução e comparação bit-a-bit dos resultados.

No terminal (na raiz do projeto), execute:

```bash
.\testa_tudo.bat
```

O script irá:

* Compilação: Certifica-se de que as classes Java estão atualizadas.

* Execução: Roda o Main.java.AnalisadorJack sobre as pastas de teste dentro de test/.

* Comparação: Chama o TextComparer do Nand2Tetris para validar os arquivos gerados contra o gabarito oficial em nand2tetris/projects/10.

* Resultado: Exibe Comparison ended successfully para cada teste aprovado.


* Compilar as classes Java.

* Rodar o Main.java.AnalisadorJack sobre as pastas de teste padrão.

* Comparar os arquivos gerados com os arquivos de referência (comparação de strings).

## ⚙️ Detalhamento dos Componentes

* **`Main.java.AnalisadorJack.java`**: Classe driver que gerencia a entrada de dados. Identifica arquivos `.jack`, coordena o processo de leitura e garante o escapamento correto de caracteres especiais no XML (como `<`, `&`, `>`).
* **`Main.java.LeitorLexicoJack.java`**: O núcleo do projeto. Implementa a lógica de tokenização utilizando expressões regulares para classificar os tokens em `Keyword`, `Symbol`, `Identifier`, `IntConst` e `StringConst`, além de ignorar comentários e espaços em branco.
* **`testa_tudo.bat`**: Script de integração que une o código Java às ferramentas de comparação do curso, permitindo auditoria rápida da conformidade do scanner.