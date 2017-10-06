/**
 * this class represetns the blockchain object
 * @param <T> is the type of object that each block will store.
 */
package Model.DataStructures;

import java.security.NoSuchAlgorithmException;

    /**
     * This class represents the {@code Blockcahin} object.
     * @param <T> is the data type that is each {@code Block} will store.
     */
public class Blockchain <T> {

    private Node lastNode;
    public final static String HASH_FUNCTION = "SHA-256";
    public final static String GENESIS = "0000000000000000000000000000000";
    private String zeros;
    public HashFunction encoder;

    /**
     * This constructor method will create an empty {@code Blockchain} object.
     * @param zeros are the number of zeros that the hash of each {@code Block} must have
     */
    public Blockchain(int zeros) throws NoSuchAlgorithmException, CloneNotSupportedException {
        if( zeros < 0){
            throw  new IllegalArgumentException();
        }
        this.encoder = HashFunction.getSingletonInstance(HASH_FUNCTION);
        this.zeros = generateExpReg(zeros);
        lastNode = null;
    }

    private static String generateExpReg(int zeros){
        StringBuffer expReg = new StringBuffer(zeros +3);
        expReg.append('^');
        for (int i = 0 ; i < zeros ; i++)
            expReg.append('0');
        expReg.append('.');
        expReg.append('*');
        return new String(expReg);
    }


    /**
     * This method adds data to the {@BlockChain} in a new {@code Block}.
     * @param data the data to ve insert.
     */
    public void add(T data){
        Block<T> b = null;
        if(lastNode == null){
            b = createGenesis(data);
        }else{
            Block<T> prev = lastNode.block;
            b = new Block<T>(prev.getIndex()+1, data, prev.getHash(), zeros);
            b.mine();
        }

        Node n = new Node(b);
        n.next = lastNode;
        lastNode = n;
    }

    public Block<T> createGenesis(T data){
        Block<T> b = new Block<T>(0, data, GENESIS, zeros);
        b.mine();
        return b;
    }

    public boolean verify(){
        if(lastNode == null || lastNode.block.getPrevHash().equals(GENESIS)){
            return true;
        }

        Node current = lastNode;
        Block<T> b ;
        while( current != null && current.next != null){
            b = current.block;
            Block<T> next = current.next.block;
            if(!b.getPrevHash().equals(next.getHash())){
                return false;
            }
            current = current.next;
        }
        return true;
    }

    /*
    * @deprecated
    * do not use , stack memory problems with chains bigger than 10 000 elements
    * */
    private boolean verify(Node n, String hash){
        if(n == null || n.block.getPrevHash().equals(GENESIS)){
            return  true;
        }

        if(n.block.getHash().equals(hash)){
            return verify(n.next, n.block.getPrevHash());
        }
        return  false;
    }


    public static void  main(String[] args) throws NoSuchAlgorithmException, CloneNotSupportedException {
        Blockchain<Integer> b = new Blockchain<>(4);
        for (int i = 0; i < 10; i++) {
            b.add(i);
            System.out.println("finish proccessing "+ i);
        }
        System.out.println(b.verify());
    }

    private class Node {
        Block<T> block;
        Node next;

        public Node(Block<T> block){
            if(block == null){
                throw  new IllegalArgumentException("a block must not be null");
            }
            this.block = block;
        }
    }

}
