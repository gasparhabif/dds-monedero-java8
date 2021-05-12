
# Code Smells

## Clase Cuenta
0. Tanto en `poner` como en `sacar` se invoca a `agregateA` cuando simplemente podria agregarse el movimiento a la lista de movimientos
1. El constructor que iguala saldo a 0 es innecesario, pues el saldo ya comienza en   0 y existe otro constructor para crear una cuenta con un monto específico.
2. El método `setMovimientos` no se utiliza nunca, además no tiene demasiado sentido que la lista de movimientos sea mutable, ya que se utiliza como un historial de movimientos. En este caso `movimientos` podria pasar a ser `final`
3. Tanto `poner` como `sacar` tienen lógica bastante similar, se realizan validaciones con respecto a la operación, y luego se agrega un movimiento a la lista de movimientos. Podría simplificarse aplicando alguna de las estrategias vistas en clase.
#### En el método `Poner`
4. Duplicación de código, pues la validación de `cuanto <= 0` existe también en el método `sacar`.
5. No se delega a funciones auxiliares, quedando así una complejidad innecesaria, donde se están cumpliendo múltiples funciones en lugar de una sola.
6. La segunda validación utiliza un `getter` para una variable que es propia de la clase. Además el límite de depósitos esta hardcodeado, haciendo engorroso un cambio futuro.
7. Delega el agregado de un movimiento a la clase Movimiento, sin demasiado sentido, pues esta vuelve a invocar a la clase Cuenta para agregarlo a la lista de movimientos, perteneciente a esta última clase.

#### En el método `Sacar`
8. Duplicación de código, pues la validación de cuanto <= 0 existe también en el método `poner`
9. Al igual que en el método poner, no se delega a funciones auxiliares.
10. Nuevamente la segunda validación utiliza un getter innecesario
11. Al momento de calcular el límite se utiliza el número 1000 de manera hardcodeada, y luego se vuelve a utilizar en la   excepción. Este monto podría ser extraído a una variable estática, para que en un futuro, ante la necesidad de cambiar el límite, sea una sola variable lo que haya que modificar.
12. Dentro del método `getMontoExtraidoA` se realiza un filtrado para saber si es depósito y si es del dia de hoy, dicha responsabilidad debería corresponder a la clase de Movimiento.

## Clase Movimiento
13. Los métodos `fueDepositado` y `fueExtraido` no son utilizados nunca, sin embargo habrían sido muy útiles para resolver el filtrado mencionado en el punto 12.
14. El método `agregateA` no tiene sentido alguno por lo explicado anteriormente.
15.  `calcularValor` no es una responsabilidad que corresponda al movimiento, debería ser `Cuenta` quien conozca y resuelva este asunto.
