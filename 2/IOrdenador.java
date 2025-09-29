import java.util.Comparator;

public interface IOrdenador<T> {
    T[] ordenar(T[] dados, Comparator<T> comp);
    int getComparacoes();
    int getMovimentacoes();
    double getTempoOrdenacao();
}
