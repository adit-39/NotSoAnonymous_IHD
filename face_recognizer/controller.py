import face_recognition_test
import keyword_extract
import face_detector
import os



def identify(fileName, path):
    global captionToks
    folder = path.strip().split('/')[-1]
    os.chdir('/home/pydev/hacks/InMobi_ImageCap/face_recognizer')
    numFaces = face_detector.main('/home/pydev/hacks/InMobi_ImageCap/'+ folder +'/'+fileName)
    for face in range(numFaces):
        #print "Face: {}".format(face)
        persons = face_recognition_test.predictPersons(fileName.split('.')[0] + 'Face_{}.jpg'.format(face))
        os.remove(fileName.split('.')[0] + 'Face_{}.jpg'.format(face))
        for person in persons:
            captionToks[fileName].add(person)

def main(path_to_image):
    os.chdir('/home/pydev/hacks/InMobi_ImageCap/neuraltalk2-master')
    os.system('th eval.lua -model /home/pydev/hacks/InMobi_ImageCap/model_id1-501-1448236541.t7_cpu.t7 -image_folder '+path_to_image+' -num_images -1 -gpuid -1 > output.txt')
    global captionToks
    captionToks = keyword_extract.getTokens()
    for k in captionToks.keys():
        l = list(captionToks[k])
        if 'group' in l or 'people' in l or 'men' in l or 'boys' in l or 'friends' in l:
            captionToks[k] = captionToks[k].union(set(['group', 'people', 'men', 'boys', 'friends']))
            identify(k, path_to_image)
        elif 'man' in l or 'woman' in l or 'person' in l:
            identify(k, path_to_image)
        elif 'laptop' in l or 'computer' in l:
            captionToks[k].add('notes')
            captionToks[k].add('documents')
        elif 'plate' in l or 'food' in l or 'sandwich' in l or 'pizza' in l or 'fork' in l:
            captionToks[k].add('cooking')
            captionToks[k].add('food')
        if 'snow' in l or 'slope' in l:
            captionToks[k].add('mountain')
            captionToks[k].add('snow')

    f = open('gallery_metadata.txt', 'w')
    for i in captionToks.keys():
        l = list(captionToks[i])
        s = str(i) + ';'
        metadata = ','.join(l)
        f.write(s+metadata.lower()+'\n')
    f.close()
    return s+metadata.lower()

if __name__=='__main__':
    main('/home/pydev/hacks/InMobi_ImageCap/Gallery')

