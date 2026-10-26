/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import javax.swing.JTextArea;

/**
 *
 * @author Pichau
 */
public class VerificarJTextArea {
    public boolean campoVazio(JTextArea descricao){
        if(descricao.getText().trim().isEmpty()){
            descricao.requestFocusInWindow();
            return true;
        }
        return false;
    }
    
}
