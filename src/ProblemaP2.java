import java.util.*;
import java.io.*;

public class ProblemaP2 {
    /*
     * Autores: Juan Felipe Ochoa y Juan Felipe Hortúa
     * 
     */

    static class UnionFind {
        private int[] padre;
        private int[] rango;

        public UnionFind(int n) {
            padre = new int[n+1];
            rango = new int[n+1];
            for (int i=0; i<=n; i++) {
                padre[i] = i;
                rango[i] = 0;
            }
        }

        public int find(int u){
            if (padre[u] != u) {
                padre[u] = find(padre[u]); // utilizamos path comprassion
            }
            return padre[u];
        }

        public void union(int u, int v){
            int raizU = find(u);
            int raizV = find(v);

            if (raizU != raizV){
                // se utiliza una unión por rango
                if (rango[raizU] < rango[raizV]){
                    padre[raizU] = raizV;
                } else if (rango[raizU] > rango[raizV]){
                    padre[raizV] = raizU;
                } else {
                    padre[raizV] = raizU;
                    rango[raizU]++;
                }
            }
        }

        public boolean connected(int u, int v){
            return find(u) == find(v);
        }
    }

    private static boolean esRedundante(UnionFind fibra, UnionFind coaxial, int n){
        
        // conjuntos para contar componentes conexas
        Map<Integer, Integer> componentesFibra = new java.io.HashMap();
        Map<Integer, Integer> componentesCoaxial = new HashMap();

        // se agrupan los nodos por componentes en fibra y en coaxial
        for (int i=1; i<=n; i++){
            int raizFibra = fibra.find(i);
            componentesFibra.putIfAbsent(raizFibra,new HashSet<>());
            componentesFibra.get(raizFibra).add(i);
        }  

        for (int i=1; i<=n; i++){
            int raizCoaxial = coaxial.find(i);
            componentesCoaxial.putIfAbsent(raizCoaxial,new HashSet<>());
            componentesCoaxial.get(raizCoaxial).add(i);
        }

        // verificamos que cada componente de fibra corresponda a uno coazial
        for (Set<Integer> compFibra: componentesFibra.values()){
            if (compFibra.size()>1){
                int nodoRefer = compFibra.iterator().next();
                int raizCoaxialRef = coaxial.find(nodoRefer);
                Set<Integer> compCoaxilaCorrespondiente = componentesCoaxial.get(raizCoaxialRef);

                if(!compCoaxilaCorrespondiente.containsAll(compFibra)){
                    return false;
                }
                if (compCoaxilaCorrespondiente.size() != compFibra.size()){
                    return false;
                }
            }
        }

        for (Set<Integer> compCoaxial: componentesCoaxial.values()){
            if (compCoaxial.size()>1){
                int nodoRefer = compCoaxial.iterator().next();
                int raizFibraRef = fibra.find(nodoRefer);
                Set<Integer> compFibraCorrespondiente = componentesFibra.get(raizFibraRef);

                if(!compFibraCorrespondiente.containsAll(compCoaxial)){
                    return false;
                }
                if (compFibraCorrespondiente.size() != compCoaxial.size()){
                    return false;
                }
            }
        }
        return true;
    }   

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());

        StringBuilder output = new StringBuilder();

        for (int t = 0; t<T; t++){
            String[] firstLine = br.readLine().trim().split(" ");
            int N = Integer.parseInt(firstLine[0]);
            int M = Integer.parseInt(firstLine[1]);

            UnionFind fibra = new UnionFind(N);
            UnionFind coaxial = new UnionFind(N);   

            StringBuilder resultado = new StringBuilder();

            for (int m = 0; m<M; m++){
                String[] conexion = br.readLine().trim().split(" ");
                int i = Integer.parseInt(conexion[0]);
                int j = Integer.parseInt(conexion[1]);  
                int k = Integer.parseInt(conexion[2]);

                if (k == 1){
                    fibra.union(i, j);
                } else {
                    coaxial.union(i, j);
                }

                if (esRedundante(fibra, coaxial, N)){
                    resultado.append("1");
                } else {
                    resultado.append("0");
                }

                if (m < M-1){
                    resultado.append(" ");
                } 
            }

            output.append(resultado.toString());
            if (t < T-1){
                output.append("\n");
            }
        }

        System.out.print(output.toString());
        
    }
    
}
