package Huffman.tree;

/**
 * Classe que representa o nó da arvore do compressor Huffman.
 *
 * <br><br>
 * Esse projeto faz parte de uma das atividades da disciplina de Introdução
 * à Teoria da Informação do Departamento de Informática da Universidade Federal
 * da Paraíba (UFPB) do período 2010.1 ministrada pelo professor Leonardo Vidal.
 *
 * @since 11 de maio de 2010
 * @author Elenilson Vieira - elenilson[at]elenilsonvieira.com
 * @author Daniel Pires - dpsmetal[at]gmail.com
 * @author Wolgrand Cardoso - wolgrandcardoso[at]gmail.com
 * @version 1.0
 * @see www.di.ufpb.br
 */
public class HuffmanTreeNode {

    private long ocurrences;
    private byte value;
    private boolean isEscape;

    /**
     * Construtor da classe
     *
     * @param value o simbolo
     * @param isEscape verdadeiro para indicar se e´ escape e falso caso contrario
     */
    public HuffmanTreeNode(byte value, boolean isEscape) {
        this.value = value;
        this.isEscape = isEscape;
    }

    /**
     * Retorna o numero de ocorrencias
     *
     * @return o numero de ocorrencias
     */
    public long getOcurrences() {
        return ocurrences;
    }

    /**
     * Seta o numero de ocorrencias
     *
     * @param ocurrences o numero de ocorrencias
     */
    public void setOcurrences(long ocurrences) {
        this.ocurrences = ocurrences;
    }

    /**
     * Adiciona um nova ocorrencia
     */
    public void addOcurrece(){
        ocurrences++;
    }

    /**
     * Retorna o valor do byte
     *
     * @return o byte
     */
    public byte getValue() {
        return value;
    }

    /**
     * Diz se o simbolo e´ um escape ou nao
     *
     * @return verdadeiro se for escape e falso caso contrario
     */
    public boolean isEscape() {
        return isEscape;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HuffmanTreeNode other = (HuffmanTreeNode) obj;
        if (this.value != other.value) {
            return false;
        }
        if (this.isEscape != other.isEscape) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.value;
        hash = 17 * hash + (this.isEscape ? 1 : 0);
        return hash;
    }

}
