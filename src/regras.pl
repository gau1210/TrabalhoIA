%Movimentar.
  :-dynamic coordenadas.
  :-dynamic sentido.
 %Pokemon(NOME, CODIGO, TIPO1, TIPO2, TIPO3, TIPO_TERRENO).
  :-dynamic pokemon.
 %Mapa(COORDENADA_X, COORDENADA_Y, TIPO_TERRENO).
  :-dynamic mapa.
 %Conhecimento das coordenadas que ja passou.
 %mapaExplorado(COORDENADA_X, COORDENADA_Y).
 %:-dynamic mapaExplorado.
 %Pokebolas.
  :-dynamic pokebolas.
 %Energia dos Pokemons
  :-dynamic energia.
 %Quantidade de Pokemons capturados.
  :-dynamic totalPokemons.
 %Pontuação total.
  :-dynamic pontos.
 %Fatos correspondentes aos sensores e perceber o ambiente
  estimulo(gritoTreinador). %gritos do treinador
  estimulo(perfumeJoy). %perfume de joy
  estimulo(ouvirVendedor). %agente ouve o vendedor oferencendo pokemons
 %Elementos do ambiente
  elementosAmbiente(centro).
  elementosAmbiente(loja).
  elementosAmbiente(treinador).
 %Tipos de terreno.
  terreno(grama).
  terreno(agua).
  terreno(montanha).
  terreno(caverna).
  terreno(vulcao).
 %Tipos que de acesso a outros terrenos
  acesso('voo').
  acesso('agua').
  acesso('eletrico').
  acesso('fogo').

%Regras de movimento

 verificarGiro(SO,SD):-(SO=\=SD),(SO>SD->(giraDireita);((SO<SD)->(giraEsquerda))).%Precisa ser recursivo para girar quantas vezes for necessário.
 sentidoDesejado(_,Y,_,W,SD,SO):-(Y=\=W),((Y>W)->SD is 0;((Y<W)-> SD is 2;SD is SO)).
 sentidoDesejado(X,_,K,_,SD,SO):-(X=\=K),((X>K)->SD is 3;((X<K)-> SD is 1;SD is SO)).

%Passar por terrenos de acorodo com o pokemon.
 possiveis_caminhos_proximos(K,W):-
                 coordenadas(X,Y),
                 pokemon(_,_,_,_,_,Z),
                 ((K is X, W is Y+1);
                  (K is X, W is Y-1);
                  (K is X+1, W is Y);
                  (K is X-1, W is Y)),
                 mapa(K,W,Z).
% Para passar por terreno de grama sem pokemons ou os de grama.
 possiveis_caminhos_proximos(K,W):-
                 coordenadas(X,Y),
                 ((K is X, W is Y+1);
                  (K is X, W is Y-1);
                  (K is X+1, W is Y);
                  (K is X-1, W is Y)),
                 mapa(K,W,grama).

%Para verificar os caminhos segundo o sentido.
 segue_sentido_direto(K,W,3):-coordenadas(X,Y),(K is X-1, W is Y).
 segue_sentido_direto(K,W,2):-coordenadas(X,Y),(K is X, W is Y+1).
 segue_sentido_direto(K,W,1):-coordenadas(X,Y),(K is X+1, W is Y).
 segue_sentido_direto(K,W,0):-coordenadas(X,Y),(K is X, W is Y-1).
 segue_sentido(K,W):-
         sentido(X),
         segue_sentido_direto(K,W,X),
         possiveis_caminhos_proximos(K,W).

 operacao:-
          segue_sentido(K,W),
          armazenaCoordenada(K,W),
          decrementarPontos(1).
 operacao:-
          coordenadas(X,Y),
          sentido(SO),
          possiveis_caminhos_proximos(K,W),
          sentidoDesejado(X,Y,K,W,SD,SO),
          verificarGiro(SO,SD),
          decrementarPontos(1).

 decidirAcao:-operacao.

%Capturar Pokemon

%Armazenar pokemons por terreno.
 classificaPokeTerreno(NOME,COD,T1,T2,T3):-(acesso(T1),T1='fogo')->(asserta(pokemon(NOME,COD,fogo,T2,T3,vulcao))).
 classificaPokeTerreno(NOME,COD,T1,T2,T3):-(acesso(T1),T1='agua')->(asserta(pokemon(NOME,COD,agua,T2,T3,agua))).
 classificaPokeTerreno(NOME,COD,T1,T2,T3):-(acesso(T1),T1='eletrico')->(asserta(pokemon(NOME,COD,eletrico,T2,T3,caverna))).
 classificaPokeTerreno(NOME,COD,T1,T2,T3):-(acesso(T1),T1='voo')->(asserta(pokemon(NOME,COD,voo,T2,T3,montanha))).
 armazenaPoke(NOME,COD,T1,T2,T3):-
                                   ((acesso(T1)),(acesso(T2)),(acesso(T3)))->
                                                                            (pokemon(_,_,_,_,_,_));
                                                                            (assertz(pokemon(NOME,COD,T1,T2,T3,-))).

 incrementarPokemons:-totalPokemons(T), NOVOTOTAL is T +1, setarTotalPokemons(NOVOTOTAL).

 setarPokemon(NOME,COD,T1,T2,T3):-
                                 ((classificaPokeTerreno(NOME,COD,T1,T2,T3);
                                   classificaPokeTerreno(NOME,COD,T2,T1,T3);
                                   classificaPokeTerreno(NOME,COD,T3,T2,T1));
                                   armazenaPoke(NOME,COD,T1,T2,T3)),
                                 incrementarPokemons.

