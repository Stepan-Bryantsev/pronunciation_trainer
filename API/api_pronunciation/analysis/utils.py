import cv2


class ImageComparator:
    __COMPRESSION_SIZE = 10

    @staticmethod
    def __calc_hash(path):
        image = cv2.imread(path)
        resized_image = cv2.resize(image, (ImageComparator.__COMPRESSION_SIZE, ImageComparator.__COMPRESSION_SIZE),
                                   interpolation=cv2.INTER_AREA)
        grey_scaled_image = cv2.cvtColor(resized_image, cv2.COLOR_BGR2GRAY)
        avg = grey_scaled_image.mean()
        ret, threshold_image = cv2.threshold(grey_scaled_image, avg, 255, 0)

        hash_string = ""
        for x in range(ImageComparator.__COMPRESSION_SIZE):
            for y in range(ImageComparator.__COMPRESSION_SIZE):
                hash_string += "1" if threshold_image[x, y] == 255 else "0"
        return hash_string

    @staticmethod
    def __compare_hashes(first_hash, second_hash):
        errors_count = 0
        for i in range(len(first_hash)):
            if first_hash[i] != second_hash[i]:
                errors_count += 1
        return errors_count ** 2

    @staticmethod
    def compare_images(first_path, second_path):
        first_hash = ImageComparator.__calc_hash(first_path)
        second_hash = ImageComparator.__calc_hash(second_path)
        error = ImageComparator.__compare_hashes(first_hash, second_hash) / ImageComparator.__COMPRESSION_SIZE ** 2
        return max(1 - error, 0)
