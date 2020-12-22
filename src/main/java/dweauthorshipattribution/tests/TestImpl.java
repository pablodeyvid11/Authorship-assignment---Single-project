package dweauthorshipattribution.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.hibernate.Session;

import Compressores.TextFile;
import PPM.modules.PPMEncoder;
import dweauthorshipattribution.allocator.PPMAuthor;
import dweauthorshipattribution.interfaces.WordClassifierIF;
import dweauthorshipattribution.interfaces.WordIF;
import dweauthorshipattribution.interfaces.WordIF.WordClassification;
import dweauthorshipattribution.lexicon.Word;
import dweauthorshipattribution.lexicon.WordClassifier;
import dweauthorshipattribution.persistence.DWEPersistenceUtil;
import dweauthorshipattribution.persistence.dao.WordDAO;
import dweauthorshipattribution.util.FileUtils;

/**
 * Classe main do projeto de atribuicao de autoria.
 *
 * <br>
 * <br>
 * Esse projeto faz parte do projeto integrador dos alunos:
 * Clara Yasmin Cunha Fernandes dos Santos;
 * Pablo Deyvid de Paiva.
 * 
 * Orientados pelo professor mestre Elenilson Vieira.
 * 
 *
 * @since 2020
 * @author Elenilson Vieira - elenilson[at]elenilsonvieira.com
 * @author Pablo Deyvid - claryasmin.cf[at]gmail.com
 * @author Clara Yasmin - wolgrandcardoso[at]gmail.com
 * @version 2.0
 */
public class TestImpl {

   public final static String CAMINHO = "C:/Users/wolgrand/Documents/NetBeansProjects/iti/base/";
    /**
     * Main de teste do projeto.
     * 
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) throws IOException, IllegalAccessException {

        do{
        String s = JOptionPane.showInputDialog("Digite o nome do arquivo:");
        if(s.equalsIgnoreCase("sair"))
            break;
        System.out.println(s);
        populateDatabase(CAMINHO + s);
        }while(true);

    }

    public static void saveTest(){
        Test t = new Test("elenilson");
        Session session = (Session) DWEPersistenceUtil.entitityManager.getDelegate();
        org.hibernate.Transaction transaction = session.beginTransaction();

        transaction.begin();
        session.merge(t);
        transaction.commit();
    }

    public static void getWord(){
        WordDAO wordDAO = new WordDAO();
        WordIF word = wordDAO.getWord("elenilsodvfvfn");
        System.out.println(">> " + word);
    }

    public static void saveWord(){
        Word word = new Word("elenilsón", WordClassification.NOUN);
        WordDAO wordDAO = new WordDAO();
        wordDAO.save(word);
    }

    public static void tokenizer() throws IOException, IllegalAccessException{
        TextFile t = new TextFile("teste.txt");
        t.setContent("eleniláon,vieira,daniel,wolgrand.... tiago machado");

        String[] tokens = t.tokenizer();
        for(int i = 0;i < tokens.length ;i++ ){
            System.out.println(">> " + tokens[i]);
        }
    }

    public static void path(){
        String local = System.getProperty("user.dir");
        System.out.println(local);
    }

    /**
     * ATENCAO: esse teste so funciona se tiver o syncronized em todos os metodos do DAO.
     */
    public static void populateDatabase(String s) throws IOException{
        WordClassifierIF wordClassifier = new WordClassifier();
        TextFile textFile = new TextFile(s);

        String[] tokens = textFile.tokenizer();

        System.out.println(">> iniciando for com size " + tokens.length);
        for (String token : tokens) {
        wordClassifier.classificate(token);
        }
        System.out.println(">> terminando for");
    }


    static class Run implements Runnable{

        private WordClassifierIF wordClassifier;
        private String token;

        public Run(WordClassifierIF wordClassifier, String token) {
            this.wordClassifier = wordClassifier;
            this.token = token;
        }

        public void run() {
            wordClassifier.classificate(token);
        }

    }

    public static void testSerializacao() throws FileNotFoundException, IOException{
        PPMEncoder ppme = new PPMEncoder();
        
        FileUtils.writeObject(ppme, "lalala");
    }

    public static void testDesserializacao() throws IOException, ClassNotFoundException{
        PPMAuthor ppma = (PPMAuthor) FileUtils.readObject("AdolfoCaminha.training-ppm");
        System.out.println(">> " + ppma);
    }
}
