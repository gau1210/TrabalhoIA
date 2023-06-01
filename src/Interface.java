import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Interface extends JFrame{
    
    private JPanel mapa = new JPanel(), controles = new JPanel(), painelInfo = new JPanel();
    private JPanel painelAtual = new JPanel(), painelLog = new JPanel();;
    private int altura, largura, posiX, posiY, indexPainelAtual, indexPainelLog;
    private ArrayList<JLabel> terrenos = new ArrayList<>();
    private ArrayList<JLabel> pokemons = new ArrayList<>();
    private ImageIcon[] tipoter = new ImageIcon[5];
    private ImageIcon[] tipoAvat = new ImageIcon[5]; 
    private ImageIcon[] tipoEle = new ImageIcon[3];
    private JLabel pontos = new JLabel(), pokebolas = new JLabel(), carga = new JLabel(), log = new JLabel(), pokedex = new JLabel();
    private Regras regras;
    private int pokemonsAnterior=0, codPokeAnt=0, Ianterior=0, Janterior=0;
    private JLabel sentido = new JLabel();
    
    final int CENTRO = 152;
    final int LOJA = 153;
    final int TREINADOR = 154;

    final int PERFUME = 155;    
    final int PROPAGANDA_BOLAS = 156;
    final int DESAFIO = 157;

    //CONSTRUTOR//
    public Interface(int altura, int largura, Regras regra){
        setAltura(altura);
        setLargura(largura);
        defineTipos();
        defineAvatar();
        defineElementos();
        this.setRegras(regra);
    }

    //INFORMAÇÕES//
    public void repassarInterface(int coordenadaX, int coordenadaY, int pontos, int pokebolas, int carga, int totalPokemons, int ult_capturado, int sentido){
        movimentar(coordenadaY,coordenadaX);
        this.pontos.setText(Integer.toString(pontos));
        this.pokebolas.setText(Integer.toString(pokebolas));
        if(carga==1){
            this.carga.setText("Alta");
        }else{
            this.carga.setText("Baixa");
        }
        if(pokemonsAnterior<totalPokemons){
            eliminarPokemons(codPokeAnt);
        }
        switch (sentido) {
            case 0:
                this.sentido.setText("Norte");
                break;
            case 1:
                this.sentido.setText("Leste");
                break;
            case 2:
                this.sentido.setText("Sul");
                break;
            default:
                if(sentido==2){
                    this.sentido.setText("Oeste");
                    break; 
                } 
        }
    }
    public void apresentarLog(String log){
        this.log.setText(log);
    }
    
    public void eliminarPokemons(int codigo){
        if(regras.getMatrizElementos()[Ianterior][Janterior]!=0){
            regras.getMatrizElementos()[Ianterior][Janterior]=0;
            mapa.setComponentZOrder(pokemons.get(codigo-1), 0);
        
        }
    }
    //INFORMAÇÕES//

    public String elementosDaCasa(){
        int i = regras.getPosicaoAtual()[0], j = regras.getPosicaoAtual()[1];
        Ianterior = i;
        Janterior=j;
        int ele = regras.getMatrizElementos()[i][j];
        System.out.println("Switch "+ele);
        String comando = "decidirAcao";
        switch(ele){
            case 0:
                System.out.println("Movimentar");
                comando="decidirAcao";
                break;
            case CENTRO:
                System.out.println("Centro");
                comando="decidirAcao(centro)";
                break;
            case LOJA:
                System.out.println("Loja");
                comando="decidirAcao(loja)";
                break;
            case TREINADOR:
                System.out.println("Treinador");
                comando="decidirAcao(treinador)";
                break;
            case PERFUME:
                System.out.println("Perfume");
                comando="decidirAcao(perfumeJoy)";
                break;
            case PROPAGANDA_BOLAS:
                System.out.println("Propaganda Bolas");
                comando="decidirAcao(ouvirVendedor)";
                break;
            case DESAFIO:
                System.out.println("Desafio");
                comando="decidirAcao(gritoTreinador)";
                break;
            default:
                if(ele>=1 && ele<=150){
                    System.out.println("Pokemon: "+ele);
                    comando="decidirAcao("+regras.getListaPokemons().get(ele-1).stringToProlog()+")";
                    pokedex.setText(regras.getListaPokemons().get(ele-1).toString());
                    codPokeAnt=ele;
                    System.out.println(" ");
                }
                break;
        }
        return comando;
    }
    //GET E SET//

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public int getPosiX() {
        return posiX;
    }

    public void setPosiX(int posiX) {
        this.posiX = posiX;
    }

    public int getPosiY() {
        return posiY;
    }

    public void setPosiY(int posiY) {
        this.posiY = posiY;
    }

    public Regras getRegras() {
        return regras;
    }

    public void setRegras(Regras regras) {
        this.regras = regras;
    }

    private void adicionar(Component ob){
        this.add(ob);
    }
    private void posicionarPersonagem(int i, int j){
        int indexTerrenos = (i*42)+j;
        this.terrenos.get(indexTerrenos).setIcon(tipoAvat[this.getRegras().getMatrizTerreno()[i][j]]);
        regras.setPosicaoAtual(i, j);
    }
    private boolean movimentar(int sentidoLin, int sentidoCol){
        if((sentidoLin<0) && (sentidoCol<0)){return false;}
        int lin = this.regras.getPosicaoAtual()[0], col = this.regras.getPosicaoAtual()[1];
        int indexTerrenos = (lin*42)+col;
        this.terrenos.get(indexTerrenos).setIcon(tipoter[this.getRegras().getMatrizTerreno()[lin][col]]);
        posicionarPersonagem(sentidoLin, sentidoCol);
        return true;
    }
    //ÍCONES//
    private void defineTipos(){ //Carrega as imagens para os icones que serão usados nos labels;
        tipoter[0] = new ImageIcon(getClass().getResource("imagens/grama.png"));
        tipoter[1] = new ImageIcon(getClass().getResource("imagens/agua.png"));
        tipoter[2] = new ImageIcon(getClass().getResource("imagens/montanha.png"));
        tipoter[3] = new ImageIcon(getClass().getResource("imagens/caverna.png"));
        tipoter[4] = new ImageIcon(getClass().getResource("imagens/vulcao.png"));
    }
    
    private void defineAvatar(){ //Carrega as imagens para os icones que serão usados nos labels quando troca o avatar;
        tipoAvat[0] = new ImageIcon(getClass().getResource("imagens/avatarGrama.png"));
        tipoAvat[1] = new ImageIcon(getClass().getResource("imagens/avatarAgua.png"));
        tipoAvat[2] = new ImageIcon(getClass().getResource("imagens/avatarMontanha.png"));
        tipoAvat[3] = new ImageIcon(getClass().getResource("imagens/avatarCaverna.png"));
        tipoAvat[4] = new ImageIcon(getClass().getResource("imagens/avatarVulcao.png"));
    }
    
    private void defineElementos(){ //Carrega as imagens para os icones que serão usados nos labels quando troca o avatar;
        tipoEle[0] = new ImageIcon(getClass().getResource("imagens/centropoke.png"));
        tipoEle[1] = new ImageIcon(getClass().getResource("imagens/lojapoke.png"));
        tipoEle[2] = new ImageIcon(getClass().getResource("imagens/treinadorpoke.png"));
    }
    
    private ImageIcon definePokemon(int numPok){
        ImageIcon pokemon = new ImageIcon(getClass().getResource("imagens/pokemons/"+numPok+"MS.png"));
        return pokemon;
    }
    //GRÁFICOS//
    public void geraInterface(){
        this.setSize(getLargura(), getAltura());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        adicionarMapa();
        introduzPokemons(42, 42);
        introduzElementos(42, 42);
        this.setLayout(null);
        this.setVisible(true);     
        
    }
    private void adicionarMapa(){
        
        mapa.setSize(getLargura()-780, getAltura()-60);
        mapa.setLocation(getLargura()-(getLargura()-370), getAltura()-(getAltura()-15));
        mapa.setBackground(Color.black);
        mapa.setLayout(null);
        geraTerrenos(mapa, 42, 42);
        adicionar(mapa);
    }
    
    private void geraTerrenos(JPanel painel, int lin, int col){
        setPosiX(0);
        setPosiY(0);
        for(int i=0; i<lin; i++){
            for(int j=0; j<col; j++){
                JLabel terreno = new JLabel(tipoter[this.getRegras().getMatrizTerreno()[i][j]]);
                terreno.setBounds(getPosiX(), getPosiY(), 14, 16);
                terrenos.add(terreno);
                painel.add(terreno);
                setPosiX(getPosiX()+14);
            }
            setPosiX(0);
            setPosiY(getPosiY()+16);
        }
    }
    public void introduzPokemons(int lin, int col){
        for(int i=0; i<lin; i++){
            for(int j=0; j<col; j++){
                if((regras.getMatrizElementos()[i][j]>0) && (regras.getMatrizElementos()[i][j]<151)){
                    JLabel pokemon = new JLabel(definePokemon(regras.getMatrizElementos()[i][j]));
                    pokemon.setBounds(terrenos.get((i*42)+j).getBounds());
                    this.mapa.add(pokemon);
                    mapa.setComponentZOrder(pokemon, 0);
                    pokemons.add(pokemon);
                }
            }
        }
    }
    
    public void introduzElementos(int lin, int col){
        for(int i=0; i<lin; i++){
            for(int j=0; j<col; j++){
                if((regras.getMatrizElementos()[i][j]>151) && (regras.getMatrizElementos()[i][j]<155)){
                    JLabel pokemon = new JLabel(tipoEle[(regras.getMatrizElementos()[i][j])-152]);
                    pokemon.setBounds(terrenos.get((i*42)+j).getBounds());
                    this.mapa.add(pokemon);
                    mapa.setComponentZOrder(pokemon, 0);
                }
            }
        }
    }
}
