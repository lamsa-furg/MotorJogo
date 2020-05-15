package motorJava;

/**
 * Produto
 */
public abstract class Produto {
    private int id;
    private String tipo;
    private double custo;
    private double poluicao;

    public Produto(int id, String tipo, double custo, double poluicao){
        this.id = id;
        this.tipo = tipo;
        this.custo = custo;
        this.poluicao = poluicao;
    }

    public void imprimeCaracteristicas(){
        System.out.println("ID: " + this.id + "; Tipo: " + this.tipo + "; Custo: " + this.custo + "; Poluicao: " + this.poluicao); //, "; Produtividade: ", this.produtividade);
    }

    public String imprimeCaracteristicasString(){
    	String dados = "";
        dados += "ID: " + this.id + "; Tipo: " + this.tipo + "; Custo: " + this.custo + "; Poluicao: " + this.poluicao + "\n";
        return dados;
    }
    
    public int getId(){
        return this.id;
    }

    public String getTipo(){
        return this.tipo;
    }

    public double getCusto(){
        return this.custo;
    }

    public double getPoluicao(){
        return this.poluicao;
    }

}