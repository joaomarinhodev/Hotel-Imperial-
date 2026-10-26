/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author User
 */
public class ValidarSomLetras {
    
    private Set<javax.swing.JTextField> camposAvisados = new HashSet<>();
    
    private boolean isCaractereValido(char c){
        return Character.isLetter(c) || 
                Character.isWhitespace(c) || 
                 Character.isISOControl(c);
    }
    
    public void validarSomenteLetras(javax.swing.JTextField campo, java.awt.event.KeyEvent evt) {

        char caractere = evt.getKeyChar();
        String textoAtual = campo.getText();
        String novoTexto = textoAtual + caractere;

        if (!isCaractereValido(caractere)) {
            evt.consume();

            if (!camposAvisados.contains(campo)) {
                JOptionPane.showMessageDialog(null, "Digite apenas letras no campo!");
                camposAvisados.add(campo);
            }
        }
    }
}
