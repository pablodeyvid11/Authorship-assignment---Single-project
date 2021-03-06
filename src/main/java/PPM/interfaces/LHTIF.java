package PPM.interfaces;

import java.io.Serializable;

/**
 * Interface que representa o low, high e total de um simbolo em um determinado
 * contexto.
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
public interface LHTIF extends Serializable{

    /**
     * O inicio da faixa do simbolo.
     * 
     * @returno low do simbolo
     */
    public int getLow();

    /**
     * O fim da faixa do simbolo.
     *
     * @returno o high do simbolo
     */
    public int getHigh();

    /**
     * O valor do intervalo total.
     *
     * @return o total do intervalo
     */
    public int getTotal();

}
