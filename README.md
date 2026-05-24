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
├── test/                   # Pastas de teste (11_test: Seven, Square, Pong, etc.)
├── .gitignore              # Arquivos ignorados pelo Git
├── README.md               # Documentação
└── CompilarFase11.bat      # Script de automação e compilação do projeto
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

Para garantir a geração correta do código, desenvolvemos um script de automação que processa todos os programas de teste simultaneamente.

No terminal (na raiz do projeto), execute:

```bash
.\ExecutaTestesVM.bat
```
### **1. O que o script realiza**

* **Build Automático:** Compila todas as classes Java presentes na pasta `src/Main/java`.
* **Processamento em Lote:** Percorre os diretórios em `test/11_test` (Seven, Square, Average, etc.).
* **Geração VM:** Instancia o compilador para ler os arquivos `.jack` e gerar os correspondentes `.vm` de saída na mesma pasta.

### **2. Emulação do Código Gerado**

Para testar a validade da tradução gerada pelo nosso compilador:
1. Abra a ferramenta **`VMEmulator.bat`** (localizada em `nand2tetris/tools`).
2. Utilize o botão **Load Program** para carregar uma das pastas processadas (ex: `test/11_test/Pong`).
3. Ajuste a velocidade para "Fast" e execute para visualizar o software final rodando no emulador da plataforma.

## ⚙️ Detalhamento dos Componentes

### **MecanismoCompilacao.java**
O núcleo do compilador. Transita a árvore sintática gerando código de máquina. Lida com a resolução de expressões (notação pós-fixada), controle de fluxo (geração de *labels* para `if`/`while`), instanciamento de objetos e cálculo de endereçamento para *Arrays*.

### **TabelaSimbolos.java**
Responsável pelo gerenciamento de escopo e memória.
Mantém registro de variáveis estáticas e campos de classe, além de argumentos e variáveis locais atreladas a sub-rotinas específicas, fornecendo os índices cruciais para as instruções VM (`local 0`, `argument 1`).

### **EscritorVM.java**
Interface de abstração para saída de dados. Simplifica e padroniza a geração dos comandos da máquina virtual (como `push`, `pop`, `call`, `if-goto`), mantendo a classe principal limpa e legível.

### **AnalisadorJack.java e LeitorLexicoJack.java**
Classes base que gerenciam a entrada, tokenizando a linguagem Jack e configurando os caminhos de saída para a extensão final `.vm`.

## ✨ Destaques da Implementação

* **Notação Pós-Fixada:** Tradução limpa de expressões matemáticas e lógicas usando a Máquina de Pilha.
* **Manipulação Limpa de Ponteiros:** Cálculo nativo de memória para operações de *Arrays* usando o segmento `THAT`.
* **Tratamento de Strings:** Chamada dinâmica ao SO (`String.new` e `String.appendChar`) para converter literais em instâncias de caracteres de forma procedimental.
* **Automação:** Fluxo de construção integrado (Build + Execução) em um único `.bat`.