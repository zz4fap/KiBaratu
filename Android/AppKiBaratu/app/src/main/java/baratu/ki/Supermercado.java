package baratu.ki;

public class Supermercado {

    private Integer id;
    private String nome;
    private String unidade;
    private String endereco;
    private String localizacao;

    public Supermercado(){}

    public Supermercado(Integer id, String nome, String unidade, String endereco, String localizacao){

        this.id = id;
        this.nome = nome;
        this.unidade = unidade;
        this.endereco = endereco;
        this.localizacao = localizacao;
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

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
