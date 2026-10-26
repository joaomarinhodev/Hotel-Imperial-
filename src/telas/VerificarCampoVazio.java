/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class VerificarCampoVazio {
    public boolean campoVazio(javax.swing.JTextField campo) {

    if (campo.getText().trim().isEmpty()) {
        campo.requestFocusInWindow();
        return true;
    }
    return false;
}
}
