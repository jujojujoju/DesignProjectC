package project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TimerTask;

public class Crawler extends TimerTask implements Runnable {
	public Crawler() {

	}

	public void run() {
		try {
			readFile();
		}catch(Exception e) {
		}
	}

	public boolean readFile(){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = Files.newInputStream(Paths.get("paperList4.txt"));
			{
  			/* Read decorated stream (dis) to EOF as normal... */
			}
			DigestInputStream dis = new DigestInputStream(is, md);
			byte[] digest = md.digest();
			for(byte b: digest) {
				final StringBuilder builder = new StringBuilder();
				builder.append(String.format("%02x", b));
				System.out.print(builder.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
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
			System.err.println(e); // ������ �ִٸ� �޽��� ���
			System.exit(1);
		}


	}

	public static void main(String args[]) {
		readCoauthorFile();
	}
}
