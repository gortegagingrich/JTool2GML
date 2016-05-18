/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmap2gml;

import java.awt.Color;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author gabrielo
 */
public class ItemFromFile extends Item {

    public ItemFromFile(String[] str) {
        super(str);
        
        parseFile(str[2],"ItemConfig");
    }
    
    private void parseFile(String objId, String fName) {
        String[] tmp,tmp2;
        int[] tmp3;
        int i;
        File f;
        Scanner s;
        
        try {
            f = new File(fName);
            s = new Scanner(f);
            
            while (s.hasNext()) {
                tmp = s.nextLine().split(";");
                System.out.println(tmp[0]);
                
                if (tmp.length > 0 && tmp[0].equals(objId)) {
                    // get x values
                    tmp2 = tmp[1].split(",");
                    xArr = new int[tmp2.length];
                    
                    for (i = 0; i < xArr.length; i++) {
                        xArr[i] = Integer.parseInt(tmp2[i]);
                    }
                    
                    // get y values
                    tmp2 = tmp[2].split(",");
                    yArr = new int[tmp2.length];
                    
                    for (i = 0; i < yArr.length; i++) {
                        yArr[i] = Integer.parseInt(tmp2[i]);
                    }
                    
                    // get fill color
                    tmp2 = tmp[3].split(",");
                    tmp3 = new int[tmp2.length];
                    
                    for (i = 0; i < tmp3.length; i++) {
                        tmp3[i] = Integer.parseInt(tmp2[i]);
                    }
                    
                    color1 = new Color(tmp3[0],tmp3[1],tmp3[2],tmp3[3]);
                    
                    // get outline color
                    tmp2 = tmp[4].split(",");
                    tmp3 = new int[tmp2.length];
                    
                    for (i = 0; i < tmp3.length; i++) {
                        tmp3[i] = Integer.parseInt(tmp2[i]);
                    }
                    
                    color2 = new Color(tmp3[0],tmp3[1],tmp3[2],tmp3[3]);
                    
                    // get depth
                    depth = Integer.parseInt(tmp[5]);
                }
            }
        } catch (Exception e) {
            
        }
    }
    
}
