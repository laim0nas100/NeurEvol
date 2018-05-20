/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.lb.neurevol.Evoliution.Coevolution;

/**
 *
 * @author Lemmin
 */
public class PairingInfo {

    public PairingInfo(int subIndex, int memIndex) {
        this.memberIndex = memIndex;
        this.subpopulaionIndex = subIndex;
    }

    public int subpopulaionIndex;
    public int memberIndex;

    @Override
    public String toString() {
        return this.subpopulaionIndex + ":" + this.memberIndex;
    }
}
