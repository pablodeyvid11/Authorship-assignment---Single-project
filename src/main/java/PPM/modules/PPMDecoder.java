package PPM.modules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Compressores.InputFile;
import Compressores.OutputFile;
import PPM.arithcode.ArithDecoder;
import PPM.interfaces.LHTIF;

/**
 * Classe do descompressor do PPM com modelo o aritmetico.
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
public class PPMDecoder extends AbstractPPMModule {

    private ArithDecoder arithDecoder;

    /**
     * Construtor da classe.
     *
     * @param sourceFile o arquivo origem
     * @param destinyFile o arquivo onde o arquivo compremido sera gravado
     */
    public PPMDecoder(File sourceFile, File destinyFile) {
        super(sourceFile, destinyFile);
    }

    public void execute() throws IOException {
        /*** Para leitura do arquivo comprimido ***/
        InputFile inputFile = new InputFile(getSourceFile());
        OutputFile outputFile = new OutputFile(getDestinyFile());

        /*** Ler o tamanho do arquivo ***/
        int compressFileLengh = inputFile.readInt();
        //System.out.println("\n>> Decodificacao");

        if (!DO_USE_CONTINOUS_COMPRESS) {
            //System.out.println(">> tamaho lido: " + compressFileLengh);
        }

        arithDecoder = new ArithDecoder(inputFile.getDataInputStream());
        int bytesUncompressed = 0;

        /*** Chama o aritmetico ate que o numero de bytes original seja atingido ***/
        while (shouldStop(bytesUncompressed, compressFileLengh, DO_USE_CONTINOUS_COMPRESS)) {
            Byte b = processValue();
            //System.out.println(">> symbol decodificado: " + new String(new byte[]{b}));

            if (b == null) {
                throw new IllegalStateException("Process value return is null!");
            }

            outputFile.write(b);
            bytesUncompressed++;
        }

        arithDecoder.close();
        outputFile.flush();
        outputFile.close();
    }

    /**
     * Diz se o decoder deve continuar descomprimindo bytes ou deve parar.
     *
     * @param bytesUncompressed o numero de bytes descomprimidos ate o momento
     * @param originalFileLenght o tamanho de bytes do arquivo original (se estiver disponivel)
     * @param useContinousCompressed se foi feita compressao continua ou foi inserido o tamanho do arquivo no arquivo comprimido
     *
     * @return verdadeiro para continuar comprimindo ou falso para parar
     */
    private boolean shouldStop(int bytesUncompressed, int originalFileLenght, boolean useContinousCompressed) {
        return useContinousCompressed ? !arithDecoder.endOfStream() : bytesUncompressed < originalFileLenght;
    }

    /**
     * Descobre qual o simbolo a ser escrito no arquivo e o retorna.
     *
     * @return o simbolo descoberto
     */
    private Byte processValue() throws IOException {
        Context currentContext;
        ContextKMinusOne contextKMinusOne = getContextKMinusOne();
        List<Byte[]> emptyContexts = new ArrayList<Byte[]>();
        List<Byte[]> escapedContexts = new ArrayList<Byte[]>();

        Byte[] lookback = getLookBack();
        Byte symbol;
        int indice;
        LHTIF lowHighTotal;

        do {
            currentContext = (Context) getContext(lookback);
            //SE NAO TEM NADA NO CONTEXTO
            if (currentContext == null) {
                if (lookback == null) {

                    indice = arithDecoder.getCurrentSymbolCount(contextKMinusOne.getTotalOfOccurrences());
                    
                    symbol = contextKMinusOne.getSymbol(indice);
                    lowHighTotal = contextKMinusOne.getLowHighTotal(symbol);
                    addContext(lookback, symbol);
                    try {
                        arithDecoder.removeSymbolFromStream(lowHighTotal.getLow(), lowHighTotal.getHigh(), lowHighTotal.getTotal());

                    } catch (IOException ex) {
                        Logger.getLogger(PPMDecoder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    contextKMinusOne.deleteSymbol(symbol);
                    break;
                } else {
                    emptyContexts.add(lookback);
                    lookback = decrementLookBack(lookback);
                }
            } else {
                indice = arithDecoder.getCurrentSymbolCount(currentContext.getTotalOfOccurrences());
                symbol = currentContext.getSymbol(indice);
                //System.out.println("Indice: " + indice);
                lowHighTotal = currentContext.getLowHighTotal(symbol);
                try {
                    arithDecoder.removeSymbolFromStream(lowHighTotal.getLow(), lowHighTotal.getHigh(), lowHighTotal.getTotal());
                } catch (IOException ex) {
                    Logger.getLogger(PPMDecoder.class.getName()).log(Level.SEVERE, null, ex);
                }

                if ((symbol == null)) {

                    escapedContexts.add(lookback);

                    if ((lookback == null)) {

                        indice = arithDecoder.getCurrentSymbolCount(contextKMinusOne.getTotalOfOccurrences());
                        symbol = contextKMinusOne.getSymbol(indice);
                        //System.out.println("Indice: " + indice);
                        lowHighTotal = contextKMinusOne.getLowHighTotal(symbol);
                        addContext(lookback, symbol);

                        try {
                            arithDecoder.removeSymbolFromStream(lowHighTotal.getLow(), lowHighTotal.getHigh(), lowHighTotal.getTotal());

                        } catch (IOException ex) {
                            Logger.getLogger(PPMDecoder.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        contextKMinusOne.deleteSymbol(symbol);
                        break;
                    }
                    lookback = decrementLookBack(lookback);//DECREMENTA LOOKBACK
                } else if ((symbol != null)) {
                    currentContext.incrementSymbol(symbol);
                    break;
                }

            }

        } while (true);

        //Adicao dos contextos anteriores.
        for (Byte[] c : emptyContexts) {
            addContext(c, symbol);
        }

        //atualizacao dos contextos anteriores.
        for (Byte[] c : escapedContexts) {
            getContext(c).incrementSymbol(symbol);
        }

        updateLookBack(symbol);
        return symbol;
    }
}
