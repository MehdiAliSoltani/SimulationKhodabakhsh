/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AppWorkload;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mehdi
 */
public class ReadWritetoFile {

    public void writeinFile(Object obj, String filepath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filepath);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            // write something in the file
            oout.writeObject(obj);
            //         oout.writeObject(i);
            // close the stream
            oout.close();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadWritetoFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadWritetoFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeListinFile(List<Object> obj, String filepath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filepath);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            // write something in the file
            oout.writeObject(obj);
            //         oout.writeObject(i);
            // close the stream
            oout.close();
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadWritetoFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadWritetoFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveObjectfirstTime(Object obj, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path), true));
            oos.writeObject(obj);
            oos.reset();
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            System.out.println("Error happend in method saveObjectfirstTime to save an object for the first time in file");
            ex.printStackTrace();
        }
    }

    public void AppendObject(Object obj, String path) {
        try {

            NoHeaderObjectOutputStream oos = new NoHeaderObjectOutputStream(new FileOutputStream(new File(path), true));

            oos.writeObject(obj);
            oos.reset();
            oos.flush();
            oos.close();

        } catch (Exception ex) {
            System.out.println("Error happend in method AppendObject when the program wanted to append in a file.");
            ex.printStackTrace();
        }
    }

    public void writeListfirstTime(List<Workload> obj, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path), true));
            oos.writeObject(obj);
            oos.reset();
            oos.flush();
            oos.close();
        } catch (Exception ex) {
            System.out.println("Error happend in method saveObjectfirstTime to save an object for the first time in file");
            ex.printStackTrace();
        }
    }

    public void AppendListtoFile(List<Workload> obj, String path) {
        try {

            NoHeaderObjectOutputStream oos = new NoHeaderObjectOutputStream(new FileOutputStream(new File(path), true));

            oos.writeObject(obj);
            oos.reset();
            oos.flush();
            oos.close();

        } catch (Exception ex) {
            System.out.println("Error happend in method AppendObject when the program wanted to append in a file.");
            ex.printStackTrace();
        }
    }

    public List<List<Workload>> readfromFile(String filepath) {
//        List<Object> lo = new ArrayList<>();
        List<List<Workload>> retlist = new ArrayList<List<Workload>>();
        ObjectInputStream ois = null;
//        Object obj = null;
        List<Workload> readlist = new ArrayList<Workload>();
        int t = 0;
        try {
//            double time  = System.currentTimeMillis();
            ois = new ObjectInputStream(new FileInputStream(filepath));
            try {
                // read and print what we wrote before
                do {
//                    obj = ois.readObject();
//                    obj  =  ois.readObject();
                    readlist = (List<Workload>) ois.readObject();
                    retlist.add(readlist);
//                        lo.add(obj);
//                    if(ois.read() == -1){
//                        break;
//                    }
                    System.out.println(t++);
                } while (readlist != null);
                ois.close();
//                System.out.println(System.currentTimeMillis() - time );
            } catch (EOFException e) {
                //System.out.println("=======================");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReadWritetoFile.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(ReadWritetoFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            return lo;
            return retlist;
        }

    }

    public List<List<Workload>> readWorkloadFiles(String filepath) {
        List<List<Workload>> retlist = new ArrayList<List<Workload>>();
        ObjectInputStream ois = null;
        List<Workload> readlist = new ArrayList<Workload>();
        ObjectInputStream objectIn = null;
        int t=0;
        try {
            objectIn = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(filepath)));
            do {
                readlist = (List<Workload>) objectIn.readObject();
                retlist.add(readlist);
//                System.out.println(t++);
            } while (readlist != null);
            objectIn.close();
        } catch (EOFException ex) {
            ;
        } catch (ClassNotFoundException ex) {
            ;
        } catch (IOException ex) {
            ;
        }
        return retlist;
    }

}
