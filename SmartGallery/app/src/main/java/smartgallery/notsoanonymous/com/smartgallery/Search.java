package smartgallery.notsoanonymous.com.smartgallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arvind
 */
public class Search {

    private HashMap<String, HashSet<String>> index, reverse_index;
    Context context;

    public Search(Context _context)
    {
        File f = new File(Environment.getExternalStorageDirectory(), "metadata.txt");
        context = _context;
        index = new HashMap<>();
        reverse_index = new HashMap<>();


        String fileString = null;
        try {
            fileString = FileReaderUtil.read(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HashSet<String> temp = new HashSet<>();
//        temp.addAll(Arrays.asList(fileString.split("\\n")[0].trim().split(";")[0].split(",")));
//        temp.addAll(Arrays.asList(fileString.split("\\n")[0].trim().split(";")[1].split(",")));
        for(String line: fileString.split("\\n"))
        {
            HashSet<String> h = new HashSet<>();
//            h.addAll(Arrays.asList(line.trim().split(";")[0].split(",")));
            h.addAll(Arrays.asList(line.trim().split(";")[1].split(",")));
            temp.addAll(h);
            index.put(line.trim().split(";")[0], h);
        }
        for(String tag: temp)
        {
            HashSet<String> h = new HashSet<>();
            for(String key : index.keySet())
            {
                if(index.get(key).contains(tag))
                    h.add(key);
            }
            reverse_index.put(tag, h);
        }
    }

    public HashSet<String> search(HashSet<String> stopwords, String query)
    {
        query = query.replace("me", "chiraag,me");
        HashSet<String> addresses = new HashSet<>();
        String queryTerms[] = query.toLowerCase().split("\\W+");
        addresses.addAll(index.keySet());
        for(String term: queryTerms)
        {
            if(!stopwords.contains(term))
                if(reverse_index.get(term)!=null)
                    addresses.retainAll(reverse_index.get(term));
        }
        if(addresses.size()>0)
        {
            return addresses;
        }
        int maxMatch = 0;
        for(String term: index.keySet())
        {
            addresses = new HashSet<>();
            addresses.addAll(Arrays.asList(queryTerms));
            addresses.retainAll(index.get(term));
            if(maxMatch<addresses.size())
            {
                maxMatch = addresses.size();
            }
        }
        HashSet<String> temp = null;
        for(String term: index.keySet())
        {
            temp = new HashSet<>();
            temp.addAll(Arrays.asList(queryTerms));
            temp.retainAll(index.get(term));
            if(maxMatch==temp.size())
            {
                addresses.add(term);

            }
        }
        return addresses;
    }

    public HashSet<String> stopWordCreator()
    {
        String[] stop = {"i", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am","is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};
        HashSet<String> stopwords = new HashSet<String>();
        for(String s: stop)
        {
            stopwords.add(s);
        }
        return stopwords;
    }

//    private String readFileContents(String filename) {
//        String content = null;
//        File file = new File(filename); //for ex foo.txt
//        FileReader reader = null;
//        try
//        {
//            reader = new FileReader(file);
//            char[] chars = new char[(int) file.length()];
//            reader.read(chars);
//            content = new String(chars);
//            reader.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            if(reader !=null)
//            {
//                try
//                {
//                    reader.close();
//                }
//                catch(IOException ex)
//                {
//                    Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//        return content;
//    }

}