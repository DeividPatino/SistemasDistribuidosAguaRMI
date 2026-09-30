package consumoagua.servidor;

import consumoagua.rmi.lib.DatosConsumoAgua;
import consumoagua.rmi.lib.IRemotaCalculoConsumoAgua;

public class CalculoRmiConsumoAguaImplem implements IRemotaCalculoConsumoAgua {
    @Override
    public DatosConsumoAgua calcularConsumo(DatosConsumoAgua datos) {
        if (datos.getHabitantes() <= 0) {
            throw new IllegalArgumentException("El número de habitantes debe ser mayor que cero.");
        }
        double resultado = datos.getLitros() / datos.getHabitantes();
        datos.setResultado(resultado);
        return datos;
    }
}
