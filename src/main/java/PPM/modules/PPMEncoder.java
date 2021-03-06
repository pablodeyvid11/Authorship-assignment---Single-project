package PPM.modules;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import Compressores.InputFile;
import Compressores.OutputFile;
import PPM.arithcode.ArithEncoder;
import PPM.interfaces.LHTIF;

/**
 * Classe do compressor do PPM com modelo o aritmetico.
 *
 * <br><br>
 * Esse projeto faz parte de uma das atividades da disciplina de Introdução
 * à Teoria da Informação do Departamento de Informática da Universidade Federal
 * da Paraíba (UFPB) do período 2010.1 ministrada pelo professor Leonardo Vidal.
 *
 * @since 9 de junho de 2010
 * @author Elenilson Vieira - elenilson[at]elenilsonvieira.com
 * @author Daniel Pires - dpsmetal[at]gmail.com
 * @author Wolgrand Cardoso - wolgrandcardoso[at]gmail.com
 * @version 1.0
 * @see www.di.ufpb.br
 */
public class PPMEncoder extends AbstractPPMModule implements Serializable{

    private ArithEncoder arithEncoder;

    /**
     * Construtor da classe.
     *
     * @param sourceFile o arquivo origem
     * @param destinyFile o arquivo onde o arquivo compremido sera gravado
     */
    public PPMEncoder(File sourceFile, File destinyFile) {
        super(sourceFile, destinyFile);
    }

    /**
     * Construtor sem argumentos.
     */
    public PPMEncoder() {
    }

    public void execute() throws FileNotFoundException, IOException {
        /*** Para leitura do arquivo ***/
        InputFile sourceByteFile = new InputFile(getSourceFile());
        
        /*** 
         * Escrita ou nao do numero de bytes no arquivo codificado
         * para saber quando para na decodificacao.
         ***/
        OutputFile outputFile = insertOriginalFileLenghOndeDestinyFile(!DO_USE_CONTINOUS_COMPRESS);
        
        /*** Passando o stream para o artimetico ***/
        arithEncoder = new ArithEncoder(outputFile.getDataOutputStream());
        //System.out.println(">> Codificacao");

        try {
            while (true) {
                byte readByte = sourceByteFile.readByte();
                //System.out.println(">> Byte lido: " + readByte);
                processSymbol(readByte);
            }
        } catch (EOFException ex) {
        }

        sourceByteFile.close();
        arithEncoder.flush();
        arithEncoder.close();
    }

    /**
     * Faz todo o processo de leitura dos contextos e envio pro aritimetico
     * do simbolo recebido como argumento.
     *
     * @param symbol o simbolo a ser processado
     */
    private void processSymbol(Byte symbol) {
        /***
         * Aqui preciso pegar o low, high e total do simbolo ou dos escapes
         * que aparecerem no meio do caminho e depois chamar o metodo encode
         * seguido do flush do aritmetico.
         ***/
        Context currentContext;
        Context previousContext;//Podera ser utilizado para realizar exclusao no PPM

        LHTIF currentSymbolHighTotal;

        Byte[] lookback = getLookBack();

        do {
            currentContext = (Context) getContext(lookback);
            //SE NAO TEM NADA NO CONTEXTO
            if (currentContext == null) {
                addContext(lookback, symbol);
                if (lookback == null) {//CASO k = 0
                    currentSymbolHighTotal = getContextKMinusOne().getLowHighTotal(symbol);
                    try {//manda os coeficiente do contexto k = -1
                        arithEncoder.encode(currentSymbolHighTotal.getLow(), currentSymbolHighTotal.getHigh(), currentSymbolHighTotal.getTotal());
                    } catch (IOException ex) {
                        Logger.getLogger(PPMEncoder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    getContextKMinusOne().deleteSymbol(symbol);
                    break;
                } //CASO k > 0
                
                lookback = decrementLookBack(lookback);
                
            } else {//NESSE CASO TEM ALGO NO CONTEXTO
                //NAO ACHOU O SIMBOLO!
                if (currentContext.getSymbol(symbol) == null) {
                    //codifica o escape
                    currentSymbolHighTotal = currentContext.getLowHighTotal(null);
                    try {
                        //manda escape para aritmetico
                        arithEncoder.encode(currentSymbolHighTotal.getLow(), currentSymbolHighTotal.getHigh(), currentSymbolHighTotal.getTotal());
                    } catch (IOException ex) {
                        Logger.getLogger(PPMEncoder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    currentContext.incrementSymbol(symbol);//incrementa o simbolo atual naquele contexto
                                       //CASO k = 0
                    if (lookback == null) {
                        currentSymbolHighTotal = getContextKMinusOne().getLowHighTotal(symbol);
                        try {
                            arithEncoder.encode(currentSymbolHighTotal.getLow(), currentSymbolHighTotal.getHigh(), currentSymbolHighTotal.getTotal());
                        } catch (IOException ex) {
                            Logger.getLogger(PPMEncoder.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        getContextKMinusOne().deleteSymbol(symbol);
                        break;
                    }
                     lookback = decrementLookBack(lookback);//DECREMENTA LOOKBACK

                } else if (currentContext.getSymbol(symbol) != null) {//ACHOU O SIMBOLO
                    currentSymbolHighTotal = currentContext.getLowHighTotal(symbol);//pega o low e high do simbolo
                    try {//manda o low high e total para  o aritmetico
                        arithEncoder.encode(currentSymbolHighTotal.getLow(), currentSymbolHighTotal.getHigh(), currentSymbolHighTotal.getTotal());
                    } catch (IOException ex) {
                        Logger.getLogger(PPMEncoder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    currentContext.incrementSymbol(symbol);//incrementa o simbolo na tabela
                    break;
                }

            }


//            }
        } while (true);

        updateLookBack(symbol);
        //da um update
//         throw new UnsupportedOperationException("Not supported yet.");?
    }

    /**
     * Insere ou nao o tamanho do arquivo original no arquivo de saida.
     *
     * @param insert verdadeiro para inserir e falso caso contrario
     *
     * @return o arquivo de saida para o artimetico
     */
    private OutputFile insertOriginalFileLenghOndeDestinyFile(boolean insert) throws FileNotFoundException, IOException {
        OutputFile outputFile = new OutputFile(getDestinyFile());

        if(insert){
            outputFile.write((int) getSourceFile().length());//Isso pode da merda, eu sei!
            outputFile.flush();
        }
        
        return outputFile;
    }
}
