import random
from analysis.comparator import Comparator


@staticmethod
def check_pronunciation(user, word, user_audio):
    score = Comparator.compare(user_audio, word.audio_path)

    user.average_score = (user.average_score * user.pronounced + score) / (user.pronounced + 1)
    user.pronounced += 1
    user.save()

    return score
