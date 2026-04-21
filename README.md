# 🏛️ Analisador Sintático - Linguagem Jack (Nand2Tetris)

Projeto desenvolvido para a disciplina de **Compiladores** - Universidade Federal do Maranhão (UFMA).

## 👥 Integrantes e Matrículas

* **Gabryella Cruz Sousa** - Matrícula: `20250013701`
* **Mateus Dutra Vale** - Matrícula: `20250071302`

## 💻 Linguagem e Tecnologias

* **Linguagem:** Java
* **Ferramentas:** IntelliJ IDEA / Git
* **Projeto Base:** Projeto 10 do curso **Nand2Tetris** (Unidade 1 - Syntax Analysis)

## 📂 Estrutura do Projeto

A organização do repositório garante que os scripts de teste funcionem de forma relativa, mantendo as ferramentas de validação integradas:

```plaintext
JackCompilador-Java/
├── nand2tetris/            # Ferramentas e gabaritos oficiais
├── src/Main/java/          # Código-fonte (Analisador, Leitor e Mecanismo)
├── test/                   # Pastas de teste (ArrayTest, SquareTest, etc.)
├── .gitignore              # Arquivos ignorados pelo Git
├── README.md               # Documentação
└── ExecutarTestesParse.bat # Script de validação automática do Parser
```
## 🚀 Instruções para Compilar e Executar

### **1. Pré-requisitos**

* Java JDK instalado (**versão 11 ou superior recomendada**)
* Sistema Operacional **Windows** (necessário para executar o script `.bat` de validação)

### **2. Compilação**

A partir da raiz do projeto, navegue até a pasta das classes e compile:

```bash
cd src/Main/java
javac *.java
```
### **3. Execução Manual**

O analisador processa diretórios completos.  
O programa gerará arquivos `P.xml` (Parser) contendo a árvore sintática no mesmo diretório de origem.

```bash
# Estando em src/Main/java
java AnalisadorJack "../../../test/ArrayTest/"
```
## ✅ Validação e Testes Oficiais

Para garantir que a saída está rigorosamente dentro do padrão exigido pelo curso (**comparação bit-a-bit**), utilizamos o script de automação localizado na raiz do projeto.

### **Utilizando o `ExecutarTestesParse.bat`**

Este arquivo é o validador oficial da Unidade 1.  
Ele automatiza a comparação dos resultados gerados pelo código com os arquivos de referência do MIT.

No terminal (na raiz do projeto), execute:

```bash
.\ExecutarTestesParse.bat
```
### **O que o script realiza**

* **Comparação:** chama o `TextComparer` do Nand2Tetris
* **Validação:** compara o arquivo gerado (ex: `MainP.xml`) com o gabarito oficial em `nand2tetris/projects/10`
* **Resultado:** exibe `Comparison ended successfully` para cada teste aprovado

## ⚙️ Detalhamento dos Componentes

### **AnalisadorJack.java**

Classe driver que gerencia a entrada.  
Localiza arquivos `.jack`, define o nome de saída como `P.xml` e coordena o início da compilação.

### **MecanismoCompilacao.java**

O núcleo do Parser.  
Implementa a descida recursiva para processar a gramática Jack, gerando a estrutura XML identada e tratando o escapamento de caracteres especiais:

* `<`
* `>`
* `&`
* `"`

### **LeitorLexicoJack.java**

Responsável pela tokenização, classificando os elementos em:

* `keyword`
* `symbol`
* `identifier`
* `integerConstant`
* `stringConstant`

### **ExecutarTestesParse.bat**

Script de integração que automatiza a auditoria de conformidade do analisador sintático.

## ✨ Destaques da Implementação

* **Tratamento de Strings:** remoção de aspas duplas no conteúdo das tags `<stringConstant>`
* **Recursividade:** processamento de expressões, termos e declarações de sub-rotinas
* **Saída XML:** geração de arquivos com identação hierárquica para representação da árvore sintática