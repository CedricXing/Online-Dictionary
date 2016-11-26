/**
 * Created by FelixXiao on 2016/11/25.
 */
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnlineTranslation {
//    public static void main(String[] args) {
//        OnlineTranslation onlineTranslation = new OnlineTranslation();
//
//        ArrayList<String> arrayList = onlineTranslation.icibaTranslation("get");
//        for(int i = 0; i < arrayList.size(); i++)
//            System.out.println(arrayList.get(i));
//    }
    //constructor
    public OnlineTranslation() {
    }

    //youdao translation
    public ArrayList<String> youdaoTranslation(String word) {
        String urlString = "http://dict.youdao.com/w/eng/" + word + "/#keyfrom=dict2.index";
        URL url = null;
        java.util.Scanner input = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            url = new URL(urlString);
            input = new java.util.Scanner(url.openStream());
            boolean isBegin = false;
            boolean isEnglish = true;
            Pattern trans = Pattern.compile(".*<li>(.*)</li>");
            Pattern pronounce = Pattern.compile(".*<span class=\"phonetic\">(.*)</span>");
            while(input.hasNext()) {
                String temp = input.nextLine();
                Matcher mTrans = trans.matcher(temp);
                Matcher mPronounce = pronounce.matcher(temp);

                //get pronounce
                if(mPronounce.matches() && isEnglish ) {
                    list.add("英 " + mPronounce.group(1));
                    isEnglish = false;
                }
                else if(mPronounce.matches() && !isEnglish ) {
                    list.add("美 " + mPronounce.group(1));
                    isBegin = false;
                }

                //get translation
                if(Pattern.matches(".*<div class=\"trans-container\">",temp))
                    isBegin = true;
                if(mTrans.matches() && isBegin ) {
                    list.add(mTrans.group(1));
                }
                if(Pattern.matches(".*</div>",temp) && isBegin)
                    isBegin = false;
            }
        }
        catch(MalformedURLException ex) {
            System.out.println("Can't open!");
            ex.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //iciba translation
    public ArrayList<String> icibaTranslation(String word) {
        String urlString = "http://www.iciba.com/" + word;
        URL url = null;
        java.util.Scanner input = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            url = new URL(urlString);
            input = new java.util.Scanner(url.openStream());
            boolean isBegin = false;
            boolean isEnglish = true;
            Pattern trans = Pattern.compile(".*<span>(.*)</span>");
            Pattern pronounce = Pattern.compile(".*<span>(.*)</span>");
            while(input.hasNext()) {
                String temp = input.nextLine();
                Matcher mPronounce = pronounce.matcher(temp);
                //get pronounce
                if(Pattern.matches(".*<div class=\"base-speak\">.*",temp))
                    isBegin = true;
                if(mPronounce.matches() && isBegin && isEnglish ) {
                    list.add(mPronounce.group(1));
                    isEnglish = false;
                }
                else if(mPronounce.matches() && isBegin && !isEnglish ) {
                    list.add(mPronounce.group(1));
                    isBegin = false;
                }

                //get translation
                if(Pattern.matches(".*<ul class=\"base-list switch_part\" class=\"\">",temp)) {
                    Pattern characteristic = Pattern.compile(".*<span class=\"prop\">(.*)</span>");
                    temp = input.nextLine();
                    while(!Pattern.matches(".*<div class=\"base-bt-bar\">",temp)) {
                        Matcher mChar = characteristic.matcher(temp);
                        //boolean isChar = false;
                        if(mChar.matches()) {
                            StringBuffer stringBuffer = new StringBuffer(mChar.group(1));
                            temp = input.nextLine();
                            while(!Pattern.matches(".*</li>",temp)) {
                                Matcher mTrans = trans.matcher(temp);
                                if(mTrans.matches())
                                    stringBuffer.append(mTrans.group(1));
                                temp = input.nextLine();
                            }
                            list.add(stringBuffer.toString());
                        }
                        temp = input.nextLine();
                    }
                }
            }
        }
        catch(MalformedURLException ex) {
            System.out.println("Can't open!");
            ex.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //bing translation
    public ArrayList<String> bingTranslation(String word) {
        String urlString = "http://cn.bing.com/dict/search?q=" + word + "&go=搜索&qs=n&form=Z9LH5&pq=hello&sc=7-5&sp=-1&sk=&cvid=0D404E73D43746D98E60093F005DC018";
        URL url = null;
        java.util.Scanner input = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            url = new URL(urlString);
            input = new java.util.Scanner(url.openStream());
            Pattern p = Pattern.compile(".*<meta name=\"description\" content=\"必应词典为您提供" + word + "的释义，(.*)\" /><meta content.*");
            while(input.hasNext()) {
                String temp = input.nextLine();
                Matcher m = p.matcher(temp);
                if(m.matches()) {
                    String translation = m.group(1);
                    String[] afterSplit = translation.split("，");
                    String trans;
                    if(afterSplit.length == 3) {
                        list.add(afterSplit[0]);
                        list.add(afterSplit[1]);
                        trans = afterSplit[2];
                    }
                    else {
                        trans = afterSplit[0];
                    }
                    String[] transAfterSplit = trans.split("； ");
                    for(int i = 0; i < transAfterSplit.length; i++)
                        list.add(transAfterSplit[i]);
                }
            }
        }
        catch(MalformedURLException ex) {
            System.out.println("Can't open!");
            ex.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
