import random


def check_pronunciation(user, file):
    score = random.randint(0, 100)

    user.average_score = (user.average_score * user.pronounced + score) / (user.pronounced + 1)
    user.pronounced += 1
    user.save()

    print('here')

    return score
