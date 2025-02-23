package org.example.util;

import org.example.render.Cell;
import org.example.render.Scene;

import java.awt.*;
import java.io.*;

public class TextToSceneConverter {

    public static Scene convert(String path){
        return convert(path,new Pos(0,0));
    }

    public static Scene convert(String path, Pos coordinate){
        try {
            InputStream is = TextToSceneConverter.class.getClassLoader().getResourceAsStream(path);

            if (is == null) {
                System.err.println("Resource not found: " + path);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            StringBuilder builder = new StringBuilder();
            int height = 0;
            int width = 0;
            while (line != null){
                builder.append(line);
                builder.append("\n");
                height++;
                width = Math.max(width,line.length());
                line = reader.readLine();
            }
            Cell[][] cells = new Cell[height][width];
            int y = 0;
            for (String str : builder.toString().split("\n")){
                char[] chars = str.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    cells[y][i] = new Cell(Color.WHITE, chars[i],false);
                }
                y++;
            }
            return new Scene(cells,coordinate);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
