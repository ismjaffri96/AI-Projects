import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {
    
    private Map<String,Integer> negWordsPerLabel = new HashMap<String,Integer>();
    private Map<String,Integer> posWordsPerLabel = new HashMap<String,Integer>();
    private Map<Label,Integer> labelWordCount = new HashMap<Label,Integer>();
    private int totalwordcount = 0; //think about this one more.
    private int neg1 = 0;
    private int pos1 = 0;
        
   

    /**
     * Trains the classifier with the provided training data and vocabulary size
     */
    @Override
    public void train(List<Instance> trainData, int v) {
        // TODO : Implement
        // Hint: First, calculate the documents and words counts per label and store them. 
        // Then, for all the words in the documents of each label, count the number of occurrences of each word.
        // Save these information as you will need them to calculate the log probabilities later.
        //
        // e.g.
        // Assume m_map is the map that stores the occurrences per word for positive documents
        // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
        // m_map.get("asdasd") would return null, when the word has not appeared before.
        // Use m_map.put(word,1) to put the first count in.
        // Use m_map.replace(word, count+1) to update the value
        Map<String,Integer> negMap = new HashMap<String,Integer>(); //may have to change to map
        Map<String,Integer> posMap = new HashMap<String,Integer>(); //may have to change to map
        
 
        this.totalwordcount = v;
        for (int i = 0; i < trainData.size(); i++) {
          if (trainData.get(i).label == Label.POSITIVE) {
            for (int j = 0; j < trainData.get(i).words.size(); j++) {
              if (posMap.get(trainData.get(i).words.get(j)) != null) {
                int occurrence = posMap.get(trainData.get(i).words.get(j));
                posMap.replace(trainData.get(i).words.get(j), occurrence + 1);
              }
              else {
                posMap.put(trainData.get(i).words.get(j), 1);
              }
            }
          }
          else if (trainData.get(i).label == Label.NEGATIVE){
            for (int j = 0; j < trainData.get(i).words.size(); j++) {
              if (negMap.get(trainData.get(i).words.get(j)) != null) {
                int occurrence = negMap.get(trainData.get(i).words.get(j));
                negMap.replace(trainData.get(i).words.get(j), occurrence + 1);
              }
              else {
                negMap.put(trainData.get(i).words.get(j), 1);
              }
            }
            
          }
          
        }
        
        
        getDocumentsCountPerLabel(trainData);
        labelWordCount = getWordsCountPerLabel(trainData);
        negWordsPerLabel = negMap;
        posWordsPerLabel = posMap;
      
        
        
      
    }

    /*
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
        int positiveCount = 0;
        int negativeCount = 0;
        Map<Label,Integer> map = new HashMap<Label,Integer>();//ADD A HASHMAP!!!
            
        for (int i = 0; i < trainData.size(); i++) {
          if (trainData.get(i).label == Label.POSITIVE) { //perhaps change to an iterator
            positiveCount = positiveCount + trainData.get(i).words.size();           
          }
          else if (trainData.get(i).label == Label.NEGATIVE) { //maybe change to just else {
            negativeCount = negativeCount + trainData.get(i).words.size();
          }
        }
        map.put(Label.POSITIVE, positiveCount);
        map.put(Label.NEGATIVE, negativeCount);
        return map; //RETURN A HASHMAP!
    }


    /*
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
        // TODO : Implement
      int positiveCount = 0;
      int negativeCount = 0;
      Map<Label,Integer> map = new HashMap<Label,Integer>();//ADD A HASHMAP!!!
          
      for (int i = 0; i < trainData.size(); i++) {
        if (trainData.get(i).label == Label.POSITIVE) { //perhaps change to an iterator!
          positiveCount = positiveCount + 1;            
        }
        else if (trainData.get(i).label == Label.NEGATIVE) { //perhaps change to just else {
          negativeCount = negativeCount + 1;
        }

        
      }
      
      map.put(Label.POSITIVE, positiveCount);
      map.put(Label.NEGATIVE, negativeCount);
      this.neg1 = negativeCount;//understand this
      this.pos1 = positiveCount;//understand this
      
      return map; //RETURN A HASHMAP!
    }


    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
     */
    private double p_l(Label label) {
        // TODO : Implement
        double probTotal = neg1 + pos1;
        if (label.equals(Label.NEGATIVE)) {
          return (double) neg1/  probTotal;
        }
        else {
          return (double) pos1 /  probTotal;
        }
          // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.

    }

    /**
     * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE) or
     * P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
        double clw = 0.0;
        double totalw = 0.0;
        
        if (label == Label.POSITIVE) {
          totalw = labelWordCount.get(Label.POSITIVE);
          
        }
        else {
          totalw = labelWordCount.get(Label.NEGATIVE);
        }
        double bottom = (double)totalwordcount + totalw;
       //check label of the given word
        if (label.equals(Label.POSITIVE)) {
          if (posWordsPerLabel.get(word) == null) {
            return 1.0 / bottom;
          }
          clw = posWordsPerLabel.get(word);
          
        }
        else {
          if (negWordsPerLabel.get(word) == null) {
            return 1.0 / bottom;
          }
          clw = negWordsPerLabel.get(word); 
        }
        return (clw + 1.0) / bottom;
        
        // Calculate the probability with Laplace smoothing for word in class(label)
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // TODO : Implement
        // Sum up the log probabilities for each word in the input data, and the probability of the label
        // Set the label to the class with larger log probability
        double positiveLog = Math.log(p_l(Label.POSITIVE));
        double negativeLog =  Math.log(p_l(Label.NEGATIVE));
        
        for( int i = 0; i < words.size(); i++) {
        	positiveLog = positiveLog + Math.log(p_w_given_l(words.get(i), Label.POSITIVE));
        	negativeLog = negativeLog + Math.log(p_w_given_l(words.get(i), Label.NEGATIVE));
			
		}
        
        ClassifyResult nextResult = new ClassifyResult();
        nextResult.logProbPerLabel = new HashMap<Label, Double>();
        
        if (positiveLog < negativeLog) {
          nextResult.label = Label.NEGATIVE;
        }
        else {
          nextResult.label = Label.POSITIVE;
        }
        
        nextResult.logProbPerLabel.put(Label.POSITIVE, positiveLog);
        nextResult.logProbPerLabel.put(Label.NEGATIVE, negativeLog);
        
        return nextResult;
    }


}
