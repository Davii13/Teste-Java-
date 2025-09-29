import java.util.Comparator;

public class ComparadorPorValor implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        int cmp = Double.compare(p1.valorFinal(), p2.valorFinal());
        if (cmp == 0) {
            cmp = Integer.compare(p1.getQuantosProdutos(), p2.getQuantosProdutos());
            if (cmp == 0) {
                return Integer.compare(p1.getIdPedido(), p2.getIdPedido());
            }
        }
        return cmp;
    }
}
