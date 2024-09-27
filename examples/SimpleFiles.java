import java.io.File;
import java.util.ArrayList;

public class SimpleFiles {
    public static ArrayList<File> SimpleFiles(){
        File file1 = new File("imagefiles/simple1.png");
        File file2 = new File("imagefiles/simple2.png");
        File file3 = new File("imagefiles/simple3.png");
        File file4 = new File("imagefiles/simple4.png");
        File file5 = new File("imagefiles/simple5.png");
        ArrayList<File> fileArrayList = new ArrayList<>();
        fileArrayList.add(file1);
        fileArrayList.add(file2);
        fileArrayList.add(file3);
        fileArrayList.add(file4);
        fileArrayList.add(file5);

        return fileArrayList;
    }
}
