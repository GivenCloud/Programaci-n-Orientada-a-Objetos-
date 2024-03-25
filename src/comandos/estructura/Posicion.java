package comandos.estructura;

import java.util.Objects;

public class Posicion {

	private final int x;
	private final int y;
	
	public Posicion(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Posicion adyacente(Direccion dir) {
		if(dir==Direccion.ARRIBA) {
			Posicion posAdyacente = new Posicion(x,y+1);
			return posAdyacente;
		}else if(dir==Direccion.ABAJO) {
			Posicion posAdyacente = new Posicion(x,y-1);
			return posAdyacente;
		}else if(dir==Direccion.DERECHA) {
			Posicion posAdyacente = new Posicion(x+1,y);
			return posAdyacente;
		}else { //Direccion.IZQUIERDA
			Posicion posAdyacente = new Posicion(x-1,y);
			return posAdyacente;
		}	
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Posicion other = (Posicion) obj;
		return x == other.x && y == other.y;
	}

}
