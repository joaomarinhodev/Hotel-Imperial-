/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import javax.swing.JCheckBox;

/**
 *
 * @author Pichau
 */
public class VerificarJCheckBox {
    public boolean campoVazio(JCheckBox combo) {

        if (combo.getSelectedIcon()== null ) {
            combo.requestFocusInWindow();
            return true;
        }
        return false;
    }
}
