package Model;

import Model.DataStructures.AVLData;
import Model.DataStructures.AVLTree;
import Model.DataStructures.Blockchain;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents a {@code AVLTree} with a {@code Blockchain} to register all the operations in
 * the {@code AVLTree}.
 * @param <T> the generic type of data for the tree.
 */
public class AVLBlockchain<T> {

    private Blockchain<AVLData<T>> blockchain;
    private AVLTree<T> tree;

    /**
     * This method returns a new {@code AVLBlockchain} Object.
     * @param zeros this parameter is the specified quantity of zeros for the {@code Blockchain}.
     * @param cmp the {@code Comparator<T>} for the elements in the {@code AVLTree}.
     * @throws CloneNotSupportedException if the specified algorithm were invalid.
     * @throws NoSuchAlgorithmException if a new instance of {@code HashFunction} is required.
     */
    public AVLBlockchain(int zeros, Comparator<T> cmp) throws CloneNotSupportedException, NoSuchAlgorithmException {
        this.blockchain = new Blockchain<>(zeros);
        this.tree = new AVLTree<>(cmp);
    }

    /**
     * This method add a new element to the {@code AVLTree}.
     * @param element a new element.
     */
    public void add(T element) {
        AVLData<T> data = tree.insert(element);
        blockchain.add(data);
    }

    /**
     * This method remove a element from the {@code AVLTree}.
     * @param element a element to remove.
     */
    public void remove(T element) {
        AVLData<T> data = tree.remove(element);
        blockchain.add(data);
    }

    /**
     * This method lookup a specified element in the {@code AVLTree} and return all the indices of the
     * {@code Block} in the {@code Blockchain} that include any operation with a specified element.
     * @param element a specified element for lookup.
     * @return a new {@code List<Long>} with the required indices.
     */
    public List<Long> lookup(T element) {
        AVLData<T> data = tree.search(element);
        blockchain.add(data);

        if (data.getResult()) {
            return null;
        }
        return findBlocks(element);
    }

    /**
     * This method return a {@code List<Long>} with the indices of the {@code Block} in
     * the {@code Blockchain} that include any operation with a specified element.
     * @param element a specified element.
     * @returna new {@code List<Long>} with the required indices.
     */
    private List<Long> findBlocks(T element) {
        List<Long> list = new ArrayList<>();
        long index = 0;
        for (AVLData<T> data : blockchain) {
            if (data.contains(element)) {
                list.add(index);
            }
            index++;
        }
        return list;
    }

    /**
     * This method modify a {@code Block} that has a specified index in the {@code Blockchain}
     * and replace the data with the data of a specified file.
     * @param index the index of the {@code Block} in the {@code Blockchain}.
     * @param filePath the specify absolute path of the file.
     * @throws FileNotFoundException if the specified path is empty.
     */
    public void modify(int index, String filePath) throws FileNotFoundException {
        FileReader fr = new FileReader(filePath);
        StringBuffer info = new StringBuffer("");
        for (String each : fr)
            info.append(each);
        String data = new String(info);
        AVLData<T> newdata = new AVLData<>();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!REVISAR CONSIGNA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        newdata.addModified(((T)data));
        blockchain.setBlock(index, newdata);
    }

    /**
     * This method modify a {@code Block} that has a specified index in the {@code Blockchain}
     * and replace the data with empty data.
     * @param index the index of the {@code Block} in the {@code Blockchain}.
     */
    public void modify(int index){
        AVLData<T> voidSentinelAVLData = new AVLData<>();
        blockchain.setBlock(index, voidSentinelAVLData);
    }

}