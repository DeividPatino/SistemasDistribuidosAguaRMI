package consumoagua.rmi.lib;

import java.io.Serializable;

public class DatosConsumoAgua implements Serializable {
    private double litros;
    private int habitantes;
    private double resultado;

    public DatosConsumoAgua(double litros, int habitantes) {
        this.litros = litros;
        this.habitantes = habitantes;
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }

    public int getHabitantes() {
        return habitantes;
    }

    public void setHabitantes(int habitantes) {
        this.habitantes = habitantes;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }
}