%Para capturar pokemons.
 capturar(NOME,COD,T1,T2,T3):-
          pokebolas(W),(W>0),
          setarPokemon(NOME,COD,T1,T2,T3),
          (K is W-1),
          setarPokebolas(K),
          decrementarPontos(5).



 decidirAcao(NOMEPOKEMON,COD,TIPO1,TIPO2,TIPO3):-
                                               (COD > 0)->
                                                         capturar(NOMEPOKEMON,COD,TIPO1,TIPO2,TIPO3),
                                                         decidirAcao.

%Centro Pokemon

 recarregarEnergia:-
                   energia(Carga),
                   ((Carga=\=1)->
                                (setarEnergia(1),
                                 decrementarPontos(100));
                                (energia(_))
                   ).

 decidirAcao(centro):-
                    (elementosAmbiente(centro))->
                                                recarregarEnergia,
                                                decidirAcao.
%Loja
 recarregarPokebolas:-
               pokebolas(X),
               Z is X +25,
               setarPokebolas(Z),
               decrementarPontos(10).

decidirAcao(loja):-
                  (elementosAmbiente(loja))->
                                             recarregarPokebolas,
                                             decidirAcao.

%Treinador Ações
 batalha(GouP):-
              totalPokemons(Qtd),
              energia(Carga),
              (Qtd>0)->
                       ((Carga=:=1)->
                                    (GouP is 2);
                                    (GouP is 1)
                       ).
 batalha(GouP):-
              totalPokemons(Qtd),
              ((Qtd=:=0)->
                        GouP is 0;
                        GouP is -1
              ).
 batalhar:-
          batalha(GouP),
          ((GouP>0)->
                   (((GouP>1)->
                             incrementarPontos(150);
                             decrementarPontos(1000)),
                     setarEnergia(0)
                   );
                   (pontos(_))).


decidirAcao(treinador):-
                       (elementosAmbiente(treinador))->
                                                      batalhar,
                                                      decidirAcao.

decidirAcao(gritoTreinador):-
                            (estimulo(gritoTreinador))->
                                                       decidirAcao.

decidirAcao(perfumeJoy):-
                        (estimulo(perfumeJoy))->
                                               decidirAcao.

decidirAcao(ouvirVendedor):-
                           (estimulo(ouvirVendedor))->
                                                     decidirAcao.



%Informaçoes para o java.

 passarInformacoes(CoordenadaX, CoordenadaY, Pontos, Pokebolas, Carga, TotalPokemons,UltCapturado, Sentido):-
                  coordenadas(CoordenadaX, CoordenadaY),
                  pontos(Pontos),
                  pokebolas(Pokebolas),
                  energia(Carga),
                  totalPokemons(TotalPokemons),
                  pokemon(_,UltCapturado,_,_,_,_),
                  sentido(Sentido).

%Setar valores

  armazenarTerrenos(X,Y,Z):-limites(X,Y),asserta(mapa(X,Y,Z)).
  setarCoordenadas(X,Y):-limites(X,Y), asserta(coordenadas(X,Y)).
  setarSentido(X):-X>=0, X<4, asserta(sentido(X)).
  setarPokebolas(X):-asserta(pokebolas(X)).
  setarEnergia(Carga):-asserta(energia(Carga)).
  setarTotalPokemons(Quantidade):-asserta(totalPokemons(Quantidade)).
  setarPontos(Pontos):-asserta(pontos(Pontos)).

%Icrementar ou Decrementar pontos

 incrementarPontos(Incremento):-
                               pontos(Pontos),
                               NovosPontos is Incremento +Pontos,
                               setarPontos(NovosPontos).
 decrementarPontos(Decremento):-
                               pontos(Pontos),
                               NovosPontos is Pontos - Decremento,
                               setarPontos(NovosPontos).

%Regra para inicializar
 inicializar:-
             setarCoordenadas(19,24),
             setarSentido(2),
             setarPokebolas(25),
             setarEnergia(1),
             setarTotalPokemons(0),
             setarPontos(0),
             assertz(pokemon(tes,0,tes,tes,tes,tes)).

%Regras para armazenar coordenadas
 armazenaExplorado:-
                   coordenadas(X,Y),
                   asserta(mapaExplorado(X,Y)).
 limpaCoordenadas:-retractall(coordenadas(_,_)).
 armazenaCoordenada(X,Y):-
                         armazenaExplorado,
                         limpaCoordenadas,
                         setarCoordenadas(X,Y).
 limites(X,Y):-X<42,X>=0,Y<42,Y>=0.


%Regras Para girar
 limpaSentido:-retractall(sentido(_)).
 sentidoEmCicloEsq(X):-
                      X<4->
                          asserta(sentido(X));
                          asserta(sentido(0)).
 sentidoEmCicloDir(X):-
                      X>=0->
                           asserta(sentido(X));
                           asserta(sentido(3)).
 giraEsquerda:-
              sentido(Y),
              Z is 1 + Y ,
              limpaSentido,
              sentidoEmCicloEsq(Z).
 giraDireita:-
             sentido(Y),
             Z is -1 + Y,
             limpaSentido,
             sentidoEmCicloDir(Z).



















