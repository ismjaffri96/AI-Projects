import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
	/*
	 * Returns the k-fold cross validation score of classifier clf on training data.
	 */
	public static double kFoldScore(Classifier clf, List<Instance> traindata, int k, int v) {
		double testDouble = 0.0; // TODO : Implement
		for (int i = 0; i < k; i++) {
			List<Instance> testlist = new ArrayList<Instance>();
			List<Instance> trainlist = new ArrayList<Instance>();
			for (int j = 0; j < traindata.size(); j++) {
				if (i != (j / (traindata.size() / k))) {
					trainlist.add(traindata.get(j));
				} else {
					testlist.add(traindata.get(j));
				}
			}
			clf.train(trainlist, v);
			ClassifyResult training = new ClassifyResult();
			int right = 0;
			int total = 0;
			for (int l = 0; l < testlist.size(); l++) {
				training = clf.classify(testlist.get(l).words);
				total = total + 1;
				if (training.label.equals(testlist.get(l).label)) {
					right = right + 1;
				}
			}

			testDouble = testDouble + (double) right / (double) total;
		}
		double accuracy = testDouble / k;
		return accuracy;

	}
}
