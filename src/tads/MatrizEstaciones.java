/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tads;



/**
 *
 * @author ljgp2
 */
public class MatrizEstaciones {

    private String[][] mapa;

    public MatrizEstaciones(String[][] mapa) {
        this.mapa = mapa;
    }

    public boolean esValido() {
        return mapa != null && mapa.length > 0;
    }

    public int[] contarEstacionesPorFila() {
        if (!esValido()) return new int[0];

        int filas = mapa.length;
        int columnas = 0;
        for (String[] fila : mapa) if (fila != null) columnas = Math.max(columnas, fila.length);

        int[] conteo = new int[filas];

        for (int i = 0; i < filas; i++) {
            if (mapa[i] == null) continue; // ignorar fila nula
            for (int j = 0; j < mapa[i].length; j++) {
                if (esEstacion(mapa[i][j])) conteo[i]++;
            }
        }

        return conteo;
    }

    public int[] contarEstacionesPorColumna() {
        if (!esValido()) return new int[0];

        int filas = mapa.length;
        int columnas = 0;
        for (String[] fila : mapa) if (fila != null) columnas = Math.max(columnas, fila.length);

        int[] conteo = new int[columnas];

        for (int j = 0; j < columnas; j++) {
            for (int i = 0; i < filas; i++) {
                if (mapa[i] != null && j < mapa[i].length && esEstacion(mapa[i][j])) conteo[j]++;
            }
        }

        return conteo;
    }

    public boolean hayTresColumnasAscendentes() {
        int[] conteoCol = contarEstacionesPorColumna();
        if (conteoCol.length < 3) return false;
        for (int j = 0; j < conteoCol.length - 2; j++) {
            if (conteoCol[j] < conteoCol[j + 1] && conteoCol[j + 1] < conteoCol[j + 2])
                return true;
        }
        return false;
    }

    public int maximoFila() {
        int[] filas = contarEstacionesPorFila();
        if (filas.length == 0) return 0;
        int m = 0;
        for (int f : filas) if (f > m) m = f;
        return m;
    }

    public int maximoColumna() {
        int[] col = contarEstacionesPorColumna();
        if (col.length == 0) return 0;
        int m = 0;
        for (int c : col) if (c > m) m = c;
        return m;
    }

    private boolean esEstacion(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
}
