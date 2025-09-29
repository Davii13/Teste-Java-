import java.util.Comparator;

public class HeapSort implements IOrdenador<Pedido> {

    private int comparacoes;
    private int movimentacoes;
    private double tempoOrdenacao;

    @Override
    public Pedido[] ordenar(Pedido[] dados, Comparator<Pedido> comp) {
        comparacoes = 0;
        movimentacoes = 0;
        long inicio = System.currentTimeMillis();

        int n = dados.length;

        // Construir max-heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(dados, n, i, comp);
        }

        // Extrair elementos do heap
        for (int i = n - 1; i > 0; i--) {
            trocar(dados, 0, i);          // move o maior para o final
            heapify(dados, i, 0, comp);   // refaz heap
        }

        long fim = System.currentTimeMillis();
        tempoOrdenacao = fim - inicio;

        return dados;
    }

    private void heapify(Pedido[] dados, int n, int i, Comparator<Pedido> comp) {
        int maior = i;          // raiz
        int esq = 2 * i + 1;    // filho esquerdo
        int dir = 2 * i + 2;    // filho direito

        if (esq < n) {
            comparacoes++;
            if (comp.compare(dados[esq], dados[maior]) > 0) {
                maior = esq;
            }
        }

        if (dir < n) {
            comparacoes++;
            if (comp.compare(dados[dir], dados[maior]) > 0) {
                maior = dir;
            }
        }

        if (maior != i) {
            trocar(dados, i, maior);
            heapify(dados, n, maior, comp);
        }
    }

    private void trocar(Pedido[] dados, int i, int j) {
        Pedido temp = dados[i];
        dados[i] = dados[j];
        dados[j] = temp;
        movimentacoes += 3; // cada troca conta como 3 movimentações
    }

    @Override
    public int getComparacoes() {
        return comparacoes;
    }

    @Override
    public int getMovimentacoes() {
        return movimentacoes;
    }

    @Override
    public double getTempoOrdenacao() {
        return tempoOrdenacao;
    }
}
