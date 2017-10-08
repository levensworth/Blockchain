package Model;

import Model.DataStructures.AVLData;
import Model.DataStructures.AVLTree;
import Model.DataStructures.Blockchain;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AVLBlockchain<T> {

    private Blockchain<AVLData<T>> blockchain;
    private AVLTree<T> tree;
    private AVLData<T> voidSentinelAVLData;

    public AVLBlockchain(int zeros, Comparator<T> cmp) throws CloneNotSupportedException, NoSuchAlgorithmException {
        this.blockchain = new Blockchain<>(zeros);
        this.tree = new AVLTree<>(cmp);
        this.voidSentinelAVLData = new AVLData<T>();
    }

    public void add(T element) {
        AVLData<T> data = tree.insert(element);
        blockchain.add(data);
    }

    public void remove(T element) {
        AVLData<T> data = tree.remove(element);
        blockchain.add(data);
    }

    public List<Long> lookup(T element) {
        AVLData<T> data = tree.search(element);
        blockchain.add(data);

        if(data.getResult()) {
            return null;
        }
        return findBlocks(element);
    }

    private List<Long> findBlocks(T element) {
        List<Long> list = new ArrayList<>();
        long index = 0;
        for(AVLData<T> data: blockchain) {
            if (data.contains(element)){
                list.add(index);
            }
            index++;
        }
        return list;
    }

    public void modify(int index, String filePath) throws FileNotFoundException {
        FileReader fr = new FileReader(filePath);
        StringBuffer info = new StringBuffer("");
        for(String each : fr)
            info.append(each);
        String data = new String(info);
        AVLData<T> newdata = new AVLData<T>();
        newdata.addModified(((Object)data));
        blockchain.setBlock(index, newdata);
    }
    public void modify(int index){
        blockchain.setBlock(index, this.voidSentinelAVLData);
    }
}
