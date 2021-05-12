package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private final static int MAXIMO_DEPOSITOS_PERMITIDOS = 3;
  private final static double MONTO_MAXIMO_EXTRACCION = 1000;

  private double saldo = 0;
  private final List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(double cuanto) {
    validarMontoPositivo(cuanto);
    validarDepositosDiariosPermitidos();
    agregarMovimiento(LocalDate.now(), cuanto, true);
  }

  public void sacar(double cuanto) {
    validarMontoPositivo(cuanto);
    validarSaldoSuficiente(cuanto);
    validarMontoDiario(cuanto);
    agregarMovimiento(LocalDate.now(), cuanto, false)
  }

  private void validarMontoDiario(double cuanto) {
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = MONTO_MAXIMO_EXTRACCION - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + MONTO_MAXIMO_EXTRACCION
          + " diarios, límite: " + limite);
    }
  }

  private void validarSaldoSuficiente(double cuanto) {
    if (this.saldo - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + this.saldo + " $");
    }
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  private void validarDepositosDiariosPermitidos() {
    if (this.movimientos.stream().filter(Movimiento::isDeposito).count() >= MAXIMO_DEPOSITOS_PERMITIDOS) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + MAXIMO_DEPOSITOS_PERMITIDOS + " depositos diarios");
    }
  }

  private void validarMontoPositivo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
