/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PPM.tomaz;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Usuário
 */
public class PPM {
    private final Map<String,Contexto> contextos = new HashMap<String, Contexto>();

    public void verificaContexto(byte[] valor,byte simb) {
        Contexto c = null;
        String val = valor == null ? null : new String(valor);
        c = findOrCreateContexto(val, simb);

    }

    public static void main(String[] args) {
        PPM  ppm = new PPM();
        ppm.verificaContexto(null, (byte)'a');

    }

    private Contexto put(String s) {
        Contexto c = new Contexto(s);
        contextos.put(s, c);
        return c;
    }

    private Contexto findOrCreateContexto(String contexto,byte simb) {
        Contexto c = contextos.get(contexto);
        if (c == null) {
            c = put(contexto);
        }
        findSimbol(c,simb);
        return c;
    }

    private void findSimbol(Contexto c, byte simb) {
        c.operaSimbol(simb);
    }
}
