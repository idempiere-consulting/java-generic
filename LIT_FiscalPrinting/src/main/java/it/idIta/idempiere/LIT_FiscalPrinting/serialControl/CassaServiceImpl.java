/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.idIta.idempiere.LIT_FiscalPrinting.serialControl;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import it.idIta.idempiere.LIT_FiscalPrinting.model.Prodotto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author pilo
 */
@Service
public class CassaServiceImpl {

    List<String> listaPorte;
    private SerialPort sp;
    static final int BAUDRATE = 9600;

    

    public Boolean checkPort() {
        Enumeration tmpports = CommPortIdentifier.getPortIdentifiers();
        if (tmpports != null) {
            return tmpports.hasMoreElements();
        }
        return false;

    }

    public void scanPort() {
        listaPorte = new ArrayList<String>();
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        System.out.println("PORT INIT::: ");

        while (ports.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) ports.nextElement();
            System.out.println("PORT::: "+portId.toString());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().contains("/dev/ttyUSB")) {
                try {
                    listaPorte.add(portId.getName());
                    sp = (SerialPort) portId.open("Demo application", 10000);
                    configPort();
                } catch (PortInUseException ex) {
                    Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    public void configPort() {
        if (sp != null) {
            BufferedReader br = null;
            try {

                br = new BufferedReader(
                        new InputStreamReader(
                                sp.getInputStream(),
                                "US-ASCII"));
                sp.setSerialPortParams(
                        BAUDRATE,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                sp.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT);

                try {
                    sp.addEventListener(new SerialPortEventListener() {

                        @Override
                        public void serialEvent(SerialPortEvent spe) {
                            SerialPort port = (SerialPort) spe.getSource();
                            switch (spe.getEventType()) {
                                case SerialPortEvent.DATA_AVAILABLE:
        //                            System.out.println("dati disponibili");
                            }

                        }
                    });
                } catch (TooManyListenersException ex) {
                    Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                sp.notifyOnDataAvailable(true);
                OutputStream outStream = sp.getOutputStream();
                InputStream inStream = sp.getInputStream();
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedCommOperationException ex) {
                Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void sendCommand(String command) {
        if (sp != null) {
            OutputStream outStream = null;
            try {
                outStream = sp.getOutputStream();
                outStream.write((command + "\r\n").getBytes(Charset.forName("UTF-8")));
            } catch (IOException ex) {
                Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    outStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(CassaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
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
            sendProdotto(p.getNome(), Float.valueOf(p.getPrezzo()), -1, Integer.valueOf(p.getQta()));
        }
    }

    
    
    public SerialPort getPort() {
        return sp;
    }
    
    public List<String> getPortList() {
        return listaPorte;
    }
}
