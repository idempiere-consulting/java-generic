/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.idIta.idempiere.LIT_FiscalPrinting.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"id",
	"codice_prodotto",
    "nome",
	"bar_code_conf",
    "id_iva",
    "consistenza",
    "prezzo",
    "id_cat_merc",
    "qta"
})
public class Prodotto implements Serializable {

	@JsonProperty("id")
	private int id;
    @JsonProperty("codice_prodotto")
    private String codiceProdotto;
    @JsonProperty("nome")
    private String nome;
    @JsonProperty("bar_code_conf")
    private String barcode;
    @JsonProperty("id_iva")
    private String idIva;
    @JsonProperty("consistenza")
    private int consistenza;
    @JsonProperty("prezzo")
    private String prezzo;
    @JsonProperty("id_cat_merc")
    private String idCatMerc;
    @JsonProperty("qta")
    private String qta;

//    @Transient
//    private String $$hashKey;
    
    public Prodotto() {
    }

    public Prodotto(int idProd, String codiceProdotto, String nome, String barcode, int consistenza, String prezzo, String idCatMerc, String iva) {
    	this.id = idProd;
        this.codiceProdotto = codiceProdotto;
        this.nome = nome;
        this.barcode = barcode;
        this.consistenza = consistenza;
        this.prezzo = prezzo;
        this.idCatMerc = idCatMerc;
        this.idIva = iva;
    }

    public Prodotto(int idProd, String codiceProdotto, String nome, String barcode, int consistenza, String prezzo, String idCatMerc, String iva, String qta, String $$hashKey) {
    	this.id = idProd;
        this.codiceProdotto = codiceProdotto;
        this.nome = nome;
        this.barcode = barcode;
        this.consistenza = consistenza;
        this.prezzo = prezzo;
        this.idCatMerc = idCatMerc;
        this.idIva = iva;
        this.qta = qta;
        //this.$$hashKey = $$hashKey;
    }

    
//    public String get$$hashKey() {
//        return $$hashKey;
//    }
//    
//    public void set$$hashKey(String $$hashKey) {
//        this.$$hashKey = $$hashKey;
//    }
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }
    @JsonProperty("qta")
    public String getQta() {
        return qta;
    }
    @JsonProperty("qta")
    public void setQta(String qta) {
        this.qta = qta;
    }
    @JsonProperty("id_cat_merc")
    public String getIdCatMerc() {
        return idCatMerc;
    }
    @JsonProperty("id_cat_merc")
    public void setIdCatMerc(String idCatMerc) {
        this.idCatMerc = idCatMerc;
    }
    @JsonProperty("codice_prodotto")
    public String getCodiceProdotto() {
        return codiceProdotto;
    }
    @JsonProperty("codice_prodotto")
    public void setCodiceProdotto(String codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }
    @JsonProperty("nome")
    public String getNome() {
        return nome;
    }
    @JsonProperty("nome")
    public void setNome(String nome) {
        this.nome = nome;
    }
    @JsonProperty("bar_code_conf")
    public String getBarcode() {
        return barcode;
    }
    @JsonProperty("bar_code_conf")
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    @JsonProperty("id_iva")
    public String getIdIva() {
        return idIva;
    }
    @JsonProperty("id_iva")
    public void setIdIva(String idIva) {
        this.idIva = idIva;
    }
    @JsonProperty("consistenza")
    public int getConsistenza() {
        return consistenza;
    }
    @JsonProperty("consistenza")
    public void setConsistenza(int consistenza) {
        this.consistenza = consistenza;
    }
    @JsonProperty("prezzo")
    public String getPrezzo() {
        return prezzo;
    }
    @JsonProperty("prezzo")
    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

}
