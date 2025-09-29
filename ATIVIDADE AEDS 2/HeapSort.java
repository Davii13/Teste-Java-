import java.util.Comparator;

public class HeapSort {
    
    // Ordena um vetor qualquer usando Comparable (ordem natural)
    public static <T extends Comparable<T>> void sort(T[] vetor) {
        int n = vetor.length;

        // Monta o heap máximo
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(vetor, n, i);
        }

        // Extrai elementos do heap
        for (int i = n - 1; i > 0; i--) {
            trocar(vetor, 0, i);
            heapify(vetor, i, 0);
        }
    }

    // Ordena usando um Comparator (ordem personalizada)
    public static <T> void sort(T[] vetor, Comparator<T> comp) {
        int n = vetor.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(vetor, n, i, comp);
        }

        for (int i = n - 1; i > 0; i--) {
            trocar(vetor, 0, i);
            heapify(vetor, i, 0, comp);
        }
    }

    // Heapify com Comparable
    private static <T extends Comparable<T>> void heapify(T[] vetor, int n, int i) {
        int maior = i;
        int esq = 2 * i + 1;
        int dir = 2 * i + 2;

        if (esq < n && vetor[esq].compareTo(vetor[maior]) > 0) {
            maior = esq;
        }
        if (dir < n && vetor[dir].compareTo(vetor[maior]) > 0) {
            maior = dir;
        }
        if (maior != i) {
            trocar(vetor, i, maior);
            heapify(vetor, n, maior);
        }
    }

    // Heapify com Comparator
    private static <T> void heapify(T[] vetor, int n, int i, Comparator<T> comp) {
        int maior = i;
        int esq = 2 * i + 1;
        int dir = 2 * i + 2;

        if (esq < n && comp.compare(vetor[esq], vetor[maior]) > 0) {
            maior = esq;
        }
        if (dir < n && comp.compare(vetor[dir], vetor[maior]) > 0) {
            maior = dir;
        }
        if (maior != i) {
            trocar(vetor, i, maior);
            heapify(vetor, n, maior, comp);
        }
    }

    // Método utilitário para trocar elementos
    private static <T> void trocar(T[] vetor, int i, int j) {
        T temp = vetor[i];
        vetor[i] = vetor[j];
        vetor[j] = temp;
    }
}
