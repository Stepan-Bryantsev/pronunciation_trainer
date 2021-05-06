from analysis.audio import Audio
from analysis.utils import ImageComparator


class Comparator:
    TEMP_DIRECTORY_PATH = "analysis/images/"

    @staticmethod
    def __preprocess(user_audio, reference_audio):
        user_audio.trim()
        reference_audio.trim()
        user_audio.resample_to_target(reference_audio.duration)

    @staticmethod
    def __compare_waves(user_audio, reference_audio):
        user_audio.wave_plot(Comparator.TEMP_DIRECTORY_PATH + "user_wave.png")
        reference_audio.wave_plot(Comparator.TEMP_DIRECTORY_PATH + "reference_wave.png")
        return ImageComparator.compare_images(Comparator.TEMP_DIRECTORY_PATH + "user_wave.png",
                                              Comparator.TEMP_DIRECTORY_PATH + "reference_wave.png")

    @staticmethod
    def __compare_fourier_transform(user_audio, reference_audio):
        user_audio.fourier_transform_plot(Comparator.TEMP_DIRECTORY_PATH + "user_fourier.png")
        reference_audio.fourier_transform_plot(Comparator.TEMP_DIRECTORY_PATH + "reference_fourier.png")
        return ImageComparator.compare_images(Comparator.TEMP_DIRECTORY_PATH + "user_fourier.png",
                                              Comparator.TEMP_DIRECTORY_PATH + "reference_fourier.png")

    @staticmethod
    def __compare_spectrogram(user_audio, reference_audio):
        user_audio.spectrogram(Comparator.TEMP_DIRECTORY_PATH + "user_spectrogram.png")
        reference_audio.spectrogram(Comparator.TEMP_DIRECTORY_PATH + "reference_spectrogram.png")
        return ImageComparator.compare_images(Comparator.TEMP_DIRECTORY_PATH + "user_spectrogram.png",
                                              Comparator.TEMP_DIRECTORY_PATH + "reference_spectrogram.png")

    @staticmethod
    def __compare_mfcc(user_audio, reference_audio):
        user_audio.mfcc(Comparator.TEMP_DIRECTORY_PATH + "user_mfcc.png")
        reference_audio.mfcc(Comparator.TEMP_DIRECTORY_PATH + "reference_mfcc.png")
        return ImageComparator.compare_images(Comparator.TEMP_DIRECTORY_PATH + "user_mfcc.png",
                                              Comparator.TEMP_DIRECTORY_PATH + "reference_mfcc.png")

    @staticmethod
    def compare(user_audio_data, reference_audio_path):
        user_audio = Audio.load(user_audio_data)
        reference_audio = Audio.load(reference_audio_path)

        Comparator.__preprocess(user_audio, reference_audio)

        waves_score = Comparator.__compare_waves(user_audio, reference_audio)
        fourier_transform_score = Comparator.__compare_fourier_transform(user_audio, reference_audio)
        spectrogram_score = Comparator.__compare_spectrogram(user_audio, reference_audio)
        # mfcc_score = Comparator.__compare_mfcc(user_audio, reference_audio)
        # print(waves_score)
        # print(fourier_transform_score)
        # print(spectrogram_score)
        # print(mfcc_score)
        return (waves_score + fourier_transform_score + spectrogram_score) / 3
