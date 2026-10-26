/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import javax.swing.JComboBox;

/**
 *
 * @author User
 */
public class VerificarJboxVazio {
    public boolean campoVazio(JComboBox combo) {

        if (combo.getSelectedItem() == null || combo.getSelectedItem().toString().trim().isEmpty()) {
            combo.requestFocusInWindow();
            return true;
        }

        return false;
    }
}
