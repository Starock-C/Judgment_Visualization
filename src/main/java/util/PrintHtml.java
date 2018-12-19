package util;
import model.Defendant;
import model.Judgment;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class PrintHtml {
	public static void main(String args[])
	{
//		 String filePath = "E:\\hh_web_space\\ecp\\web\\ecp_web_page\\src\\main\\webapp\\template\\template.html";
//	        String imagePath ="http://localhost:8080/ecp/upload/1461293787628/1461293787628.jpg";
//	        String disrPath = "E:\\hh_web_space\\ecp\\web\\ecp_web_page\\src\\main\\webapp\\template\\";
//	        String fileName = "liuren";
//	        MakeHtml(filePath,disrPath,fileName);

		List<Defendant> defendants=new ArrayList<Defendant>();
		Defendant defendant=new Defendant();
		defendant.setName("jack");
		defendants.add(defendant);
		Judgment judgment=new Judgment();
		judgment.setCaseNumber("123456");
		System.out.println(getNodeName(judgment.getCaseNumber()));
		System.out.println(getLinks(1));
		
//		MakeHtml(filePath, defendants, judgment, disrPath, fileName);
	}

	public static String MakeHtml(List<Defendant> defendants,Judgment judgment){
		String output = "D:\\Desktop";
        try {
//            String title = "<image src="+'"'+imagePath+'"'+"/>";

	        String filePath="D:\\Desktop\\1.html";
	        String disrPath="D:\\Desktop";
	        String fileName="test";
            System.out.print(filePath);
            String templateContent = "";
            FileInputStream fileinputstream = new FileInputStream(filePath);// ��ȡģ���ļ�
            int lenght = fileinputstream.available();
            byte bytes[] = new byte[lenght];
            fileinputstream.read(bytes);
            fileinputstream.close();
            templateContent = new String(bytes);
            
            String replaceNode=getNodeName(judgment.getCaseNumber());
            int i;
            for(i=0;i<defendants.size();i++)
            {
            	replaceNode=replaceNode+getNodeName(defendants.get(i).getName());
            }
            String replaceLinks=getLinks(judgment.getDefendantsCount());
            templateContent = templateContent.replaceAll("###replaceNode###",replaceNode);
//            replaceLinks="{helloworld}";
            templateContent=templateContent.replaceAll("###replaceLinks###", replaceLinks);
            
            
            System.out.println(templateContent);
            
            String fileame = fileName + ".html";
            fileame = disrPath+"\\" + fileame;// ���ɵ�html�ļ�����·����
            FileOutputStream fileoutputstream = new FileOutputStream(fileame);// �����ļ������
            System.out.print("�ļ����·��:");
            System.out.print(fileame);
            byte tag_bytes[] = templateContent.getBytes();
            fileoutputstream.write(tag_bytes);
            fileoutputstream.close();
	        output = fileame;

        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return output;
    }
	public static String getNodeName(String name)
	{
		String NodeName="{name:\""+name+"\"},";
		return NodeName;
	}
	public static String getLinks(int number)
	{
		String links="";
		for(int i=1;i<=number;i++)
		{
//			links=links+"{source:0,target:"+i+"},"+"{source:"+i+",target:0},";
			links=links+"{source:0,target:"+i+"},"+"{source:"+i+",target:0},";
//		links=links+"{source:0,target:"+i+"},";

		}
		return links;
	}
}
