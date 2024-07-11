package com.test.export;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;

public class WordExporter {

    public static void main(String[] args) {
        // 创建一个空的Word文档
        XWPFDocument document = new XWPFDocument();

        // 创建一个段落
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("Hello, this is a test Word document created using Apache POI.");

        // 导出Word文档
        try (FileOutputStream out = new FileOutputStream("example.docx")) {
            document.write(out);
            System.out.println("Word document created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}