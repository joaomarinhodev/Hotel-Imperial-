/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
/**
 *
 * @author User
 */
public class ValidarSomNum {
    private Set<javax.swing.JTextField> camposAvisados = new HashSet<>();
     
    private boolean isCaratereValido(char c){
         return Character.isLetter(c) ||
                 Character.isWhitespace(c);
    }
    
    public void verificarSomenteNumeros(javax.swing.JTextField campo, java.awt.event.KeyEvent evt){
        char caractere = evt.getKeyChar();
        String textoAtual = campo.getText();
        String novoTexto = textoAtual + caractere;
        
        if(isCaratereValido(caractere)){
            evt.consume();
            
            if(!camposAvisados.contains(campo)){
                JOptionPane.showMessageDialog(null, "Digite apenas números no campo!");
                camposAvisados.add(campo);
            }
        }
            
     
    }
}
