import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {
        Regras regra = new Regras();
        regra.lerMatrizTerreno();
        regra.sortearTudo();
        regra.lerInformacoesPokemon();
        //Instancia objeto do tipo interface que carrega a interface gráfica do jogo, com os controles.
        Interface inter = new Interface(730, 1370, regra);
        Agent agente = new Agent(inter);
        inter.geraInterface();
        agente.executarAgente();
    }
}
