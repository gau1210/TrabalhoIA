import org.jpl7.Query;
import java.util.Map;
import org.jpl7.Term;

public class Agent {
    
    private int[][] matrizTerreno = new int[42][42];//matriz que receberá os valores referentes à localização dos terrenos
    private String[] vetorTerreno = {"grama", "agua", "montanha", "caverna", "vulcao"};
    private Interface inter;
    
    public int[][] getMatrizTerreno() {
        return matrizTerreno;
    }

    public void setMatrizTerreno(int[][] matrizTerreno) {
        this.matrizTerreno = matrizTerreno;
    }

    public Agent(Interface inter){
        this.inter = inter;
    }
    
    public void executarAgente(){
        String comando, regra;
        Query execucaoComando;
        Map<String, Term> results;
        int coordenadaX, coordenadaY, pontos, pokebolas, carga, totalPokemons, ult_capturado, sentido, totpoke=0;
        
        comando = "consult('src/regras.pl')";
        execucaoComando = new Query(comando);
        inter.apresentarLog(comando);
        System.out.println(comando + " " + (execucaoComando.hasSolution() ? "correto" : "falhou"));
        comando = "inicializar";
        execucaoComando = new Query(comando);
        inter.apresentarLog(comando);
        System.out.println(comando + " " + (execucaoComando.hasSolution() ? "correto" : "falhou"));
        for(int i = 0; i<42; i++){
            for(int j= 0; j<42; j++){
                comando = "armazenarTerrenos("+j+","+i+","+vetorTerreno[inter.getRegras().getMatrizTerreno()[i][j]]+")";
                execucaoComando = new Query(comando);
                System.out.println(comando + " " + (execucaoComando.hasSolution() ? "correto" : "falhou"));
                execucaoComando.hasSolution();
                inter.apresentarLog(comando);
            }
        }
       while(totpoke<150){
            comando = "passarInformacoes(CoordenadaX, CoordenadaY, Pontos, Pokebolas, Carga, TotalPokemons, UltCapturado, Sentido)";
            execucaoComando = new Query(comando);
            execucaoComando.hasMoreSolutions();
            results = execucaoComando.nextSolution();
            System.out.println("CoordenadaX " + results.get("CoordenadaX") + ", CoordenadaY " + results.get("CoordenadaY")+", Pontos "+ results.get("Pontos")+", Pokebolas "+ results.get("Pokebolas")+", Carga "+ results.get("Carga")+", TotalPokemons "+ results.get("TotalPokemons")+" Último Pokemon Capturado "+results.get("UltCapturado")+" Sentido "+results.get("Sentido"));
            coordenadaX = Integer.parseInt(results.get("CoordenadaX").toString());
            coordenadaY = Integer.parseInt(results.get("CoordenadaY").toString());
            pontos = Integer.parseInt(results.get("Pontos").toString());
            pokebolas = Integer.parseInt(results.get("Pokebolas").toString());
            carga = Integer.parseInt(results.get("Carga").toString());
            totalPokemons = Integer.parseInt(results.get("TotalPokemons").toString());
            ult_capturado = Integer.parseInt(results.get("UltCapturado").toString());
            sentido = Integer.parseInt(results.get("Sentido").toString());
            inter.repassarInterface(coordenadaX, coordenadaY, pontos, pokebolas, carga, totalPokemons, ult_capturado, sentido);
           
            inter.apresentarLog(comando);
            
            try {
                Thread.sleep(1000);
             } catch (Exception e) {
                e.printStackTrace();
             }
           
            regra = inter.elementosDaCasa();
            System.out.println(regra);
            inter.apresentarLog(regra);

            comando = inter.elementosDaCasa();
            execucaoComando = new Query(comando);
            execucaoComando.hasMoreSolutions();

           
           totpoke=totalPokemons;
           

            if(totalPokemons>0){
                comando = "pokemon(NOME,COD,T1,T2,T3,TERRENO)";
                execucaoComando = new Query(comando);
                while(execucaoComando.hasMoreSolutions()){
                    results = execucaoComando.nextSolution();
                    System.out.println("NOME "+results.get("NOME").toString()+
                                       " COD "+results.get("COD").toString()+
                                       " T1 "+results.get("T1").toString()+
                                       " T2 "+results.get("T2").toString()+
                                       " T3 "+results.get("T3").toString()+
                                       " TERRENO "+results.get("TERRENO").toString());
                }
            }
       }
    }
}
