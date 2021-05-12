package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;
  private LocalDate hoy;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
    hoy = LocalDate.now();
  }

  @Test
  void Poner() {
    double monto = 1500;
    cuenta.poner(monto);
    assertEquals(cuenta.getSaldo(), monto);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    double deposito1 = 1500;
    double deposito2 = 456;
    double deposito3 = 1900;
    double total = deposito1 + deposito2 + deposito3;

    cuenta.poner(deposito1);
    cuenta.poner(deposito2);
    cuenta.poner(deposito3);

    assertEquals(cuenta.getSaldo(), total);
    assertEquals(cuenta.getMovimientos().size(), 3);
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  void ExtraerMontoPermitido(){
    double saldo = 2500;
    double monto = 357;
    double remanente = saldo - monto;
    cuenta.setSaldo(saldo);

    cuenta.sacar(monto);

    assertEquals(cuenta.getSaldo(), remanente);
    assertEquals(monto, cuenta.getMontoExtraidoA(hoy));
    assertEquals(cuenta.getMovimientos().size(), 1);
  }

  @Test
  void MovimientoDepositadoHoy() {
    cuenta.poner(100);

    assert cuenta.getMovimientos().get(0).fueDepositado(hoy);
  }

  @Test
  void MovimientoExtraidoHoy() {
    cuenta.setSaldo(1000);

    cuenta.sacar(100);

    assert cuenta.getMovimientos().get(0).fueExtraido(hoy);
  }

}