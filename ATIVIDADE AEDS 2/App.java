import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class App {

    static String nomeArquivoDados;
    static Scanner teclado;

    static Produto[] produtosCadastrados;
    static int quantosProdutos = 0;

    static Pedido[] pedidosCadastrados;
    static Pedido[] pedidosOrdenadosPorData;
    static Pedido[] pedidosOrdenadosPorCodigo;
    static int quantPedidos = 0;

    static IOrdenator<Pedido> ordenador;

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        T valor;
        System.out.println(mensagem);
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (Exception e) {
            return null;
        }
        return valor;
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {
        Scanner arquivo = null;
        int numProdutos;
        String linha;
        Produto produto;
        Produto[] produtosCadastrados;

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));

            numProdutos = Integer.parseInt(arquivo.nextLine());
            produtosCadastrados = new Produto[numProdutos];

            for (int i = 0; i < numProdutos; i++) {
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                produtosCadastrados[i] = produto;
            }
            quantosProdutos = numProdutos;

        } catch (IOException excecaoArquivo) {
            produtosCadastrados = null;
        } finally {
            if (arquivo != null) arquivo.close();
        }

        return produtosCadastrados;
    }

    static Pedido[] lerPedidos(String nomeArquivoDados) {
        Pedido[] pedidosCadastrados;
        Scanner arquivo = null;
        int numPedidos;
        String linha;
        Pedido pedido;

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));

            numPedidos = Integer.parseInt(arquivo.nextLine());
            pedidosCadastrados = new Pedido[numPedidos];

            for (int i = 0; i < numPedidos; i++) {
                linha = arquivo.nextLine();
                pedido = criarPedido(linha);
                pedidosCadastrados[i] = pedido;
            }
            quantPedidos = numPedidos;

        } catch (IOException excecaoArquivo) {
            pedidosCadastrados = null;
        } finally {
            if (arquivo != null) arquivo.close();
        }

        return pedidosCadastrados;
    }

    private static Pedido criarPedido(String dados) {
        String[] dadosPedido;
        DateTimeFormatter formatoData;
        LocalDate dataDoPedido;
        int formaDePagamento;
        Pedido pedido;
        Produto produto;

        dadosPedido = dados.split(";");
        formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataDoPedido = LocalDate.parse(dadosPedido[0], formatoData);

        formaDePagamento = Integer.parseInt(dadosPedido[1]);
        pedido = new Pedido(dataDoPedido, formaDePagamento);

        for (int i = 2; i < dadosPedido.length; i++) {
            produto = pesquisarProduto(dadosPedido[i]);
            pedido.incluirProduto(produto);
        }
        return pedido;
    }

    static Produto pesquisarProduto(String pesquisado) {
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].descricao.equalsIgnoreCase(pesquisado)) {
                return produtosCadastrados[i];
            }
        }
        return null;
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Procurar por pedidos realizados em uma data");
        System.out.println("2 - Ordenar pedidos");
        System.out.println("3 - Embaralhar pedidos");
        System.out.println("4 - Listar todos os pedidos");
        System.out.println("0 - Finalizar");

        return lerOpcao("Digite sua opção: ", Integer.class);
    }

    /** --------- IMPLEMENTAÇÃO DA TAREFA 2 --------- */
    static void localizarPedidosPorData() {
        cabecalho();
        System.out.print("Digite a data no formato dd/MM/yyyy: ");
        String entrada = teclado.nextLine();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(entrada, formato);

        boolean encontrado = false;
        for (Pedido p : pedidosOrdenadosPorData) {
            if (p.getDataPedido().equals(data)) {
                System.out.println(p);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Nenhum pedido encontrado nessa data.");
        }
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.out.println("5 - Heapsort");
        System.out.println("0 - Finalizar");

        return lerOpcao("Digite sua opção: ", Integer.class);
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Por código");
        System.out.println("2 - Por data");
        System.out.println("3 - Por valor");

        return lerOpcao("Digite sua opção: ", Integer.class);
    }

    /** --------- IMPLEMENTAÇÃO DA TAREFA 1 --------- */
    static void ordenarPedidos() {
        int opcAlgoritmo = exibirMenuOrdenadores();
        int opcComparador = exibirMenuComparadores();

        Comparator<Pedido> comparador = switch (opcComparador) {
            case 1 -> new ComparadorPorCodigo();
            case 2 -> new ComparadorPorData();
            case 3 -> new ComparadorPorValor();
            default -> null;
        };

        switch (opcAlgoritmo) {
            case 1 -> ordenador = new BolhaSort<>();
            case 2 -> ordenador = new InsercaoSort<>();
            case 3 -> ordenador = new SelecaoSort<>();
            case 4 -> ordenador = new MergeSort<>();
            case 5 -> ordenador = new Heapsort<>();
            default -> {
                System.out.println("Algoritmo inválido.");
                return;
            }
        }

        long inicio = System.currentTimeMillis();
        ordenador.ordenar(pedidosCadastrados, comparador);
        long fim = System.currentTimeMillis();

        System.out.println("Pedidos ordenados com sucesso!");
        System.out.println("Tempo gasto: " + (fim - inicio) + " ms");
    }

    static void embaralharPedidos() {
        Collections.shuffle(Arrays.asList(pedidosCadastrados));
    }

    static void listarTodosOsPedidos() {
        cabecalho();
        System.out.println("\nPedidos cadastrados: ");
        for (int i = 0; i < quantPedidos; i++) {
            System.out.println(String.format("%02d - %s\n", (i + 1), pedidosCadastrados[i].toString()));
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in, Charset.forName("UTF-8"));

        nomeArquivoDados = "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);

        String nomeArquivoPedidos = "pedidos.txt";
        pedidosCadastrados = lerPedidos(nomeArquivoPedidos);

        // Criar cópias já ordenadas
        pedidosOrdenadosPorCodigo = Arrays.copyOf(pedidosCadastrados, quantPedidos);
        Arrays.sort(pedidosOrdenadosPorCodigo, new ComparadorPorCodigo());

        pedidosOrdenadosPorData = Arrays.copyOf(pedidosCadastrados, quantPedidos);
        Arrays.sort(pedidosOrdenadosPorData, new ComparadorPorData());

        int opcao;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> localizarPedidosPorData();
                case 2 -> ordenarPedidos();
                case 3 -> embaralharPedidos();
                case 4 -> listarTodosOsPedidos();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
            }
            if (opcao != 0) pausa();
        } while (opcao != 0);

        teclado.close();
    }
}
