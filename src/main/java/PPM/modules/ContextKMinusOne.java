package PPM.modules;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import PPM.interfaces.ContextIF;
import PPM.interfaces.LHTIF;
import PPM.interfaces.SymbolIF;

/**
 * Representa um contexto k - 1 do PPM.
 *
 * <br><br>
 * Esse projeto faz parte de uma das atividades da disciplina de Introdução
 * à Teoria da Informação do Departamento de Informática da Universidade Federal
 * da Paraíba (UFPB) do período 2010.1 ministrada pelo professor Leonardo Vidal.
 *
 * @since 10 de junho de 2010
 * @author Elenilson Vieira - elenilson[at]elenilsonvieira.com
 * @author Daniel Pires - dpsmetal[at]gmail.com
 * @author Wolgrand Cardoso - wolgrandcardoso[at]gmail.com
 * @version 1.0
 * @see www.di.ufpb.br
 */
public class ContextKMinusOne implements ContextIF, Serializable{

    private List<Byte> symbols;

    /**
     * Construtor da classe.
     */
    public ContextKMinusOne() {
        init();
    }

    public String getContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TreeMap<Byte, SymbolIF> getSymbols() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSymbolOccurreces(Byte symbol) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getEscapeOccurrences() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LHTIF getLowHighTotal(Byte symbol) {
        if(symbol == null /*|| !symbols.contains(symbol)*/)
            return null;
        
        int position = Collections.binarySearch(symbols, symbol);
        LHTIF lht = new LHT(position, position + 1, symbols.size());
        //System.out.println("("+lht.getLow()+", "+lht.getHigh()+", "+lht.getTotal()+")");
        return lht;
    }

    public SymbolIF getSymbol(BigInteger arithmeticValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getTotalOfOccurrences() {

        //System.out.println("total= "+symbols.size());
        return symbols.size();
    }

    public void incrementSymbol(Byte symbol) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Deleta o simbolo da lista.
     *
     * @param symbol o simbolo a ser deletado
     */
    public void deleteSymbol(Byte symbol){
        if(symbol != null)
            symbols.remove(symbol);
    }

    /**
     * Preenche a lista com todos os simbolos
     */
    private void init() {
        symbols = new LinkedList<Byte>();

        for(byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++){
            symbols.add(b);
        }

        symbols.add(Byte.MAX_VALUE);
    }

    public SymbolIF getSymbol(Byte symbol) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Byte getSymbol(int position) {
        return position >= 0 && position < symbols.size() ? symbols.get(position) : null;
    }

    public SymbolIF getSymbol(BigDecimal arithmeticValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return String.format("Total: %d, Symbols: %s", symbols.size(), symbols);
    }
}
