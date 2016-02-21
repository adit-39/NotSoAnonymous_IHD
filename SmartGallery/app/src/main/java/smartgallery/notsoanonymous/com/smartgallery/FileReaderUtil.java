package smartgallery.notsoanonymous.com.smartgallery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderUtil {
    public static String read(File file) throws FileNotFoundException {
        String content = null;
        FileReader reader = null;
        try
        {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(reader !=null)
            {
                try
                {
                    reader.close();
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return content;
    }
}
