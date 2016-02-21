import nltk
import re

def getTokens():
    f = open("../neuraltalk2-master/output.txt", "r")
    output = f.readlines()
    f.close()
    filenames = {}
    for line in range(len(output)):
        if(re.match(r'cp.*',output[line])):
            imgName = output[line].split('"')[1].split('/')[-1]
            filenames[imgName] = set()
            cap = output[line+1].split(':')[-1].strip()
            tagged = nltk.tag.pos_tag(nltk.word_tokenize(cap))
            for word,tag in tagged:
                if(tag=='NNP' or tag=='NN' or tag=='NNS' or tag=='NNPS'):
                    filenames[imgName].add(word)
    #print filenames
    return filenames

if __name__ == "__main__":
    getTokens()
