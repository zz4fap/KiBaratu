package baratu.ki;

public class Produto {

    private Integer id;
    private String nome;
    private String marca;
    private Double preco;
    private Integer supermercadoId;
    private String supermercadoNome;

    public Produto(){}

    public Produto(Integer id, String nome, String marca, Double preco, String supermercadoId)
    {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
        this.supermercadoNome = supermercadoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getSupermercadoId() {
        return supermercadoId;
    }

    public void setSupermercadoId(Integer supermercadoId) {
        this.supermercadoId = supermercadoId;
    }

    public String getSupermercadoNome() {
        return supermercadoNome;
    }

    public void setSupermercadoNome(String supermercadoNome) {
        this.supermercadoNome = supermercadoNome;
    }
}
