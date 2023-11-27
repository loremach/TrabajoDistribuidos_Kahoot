package dominio;

import java.io.Serializable;

public class Persona implements Serializable{
	
	private static final long serialVersionUID = 111L;
	private String ip;
	private int puerto;
	private String alias;
	
	public Persona(String ip, int puerto) {
		this.ip=ip;
		this.puerto=puerto;
		this.alias = "";
	}

	public String getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}
	
	public String getAlias() {
		return this.alias;
	}
	
	public void setAlias(String alias) {
		this.alias=alias;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Persona p = (Persona) o;
		return this.ip.equals(p.ip) && this.puerto == p.puerto;
	}
}
