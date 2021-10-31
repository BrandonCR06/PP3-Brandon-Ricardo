package proyecto3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import org.jpl7.*;

/**
 *
 * @author Ricardo, Brandon
 */

public class Proyecto3 {
    
    /**
     * Clase del objeto sudoku 
     */
    public static class SudokuX {
        
        ArrayList<ArrayList<java.lang.Integer>> TableroGenerado;
        ArrayList<ArrayList<java.lang.Integer>> TableroOculto;
        ArrayList<ArrayList<java.lang.Integer>> TableroEnJuego = new  ArrayList(new ArrayList()); 
        int reinicios = 0; 
        int verificaciones = 0 ; 
        int sugerencias = 0;
        int cantCeldas = 0 ;
        int cantErroresVerificacion =0;
        
        /**
         * Método que genera el tablero y encila hasta hasta ganar el juego.
         */
        public void iniciarJuego() {
            generarTablero();
            //formatoMatriz(TableroGenerado);           //MATRIZ RESUELTA
            while (verificarGane(TableroGenerado, TableroEnJuego) == false) {
                formatoMatriz(TableroEnJuego);
                pedirPos();
                verificarGane(TableroGenerado, TableroEnJuego);
            }
            System.out.println("   ¡FELICIDADES GANASTE EL JUEGO!");
        }
        
        /**
         * Método que realiza la consulta desde el archivo de prolog
         */
        public void generarTablero() {
            String t1 = "consult('SudokuX.pl')";    //aqui colocan el nombre de su archivo a compilar
            Query q1 = new Query(t1);
            if (q1.hasSolution()) {
                System.out.println("");
            } else {
                System.out.println("Falso");
            }

            q1 = new Query("a(Z)");
            Map<String, Term> res = q1.oneSolution();
            
            Object[] list2 = res.values().toArray();
        
            String matriz = list2[0].toString();

            String replace = matriz.replace("[","");
            String replace1 = replace.replace("]","");
            List<String> arrayList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
            List<java.lang.Integer> favList = new ArrayList<java.lang.Integer>();
            for(String fav:arrayList){
                favList.add(java.lang.Integer.parseInt(fav.trim()));
            }
            
            

            TableroGenerado = convert((ArrayList<java.lang.Integer>) favList);
            TableroOculto = ocultar(convert((ArrayList<java.lang.Integer>) favList));
             
            
            
            
            for(int i = 0; i < 9; i++){
                ArrayList<java.lang.Integer> temp = new ArrayList();
                for(int j = 0 ; j < 9; j++){
                    temp.add(TableroOculto.get(i).get(j));                       
                }            
                TableroEnJuego.add(temp);
            }
             
           
            

            
            
        
        }
        public void mostrarSugerencia(){
            ArrayList<ArrayList<java.lang.Integer>> a = new ArrayList();
            
            
            for(int i =0 ; i < 9; i++){
                for(int j = 0 ; j < 9 ; j++){
                    if(TableroEnJuego.get(i).get(j)==0){
                        ArrayList<java.lang.Integer> b =  new ArrayList();
                        b.add(i);                        
                        b.add(j);                        
                        a.add(b);
                    }
                }
            }
            Random n = new Random();
            int randomIndex = n.nextInt(a.size());
            if(a.size()==0){
                System.out.println("No hay sugerencias a mostrar");
                pedirPos();
            }
            int n1 = a.get(randomIndex).get(0);
            int n2 = a.get(randomIndex).get(1);
            TableroGenerado.get(n1).get(n2);
            ArrayList<ArrayList<java.lang.Integer>> newMat = new ArrayList();
            
            
            for(int i = 0 ; i < 9 ; i++){
                ArrayList<java.lang.Integer> val = new ArrayList();
                for(int k = 0 ; k <9 ;k++){                    
                    if(n1==i && n2==k){
                        val.add(TableroGenerado.get(n1).get(n2));                        
                    } else {
                        val.add(TableroEnJuego.get(i).get(k));                        
                    }                    
                }                
                newMat.add(val);  
            }
           
            formatoMatriz(newMat);
             System.out.print("La sugerencia se encuentra en la posición: ");
            System.out.print(n1+1);
            System.out.print(',');
            System.out.println(n2+1);
            sugerencias+=1;
            pedirPos();
            
        }
        public void verificar() {
            int errores = 0  ;
            int vacias = 0 ; 
            for(int i =0 ; i < 9 ; i ++){
                for(int j = 0 ; j < 9 ; j ++){
                    if(TableroEnJuego.get(i).get(j)!=TableroGenerado.get(i).get(j) && 
                            TableroEnJuego.get(i).get(j)!=0){ 
                        errores++;
                    }
                    if(TableroEnJuego.get(i).get(j)==0){
                        vacias++;
                    }
                }                
            }
            if(vacias ==0 && errores==0){
                System.out.println("Felicidades, has ganado!");
                pedirPos();
            }
            if(vacias>0 && errores== 0 ){
                System.out.println("Hay 0 errores y " + vacias+ " celdas vacias");                
            } else {
                System.out.println("Hay "+errores+" errores y "+ vacias+ " celdas vacias");                
            }
            cantErroresVerificacion+=errores;
            verificaciones+=1;
        }
        public void stats(){
        System.out.println("Cantidad de reinicios: "+reinicios); 
        System.out.println("Cantidad de verificaciones: "+verificaciones); 
        System.out.println("Cantidad de sugerencias: "+sugerencias); 
        System.out.println("Cantidad de celdas ingresadas: "+cantCeldas); 
        System.out.println("Cantidad de errores de verificacion: "+cantErroresVerificacion); 
        formatoMatriz(TableroEnJuego);
        pedirPos();
        
            
        }
       
