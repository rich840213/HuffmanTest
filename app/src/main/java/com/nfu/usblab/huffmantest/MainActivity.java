package com.nfu.usblab.huffmantest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {

    private EditText mInputText, mResultTID, mfinalTID;
    private TextView mResultView;

    private Map<Character, Integer> mInitStatistics;
    private String mEndChar = "!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();
    }

    private void findViewById() {
        mInputText = findViewById(R.id.editText);
        mResultView = findViewById(R.id.result);
        mResultTID = findViewById(R.id.result_tid);
        mfinalTID = findViewById(R.id.final_tid);
    }

    public void call(View view) {
        mResultView.setText("");
        mResultTID.setText("");

        if (!mInputText.getText().toString().isEmpty()) {
            String oriStr = mInputText.getText().toString();
            Map<Character, Integer> statistics = statistics(oriStr.toCharArray());
            mInitStatistics = statistics;
//            String str = "";

//            for (Map.Entry<Character, Integer> entry : statistics.entrySet()) {
//                str += entry.getKey() + "\t" + entry.getValue() + "\n";
//            }

            String encodedBinariStr = encode(oriStr, statistics);
            String decodedStr = decode(encodedBinariStr, statistics);
            System.out.println("Original string: " + oriStr);
            System.out.println("Huffman encode binary string: " + encodedBinariStr);
            System.out.println("decoded string from binary string: " + decodedStr);
            System.out.println("binary string of UTF-8: " + getStringOfByte(oriStr, Charset.forName("UTF-8")));
            System.out.println("binary string of UTF-16: " + getStringOfByte(oriStr, Charset.forName("UTF-16")));
            System.out.println("binary string of US-ASCII: " + getStringOfByte(oriStr, Charset.forName("US-ASCII")));
            System.out.println("binary string of GB2312: " + getStringOfByte(oriStr, Charset.forName("GB2312")));
            System.out.println("Huffman encode binary string: " + encodedBinariStr);
            System.out.println("8 multiples: " + check8Multiples(encodedBinariStr));

            String final_tid = "4054" + binarytoHexString(check8Multiples(encodedBinariStr)) + "000000010001";
            if (final_tid.length() > 32) {
                mfinalTID.setText(final_tid + " -F -" + final_tid.length());
            } else {
                mfinalTID.setText(final_tid + " -T -" + final_tid.length());
            }
        }
    }

    private String check8Multiples(String str) {
        int size = str.length();
        int new_size;
        int i = 0;

//        if (size % 8 != 0) {
//            do {
//                str += "0";
//                i++;
//            } while (str.length() % 8 != 0);
//        }

        // A test
        if (size % 4 != 0) {
            do {
                i++;
                new_size = size + i;
            } while (new_size % 4 != 0);
        }

        System.out.println("add zero: " + i);
        if (i != 0) {
            // A test
            str = String.format("%0" + i + "d", 0) + str;

            mEndChar = String.valueOf(i);
        } else {
            mEndChar = "!";
        }

        return str;
    }

    private void binarytoString(String str) {
        String s = "";
        ArrayList<String> list = new ArrayList<>();

        for (int index = 0; index < str.length(); index += 8) {
            String temp = str.substring(index, index + 8);
            System.out.println("string: " + temp);
            int num = Integer.parseInt(temp, 2);
            char letter = (char) num;

            System.out.println(DataTransform.byteArrayToHexStr(DataTransform.strToByteArray(temp)));
            list.add(DataTransform.byteArrayToHexStr(DataTransform.strToByteArray(temp)));
        }

        for (int i = 0; i < list.size(); i++) {
            s += list.get(i);
        }
        mResultView.setText(s);
        System.out.println(s);
    }

    private String binarytoHexString(String str) {
        String s = "";
        ArrayList<String> list = new ArrayList<>();

        try {
            for (int index = 0; index < str.length(); index += 4) {
                String temp = str.substring(index, index + 4);
                System.out.println("string: " + temp);
                int num = Integer.parseInt(temp, 2);

                System.out.println(Integer.toString(num, 16));
                list.add(Integer.toString(num, 16));
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            s += list.get(i);
        }

        mResultView.setText(s);
        System.out.println("binarytoHexString: " + s);
        System.out.println(DataTransform.hexToBin(s) + mEndChar);

        String reductionBinString = reductionBinary(DataTransform.hexToBin(s) + mEndChar, mEndChar);
        System.out.println("reductionBinary: " + reductionBinString);

        // 還原
        String decodeString = decode(reductionBinString, mInitStatistics);
        if (decodeString.equals(mInputText.getText().toString())) {
            mResultTID.setText(decodeString + "-T");
        } else {
            mResultTID.setText(decodeString + "-F");
        }

        return s;
    }

    private String reductionBinary(String s, String num) {
        System.out.println("input: " + s);
        int size = s.length();
        String endChar = s.substring(size - 1, size);
        String str = "";

        if (endChar.equals("!")) {
            str = s.substring(0, size - 1);
        } else {
            if (DataTransform.isNumeric(endChar)) {
                str = s.substring(Integer.parseInt(num), size - 1);
            }
        }

        System.out.println("output: " + str);
        return str;
    }

    public static String getStringOfByte(String str, Charset charset) {
        if (str == null || str.equals("")) {
            return "";
        }
        byte[] byteArray = str.getBytes(charset);
        int size = byteArray.length;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            byte temp = byteArray[i];
            buffer.append(getStringOfByte(temp));
        }
        return buffer.toString();
    }

    public static String getStringOfByte(byte b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            byte temp = (byte) ((b >> i) & 0x1);
            buffer.append(String.valueOf(temp));
        }
        return buffer.toString();
    }

    private Map<Character, Integer> statistics(char[] charArray) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char c : charArray) {
            Character character = new Character(c);
            if (map.containsKey(character)) {
                map.put(character, map.get(character) + 1);
            } else {
                map.put(character, 1);
            }
        }
        return map;
    }

    private static Tree buildTree(Map<Character, Integer> statistics, List<Node> leafs) {
        Character[] keys = statistics.keySet().toArray(new Character[0]);
        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
        for (Character character : keys) {
            Node node = new Node();
            node.chars = character.toString();
            node.frequence = statistics.get(character);
            priorityQueue.add(node);
            leafs.add(node);
        }
        int size = priorityQueue.size();
        for (int i = 1; i <= size - 1; i++) {
            Node node1 = priorityQueue.poll();
            Node node2 = priorityQueue.poll();
            Node sumNode = new Node();
            sumNode.chars = node1.chars + node2.chars;
            sumNode.frequence = node1.frequence + node2.frequence;
            sumNode.leftNode = node1;
            sumNode.rightNode = node2;
            node1.parent = sumNode;
            node2.parent = sumNode;
            priorityQueue.add(sumNode);
        }
        Tree tree = new Tree();
        tree.root = priorityQueue.poll();
        return tree;
    }

    // Tree
    public static class Tree {
        private Node root;

        public Node getRoot() {
            return root;
        }

        public void setRoot(Node root) {
            this.root = root;
        }
    }

    public static class Node implements Comparable<Node> {
        private String chars = "";
        private int frequence = 0;
        private Node parent;
        private Node leftNode;
        private Node rightNode;

        @Override
        public int compareTo(Node n) {
            return frequence - n.frequence;
        }

        public boolean isLeaf() {
            return chars.length() == 1;
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.leftNode;
        }

        public int getFrequence() {
            return frequence;
        }

        public void setFrequence(int frequence) {
            this.frequence = frequence;
        }

        public String getChars() {
            return chars;
        }

        public void setChars(String chars) {
            this.chars = chars;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeftNode() {
            return leftNode;
        }

        public void setLeftNode(Node leftNode) {
            this.leftNode = leftNode;
        }

        public Node getRightNode() {
            return rightNode;
        }

        public void setRightNode(Node rightNode) {
            this.rightNode = rightNode;
        }
    }

    // 編碼
    public static String encode(String originalStr, Map<Character, Integer> statistics) {
        if (originalStr == null || originalStr.equals("")) {
            return "";
        }
        char[] charArray = originalStr.toCharArray();
        List<Node> leafNodes = new ArrayList<Node>();
        buildTree(statistics, leafNodes);
        Map<Character, String> encodInfo = buildEncodingInfo(leafNodes);
        StringBuffer buffer = new StringBuffer();
        for (char c : charArray) {
            Character character = new Character(c);
            buffer.append(encodInfo.get(character));
        }

        System.out.println(buffer.toString().length());
        return buffer.toString();
    }

    private static Map<Character, String> buildEncodingInfo(List<Node> leafNodes) {
        Map<Character, String> codewords = new HashMap<Character, String>();
        for (Node leafNode : leafNodes) {
            Character character = new Character(leafNode.getChars().charAt(0));
            String codeword = "";
            Node currentNode = leafNode;
            do {
                if (currentNode.isLeftChild()) {
                    codeword = "0" + codeword;
                } else {
                    codeword = "1" + codeword;
                }
                currentNode = currentNode.parent;
            } while (currentNode.parent != null);
            codewords.put(character, codeword);
        }
        return codewords;
    }

    // 解碼
    public static String decode(String binaryStr, Map<Character, Integer> statistics) {
        try {
            if (binaryStr == null || binaryStr.equals("")) {
                return "";
            }
            char[] binaryCharArray = binaryStr.toCharArray();
            LinkedList<Character> binaryList = new LinkedList<Character>();
            int size = binaryCharArray.length;
            for (int i = 0; i < size; i++) {
                binaryList.addLast(new Character(binaryCharArray[i]));
            }
            List<Node> leafNodes = new ArrayList<Node>();
            Tree tree = buildTree(statistics, leafNodes);
            StringBuffer buffer = new StringBuffer();
            while (binaryList.size() > 0) {
                Node node = tree.root;
                do {
                    Character c = binaryList.removeFirst();
                    if (c.charValue() == '0') {
                        node = node.leftNode;
                    } else {
                        node = node.rightNode;
                    }
                } while (!node.isLeaf() && (binaryList.size() != 0));
                buffer.append(node.chars);
            }

            return buffer.toString();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
