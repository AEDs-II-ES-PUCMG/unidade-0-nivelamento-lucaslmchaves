

public class ProdutoNaoPerecivel extends Produto {
    

    public ProdutoNaoPerecivel(String descricao, double precoCusto, double margemLucro) {
        super(descricao, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String descricao, double precoCusto) {
        super(descricao, precoCusto);
    }
    
    @Override
    public double valorDeVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}
    
    /** * Gera uma linha de texto a partir dos dados do produto não perecível.
     * @return Uma string no formato "1;descrição;preçoDeCusto;margemDeLucro"
     */
@Override
    public String gerarDadosTexto() {
        String preco = String.format("%.2f", precoCusto).replace(",", ".");
        String margem = String.format("%.2f", margemLucro).replace(",", ".");
        return String.format("1;%s;%s;%s", descricao, preco, margem);
    }
}
