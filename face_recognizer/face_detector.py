from PIL import Image
import sys, cv2


def cascade_detect(cascade, image):
    gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    return cascade.detectMultiScale(
            gray_image,
            scaleFactor=1.15,
            minNeighbors=5,
            minSize=(30, 30),
            flags=cv2.CASCADE_SCALE_IMAGE
    )

def detections_draw(image, detections, imgPth):
    i = 0
    for (x, y, w, h) in detections:
        #print "({0}, {1}, {2}, {3})".format(x, y, w, h)
        cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)
        PILimage = Image.fromarray(image)
        faceImg = PILimage.crop((x, y, x + w, y + h))
        faceToSave = faceImg.resize((150,150), Image.ANTIALIAS)
        faceToSave.save(open(imgPth.strip().split('/')[-1].split('.')[0] + "Face_{}.jpg".format(i), "w"))
        i += 1

def main(image_path):
    #if argv is None:
    #    argv = sys.argv

    cascade_path = 'haarcascade_frontalface_default.xml'
    #image_path = sys.argv[2]
    #result_path = sys.argv[3] if len(sys.argv) > 3 else None

    cascade = cv2.CascadeClassifier(cascade_path)
    image = cv2.imread(image_path)
    if image is None:
        print "ERROR: Image did not load."
        return 2

    detections = cascade_detect(cascade, image)
    detections_draw(image, detections, image_path)

    #print "Found {0} objects!".format(len(detections))
    #if result_path is None:
    #    cv2.imshow("Objects found", image)
    #    cv2.waitKey(0)
    #else:
    #   cv2.imwrite(result_path, image)
    return len(detections)

if __name__ == "__main__":
    main(sys.argv[1])
