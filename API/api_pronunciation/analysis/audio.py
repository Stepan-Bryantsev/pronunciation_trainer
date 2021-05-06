import librosa
import librosa.display
import matplotlib.pyplot as plt


class Audio:
    def __init__(self, audio, sample_rate):
        self.audio = audio
        self.sample_rate = sample_rate
        self.duration = librosa.get_duration(self.audio)

    def trim(self):
        self.audio = librosa.effects.trim(self.audio, top_db=20)[0]
        self.duration = librosa.get_duration(self.audio)

    def resample_to_target(self, target_duration):
        target_rate = int(self.sample_rate * target_duration / self.duration)
        self.audio = librosa.resample(self.audio, self.sample_rate, target_rate)
        self.sample_rate = target_rate
        self.duration = librosa.get_duration(self.audio)

    def wave_plot(self, path):
        figure = plt.figure(figsize=(14, 5))
        plt.axis('off')
        librosa.display.waveplot(self.audio, sr=self.sample_rate)
        figure.savefig(path, transparent=True, pad_inches=0)

    def fourier_transform_plot(self, path):
        fourier_transform = librosa.stft(self.audio)
        fourier_transform_db = librosa.amplitude_to_db(abs(fourier_transform))
        plt.figure(figsize=(20, 20))
        plt.axis("off")
        librosa.display.specshow(fourier_transform_db, sr=self.sample_rate)
        plt.savefig(path, transparent=True, pad_inches=0)

    def spectrogram(self, path):
        plt.figure(figsize=(20, 20))
        plt.specgram(self.audio, NFFT=2048, Fs=2, Fc=0, noverlap=128, cmap=plt.get_cmap('inferno'), sides='default',
                     mode='default', scale='dB')
        plt.axis('off')
        plt.savefig(path, transparent=True, pad_inches=0)

    def mfcc(self, path):
        mfcc = librosa.feature.mfcc(self.audio, sr=self.sample_rate, n_mfcc=5)
        plt.figure(figsize=(20, 20))
        plt.axis("off")
        librosa.display.specshow(mfcc, sr=self.sample_rate)
        plt.savefig(path, transparent=True, pad_inches=0)

    @staticmethod
    def load(path):
        audio, sample_rate = librosa.load(path)
        return Audio(audio, sample_rate)
