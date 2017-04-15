package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Crawler {
	public Crawler() {

	}

	public static void readCoauthorFile() {
		String coauthor = "dblp-paper.txt";

		try {
			BufferedReader in = new BufferedReader(new FileReader(coauthor));
			String s;

			while ((s = in.readLine()) != null) {
				String[] result = s.split("\\|\\|");
				String[] authorList = result[1].split("\\&\\&");


				for(int i =0; i<result.length; i++)
				{
					System.out.println(result[i]);
				}
				for(int i =0; i<authorList.length; i++)
				{
					System.out.println(authorList[i]);
				}




			}
			in.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}


	}

	public static void main(String args[]) {
		readCoauthorFile();
	}
}
