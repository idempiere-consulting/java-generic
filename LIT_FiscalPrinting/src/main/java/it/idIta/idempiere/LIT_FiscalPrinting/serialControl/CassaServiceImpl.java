/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.idIta.idempiere.LIT_FiscalPrinting.serialControl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import it.idIta.idempiere.LIT_FiscalPrinting.model.Prodotto;
//import gnu.io.CommPortIdentifier;
//import gnu.io.PortInUseException;
//import gnu.io.SerialPort;
//import gnu.io.SerialPortEvent;
//import gnu.io.SerialPortEventListener;
//import gnu.io.UnsupportedCommOperationException;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author pilo
 */
@Service
public class CassaServiceImpl {

    List<String> listaPorte;
    private SerialPort sp;
    static final int BAUDRATE = 9600;

    
/*
    public Boolean checkPort() {
        Enumeration tmpports = CommPortIdentifier.getPortIdentifiers();
        if (tmpports != null) {
            return tmpports.hasMoreElements();
        }
        return false;

    }
*/
    public void scanPort() {
    	listaPorte = new ArrayList<String>();
    	for(String port : SerialPortList.getPortNames()) {
    		Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.INFO, "PORT::: "+port, "PORT::: "+port);
    		//System.out.println("PORT::: "+port);
    		if(port!=null && port.contains("/dev/ttyUSB")) {
                    listaPorte.add(port);
                    sp = new SerialPort(port);
                    Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.INFO, "CONFIG_PORT::: "+port, "CONFIG_PORT::: "+port);
                    //System.out.println("CONFIG_PORT::: "+port);
                    configPort();
                    break;
    		}
    	}
    }

    public void configPort() {
    	if (sp != null) {
    		try {
				sp.openPort();
                sp.setParams(BAUDRATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
                sp.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT);
                
			} catch (SerialPortException e) {
				Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, e);
			}
    	}
    }
    
    public boolean sendCommand(String command) {
    	boolean send = false;
        if (sp != null) {
            try {
            	send = sp.writeBytes((command + "\r\n").getBytes(Charset.forName("UTF-8")));
            } 
            catch (SerialPortException e) {
            	Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, e);
            	send = false;
			}
        }
        return send;
    }

    public boolean sendSubtotale() {
        return sendCommand("=");
    }

    public boolean sendProdotto(String descrizione, Float prezzo, Integer reparto, Integer qta) {
        prezzo = prezzo*100;
        Integer tmpPrezzo=Math.round(prezzo);
        String comando = "\"" + descrizione + "\"" + qta+"*"+tmpPrezzo + "H" + reparto + "R";
        return sendCommand(comando);
    }

    public boolean sendProdotti(List<Prodotto> listaProdotti) {
    	Integer n_reparto = 1;
    	boolean sendBySerial = true;
        for (Prodotto p : listaProdotti){
        	if(p.getIdCatMerc().equalsIgnoreCase("Ristorante")) {
        		n_reparto = 1;
        	}else if(p.getIdCatMerc().equalsIgnoreCase("Bottega")) {
        		if(p.getIdIva().startsWith("22"))
        			n_reparto = 2;
        		else if(p.getIdIva().startsWith("4"))
        			n_reparto = 3;
        		else if(p.getIdIva().startsWith("10"))
        			n_reparto = 4;
        	}
        	
            if(!sendProdotto(p.getNome(), Float.valueOf(p.getPrezzo()), n_reparto, Integer.valueOf(p.getQta()))) {
            	sendBySerial = false;
            	break;
            }
        }
        
        return sendBySerial;
    }

    
    
    public SerialPort getPort() {
        return sp;
    }
    
    public List<String> getPortList() {
        return listaPorte;
    }
}
