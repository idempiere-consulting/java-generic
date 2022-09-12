package it.idIta.idempiere.LIT_ServerSilosREST.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "silos")
public class Silos {
	
	@Id
	@Column(name = "idSilo")
	private long idSilo;
	@Column(name = "Nome")
	private String Nome;
	@Column(name = "idTranslation")
	private long idTranslation;
	@Column(name = "idProdotto")
	private long idProdotto;
	@Column(name = "Descrizione")
	private String Descrizione;
	@Column(name = "Codice")
	private String Codice;
	@Column(name = "DataCarico")
	private Timestamp DataCarico;
	@Column(name = "idTipo")
	private long idTipo;
	@Column(name = "Capacita_m3")
	private double Capacita_m3;
	@Column(name = "Capacita_Kg")
	private double Capacita_Kg;
	@Column(name = "Densita")
	private double Densita;
	@Column(name = "Livello_Alto")
	private double Livello_Alto;
	@Column(name = "Livello_Basso")
	private double Livello_Basso;
	@Column(name = "Livello")
	private double Livello;
	@Column(name = "state")
	private boolean state;
	
	public Silos() {}
	
	public Silos(long idSilo, String nome, long idTranslation, long idProdotto, String descrizione, String codice,
			Timestamp dataCarico, long idTipo, double capacita_m3, double capacita_Kg, double densita,
			double livello_Alto, double livello_Basso, double livello, boolean state) {
		super();
		this.idSilo = idSilo;
		Nome = nome;
		this.idTranslation = idTranslation;
		this.idProdotto = idProdotto;
		Descrizione = descrizione;
		Codice = codice;
		DataCarico = dataCarico;
		this.idTipo = idTipo;
		Capacita_m3 = capacita_m3;
		Capacita_Kg = capacita_Kg;
		Densita = densita;
		Livello_Alto = livello_Alto;
		Livello_Basso = livello_Basso;
		Livello = livello;
		this.state = state;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public long getIdTranslation() {
		return idTranslation;
	}

	public void setIdTranslation(long idTranslation) {
		this.idTranslation = idTranslation;
	}

	public long getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(long idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getDescrizione() {
		return Descrizione;
	}

	public void setDescrizione(String descrizione) {
		Descrizione = descrizione;
	}
	
	public String getCodice() {
		return Codice;
	}

	public void setCodice(String codice) {
		Codice = codice;
	}

	public Timestamp getDataCarico() {
		return DataCarico;
	}

	public void setDataCarico(Timestamp dataCarico) {
		DataCarico = dataCarico;
	}

	public long getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(long idTipo) {
		this.idTipo = idTipo;
	}

	public double getCapacita_m3() {
		return Capacita_m3;
	}

	public void setCapacita_m3(double capacita_m3) {
		Capacita_m3 = capacita_m3;
	}

	public double getCapacita_Kg() {
		return Capacita_Kg;
	}

	public void setCapacita_Kg(double capacita_Kg) {
		Capacita_Kg = capacita_Kg;
	}

	public double getDensita() {
		return Densita;
	}

	public void setDensita(double densita) {
		Densita = densita;
	}

	public double getLivello_Alto() {
		return Livello_Alto;
	}

	public void setLivello_Alto(double livello_Alto) {
		Livello_Alto = livello_Alto;
	}

	public double getLivello_Basso() {
		return Livello_Basso;
	}

	public void setLivello_Basso(double livello_Basso) {
		Livello_Basso = livello_Basso;
	}

	public double getLivello() {
		return Livello;
	}

	public void setLivello(double livello) {
		Livello = livello;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public long getIdSilo() {
		return idSilo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Silos [idSilo=");
		builder.append(idSilo);
		builder.append(", Nome=");
		builder.append(Nome);
		builder.append(", idTranslation=");
		builder.append(idTranslation);
		builder.append(", idProdotto=");
		builder.append(idProdotto);
		builder.append(", Descrizione=");
		builder.append(Descrizione);
		builder.append(", DataCarico=");
		builder.append(DataCarico);
		builder.append(", idTipo=");
		builder.append(idTipo);
		builder.append(", Capacita_m3=");
		builder.append(Capacita_m3);
		builder.append(", Capacita_Kg=");
		builder.append(Capacita_Kg);
		builder.append(", Densita=");
		builder.append(Densita);
		builder.append(", Livello_Alto=");
		builder.append(Livello_Alto);
		builder.append(", Livello_Basso=");
		builder.append(Livello_Basso);
		builder.append(", Livello=");
		builder.append(Livello);
		builder.append(", state=");
		builder.append(state);
		builder.append("]");
		return builder.toString();
	}

	
}
