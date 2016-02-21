import glob, os, sys
from PIL import Image
import cv2

i = 0
os.mkdir('results/'+sys.argv[1])
for imfile in glob.glob('yalefaces/'+sys.argv[1]+'*'):
	imVec = cv2.imread(imfile)
	print imVec.shape
	greyIm = cv2.cvtColor(imVec, cv2.COLOR_BGR2GRAY)
	im = Image.fromarray(greyIm)
	im.resize((150,150), Image.ANTIALIAS)
	im.save('results/'+sys.argv[1]+'/'+sys.argv[1]+'_'+str(i)+'.jpg')
	i+=1
