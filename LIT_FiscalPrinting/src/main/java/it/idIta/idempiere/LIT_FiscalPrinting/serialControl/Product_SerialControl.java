package it.idIta.idempiere.LIT_FiscalPrinting.serialControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.idIta.idempiere.LIT_FiscalPrinting.model.Prodotto;
import jssc.SerialPortException;

public class Product_SerialControl {

	public Product_SerialControl() {
		
	}
	
	public void cmdProduct(String json) {
		ObjectMapper mapper = new ObjectMapper();
        List<Prodotto> prodotti = new ArrayList<Prodotto>();
        try {
            prodotti = mapper.readValue(json, new TypeReference<List<Prodotto>>() {});
            
        } catch (IOException ex) {
            Logger.getLogger(Product_SerialControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CassaServiceImpl cassaService = new CassaServiceImpl();
        if (cassaService.getPort() == null) {
            //cassaService.checkPort();
            cassaService.scanPort();
            //cassaService.configPort();
        }
        cassaService.sendProdotti(prodotti);
        cassaService.sendSubtotale();
        if(cassaService.getPort()!=null)
			try {
				cassaService.getPort().closePort();
			} catch (SerialPortException e) {
				Logger.getLogger(Product_SerialControl.class.getName()).log(Level.SEVERE, null, e);
			}
        
	}

}
