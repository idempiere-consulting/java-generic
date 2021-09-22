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
    		System.out.println("PORT::: "+port);
    		if(port!=null && port.contains("/dev/ttyUSB")) {
                    listaPorte.add(port);
                    sp = new SerialPort(port);
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
    
    public void sendCommand(String command) {
        if (sp != null) {
            try {
            	sp.writeBytes((command + "\r\n").getBytes(Charset.forName("UTF-8")));
            } 
            catch (SerialPortException e) {
            	Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, e);
			}
        }
    }

    public void sendSubtotale() {
        sendCommand("=");
    }

    public void sendProdotto(String descrizione, Float prezzo, Integer reparto, Integer qta) {
        prezzo = prezzo*100;
        Integer tmpPrezzo=Math.round(prezzo);
        String comando = "\"" + descrizione + "\"" + qta+"*"+tmpPrezzo + "H" + reparto + "R";
        sendCommand(comando);
    }

    public void sendProdotti(List<Prodotto> listaProdotti) {
        for (Prodotto p : listaProdotti){
            sendProdotto(p.getNome(), Float.valueOf(p.getPrezzo()), 1, Integer.valueOf(p.getQta()));
        }
    }

    
    
    public SerialPort getPort() {
        return sp;
    }
    
    public List<String> getPortList() {
        return listaPorte;
    }
}
