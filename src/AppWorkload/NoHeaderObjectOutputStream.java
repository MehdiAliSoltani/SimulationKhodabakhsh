/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AppWorkload;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author Mehdi
 */
public class NoHeaderObjectOutputStream extends ObjectOutputStream {

    public NoHeaderObjectOutputStream(OutputStream os) throws IOException {
        super(os);
    }

    protected void writeStreamHeader() {
    }
}
