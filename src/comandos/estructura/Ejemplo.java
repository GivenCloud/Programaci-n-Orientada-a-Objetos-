package comandos.estructura;

public class Ejemplo {
	public static void main(String[] args) {
		Escenario e = new Escenario(5,9, "Escenario");
		Posicion p = new Posicion(8,3);
		Posicion p2 = new Posicion(2,2);
		
		System.out.println(e.estaCerrado(8, 3));
		System.out.println(e.estaCerrado(2, 2));
		System.out.println(e.estaCerrado(p));
		System.out.println(e.estaCerrado(p2));
		System.out.println(e.posiblesDirecciones(0,1));
	}
}