        /**
         * Método que solicita la posicion de las fila y valores, o las opciones de juego,
         */
        public void pedirPos() {
            System.out.println ("R = (Reiniciar) V = (Verificar) S = (Sugerencia) X = (Ver Solucion) T = (Estadisticas)");
            System.out.println ("Por favor introduzca la fila por teclado (A-I):");
            String entradaTeclado = "";
            Scanner entradaEscaner = new Scanner (System.in); 
            entradaTeclado = entradaEscaner.nextLine (); 
            if (entradaTeclado.charAt(0) >= 'A' && entradaTeclado.charAt(0)<= 'I') {
                java.lang.Integer fila = getFila(entradaTeclado.charAt(0));
                System.out.println ("Por favor introduzca la columna por teclado (1-9):");
                entradaTeclado = entradaEscaner.nextLine (); 
                if (java.lang.Integer.parseInt(entradaTeclado) >= 1 && java.lang.Integer.parseInt(entradaTeclado)<=9) {
                    java.lang.Integer columna = getColumna(java.lang.Integer.parseInt(entradaTeclado));   
                    System.out.println ("Por favor introduzca el numero que desea insertar en esa posicion (1-9):");
                    entradaTeclado = entradaEscaner.nextLine (); 
                    if (java.lang.Integer.parseInt(entradaTeclado) >= 1 && java.lang.Integer.parseInt(entradaTeclado)<=9) {
                        java.lang.Integer valor = java.lang.Integer.parseInt(entradaTeclado);
                        System.out.println("Usted indico la posicion: " + fila + "," + columna);
                        if (TableroOculto.get(fila).get(columna) == 0) {
                            TableroEnJuego.get(fila).set(columna, valor);
                            cantCeldas+=1;
                        } else {
                            System.out.println("No puedes cambiar los valores que ya vienen por defecto");
                        } 
                    } else {
                        System.out.println("Debes introducir un valor valido (1-2-3-4-5-6-7-8-9)");
                        pedirPos();
                    }
                } else {
                    System.out.println("Debes introducir una columna (1-2-3-4-5-6-7-8-9)");
                    pedirPos();
                }
            } else if (entradaTeclado.charAt(0) == 'R') {
                System.out.println("Uste eligió reiniciar el juego");
                TableroEnJuego.clear();
                for(int i = 0; i < 9; i++){
                    ArrayList<java.lang.Integer> temp = new ArrayList();
                    for(int j = 0 ; j < 9; j++){
                        temp.add(TableroOculto.get(i).get(j));                       
                    }            
                    TableroEnJuego.add(temp);
                }
                reinicios+=1;
                formatoMatriz(TableroEnJuego);
                pedirPos();
            } else if (entradaTeclado.charAt(0) == 'V') {
                
                //FUNCION VERIFICAR
                verificar();
                
            } else if (entradaTeclado.charAt(0) == 'S') {
                
                //FUNCION SUGERENCIA
                mostrarSugerencia();
                
                
            } else if (entradaTeclado.charAt(0) == 'X') {
                
                System.out.println("Solucion del Sudoku");
                formatoMatriz(TableroGenerado);
                System.exit(0);
                
            } else if (entradaTeclado.charAt(0) == 'T') {
                
               stats();
            }
           
            else {
                System.out.println("Debes introducir una fila (A-B-C-D-E-F-G-H-I) o letra válida");
                pedirPos();
            }
            
        }
    
