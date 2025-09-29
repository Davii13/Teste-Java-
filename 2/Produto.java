import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Produto implements Comparable<Produto> {

    /** Tipo do produto: 1 = NÃO PERECÍVEL, 2 = PERECÍVEL */
    protected int tipo;
    
    /** Descrição do produto */
    protected String descricao;
    
    /** Preço de custo do produto */
    protected double precoDeCusto;
    
    /** Margem de lucro do produto */
    protected double margemDeLucro;
    
    /** Data de validade (apenas para produtos perecíveis) */
    protected LocalDate dataDeValidade;
    
    /**
     * Construtor para produto NÃO PERECÍVEL
     */
    public Produto(int tipo, String descricao, double precoDeCusto, double margemDeLucro) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.precoDeCusto = precoDeCusto;
        this.margemDeLucro = margemDeLucro;
        this.dataDeValidade = null;
    }
    
    /**
     * Construtor para produto PERECÍVEL
     */
    public Produto(int tipo, String descricao, double precoDeCusto, double margemDeLucro, LocalDate dataDeValidade) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.precoDeCusto = precoDeCusto;
        this.margemDeLucro = margemDeLucro;
        this.dataDeValidade = dataDeValidade;
    }
    
    /**
     * Cria um produto a partir de uma linha de texto
     * Formato: tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]
     */
    public static Produto criarDoTexto(String linha) {
        String[] dados = linha.split(";");
        
        int tipo = Integer.parseInt(dados[0]);
        String descricao = dados[1];
        double precoDeCusto = Double.parseDouble(dados[2]);
        double margemDeLucro = Double.parseDouble(dados[3]);
        
        if (tipo == 2 && dados.length > 4) {
            // Produto perecível
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataDeValidade = LocalDate.parse(dados[4], formato);
            return new Produto(tipo, descricao, precoDeCusto, margemDeLucro, dataDeValidade);
        } else {
            // Produto não perecível
            return new Produto(tipo, descricao, precoDeCusto, margemDeLucro);
        }
    }
    
    /**
     * Calcula o valor de venda do produto
     */
    public double valorDeVenda() {
        double valorVenda = precoDeCusto * (1.0 + margemDeLucro);
        BigDecimal valorBD = new BigDecimal(Double.toString(valorVenda));
        valorBD = valorBD.setScale(2, RoundingMode.HALF_UP);
        return valorBD.doubleValue();
    }
    
    /**
     * Representação em String do produto
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("NOME: ").append(descricao).append(": R$ ");
        str.append(String.format("%.2f", valorDeVenda()));
        
        if (tipo == 2 && dataDeValidade != null) {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            str.append("\nVálido até: ").append(formato.format(dataDeValidade));
        }
        
        return str.toString();
    }
    
    /**
     * Comparação padrão: por descrição
     */
    @Override
    public int compareTo(Produto outro) {
        return this.descricao.compareTo(outro.descricao);
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public double getPrecoDeCusto() {
        return precoDeCusto;
    }
    
    public double getMargemDeLucro() {
        return margemDeLucro;
    }
    
    public int getTipo() {
        return tipo;
    }
    
    public LocalDate getDataDeValidade() {
        return dataDeValidade;
    }
}