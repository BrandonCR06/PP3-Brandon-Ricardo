:-use_module(library(clpfd)).          % Libreria de programación de restricciones

a(R):-                                 % Predicado principal
    l(9,R),m(l(9),R),append(R,V),      % R es una lista de 9 listas, cada una 9 elementos 
    V ins 1..9,                        % Valores en R entre 1-9
    transpose(R,C),                    % C es la transpuesta de R
    d(0,R,D),                          % D es la diagonal R
    maplist(reverse,R,S),d(0,S,E),     % E es la anti-diagonal de R
    r(R),                              % Bloques 3 * 3 deben contener números del 1 al 9
    m(m(all_distinct),[R,C,[D,E]]),    % Cada fila de R, cada fila de C, D y E debe contener
                                       % solo números distintos
    get_time(I),J is ceil(I),          % J es un numero aleatorio basado en la hora actual
    m(labeling([random_value(J)]),R).  % Encontrar aleotoriamente una solucion para R.

l(L,M):-                               % L es el largo de M
    length(M,L).

r([A,B,C|T]):-                         % Cada grupo de 3 filas, los bloques de 3 * 3 deben
    b(A,B,C),r(T);!.                   % contener distintos numeros

b([A,B,C|X],[D,E,F|Y],[G,H,I|Z]):-     % Comprobar que los 3 bloques 3 * 3 de 3 filas tengan
                                       % numeros distintos dentro de ellos
    all_distinct([A,B,C,D,E,F,G,H,I]),
    b(X,Y,Z);!.

d(X,[H|R],[A|Z]):-                     % [A|Z] es la diagonal de [H|R]
    nth0(X,H,A),
    Y is X+1,
    (R=[],Z=R;d(Y,R,Z)).

m(A,B):-
    maplist(A,B),nl.
