package util;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class POI {
	public static String readWord(String name)
	{
		FileInputStream in;
		String text = null;
		try
		{
			in = new FileInputStream(name);
			System.out.println(name);
			if (name.contains("docx")){
				XWPFDocument doc = new XWPFDocument(in);
				XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
				text = extractor.getText();
			}
			else if (name.contains("docm")){
				XWPFDocument doc = new XWPFDocument(in);
				XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
				text = extractor.getText();
			}
			else if (name.contains("pdf")) {
			}
			else {
				WordExtractor extractor = new WordExtractor(in);
				text = extractor.getText();
			}

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IllegalArgumentException e){
			e.printStackTrace();
		}catch (RecordFormatException e){
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
}
