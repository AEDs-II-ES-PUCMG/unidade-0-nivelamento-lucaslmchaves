import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {
    private LocalDate dataDeValidade;
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO= 7; 

    public ProdutoPerecivel(String descricao, double precoCusto, double margemLucro, LocalDate validade) {
        super(descricao, precoCusto, margemLucro);
        if(validade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("O produto está vencido!");
        }   
        dataDeValidade = validade; 
    }

   @Override
    public double valorDeVenda() {
		double desconto = 0d;
        int diasValidade = LocalDate.now().until(dataDeValidade).getDays();
        if (diasValidade <= PRAZO_DESCONTO) {
            desconto=DESCONTO;
        }
        return (precoCusto * (1 + margemLucro)) * (1 - desconto);
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String dados = super.toString();
        dados += "\nVálido até: " + formato.format(dataDeValidade);
        return dados;
    }

    /** * Gera uma linha de texto a partir dos dados do produto perecível.
     * @return Uma string no formato "2;descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
     */
@Override
    public String gerarDadosTexto() {
        String preco = String.format("%.2f", precoCusto).replace(",", ".");
        String margem = String.format("%.2f", margemLucro).replace(",", ".");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("2;%s;%s;%s;%s", descricao, preco, margem, formato.format(dataDeValidade));
    }
}