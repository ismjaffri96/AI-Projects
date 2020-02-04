import java.util.ArrayList;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 */
public class DecisionTreeImpl {
    public DecTreeNode root;
    public List<List<Integer>> trainData;
    public int maxPerLeaf;
    public int maxDepth;
    public int numAttr;

    // Build a decision tree given a training set
    DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
        this.trainData = trainDataSet;
        this.maxPerLeaf = mPerLeaf;
        this.maxDepth = mDepth;
        if (this.trainData.size() > 0)
            this.numAttr = trainDataSet.get(0).size() - 1;
        this.root = buildTree();
    }

    private double log2(double k) {
        if (k == 0.0) {
            return 0.0; // it may be just 0
        } else {
            return (Math.log(k) / Math.log(2));
        }
    }

    private double division(double a, double b) {
        return a / b;
    }

    private double condEntropy(List<List<Integer>> datalist, int attribute, int threshold) {
        double counter0small = 0;
        double counter0big = 0;
        double counter1small = 0;
        double counter1big = 0;
        double listSize = 1.0 * datalist.size();
        double condEnt = 0;
        for (int i = 0; i < datalist.size(); i++) {
            List<Integer> firstList = datalist.get(i);

            if (firstList.get(attribute) <= threshold && firstList.get(numAttr) == 0) {
                counter0small++;
            }
            if (firstList.get(attribute) <= threshold && firstList.get(numAttr) == 1) {
                counter1small++;
            }

            if (firstList.get(attribute) > threshold && firstList.get(numAttr) == 0) {
                counter0big++;
            }
            if (firstList.get(attribute) > threshold && firstList.get(numAttr) == 1) {
                counter1big++;
            }

        }

        double lessprob = division((counter0small + counter1small), listSize);
        double bigprob = division((counter0big + counter1big), listSize);
        double lessprob0 = division(counter0small, (counter0small + counter1small));
        double bigprob0 = division(counter0big, (counter1big + counter0big));
        double lessprob1 = division(counter1small, (counter0small + counter1small));
        double bigprob1 = division(counter1big, (counter1big + counter0big));
        condEnt = -(lessprob * (lessprob0 * log2(lessprob0) + lessprob1 * log2(lessprob1))
                + bigprob * (bigprob0 * log2(bigprob0) + (bigprob1) * log2(bigprob1)));

        return condEnt;
    }

    private double retrieveCondEnt(List<List<Integer>> data) { 
                                                                
        double num = 0;
        double num2 = 0;

        for (int i = 0; i < data.size(); i++) {
            List<Integer> list2 = data.get(i);

            if (list2.get(numAttr) == 1) {
                num = num + 1;
            }
            if (list2.get(numAttr) == 0) {
                num2 = num2 + 1;
            }
        }
        double num2prob = division(num2, data.size());
        double numprob = division(num, data.size());
        double entropy = -(num2prob * log2(num2prob) + numprob * log2(numprob));

        return entropy;
    }

    private double infoGain(List<List<Integer>> datalist3, int attributeVal, int thresholdVal) {
        double infogain = this.retrieveCondEnt(datalist3) - condEntropy(datalist3, attributeVal, thresholdVal);
        
        return infogain;

    }



    private DecTreeNode buildTreeHelper(List<List<Integer>> datalist, int depth) {
        int num0 = 0;
        int num1 = 0;
        int majority = 0;
        int attribute = 0;
        int threshold = 0;
        double infogain = 0.0;
        boolean identical = true;
        List<List<Integer>> smallDataList = new ArrayList<List<Integer>>();
        List<List<Integer>> bigDataList = new ArrayList<List<Integer>>();
        
        if (datalist.size() == 0) {
            return new DecTreeNode(1, 0, 0);
        }

        
        
        for (int i = 0; i < numAttr; i++) { 
            for (int j = 1; j <= 9 ; j++) { 
                double infogained = this.infoGain(datalist, i, j);

                if (infogained > infogain) {
                    infogain = infogained;
                    attribute = i;
                    threshold = j;
                }
            }
        }
        
      // search for identical node labels 
        for (int i = 0; i < datalist.size() -1 ; i++) {
            
            if (datalist.get(i).get(datalist.get(i).size() - 1) != datalist.get(i + 1).get(datalist.get(i).size() - 1)) {
                identical = false;
            }
        }
        
        // majority calculation 
        for (int i = 0; i < datalist.size() ; i++) {
            if (datalist.get(i).get(numAttr) == 0) {
                num0++;
            } 
            if (datalist.get(i).get(numAttr) == 1){
                num1++;
            } }
        
         if(num0 > num1) {
             majority = 0;
         }
         if(num0 <= num1) {
             majority = 1;
         }
        
        
        DecTreeNode node2 = new DecTreeNode(majority, attribute, threshold);
        
        if (identical == true || maxPerLeaf > datalist.size() || this.infoGain(datalist, attribute, threshold) == 0 || this.maxDepth == depth) {
            return node2;
        }

        for (int i = 0; i < datalist.size(); i++) {
            if (datalist.get(i).get(attribute) <= threshold) {
                smallDataList.add(datalist.get(i));

            } 
            if (datalist.get(i).get(attribute) > threshold) {
                bigDataList.add(datalist.get(i));
            }
        }
            node2.left = buildTreeHelper(smallDataList, depth + 1);
            node2.right = buildTreeHelper(bigDataList, depth + 1);

        return node2;

    }
    private DecTreeNode buildTree() {
        DecTreeNode node = buildTreeHelper(this.trainData, 0);
        return node;
    }

    public int classify(List<Integer> instance) {
          int classify = classifyhelper(root,instance).classLabel;
        return classify;
    }

    public DecTreeNode classifyhelper(DecTreeNode node, List<Integer> data) { // fix this code!!!!!!!!
        if (node.isLeaf()) {
            return node;
        }
        if (data.get(node.attribute) > node.threshold) {
            node = node.right;
            return classifyhelper(node, data);

        } else {
            node = node.left;
            return classifyhelper(node, data);
        }

    }

    // Print the decision tree in the specified format
    public void printTree() {
        printTreeNode("", this.root);
    }

    public void printTreeNode(String prefixStr, DecTreeNode node) {
        String printStr = prefixStr + "X_" + node.attribute;
        System.out.print(printStr + " <= " + String.format("%d", node.threshold));
        if (node.left.isLeaf()) {
            System.out.println(" : " + String.valueOf(node.left.classLabel));
        } else {
            System.out.println();
            printTreeNode(prefixStr + "|\t", node.left);
        }
        System.out.print(printStr + " > " + String.format("%d", node.threshold));
        if (node.right.isLeaf()) {
            System.out.println(" : " + String.valueOf(node.right.classLabel));
        } else {
            System.out.println();
            printTreeNode(prefixStr + "|\t", node.right);
        }
    }

    public double printTest(List<List<Integer>> testDataSet) {
        int numEqual = 0;
        int numTotal = 0;
        for (int i = 0; i < testDataSet.size(); i++) {
            int prediction = classify(testDataSet.get(i));
            int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
            System.out.println(prediction);
            if (groundTruth == prediction) {
                numEqual++;
            }
            numTotal++;
        }
        double accuracy = numEqual * 100.0 / (double) numTotal;
        System.out.println(String.format("%.2f", accuracy) + "%");
        return accuracy;
    }
}
