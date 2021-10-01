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
	
	public boolean cmdProduct(String json) {
		ObjectMapper mapper = new ObjectMapper();
        List<Prodotto> prodotti = new ArrayList<Prodotto>();
        boolean result_send = false;
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
        result_send = cassaService.sendProdotti(prodotti);
        if(result_send)
        	result_send = cassaService.sendSubtotale();
        if(cassaService.getPort()!=null) {
			try {
				cassaService.getPort().closePort();
			} catch (SerialPortException e) {
				result_send = false;
				Logger.getLogger(Product_SerialControl.class.getName()).log(Level.SEVERE, null, e);
			}
        }
        
        return result_send;
	}

}
