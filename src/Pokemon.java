public class Pokemon {
    
    private String nome, descricao, tipo;
    private int identificador;
    private String tipos[];

    public Pokemon(){}

    public String[] getTipos() {
        return tipos;
    }

    public void setTipos(String[] tipos) {
        this.tipos = tipos;
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String toString() {
        return "Identificador:" + identificador + "\n" + "Nome:" +nome + "\n" + "Descrição:" + descricao + 
        "\n" + "Tipo(s):" + tipo + "\n";
    }
    
    public String stringToProlog() {
        return "'"+nome+"',"+identificador+",'"+tipos[0]+"','"+tipos[1]+"','"+tipos[2]+"'";
    }
}

