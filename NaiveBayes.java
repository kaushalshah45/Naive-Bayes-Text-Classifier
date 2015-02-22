import java.io.*;
public class NaiveBayes {

	public static void main(String[] args) throws FileNotFoundException, IOException{
		
		double class1 = 0, class0 = 0;
		
		double words_in0 = 0, words_in1 = 0;
		
		
		File trainData = new File("train.csv");
		File trainLabels = new File("train_labels.txt");
		File testData = new File("test.csv");
		File testLabels = new File("test_labels.txt");
		File total_words = new File("word_indices.txt");
		
		BufferedReader train = new BufferedReader(new FileReader(trainData));
		BufferedReader train_label = new BufferedReader(new FileReader(trainLabels));
		BufferedReader test = new BufferedReader(new FileReader(testData));
		BufferedReader test_labels = new BufferedReader(new FileReader(testLabels));
		BufferedReader word_indices = new BufferedReader(new FileReader(total_words));
		
		int count = 0;
		String word_index = null;
		while((word_index = word_indices.readLine()) != null) {
			
			count++;
		}
		word_indices.close();
		
		final double vocab = count;
		
		double [] word_count_class0 = new double[(int)vocab];
		double [] word_count_class1 = new double[(int)vocab];
		
		double [] prob0 = new double[(int)vocab];
		double [] prob1 = new double[(int)vocab];
		
		String label = null;
		String data = null;
		while((label = train_label.readLine()) != null && (data = train.readLine()) != null) {
			
			Integer cl = Integer.parseInt(label);
			String [] words = data.split(",");
			if(cl == 0) {
				class0 += 1;
				
				for(int i = 0; i < words.length; i++) {
					
					Integer temp = Integer.parseInt(words[i]);
					words_in0 += temp;
					word_count_class0[i] += temp;
				}
			}
			else {
				class1 += 1;
				
				for(int i = 0; i < words.length; i++) {
					
					Integer temp = Integer.parseInt(words[i]);
					words_in1 += temp;
					word_count_class1[i] += temp;
				}
			}
		}
		
		double class1_prior = class1 / (class1 + class0);
		double class0_prior = class0 / (class1 + class0);
		
		
		double denom_for_class0 = words_in0 + vocab;
		double denom_for_class1 = words_in1 + vocab;
		
		for(int i = 0; i < prob0.length; i++) {
			
			prob0[i] = Math.log( (1 + word_count_class0[i]) / denom_for_class0 );
			prob1[i] = Math.log( (1 + word_count_class1[i]) / denom_for_class1 );
		}
		
		
		train.close();
		train_label.close();
		
		int test_count0 = 0, test_count1 = 0;
		String testString = null;
		while((testString = test.readLine()) != null) {
			
			double p0 = 1, p1 = 1;
			String [] test_words = testString.split(",");
			for(int i = 0; i < test_words.length; i++) {
				
				Integer t = Integer.parseInt(test_words[i]);
				if(t > 0) {
					
					p0 *= Math.pow(prob0[i], t);
					p1 *= Math.pow(prob1[i], t);
				}
			}
			p0 *= class0_prior;
			p1 *= class1_prior;
			
			if(p0 > p1) 
				test_count0 += 1;
			else 
				test_count1 += 1;
		}
		String tLabels = null;
		int actual_count0 = 0;
		while((tLabels = test_labels.readLine()) != null) {
			
			Integer r = Integer.parseInt(tLabels);
			if(r == 0)
				actual_count0 += 1;
			
		}
		test.close();
		test_labels.close();
		double temporary = (actual_count0 - test_count0);
		double acc = 100 - ((temporary / 1806) * 100);
		System.out.println("Accuracy for the test set is:  "+acc);
	}
	

}
