import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N  (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
        static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos = null;
        try (Scanner leitor = new Scanner(new File(nomeArquivoDados))) {
            if (leitor.hasNextLine()) {
                quantosProdutos = Integer.parseInt(leitor.nextLine());
                vetorProdutos = new Produto[quantosProdutos + MAX_NOVOS_PRODUTOS];
                for (int i = 0; i < quantosProdutos; i++) {
                    if (leitor.hasNextLine()) {
                        vetorProdutos[i] = Produto.criarDoTexto(leitor.nextLine());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado. Criando vetor vazio.");
            vetorProdutos = new Produto[MAX_NOVOS_PRODUTOS];
            quantosProdutos = 0;
        }
        return vetorProdutos; 
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    /** Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus dados. 
     *  A busca não é sensível ao caso.  Em caso de não encontrar o produto, imprime mensagem padrão */
     static void localizarProdutos() {
        System.out.print("Descrição do produto para busca: ");
        String busca = teclado.nextLine().toLowerCase();
        boolean encontrado = false;

        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].descricao.toLowerCase().contains(busca)) {
                System.out.println(produtosCadastrados[i].toString());
                encontrado = true;
            }
        }
        if (!encontrado) System.out.println("Produto não encontrado.");
    }
    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito 
     * melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos. 
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos objetos.
     */
    static void cadastrarProduto() {
        System.out.print("Tipo (1-Não Perecível, 2-Perecível): ");
        int tipo = Integer.parseInt(teclado.nextLine());
        System.out.print("Descrição: ");
        String desc = teclado.nextLine();
        System.out.print("Preço Custo: ");
        double custo = Double.parseDouble(teclado.nextLine());
        System.out.print("Margem Lucro: ");
        double margem = Double.parseDouble(teclado.nextLine());

        if (tipo == 1) {
            produtosCadastrados[quantosProdutos] = new ProdutoNaoPerecivel(desc, custo, margem);
        } else {
            System.out.print("Validade (dd/mm/aaaa): ");
            LocalDate data = LocalDate.parse(teclado.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            produtosCadastrados[quantosProdutos] = new ProdutoPerecivel(desc, custo, margem, data);
        }
        quantosProdutos++;
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(nomeArquivo))) {
            escritor.println(quantosProdutos); // Escreve o novo N
            for (int i = 0; i < quantosProdutos; i++) {
                escritor.println(produtosCadastrados[i].gerarDadosTexto());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados.");
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "src/dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();   
    } 
   
        //Somar números pares até um limite
    static int somarPares(int limite) {
        if (limite <= 0) return 0;
        if (limite % 2 == 0) return limite + somarPares(limite - 2);
        return somarPares(limite - 1);
    }

    // Somar todos os elementos de um vetor double
    static double somarVetor(double[] vet, int tam) {
        if (tam <= 0) return 0;
        return vet[tam - 1] + somarVetor(vet, tam - 1);
    }

    // Contar repetições de um número no vetor
    static int contarRepeticoes(int[] vet, int tam, int busca) {
        if (tam <= 0) return 0;
        int count = (vet[tam - 1] == busca) ? 1 : 0;
        return count + contarRepeticoes(vet, tam - 1, busca);
    
    }
}
