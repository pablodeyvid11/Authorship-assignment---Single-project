package dweauthorshipattribution.interfaces;

import java.util.Comparator;

/**
 * Representa o resultado de uma compressao.
 *
 * <br><br>
 * Esse projeto faz parte de uma das atividades da disciplina de Introdução
 * à Teoria da Informação do Departamento de Informática da Universidade Federal
 * da Paraíba (UFPB) do período 2010.1 ministrada pelo professor Leonardo Vidal e
 * consiste na atribuicao de autoria utilizando a frequencia de classificacao das
 * palavras utilizadas por um determinado autor.
 * Este e' o projeto final da disciplina.
 *
 * @since 26 de junho de 2010
 * @author Elenilson Vieira - elenilson[at]elenilsonvieira.com
 * @author Daniel Pires - dpsmetal[at]gmail.com
 * @author Wolgrand Cardoso - wolgrandcardoso[at]gmail.com
 * @version 1.0
 * @see www.di.ufpb.br
 */
public interface AuthorCompressFileLenghIF {
    
    /**
     * O comparator a ser utilizado entre os bytes.
     */
    public static final Comparator<AuthorCompressFileLenghIF> COMPARATOR = new Comparator<AuthorCompressFileLenghIF>(){

            public int compare(AuthorCompressFileLenghIF o1, AuthorCompressFileLenghIF o2) {
                return (int) (o1.getFileLenght() - o2.getFileLenght());
            }

    };

    /**
     * Retorna o autor.
     * 
     * @return o autor
     */
    public AuthorIF getAuthor();

    /**
     * O tamanho do arquivo apos a utilizacao do PPM.
     *
     * @return o tamanho do arquivo
     */
    public long getFileLenght();
}