        /**
         * Método que convierte una lista de enteros en una matriz de enteros 9 * 9
         * @param lista
         * @return
         */
        public static ArrayList<ArrayList<java.lang.Integer>> convert(ArrayList<java.lang.Integer> lista) {
            ArrayList<ArrayList<java.lang.Integer>> matriz = new  ArrayList(new ArrayList()); 
            for(int i = 0; i < 9; i++){
                ArrayList<java.lang.Integer> lista2 = new ArrayList();
                for(int j = i*9 ; j < (i*9)+9; j++){
                    lista2.add(lista.get(j));                       
                }            
                matriz.add(lista2);
            }
            return matriz;
        }

        /**
         * Método que le da formato a una matriz.
         * @param matriz
         */
        public static void formatoMatriz(ArrayList<ArrayList<java.lang.Integer>> matriz) {
            System.out.println("            ╔══╗──╔╗─╔╗───╔╗╔╗");
            System.out.println("            ║══╬╦╦╝╠═╣╠╦╦╗╚╗╔╝");
            System.out.println("            ╠══║║║╬║╬║═╣║║╔╝╚╗");
            System.out.println("            ╚══╩═╩═╩═╩╩╩═╝╚╝╚╝");
            System.out.println("     1  2  3     4  5  6     7  8  9");
            char letra = 'A'-1;
            for (int i = 0; i < 9; i++) {
                if (i % 3 == 0) {
                    System.out.println("  ══════════════════════════════════════════════");
                }
                letra++;
                System.out.print(letra);
                for (int j = 0; j < 9; j++) {
                    if (j % 3 == 0) {
                        System.out.print(" ║ ");
                    } 
                    java.lang.Integer num = matriz.get(i).get(j);
                    if (num == 0) {
                        System.out.print(" _ ");
                    } else {
                        System.out.print(" " + matriz.get(i).get(j) + " ");
                    }
                }
                System.out.println(" ║ ");
            }  
            System.out.println("  ══════════════════════════════════════════════");
        }

        /**
         * Método que copia los datos de una matriz en otra.
         * @param tablero
         * @return
         */
        public static ArrayList<ArrayList<java.lang.Integer>> copiar(ArrayList<ArrayList<java.lang.Integer>> tablero) {
            ArrayList<ArrayList<java.lang.Integer>> temp;
            temp = tablero;
            return temp;
        }

        /**
         * Método que permite ocultar casillas aleatorias del tablero
         * @param tablero
         * @return
         */
        public static ArrayList<ArrayList<java.lang.Integer>> ocultar(ArrayList<ArrayList<java.lang.Integer>> tablero) {

            ArrayList<ArrayList<java.lang.Integer>> temp;
            temp = tablero;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 12; j++) {     
                    Random indiceOcultos = new Random();
                    int n2; // 0 A 9
                    n2 = indiceOcultos.nextInt(9);
                    
                    temp.get(i).set(n2, 0);
                }
            }
            return temp;
        }

        /**
         * Método que verifica si dos matrices son iguales, lo que indicaría el gane de la partida.
         * @param tablero1
         * @param tablero2
         * @return
         */
        public static boolean verificarGane(ArrayList<ArrayList<java.lang.Integer>> tablero1, ArrayList<ArrayList<java.lang.Integer>> tablero2) {
            return tablero1.containsAll(tablero2) && tablero2.containsAll(tablero1);
        }

        /**
         * Método que obtiene la fila del teclado interpretando la letra introducida por un indice i
         * @param pos
         * @return
         */
        public static java.lang.Integer getFila(char pos) {
            java.lang.Integer fila;
            fila = pos-65;
            return fila;
        }

        /**
         * Método que obtiene la columna del teclado interpretando el numero por un indice j
         * @param pos
         * @return
         */
        public static java.lang.Integer getColumna(java.lang.Integer pos) {
            java.lang.Integer columna;
            columna = pos-1;
            return columna;
        }

    
    
    }
    
    
    
    

    /**
     * @param args
     * @PROGRAMA PRINCIPAL
     */
    public static void main(String[] args) {
        
        
        SudokuX sudoku = new SudokuX();
        sudoku.iniciarJuego();
        
    }
    
}
