package dweauthorshipattribution.allocator;

import dweauthorshipattribution.interfaces.AuthorCompressFileLenghIF;
import dweauthorshipattribution.interfaces.AuthorshipAllocatorResultIF;

/**
 * Resultado da atribuicao de autoria.
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
public class AuthorshipAllocatorResult implements AuthorshipAllocatorResultIF{

    private AuthorCompressFileLenghIF possibleAuthor;
    private AuthorCompressFileLenghIF[] testedAuthors;

    /**
     * Construtor da classe.
     * 
     * @param possibleAuthor o possivel autor
     * @param testedAuthors os autores testados
     */
    public AuthorshipAllocatorResult(AuthorCompressFileLenghIF possibleAuthor, AuthorCompressFileLenghIF[] testedAuthors) {
        if(testedAuthors == null || testedAuthors.length == 0)
            throw new IllegalArgumentException("Tested authors can't be null or empty!");

        if(possibleAuthor == null)
            throw new IllegalArgumentException("Possible author can't be null!");

        this.possibleAuthor = possibleAuthor;
        this.testedAuthors = testedAuthors;
    }

    /**
     * Construtor da classe que ja procura qual o possivel autor
     * 
     * @param testedAuthors os autores testados
     */
    public AuthorshipAllocatorResult(AuthorCompressFileLenghIF[] testedAuthors) {
        if(testedAuthors == null || testedAuthors.length == 0)
            throw new IllegalArgumentException("Tested authors can't be null or empty!");

        this.testedAuthors = testedAuthors;

        AuthorCompressFileLenghIF authorCompressFileLenghIF = testedAuthors[0];

        for(int i = 1; i < testedAuthors.length; i++){
            if(authorCompressFileLenghIF.getFileLenght() > testedAuthors[i].getFileLenght())
                authorCompressFileLenghIF = testedAuthors[i];
        }

        this.possibleAuthor = authorCompressFileLenghIF;
    }

    public AuthorCompressFileLenghIF getPossibleAuthor() {
        return possibleAuthor;
    }

    public AuthorCompressFileLenghIF[] getTestedAuthors() {
        return testedAuthors;
    }

}
